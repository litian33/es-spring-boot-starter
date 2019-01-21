package cn.yzf.pass.es.starter.common.config;

import org.springframework.util.StringUtils;

public class ClientProperteis {
    // Client连接Server时使用的http代理信息
    private String proxy;

    // 网络请求处理线程数，默认和CPU内核数一致
    private int threadCount;

    // ES基础认证信息
    private AuthProperties authInfo = new AuthProperties();

    // 使用SSL链接时的证书信息
    private KeyStoreProperties keyStore = new KeyStoreProperties();

    public class AuthProperties {
        // 用户名
        private String user ;

        // 密码信息（BASE64编码）
        private String passwd;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public boolean valid(){
            return !(StringUtils.isEmpty(user) || StringUtils.isEmpty(passwd));
        }
    }

    public class KeyStoreProperties {
        // 证书类型
        private String type ;

        // 本地证书存放路径
        private String path;

        // 证书密码
        private String storePass;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getStorePass() {
            return storePass;
        }

        public void setStorePass(String storePass) {
            this.storePass = storePass;
        }

        public boolean valid(){
            return !(StringUtils.isEmpty(type) || StringUtils.isEmpty(path));
        }
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public AuthProperties getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthProperties authInfo) {
        this.authInfo = authInfo;
    }

    public KeyStoreProperties getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStoreProperties keyStore) {
        this.keyStore = keyStore;
    }
}


