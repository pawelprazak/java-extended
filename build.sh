#!/usr/bin/env bash

abort()
{
    echo >&2 '
***************
*** ABORTED ***
***************
'
    echo "An error occurred. Exiting..." >&2
    exit 1
}

trap 'abort' 0

set -e -u

LINE="---------------------------------------------------------------------------------------------------------------"

echo "Building branch: '${TRAVIS_BRANCH}', tag: '${TRAVIS_TAG}'"

if [[ "${TRAVIS_BRANCH}" =~ ^release.* ]] || [[ "${TRAVIS_TAG}" =~ ^release.* ]]; then
    MVN_CMD="mvn deploy --settings travis-settings.xml -Pbuild-release -B"
else
    MVN_CMD="mvn test -Pbuild-test -B"
fi

${MVN_CMD} -am -pl guava -Dguava.version=15.0
${MVN_CMD} -am -pl guava -Dguava.version=16.0
${MVN_CMD} -am -pl guava -Dguava.version=17.0
${MVN_CMD} -am -pl guava -Dguava.version=18.0
${MVN_CMD} -am -pl time -Djoda-time.version=2.1
${MVN_CMD} -am -pl time -Djoda-time.version=2.3
${MVN_CMD} -am -pl time -Djoda-time.version=2.9.1
${MVN_CMD} -am -pl junit -Djunit.version=4.10
${MVN_CMD} -am -pl junit -Djunit.version=4.11
${MVN_CMD} -am -pl junit -Djunit.version=4.12
${MVN_CMD} -am -pl hamcrest -Dhamcrest.version=1.3
${MVN_CMD} -am -pl mockito -Dmockito.version=1.10.19

if [ "${TRAVIS_REPO_SLUG}" == "pawelprazak/java-extended" ] && \
   [ "${TRAVIS_JDK_VERSION}" == "oraclejdk7" ] && \
   [ "${TRAVIS_PULL_REQUEST}" == "false" ] && \
   [ "${TRAVIS_JOB_NUMBER}" == "${TRAVIS_BUILD_NUMBER}.1" ] && \
   [ "${TRAVIS_BRANCH}" == "master" ]; then

  echo ${LINE}
  echo
  mvn clean test jacoco:report -Pbuild-test,enable-mutation-tests -B
  echo
  echo ${LINE}
fi

trap : 0

echo >&2 '
************
*** DONE ***
************
'