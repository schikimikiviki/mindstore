#!/bin/sh

# Exit immediately if a command fails
set -e

echo "Reindexing..."
curl -X POST "http://localhost:9200/_reindex" -H "Content-Type: application/json" -d '
{
  "source": {
    "index": "text-index-backup"
  },
  "dest": {
    "index": "text-index"
  }
}'

