#!/bin/sh

# Exit immediately if a command fails
set -e

echo "Setting up repository connector.." 

curl -X PUT "http://manifoldcf:8345/mcf-api-service/json/repositoryconnections/stackOverFlowRepository" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryconnection": {
      "name": "stackOverFlowRepository",
      "class_name": "org.apache.manifoldcf.crawler.connectors.webcrawler.WebcrawlerConnector",
      "description": "Seed data from Stack Overflow tag java",
      "max_connections": "10",
       "configuration": {
        "_PARAMETER_": [
          {
            "_attribute_name": "Email address",
            "_value": "test@example.com"
          },
          {
            "_attribute_name": "Seed URLs",
            "_value": "https://stackoverflow.com/questions/tagged/java"
          },
          {
            "_attribute_name": "Include URLs",
            "_value": "https://stackoverflow.com/questions/tagged/java.*"
          }
        ]
      }
    }
  }'

