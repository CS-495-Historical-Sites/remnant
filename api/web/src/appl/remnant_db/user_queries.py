from src.appl import db
from src.appl.models import LoginAttempt, RegistrationRequest, User


def email_exists(email: str) -> bool:
    return User.query.filter_by(email=email).first() is not None


def create_user(registration_info: RegistrationRequest) -> None:
    user = User(
        username=registration_info.username,
        email=registration_info.email,
        supplied_password=registration_info.password,
        interested_eras=registration_info.interested_eras
    )
    db.session.add(user)
    db.session.commit()


def create_admin(registration_info: RegistrationRequest) -> None:
    user = User(
        username=registration_info.username,
        email=registration_info.email,
        supplied_password=registration_info.password,
        interested_eras=registration_info.interested_eras,
        is_admin=True,
    )
    db.session.add(user)
    db.session.commit()


def get_user(email: str) -> User | None:
    return User.query.filter_by(email=email).first()


def get_admin(email: str) -> User | None:
    return User.query.filter_by(email=email, is_admin=True).first()


def log_login_attempt(email: str, success: bool):
    db.session.add(LoginAttempt(email=email, success=success))
    db.session.commit()


def successful_login_attempts(email: str):
    return LoginAttempt.query.filter_by(email=email).count()
