import pytest

from hs_backend.appl import init_app, db
from hs_backend.appl import hs_db
from tests import DB_URI

from hs_backend.appl.models import Location, User, RegistrationRequest
from tests.atomic import TestIsolator


@pytest.fixture()
def app():
    flask_app = init_app(testing=True, db_uri=DB_URI)

    flask_app.config.update({"TESTING": True, "SQLALCHEMY_DATABASE_URI": DB_URI})

    with flask_app.app_context():
        yield flask_app


@pytest.fixture
def valid_user() -> User:
    user_email = "DamonArnette34@gmail.com"
    user_unhashed_password = "IloveSQL#667"
    return User(user_email, user_unhashed_password)


class TestUser(TestIsolator):
    def test_can_add_and_retrieve_user_from_db(self, app):
        self.setup_app()
        user_email = "DamonArnette34@gmail.com"
        user_unhashed_password = "IloveSQL#667"

        test_info = RegistrationRequest(user_email, user_unhashed_password)
        hs_db.create_user(test_info)

        fetched_user: User | None = User.query.filter_by(email=user_email).first()

        assert fetched_user is not None
        assert fetched_user.email == user_email
        assert fetched_user.password_hash != user_unhashed_password

        self.teardown_app()

    def test_email_exists(self, app, valid_user: User):
        self.setup_app()

        db.session.add(valid_user)
        db.session.commit()

        assert hs_db.email_exists(valid_user.email)
        assert not hs_db.email_exists("Totally not an email")

        self.teardown_app()


class TestLocation(TestIsolator):
    def test_can_add_and_retrieve_location_from_db(self, app):
        self.setup_app()
        loc_name = "Tuscaloosa"
        lat = 68.004
        long = 87.0003
        image = "pictureoftuscaloosa.png"
        short = " i love tuscaloosa"
        long_description = "lsdfkaslkjflasjlfkdjsaldfjlsajfdjsaldfjlsajdfljsaldfjlasjdflkasjdlfjaslfjalsjflsajdfjasldfjalsjdflaskjdflasjdflasjldfjasldjfklasjdfljas"
        hs_db.create_location(loc_name, lat, long, short, long_description, image)

        fetched_location = Location.query.filter_by(name=loc_name).first()

        assert fetched_location is not None
        assert fetched_location.name == loc_name
        assert fetched_location.latitude == lat
        assert fetched_location.longitude == long
        assert fetched_location.short_description == short
        assert fetched_location.long_description == long_description
        assert fetched_location.wikidata_image_name == image

        self.teardown_app()
