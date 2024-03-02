import pytest
from src.appl import db, init_app
from src.appl.models import program_metadata


@pytest.fixture()
def app():
    flask_app = init_app(testing=True)

    flask_app.config.update({"TESTING": True})

    with flask_app.app_context():
        yield flask_app

        db.session.close()
        program_metadata.drop_all(db.engine)


@pytest.fixture()
def client(app):
    return app.test_client()
