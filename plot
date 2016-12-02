#!/bin/bash

if [ ! -f bin/Evaluator.jar ]; then
    make evaluator > /dev/null
fi

java -jar bin/Evaluator.jar "$@"
