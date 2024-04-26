# Delete Visited Location

### Request

- **URL:** `/api/user/visited_locations`
- **Method:** `DELETE`
- **Authorization:** Bearer Token

#### Body Parameters

The request body should be in JSON format and contain the following field:

| Field | Type   | Description        |
| ----- | ------ | ------------------ |
| id    | string | ID of the location |

### Responses

- **200 OK**
  ```json
  {
    "message": "Removed Location"
  }
  ```
- **400 Bad Request**
  ```json
  {
    "message": "User not found"
  }
  ```
- **400 Bad Request**
  ```json
  {
    "message": "Invalid location ID"
  }
  ```
- **404 Not found**
  ```json
  {
    "message": "Location not found"
  }
  ```
