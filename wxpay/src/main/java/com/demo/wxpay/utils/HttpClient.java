package com.demo.wxpay.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.Serializable;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @Package: com.demo.wxpay.pojo
 * @author: jt
 * @date: 2021-08-31   14:50
 * @Description:
 */
public class HttpClient implements Serializable {
    private String url;
    private Map<String, String> paramMap;
    private Integer statusCode;
    private String content;
    private String xmlParam;
    private Boolean isHttps;

    public HttpClient() {
    }

    public HttpClient(String url) {
        this.url = url;
    }

    public HttpClient(String url, Map<String, String> paramMap) {
        this.url = url;
        this.paramMap = paramMap;
    }

    public Boolean getHttps() {
        return isHttps;
    }

    public void setXmlParam(String xmlParam) {
        this.xmlParam = xmlParam;
    }

    public String getXmlParam() {
        return xmlParam;
    }

    public void setHttps(Boolean https) {
        isHttps = https;
    }

    /**
     * @param map
     */
    public void setParameter(Map<String, String> map) {
        paramMap = map;
    }

    /**
     * @param key
     * @param value
     */
    public void addParameter(String key, String value) {
        if (null == paramMap) {
            paramMap = new HashMap<String, String>();
            paramMap.put(key, value);
        }
    }

    public void post() throws IOException {
        HttpPost http = new HttpPost(url);
        setEntity(http);
        execute(http);
    }

    public void put() throws IOException {
        HttpPut http = new HttpPut(url);
        setEntity(http);
        execute(http);
    }

    public void get() throws IOException {
        if (paramMap != null) {
            StringBuilder stringBuilder = new StringBuilder(this.url);
            boolean isFirst = true;
            Set<String> keys = paramMap.keySet();
            for (String key : keys) {
                if (isFirst) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append(key).append("=").append(paramMap.get(key));
            }
            this.url = stringBuilder.toString();
        }
        HttpGet http = new HttpGet(url);
        execute(http);
    }

    /**
     * set http post, put paramMap
     * @param http
     */
    private void setEntity(HttpEntityEnclosingRequestBase http) {
        if (paramMap != null) {
            LinkedList<NameValuePair> nvps = new LinkedList<>();
            Set<String> keys = paramMap.keySet();
            for (String key : keys) {
                // 参数
                nvps.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            // 设置参数
            http.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        }
        if (xmlParam != null) {
            http.setEntity(new StringEntity(xmlParam, Consts.UTF_8));
        }
    }

    /**
     * @param http
     * @throws IOException
     */
    private void execute(HttpUriRequest http) throws IOException {
        CloseableHttpClient httpClient = null;
        try {
            if (isHttps) {
                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustStrategy() {
                            @Override
                            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                return true;
                            }
                        }).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
                httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } else {
                httpClient = HttpClients.createDefault();
            }
            CloseableHttpResponse response = httpClient.execute(http);

            try {
                if (response != null) {
                    if (response.getStatusLine() != null) {
                        statusCode = response.getStatusLine().getStatusCode();
                    }
                    HttpEntity entity = response.getEntity();
                    // 相应内容
                    content = EntityUtils.toString(entity, Consts.UTF_8);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

}
