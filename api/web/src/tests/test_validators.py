import pytest

from src.appl.validation import check_valid_email


class TestValidators:
    @pytest.mark.parametrize(
        "email, expected_is_valid_result",
        [("empty", False), ("some.email@withperiodsinitsfront.com", True)],
    )
    def test_is_valid_email(self, email: str, expected_is_valid_result: bool):
        assert check_valid_email(email) == expected_is_valid_result
