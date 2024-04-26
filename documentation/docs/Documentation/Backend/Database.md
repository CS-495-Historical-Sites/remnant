# Database

``` mermaid
erDiagram
    User {
        int id PK
        string username
        string email UK
        string password_hash
        JSONB answers
        string email_confirmation_token
        int lockout
        bool is_admin
        bool has_confirmed_email
    }
    Location {
        int id  PK
        string name 
        float latitude
        float longitude
        string short_description
        string long_description
        string image_link
        int version
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
    LocationAddSuggestion {
        int id  PK
        int user_id  FK
        datetime suggestion_time
        float latitude
        float longitude
        string name 
        string short_description
        string wikipedia_link
        string image_link
    }
    LocationEditSuggestion {
        int id PK
        int user_id FK
        int location_id FK
        datetime suggestion_time
        string name 
        string short_description
        string long_description
    }
    LoginAttempt {
        int id PK
        string email
        datetime attempt_time
        bool success
        int lockout
    }

    SuggestionApproval {
        int id PK
        string suggestion_type
        int suggestion_id FK
        int admin_id FK
        string status
        string comments
        datetime decision_time
    }

    LocationHistory {
        int id PK
        int location_id FK
        string name 
        float latitude
        float longitude
        string short_description
        string long_description
        string image_link
        int version
        datetime modified_at
    }

    User ||--o{ LocationAddSuggestion : "suggests"
    User ||--o{ LocationEditSuggestion : "suggests"
    LocationEditSuggestion ||--o{ Location : "edits existing"
    LocationAddSuggestion ||--o{ Location : "creates new"
    User ||--o{ Visit : "visits"
    Location ||--o{ Visit : "visited by"
    User ||--o{BlacklistToken : "old token"
    Admin ||--o{SuggestionApproval : "decides"

    SuggestionApproval ||--o{ LocationAddSuggestion : "decides"
    SuggestionApproval ||--o{ LocationEditSuggestion : "decides"

    Location ||--o{ LocationHistory : "has"
```