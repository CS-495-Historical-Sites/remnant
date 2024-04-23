from datetime import datetime
from functools import wraps
from src.appl.models import LoginAttempt, User

from flask import Blueprint, request, jsonify
from flask_jwt_extended import (
    create_access_token,
    create_refresh_token,
    get_jwt_identity,
    jwt_required,
    get_jwt,
)
from sqlalchemy.exc import DatabaseError

from src.appl import LOGGER, Config, db
from src.appl.postmark import send_welcome_email
from src.appl.models import RegistrationRequest, LoginRequest
from src.appl.remnant_db import user_queries, token_queries
from src.appl.validation import (
    check_valid_password,
    check_valid_email,
    check_valid_username,
    check_types,
)


def user_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        user_identity = get_jwt_identity()
        user = user_queries.get_user(user_identity)
        if user is None:
            return jsonify({"message": "User not found"}), 401
        return f(user, *args, **kwargs)

    return decorated_function


def admin_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        user_identity = get_jwt_identity()
        user = user_queries.get_admin(user_identity)
        if user is None:
            return jsonify({"message": "User not found"}), 401
        return f(user, *args, **kwargs)

    return decorated_function


auth_blueprint = Blueprint(
    "auth_blueprint",
    __name__,
)


@auth_blueprint.route("/api/user/register", methods=["POST", "OPTIONS"])
def register():
    if request.method == "OPTIONS":
        return "", 200

    data = request.get_json()

    try:
        username = data["username"]
        email = data["email"]
        non_hash_password = data["password"]
    except KeyError:
        return (
            jsonify(
                {"message": "Incomplete request. Username, Email and Password required"}
            ),
            400,
        )

    # making sure the json data types are correct before we move forward
    # these should always be string but just to be safe this will handle any weird requests
    if not check_types([(email, non_hash_password, str)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    # checking for valid credentials
    # no point in checking the database if the credentials are not valid
    if (
        not check_valid_email(email)
        or not check_valid_password(non_hash_password)
        or not check_valid_username(username)
    ):
        return jsonify({"message": "Invalid credentials entered"}), 422

    registration_info = RegistrationRequest(
        username=username, email=email, password=non_hash_password
    )

    LOGGER.debug(f"Attempting to register {registration_info.email}")

    if user_queries.email_exists(registration_info.email):
        return jsonify({"message": "Email already exists"}), 422

    if registration_info.email in Config.ADMIN_EMAILS:
        user_queries.create_admin(registration_info)
    else:
        user = user_queries.create_user(registration_info)
        if not Config.TESTING:
            send_success = send_welcome_email(user)
            if not send_success:
                return jsonify({"message": "Failed to send email"}), 500

    return jsonify({"email": registration_info.email, "errorString": ""}), 200


@auth_blueprint.route("/api/user/login", methods=["POST", "OPTIONS"])
def login():
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

    login_info = LoginRequest(email, non_hash_password)

    LOGGER.debug(f"Attemping to login {login_info.email}")

    user = user_queries.get_user(login_info.email)
    if not user or not user.password_matches_hash(login_info.password):
        lock = LoginAttempt.query.filter_by(email=login_info.email).first()
        if lock is None:
            user_queries.log_login_attempt(
                email=login_info.email, success=False, lock=0
            )
            return (
                jsonify({"message": "Invalid email or password"}),
                422,
            )
        else:
            user_queries.log_login_attempt(
                email=login_info.email, success=False, lock=lock.lockout
            )
        if lock.lockout == 0:
            if (
                user_queries.unsuccesful_login_attempts(
                    email=login_info.email, mins=4, lock=0
                )
                > 4
            ):
                lock.lockout += 1
                db.session.commit()
                return (
                    jsonify(
                        {
                            "message": "Too many failed login attempts. Try again in 5 minutes."
                        }
                    ),
                    429,
                )
        if lock.lockout == 1:
            if (
                user_queries.unsuccesful_login_attempts(
                    email=login_info.email, mins=9, lock=1
                )
                > 0
            ):
                lock.lockout += 1
                db.session.commit()
                return (
                    jsonify(
                        {
                            "message": "Too many failed login attempts. Try again in 10 minutes."
                        }
                    ),
                    429,
                )
        if lock.lockout == 2:
            if (
                user_queries.unsuccesful_login_attempts(
                    email=login_info.email, mins=16, lock=2
                )
                > 0
            ):
                lock.lockout += 1
                db.session.commit()
                return (
                    jsonify(
                        {
                            "message": "Too many failed login attempts. Try again in 15 minutes."
                        }
                    ),
                    429,
                )
        if lock.lockout == 3:
            if (
                user_queries.unsuccesful_login_attempts(
                    email=login_info.email, mins=25, lock=3
                )
                > 0
            ):
                lock.lockout += 1
                db.session.commit()
                return (
                    jsonify(
                        {
                            "message": "Too many failed login attempts. Try again in 25 minutes."
                        }
                    ),
                    429,
                )
        if lock.lockout == 4:
            if (
                user_queries.unsuccesful_login_attempts(
                    email=login_info.email, mins=20160, lock=4
                )
                > 0
            ):
                lock.lockout += 1
                db.session.commit()
                return (
                    jsonify(
                        {
                            "message": "Too many failed login attempts. Please contact an administrator."
                        }
                    ),
                    429,
                )
        return (
            jsonify({"message": "Invalid email or password"}),
            422,
        )

    user_queries.log_login_attempt(email=login_info.email, success=True, lock=0)

    access_token = create_access_token(identity=user.email)
    refresh_token = create_refresh_token(identity=user.email)
    is_first_login = user_queries.successful_login_attempts(email=login_info.email) == 1

    return (
        jsonify(
            access_token=access_token,
            refresh_token=refresh_token,
            first_login=is_first_login,
            has_confirmed_email=user.has_confirmed_email,
        ),
        200,
    )


@auth_blueprint.route("/api/refresh", methods=["POST"])
@jwt_required(refresh=True)
def refresh():
    identity = get_jwt_identity()
    access_token = create_access_token(identity=identity)
    return jsonify(access_token=access_token), 200


@auth_blueprint.route("/api/confirmation/<string:token>", methods=["POST"])  # type: ignore
def confirm_email(token):
    user = user_queries.get_user_by_confirmation_token(token)
    if user is None:
        return jsonify({"message": "User not found"}), 400

    user.has_confirmed_email = True
    db.session.commit()
    return jsonify({"message": "Email confirmed"}), 200


@auth_blueprint.route("/api/user/logout", methods=["DELETE", "OPTIONS"])
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
