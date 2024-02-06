from . import db
from .models import RegistrationRequest, User, Location


def email_exists(email: str) -> bool:
    return User.query.filter_by(email=email).first() is not None


def create_user(registration_info: RegistrationRequest) -> None:
    user = User(email=registration_info.email)
    user.set_password(registration_info.password)
    db.session.add(user)
    db.session.commit()


def get_user(email: str) -> User | None:
    return User.query.filter_by(email=email).first()


def create_location(location_name: str) -> None:
    loc = Location(name=location_name)
    db.session.add(loc)
    db.session.commit()


def get_all_locations() -> list[Location]:
    return Location.query.all()
