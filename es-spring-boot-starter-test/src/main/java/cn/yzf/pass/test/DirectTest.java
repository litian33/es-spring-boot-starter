package cn.yzf.pass.test;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class DirectTest {
    public static void main(String[] args) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "elastic"));

        RestClientBuilder builder = RestClient.builder(new HttpHost("127.0.0.1", 9200,"https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

                        String path="/home/litian/program/elasticsearch-6.5.4/config/certs/elastic-certificates.p12";
//                        KeyStore truststore = null;
//                        try {
//                            truststore = KeyStore.getInstance("jks");
//                        } catch (KeyStoreException e) {
//                            e.printStackTrace();
//                        }
//
//                        InputStream is = null;
//                        try {
//                            is = new FileInputStream(path);
//                            truststore.load(is, "elastic".toCharArray());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }finally {
//                            try {
//                                is.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        SSLContext sslContext = null;
//                        try {
//                            SSLContextBuilder sslBuilder = SSLContexts.custom()
//                                    .loadTrustMaterial(truststore, null);
//                            sslContext = sslBuilder.build();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        httpClientBuilder.setSSLContext(sslContext);
                        try {
                            SSLContext sslContext = SSLContexts.custom()
                                    .loadTrustMaterial(new File(path), "elastic".toCharArray())
                                    .build();
                            httpClientBuilder.setSSLContext(sslContext);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                        } catch (KeyStoreException e) {
                            e.printStackTrace();
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return httpClientBuilder;
                    }
                });

        RestClient restClient = builder.build();


        Request request = new Request(
                "GET",
                "/_cat/health?v");
        try {
            Response response = restClient.performRequest(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
