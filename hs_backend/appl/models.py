from dataclasses import dataclass


from . import db

from werkzeug.security import generate_password_hash, check_password_hash


@dataclass
class RegistrationRequest:
    email: str
    password: str


@dataclass
class LoginRequest:
    email: str
    password: str


class User(db.Model):
    __tablename__ = "user"

    id = db.Column(db.Integer, primary_key=True)

    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(128), nullable=False)

    def set_password(self, supplied_password: str):
        self.password_hash = generate_password_hash(supplied_password)

    def password_matches_hash(self, password: str) -> bool:
        return check_password_hash(self.password_hash, password)
