package com.mall4j.cloud.biz.wx.wx.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
/**
 * @Date 2021年12月30日, 0030 14:55
 */
@Slf4j
public class WechatUtils {

    private static final String COMPONENT_ACCESS_TOKEN = "COMPONENT_ACCESS_TOKEN";

    /**
     * 获取授权token
     */
    public static String getAuthorizerAccessToken() {
        // 从缓存获取授权信息,使用授权码获取授权信息
        String authorizerAccessToken = "";
        return authorizerAccessToken;
    }

    /**
     * 替换请求URL的COMPONENT_ACCESS_TOKEN
     */
    public static String replaceComponentAccessToken(String url, String componentAccessToken) {
        if (StrUtil.isEmpty(componentAccessToken)) {
            return url;
        }
        return url.replaceAll(COMPONENT_ACCESS_TOKEN, componentAccessToken);
    }

    /**
     * 处理Get请求
     *
     * @param url
     * @return
     */
    public static JSONObject doGetstr(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                //jsonObject =JSONObject.fromObject(result);
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 处理Post请求
     *
     * @param url    请求url
     * @param outStr 请求字符串
     * @return JSONObject
     */
    public static JSONObject doPoststr(String url, String outStr) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        try {
            httpPost.setEntity(new StringEntity(outStr, "utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * xml转map
     *
     * @param xml 要转换的xml字符串
     * @return 转换成map后返回结果
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String xml) throws Exception {
        Map<String, String> respMap = new HashMap<>(16);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new ByteArrayInputStream(xml.getBytes("utf-8")));
        Element root = doc.getRootElement();
        xmlToMap(root, respMap);
        return respMap;
    }

    /**
     * xml转 WxMpXmlMessage
     * @param xml
     * @return
     */
    public static WxMpXmlMessage parseXmlMessage(String xml){
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(xml);
        return inMessage;
    }

    /**
     * 递归转换
     *
     * @param root Element
     * @param map  Map
     * @return
     */
    private static Element mapToXml(Element root, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Element element = root.addElement(entry.getKey());
                mapToXml(element, (Map<String, Object>) entry.getValue());
            } else {
                root.addElement(entry.getKey()).addText(entry.getValue().toString());
            }
        }
        return root;
    }

    /**
     * 递归转换
     *
     * @param tmpElement
     * @param respMap
     * @return
     */
    private static Map<String, String> xmlToMap(Element tmpElement, Map<String, String> respMap) {
        if (tmpElement.isTextOnly()) {
            respMap.put(tmpElement.getName(), tmpElement.getText());
            return respMap;
        }
        Iterator<Element> eItor = tmpElement.elementIterator();
        while (eItor.hasNext()) {
            Element element = eItor.next();
            xmlToMap(element, respMap);
        }
        return respMap;
    }

    /**
     * 将微信消息中的CreateTime转换成标准格式的时间（MM/dd/yyyy HH:mm:ss）
     *
     * @param createTime 消息创建时间
     * @return
     */
    public static String formatTime(String createTime) {
        // 将微信传入的CreateTime转换成long类型，再乘以1000
        long msgCreateTime = Long.parseLong(createTime) * 1000L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(msgCreateTime));
    }

    public static Date formatDate(String createTime) {
        if(StrUtil.isEmpty(createTime) || createTime.equals("0")){
            return null;
        }
        // 将微信传入的CreateTime转换成long类型，再乘以1000
        long msgCreateTime = Long.parseLong(createTime) * 1000L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date(msgCreateTime);
    }

    public static String repScene(Map<String, String> params,String scene){
        if(CollectionUtil.isNotEmpty(params)){
            StringBuffer sb=new StringBuffer();
            sb.append(scene);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String param=entry.getKey()+"="+entry.getValue();
                sb.append("&").append(param);
            }
            return sb.toString();
        }
        return scene;
    }
}
