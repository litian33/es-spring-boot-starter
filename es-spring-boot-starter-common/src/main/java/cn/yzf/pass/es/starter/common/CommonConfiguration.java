package cn.yzf.pass.es.starter.common;

import cn.yzf.pass.es.starter.common.config.ClientProperteis;
import cn.yzf.pass.es.starter.common.config.ESProperties;
import cn.yzf.pass.es.starter.common.config.RequestProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.MessageFormat;
import java.util.List;

public class CommonConfiguration {
    private final Logger logger = LoggerFactory.getLogger(CommonConfiguration.class);
    private RestClientBuilder builder = null;

    public CommonConfiguration(ESProperties prop){
        if (!prop.isEnabled()) {
            return;
        }

        logger.info("Initializing ElasticSearch client");
        long beginTime = System.currentTimeMillis();

        List<String> hosts = prop.getHosts();
        if (hosts == null || hosts.size() == 0) {
            logger.warn("no elasticsearch host configured! use localhost as default");
            builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        } else {
            HttpHost[] httpHosts = new HttpHost[hosts.size()];
            for (int i = 0; i < hosts.size(); i++) {
                httpHosts[i] = HttpHost.create(hosts.get(i));
            }
            builder = RestClient.builder(httpHosts);
        }

        setClientProperties(prop, builder);
        setRequestProperties(prop, builder);

        logger.info(MessageFormat.format(
                "Initialized ElasticSearch client in {0}ms",
                System.currentTimeMillis() - beginTime));
    }

    public RestClientBuilder getBuilder(){
        return builder;
    }

    private void setRequestProperties(ESProperties prop, RestClientBuilder builder) {
        RequestProperties req = prop.getRequest();

        // 设置默认请求头
        if (req.getDefaultHeaders() != null) {
            builder.setDefaultHeaders(req.getDefaultHeaders());
        }

        if (req.getMaxRetryTimeout() > 0) {
            builder.setMaxRetryTimeoutMillis(req.getMaxRetryTimeout());
        }

        if (req.getSocketTimeout() > 0 || req.getConnectTimeout() > 0) {
            final int socketTimeout = req.getSocketTimeout();
            final int connectTimeout = req.getConnectTimeout();

            builder.setRequestConfigCallback(
                    new RestClientBuilder.RequestConfigCallback() {
                        public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                            if (socketTimeout > 0) {
                                requestConfigBuilder.setSocketTimeout(socketTimeout);
                            }
                            if (connectTimeout > 0) {
                                requestConfigBuilder.setSocketTimeout(connectTimeout);
                            }
                            return requestConfigBuilder;
                        }
                    });
        }
    }

    private void setClientProperties(final ESProperties prop, RestClientBuilder builder) {

        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            public HttpAsyncClientBuilder customizeHttpClient(
                    HttpAsyncClientBuilder httpClientBuilder) {
                ClientProperteis client = prop.getConfig();
                String proxy = client.getProxy();
                int threadCount = client.getThreadCount();
                ClientProperteis.AuthProperties authInfo = client.getAuthInfo();
                ClientProperteis.KeyStoreProperties keyStore = client.getKeyStore();

                if (!StringUtils.isEmpty(proxy)) {
                    httpClientBuilder.setProxy(
                            HttpHost.create(proxy));
                }
                if (threadCount > 0) {
                    httpClientBuilder.setDefaultIOReactorConfig(
                            IOReactorConfig.custom()
                                    .setIoThreadCount(threadCount)
                                    .build());
                }

                if (authInfo.valid()) {
                    setBasicAuthInfo(httpClientBuilder, authInfo);
                }

                if (keyStore.valid()) {
                    setSSLContext(httpClientBuilder, keyStore);
                }

                return httpClientBuilder;
            }

            private void setBasicAuthInfo(HttpAsyncClientBuilder httpClientBuilder, ClientProperteis.AuthProperties authInfo) {
                final CredentialsProvider credentialsProvider =
                        new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(authInfo.getUser(), authInfo.getUser()));

//                httpClientBuilder.disableAuthCaching();
                httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider);
            }

            private void setSSLContext(HttpAsyncClientBuilder httpClientBuilder, ClientProperteis.KeyStoreProperties keyStore) {
                SSLContextBuilder sslBuilder = SSLContexts.custom();
                if (!StringUtils.isEmpty(keyStore.getType())){
                    sslBuilder.setKeyStoreType(keyStore.getType());
                }
                try{
                    File f = new ClassPathResource(keyStore.getPath()).getFile();
                    if (StringUtils.isEmpty(keyStore.getStorePass())) {
                        sslBuilder.loadTrustMaterial(f);
                    }else{
                        sslBuilder.loadTrustMaterial(f, keyStore.getStorePass().toCharArray());
                    }
                }catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException ex){
                    logger.error(MessageFormat.format("load keyStore file {0} error", keyStore.getPath()), ex);
                    return;
                }

                try {
                    SSLContext sslContext = sslBuilder.build();
                    httpClientBuilder.setSSLContext(sslContext);
                } catch (KeyManagementException | NoSuchAlgorithmException e) {
                    logger.error("initialize sslContext error",e);
                }
            }
        });
    }

}
