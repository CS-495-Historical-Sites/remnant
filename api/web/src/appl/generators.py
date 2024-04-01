# standard library
import random
import string


def generate_email_confirmation_token() -> str:
    return "".join(
        random.SystemRandom().choice(
            string.ascii_lowercase + string.ascii_uppercase + string.digits
        )
        for _ in range(15)
    )
