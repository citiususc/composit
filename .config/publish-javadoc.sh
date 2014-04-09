#!/bin/bash

echo "Preparing JavaDoc auto-deploy..."
echo "TRAVIS_REPO_SLUG=$TRAVIS_REPO_SLUG - TRAVIS_JDK_VERSION=$TRAVIS_JDK_VERSION - TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST"

if [ "$TRAVIS_REPO_SLUG" == "citiususc/composit" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  VERSION=$TRAVIS_BRANCH
  
  if [ "$VERSION" = "master" ]; then
    VERSION="latest"
  else
    wget https://raw.githubusercontent.com/citiususc/composit/$TRAVIS_BRANCH/pom.xml
    # Take the version from the main pom.xml
    VERSION=`grep -m 1 "<version>" pom.xml | cut -d ">" -f 2 | cut -d "<" -f 1`
    rm pom.xml
  fi
  
  echo "Deploying [$VERSION] ComposIT JavaDoc to GitHub gh-pages"
  echo "Current directory: `pwd`"

  # Copy the build folder with the javadoc to the corresponding folder
  mkdir $HOME/javadoc
  cp -R target/apidocs/ $HOME/javadoc/
  
  cd $HOME
  git clone --quiet --branch=gh-pages https://github.com/citiususc/composit.git gh-pages > /dev/null

  # Update the content of gh-pages
  cd gh-pages

  # Config git user and credentials
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git config credential.helper "store --file=.git/credentials"
  echo "https://${GITHUB_TOKEN}:@github.com" > .git/credentials

  rm -rf javadoc/$VERSION/
  cp -R $HOME/javadoc/ javadoc/$VERSION
  git add .
  git commit -a -m "auto-commit $TRAVIS_BRANCH ComposIT JavaDoc (build $TRAVIS_BUILD_NUMBER)"
  git push -q origin gh-pages > /dev/null
  echo "Published $TRAVIS_BRANCH JavaDoc to gh-pages."
  
fi 
