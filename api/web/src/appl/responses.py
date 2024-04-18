from src.appl.models import (
    Location,
    LocationAddSuggestion,
    LocationEditSuggestion,
    ShortLocationDescription,
    LongLocationDescription,
)
from src.appl import LOGGER


def short_location_repr(location_tuple) -> ShortLocationDescription:
    location, is_liked = location_tuple
    return {
        "id": location.id,
        "name": location.name,
        "latitude": location.latitude,
        "longitude": location.longitude,
        "short_description": location.short_description,
        "image_link": location.image_link,
        "is_liked": is_liked
    }


def long_location_repr(l: Location) -> LongLocationDescription:
    return {
        "id": l.id,
        "name": l.name,
        "latitude": l.latitude,
        "longitude": l.longitude,
        "short_description": l.short_description,
        "long_description": l.long_description,
        "image_link": l.image_link,
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
        "image_url": s.image_url,
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
