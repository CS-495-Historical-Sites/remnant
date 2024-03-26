import logging
import os

import boto3


class Config:
    SECRET_KEY = os.environ.get("FLASK_SECRET_KEY")
    LOG_LEVEL = logging.DEBUG

    # Configure PostgreSQL database connection
    POSTGRES_USER = os.environ.get("POSTGRES_USER")
    POSTGRES_PASSWORD = os.environ.get("POSTGRES_PASSWORD")
    POSTGRES_HOST = os.environ.get("POSTGRES_HOST", "postgres")
    POSTGRES_PORT = os.environ.get("POSTGRES_PORT")
    POSTGRES_DB = os.environ.get("POSTGRES_DB")
    IMAGES_DIR = os.environ.get("FLASK_IMAGES_DIR", "/path/to/default/images/directory")

    ADMIN_EMAILS = os.getenv("ADMIN_EMAILS").split(";")

    AWS_ACCESS_KEY = os.getenv("AWS_ACCESS_KEY")
    AWS_SECRET_KEY = os.getenv("AWS_SECRET_KEY")
    AWS_SESSION_TOKEN = os.getenv("AWS_SESSION_TOKEN")

    S3_CLIENT = boto3.client(
        "s3",
        aws_access_key_id=AWS_ACCESS_KEY,
        aws_secret_access_key=AWS_SECRET_KEY,
    )

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
