# Get All Location Add Suggestions

### Request

-   **URL:** `/api/suggestions/location_add_suggestions`
-   **Method:** `GET`

### Responses

-   **200 OK**
    ```json
    [
      {
        "id": "<suggestion_id>",
        "user": "<user_id>",
        "suggestion_time": "<suggestion_time>",
        "name": "<location_name>",
        "latitude": "<latitude>",
        "longitude": "<longitude>",
        "short_description": "<short_description>",
        "wikipedia_link": "<wikipedia_link>"
      },
      ...
    ]
    ```
