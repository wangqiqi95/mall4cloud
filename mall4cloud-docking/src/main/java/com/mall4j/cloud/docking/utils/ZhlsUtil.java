package com.mall4j.cloud.docking.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Iterator;
import java.util.Random;

/**
 * 有数工具类
 */
@Slf4j
@Component
public class ZhlsUtil {

    public static String app_id;
    private static  String app_secret;
    public static String merchantId;
    public static String HOST;
    public static String RECOMMEND_HOST;

    //测试环境
//    public static String app_id="bi51817f6c883e485a";
//    private static  String app_secret="f14e5825d11843fa945cc0b7e0e627f0";
//    public static String merchantId="10005630";
//    public static String HOST="https://test.zhls.qq.com/";
//    public static String RECOMMEND_HOST="https://test.rec.tmc.tencent.com/";

    //正式环境
//    public static String app_id = "biee157637f8a6440b";
//    public static String app_secret = "10afb96e227f4b779a52462aa419f0dd";
//    public static String merchantId = "10000988";
//    public static String HOST = "https://zhls.qq.com/";



    public final static String PRODUCT_RECOMMEND_URI="product_service/recommend/get";
    public final static String ADD_SKU_INFO_URI="data-api/v1/sku_info/add";
    public final static String UPDATE_SKU_INFO_URI="data-api/v1/sku_info/update";
    public final static String PRODUCT_CATEGORIES_URI="data-api/v1/product_categories/add";
    static StringBuilder hexStringBuilder = new StringBuilder();

    public static ZhlsUtil zhlsUtil = new ZhlsUtil();

    public static ZhlsUtil clients() {
        return zhlsUtil;
    }

    /**
     * app_id=${app_id}&nonce=${nonce}&sign=sha256&timestamp=${timestamp}&signature=${signature}
     * @return
     * @throws Exception
     */
    private String sign() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = String.valueOf(Math.abs(random.nextLong()));
        // try the doc example
        // timestamp="1542951251";
        // nonce="407313d23c3f7";
        // ----
        String str = String.format("app_id=%s&nonce=%s&sign=sha256&timestamp=%s", app_id, nonce, timestamp);
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(app_secret.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(str.getBytes("UTF-8"));
        hexStringBuilder.setLength(0);
        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                hexStringBuilder.append('0');
            }
            hexStringBuilder.append(hex);
        }
        String signature = hexStringBuilder.toString();
        return str + "&signature=" + signature;
    }


    public void addDataSource() {
        try {
            String url = HOST+"data-api/v1/data_source/add?"+sign();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchantId",merchantId);
            String result = HttpUtil.post(url, jsonObject.toJSONString());
            if (StringUtils.isNotBlank(result)) {
                JSONObject object = JSONObject.parseObject(result);
                String retcode = object.getString("retcode");
                if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
                    log.info("添加仓库失败:{}",object.getString("errmsg"));
                }
                log.info("添加仓库成功:{}",object.toJSONString());
            }
        }catch (Exception e){
            log.info("",e);
        }
    }

    /**
     * 0:订单，7：微信
     * @return
     */
    public String getSourceId() {
        try {
            String url = HOST + "data-api/v1/data_source/get?" + sign();
            String result = HttpUtil.get(url);
            if (StringUtils.isNotBlank(result)) {
                JSONObject object = JSONObject.parseObject(result);
                String retcode = object.getString("retcode");
                if (StringUtils.isBlank(retcode) || !retcode.equals("0")) {
                    log.info("添加仓库失败:{}", object.getString("errmsg"));
                }
                log.info("查询数据源:{}", object.toJSONString());
                JSONObject data = object.getJSONObject("data");
                if (data != null) {
                    JSONArray dataSources = data.getJSONArray("dataSources");
                    Iterator<Object> i = dataSources.iterator();
                    while (i.hasNext()) {
                        JSONObject jsonObject = (JSONObject) i.next();
                        String id = jsonObject.getString("id");
                        if (StringUtils.isNotBlank(id)) {
                            return id;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取数据源异常:{}",e);
        }
        return "";
    }

    public String post(String url,String reqJsonStr) {
        log.info("调用有数入参:{}",reqJsonStr);
        long start = System.currentTimeMillis();
        String result = null;
        try {
            result = HttpUtil.post(url + "?" + sign(), reqJsonStr,6000);
        } catch (Exception e) {
            log.error("调用有数接口异常url为:{}, 异常信息:{}, 共耗时: {}",url,e,System.currentTimeMillis() - start);
        }finally {
            log.info("调用有数接口结束url为:{}, 响应json参数为:{}, 共耗时: {}",url,result,System.currentTimeMillis() - start);
        }
        return result;
    }

    public String get(String url,String reqJsonStr) {
        log.info("调用有数入参:{}",reqJsonStr);
        long start = System.currentTimeMillis();
        String result = null;
        try {
            result = HttpUtil.get(url + "?" + sign(),6000);
        } catch (Exception e) {
            log.error("调用有数接口异常url为:{}, url参数为:{}, 异常信息:{}, 共耗时: {}",url,reqJsonStr,e,System.currentTimeMillis() - start);
        }finally {
            log.info("调用有数接口结束url为:{}, 响应json参数为:{}, 共耗时: {}",url,result,System.currentTimeMillis() - start);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        ZhlsUtil zhlsApiUtil = new ZhlsUtil();

//        String sourceId = zhlsApiUtil.getSourceId();
//        String addurl = HOST+"/data-api/v1/sku_info/add?"+zhlsApiUtil.sign();
//        String json="{\"dataSourceId\":\"13075\",\"skus\":[{\"external_sku_id\":\"232450-NVY115\",\"external_spu_id\":\"232450\",\"media_info\":{\"primary_imgs\":{\"img_url\":\"https://xcx-prd.skechers.cn/product/iph/289bf5304a8947db9d3646287bd71d62.jpg\"},\"imgs\":[{\"img_url\":\"https://xcx-prd.skechers.cn/product/iph/289bf5304a8947db9d3646287bd71d62.jpg\"}]},\"category_info\":[{\"category_type\":1,\"category_level_1_id\":\"3498\",\"category_level_1_name\":\"运动户外\",\"category_level_2_id\":\"3500\",\"category_level_2_name\":\"鞋类\",\"category_level_3_id\":\"3502\",\"category_level_3_name\":\"时尚运动鞋\"}],\"sales_info\":{\"is_available\":true,\"product_type\":1,\"sku_stock_status\":1},\"desc_info\":{\"product_name_chinese\":\"1测试【闪穿slip-ins】Skechers斯凯奇男士高回弹轻量缓震舒适一脚蹬休闲运动鞋232450海军蓝色/NVY,45.5\"},\"price\":{\"current_price\":699,\"sku_price\":699}}]}";
//        String result = HttpUtil.post(addurl, json);


//        //上下架商品13075
//        String addurl = HOST+"/data-api/v1/sku_info/update?"+zhlsApiUtil.sign();
//        String json="{\"dataSourceId\":\"13075\",\"skus\":[{\"external_sku_id\":\"L321U017-00PS99\",\"external_spu_id\":\"L321U017\",\"sales_info\":{\"is_available\":true,\"is_deleted\":\"0\"}}]}";
//        String result = HttpUtil.post(addurl, json);

        //推荐列表
        String addurl = "https://test.rec.tmc.tencent.com/product_service/recommend/get?"+zhlsApiUtil.sign();
        String json="{\"uid_type\":2,\"user_id\":\"oZ2-b5dCSkOUT0Y1rNVqYOy_hGjo\",\"channel_id\":62059,\"sequence_id\":\"3708f680-d6c3-428d-98d8-43ee0aa9a1d0_1683516485527_600\",\"store_ids\":[\"0\"],\"item_id\":\"\",\"page_size\":10,\"page_no\":1}";
        String result = HttpUtil.post(addurl, json);

        //新建商品池
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/pool/create?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"poolName\":\"spu维度商品池\",\"outerGroupId\":\"\"}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //删除商品池
//        String addurl = "http://test.rec.tmc.tencent.com/recommend/manage/pool/delete?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"poolId\":68954,\"outerGroupId\":\"group1\"}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //获取商品池列表
//        String addurl = "http://test.rec.tmc.tencent.com/recommend/manage/pool/listDetail?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"adfalkjj\"}";
//        String result = ZhlsUtil.clients().post(addurl, json);

//        //批量添加商品到商品池
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/product/addToPool?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"poolId\":70678,\"productIds\":[\"L321U017\"]}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //批量从商品池移除商品
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/product/removeFromPool?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"poolId\":68954,\"productIds\":[\"11923-BKSL9\"]}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //根据商品池id查询池内商品列表
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/product/listByPool?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"poolId\":68954,\"pageNo\":1,\"pageSize\":10}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //新建推荐场景
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/channel/create?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"channelName\":\"商详页推荐\",\"remark\":\"商详页推荐\",\"sceneType\":2,\"poolList\":[68954]}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //编辑推荐场景
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/channel/update?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"channelId\":63218,\"channelName\":\"商详推荐\",\"remark\":\"双十一大促用\",\"sceneType\":2,\"poolList\":[70678]}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //删除推荐场景
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/channel/delete?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"channelId\":1}";
//        String result = ZhlsUtil.clients().post(addurl, json);

        //获取推荐场景列表
//        String addurl = "https://test.rec.tmc.tencent.com/recommend/manage/channel/listByDetails?"+zhlsApiUtil.sign();
//        String json="{\"requestId\":\"123456\",\"pageNo\":1,\"pageSize\":10}";
//        String result = ZhlsUtil.clients().post(addurl, json);


        System.out.println(result);

    }

    @Value("${zhls.appId:bi51817f6c883e485a}")
    public void setApp_id(String app_id) {
        ZhlsUtil.app_id = app_id;
    }

    @Value("${zhls.appSecret:f14e5825d11843fa945cc0b7e0e627f0}")
    public void setApp_secret(String app_secret) {
        ZhlsUtil.app_secret = app_secret;
    }

    @Value("${zhls.merchantId:10005630}")
    public void setMerchantId(String merchantId) {
        ZhlsUtil.merchantId = merchantId;
    }

    @Value("${zhls.host:https://test.zhls.qq.com/}")
    public void setHOST(String HOST) {
        ZhlsUtil.HOST = HOST;
    }

    @Value("${zhls.recommend.host:https://test.rec.tmc.tencent.com/}")
    public void setRecommendHost(String recommendHost) {
        RECOMMEND_HOST = recommendHost;
    }
}

