import pytest
from hs_backend.appl import init_app
from tests import DB_URI
from tests.atomic import TestIsolator
from hs_backend.appl.models import User, Location, Visit, RegistrationRequest
from hs_backend.appl import db, hs_db
from flask_jwt_extended import get_jwt_identity
from hs_backend.appl.hs_db import get_user


@pytest.fixture()
def app():
    flask_app = init_app(testing=True, db_uri=DB_URI)

    flask_app.config.update({"TESTING": True, "SQLALCHEMY_DATABASE_URI": DB_URI})

    with flask_app.app_context():
        yield flask_app


@pytest.fixture()
def client(app):
    return app.test_client()


class TestVisitedLocations(TestIsolator):
    @pytest.mark.parametrize(
        "location_data, expected_status",
        [
            ({"id": 0}, 404),
            ({"id": 1}, 200),
            ({"id": 2}, 200),
            ({"id": 3}, 200),
            ({"id": 4}, 200),
            ({"id": 5}, 200),
            ({"id": 6}, 200),
            ({"id": 7}, 200),
            ({"id": 8}, 200),
            ({"id": 9}, 200),
            ({"id": 10}, 404),
        ],
    )
    # MAKING SURE WE CAN ADD LOCATIONS TO VISIT TABLE AND RETURN PROPER ERRORS WHEN LOCATION ID DOESN'T EXIST
    def test_adding_locations(self, client, location_data, expected_status):
        self.setup_app()
        locations = [
            Location(
                "city1", 45.0283, 143.2030, "description1", "longdestdecriptionever"
            ),
            Location("City2", 34.0522, -118.2437, "Description2", "LongDescription2"),
            Location("City3", 41.8781, -87.6298, "Description3", "LongDescription3"),
            Location("City4", 37.7749, -122.4194, "Description4", "LongDescription4"),
            Location("City5", 51.5074, -0.1278, "Description5", "LongDescription5"),
            Location("City6", 45.4215, -75.6993, "Description6", "LongDescription6"),
            Location("City7", 35.6895, 139.6917, "Description7", "LongDescription7"),
            Location("City8", -33.8688, 151.2093, "Description8", "LongDescription8"),
            Location("City9", 19.0760, 72.8777, "Description9", "LongDescription9"),
        ]

        db.session.add_all(locations)
        db.session.commit()

        user_data = {"email": "testuser@example.com", "password": "Astrongpassword43*"}

        register_response = client.post("/api/register", json=user_data)
        assert register_response.status_code == 200

        login_response = client.post("/api/login", json=user_data)
        assert login_response.status_code == 200

        j_token = login_response.json.get("access_token")
        headers = {"Authorization": f"Bearer {j_token}"}

        response = client.post(
            "/api/user/visited_locations",
            json=location_data,
            headers=headers,
            content_type="application/json",
        )
        assert response.status_code == expected_status
        if response.status_code == 200:
            # testing duplicate requests are handled properly

            response = client.post(
                "/api/user/visited_locations",
                json=location_data,
                headers=headers,
                content_type="application/json",
            )
            assert response.status_code == 409

            # testing the database row was created
            test_query = Visit.query.filter_by(location_id=location_data["id"]).first()
            assert test_query is not None
        self.teardown_app()

    @pytest.mark.parametrize(
        "location_data, expected_status",
        [
            ({"id": 0}, 404),
            ({"id": 1}, 200),
            ({"id": 2}, 200),
            ({"id": 3}, 200),
            ({"id": 4}, 200),
            ({"id": 5}, 200),
            ({"id": 6}, 200),
            ({"id": 7}, 200),
            ({"id": 8}, 200),
            ({"id": 9}, 200),
            ({"id": 10}, 404),
        ],
    )
    # TESTING THAT WE CAN DELETE VISITED LOCATIONS AND RETURN ERRORS WHEN LOCATION HAS NOT BEEN VISITED
    def test_deleting_locations(self, client, location_data, expected_status):
        self.setup_app()
        hs_db.create_visited_location(1, 1)
        hs_db.create_visited_location(2, 1)
        hs_db.create_visited_location(3, 1)
        hs_db.create_visited_location(4, 1)
        hs_db.create_visited_location(5, 1)
        hs_db.create_visited_location(6, 1)
        hs_db.create_visited_location(7, 1)
        hs_db.create_visited_location(8, 1)
        hs_db.create_visited_location(9, 1)

        user_data = {"email": "testuser@example.com", "password": "Astrongpassword43*"}

        register_response = client.post("/api/register", json=user_data)
        assert register_response.status_code == 200

        login_response = client.post("/api/login", json=user_data)
        assert login_response.status_code == 200

        j_token = login_response.json.get("access_token")
        headers = {"Authorization": f"Bearer {j_token}"}

        response = client.delete(
            "/api/user/visited_locations",
            json=location_data,
            headers=headers,
            content_type="application/json",
        )
        print(response.json)
        assert response.status_code == expected_status
        if response.status_code == 200:
            test_query = Visit.query.filter_by(location_id=location_data["id"]).first()
            assert test_query is None

        self.teardown_app()

    @pytest.mark.parametrize(
        "user_id, expected_status",
        [
            ({"id": 1}, 200),
        ],
    )

    # TESTING THAT WE RETURN ALL VISITED LOCATIONS
    def test_fetching_locations(self, client, user_id, expected_status):
        self.setup_app()
        locations = [
            Location(
                "city1", 45.0283, 143.2030, "description1", "longdestdecriptionever"
            ),
            Location("City2", 34.0522, -118.2437, "Description2", "LongDescription2"),
            Location("City3", 41.8781, -87.6298, "Description3", "LongDescription3"),
            Location("City4", 37.7749, -122.4194, "Description4", "LongDescription4"),
            Location("City5", 51.5074, -0.1278, "Description5", "LongDescription5"),
            Location("City6", 45.4215, -75.6993, "Description6", "LongDescription6"),
            Location("City7", 35.6895, 139.6917, "Description7", "LongDescription7"),
            Location("City8", -33.8688, 151.2093, "Description8", "LongDescription8"),
            Location("City9", 19.0760, 72.8777, "Description9", "LongDescription9"),
        ]

        db.session.add_all(locations)
        db.session.commit()

        hs_db.create_visited_location(1, 1)
        hs_db.create_visited_location(2, 1)
        hs_db.create_visited_location(3, 1)
        hs_db.create_visited_location(4, 1)
        hs_db.create_visited_location(5, 1)
        hs_db.create_visited_location(6, 1)
        hs_db.create_visited_location(7, 1)
        hs_db.create_visited_location(8, 1)
        hs_db.create_visited_location(9, 1)

        user_data = {"email": "testuser@example.com", "password": "Astrongpassword43*"}

        register_response = client.post("/api/register", json=user_data)
        assert register_response.status_code == 200

        login_response = client.post("/api/login", json=user_data)
        assert login_response.status_code == 200

        j_token = login_response.json.get("access_token")
        r_token = login_response.json.get("refresh_token")
        headers = {"Authorization": f"Bearer {j_token}"}

        response = client.get(
            "/api/user/visited_locations",
            json=user_id,
            headers=headers,
            content_type="application/json",
        )
        assert response.status_code == expected_status

        locations_retrieved = response.json.get("visited_locations")
        assert len(locations_retrieved) == len(locations)

        """TESTING REFRESH WITH LOCATIONS"""

        headers2 = {"Authorization": f"Bearer {r_token}"}

        rr = client.post("/api/refresh", headers=headers2)
        new_token = rr.json.get("access_token")
        final_header = {"Authorization": f"Bearer {new_token}"}
        refresh_response = client.get(
            "/api/user/visited_locations",
            json=user_id,
            headers=final_header,
            content_type="application/json",
        )
        assert refresh_response.status_code == expected_status

        refresh_locations = refresh_response.json.get("visited_locations")
        assert len(refresh_locations) == len(locations)

        self.teardown_app()
