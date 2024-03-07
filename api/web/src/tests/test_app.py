import pytest


from src.tests.constants import VALID_REGISTRATION_REQUEST


class TestApp:
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
            # characters allowed in our password constraints
            (
                {
                    "email": "asldkjfs333949959494@gmail.com",
                    "password": "AGoodPassword5_+_)(*&!",
                },
                200,
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

        response = client.post("/api/register", json=registration_credentials)
        assert response.status_code == expected_status_code

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
        register_response = client.post(
            "/api/register",
            json={"email": "test@example.com", "password": "TestPassword123!"},
        )
        assert register_response.status_code == 200

        response = client.post("/api/login", json=login_credentials)
        assert response.status_code == login_expected_status_code

    def test_duplicate_registration_prevented(self, client):
        response = client.post("/api/register", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        response = client.post("/api/register", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 422

    def test_is_first_login_attempt_returns_true(self, client):
        response = client.post("/api/register", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        response = client.post("/api/login", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        assert response.json["first_login"] is True

    def test_is_first_login_attempt_returns_false_on_second_login(self, client):
        response = client.post("/api/register", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        response = client.post("/api/login", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        assert response.json["first_login"] is True

        response = client.post("/api/login", json=VALID_REGISTRATION_REQUEST)
        assert response.status_code == 200

        assert response.json["first_login"] is False

    def test_logout(self, client):
        login_credentials = {
            "email": "workingemail8@gmail.com",
            "password": "Working65**",
        }

        response = client.post("/api/register", json=login_credentials)
        assert response.status_code == 200

        response = client.post("/api/login", json=login_credentials)
        assert response.status_code == 200

        j_token = response.json.get("access_token")
        headers = {"Authorization": f"Bearer {j_token}"}

        response = client.delete("/api/logout", headers=headers)
        assert response.status_code == 200

        response = client.post(
            "/api/user/visited_locations",
            json={"id": 2},
            headers=headers,
        )

        assert response.status_code == 401

        response = client.delete("/api/logout", headers=headers)
        assert response.status_code == 401
