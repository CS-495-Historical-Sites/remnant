from datetime import timedelta
import logging
import json
import time
from datetime import timedelta


from flask import Flask

from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager
from .config import Config


LOGGER = logging.getLogger(__name__)
FORMAT = "[%(filename)s:%(lineno)s - %(funcName)20s() ] %(message)s"
logging.basicConfig(format=FORMAT)
LOGGER.setLevel(Config.LOG_LEVEL)

db = SQLAlchemy()


def init_app():
    start_time = time.time()
    app = Flask(__name__)
    jwt = JWTManager(app)

    app.config.from_object(Config)

    app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=1)
    app.config["JWT_REFRESH_TOKEN_EXPIRES"] = timedelta(days=30)

    db.init_app(app)

    with app.app_context():
        from . import auth, routes, user, locations, hs_db
        from .models import program_metadata

        app.register_blueprint(routes.main_blueprint)
        app.register_blueprint(auth.auth_blueprint)
        app.register_blueprint(user.user_blueprint)
        app.register_blueprint(locations.location_blueprint)
        program_metadata.create_all(db.engine)
        with open("wikidata.json") as f:
            data = json.load(f)
            for location in data:
                name = location["name"]
                desc = location["descriptions"]
                coordinates = location["coordinates"]
                hs_db.create_location(
                    name,
                    coordinates["lat"],
                    coordinates["long"],
                    desc,
                    suspend_commit=True,
                )

            hs_db.commit()

        end_time = time.time()
        elapsed_time = end_time - start_time
        LOGGER.info(f"Elapsed time for app init: {elapsed_time} seconds")
        return app
