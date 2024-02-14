from . import db
from .models import RegistrationRequest, User, Location, Visit
from datetime import datetime
from . import LOGGER


# user related commands
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
    loc_name: str,
    lat: int,
    long: int,
    short_desc: str,
    long_desc: str,
    suspend_commit=False,
) -> None:
    loc = Location(
        name=loc_name,
        latitude=lat,
        longitude=long,
        short_description=short_desc,
        long_description=long_desc,
    )
    db.session.add(loc)
    if not suspend_commit:
        db.session.commit()


def get_location(name: str) -> Location | None:
    return Location.query.filter_by(name=name).first()


def commit():
    db.session.commit()


def get_all_locations() -> list[Location]:
    return Location.query.all()


def get_locations_near(lat: float, long: float) -> list[Location]:
    delta_lat = 3
    delta_long = 3

    min_lat = lat - delta_lat
    max_lat = lat + delta_lat

    min_long = long - delta_long
    max_long = long + delta_long

    nearby_locations = Location.query.filter(
        (Location.latitude >= min_lat),
        (Location.latitude <= max_lat),
        (Location.longitude >= min_long),
        (Location.longitude <= max_long),
    ).all()

    return nearby_locations


def create_visited_location(location_num: int, user_num: int):
    curr_time = datetime.utcnow()
    visit = Visit(location_id=location_num, user_id=user_num, visit_time=curr_time)
    db.session.add(visit)
    db.session.commit()


def delete_visited_location(location_to_delete: Visit):
    if location_to_delete:
        db.session.delete(location_to_delete)
        db.session.commit()
        return True
    else:
        LOGGER.warning("Attempting to delete a non-existent visited location")
        return False


def get_visited_location(user_id: int) -> list:
    location_rows = (
        db.session.query(Location.id, Location.name)
        .join(Visit, Location.id == Visit.location_id)
        .filter(Visit.user_id == user_id)
        .all()
    )

    return location_rows
