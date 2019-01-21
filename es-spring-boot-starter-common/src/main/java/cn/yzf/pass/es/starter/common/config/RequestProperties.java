package cn.yzf.pass.es.starter.common.config;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

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
        if (headers == null || headers.isEmpty()){
            return null;
        }

        Header[] defaultHeaders = new Header[headers.size()];
        int headerIndex = 0;
        for(int i=0;i<headers.size();i++){
            String headerInfo = headers.get(i);
            int idx = headerInfo.indexOf(':');
            if(idx > 0 && idx < headerInfo.length()-1){
                String key = headerInfo.substring(0, idx);
                String value = headerInfo.substring(idx+1);
                defaultHeaders[headerIndex++] = new BasicHeader(key, value);
            }
        }
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
