# MindLevel Backend
This is the backend for the MindLevel project.

The android app can be found here: https://github.com/spydon/mindlevel3

## Description
REST API built with Slick and Akka on top of MariaDB.

To run as a container check the [Container Readme](container/readme.txt)

## Hacks
Since the table generation isn't flawless and we need to support multiple databases from the same table classes you have
to run this after the generation `%s/Some("mindlevel")/None`.