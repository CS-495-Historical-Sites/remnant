from src.appl.models import (
    LocationEditSuggestion,
    LocationAddSuggestion,
    SuggestionApproval,
)


def get_all_location_add_suggestions() -> list[LocationAddSuggestion]:
    return LocationAddSuggestion.query.all()


def get_all_location_edit_suggestions() -> list[LocationEditSuggestion]:
    return LocationEditSuggestion.query.all()


def get_location_edit_suggestion_by_id(
    suggestion_id: int,
) -> LocationEditSuggestion | None:
    return LocationEditSuggestion.query.filter_by(id=suggestion_id).first()


def get_location_add_suggestion_by_id(
    suggestion_id: int,
) -> LocationAddSuggestion | None:
    return LocationAddSuggestion.query.filter_by(id=suggestion_id).first()
