Copy the id_rsa and create authorized_keys to be able to log in to the docker instance

- build-and-push.sh precompiles the project and pushes the docker container to the AWS repo
- runner.sh is used to make database adjustments from EC2 to RDS and then run the backend
