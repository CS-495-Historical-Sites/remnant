from dataclasses import dataclass
from datetime import datetime
from typing import TypedDict


from sqlalchemy import MetaData, UniqueConstraint
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


class ShortLocationDescription(TypedDict):
    id: str
    name: str
    latitude: float
    longitude: float
    short_description: str


class LongLocationDescription(TypedDict):
    id: str
    name: str
    latitude: float
    longitude: float
    short_description: str
    long_description: str


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

    def __init__(
        self, name, latitude, longitude, short_description=None, long_description=None
    ):
        self.name = name
        self.latitude = latitude
        self.longitude = longitude
        self.short_description = short_description
        self.long_description = long_description

    def short_repr(self) -> ShortLocationDescription:
        return {
            "id": self.id,
            "name": self.name,
            "latitude": self.latitude,
            "longitude": self.longitude,
            "short_description": self.short_description,
        }

    def long_repr(self) -> LongLocationDescription:
        return {
            "id": self.id,
            "name": self.name,
            "latitude": self.latitude,
            "longitude": self.longitude,
            "short_description": self.short_description,
            "long_description": self.long_description,
        }


class Visit(db.Model):
    __tablename__ = "user_visited_locations"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    location_id = db.Column(db.Integer, db.ForeignKey("location.id"), nullable=False)
    visit_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)

    # to prevent duplicate rows no row can share the same user_id and location_id
    __table_args__ = (UniqueConstraint("user_id", "location_id"),)

    def __init__(self, user_id, location_id, visit_time=None):
        self.user_id = user_id
        self.location_id = location_id
        self.visit_time = visit_time


class User(db.Model):
    __tablename__ = "user"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)

    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(128), nullable=False)

    def __init__(self, email: str, supplied_password: str):
        self.email = email
        self.password_hash = generate_password_hash(supplied_password)

    def password_matches_hash(self, password: str) -> bool:
        return check_password_hash(self.password_hash, password)
