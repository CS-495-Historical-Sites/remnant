# pylint: disable=unused-argument, line-too-long
import pytest

from src.appl.models import Visit
from src.appl.remnant_db import visit_queries

from src.tests import helpers


class TestVisitedLocations:
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
    def test_adding_visits(self, client, location_data, expected_status):

        helpers.fill_with_locations(client)

        headers, _ = helpers.register_user(client)

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
            assert response.status_code == 200

            # testing the database row was created
            test_query = Visit.query.filter_by(location_id=location_data["id"]).first()
            assert test_query is not None

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

        helpers.fill_with_locations(client)

        headers, _ = helpers.register_user(client)

        for i in range(1, 10):
            visit_queries.create_visited_location(i, 1)

        response = client.delete(
            "/api/user/visited_locations",
            json=location_data,
            headers=headers,
            content_type="application/json",
        )

        assert response.status_code == expected_status
        if response.status_code == 200:
            test_query = Visit.query.filter_by(location_id=location_data["id"]).first()
            assert test_query is None

    @pytest.mark.parametrize(
        "user_id, expected_status",
        [
            ({"id": 1}, 200),
        ],
    )

    # TESTING THAT WE RETURN ALL VISITED LOCATIONS
    def test_fetching_locations(self, client, user_id, expected_status):

        locations_added = helpers.fill_with_locations(client)

        headers, login_response = helpers.register_user(client)

        r_token = login_response.json.get("refresh_token")
        for i in range(1, 10):
            visit_queries.create_visited_location(i, 1)

        response = client.get(
            "/api/user/visited_locations",
            json=user_id,
            headers=headers,
            content_type="application/json",
        )
        assert response.status_code == expected_status

        locations_retrieved = response.json.get("visited_locations")
        assert len(locations_retrieved) == locations_added
