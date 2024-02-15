# Get Visited Locations

### Request

- **URL:** `/api/user/visited_locations`
- **Method:** `GET`
- **Authorization:** Bearer Token

### Responses

- **200 OK**
  ```json
  {
    "visited_locations": [
      {
        "id": "<location_id>",
        "name": "<location_name>"
      },
      ...
    ]
  }
  ```
- **200 OK**
  ```json
  {
    "message": "No visited locations found for the user",
    "visited_locations": []
  }
  ```
- **400 Bad Request**
  ```json
  {
    "message": "User not found"
  }
  ```