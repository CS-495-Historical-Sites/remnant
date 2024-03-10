from math import cos, pi

from src.appl import db
from src.appl.models import Location


def create_location(location: Location, suspend_commit=False) -> None:
    db.session.add(location)
    if not suspend_commit:
        db.session.commit()


def get_location(location_id: int) -> Location | None:
    return Location.query.filter_by(id=location_id).first()


def commit():
    db.session.commit()


def get_all_locations() -> list[Location]:
    return Location.query.all()


def get_locations_near(
    lat: float, long: float, kilometer_radius: float
) -> list[Location]:
    # 1 degree of latitude in kilometers, ~111km
    delta_lat = kilometer_radius / 111

    # Adjust delta_long for the latitude
    delta_long = kilometer_radius / (111 * abs(cos(lat * (pi / 180))))

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
