from src.appl import db
from src.appl.models import Location

from src.tests import helpers


class TestVisitedLocations:

    def test_get_all_locations(self, client):
        added_locations = helpers.fill_with_locations(client)

        get_all_locations = client.get(
            "/api/locations",
        )
        assert get_all_locations.status_code == 200
        assert len(get_all_locations.json) == added_locations

    def test_get_specific_locoation(self, client):
        locations = [
            Location(
                "city1",
                45.0283,
                143.2030,
                "image_name",
                "description1",
                "longdecription0",
            ),
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
            "wikidata_image_name": "image_name",
        }

    def test_get_specific_location_404(self, client):
        locations = [
            Location(
                "city1",
                45.0283,
                143.2030,
                "image_name",
                "description1",
                "longdecription0",
            ),
        ]

        db.session.add_all(locations)
        db.session.commit()

        location_one = client.get(
            "/api/locations/-1",
        )

        assert location_one.status_code == 404
        assert location_one.json == {"message": "Location not found"}
