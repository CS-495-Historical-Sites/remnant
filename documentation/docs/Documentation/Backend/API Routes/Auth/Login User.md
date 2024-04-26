# Login User

### Request

- **URL:** `/api/login`
- **Method:** `POST`

#### Body Parameters

The request body should be in JSON format and contain the following fields:

| Field    | Type   | Description     |
| -------- | ------ | --------------- |
| email    | string | User's email    |
| password | string | User's password |

### Responses

- **200 OK**
  ```json
  {
    "access_token": "<access_token>",
    "refresh_token": "<refresh_token>"
  }
  ```
- **422 Unprocessable Entity**
  ```json
  {
    "message": "Invalid email or password",
    "accessToken": ""
  }
  ```
- **400 Bad Request**
  ```json
  {
    "message": "Incomplete request"
  }
  ```
