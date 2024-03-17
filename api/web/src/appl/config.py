import logging
import os


class Config:
    SECRET_KEY = os.environ.get("FLASK_SECRET_KEY")
    LOG_LEVEL = logging.DEBUG

    # Configure PostgreSQL database connection
    POSTGRES_USER = os.environ.get("POSTGRES_USER")
    POSTGRES_PASSWORD = os.environ.get("POSTGRES_PASSWORD")
    POSTGRES_HOST = os.environ.get("POSTGRES_HOST", "postgres")
    POSTGRES_PORT = os.environ.get("POSTGRES_PORT")
    POSTGRES_DB = os.environ.get("POSTGRES_DB")

    ADMIN_EMAILS = os.getenv("ADMIN_EMAILS").split(";")

    assert all(
        [
            SECRET_KEY,
            POSTGRES_USER,
            POSTGRES_PASSWORD,
            POSTGRES_DB,
            POSTGRES_PORT,
            POSTGRES_HOST,
        ]
    )

    # pylint: disable=line-too-long
    SQLALCHEMY_DATABASE_URI = f"postgresql://{POSTGRES_USER}:{POSTGRES_PASSWORD}@{POSTGRES_HOST}:{POSTGRES_PORT}/{POSTGRES_DB}"

    SQLALCHEMY_TRACK_MODIFICATIONS = False
