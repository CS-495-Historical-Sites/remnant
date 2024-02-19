import os

basedir = os.path.abspath(os.path.dirname(__file__))
database_location = os.path.join(basedir, "test.db")
DB_URI = "sqlite:///" + database_location
