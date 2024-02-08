from flask import jsonify, Blueprint


main_blueprint = Blueprint("main_blueprint", __name__)


@main_blueprint.route("/")
def home():
    return jsonify({"message": "Welcome to the API"})








