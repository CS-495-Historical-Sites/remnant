from flask import Blueprint, jsonify

from . import hs_db
from .models import Location

location_blueprint = Blueprint(
    "location_blueprint",
    __name__,
)


@location_blueprint.route("/api/locations", methods=["GET"])
def get_all_locations():
    all_locations: list[Location] = hs_db.get_all_locations()
    return jsonify([l.user_repr() for l in all_locations]), 200
