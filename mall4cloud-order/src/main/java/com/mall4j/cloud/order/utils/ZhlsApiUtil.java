package com.mall4j.cloud.order.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.config.WxConfig;
import com.mall4j.cloud.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;


@Slf4j
@Component
public class ZhlsApiUtil {

    /**
     * 上为测试环境，下为正式环境
     */
    @Value("${zhls.appId}")
    private String app_id;
    //            = "bi51817f6c883e485a";
//    public static String app_id = "biee157637f8a6440b";
    @Value("${zhls.appSecret}")
    private String app_secret;
    //    = "f14e5825d11843fa945cc0b7e0e627f0";
//    public static String app_secret = "10afb96e227f4b779a52462aa419f0dd";
    @Value("${zhls.merchantId}")
    private String merchantId;
    //    = "10005630";
//    public static String merchantId = "10000988";
    @Value("${zhls.host}")
    private String HOST;
    //    = "https://test.zhls.qq.com/";
//    public static String HOST = "https://zhls.qq.com/";
    StringBuilder hexStringBuilder = new StringBuilder();

    // 获取微信上报数据
    public static String GET_WEANALYSIASPP_VISIT =  "https://api.weixin.qq.com/datacube/getweanalysisappidvisitpage?access_token=ACCESS_TOKEN";

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private SpuFeignClient spuFeignClient;

    /**
     * app_id=${app_id}&nonce=${nonce}&sign=sha256&timestamp=${timestamp}&signature=${signature}
     * @return
     * @throws Exception
     */
    public String sign() throws Exception {
//        Random random = new Random(System.currentTimeMillis());
//        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        String nonce = String.valueOf(Math.abs(random.nextLong()));
//        // try the doc example
//        // timestamp="1542951251";
//        // nonce="407313d23c3f7";
//        // ----
//        String str = String.format("app_id=%s&nonce=%s&sign=sha256&timestamp=%s", app_id, nonce, timestamp);
//        Mac mac = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKey = new SecretKeySpec(app_secret.getBytes("UTF-8"), mac.getAlgorithm());
//        mac.init(secretKey);
//        byte[] bytes = mac.doFinal(str.getBytes("UTF-8"));
//        hexStringBuilder.setLength(0);
//        for (int i = 0; i < bytes.length; ++i) {
//            String hex = Integer.toHexString(0xff & bytes[i]);
//            if (hex.length() == 1) {
//                hexStringBuilder.append('0');
//            }
//            hexStringBuilder.append(hex);
//        }
//        String signature = hexStringBuilder.toString();
//        return str + "&signature=" + signature;
        return "";
    }


    public void addDataSource() {
//        try {
//            String url = HOST+"data-api/v1/data_source/add?"+sign();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("merchantId",merchantId);
//            String result = HttpUtil.post(url, jsonObject.toJSONString());
//            if (StringUtils.isNotBlank(result)) {
//                JSONObject object = JSONObject.parseObject(result);
//                String retcode = object.getString("retcode");
//                if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
//                    log.info("添加仓库失败:{}",object.getString("errmsg"));
//                }
//                log.info("添加仓库成功:{}",object.toJSONString());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    /**
     * 0:订单，7：微信
     * @return
     */
    public String getSourceId() {
//        try {
//            String url = HOST + "data-api/v1/data_source/get?" + sign();
//            String result = HttpUtil.get(url);
//            if (StringUtils.isNotBlank(result)) {
//                JSONObject object = JSONObject.parseObject(result);
//                String retcode = object.getString("retcode");
//                if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
//                    log.info("添加仓库失败:{}", object.getString("errmsg"));
//                }
//                log.info("查询数据源:{}", object.toJSONString());
//                JSONObject data = object.getJSONObject("data");
//                if (data != null) {
//                    JSONArray dataSources = data.getJSONArray("dataSources");
//                    Iterator<Object> i = dataSources.iterator();
//                    while (i.hasNext()) {
//                        JSONObject jsonObject = (JSONObject) i.next();
//                        String id = jsonObject.getString("id");
//                        if (StringUtils.isNotBlank(id)) {
//                            return id;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "";
    }

    @Async
    public void addOrder(List<Order> data,String orderType) {
        log.info("添加订单入参：{},会员数据:{}",JSONObject.toJSONString(data),data.get(0).getUserId());
//        try {
//            String url = HOST+"data-api/v1/order/add_order?"+sign();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("dataSourceId",getSourceId());
//            JSONArray orders = new JSONArray();
//            JSONArray goodsInfo = new JSONArray();
//            JSONArray paymentInfo = new JSONArray();
//            data.forEach(order ->{
//                JSONObject object = new JSONObject();
//                object.put("external_order_id",order.getOrderNumber());
//                object.put("create_time",DateUtil.date().getTime()+"");
//                if (order.getCreateTime() != null) {
//                    object.put("create_time",order.getCreateTime().getTime()+"");
//                }
//                object.put("order_source","wxapp");
//                if (order.getOrderType() == 0){
//                    object.put("order_type",1);
//                }else if (order.getOrderType() == 3) {
//                    object.put("order_type",7);
//                }else {
//                    object.put("order_type",1);
//                }
//                object.put("goods_num_total",order.getAllCount());
//                float orderAmount = 0f;
//                Float total = Float.parseFloat(order.getTotal().toString());
//                if (total != null && total != 0){
//                    total = total/100;
//                    orderAmount = total;
//                }
//                object.put("goods_amount_total",total);
//                Float freightAmount = Float.parseFloat(order.getFreightAmount().toString());
//                if (freightAmount != null && freightAmount != 0){
//                    freightAmount = freightAmount/100;
//                    orderAmount = orderAmount + freightAmount;
//                }
//                object.put("freight_amount",freightAmount);
//                Float actualTotal = Float.parseFloat(order.getActualTotal().toString());
//                if (actualTotal != null && actualTotal != 0){
//                    actualTotal = actualTotal/100;
//                }
//
//                object.put("order_amount",orderAmount);
//                object.put("payable_amount",actualTotal);
//                object.put("payment_amount",actualTotal);
//                object.put("order_status",orderType);
//                object.put("status_change_time",System.currentTimeMillis()+"");
//                order.getOrderItems().forEach(temp -> {
//                    JSONObject goods = new JSONObject();
//                    goods.put("external_sku_id",temp.getSkuId()+"");
//                    if (temp.getSkuId() != null) {
//                        ServerResponseEntity<SkuVO> sku = skuFeignClient.insiderGetById(temp.getSkuId());
//                        log.info("sku数据：{}",JSONObject.toJSONString(sku));
//                        if (sku != null && sku.isSuccess() && sku.getData() != null) {
//                            if (StrUtil.isNotBlank(sku.getData().getName())) {
//                                goods.put("sku_name_chinese",sku.getData().getName());
//                            }
//                            if (StrUtil.isBlank(goods.getString("sku_name_chinese")) && StrUtil.isNotBlank(sku.getData().getSkuName())) {
//                                goods.put("sku_name_chinese",sku.getData().getSkuName());
//                            }
//                            if (StrUtil.isNotBlank(sku.getData().getSkuCode())) {
//                                goods.put("external_sku_id",sku.getData().getSkuCode());
//                            }
//                            if (StrUtil.isBlank(goods.getString("sku_name_chinese")) && StrUtil.isNotBlank(temp.getSpuName())) {
//                                goods.put("sku_name_chinese",temp.getSpuName());
//                            }
//                        }
//                    } else {
//                        goods.put("sku_name_chinese",temp.getSkuName());
//                    }
//                    if (StrUtil.isBlank(goods.getString("sku_name_chinese"))) {
//                        goods.put("sku_name_chinese",goods.getString("external_sku_id"));
//                    }
//                    Float price = Float.parseFloat(temp.getPrice().toString());
//                    if (price != null && price != 0){
//                        price = price / 100;
//                    }
//                    Float getActualTotal = Float.parseFloat(temp.getActualTotal().toString());
//                    if (getActualTotal != null && getActualTotal != 0){
//                        getActualTotal = getActualTotal / 100;
//                    }
//                    goods.put("goods_amount",price);
//
//                    goods.put("payment_amount",getActualTotal);
//                    goods.put("external_spu_id",temp.getSpuId()+"");
//                    if (temp.getSpuId() != null) {
//                        ServerResponseEntity<SpuVO> spu = spuFeignClient.insiderGetById(temp.getSpuId());
//                        log.info("spu数据：{}",JSONObject.toJSONString(spu));
//                        if (spu != null && spu.isSuccess() && spu.getData() != null) {
//                            if (StrUtil.isNotBlank(spu.getData().getSpuCode())) {
//                                goods.put("external_spu_id",spu.getData().getSpuCode());
//                            }
//                        }
//                    }
//                    goods.put("spu_name_chinese",temp.getSpuName());
//                    goods.put("goods_num",temp.getCount());
//                    goodsInfo.add(goods);
//                });
//                JSONObject userInfo = new JSONObject();
//                ServerResponseEntity<UserApiVO> responseEntity = userFeignClient.getUserById(order.getUserId());
//                log.info("会员数据:{}",JSONObject.toJSONString(responseEntity));
//                if (responseEntity != null && responseEntity.isSuccess()) {
//                    String openId = responseEntity.getData().getOpenId();
//                    if (StringUtils.isNotBlank(openId)) {
//                        userInfo.put("open_id",openId);
//                    }
//                }else {
//                    userInfo.put("open_id","");
//                }
//                userInfo.put("user_id",order.getUserId()+"");
//                object.put("user_info",userInfo);
//                object.put("goods_info",goodsInfo);
//                if (!orderType.equals("1110")) {
//                    JSONObject info = new JSONObject();
//                    info.put("payment_type","99999");
//                    info.put("trans_id",order.getOrderId().toString());
//                    info.put("trans_amount",actualTotal);
//                    paymentInfo.add(info);
//                    object.put("payment_info",paymentInfo);
//                }
//                object.put("is_deleted",0);
//                orders.add(object);
//            });
//            jsonObject.put("orders",orders);
//            log.info("推送数据：{}",jsonObject.toJSONString());
//            String result = HttpUtil.post(url, jsonObject.toJSONString());
//            if (StringUtils.isNotBlank(result)) {
//                log.info("返回参数：{}",result);
//                JSONObject object = JSONObject.parseObject(result);
//                String retcode = object.getString("retcode");
//                if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
//                    log.info("添加仓库失败:{}",object.getString("errmsg"));
//                    return;
//                }
//                log.info("添加仓库成功:{}",object.toJSONString());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void addOrderSum(JSONObject data) {
//        try {
//            String url = HOST+"data-api/v1/order/add_order_sum?"+sign();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("dataSourceId",getSourceId());
//            JSONArray orders = new JSONArray();
//            orders.add(data);
//            jsonObject.put("orders",orders);
//            log.info("推送数据：{}",jsonObject.toJSONString());
//            String result = HttpUtil.post(url, jsonObject.toJSONString());
//            if (StringUtils.isNotBlank(result)) {
//                log.info("返回参数：{}",result);
//                JSONObject object = JSONObject.parseObject(result);
//                String retcode = object.getString("retcode");
//                if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
//                    log.info("添加仓库失败:{}",object.getString("errmsg"));
//                    return;
//                }
//                log.info("添加仓库成功:{}",object.toJSONString());
//            }
//        }catch (Exception e){
//            log.info("推送订单汇总数据上报失败");
//            e.printStackTrace();
//        }
    }

    public void addWxAppVisit() {
//        try {
//            String  getPageUrl = GET_WEANALYSIASPP_VISIT.replace("ACCESS_TOKEN", wxConfig.getWxMaService().getAccessToken());
//            JSONObject jsonString = new JSONObject();
//            jsonString.put("begin_date", DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
//            jsonString.put("end_date",DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
//            log.info("获取数据分析信息，请求参数：【{}】，请求路径：【{}】", jsonString, getPageUrl);
//            JSONObject jsonObj = WechatUtils.doPoststr(getPageUrl, jsonString.toJSONString());
//            log.info("===========获取数据分析详细Info==="+jsonObj.toString()+"===========");
//            if (jsonObj != null && !jsonObj.containsKey("errcode")) {
//                String url = HOST+"data-api/v1/analysis/add_wxapp_visit_page?"+sign();
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("dataSourceId",getSourceId());
//                JSONArray rawMsg = new JSONArray();
//                rawMsg.add(jsonObj);
//                jsonObject.put("rawMsg",rawMsg);
//                log.info("推送数据：{}",jsonObject.toJSONString());
//                String result = HttpUtil.post(url, jsonObject.toJSONString());
//                if (StringUtils.isNotBlank(result)) {
//                    log.info("返回参数：{}",result);
//                    JSONObject object = JSONObject.parseObject(result);
//                    String retcode = object.getString("retcode");
//                    if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
//                        log.info("添加仓库失败:{}",object.getString("errmsg"));
//                        return;
//                    }
//                    log.info("添加仓库成功:{}",object.toJSONString());
//                }
//            } else {
//                log.info("推送微信数据失败,获取微信数据失败");
//            }
//        }catch (Exception e){
//            log.info("推送微信数据上报失败！");
//            e.printStackTrace();
//        }
    }

}

