# Deployment

The backend is deployed on an AWS EC2 instance. We are using Docker Compose to orchestrate two containers running on the instance.

1. PostgreSQL database
2. Gunicorn / Flask application

## EC2 Instance Configuration

To configure the EC2 instance, we are using Ansible to run the playbook given in the `/api/ansible/` directory. This playbook ensures that software like NGINX, git, and docker is installed on the instance.

## Next Steps

The following steps are done by using `ssh` to connect to the EC2 instance remotely. Through the AWS console, we have ensured that only certain IP addresses can connect through `ssh`.

#### NGINX

We have configured NGINX on our EC2 instance to serve HTTPS traffic with an SSL certificate for `api.uahistoricalsites.com`

#### Code Deployment

Currently, we have to manually use git to retrieve our repository from github and run docker compose commands to restart the containers.
