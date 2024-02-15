# Database

``` mermaid
erDiagram
    User {
        int id  PK
        string email UK
        string password_hash 
    }
    Location {
        int id  PK
        string name 
        float latitude
        float longitude
        string short_description
        string long_description
    }
    Visit {
        int id  PK
        datetime visit_time 
        int user_id  FK
        int location_id  FK
    }

    User ||--o{ Visit : "visits"
    Location ||--o{ Visit : "visited by"
```