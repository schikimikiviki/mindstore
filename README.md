# Mindstore

Search engine application that allows searching for IT terms and topics and adding new files to the data store.

## Used technologies

- Angular, TypeScript
- Docker
- Java
- Spring Boot, Spring Security
- Postgres
- JWT Authentication (HTTP only Cookie)
- Opensearch

## Data

![Image](https://github.com/user-attachments/assets/3c3477a8-99f1-4513-9d51-c764674fdf69)

## Info for deployed version

the data in opensearch is  not persisted yet. Make sure to copy the contents before restarting: 

```
curl -X GET "http://localhost:9200/text-index/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match_all": {}
  },
  "size": 10000
}
' | jq '[.hits.hits[]._source]'
```

## Deployment

1. Run the following command to set up the database:

```
docker compose up --build -d postgres
```

2. Run the following command to start all services: 

```
docker compose up -d 
```

3. The opensearch dashboard can be found at: 

```
http://localhost:5601
```

4. The Apache Manifold UI can be found at: 

```
http://localhost:8345/mcf-crawler-ui/
```

## Development

1. Start the database and the opensearch services with docker using: 

```
docker compose up manifoldcf opensearch postgres opensearch-dashboards opensearch-init -d
```

2. In the backend, change properties to "localhost" in the application.properties.
This is important for the opensearch URL and the postgres URL. 

3. You can start the frontend with IntelliJ.
