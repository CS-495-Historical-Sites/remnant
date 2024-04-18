from src.appl import db
from src.appl.models import Location


class TestVisitedLocations:

    def test_get_specific_locoation(self, client):
        locations = [
            Location(
                "city1",
                45.0283,
                143.2030,
                "image_link",
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
            "image_link": "image_link",
        }

    def test_get_specific_location_404(self, client):
        locations = [
            Location(
                "city1",
                45.0283,
                143.2030,
                "image_link",
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
