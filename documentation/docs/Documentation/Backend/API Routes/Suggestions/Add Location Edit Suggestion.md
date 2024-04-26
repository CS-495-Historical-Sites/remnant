# Add Location Edit Suggestion

### Request

-   **URL:** `/api/suggestions/location_edit_suggestions/<location_id>`
-   **Method:** `POST`
-   **Authorization:** Requires JWT token

#### Body Parameters

The request body will contain the following data:

| Field             | Type   | Description          |
| ----------------- | ------ | -------------------- |
| name              | string | Name of the location |
| short_description | string | Short description    |
| long_description  | string | Long description     |

### Responses

-   **200 OK**

    ```json
    {
        "message": "Suggestion Successfully Added"
    }
    ```

-   **400 Bad Request**
    ```json
    {
    "message": "Invalid location ID" | "Incomplete request" | "Invalid data submitted"
    }
    ```
