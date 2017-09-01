Copy the id_rsa and create authorized_keys to be able to log in to the docker instance
The database is updated according to the latest mindlevel_schema.sql in the release branch
The project is built and generates slick entries from a local database
Once the container is run on the EC2 instance it will first update the RDS DB and then run
the precompiled fat jar.

- build-and-push.sh precompiles the project and pushes the docker container to the AWS repo
- runner.sh is used to make database adjustments from EC2 to RDS and then run the backend
