import pytest
from hs_backend.appl import init_app
from tests import DB_URI
from tests.atomic import TestIsolator
from hs_backend.appl.models import Location
from hs_backend.appl import db


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

    def test_get_all_locations(self, client):
        self.setup_app()

        locations = [
            Location("city1", 45.0283, 143.2030, "description1", "longdecription0"),
            Location("City2", 34.0522, -118.2437, "Description2", "LongDescription2"),
            Location("City3", 41.8781, -87.6298, "Description3", "LongDescription3"),
            Location("City4", 37.7749, -122.4194, "Description4", "LongDescription4"),
            Location("City5", 51.5074, -0.1278, "Description5", "LongDescription5"),
            Location("City6", 45.4215, -75.6993, "Description6", "LongDescription6"),
            Location("City7", 35.6895, 139.6917, "Description7", "LongDescription7"),
            Location("City8", -33.8688, 151.2093, "Description8", "LongDescription8"),
            Location("City9", 19.0760, 72.8777, "Description9", "LongDescription9"),
            Location("city10", 45.1283, 123.2030, "desc10", "longdesc10"),
        ]

        db.session.add_all(locations)
        db.session.commit()

        get_all_locations = client.get(
            "/api/locations",
        )
        assert get_all_locations.status_code == 200
        assert len(get_all_locations.json) == 10
        self.teardown_app()

    def test_get_specific_locoation(self, client):
        self.setup_app()
        locations = [
            Location("city1", 45.0283, 143.2030, "description1", "longdecription0"),
        ]

        db.session.add_all(locations)
        db.session.commit()

        location_one = client.get(
            "/api/locations/1",
        )
        assert location_one.status_code == 200

        assert location_one.json == {
            "id": 1,
            "latitude": 45.0283,
            "long_description": "longdecription0",
            "longitude": 143.203,
            "name": "city1",
            "short_description": "description1",
        }

        self.teardown_app()

    def test_get_specific_location_404(self, client):
        self.setup_app()
        locations = [
            Location("city1", 45.0283, 143.2030, "description1", "longdecription0"),
        ]

        db.session.add_all(locations)
        db.session.commit()

        location_one = client.get(
            "/api/locations/-1",
        )

        assert location_one.status_code == 404
        assert location_one.json == {"message": "Location not found"}

        self.teardown_app()
