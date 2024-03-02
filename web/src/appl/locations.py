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

    if any([latitude, longitude]) and not all([latitude, longitude]):
        return jsonify({"message": "Must give both lat and long or neither"}), 400

    if latitude and longitude:
        latitude = float(latitude)
        longitude = float(longitude)
        near_locations = location_queries.get_locations_near(latitude, longitude)
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
