# Server

We are using Flask for our backend.

## Deployment

- We are using AWS Elastic Beanstalk to deploy our web server. 
    - [Elastic Beanstalk Webserver Environments](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/concepts-webserver.html)

- We have registered the domain name uahistoricalsites.com.

- We upload our docker container to AWS to be automatically deployed.
    - [Elastic Beanstalk CLI](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3.html)


### Elastic Beanstalk Details

1. The subdomain api.uahistoricalsites.com routes requests to the load balancer for our environment. A load balancer is designed to split traffic across multiple instances (servers), but we have AWS configured to only ever use 1 instance.

2. Elastic Beanstalk handles the deployment of the application software onto an AWS EC2 instance, with software to run the web server environment. For our application, this software includes
    - Docker
        - Runs the container
    - nginx
        - Serves as a proxy server
    - AWS Host Manager
        - Deploys the application

        - Monitors the application log files for critical errors

        - Monitors the application server

        - Patches instance components

        - Rotates application's log files and publishing them to Amazon S3



## API Routes

#### Authentication
  - [Login User](API%20Routes/Auth/Login%20User.md)
  - [Register User](API%20Routes/Auth/Register%20User.md)
  - [Refresh Access Token](API%20Routes/Auth/Refresh%20Access%20Token.md)

#### Location
- [Get Locations](API%20Routes/Locations/Get%20Locations.md)

#### User
- [Add Visited Location](API%20Routes/User/Add%20Visited%20Location.md)
- [Delete Visited Location](API%20Routes/User/Delete%20Visited%20Location.md)
- [Get Visited Locations](API%20Routes/User/Get%20Visited%20Locations.md)
