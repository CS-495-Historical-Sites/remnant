import re


def check_types(variables: list):
    for entry in variables:
        n_vars_to_check = len(entry) - 1
        vars_to_check, var_types = entry[0:n_vars_to_check], entry[-1]
        if not all(isinstance(var, var_types) for var in vars_to_check):
            return False

    return True


# Regex to match pattern with 3 constraints: (1@2.3)
# 1. Upper/lowercase letters, digits, or ._%+- followed by @
# 2. Upper/lowercase letters, digits, or .- followed by .
# 3. Upper/lowercase letters 2-7 characters for domains.
def check_valid_email(email):
    if email == "":
        return False
    regex = r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,7}\b"
    return bool(re.fullmatch(regex, email)) and len(email) < 40


# only allows digits, upper or lowercase letters, and the special characters @!$%*?&
def check_valid_password(password):
    password_regex = re.compile(
        r"^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]+$"
    )

    return bool(re.fullmatch(password_regex, password)) and (
        (len(password)) >= 8 and (len(password) < 25)
    )
