# Config Manager Api

## Overview
Rest Application that provides access to endpoints in order to store and provides access to configuration entries.
 
## Requierments
- Docker 1.7
- Java 1.8

## Build and Run docker image

In order to build and image use:
- ./gradlew buildDocker

if you want to push it
- docker push adolfoecs/config-manager:0.1.0-SNAPSHOT

In order to run the image use:

- docker run -p 8580 --name config-manager-instance1 -t adolfoecs/config-manager:0.1.0-SNAPSHOT