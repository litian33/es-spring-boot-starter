package cn.yzf.pass.es.starter.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "es.client")
public class ESProperties {
    private boolean enabled;

    private List<String> hosts;

    @NestedConfigurationProperty
    private ClientProperteis config = new ClientProperteis();

    @NestedConfigurationProperty
    private RequestProperties request = new RequestProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public ClientProperteis getConfig() {
        return config;
    }

    public void setConfig(ClientProperteis config) {
        this.config = config;
    }

    public RequestProperties getRequest() {
        return request;
    }

    public void setRequest(RequestProperties request) {
        this.request = request;
    }
}
