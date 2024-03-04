from src.appl import db
from src.appl.models import User, RegistrationRequest


def email_exists(email: str) -> bool:
    return User.query.filter_by(email=email).first() is not None


def create_user(registration_info: RegistrationRequest) -> None:
    user = User(
        email=registration_info.email, supplied_password=registration_info.password
    )
    db.session.add(user)
    db.session.commit()


def get_user(email: str) -> User | None:
    return User.query.filter_by(email=email).first()
