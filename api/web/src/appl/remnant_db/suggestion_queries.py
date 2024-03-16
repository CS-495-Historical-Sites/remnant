from src.appl.models import LocationEditSuggestion, LocationSuggestion


def get_all_location_add_suggestions() -> list[LocationSuggestion]:
    return LocationSuggestion.query.all()


def get_all_location_edit_suggestions() -> list[LocationEditSuggestion]:
    return LocationEditSuggestion.query.all()


def get_all_location_edit_suggestion_by_id(
    suggestion_id: int,
) -> LocationEditSuggestion | None:
    return LocationEditSuggestion.query.filter_by(id=suggestion_id).first()
