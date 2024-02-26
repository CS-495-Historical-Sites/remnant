# Logout User

### Request

- **URL:** `/api/logout`
- **Method:** `POST`

#### Body Parameters

The request body will contain the json web token that is assigned to the current user.
The route will store the logged out users access token and refresh token into a blacklist database.

| Field    | Type   | Description               |
| -------- | ------ | --------------------------|
| jwt      | dict   | jwt representing the user |


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
- **500 Database Error**
  ```json
  {
    "message": "Database Error"
  }
  ```