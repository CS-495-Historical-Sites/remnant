import pytest

from hs_backend.appl import init_app
from tests import DB_URI
from hs_backend.appl import hs_db
from hs_backend.appl.models import RegistrationRequest, User
from tests.atomic import TestIsolator


@pytest.fixture()
def app():
    flask_app = init_app(testing=True, db_uri=DB_URI)

    flask_app.config.update({"TESTING": True, "SQLALCHEMY_DATABASE_URI": DB_URI})

    with flask_app.app_context():
        yield flask_app


class TestUser(TestIsolator):
    def test_user_construction_hashes_password(self, app):
        self.setup_app()

        """ TESTING ADDING A USER TO OUR DATABASE WITH OUR CREATE_USER FUNCTION"""

        test_user = RegistrationRequest("DamonArnette34@gmail.com", "IloveSQL#667")
        hs_db.create_user(test_user)
        test_query = User.query.filter_by(email=test_user.email).first()
        assert test_query.email == "DamonArnette34@gmail.com"
        assert test_query.password_hash != "IloveSQL#667"

        self.teardown_app()
