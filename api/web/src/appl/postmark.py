# third party
from typing import Dict
import requests

# local
from src.appl import db, LOGGER
from src.appl.config import Config
from src.appl.models import User


def compose_welcome_body(
    template_alias: str,
    email_to: str,
    username: str,
    confirmation_token: str,
) -> Dict:
    """Compose the body of the email."""

    data = {
        "From": Config.EMAIL_ADDRESS,
        "To": email_to,
        "TemplateAlias": template_alias,
        "TemplateModel": {
            "product_url": Config.PRODUCT_URL,
            "product_name": Config.PRODUCT_NAME,
            "username": username,
            "action_url": f"some action url with confirmation token: {confirmation_token}",
            "company_name": Config.COMPANY_NAME,
            "company_address": Config.COMPANY_ADDRESS,
        },
    }
    return data


def send_welcome_email(user: User) -> bool:
    """Send a welcome email to the user."""

    # Get the template from Postmark
    url = "https://api.postmarkapp.com/email/withTemplate"
    postmark_token = Config.POSTMARK_API_KEY
    headers = {
        "Accept": "application/json",
        "Content-Type": "application/json",
        "X-Postmark-Server-Token": postmark_token,
    }

    # Compose the body of the email
    body = compose_welcome_body(
        template_alias="Welcome-w-confirmation",
        email_to=user.email,
        username=user.username,
        confirmation_token=user.email_confirmation_token,
    )

    # Send the email
    response = requests.post(url, headers=headers, json=body)
    if response.status_code != 200:
        LOGGER.error(
            "Failed to send welcome email to %s: %s", user.email, response.text
        )
        return False
    return True
