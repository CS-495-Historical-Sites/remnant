from src.appl import db
from src.appl.models import Location


# pylint:disable=unused-argument
def fill_with_locations(client):
    locations = [
        Location("city1", 45.0283, 143.2030, "description1", "longdestdecriptionever"),
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

    return len(locations)


def register_user(client) -> dict[str, str]:
    user_data = {
        "username": "ausername",
        "email": "testuser@example.com",
        "password": "Astrongpassword43*",
    }

    register_response = client.post("/api/user/register", json=user_data)
    assert register_response.status_code == 200

    login_response = client.post("/api/user/login", json=user_data)
    assert login_response.status_code == 200

    j_token = login_response.json.get("access_token")
    headers = {"Authorization": f"Bearer {j_token}"}

    return headers, login_response
