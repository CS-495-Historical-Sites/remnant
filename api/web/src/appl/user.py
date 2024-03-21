from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required

from src.appl import LOGGER
from src.appl.auth import user_required
from src.appl.models import User
user_blueprint = Blueprint(
    "user_blueprint",
    __name__,
)

@user_blueprint.route("/api/user/privateinfo", methods=["GET"])
@jwt_required()
@user_required
def get_profile_info(user: User):
    user_info: dict = {"email": user.email, "username": user.username}
    return jsonify(user_info), 200

@user_blueprint.route("/api/user/privateinfo", methods=["PATCH"])
@jwt_required()
@user_required
def update_username(user: User)