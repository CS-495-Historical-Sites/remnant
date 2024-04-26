# Get Location

### Request

-   **URL:** `/api/locations/<location_id>`
-   **Method:** `GET`

#### Path Parameters

| Parameter   | Type   | Description        |
| ----------- | ------ | ------------------ |
| location_id | string | ID of the location |

### Responses

-   **200 OK**

    ```json
    {
        "id": "<location_id>",
        "name": "<location_name>",
        "latitude": "<latitude>",
        "longitude": "<longitude>",
        "short_description": "<short_description>",
        "long_description": "<long_description>"
    }
    ```

-   **400 Bad Request**

    ```json
    {
        "message": "Invalid location ID"
    }
    ```

-   **404 Not Found**
    ```json
    {
        "message": "Location not found"
    }
    ```
