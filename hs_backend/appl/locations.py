from flask import Blueprint, jsonify, request

from . import hs_db
from .models import Location

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
        near_locations = hs_db.get_locations_near(latitude, longitude)
        print(len(near_locations))
        return jsonify([l.location_repr() for l in near_locations]), 200

    all_locations = hs_db.get_all_locations()
    return jsonify([l.location_repr() for l in all_locations]), 200
