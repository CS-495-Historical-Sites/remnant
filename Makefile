.PHONY: run docker-build docker-run format export

docker-build:
	docker build --tag 'hs' .

docker-run: docker-build
	docker run -p 8080:8080 hs

format:
	poetry run black .

export:
	poetry export -f requirements.txt --output requirements.txt
