from flask import jsonify, Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from src.appl import LOGGER
from src.appl.models import Visit, Location
from src.appl.remnant_db import location_queries, user_queries, visit_queries
from src.appl.validation import check_types

visit_blueprint = Blueprint(
    "visit_blueprint",
    __name__,
)


@visit_blueprint.route("/api/user/visited_locations", methods=["GET"])
@jwt_required()
def get_visited_locations():
    user_identity = get_jwt_identity()
    user = user_queries.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    visited_locations: list = visit_queries.get_visited_location(user.id)

    location_data = [
        {"id": location_id, "name": name} for location_id, name in visited_locations
    ]
    return jsonify({"visited_locations": location_data}), 200


@visit_blueprint.route("/api/user/visited_locations", methods=["DELETE"])
@jwt_required()
def delete_visited_location():
    user_identity = get_jwt_identity()
    user = user_queries.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    data = request.get_json()

    try:
        location_key = data["id"]
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400

    if not check_types([(location_key, int)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    location_to_add = Location.query.filter_by(id=location_key).first()
    if not location_to_add:
        return jsonify({"message": "Location not found"}), 404

    visit_to_delete = Visit.query.filter_by(
        location_id=location_key, user_id=user.id
    ).first()

    if visit_to_delete:
        visit_queries.delete_visited_location(visit_to_delete)
        return jsonify({"message": "Removed Visit"}), 200

    return jsonify({"message": "Visit not found"}), 404


@visit_blueprint.route("/api/user/visited_locations", methods=["POST"])
@jwt_required()
def add_visited_location():
    user_identity = get_jwt_identity()
    user = user_queries.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    data = request.get_json()

    try:
        location_key = data["id"]
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400

    if not check_types([(location_key, int)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    location_to_add = location_queries.get_location(location_id=location_key)
    if not location_to_add:
        LOGGER.warning("location not found %s", location_key)
        return jsonify({"message": "Location not found"}), 404

    check_duplicate = Visit.query.filter_by(
        user_id=user.id, location_id=location_to_add.id
    ).first()
    if check_duplicate:
        return jsonify({"message": "Visit Successfully Added"}), 200

    visit_queries.create_visited_location(location_to_add.id, user.id)
    return jsonify({"message": "Visit Successfully Added"}), 200
