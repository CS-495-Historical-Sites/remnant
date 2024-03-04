from src.appl import db
from src.appl.models import LocationSuggestion


def add_location_suggestion(suggestion: LocationSuggestion) -> None:
    db.session.add(suggestion)
    db.session.commit()


def get_all_suggestions() -> list[LocationSuggestion]:
    return LocationSuggestion.query.all()
