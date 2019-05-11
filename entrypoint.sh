#!/usr/bin/env bash
set -e

if [ -z ${CRED+x} ]; then
    echo "No authentication has been set, you might run into errors...";
else
    echo "Authentication using $CRED";

    if [ $CRED = "username" ];
    then
        sh -c "jfrog rt config --interactive=false --enc-password=true --url=$URL --user=$USER --password=$PASSWORD"
    elif [ $CRED = "apikey" ];
    then
        sh -c "jfrog rt config --interactive=false --enc-password=true --url=$URL --apikey=$APIKEY"
    elif [ $CRED = "accesstoken" ];
    then
        sh -c "jfrog rt config --interactive=false --enc-password=true --url=$URL --access-token=$ACCESSTOKEN"
    else
        echo "";
    fi
fi

for cmd in "$@"; do
    echo "Running '$cmd'..."
    if [ "$cmd" != "-v" ]; then
        if sh -c "jfrog rt $cmd"; then
            # no op
            echo "Successfully ran '$cmd'"
        else
            exit_code=$?
            echo "Failure running '$cmd', exited with $exit_code"
            exit $exit_code
        fi
    else
        sh -c "jfrog $cmd"
        echo "Successfully ran '$cmd'"
    fi
done

