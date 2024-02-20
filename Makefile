.PHONY: docker-run docker-test format lint

docker-run: 
	poetry export -f requirements.txt --output requirements.txt
	docker build --tag 'hs' -f Dockerfile .
	docker run -p 8080:8080 hs

docker-test:
	docker build -t testing-image:v1.0 -f TestDockerfile .  
	docker run --rm testing-image:v1.0  

format:
	poetry run black .

lint:
	poetry run pylint hs_backend/
