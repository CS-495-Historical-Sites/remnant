from . import db
from .models import RegistrationRequest, User, Location, Visit
from datetime import datetime
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


def get_location(name: str) -> Location | None:
    return Location.query.filter_by(name=name).first()

 #location related commands
def create_location(location_name: str) -> None:
    loc = Location(name=location_name)
    db.session.add(loc)
    db.session.commit()


def get_all_locations() -> list[Location]:
    return Location.query.all()


def create_visited_location(location_num: int, user_num: int):
    curr_time = datetime.utcnow()
    visit = Visit(location_id = location_num, user_id = user_num, visit_time = curr_time)
    db.session.add(visit)
    db.session.flush()
    db.session.commit()

def delete_visited_location(location_num: int, user_num: int):
    location_to_delete = Visit.query.filter_by(location_id=location_num, user_id=user_num).first()

    if location_to_delete:
        db.session.delete(location_to_delete)
        db.session.flush()
        db.session.commit()
        return True
    else:
        print(f"No record found for visited location")
        return False




def get_visited_location(user_id: int) -> list:
    visited_locations = (
        db.session.query(Location.name)
        .join(Visit, Location.id == Visit.location_id)
        .filter(Visit.user_id == user_id)
        .all()
    )
    location_names = [location.name for location in visited_locations]

    return location_names
