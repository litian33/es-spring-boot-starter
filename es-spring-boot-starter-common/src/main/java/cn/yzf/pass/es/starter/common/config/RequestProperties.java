package cn.yzf.pass.es.starter.common.config;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class RequestProperties {
    // 默认请求头信息
    private List<String> headers = new ArrayList<String>();

    // 最大重试超时时间(多次重试累加计算)
    private int maxRetryTimeout;

    // socket链接超时时间
    private int socketTimeout;

    // 客户端链接超时时间
    private int connectTimeout;

    public List<String> getHeaders() {
        return headers;
    }

    public Header[] getDefaultHeaders() {
        if (headers == null || headers.isEmpty()) {
            return null;
        }

        Header[] defaultHeaders = headers.stream().filter(item -> item.indexOf(':') > 0 && item.indexOf(':') < item.length() - 1).map(item -> {
            int idx = item.indexOf(':');
            String key = item.substring(0, idx);
            String value = item.substring(idx + 1);
            return new BasicHeader(key, value);
        }).toArray(Header[]::new);

        return defaultHeaders;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public int getMaxRetryTimeout() {
        return maxRetryTimeout;
    }

    public void setMaxRetryTimeout(int maxRetryTimeout) {
        this.maxRetryTimeout = maxRetryTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
