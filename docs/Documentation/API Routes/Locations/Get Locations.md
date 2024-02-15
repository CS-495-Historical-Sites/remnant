# Get All Locations

### Request

- **URL:** `/api/locations`
- **Method:** `GET`

#### Query Parameters

| Parameter | Type   | Description                 |
| --------- | ------ | --------------------------- |
| lat       | string | Latitude of the location    |
| long      | string | Longitude of the location   |

### Responses

- **200 OK**
  ```json
  [
    {
      "id": "<location_id>",
      "name": "<location_name>",
      "latitude": "<latitude>",
      "longitude": "<longitude>"
    },
    ...
  ]
  ```
- **400 Bad Request**
  ```json
  {
    "message": "Must give both lat and long or neither"
  }
  ```