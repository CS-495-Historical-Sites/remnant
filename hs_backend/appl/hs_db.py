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


def create_location(
    loc_name: str, lat: int, long: int, desc: str, suspend_commit=False
) -> None:
    loc = Location(name=loc_name, latitude=lat, longitude=long, description=desc)
    db.session.add(loc)
    if not suspend_commit:
        db.session.commit()


def commit():
    db.session.commit()


def get_all_locations() -> list[Location]:
    return Location.query.all()
