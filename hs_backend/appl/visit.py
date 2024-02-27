from flask import jsonify, Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required
from . import hs_db
from .models import Visit, Location

visit_blueprint = Blueprint(
    "visit_blueprint",
    __name__,
)


@visit_blueprint.route("/api/user/visited_locations", methods=["GET"])
@jwt_required()
def get_visited_locations():
    user_identity = get_jwt_identity()
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    visited_locations = hs_db.get_visited_location(user.id)

    if visited_locations:
        location_data = [
            {"id": location_id, "name": name} for location_id, name in visited_locations
        ]
        return jsonify({"visited_locations": location_data}), 200

    return (
        jsonify(
            {
                "message": "No visited locations found for the user",
                "visited_locations": [],
            }
        ),
        200,
    )


@visit_blueprint.route("/api/user/visited_locations", methods=["DELETE"])
@jwt_required()
def delete_visited_location():
    user_identity = get_jwt_identity()
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    data = request.get_json()

    try:
        location_key = int(data.get("id"))
    except ValueError:
        return jsonify({"message": "Invalid location ID"}), 400

    location_to_delete = Visit.query.filter_by(
        location_id=location_key, user_id=user.id
    ).first()

    if location_to_delete:
        hs_db.delete_visited_location(location_to_delete)
        return jsonify({"message": "Removed Location"}), 200

    return jsonify({"message": "Location not found"}), 404


@visit_blueprint.route("/api/user/visited_locations", methods=["POST"])
@jwt_required()
def add_visited_location():
    user_identity = get_jwt_identity()
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    data = request.get_json()

    try:
        location_key = int(data.get("id"))
    except ValueError:
        return jsonify({"message": "Invalid location ID"}), 400

    location_to_add = Location.query.filter_by(id=location_key).first()
    if not location_to_add:
        return jsonify({"message": "Location not found"}), 404

    check_duplicate = Visit.query.filter_by(
        user_id=user.id, location_id=location_to_add.id
    ).first()
    if check_duplicate:
        return jsonify({"message": "User has already visited this location"}), 409

    if location_to_add:
        hs_db.create_visited_location(location_to_add.id, user.id)
        return jsonify({"message": "Location Successfully Added"}), 200

    return jsonify({"message": "Location not found"}), 404
