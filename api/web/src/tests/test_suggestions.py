# pylint: disable=unused-argument, line-too-long
import pytest

from src.appl.models import Visit
from src.appl.remnant_db import visit_queries

from src.tests import helpers


class TestSuggestions:
    @pytest.mark.parametrize(
        "test_data, expected_status",
        [
            # Incomplete request
            ({"latitude": 40.7128, "longitude": -74.0060, "name": "New Landmark"}, 400),
            # Invalid format of lat
            (
                {
                    "latitude": "40.7128",
                    "longitude": -74.0060,
                    "name": "New York City",
                    "short_description": "The Big Apple",
                    "wikipedia_link": "https://en.wikipedia.org/wiki/New_Landmark",
                },
                400,
            ),
            # Invalid format of long
            (
                {
                    "latitude": 40.7128,
                    "longitude": "-74.0060",
                    "name": "New York City",
                    "short_description": "The Big Apple",
                    "wikipedia_link": "https://en.wikipedia.org/wiki/New_Landmark",
                },
                400,
            ),
            # Good request with wikipedia link
            (
                {
                    "latitude": 40.7128,
                    "longitude": -74.0060,
                    "name": "New York City",
                    "short_description": "The Big Apple",
                    "wikipedia_link": "https://en.wikipedia.org/wiki/New_Landmark",
                },
                200,
            ),
            # Good request without wikipedia link
            (
                {
                    "latitude": 40.7128,
                    "longitude": -74.0060,
                    "name": "New York City",
                    "short_description": "The Big Apple",
                },
                200,
            ),
        ],
    )
    def test_suggestion_add(self, client, test_data, expected_status):
        headers, _ = helpers.register_user(client)

        # Make a POST request to the endpoint
        response = client.post(
            "/api/location_suggestions",
            json=test_data,
            headers=headers,
            content_type="application/json",
        )

        # Assert response status code
        assert response.status_code == expected_status
