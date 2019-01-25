package cn.yzf.pass.es.starter.lowlevel;

import cn.yzf.pass.es.starter.common.config.ClientProperteis;
import cn.yzf.pass.es.starter.common.config.ESProperties;
import cn.yzf.pass.es.starter.common.config.RequestProperties;
import org.apache.http.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties(ESProperties.class)
@SpringBootTest(classes = ConfigTest.class)
public class ConfigTest {

    @Autowired
    ESProperties esProperties;

    @Test
    public void testBasic(){
        assertThat(esProperties.isEnabled()).isTrue();

        List<String> hosts = esProperties.getHosts();
        assertThat(hosts).isNotEmpty();
        assertThat(hosts).hasSize(2);

        assertThat(hosts.get(0)).isEqualTo("http://localhost:9200");
        assertThat(hosts.get(1)).isEqualTo("https://localhost:9201");
    }

    @Test
    public void testRequest() {
        RequestProperties  req = esProperties.getRequest();

        assertThat(req).isNotNull();
        assertThat(req.getConnectTimeout()).isEqualTo(5000);
        assertThat(req.getMaxRetryTimeout()).isEqualTo(30000);
        assertThat(req.getSocketTimeout()).isEqualTo(30000);

        assertThat(req.getHeaders()).isNotEmpty();
        assertThat(req.getHeaders()).hasSize(2);

        Header[] headers = req.getDefaultHeaders();
        assertThat(headers).isNotNull();
        assertThat(headers).hasSize(2);
        assertThat(headers[0].getName()).isEqualTo("key1");
        assertThat(headers[0].getValue()).isEqualTo("value1");
        assertThat(headers[1].getName()).isEqualTo("key2");
        assertThat(headers[1].getValue()).isEqualTo("value2");
    }

    @Test
    public void testConfig(){
        ClientProperteis cp = esProperties.getConfig();

        assertThat(cp).isNotNull();
        assertThat(cp.getProxy()).isEqualTo("http://localhost:8080");
        assertThat(cp.getThreadCount()).isEqualTo(2);

        ClientProperteis.AuthProperties auth = cp.getAuthInfo();
        assertThat(auth).isNotNull();
        assertThat(auth.getUser()).isEqualTo("user1");
        assertThat(auth.getPasswd()).isEqualTo("pass");

        ClientProperteis.KeyStoreProperties kp = cp.getKeyStore();
        assertThat(kp).isNotNull();
        assertThat(kp.getType()).isEqualTo("jks");
        assertThat(kp.getPath()).isEqualTo("abc");
        assertThat(kp.getStorePass()).isEqualTo("password");
    }
}
