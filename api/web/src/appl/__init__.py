from datetime import timedelta
import logging
import json
import time

from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager
from sqlalchemy import create_engine, text
from sqlalchemy.exc import OperationalError

from src.appl.config import Config

LOGGER = logging.getLogger(__name__)
FORMAT = "[%(filename)s:%(lineno)s - %(funcName)20s() ] %(message)s"
logging.basicConfig(format=FORMAT)
LOGGER.setLevel(Config.LOG_LEVEL)

db = SQLAlchemy()


def db_contains_locations(db_uri) -> bool:
    try:
        engine = create_engine(db_uri)
        with engine.connect() as connection:
            query = "SELECT COUNT(*) FROM location;"
            result = connection.execute(text(query))
            row_count = result.fetchone()[0]
            return row_count > 0
    except OperationalError:
        return False


def collate_categories(initial_categories: str) -> list[str]:
    groups = {
        "Heritage": [
            "ETHNIC_HERITAGE",
            "ART",
            "LITERATURE",
            "RELIGION",
            "PHILOSOPHY",
            "PERFORMING_ARTS",
            "NATIVE_AMERICAN",
        ],
        "Maritime History": ["MARITIME_HISTORY"],
        "Development": [
            "COMMUNITY_PLANNING_DEVELOPMENT",
            "LANDSCAPE_ARCHITECTURE",
            "POLITICS_GOVERNMENT",
            "LAW",
        ],
        "Innovation": [
            "SCIENCE",
            "HEALTH_MEDICINE",
            "ENGINEERING",
            "INVENTION",
            "ARCHEOLOGY",
            "AGRICULTURE",
            "CONSERVATION",
        ],
        "Military": ["MILITARY"],
        "History": [
            "SOCIAL_HISTORY",
            "ECONOMICS",
            "COMMERCE",
            "EDUCATION",
            "COMMUNICATIONS",
        ],
        "Infrastructure": ["INDUSTRY", "TRANSPORTATION"],
        "Recreation": ["ENTERTAINMENT_RECREATION"],
        "Diversity": ["BLACK", "EUROPEAN", "ASIAN", "PACIFIC_ISLANDER", "HISPANIC"],
        "Architecture": ["ARCHITECTURE"],
        "Prehistoric": ["PREHISTORIC"],
        "Settlement": ["EXPLORATION_SETTLEMENT"],
    }

    group_names = []
    for initial_category in initial_categories:
        for group_name, categories in groups.items():
            if initial_category in categories:
                group_names.append(group_name)

    return group_names


def init_db_with_sources_file(app):
    from src.appl.models import Location
    from src.appl.remnant_db import location_queries

    start_time = time.time()
    skipped = 0
    with open("sources/wikidata.json", "r", encoding="utf-8") as f:
        data = json.load(f)
        for location in data:
            name = location["name"]
            short_desc = location["short_description"]
            long_desc = location["long_description"]
            coordinates = location["coordinates"]

            categories_list = [c["kind"] for c in location["assosiated_categories"]]

            image_name = location["wikidata_image_name"]
            image_name = f"https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/{image_name}"
            if not coordinates:
                skipped += 1
                continue

            loc = Location(
                name=name,
                latitude=coordinates["latitude"],
                longitude=coordinates["longtitude"],
                image_link=image_name,
                categories=collate_categories(categories_list),
                short_description=short_desc,
                long_description=long_desc,
            )

            location_queries.create_location(
                loc,
                suspend_commit=True,
            )

        db.session.commit()

    end_time = time.time()
    elapsed_time = end_time - start_time
    LOGGER.info(f"Elapsed time for app init: {elapsed_time} seconds")

    LOGGER.warning(f"Skipped {skipped} locations during app init")


def init_app(testing=False, db_uri=Config.SQLALCHEMY_DATABASE_URI):

    app = Flask(__name__)
    CORS(app)
    jwt = JWTManager(app)
    if testing:
        Config.TESTING = True
    app.config.from_object(Config)
    app.config["SQLALCHEMY_DATABASE_URI"] = db_uri
    app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=24)
    app.config["JWT_REFRESH_TOKEN_EXPIRES"] = timedelta(days=30)

    db.init_app(app)

    with app.app_context():
        from src.appl import auth, routes, locations, visit, suggestions, user
        from src.appl.models import program_metadata
        from src.appl.remnant_db import token_queries

        app.register_blueprint(routes.main_blueprint)
        app.register_blueprint(auth.auth_blueprint)
        app.register_blueprint(locations.location_blueprint)
        app.register_blueprint(visit.visit_blueprint)
        app.register_blueprint(suggestions.suggestion_blueprint)
        app.register_blueprint(user.user_blueprint)

        program_metadata.create_all(db.engine)

        # register jwt blocklist function
        # pylint: disable=unused-argument
        @jwt.token_in_blocklist_loader
        def check_token(jwt_header, jwt_payload: dict) -> bool:
            jti = jwt_payload["jti"]
            return token_queries.is_token_blacklisted(jti)

        if db_contains_locations(db_uri):
            return app

        if not testing:
            init_db_with_sources_file(app)

        return app
