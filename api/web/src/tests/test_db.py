# pylint: disable=unused-argument, line-too-long

import pytest

from src.appl import db
from src.appl.remnant_db import user_queries, location_queries
from src.appl.models import Location, User, RegistrationRequest


@pytest.fixture
def valid_user() -> User:
    username = "DamonArnette34"
    user_email = "DamonArnette34@gmail.com"
    user_unhashed_password = "IloveSQL#667"
    return User(
        username=username, email=user_email, supplied_password=user_unhashed_password
    )


class TestUser:

    def test_can_add_and_retrieve_user_from_db(self, app):
        username = "DamonArnette34"
        user_email = "DamonArnette34@gmail.com"
        user_unhashed_password = "IloveSQL#667"

        test_info = RegistrationRequest(username, user_email, user_unhashed_password)
        user_queries.create_user(test_info)

        fetched_user: User | None = User.query.filter_by(email=user_email).first()

        assert fetched_user is not None
        assert fetched_user.username == username
        assert fetched_user.email == user_email
        assert fetched_user.password_hash != user_unhashed_password

    def test_email_exists(self, app, valid_user: User):

        db.session.add(valid_user)
        db.session.commit()

        assert user_queries.email_exists(valid_user.email)
        assert not user_queries.email_exists("Totally not an email")


class TestLocation:
    def test_can_add_and_retrieve_location_from_db(self, app):

        loc_name = "Tuscaloosa"
        lat = 68.004
        long = 87.0003
        image = "link_pictureoftuscaloosa.png"
        short = " i love tuscaloosa"
        long_description = "lsdfkaslkjflasjlfkdjsaldfjlsajfdjsaldfjlsajdfljsaldfjlasjdflkasjdlfjaslfjalsjflsajdfjasldfjalsjdflaskjdflasjdflasjldfjasldjfklasjdfljas"
        location = Location(loc_name, lat, long, image, short, long_description)

        location_queries.create_location(location)

        fetched_location = Location.query.filter_by(name=loc_name).first()

        assert fetched_location is not None
        assert fetched_location.name == loc_name
        assert fetched_location.latitude == lat
        assert fetched_location.longitude == long
        assert fetched_location.short_description == short
        assert fetched_location.long_description == long_description
        assert fetched_location.image_link == image
