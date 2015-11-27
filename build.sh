#!/usr/bin/env bash

#set -e -u

if [ "${TRAVIS_BRANCH}" == "master" ]; then
    MVN_CMD="mvn clean install deploy --quiet --settings travis-settings.xml -Pbuild-release -B"
else
    MVN_CMD="mvn clean test -Pbuild-test -B"
fi

${MVN_CMD} -am -pl guava -Dguava.version=15.0 && \
#${MVN_CMD} -am -pl guava -Dguava.version=16.0 && \
#${MVN_CMD} -am -pl guava -Dguava.version=17.0 && \
#${MVN_CMD} -am -pl guava -Dguava.version=18.0 && \
${MVN_CMD} -am -pl time -Djoda-time.version=2.1 && \
#${MVN_CMD} -am -pl time -Djoda-time.version=2.3 && \
#${MVN_CMD} -am -pl time -Djoda-time.version=2.9.1 && \
${MVN_CMD} -am -pl junit -Djunit.version=4.10 && \
#${MVN_CMD} -am -pl junit -Djunit.version=4.11 && \
#${MVN_CMD} -am -pl junit -Djunit.version=4.12 && \
${MVN_CMD} -am -pl hamcrest -Dhamcrest.version=1.3 && \
${MVN_CMD} -am -pl mockito -Dmockito.version=1.10.19

if [ "${TRAVIS_REPO_SLUG}" == "pawelprazak/java-extended" ] && \
   [ "${TRAVIS_JDK_VERSION}" == "oraclejdk7" ] && \
   [ "${TRAVIS_PULL_REQUEST}" == "false" ] && \
   [ "${TRAVIS_JOB_NUMBER}" == "${TRAVIS_BUILD_NUMBER}.1" ] && \
   [ "${TRAVIS_BRANCH}" == "master" ]; then
  echo "Generating Coverity Report..."

  test $(mvn com.coverity:ondemand-maven-plugin:1.4.782:check) != 0 && \
         mvn clean test -Pbuild-test jacoco:report coveralls:report

  echo "Generated Coverity Report."
fi