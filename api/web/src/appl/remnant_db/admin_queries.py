from src.appl import db
from src.appl.models import Admin, RegistrationRequest


def email_exists(email: str) -> bool:
    return Admin.query.filter_by(email=email).first() is not None


def create_admin(registration_info: RegistrationRequest) -> None:
    user = Admin(
        email=registration_info.email, supplied_password=registration_info.password
    )
    db.session.add(user)
    db.session.commit()


def get_admin(email: str) -> Admin | None:
    return Admin.query.filter_by(email=email).first()
