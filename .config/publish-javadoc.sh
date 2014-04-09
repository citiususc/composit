#!/bin/bash

echo -e "Preparing JavaDoc auto-deploy...\n"
echo -e "TRAVIS_REPO_SLUG=$TRAVIS_REPO_SLUG - TRAVIS_JDK_VERSION=$TRAVIS_JDK_VERSION - TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST\n"

if [ "$TRAVIS_REPO_SLUG" == "citiususc/composit" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  FOLDER=$TRAVIS_BRANCH
  
  if [ "$FOLDER" = "master" ]; then
    FOLDER="latest"
  fi
  
  echo -e "Deploying [$FOLDER] ComposIT JavaDoc to GitHub gh-pages\n"

  # Copy the build folder with the javadoc to the corresponding folder
  cp -R build/citiususc/composit/target/apidocs/ $HOME/javadoc/$FOLDER/

  # Config git with the travis account
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  
  cd $HOME
  git clone --quiet --branch=gh-pages https://${GITHUB_TOKEN}@github.com/citiususc/composit gh-pages > /dev/null
  cd gh-pages
  rm -rf ./javadoc/$FOLDER/
  cp -R $HOME/javadoc/$FOLDER ./javadoc/$FOLDER
  git add .
  git commit -a -m "auto-commit $TRAVIS_BRANCH ComposIT JavaDoc (build $TRAVIS_BUILD_NUMBER)"
  git push -q origin gh-pages > /dev/null
  echo -e "Published $TRAVIS_BRANCH JavaDoc to gh-pages.\n"
  
fi 
