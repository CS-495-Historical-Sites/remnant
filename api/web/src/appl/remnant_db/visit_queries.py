from datetime import datetime

from src.appl import db, LOGGER
from src.appl.models import Location, Visit


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


def is_in_visited(location_id, user_id) -> bool:
    row = Visit.query.filter_by(user_id=user_id, location_id=location_id)
    return row is not None
