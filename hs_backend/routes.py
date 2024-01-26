from flask import jsonify

from hs_backend import app

@app.route("/")
def home():
    return jsonify({"message": "Welcome to the API"})