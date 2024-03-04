from src.appl.models import (
    Location,
    ShortLocationDescription,
    LongLocationDescription,
    LocationSuggestion,
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


def add_suggestion_repr(s: LocationSuggestion) -> dict:
    return {
        "id": s.id,
        "user": s.user_id,
        "suggestion_time": s.suggestion_time,
        "name": s.name,
        "latitude": s.latitude,
        "longitude": s.longitude,
        "short_description": s.short_description,
        "wikidata_image_name": s.wikidata_image_name,
    }
