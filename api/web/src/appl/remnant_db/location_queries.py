from math import cos, pi

from src.appl import db
from src.appl.models import Location, Visit
from sqlalchemy.sql import exists, and_, select
from src.appl import LOGGER



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


def get_nearby_location_data(
    lat: float, long: float, kilometer_radius: float, user_id: int
) -> list[Location]:
    # 1 degree of latitude in kilometers, ~111km
    delta_lat = kilometer_radius / 111

    # Adjust delta_long for the latitude
    delta_long = kilometer_radius / (111 * abs(cos(lat * (pi / 180))))

    min_lat = lat - delta_lat
    max_lat = lat + delta_lat

    min_long = long - delta_long
    max_long = long + delta_long

    


    liked_subquery = exists().where(
            and_(
                Visit.user_id == user_id,
                Visit.location_id == Location.id
                )
            ).label('is_liked')

    
    locations = db.session.query(
        Location,  
        liked_subquery  
    ).filter(
        Location.latitude.between(min_lat, max_lat),
        Location.longitude.between(min_long, max_long)
    ).all()

    return locations
