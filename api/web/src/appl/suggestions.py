# pylint: disable=unused-argument
from flask import jsonify, Blueprint, request
from flask_jwt_extended import jwt_required

from src.appl import db, LOGGER
from src.appl.models import (
    LocationEditSuggestion,
    LocationEditSuggestionRequest,
    LocationAddSuggestion,
    LocationAddSuggestionRequest,
    SuggestionApproval,
    Location,
    User,
)

from src.appl.auth import admin_required, user_required
from src.appl.remnant_db import suggestion_queries
from src.appl.responses import add_suggestion_repr, edit_suggestion_repr
from src.appl.validation import check_types
from src.appl.remnant_db import location_queries

suggestion_blueprint = Blueprint(
    "suggestion_blueprint",
    __name__,
)


@suggestion_blueprint.route("/api/suggestions/locations/add", methods=["POST"])
@jwt_required()
@user_required
def add_location_suggestion(user: User):
    data = request.get_json()
    try:
        latitude, longitude = data["latitude"], data["longitude"]
        name, short_desc = data["name"], data["short_description"]
        wikipedia_link = data.get("wikipedia_link", None)
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400

    if not check_types([(latitude, longitude, float)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    if not check_types([(name, short_desc, str), (wikipedia_link, (str, type(None)))]):
        return jsonify({"message": "Invalid data submitted"}), 400

    suggestion_req = LocationAddSuggestionRequest(
        latitude, longitude, name, short_desc, wikipedia_link
    )

    suggestion = LocationAddSuggestion(user, suggestion_req)

    db.session.add(suggestion)
    db.session.commit()

    return jsonify({"message": "Suggestion Successfully Added"}), 200


@suggestion_blueprint.route(
    "/api/suggestions/locations/edit/<location_id>", methods=["POST"]
)
@jwt_required()
@user_required
def add_location_edit_suggestion(user: User, location_id):
    try:
        location_key = int(location_id)
    except ValueError:
        return jsonify({"message": "Invalid location ID"}), 400

    data = request.get_json()

    try:
        name, short_desc, long_desc = (
            data["name"],
            data["short_description"],
            data["long_description"],
        )
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400

    if not check_types([(name, short_desc, long_desc, str)]):
        return jsonify({"message": "Invalid data submitted"}), 400

    suggestion_req = LocationEditSuggestionRequest(
        location_id=location_key,
        name=name,
        short_description=short_desc,
        long_description=long_desc,
    )

    suggestion = LocationEditSuggestion(user, suggestion_req)

    db.session.add(suggestion)
    db.session.commit()

    return jsonify({"message": "Suggestion Successfully Added"}), 200


@suggestion_blueprint.route("/api/suggestions/locations/edit", methods=["GET"])
@jwt_required()
@admin_required
def get_all_location_edit_suggestions(admin: User):
    all_suggestions = suggestion_queries.get_all_location_edit_suggestions()
    return jsonify([edit_suggestion_repr(s) for s in all_suggestions]), 200


@suggestion_blueprint.route(
    "/api/suggestions/locations/edit/<suggestion_id>", methods=["GET"]
)
@jwt_required()
@admin_required
def get_location_edit_suggestion(admin: User, suggestion_id: str):
    try:
        suggestion_key = int(suggestion_id)
    except ValueError:
        return jsonify({"message": "Invalid suggestion ID"}), 400

    suggestion = suggestion_queries.get_all_location_edit_suggestion_by_id(
        suggestion_key
    )
    return jsonify(edit_suggestion_repr(suggestion)), 200


@suggestion_blueprint.route("/api/suggestions/locations/add", methods=["GET"])
@jwt_required()
@admin_required
def get_all_location_add_suggestions(admin: User):
    all_suggestions = suggestion_queries.get_all_location_add_suggestions()
    return jsonify([add_suggestion_repr(s) for s in all_suggestions]), 200


@suggestion_blueprint.route(
    "/api/suggestions/locations/edit/<suggestion_id>/approval", methods=["PATCH"]
)
@jwt_required()
@admin_required
def handle_approval_result_for_location_edit(admin: User, suggestion_id: str):
    LOGGER.info("Handling approval result for location edit")
    data = request.get_json()

    try:
        status = data["status"]
        suggestion_id = int(suggestion_id)
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400
    except ValueError:
        return jsonify({"message": "Invalid suggestion ID"}), 400

    suggestion = suggestion_queries.get_location_edit_suggestion_by_id(
        suggestion_id
    )

    if suggestion is None:
        return jsonify({"message": "Suggestion not found"}), 404

    approval = SuggestionApproval(
        suggestion_type="location_edit",
        suggestion_id=suggestion_id,
        admin_id=admin.id,
        status=status,
    )
    db.session.add(approval)
    db.session.commit()

    if status == "approved":
        location = location_queries.get_location(suggestion.location_id)
        location.apply_edit_suggestion(suggestion)
        db.session.commit()


    return jsonify({"message": "Suggestion status updated"}), 200


@suggestion_blueprint.route(
    "/api/suggestions/locations/add/<suggestion_id>/approval", methods=["PATCH"]
)
@jwt_required()
@admin_required
def handle_approval_result_for_location_add(admin: User, suggestion_id: str):
    LOGGER.info("Handling approval result for location add")
    data = request.get_json()

    try:
        status = data["status"]
        suggestion_id = int(suggestion_id)
    except KeyError:
        return jsonify({"message": "Incomplete request"}), 400
    except ValueError:
        return jsonify({"message": "Invalid suggestion ID"}), 400

    suggestion = suggestion_queries.get_location_add_suggestion_by_id(
        suggestion_id
    )

    if suggestion is None:
        return jsonify({"message": "Suggestion not found"}), 404

    approval = SuggestionApproval(
        suggestion_type="location_add",
        suggestion_id=suggestion_id,
        admin_id=admin.id,
        status=status,
    )
    db.session.add(approval)
    db.session.commit()

    if status == "approved":
        location = Location(
            name=suggestion.name,
            latitude=suggestion.latitude,
            longitude=suggestion.longitude,
            short_description=suggestion.short_description,
            wikidata_image_name="",
            long_description="",
        )
        location_queries.create_location(location)


    return jsonify({"message": "Suggestion status updated"}), 200