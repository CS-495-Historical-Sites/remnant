## Development commands

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

- need to install ansible
- run the playbook with 
    - `ansible-playbook -i ansible/inventory.ini ansible/ec2_setup.yml --private-key=<path>.pem --user=ubuntu`
    - Need to create the `inventory.ini` file with appropriate `ec2_instance` variable