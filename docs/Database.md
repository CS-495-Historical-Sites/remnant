# Database

``` mermaid
erDiagram
    User {
        integer user_id  PK
        string email 
        string password_hash 
    }
    Location {
        integer location_id  PK
        string name 
    }

    User ||--o{ User-Favorite-Locations : "has"
    Location ||--o{ User-Favorite-Locations : "has"
    User-Favorite-Locations {
        integer user_id  FK
        integer location_id  FK
    }
```