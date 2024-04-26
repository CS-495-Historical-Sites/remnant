# Add Location Add Suggestion

### Request

- **URL:** `/api/suggestions/location_add_suggestions`
- **Method:** `POST`
- **Authorization:** Requires JWT token

#### Body Parameters

The request body should contain the following data:

| Field             | Type   | Description               |
| ----------------- | ------ | ------------------------- |
| latitude          | float  | Latitude of the location  |
| longitude         | float  | Longitude of the location |
| name              | string | Name of the location      |
| short_description | string | Short description         |
| wikipedia_link    | string | (Optional) Wikipedia link |

### Responses

- **200 OK**

  ```json
  {
    "message": "Suggestion Successfully Added"
  }
  ```

- **400 Bad Request**
  ```json
  {
  "message": "Invalid location ID" | "Incomplete request" | "Invalid data submitted"
  }
  ```
