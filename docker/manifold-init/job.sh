#!/bin/sh

curl -X PUT "http://manifoldcf:8345/mcf-api-service/json/jobs/java-so-crawl-job" \
  -H "Content-Type: application/json" \
  -d '{
    "job": {
      "name": "Java StackOverflow Crawl",
      "description": "Crawl Java-tagged Stack Overflow pages",
      "repository_connection": "stackOverFlowRepository",
      "output_connection": "OpenSearchOutputConnection",
      "start_mode": "manual",
      "run_mode": "scan once",
      "hopcount_mode": "accurate",
      "documents": {
        "document_specification": {
          "nodes": []
        }
      },
      "pipeline": {
        "pipelinestages": []
      },
      "schedules": []
    }
  }'
