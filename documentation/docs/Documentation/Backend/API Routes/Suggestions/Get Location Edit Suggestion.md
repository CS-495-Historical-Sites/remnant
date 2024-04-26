# Get Location Edit Suggestion by ID

### Request

-   **URL:** `/api/suggestions/location_edit_suggestions/<suggestion_id>`
-   **Method:** `GET`

#### Path Parameters

| Parameter     | Type   | Description        |
| ------------- | ------ | ------------------ |
| suggestion_id | string | Edit suggestion ID |

### Responses

-   **200 OK**

    ```json
    {
        "id": "<suggestion_id>",
        "location_id": "<location_id>",
        "user": "<user_id>",
        "suggestion_time": "<suggestion_time>",
        "name": "<location_name>",
        "short_description": "<short_description>",
        "long_description": "<long_description>"
    }
    ```

-   **400 Bad Request**
    ```json
    {
        "message": "Invalid suggestion ID"
    }
    ```
