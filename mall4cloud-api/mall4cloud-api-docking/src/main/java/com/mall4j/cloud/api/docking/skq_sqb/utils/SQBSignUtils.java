package com.mall4j.cloud.api.docking.skq_sqb.utils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBBaseResponse;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBBizResponse;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBResponse;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBResponseBody;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBResponseHead;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  收钱吧轻posRSA签名验签类
 *
 **/
public class SQBSignUtils {
    
    private static final Logger log = LoggerFactory.getLogger(SQBSignUtils.class);
    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS_1 = "SHA1WithRSA";
    public static final String SIGN_ALGORITHMS_256 = "SHA256withRSA";
    
    //编码
    private static final String CHARSET_UTF_8 = "utf-8";
    
    /**
     * RSA签名
     *
     * @param content
     *            待签名数据
     * @param privateKey
     *            商户私钥
     * @param encode
     *            字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(privateKey));
            
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            
            Signature signature = Signature
                    .getInstance(SIGN_ALGORITHMS_256);
            
            signature.initSign(priKey);
            signature.update(content.getBytes(encode));
            
            byte[] signed = signature.sign();
            
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature
                    .getInstance(SIGN_ALGORITHMS_256);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * RSA验签名检查
     *
     * @param content
     *            待签名数据
     * @param sign
     *            签名值
     * @param publicKey
     *            分配给开发商公钥
     * @param encode
     *            字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign,
                                  String publicKey, String encode) {
        try {
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedKey));
    
            String sign_type = SIGN_ALGORITHMS_1;
            try {
                JSONObject jsonObject = JSONObject.parseObject(content);
                sign_type = ((JSONObject) jsonObject.get("head")).get("sign_type").toString();
                if(StrUtil.isNotEmpty(sign_type)){
                    if("SHA1".equals(sign_type)){
                        sign_type = SIGN_ALGORITHMS_1;
                    }
                    if("SHA256".equals(sign_type)){
                        sign_type = SIGN_ALGORITHMS_256;
                    }
                }
            }catch (Exception e){
                log.info("获取签名类型失败");
            }
            
            Signature signature = Signature
                    .getInstance(sign_type);
            
            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));
            
            return signature.verify(Base64.decodeBase64(sign));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            
            PublicKey pubKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedKey));
    
            String sign_type = SIGN_ALGORITHMS_1;
            try {
                JSONObject jsonObject = JSONObject.parseObject(content);
                sign_type = ((JSONObject) jsonObject.get("head")).get("sign_type").toString();
                if(StrUtil.isNotEmpty(sign_type)){
                    if("SHA1".equals(sign_type)){
                        sign_type = SIGN_ALGORITHMS_1;
                    }
                    if("SHA256".equals(sign_type)){
                        sign_type = SIGN_ALGORITHMS_256;
                    }
                }
            }catch (Exception e){
                log.info("获取签名类型失败");
            }
    
            Signature signature = Signature
                    .getInstance(sign_type);
            
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            
            return signature.verify(Base64.decodeBase64(sign));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public static String formatDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return formatter.format(date);
        
    }
    /**
     * 同步反参验签 （轻POS、电子发票）
     */
    public static boolean syncSign(String result,String publicKey){
        try {
            JSONObject jsonObject = JSON.parseObject(result,Feature.OrderedField);
            String response =
                    JSON.toJSONString(jsonObject.get("response"), SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames);
            String signature = jsonObject.get("signature").toString();
            boolean YQresult = doCheck(response,signature,publicKey);
            log.info("同步反参验签响应入参为：{} ,验签结果为：{}" ,result ,YQresult);
            return YQresult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 回调验签 （轻POS、电子发票）
     */
    public static boolean callbackSign(String result,String publicKey){
        try {
            JSONObject jsonObject = JSON.parseObject(result, Feature.OrderedField);
            String request =
                    JSON.toJSONString(jsonObject.get("request"), SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames);
            String signature = JSON.toJSONString(jsonObject.get("signature"), SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames);
            boolean YQresult = doCheck(request,signature,publicKey);
            log.info("回调验签请求入参为：{} ,验签结果为：{}" ,result ,YQresult);
            return YQresult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    public static String callbackContent(String appid ,String privateKey){
        SQBResponseHead head = new SQBResponseHead();
        head.setVersion("1.0.0");
        head.setAppid(appid);
        head.setSign_type("SHA256");
        head.setResponse_time(DateUtil.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
        
        //response
        SQBResponseBody body = new SQBResponseBody();
        body.setResult_code("200");
        SQBBizResponse bizResponse = new SQBBizResponse();
        bizResponse.setResult_code("200");
        body.setBiz_response(bizResponse);

        SQBResponse requestJson = new SQBResponse();
        requestJson.setHead(head);
        requestJson.setBody(body);

        SQBBaseResponse response = new SQBBaseResponse();
        //使用私钥签名，并转成BASE64编码
        String sign = sign(requestJson.toString(), privateKey, "UTF-8");
        response.setResponse(requestJson);
        response.setSignature(sign);
        return JSONObject.toJSONString(response);
    }
}
