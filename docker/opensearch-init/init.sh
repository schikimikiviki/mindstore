#!/bin/sh

# Exit immediately if a command fails
set -e

echo "Deleting existing 'text-index' if it exists..."
curl -X DELETE "http://opensearch:9200/text-index"

echo "Creating 'text-index' with mappings and analyzers..."
curl -X PUT "http://opensearch:9200/text-index" \
  -H "Content-Type: application/json" \
  --data-binary @./recreateIndex.json
