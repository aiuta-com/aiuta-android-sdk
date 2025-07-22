#!/bin/bash

if [[ -z "$1" || -z "$2" ]]; then
  echo "Usage: $0 <file> <variable_key> [--remove-suffix]"
  exit 1
fi

# Path to file
FILE="$1"
VARIABLE_KEY="$2"

# Extract value
VALUE=$(grep "^$VARIABLE_KEY=" "$FILE" | cut -d'=' -f2-)
if [[ "$3" == "--remove-suffix" ]]; then
  VALUE="${VALUE%%-*}"
fi

echo "$VALUE"
