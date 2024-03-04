# Remnant Documentation

Our public documentation is hosted at [uahistoricalsites.com](https://uahistoricalsites.com).

#### Project Structure

This repo consists of four different projects

- `android-app/`
    - Mobile app for our target user
- `api/`
    - Web server and database
    - PostgreSQL + Gunicorn & Flask
- `documentation/`
    - Project website using `mkdocs-material`
- `website/`
    - Admin website 
        - Used for interacting with user suggestions
    - Vite + React