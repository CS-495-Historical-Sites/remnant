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
    BlacklistToken {
        int id PK
        string token_id
        string token_type
        int user_id FK
        datetime logout_time
    }
    LocationSuggestion {
        int id  PK
        int user_id  FK
        datetime suggestion_time
        float latitude
        float longitude
        string name 
        string short_description
        string wikipedia_link
    }
    LoginAttempt {
        int id PK
        string email
        datetime attempt_time
        bool success
    }

    User ||--o{ LocationSuggestion : "suggests"
    User ||--o{ Visit : "visits"
    Location ||--o{ Visit : "visited by"
    User ||--o{BlacklistToken : "old token"
```