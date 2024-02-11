"""Routes for user authentication."""

from flask import Blueprint, request, jsonify
from flask_jwt_extended import (
    create_access_token,
    create_refresh_token,
    get_jwt_identity,
    jwt_required,
)

from . import hs_db, LOGGER
from .models import RegistrationRequest, LoginRequest


auth_blueprint = Blueprint(
    "auth_blueprint",
    __name__,
)


NEW_YORK_LOCATION_ID = 1


@auth_blueprint.route("/api/register", methods=["POST", "OPTIONS"])
def register():
    LOGGER.debug("register() reached")
    if request.method == "OPTIONS":
        return "", 200

    data = request.get_json()
    if not data:
        return jsonify({"message": "No data supplied"})

    if "email" not in data or "password" not in data:
        LOGGER.debug(f"Recieved incomplete request... ")
        return jsonify({"message": "Incomplete request"})

    registration_info = RegistrationRequest(data["email"], data["password"])

    LOGGER.debug(f"Attemping to register {registration_info.email}")

    if hs_db.email_exists(registration_info.email):
        LOGGER.debug(f"Email {registration_info.email} already exists")
        return jsonify({"message": "Email already exists"}), 422

    try:
        hs_db.create_user(registration_info)
    except Exception as e:
        LOGGER.error("error: {}".format(e))
        return jsonify({"message": "Unknown error"}), 500

    user = hs_db.get_user(email=registration_info.email)

    user.add_favorite_location(NEW_YORK_LOCATION_ID)

    return jsonify({"email": registration_info.email, "errorString": ""}), 200


@auth_blueprint.route("/api/login", methods=["POST", "OPTIONS"])
def login():
    LOGGER.debug("login() reached")
    if request.method == "OPTIONS":
        return "", 200

    data = request.get_json()
    if not data:
        return jsonify({"message": "No data supplied"})

    if "email" not in data or "password" not in data:
        LOGGER.debug(f"Recieved incomplete request... ")
        return jsonify({"message": "Incomplete request"})

    login_info = LoginRequest(data["email"], data["password"])

    LOGGER.debug(f"Attemping to login {login_info.email}")

    user = hs_db.get_user(login_info.email)
    if not user or not user.password_matches_hash(login_info.password):
        LOGGER.debug("Invalid email or password")
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
