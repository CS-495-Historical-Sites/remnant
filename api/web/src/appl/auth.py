"""Routes for user authentication."""

from datetime import datetime

from flask import Blueprint, request, jsonify
from flask_jwt_extended import (
    create_access_token,
    create_refresh_token,
    get_jwt_identity,
    jwt_required,
    get_jwt,
)
from sqlalchemy.exc import DatabaseError

from src.appl import LOGGER
from src.appl.models import RegistrationRequest, LoginRequest
from src.appl.remnant_db import user_queries, token_queries
from src.appl.validation import check_valid_password, check_valid_email, check_types

auth_blueprint = Blueprint(
    "auth_blueprint",
    __name__,
)


@auth_blueprint.route("/api/register", methods=["POST", "OPTIONS"])
def register():
    LOGGER.debug("register() reached")
    if request.method == "OPTIONS":
        return "", 200

    data = request.get_json()

    try:
        email = data["email"]
        non_hash_password = data["password"]
    except KeyError:
        return (
            jsonify({"message": "Incomplete request. Email and Password required"}),
            400,
        )

    # making sure the json data types are correct before we move forward
    # these should always be string but just to be safe this will handle any weird requests
    if not check_types([(email, non_hash_password, str)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    # checking for valid credentials
    # no point in checking the database if the credentials are not valid
    if not check_valid_email(email) or not check_valid_password(non_hash_password):
        return jsonify({"message": "Invalid credentials entered"}), 422

    registration_info = RegistrationRequest(data["email"], data["password"])

    LOGGER.debug(f"Attemping to register {registration_info.email}")

    if user_queries.email_exists(registration_info.email):
        return jsonify({"message": "Email already exists"}), 422

    user_queries.create_user(registration_info)

    return jsonify({"email": registration_info.email, "errorString": ""}), 200


@auth_blueprint.route("/api/login", methods=["POST", "OPTIONS"])
def login():
    LOGGER.debug("login() reached")
    if request.method == "OPTIONS":
        return "", 200

    data = request.get_json()

    try:
        email = data["email"]
        non_hash_password = data["password"]
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400

    if not check_types([(email, non_hash_password, str)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    if not check_valid_email(email) or not check_valid_password(non_hash_password):
        return jsonify({"message": "Invalid credentials entered"}), 422

    login_info = LoginRequest(data["email"], data["password"])

    LOGGER.debug(f"Attemping to login {login_info.email}")

    user = user_queries.get_user(login_info.email)
    if not user or not user.password_matches_hash(login_info.password):
        return (
            jsonify({"message": "Invalid email or password", "accessToken": ""}),
            422,
        )

    access_token = create_access_token(identity=user.email)
    refresh_token = create_refresh_token(identity=user.email)
    return jsonify(access_token=access_token, refresh_token=refresh_token), 200


@auth_blueprint.route("/api/refresh", methods=["POST"])
@jwt_required(refresh=True)
def refresh():
    identity = get_jwt_identity()
    access_token = create_access_token(identity=identity)
    return jsonify(access_token=access_token), 200


@auth_blueprint.route("/api/logout", methods=["DELETE", "OPTIONS"])
@jwt_required(verify_type=False)
def logout():
    try:
        user_identity = get_jwt_identity()
        user = user_queries.get_user(user_identity)
        if not user:
            return jsonify({"message": "Token not found or invalid"}), 404
        unique_token = get_jwt()["jti"]
        token_type = get_jwt()["type"]
        logout_date = datetime.utcnow()
        token_queries.blacklist_token(unique_token, logout_date, token_type, user.id)
    except KeyError:
        return jsonify({"message": "Token not found or invalid"}), 401
    except DatabaseError:
        return jsonify({"message": "Database error"}), 500

    return jsonify({"message": "Logged out successfully"}), 200
