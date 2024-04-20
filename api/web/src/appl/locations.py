from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required, get_jwt_identity
from src.appl import LOGGER
from src.appl.remnant_db import location_queries
from src.appl.responses import short_location_repr, long_location_repr
from src.appl.auth import user_required
from src.appl.models import User

location_blueprint = Blueprint(
    "location_blueprint",
    __name__,
)


@location_blueprint.route("/api/locations", methods=["GET"])
@jwt_required()
@user_required
def get_all_locations(user: User):
    user_info = user.id
    latitude = request.args.get("lat")
    longitude = request.args.get("long")
    kilometer_radius = request.args.get("kilometer_radius")

    if not all([latitude, longitude, kilometer_radius]):
        return (
            jsonify({"message": "Must give all of (lat, long, kilometer_radius)"}),
            400,
        )

    try:
        latitude = float(latitude)
        longitude = float(longitude)
        kilometer_radius = float(kilometer_radius)
    except ValueError:
        return (
            jsonify(
                {"message": "lat, long, and kilometer_radius must be float values"}
            ),
            400,
        )

    near_locations_tuple = location_queries.get_nearby_location_data(
        latitude, longitude, kilometer_radius, user_info
    )
    # near_locations_data creates a tuple like <Location 3445, False>
    # the tuple contains a location row from the Location table and a boolean value to determine if it has been liked or not
    LOGGER.debug(len(near_locations_tuple))
    return jsonify([short_location_repr(l) for l in near_locations_tuple]), 200


@location_blueprint.route("/api/locations/<location_id>", methods=["GET"])
def get_location_info(location_id):
    try:
        location_key = int(location_id)
    except ValueError:
        return jsonify({"message": "Invalid location ID"}), 400

    location = location_queries.get_location(location_key)
    if not location:
        return jsonify({"message": "Location not found"}), 404

    return jsonify(long_location_repr(location)), 200
