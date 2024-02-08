from datetime import timedelta
import logging

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
    app = Flask(__name__)
    jwt = JWTManager(app)

    app.config.from_object(Config)

    app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=1)
    app.config["JWT_REFRESH_TOKEN_EXPIRES"] = timedelta(days=30)

    db.init_app(app)

    with app.app_context():
        from . import auth, routes, user, locations, hs_db, visit
        from .models import program_metadata

        app.register_blueprint(routes.main_blueprint)
        app.register_blueprint(auth.auth_blueprint)
        app.register_blueprint(user.user_blueprint)
        app.register_blueprint(locations.location_blueprint)
        app.register_blueprint(visit.visit_blueprint)
        program_metadata.create_all(db.engine)

        print("here")
        hs_db.create_location(location_name="New York")
        hs_db.create_location(location_name="Broadway")
        hs_db.create_location(location_name="Jacksonville")
        hs_db.create_location(location_name="London")
        hs_db.create_location(location_name="Birmingham")
        hs_db.create_location(location_name="Sarasota")
        hs_db.create_location(location_name="New Brunswick")
        hs_db.create_location(location_name="Toronto")



        return app
