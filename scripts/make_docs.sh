#!/bin/bash
set -e

# Clean any previous Dokka sources
rm -rf ./docs/api

# Build bew
./gradlew clean dokkaGenerate
