import uuid
import logging

from src.appl.config import Config

from botocore.exceptions import ClientError


def upload_user_image(image_bytes: bytes):
    # Your bucket name
    bucket_name = "remnantphotos"

    # Generate a random file name
    file_name = f"{uuid.uuid4()}.jpg"
    try:
        Config.S3_CLIENT.put_object(Bucket=bucket_name, Key=file_name, Body=image_bytes)
    except ClientError as e:
        logging.error(e)
        return False

    # Generate the URL
    url = f"https://{bucket_name}.s3.amazonaws.com/{file_name}"
    return url
