FROM digitalgenius/alpine-scala-sbt:latest

RUN apk update
RUN apk upgrade
RUN apk add git

# Add private key to be able to clone from git
ADD /home/spydon/.ssh/id_rsa /root/.ssh/id_rsa
RUN touch /root/.ssh/known_hosts
RUN ssh-keyscan github.com >> /root/.ssh/known_hosts
RUN git clone git@github.com:spydon/mindlevel-backend.git

# Set the working directory to /app
WORKDIR /mindlevel-backend

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the scala app with sbt
CMD ["sbt", "run"]

