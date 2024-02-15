from datetime import timedelta
import logging
import json
import time

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
        from . import auth, routes, locations, hs_db, visit
        from .models import program_metadata

        app.register_blueprint(routes.main_blueprint)
        app.register_blueprint(auth.auth_blueprint)
        app.register_blueprint(locations.location_blueprint)
        app.register_blueprint(visit.visit_blueprint)
        program_metadata.create_all(db.engine)

        skipped = 0
        with open("wikidata.json", "r", encoding="utf-8") as f:
            data = json.load(f)
            for location in data:
                name = location["name"]
                short_desc = location["short_description"]
                long_desc = location["long_description"]
                coordinates = location["coordinates"]
                if not coordinates:
                    skipped += 1
                    continue
                hs_db.create_location(
                    name,
                    coordinates["lat"],
                    coordinates["long"],
                    short_desc=short_desc,
                    long_desc=long_desc,
                    suspend_commit=True,
                )

            hs_db.commit()

        end_time = time.time()
        elapsed_time = end_time - start_time
        LOGGER.info(f"Elapsed time for app init: {elapsed_time} seconds")

        LOGGER.warning(f"Skipped {skipped} locations during app init")
        return app
