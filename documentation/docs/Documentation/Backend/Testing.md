# API Testing Documentation

## Introduction

This document provides an overview of the testing strategies and procedures for
our API endpoints and database.

## Test Environment

We used pytest to automatically run multiple tests in an isolated environment.
Pytest allowed us to parameterize test cases so we made lists of data with
different edge cases that pytest automatically tested. We ran our Flask app in
testing mode and set up and testing instance of our database.

# Unit Tests

## 1. User Table

-   **Description** This was a test that we can add a valid user in our
    database, We called the User table constructor and entered valid information
    and then compared our stored information with the original information.

## 2. Location Table

-   **Description** This was a test that we can add a valid location in our
    database. We called the location table constructor and entered valid
    information and then tested if our information was stored correctly.

## 3. Visit Table

-   **Description** This was a test that we can add a valid visit row in our
    database. A visit row ties a user and a location together and has a
    uniqueness constraint based on user_id and location_id foreign keys. We
    called the visit table constructor and entered valid information and then
    tested if our information was stored correctly.

## 4. BlacklistToken Table

-   **Description** This table is made to store tokens when a user logs out.
    This ensures that these tokens are no longer valid or have access to our
    system.

# Functional Tests

### **1. Registration Endpoint**

-   **Description:** The Registration endpoint receives a request containing an
    email and a password to create a record in our User table. We have multiple
    restrictions to make sure our users have proper formatting and security.
    These are our email and password restrictions:

-   **1.** The email we allow characters A-Z, a-z, 0-9, and any of the
    characters -\_%+. and a length restriction of 40 characters.
-   **2.** The password allows characters A-Z, a-z, 0-9, and any of the
    characters !@#$%^&\*()\_+ and has a length restriction of 8 - 25.

We built multiple scenarios to handle our restrictions and user errors.

**Test Scenarios:**

-   Request is sent to registration endpoint with no json data -> status code of
    400
-   Request is sent to registration endpoint with missing json field (email or
    password) -> status code of 400
-   Request is sent to registration endpoint with valid json fields -> status
    code of 200
-   Request is sent to registration endpoint with empty json fields -> status
    code of 422
-   Request is sent to registration endpoint with improperly formatted fields
    (email or password) -> status code of 422

### **2. Login Endpoint**

-   **Description:** This endpoint is to login users that have already been
    registered. They must enter valid formatting and the credentials entered
    must match a user our User table. The test cases account for missing data in
    requsts and incorrect credentials.

**Test Scenarios:**

-   Request is sent to login endpoint with no json data -> status code of 400
-   Request is sent to login endpoint with missing json field (email or
    password) -> status code of 400
-   Request is sent to login endpoint with valid json fields -> status code of
    200
-   Request is sent to login endpoint with empty json fields -> status code of
    422
-   Request is sent to login endpoint with improperly formatted fields (email or
    password) -> status code of 422
-   Request is sent to login endpoint with credentials that do not exist in User
    table -> status code of 422

### **3. Logout Endpoint**

-   **Description:** This endpoint is to verify loged users can logout and
    blacklist any access tokens associated with their current session. The web
    tokens we use must be manually blacklisted so that logged out users cannot
    make any valid requests.

**Test Scenarios:**

-   User logs in and requests the logout endpoint -> Status Code is 200 and
    tokens are blacklisted in SQL table
-   Request is sent to other endpoint with blacklisted access token -> Status
    code of 401 for unauthorized token
-   Request is sent to other endpoint with blacklisted refresh token -> Status
    code of 401 for unauthorized token

### **4. Add Visited Location**

-   **Description:** This test verifies that a user can add a visited location
    of their choosing and it will create a row in our Visit table. This table
    ties users to a location and does not allow duplicate rows. The Location
    table is populated during testing set up and a single user is created.

**Test Scenarios:**

-   User attempts to add a location as visited -> Status Code is 200 and row is
    created in Visit table
-   User attempts to add a non existent location as visited -> Status code of
    404
-   User attempts to add an already visited location as visited -> Status code
    of 200 and no action is taken

### **5. Delete Visited Location**

-   **Description:** This test verifies that a user can delete a visited
    location of their choosing and it will remove a row in our Visit table. This
    table ties users to a location and does not allow duplicate rows. The
    Location table is populated during testing set up and a single user is
    created.

**Test Scenarios:**

-   User attempts to delete a visited location -> Status Code is 200 and row is
    deleted in Visit table
-   User attempts to delete a non existent location -> Status code of 404 no
    action is taken

### **6. Return Visited Locations**

-   **Description:** This test verifies that the backend can retrieve a list of
    all locations that a user has visited.

**Test Scenarios:**

-   Valid user requests their visited locations -> Status code of 200 and a list
    of the Locations are returned

### **7. Retrieving Locations**

-   **Description:** This test verifies that the backend can retrieve a list of
    all locations and a specific location.

**Test Scenarios:**

-   Request all locations -> Status code of 200 and a list of the Locations are
    returned and length is verified
-   Request specific location -> Status code of 200 and location is returned
-   Request non existent specific location -> Status code of 404 and no action
    is taken
