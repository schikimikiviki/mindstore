package com.mindstore.backend;

import org.junit.jupiter.api.Test;
import org.opensearch.spring.boot.autoconfigure.OpenSearchClientAutoConfiguration;
import org.opensearch.spring.boot.autoconfigure.data.OpenSearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = {
		ElasticsearchDataAutoConfiguration.class,
		ElasticsearchClientAutoConfiguration.class,
		OpenSearchClientAutoConfiguration.class,
		OpenSearchDataAutoConfiguration.class
})
class BackendApplicationTests {
	@Test
	void contextLoads() {
	}
}
