#!/bin/bash

#This scripts builds the docker container and publish them to the remote image repository
VERSION=31
REGISTRY_URL=us-east4-docker.pkg.dev/gt-retail-324419/gtsoftware-repo

#Build and publish backend image
docker buildx build --platform linux/amd64 . -t $REGISTRY_URL/gtretail-backend-app:$VERSION
docker push $REGISTRY_URL/gtretail-backend-app:$VERSION

#Build and publish frontend image
docker buildx build --platform linux/amd64 webapp/. -t $REGISTRY_URL/gtretail-frontend-app:$VERSION
docker push $REGISTRY_URL/gtretail-frontend-app:$VERSION

echo "All done!"
