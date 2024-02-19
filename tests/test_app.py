import pytest
from hs_backend.appl import init_app
from tests import DB_URI
from tests.constants import VALID_REGISTRATION_REQUEST
from tests.atomic import TestIsolator
from hs_backend.appl import init_app


@pytest.fixture()
def app():
    flask_app = init_app(testing=True, db_uri=DB_URI)

    flask_app.config.update({"TESTING": True, "SQLALCHEMY_DATABASE_URI": DB_URI})

    with flask_app.app_context():
        yield flask_app


@pytest.fixture()
def client(app):
    return app.test_client()


class TestApp(TestIsolator):
    @pytest.mark.parametrize(
        "registration_credentials, expected_status_code",
        [
            # empty data
            ({}, 400),
            # no email key supplied
            ({"password": "abc123!good"}, 400),
            # no password key supplied
            ({"email": "agoodemail@gmail.com"}, 400),
            # normal registration
            ({"email": "agoodemail@gmail.com", "password": "AGoodPassword5!"}, 200),
            # no email
            ({"email": "", "password": "AGoodPassword5!"}, 422),
            # improper length
            ({"email": "agoodemfsfsdfsail@gmail.com", "password": "sword5!"}, 422),
            # characters not allowed in our password constraints
            (
                {
                    "email": "asldkjfs333949959494@gmail.com",
                    "password": "AGoodPassword5_+_)(*&!",
                },
                422,
            ),
            # no password entered
            ({"email": "hellow%@yahoo.gov", "password": ""}, 422),
            # another success
            (
                {
                    "email": "agoodemfsfsdfsail@gmail.com",
                    "password": "swordsdfdf52323*!",
                },
                200,
            ),
        ],
    )
    def test_can_register(self, client, registration_credentials, expected_status_code):
        self.setup_app()
        response = client.post("/api/register", json=registration_credentials)
        assert response.status_code == expected_status_code
        self.teardown_app()

    @pytest.mark.parametrize(
        "login_credentials, login_expected_status_code",
        [
            # empty data
            ({}, 400),
            # no email key supplied
            ({"password": "abc123!good"}, 400),
            # no password key supplied
            ({"email": "agoodemail@gmail.com"}, 400),
            # normal login
            ({"email": "test@example.com", "password": "TestPassword123!"}, 200),
            # no email
            ({"email": "", "password": "TestPassword123!"}, 422),
            # improper password
            ({"email": "test@example.com", "password": "WrongPassword"}, 422),
            # no password
            ({"email": "test@example.com", "password": ""}, 422),
            # wrong user
            ({"email": "billyjoe@hotmail.com", "password": "wheatThinLover887*"}, 422),
        ],
    )
    def test_can_login(self, client, login_credentials, login_expected_status_code):
        self.setup_app()

        register_response = client.post(
            "/api/register",
            json={"email": "test@example.com", "password": "TestPassword123!"},
        )
        assert register_response.status_code == 200

        response = client.post("/api/login", json=login_credentials)
        assert response.status_code == login_expected_status_code

        self.teardown_app()

    def test_duplicate_registration_prevented(self, client):
        self.setup_app()

        response = client.post("/api/register", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        response = client.post("/api/register", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 422

        self.teardown_app()
