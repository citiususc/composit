#!/bin/bash

echo "Auto-deploying artifact..."
echo "Current branch: $TRAVIS_BRANCH"

if [ "$TRAVIS_REPO_SLUG" == "citiususc/composit" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Running mvn deploy..."
  mvn deploy --settings .config/maven-settings.xml -DskipTests=true
else
  echo "Skipping deployment for this build..."
fi

echo "Deployment script finished."
