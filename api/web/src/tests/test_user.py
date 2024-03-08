# pylint: disable=unused-argument

from src.appl.models import RegistrationRequest, User
from src.appl.remnant_db import user_queries


class TestUser:
    # TESTING ADDING A USER TO OUR DATABASE WITH OUR CREATE_USER FUNCTION
    def test_user_construction_hashes_password(self, app):
        test_user = RegistrationRequest(
            "damonarnette", "DamonArnette34@gmail.com", "IloveSQL#667"
        )
        user_queries.create_user(test_user)
        test_query = User.query.filter_by(email=test_user.email).first()
        assert test_query.username == "damonarnette"
        assert test_query.email == "DamonArnette34@gmail.com"
        assert test_query.password_hash != "IloveSQL#667"
