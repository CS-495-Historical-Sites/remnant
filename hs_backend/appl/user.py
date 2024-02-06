from flask import Blueprint, request, jsonify
from flask_jwt_extended import get_jwt_identity, jwt_required

from . import hs_db, LOGGER
from .models import Location, FavoriteLocationDeleteRequest

user_blueprint = Blueprint(
    "user_blueprint",
    __name__,
)


@user_blueprint.route("/api/user/favorite_locations", methods=["GET"])
@jwt_required()
def get_favorite_locations():
    user_identity = get_jwt_identity()
    LOGGER.debug("get_favorite_locations(), user identity: %s", user_identity)
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    favorite_locations: list[Location] = user.favorite_locations

    return jsonify([l.user_repr() for l in favorite_locations]), 200


@user_blueprint.route("/api/user/favorite_locations", methods=["DELETE"])
@jwt_required()
def delete_favorite_location():
    user_identity = get_jwt_identity()
    user = hs_db.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    data = request.get_json()
    if not data:
        return jsonify({"message": "No data supplied"})

    location_request = FavoriteLocationDeleteRequest(
        location_id=data.get("location_id")
    )
    user.remove_favorite_location(location_id=location_request.location_id)
    return jsonify({"message": "Removed Location"}), 200
