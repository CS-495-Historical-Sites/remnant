import logging
import os

import boto3


class Config:
    SECRET_KEY = os.environ.get("FLASK_SECRET_KEY")
    LOG_LEVEL = logging.DEBUG

    __PRODUCT_URL_PROD__ = "https://app.uahistoricalsites.com/"
    __PRODUCT_URL_DEV__ = "http://localhost:5173/"

    PRODUCT_URL = __PRODUCT_URL_PROD__

    PRODUCT_NAME = "Remnant"

    EMAIL_CONFIRMATION_URL = ""
    COMPANY_NAME = "Remnant"
    COMPANY_ADDRESS = "Tuscaloosa, AL"

    EMAIL_ADDRESS = "erich.reitz@uahistoricalsites.com"

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

    S3_CLIENT = boto3.client(
        "s3",
        aws_access_key_id=AWS_ACCESS_KEY,
        aws_secret_access_key=AWS_SECRET_KEY,
    )

    POSTMARK_API_KEY = os.getenv("POSTMARK_API_KEY")

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
