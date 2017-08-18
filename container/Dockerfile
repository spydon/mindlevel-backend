FROM digitalgenius/alpine-scala-sbt:latest

RUN apk update
RUN apk upgrade
RUN apk add git mysql-client openssh

# Add private key to be able to clone from git
RUN mkdir /root/.ssh
ADD id_rsa /root/.ssh/
ADD authorized_keys /root/.ssh/
RUN chmod 400 /root/.ssh/id_rsa
RUN touch /root/.ssh/known_hosts
RUN ssh-keyscan github.com >> /root/.ssh/known_hosts
RUN git clone git@github.com:spydon/mindlevel-backend.git

# Set the working directory to /app
WORKDIR /root/mindlevel-backend

# Setup database (make sure not to drop tables)
RUN mysql -uroot -ppassword -h mindlevel.cxkevz1h137d.eu-central-1.rds.amazonaws.com < mindlevel_schema.sql

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the scala app with sbt
CMD ["sbt", "-mem", "1000", "run"]
