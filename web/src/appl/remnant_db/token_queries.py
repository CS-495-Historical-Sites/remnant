from datetime import datetime, timedelta

from src.appl import db
from src.appl.models import BlacklistToken


def blacklist_token(token, logout_time, token_type, user_id):
    token_to_revoke = BlacklistToken(
        token_id=token, logout_time=logout_time, token_type=token_type, user_id=user_id
    )
    db.session.add(token_to_revoke)
    db.session.commit()


def is_token_blacklisted(token):
    current_token = BlacklistToken.query.filter_by(token_id=token).first()
    return current_token is not None


def clean_old_tokens():
    expiration_date = datetime.utcnow() - timedelta(days=30)
    BlacklistToken.query.filter_by(
        BlacklistToken.logout_date < expiration_date
    ).delete()
    db.session.commit()
