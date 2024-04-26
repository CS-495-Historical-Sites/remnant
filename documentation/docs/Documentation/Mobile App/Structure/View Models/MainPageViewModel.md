## View Model for the Main Page

### Overview

The `viewmodels/MainPageViewModel` directory contains a view model for handling the categories on the main page. It has a list of categories which can filter nearby locations.

### View Model Details

- **_categoriesState**

  - Stores a list of categories used for filtering.


- **updateCategories**
  - Used to update filters when categories have been changed.

- **filterClusterItems**
  - Filters nearby sites according to selected categories.

- **hasUserVisitedLocation**
   - Sends a get request to the backend to obtain visisted location data.