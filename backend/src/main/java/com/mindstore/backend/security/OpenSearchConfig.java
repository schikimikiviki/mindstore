package com.mindstore.backend.security;

import org.apache.http.HttpHost;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.opensearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * Configuration file for opensearch that uses the opensearch.host from application.properties
 * creates openSearchClient that the application uses
 */
@Configuration
    public class OpenSearchConfig {

        @Value("${opensearch.host}")
        private String openSearchHost;


        @Bean
        public OpenSearchClient openSearchClient() {
            // Create the low-level client with sniffing disabled

            URI uri = URI.create(openSearchHost);  //  parse full URI

            RestClient restClient = RestClient.builder(
                            new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme())
                    )
                    .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                    )
                    .setHttpClientConfigCallback(httpClientBuilder -> {
                        httpClientBuilder.disableConnectionState();
                        return httpClientBuilder;
                    })
                    .build();

            // Create the transport
            OpenSearchTransport transport = new RestClientTransport(
                    restClient,
                    new JacksonJsonpMapper()
            );

            return new OpenSearchClient(transport);
        }

}