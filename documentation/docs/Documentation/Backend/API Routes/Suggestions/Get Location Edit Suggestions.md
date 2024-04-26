# Get All Location Edit Suggestions

### Request

-   **URL:** `/api/suggestions/location_edit_suggestions`
-   **Method:** `GET`

### Responses

-   **200 OK**
    ```json
    [
      {
      "id": "<suggestion_id>",
      "location_id": "<location_id>",
      "user": "<user_id>",
      "suggestion_time": "<suggestion_time>",
      "name": "<location_name>",
      "short_description": "<short_description>",
      "long_description": "<long_description>"
      },
      ...
    ]
    ```
