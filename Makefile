.PHONY: docker-dev-clean docker-dev-run docker-ci-test format lint

docker-dev-clean:
	@sudo rm -rf ./pgdata

docker-dev-run:
	@poetry export -f requirements.txt --output web/requirements.txt
	@docker compose -f docker-compose.dev.yml --env-file ./.env.dev up  --build --remove-orphans

docker-ci-test: docker-dev-clean
	@docker compose -f docker-compose.ci.yml --env-file ./.env.dev  up --build --remove-orphans --exit-code-from test-runner


docker-prod-run:
	@poetry export -f requirements.txt --output web/requirements.txt
	@docker compose --env-file ./.env  up --build --remove-orphans

format:
	@poetry run black .

lint:
	@poetry run pylint --rcfile=./web/.pylintrc ./web/
