## GitHub caches a few Docker base images, one of them being the Debian Stretch image. 
## To speed up creation of docker images it is a good idea to use that as a base too.
FROM debian:stretch-slim

## A common name for your action
LABEL "name"="jfrog-cli"
## The name of the person who maintains the action.
LABEL "maintainer"="underwindfall"
## The version of your action
LABEL "version"="0.1.0"
## The name of the action as it will appear in the GitHub Actions UI
LABEL "com.github.actions.name"="JFrog CLI for GitHub Actions"
## A short description of the action
LABEL "com.github.actions.description"="Runs a JFrog CLI Command"
## The name of the Feather icon to use... (they don't have a frog :( )
LABEL "com.github.actions.icon"="compass"
## The background color used in the visual workflow editor for the action.
LABEL "com.github.actions.color"="blue"

## Make sure that the CLI doesn't offer to store credentials
ENV JFROG_CLI_OFFER_CONFIG=false
## Copy all required files over to the docker container
COPY LICENSE README.md THIRD_PARTY_NOTICE.md entrypoint.sh /
## Install cURL, get the JFrog CLI, and make the script executable
RUN apt-get update &&\
    apt-get install curl -y &&\
    rm -rf /var/lib/apt/lists/* &&\
    curl -fL https://getcli.jfrog.io | sh &&\
    mv jfrog /bin &&\
    chmod +x entrypoint.sh &&\
    mv entrypoint.sh /bin

## When the container starts, make sure it runs the script
ENTRYPOINT ["entrypoint.sh"]
