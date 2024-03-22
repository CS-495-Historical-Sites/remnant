from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required

from src.appl import LOGGER, db
from src.appl.auth import user_required
from src.appl.models import User
from src.appl.validation import check_valid_username
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
def update_profile_info(user: User):
    data = request.get_json()
    if "username" in data:
        new_username = data["username"]
        if not check_valid_username(new_username):
            return jsonify({"message": "Invalid username entered"}), 422 
        user.username = new_username
    db.session.commit()    
    user_info: dict = {"email": user.email, "username": user.username}
    return jsonify(user_info), 200

#figure out how to input eras