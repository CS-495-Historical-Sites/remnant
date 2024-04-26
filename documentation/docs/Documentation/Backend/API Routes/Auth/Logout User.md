# Logout User

### Request

- **URL:** `/api/logout`
- **Method:** `DELETE`

#### Body Parameters

The request body will contain the json web token that is assigned to the current user.
The route will store the logged out users access token and refresh token into a blacklist database.

As per the flask-jwt-extended documentation, this route should be called twice, once with the access token and again with the
refresh token.

| Field | Type | Description               |
| ----- | ---- | ------------------------- |
| jwt   | dict | jwt representing the user |

### Responses

- **200 OK**
  ```json
  {
    "message": "Logged out successfully"
  }
  ```
- **401 Token Unavailable**
  ```json
  {
    "message": "Token invalid or unavailable"
  }
  ```
- **404 Token Not Found**

  - May occur when the token is not associated with a user

  ```json
  {
    "message": "Token invalid or unavailable"
  }
  ```

- **500 Database Error**
  ```json
  {
    "message": "Database Error"
  }
  ```
