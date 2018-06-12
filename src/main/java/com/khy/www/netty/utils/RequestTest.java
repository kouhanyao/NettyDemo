package com.khy.www.netty.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class RequestTest {
    public static void main(String[] args) {
        RequestTest requestTest = new RequestTest();
        //发送json数据
        String result = null;
        /*result = requestTest.postJson("http://127.0.0.1:1201/request/one", "{\"name\":\"kouhanyao\",\"age\":\"32\",\"id\":\"1\",\"love\":[{\"sex\":\"woman\",\"play\":\"soccer\"}]}");
        System.out.println(result);*/

        Map<String, String> params = new HashMap<>();
        params.put("applyId", "D201806081409170003");
        /*params.put("age", "32");
        params.put("id", "1");
        params.put("love", "[{\"sex\":\"woman\",\"play\":\"soccer\"}]");*/
        result = requestTest.httpPost("http://daren8.vicp.cc:9001/pipegold/mobile", params);
        System.out.println(result);
    }

    /**
     * Post发送json数据
     *
     * @param url
     * @param json
     * @return
     */
    public String postJson(String url, String json) {
        try {
            CloseableHttpClient httpClient = null;
            CloseableHttpResponse response = null;
            try {
                httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(json, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                response = httpClient.execute(httpPost);
                return EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String httpPost(String url, Map<String, String> paramsMap) {
        return httpPost(url, paramsMap, null);
    }

    /**
     * Post发送form表单数据
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public String httpPost(String url, Map<String, String> paramsMap,
                           Map<String, String> headMap) {
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpPost httpPost = new HttpPost(url);
                setPostHead(httpPost, headMap);
                setPostParams(httpPost, paramsMap);
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, Charset.forName("utf-8"));
                return result;
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置POST的参数
     *
     * @param httpPost
     * @param paramsMap
     * @throws Exception
     */
    private void setPostParams(HttpPost httpPost, Map<String, String> paramsMap)
            throws Exception {
        // 创建一个提交数据的容器
        if (paramsMap != null && paramsMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> keySet = paramsMap.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        }
    }

    /**
     * 设置http的HEAD
     *
     * @param httpPost
     * @param headMap
     */
    private void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            Set<String> keySet = headMap.keySet();
            for (String key : keySet) {
                httpPost.addHeader(key, headMap.get(key));
            }
        }
    }
}
