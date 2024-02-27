"""Routes for user authentication."""

from sqlalchemy.exc import DatabaseError
import re
from datetime import datetime
from flask import Blueprint, request, jsonify
from flask_jwt_extended import (
    create_access_token,
    create_refresh_token,
    get_jwt_identity,
    jwt_required,
    get_jwt,
)

from . import hs_db, LOGGER
from .models import RegistrationRequest, LoginRequest


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
    if not isinstance(email, str) or not isinstance(non_hash_password, str):
        return jsonify({"message": "Invalid data submitted"}), 400

    # checking for valid credentials
    # no point in checking the database if the credentials are not valid
    if not check_valid_email(email) or not check_valid_password(non_hash_password):
        return jsonify({"message": "Invalid credentials entered"}), 422

    registration_info = RegistrationRequest(data["email"], data["password"])

    LOGGER.debug(f"Attemping to register {registration_info.email}")

    if hs_db.email_exists(registration_info.email):
        return jsonify({"message": "Email already exists"}), 422

    hs_db.create_user(registration_info)

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

    if not isinstance(email, str) or not isinstance(non_hash_password, str):
        return jsonify({"message": "Invalid data submitted"}), 400

    if not check_valid_email(email) or not check_valid_password(non_hash_password):
        return jsonify({"message": "Invalid credentials entered"}), 422

    login_info = LoginRequest(data["email"], data["password"])

    LOGGER.debug(f"Attemping to login {login_info.email}")

    user = hs_db.get_user(login_info.email)
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
        user = hs_db.get_user(user_identity)
        if not user:
            return jsonify({"message": "Token not found or invalid"}), 404
        unique_token = get_jwt()["jti"]
        token_type = get_jwt()["type"]
        logout_date = datetime.utcnow()
        hs_db.blacklist_token(unique_token, logout_date, token_type, user.id)
    except KeyError:
        return jsonify({"message": "Token not found or invalid"}), 401
    except DatabaseError:
        return jsonify({"message": "Database error"}), 500

    return jsonify({"message": "Logged out successfully"}), 200


# using regex to match the pattern with three constraints
# first constraint is upper/lowercase letters, digits, or the characters ._%+- these have to be followed by an @ symbol
# second constraint is upper/lowercose letters, digits, or the characters .- followed by a .
# third constraint is upper or lower case letters between the lengths of 2 and 7 for domains.
def check_valid_email(email):
    if email == "":
        return False
    regex = r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,7}\b"
    return bool(re.fullmatch(regex, email)) and len(email) < 40


# only allows digits, upper or lowercase letters, and the special characters @!$%*?&
def check_valid_password(password):
    password_regex = re.compile(
        r"^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]+$"
    )

    return bool(re.fullmatch(password_regex, password)) and (
        (len(password)) >= 8 and (len(password) < 25)
    )
