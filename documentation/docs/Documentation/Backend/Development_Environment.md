## Development Requirements

- Poetry
- Docker Compose
- Ansible (for AWS EC2 configuration)

## Setup

You must create a `.env` file, even if it is empty in the `api` directory.

### S3 Setup

Currently, this only needed for uploading a photo during location suggestion creation.

A `.env` file is required in the root of the project (in the .api folder next to .env) with the following variables:

```
AWS_ACCESS_KEY_ID=<access_key>
AWS_SECRET_KEY=<secret_key
```

### Postmark / Email Confirmation Setup

Currently, this only needed for uploading a photo during location suggestion creation.

A `.env` file is required in the root of the project (in the .api folder next to .env) with the following variables:

```
POSTMARK_API_KEY=<postmark_api_key>
```


## Development commands

These commands are for a UNIX environment, some have a Windows equivalent in the `Makefile`.

#### Run

`make docker-dev-run`

#### Clean 

`make docker-dev-clean`

#### Test

`make docker-ci-test`

#### Format 

`make format`

#### Lint 

`make lint`

## Misc 

#### Ansible things

To deploy the website on an EC2 server.

- need to install ansible
- run the playbook with 
    - `ansible-playbook -i ansible/inventory.ini ansible/ec2_setup.yml --private-key=<path>.pem --user=ubuntu`
    - Need to create the `inventory.ini` file with appropriate `ec2_instance` variable