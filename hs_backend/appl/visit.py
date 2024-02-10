from flask import jsonify, Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required
from . import hs_db
from .models import Visit, Location, User

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
        locations_json = [{'name': name} for name in visited_locations]
        return jsonify({'visited_locations': locations_json}), 200
    else:
        return jsonify({"message": "No visited locations found for the user"}), 404

@visit_blueprint.route("/api/user/visited_locations", methods=["DELETE"])
@jwt_required()
def delete_visited_location():
    user_identity = get_jwt_identity()
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400
    
    # name should be name of location to be deleted
    # may need to make this less ambiguous

    data = request.get_json()
    location_key = data.get('id')
    location_to_delete = Visit.query.filter_by(location_id=location_key, user_id=user.id).first()
    if location_to_delete:
        hs_db.delete_visited_location(location_to_delete, user.id)
        return jsonify({"message": "Removed Location"}), 200
    else:
        return jsonify({"message": "Location not found"}), 404


@visit_blueprint.route("/api/user/visited_locations", methods=["POST"])
@jwt_required()
def add_visited_location():
    user_identity = get_jwt_identity()
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400
    
    data = request.get_json()
    location_name = data.get('name')

    location_to_add = Location.query.filter_by(name = location_name).first()

    if location_to_add:
        hs_db.create_visited_location(location_to_add.id, user.id)
        return jsonify({"message": "Location Successfully Added"}), 200
    else:
        return jsonify({"message": "Location not found"}), 404
        



