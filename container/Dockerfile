FROM alpine:latest

RUN apk update
RUN apk upgrade
RUN apk add git mysql-client openssh openjdk8-jre-base

# Install scala
ENV SCALA_VERSION=2.12.3 \
    SCALA_HOME=/usr/share/scala

RUN apk add --no-cache --virtual=.build-dependencies wget ca-certificates && \
    apk add --no-cache bash && \
    cd "/tmp" && \
    wget "https://downloads.typesafe.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.tgz" && \
    tar xzf "scala-${SCALA_VERSION}.tgz" && \
    mkdir "${SCALA_HOME}" && \
    rm "/tmp/scala-${SCALA_VERSION}/bin/"*.bat && \
    mv "/tmp/scala-${SCALA_VERSION}/bin" "/tmp/scala-${SCALA_VERSION}/lib" "${SCALA_HOME}" && \
    ln -s "${SCALA_HOME}/bin/"* "/usr/bin/" && \
    apk del .build-dependencies && \
    rm -rf "/tmp/"*

# Add private key to be able to clone from git
RUN mkdir /root/.ssh
ADD id_rsa /root/.ssh/
ADD authorized_keys /root/.ssh/
RUN chmod 400 /root/.ssh/id_rsa
RUN touch /root/.ssh/known_hosts
RUN ssh-keyscan github.com >> /root/.ssh/known_hosts
RUN echo 1
ADD mindlevel-backend /root/mindlevel-backend

# Set the working directory to /app
WORKDIR /root/mindlevel-backend

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the scala app with sbt
CMD ["sh", "container/runner.sh"]
