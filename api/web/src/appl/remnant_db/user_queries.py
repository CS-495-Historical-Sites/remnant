from src.appl import db
from src.appl.models import LoginAttempt, RegistrationRequest, User
from datetime import datetime, timedelta

from src.appl.generators import generate_email_confirmation_token


def email_exists(email: str) -> bool:
    return User.query.filter_by(email=email).first() is not None


def create_user(registration_info: RegistrationRequest) -> User:
    email_confirmation_token = generate_email_confirmation_token()
    user = User(
        username=registration_info.username,
        email=registration_info.email,
        supplied_password=registration_info.password,
        confirmation_token=email_confirmation_token,
        is_admin=False,
    )
    db.session.add(user)
    db.session.commit()

    return user


def create_admin(registration_info: RegistrationRequest) -> None:
    email_confirmation_token = generate_email_confirmation_token()
    user = User(
        username=registration_info.username,
        email=registration_info.email,
        supplied_password=registration_info.password,
        confirmation_token=email_confirmation_token,
        is_admin=True,
    )
    db.session.add(user)
    db.session.commit()


def get_user(email: str) -> User | None:
    return User.query.filter_by(email=email).first()


def get_user_by_confirmation_token(token: str) -> User | None:
    return User.query.filter_by(confirmation_token=token).first()


def get_admin(email: str) -> User | None:
    return User.query.filter_by(email=email, is_admin=True).first()


def log_login_attempt(email: str, success: bool):
    db.session.add(LoginAttempt(email=email, success=success, attempt_time=datetime.utcnow()))
    db.session.commit()


def successful_login_attempts(email: str):
    return LoginAttempt.query.filter_by(email=email, success=True).count()


def unsuccesful_login_attempts(email: str, mins: int):
    return LoginAttempt.query.filter(LoginAttempt.email==email, LoginAttempt.success==False, LoginAttempt.attempt_time > datetime.utcnow() - timedelta(minutes=mins)).count()
