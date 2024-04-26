from dataclasses import dataclass
from datetime import datetime
from typing import TypedDict

from sqlalchemy import MetaData, UniqueConstraint
from sqlalchemy.dialects.postgresql import JSONB
from werkzeug.security import generate_password_hash, check_password_hash

from src.appl import db


@dataclass
class RegistrationRequest:
    username: str
    email: str
    password: str
    requesting_admin: bool = False


@dataclass
class LoginRequest:
    email: str
    password: str


# The image comes in a seperate part of the request
@dataclass
class LocationAddSuggestionRequest:
    latitude: str
    longitude: str
    name: str
    short_description: str
    wikipedia_link: str | None


@dataclass
class LocationEditSuggestionRequest:
    location_id: int
    name: str
    short_description: str
    long_description: str


class ShortLocationDescription(TypedDict):
    id: str
    name: str
    latitude: float
    longitude: float
    short_description: str
    is_liked: bool


class LongLocationDescription(TypedDict):
    id: str
    name: str
    latitude: float
    longitude: float
    short_description: str
    long_description: str
    wikidata_image_name: str


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
    version = db.Column(db.Integer, default=1, nullable=False)
    image_link = db.Column(db.Text, nullable=False)
    categories = db.Column(db.ARRAY(db.String), nullable=True)

    # pylint: disable=too-many-arguments
    def __init__(
        self,
        name,
        latitude,
        longitude,
        image_link,
        categories: list[str],
        short_description=None,
        long_description=None,
    ):
        self.name = name
        self.latitude = latitude
        self.longitude = longitude
        self.categories = categories
        self.short_description = short_description
        self.long_description = long_description
        self.image_link = image_link

    def apply_edit_suggestion(self, edit: LocationEditSuggestionRequest):
        history = LocationHistory(
            location_id=self.id,
            name=self.name,
            latitude=self.latitude,
            longitude=self.longitude,
            short_description=self.short_description,
            long_description=self.long_description,
            image_link=self.image_link,
            version=self.version,
            categories=self.categories,
        )
        db.session.add(history)

        self.name = edit.name
        self.short_description = edit.short_description
        self.long_description = edit.long_description
        self.version += 1


class Visit(db.Model):
    __tablename__ = "user_visited_locations"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(
        db.Integer, db.ForeignKey("user.id"), nullable=False, index=True
    )
    location_id = db.Column(
        db.Integer, db.ForeignKey("location.id"), nullable=False, index=True
    )
    visit_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)

    # to prevent duplicate rows no row can share the same user_id and location_id
    __table_args__ = (UniqueConstraint("user_id", "location_id"),)

    def __init__(self, user_id, location_id, visit_time=None):
        self.user_id = user_id
        self.location_id = location_id
        self.visit_time = visit_time


class LoginAttempt(db.Model):
    __tablename__ = "login_attempt"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(120), index=True, nullable=False)
    attempt_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)
    success = db.Column(db.Boolean, nullable=False)
    lockout = db.Column(db.Integer, default=0, nullable=False)

    def __init__(self, email, success, lockout):
        self.email = email
        self.success = success
        self.lockout = lockout


class User(db.Model):
    __tablename__ = "user"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)

    username = db.Column(db.String(120), index=False, unique=False)
    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(256), nullable=False)
    answers = db.Column(JSONB, nullable=True)
    email_confirmation_token = db.Column(db.String(256), nullable=False)
    lockout = db.Column(db.Integer, default=0, nullable=False)

    is_admin = db.Column(db.Boolean, default=False)
    has_confirmed_email = db.Column(db.Boolean, default=False)

    def __init__(
        self,
        username: str,
        email: str,
        supplied_password: str,
        confirmation_token: str,
        answers=None,
        is_admin=False,
    ):
        self.username = username
        self.email = email
        self.password_hash = generate_password_hash(supplied_password)
        self.answers = answers
        self.email_confirmation_token = confirmation_token
        self.is_admin = is_admin

    def password_matches_hash(self, password: str) -> bool:
        return check_password_hash(self.password_hash, password)


class SuggestionApproval(db.Model):
    __tablename__ = "suggestion_approval"
    metadata = program_metadata

    id = db.Column(db.Integer, primary_key=True)
    suggestion_type = db.Column(db.String(50), nullable=False)
    suggestion_id = db.Column(db.Integer, nullable=False)
    admin_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    status = db.Column(db.String(50), nullable=False)
    comments = db.Column(db.Text, nullable=True)
    decision_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=True)

    def __init__(self, suggestion_type, suggestion_id, admin_id, status, comments=None):
        self.suggestion_type = suggestion_type
        self.suggestion_id = suggestion_id
        self.admin_id = admin_id
        self.status = status
        self.comments = comments


class LocationAddSuggestion(db.Model):
    __tablename__ = "user_suggested_locations"
    metadata = program_metadata

    # base info
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)

    suggestion_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)

    # location suggestion info
    latitude = db.Column(db.Float, nullable=False)
    longitude = db.Column(db.Float, nullable=False)
    name = db.Column(db.String(120), index=True, unique=False, nullable=False)
    short_description = db.Column(db.Text, nullable=False)
    wikipedia_link = db.Column(db.Text, nullable=True)
    image_url = db.Column(db.Text, nullable=True)

    def __init__(self, user: User, req: LocationAddSuggestionRequest, image_url: str):
        self.user_id = user.id
        self.latitude = req.latitude
        self.longitude = req.longitude
        self.name = req.name
        self.short_description = req.short_description
        self.wikipedia_link = req.wikipedia_link
        self.image_url = image_url


class LocationEditSuggestion(db.Model):
    __tablename__ = "user_suggested_location_edit"
    metadata = program_metadata

    # base info
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    location_id = db.Column(db.Integer, db.ForeignKey("location.id"), nullable=False)
    suggestion_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)

    name = db.Column(db.String(120), index=True, unique=False, nullable=False)
    short_description = db.Column(db.Text, nullable=True)
    long_description = db.Column(db.Text, nullable=True)

    def __init__(self, user: User, req: LocationEditSuggestionRequest):
        self.user_id = user.id
        self.location_id = req.location_id
        self.name = req.name
        self.short_description = req.short_description
        self.long_description = req.long_description


class BlacklistToken(db.Model):
    __tablename__ = "blacklist_token"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)
    token_id = db.Column(db.String(36), nullable=False, index=True)
    token_type = db.Column(db.String(16), nullable=False)
    logout_time = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)

    def __init__(self, token_id, logout_time, token_type, user_id):
        self.token_id = token_id
        self.logout_time = logout_time
        self.token_type = token_type
        self.user_id = user_id


class LocationHistory(db.Model):
    __tablename__ = "location_history"
    metadata = program_metadata
    id = db.Column(db.Integer, primary_key=True)
    location_id = db.Column(db.Integer, db.ForeignKey("location.id"), nullable=False)
    name = db.Column(db.String(120), nullable=False)
    latitude = db.Column(db.Float, nullable=False)
    longitude = db.Column(db.Float, nullable=False)
    short_description = db.Column(db.Text, nullable=True)
    long_description = db.Column(db.Text, nullable=True)
    image_link = db.Column(db.Text, nullable=True)
    version = db.Column(db.Integer, nullable=False)
    modified_at = db.Column(db.DateTime, default=datetime.utcnow, nullable=False)
    categories = db.Column(db.ARRAY(db.String), nullable=True)

    # Foreign key relationship
    location = db.relationship(
        "Location", backref=db.backref("history", lazy="dynamic")
    )
