from src.appl.models import (
    Location,
    LocationAddSuggestion,
    LocationEditSuggestion,
    ShortLocationDescription,
    LongLocationDescription,
    LocationAddSuggestion,
)


def short_location_repr(l: Location) -> ShortLocationDescription:
    return {
        "id": l.id,
        "name": l.name,
        "latitude": l.latitude,
        "longitude": l.longitude,
        "short_description": l.short_description,
    }


def long_location_repr(l: Location) -> LongLocationDescription:
    return {
        "id": l.id,
        "name": l.name,
        "latitude": l.latitude,
        "longitude": l.longitude,
        "short_description": l.short_description,
        "long_description": l.long_description,
        "wikidata_image_name": l.wikidata_image_name,
    }


def add_suggestion_repr(s: LocationAddSuggestion) -> dict:
    return {
        "id": s.id,
        "user": s.user_id,
        "suggestion_time": s.suggestion_time,
        "name": s.name,
        "latitude": s.latitude,
        "longitude": s.longitude,
        "short_description": s.short_description,
        "wikipedia_link": s.wikipedia_link,
    }


def edit_suggestion_repr(s: LocationEditSuggestion) -> dict:
    return {
        "id": s.id,
        "location_id": s.location_id,
        "user": s.user_id,
        "suggestion_time": s.suggestion_time,
        "name": s.name,
        "short_description": s.short_description,
        "long_description": s.long_description,
    }