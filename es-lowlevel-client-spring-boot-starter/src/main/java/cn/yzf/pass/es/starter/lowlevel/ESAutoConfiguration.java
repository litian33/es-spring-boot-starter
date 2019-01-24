package cn.yzf.pass.es.starter.lowlevel;

import cn.yzf.pass.es.starter.common.CommonConfiguration;
import cn.yzf.pass.es.starter.common.config.ESProperties;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RestClient.class)
@EnableConfigurationProperties(ESProperties.class)
public class ESAutoConfiguration implements DisposableBean {
    private final Logger logger = LoggerFactory.getLogger(ESAutoConfiguration.class);

    private RestClient client;

    public ESAutoConfiguration(ESProperties prop) {
        RestClientBuilder builder = new CommonConfiguration(prop).getBuilder();
        if (builder != null){
            client = builder.build();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "es.client", value = "enabled", havingValue = "true")
    public RestClient restClient() {
        return client;
    }

    public void destroy() throws Exception {
        if (client != null) {
            logger.info("Closing ElasticSearch client");
            client.close();
            logger.info("ElasticSearch client was closed");
        }
    }
}
