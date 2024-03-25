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
    user_info: dict = {"email": user.email, "username": user.username, "interested_eras": user.interested_eras}
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


@user_blueprint.route("/api/user/privateinfo", methods=["POST"])
@jwt_required()  
@user_required
def input_era(user: User):
    data = request.get_json()
    if "interested_eras" in data:
        user.interested_eras = data["interested_eras"]
    db.session.commit()    
    return jsonify({"message": "Era added successfully", "interested_eras": user.interested_eras}), 201
#input era works. Needs to figure out frontend