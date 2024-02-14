from dataclasses import dataclass
from datetime import datetime
from sqlalchemy import Table, Column, ForeignKey, Integer, MetaData, UniqueConstraint
from sqlalchemy.orm import relationship
from werkzeug.security import generate_password_hash, check_password_hash

from . import db


@dataclass
class RegistrationRequest:
    email: str
    password: str


@dataclass
class LoginRequest:
    email: str
    password: str


@dataclass
class FavoriteLocationDeleteRequest:
    location_id: int


program_metadata = MetaData()


class Location(db.Model):
    __tablename__ = "location"
    metadata = program_metadata

    id = db.Column(db.Integer, primary_key=True)

    name = db.Column(db.String(120), index=True, unique=False, nullable=False)
    latitude = db.Column(db.Float, nullable=False)
    longitude = db.Column(db.Float, nullable=False)
    short_description = db.Column(db.Text, nullable=True)
    long_description = db.Column(db.Text, nullable=True)

    def location_repr(self):
        return {
            "id": self.id,
            "name": self.name,
            "latitude": self.latitude,
            "longitude": self.longitude,
        }


user_favorite_locations = Table(
    "user_favorite_locations",
    program_metadata,
    Column("user_id", Integer, ForeignKey("user.id")),
    Column("location_id", Integer, ForeignKey("location.id")),
)


# table for every location a user gets. Identical to favorite location table
class Visit(db.Model):
    __tablename__ = "user_visited_locations"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    location_id = db.Column(db.Integer, db.ForeignKey("location.id"), nullable=False)
    visit_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)

    # to prevent duplicate rows no row can share the same user_id and location_id
    __table_args__ = (UniqueConstraint("user_id", "location_id"),)


class User(db.Model):
    __tablename__ = "user"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)

    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(128), nullable=False)

    favorite_locations = relationship(
        "Location", secondary=user_favorite_locations, backref="users"
    )

    def set_password(self, supplied_password: str):
        self.password_hash = generate_password_hash(supplied_password)

    def password_matches_hash(self, password: str) -> bool:
        return check_password_hash(self.password_hash, password)

    def add_favorite_location(self, location_id: int):
        location = Location.query.get(location_id)
        if location:
            self.favorite_locations.append(location)
            db.session.commit()

    def remove_favorite_location(self, location_id: int):
        location: Location | None = Location.query.get(location_id)
        if location:
            self.favorite_locations.remove(location)
            db.session.commit()
