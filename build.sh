#!/usr/bin/env bash

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $script_dir/functions.sh

trap 'abort' 0

set -e -u

echo "Building branch: '${TRAVIS_BRANCH}', tag: '${TRAVIS_TAG}'"

if [[ "${TRAVIS_BRANCH}" =~ ^release.* ]]; then
    MVN_CMD="mvn -B install jacoco:report -Pbuild-release,enable-unit-tests,enable-integration-tests deploy --settings travis-settings.xml -am"

    ${MVN_CMD} -pl junit -Djunit.version=4.12
    ${MVN_CMD} -pl junit -Djunit.version=4.11
    ${MVN_CMD} -pl junit -Djunit.version=4.10

    ${MVN_CMD} -pl hamcrest -Dhamcrest.version=1.3

    ${MVN_CMD} -pl mockito -Dmockito.version=1.10.19

    ${MVN_CMD} -pl time -Djoda-time.version=2.9.1
    ${MVN_CMD} -pl time -Djoda-time.version=2.3
    ${MVN_CMD} -pl time -Djoda-time.version=2.1

    ${MVN_CMD} -pl guava -Dguava.version=19.0
    ${MVN_CMD} -pl guava -Dguava.version=18.0
    ${MVN_CMD} -pl guava -Dguava.version=17.0
    ${MVN_CMD} -pl guava -Dguava.version=16.0.1
    ${MVN_CMD} -pl guava -Dguava.version=15.0

else
    mvn test jacoco:report -Pbuild-test -B
fi

trap : 0

echo >&2 '
************
*** DONE ***
************
'