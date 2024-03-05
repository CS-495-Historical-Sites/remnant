from flask import jsonify, Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required


from src.appl.models import LocationSuggestion, LocationSuggestionRequest
from src.appl.remnant_db import user_queries, suggestion_queries
from src.appl.responses import add_suggestion_repr
from src.appl.validation import check_types

suggestion_blueprint = Blueprint(
    "suggestion_blueprint",
    __name__,
)


@suggestion_blueprint.route("/api/location_suggestions", methods=["POST"])
@jwt_required()
def add_location_suggestion():
    user_identity = get_jwt_identity()
    user = user_queries.get_user(user_identity)
    if user is None:
        return jsonify({"message": "User not found"}), 400

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

    suggestion_req = LocationSuggestionRequest(
        latitude, longitude, name, short_desc, wikipedia_link
    )

    suggestion = LocationSuggestion(user, suggestion_req)

    suggestion_queries.add_location_suggestion(suggestion)

    return jsonify({"message": "Suggestion Successfully Added"}), 200


@suggestion_blueprint.route("/api/location_suggestions", methods=["GET"])
def get_all_location_suggestions():
    all_suggestions = suggestion_queries.get_all_suggestions()
    return jsonify([add_suggestion_repr(s) for s in all_suggestions]), 200