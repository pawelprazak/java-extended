#!/bin/bash

contains_files()
{
    for file in "$@"
    do
        if ! [ -f $file ]
        then
            return 1
        fi
    done
    return 0
}

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
