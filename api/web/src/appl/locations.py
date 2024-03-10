from flask import Blueprint, jsonify, request

from src.appl.remnant_db import location_queries

location_blueprint = Blueprint(
    "location_blueprint",
    __name__,
)


@location_blueprint.route("/api/locations", methods=["GET"])
def get_all_locations():
    latitude = request.args.get("lat")
    longitude = request.args.get("long")
    kilometer_radius = request.args.get("kilometer_radius")

    if any([latitude, longitude, kilometer_radius]) and not all(
        [latitude, longitude, kilometer_radius]
    ):
        return (
            jsonify(
                {"message": "Must give all of (lat, long, kilometer_radius) or none"}
            ),
            400,
        )

    if latitude and longitude and kilometer_radius:
        latitude = float(latitude)
        longitude = float(longitude)
        kilometer_radius = float(kilometer_radius)
        near_locations = location_queries.get_locations_near(
            latitude, longitude, kilometer_radius
        )
        return jsonify([l.short_repr() for l in near_locations]), 200

    all_locations = location_queries.get_all_locations()
    return jsonify([l.short_repr() for l in all_locations]), 200


@location_blueprint.route("/api/locations/<location_id>", methods=["GET"])
def get_location_info(location_id):
    try:
        location_key = int(location_id)
    except ValueError:
        return jsonify({"message": "Invalid location ID"}), 400

    location = location_queries.get_location(location_key)
    if not location:
        return jsonify({"message": "Location not found"}), 404

    return jsonify(location.long_repr()), 200
