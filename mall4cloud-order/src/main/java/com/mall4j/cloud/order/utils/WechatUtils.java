package com.mall4j.cloud.order.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Date 2021年12月30日, 0030 14:55
 * @Created by eury
 */
@Slf4j
public class WechatUtils {

    private static final String AUTHORIZER_ACCESS_TOKEN = "ACCESS_TOKEN";
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
     * 替换请求URL的AUTHORIZER_ACCESS_TOKEN
     */
    public static String replaceAuthorizerAccessToken(String url, String authorizerAccessToken) {
        return url.replaceAll(AUTHORIZER_ACCESS_TOKEN, authorizerAccessToken);
    }

    /**
     * 校验响应信息
     */
    public static boolean validWechatResult(JSONObject jsonObject) {
        if (jsonObject == null) {
            log.error("执行失败,响应结果为空,jsonObject:{}", jsonObject);
            return false;
        }

        int errcode = jsonObject.getIntValue("errcode");
        String errmsg = jsonObject.getString("errmsg");
        if (0 != errcode) {
            log.error("执行失败,响应信息 errcode:{} errmsg:{}", errcode, errmsg);
            return false;
        }
        log.error("执行成功,响应信息 errcode:{} errmsg:{}", errcode, errmsg);
        return true;
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
            //jsonObject =JSONObject.fromObject(result);
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
     * map对象转行成xml
     *
     * @param map 集合
     * @return
     * @throws Exception
     */
    public static String mapToXml(Map<String, Object> map) throws Exception {
        Document d = DocumentHelper.createDocument();
        Element root = d.addElement("xml");
        mapToXml(root, map);
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriter(sw);
        xw.setEscapeText(false);
        xw.write(d);
        return sw.toString();
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
        // 将微信传入的CreateTime转换成long类型，再乘以1000
        long msgCreateTime = Long.parseLong(createTime) * 1000L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date(msgCreateTime);
    }

    /**
     * 将标准格式的时间（MM/dd/yyyy HH:mm:ss）转换为微信服务器的long型
     * @param formatTime
     * @return
     * @throws ParseException
     */
    public static long formatToLongTime(String formatTime){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2;
        try {
            dt2 = sdf.parse(formatTime);
            return dt2.getTime() / 1000;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //转换失败返回值
        return -1;
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
