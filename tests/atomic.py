import os

from hs_backend.appl import db

from tests import database_location


class TestIsolator:
    def setup_app(self):
        db.create_all()

    def teardown_app(self):
        db.drop_all()
        # remove the test.db file after each test
        if os.path.exists(database_location):
            os.unlink(database_location)
