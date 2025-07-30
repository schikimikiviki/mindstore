#!/bin/sh

# Exit immediately if a command fails
set -e

echo "Setting up output connection ..."

curl -X PUT "http://localhost:8345/mcf-api-service/json/outputconnections/OpenSearchOutputConnection" \
  -H "Content-Type: application/json" \
  -d '{
    "outputconnection": {
      "name": "OpenSearchOutputConnection",
      "class_name": "org.apache.manifoldcf.agents.output.elasticsearch.ElasticSearchConnector",
      "description": "Send to OpenSearch",
      "max_connections": "10",
      "configuration": {
        "param": [
          { "name": "SERVERLOCATION", "value": "http://opensearch:9200" },
          { "name": "USERNAME", "value": "admin" },
          { "name": "PASSWORD", "value": "changeme" },
          { "name": "INDEXNAME", "value": "stackoverflow-java" },
          { "name": "INDEXTYPE", "value": "_doc" },
          { "name": "CONTENTATTRIBUTENAME", "value": "content" },
          { "name": "URIATTRIBUTENAME", "value": "url" },
          { "name": "CREATEDDATEATTRIBUTENAME", "value": "created" },
          { "name": "MODIFIEDDATEATTRIBUTENAME", "value": "last-modified" },
          { "name": "INDEXINGDATEATTRIBUTENAME", "value": "indexed" },
          { "name": "MIMETYPEATTRIBUTENAME", "value": "mime-type" },
          { "name": "USEINGESTATTACHMENT", "value": "false" },
          { "name": "USEMAPPERATTACHMENTS", "value": "false" },
          { "name": "ELASTICSEARCH_SOCKET_TIMEOUT", "value": "900000" },
          { "name": "ELASTICSEARCH_CONNECTION_TIMEOUT", "value": "60000" }
        ]
      }
    }
  }'

