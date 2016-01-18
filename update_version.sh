#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Invalid number of arguments"
    echo "USAGE: $0 <version>"
    exit 1
fi

version=$1
working_dir="$( pwd )"
files=( "pom.xml" "README.md" )

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $script_dir/functions.sh

if ! ( contains_files ${files[@]} )
then
    working_dir=$working_dir/..
    cd $working_dir
fi

if ! ( contains_files ${files[@]} )
then
    echo "Some of the required files (${files[@]}) not found, aborting version bump"
    exit 1
fi

echo "Updating version to $version in all poms"
echo
mvn versions:set -DnewVersion=$version
mvn versions:commit > /dev/null
echo

STATUS=$?
if [[ $STATUS != 0 ]]; then
    echo "Version update failed with mvn status '$STATUS'"; exit $STATUS
else
    echo "Version update done"
fi

if [[ "$version" != *-SNAPSHOT ]]; then
    echo "Replacing version numbers in readme"
    sed -n -i '1h;1!H;${;g;s,<version>[^<]*</version>,<version>'"$version"'</version>,g;p;}' README.md
fi

echo "Committing version changes"
git add -A
git commit -m "Updated to version ${version}"

if [[ "$version" != *-SNAPSHOT ]]; then
    echo "Tagging the release in git"
    git tag -a release-${version} -m "Release version ${version}"
fi
