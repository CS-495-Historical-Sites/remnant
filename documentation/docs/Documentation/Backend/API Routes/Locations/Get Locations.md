# Get All Locations

### Request

-   **URL:** `/api/locations`
-   **Method:** `GET`

#### Query Parameters

| Parameter        | Type   | Description                                |
| ---------------- | ------ | ------------------------------------------ |
| lat              | string | Latitude of the location.                  |
| long             | string | Longitude of the location.                 |
| kilometer_radius | string | Radius in kilometers around given lat/long |

### Responses

-   **200 OK**
    ```json
    [
      {
        "id": "<location_id>",
        "name": "<location_name>",
        "latitude": "<latitude>",
        "longitude": "<longitude>",
        "short_description": "<short_description>"
      },
      ...
    ]
    ```
-   **400 Bad Request**
    ```json
    {
        "message": "Must give both lat and long or neither"
    }
    ```
