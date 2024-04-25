#### How to set up development environment

The project is built using React + TypeScript + Vite. Navigate to the `/website` directory to see the source code.

### Install dependencies

From inside the website directory, run `npm install` to install the project dependencies. [npm](https://www.npmjs.com/) is needed.

### Run

To run the website in development mode, run `npm run dev`.

### Build

To build the website, run `npm run build`. It will build the website to the `/site/` directory. You can 
then use these assets to deploy the site.


## Structure

- `/src/pages`
    - Each file is associated with a route.
- `/src/components`
    - Components to be reused
- `/src/RemnantAPI` 
    - Functions that call the backend


### User Management

User management is done using the same JWT token approach as the Android App.