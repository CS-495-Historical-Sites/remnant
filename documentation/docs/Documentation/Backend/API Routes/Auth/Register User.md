# Register User

### Request

- **URL:** `/api/register`
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
    "email": "<user_email>",
    "errorString": ""
  }
  ```
- **400 Bad Request**

  ```json
  {
    "message": "Incomplete request"
  }
  ```

- **400 Bad Request**

  ```json
  {
    "message": "No data supplied"
  }
  ```

- **422 Unprocessable Entity**
  ```json
  {
    "message": "Email already exists"
  }
  ```
