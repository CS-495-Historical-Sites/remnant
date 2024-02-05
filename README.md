### Build commands

#### Run locally (outside of docker)

`flask run --app hs_backend`

#### Docker build

`docker build --tag 'hs' .`

#### Docker run

`docker run -p 8080:8080 hs`

### Format 

`poetry run black .`

### Poetry export to requirements.txt

`poetry export -f requirements.txt --output requirements.txt`