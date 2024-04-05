package com.mall4j.cloud.docking.skq_sqb.api;

import com.alibaba.fastjson.JSONObject;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *  收钱吧轻POS对外接口
 * @author Peter_Tan
 * @date 2023-05-05 18:30
 **/
@RetrofitClient(baseUrl = "https://vapi.shouqianba.com")
public interface LitePosApi {

    /**
     * 购买
     */
    @POST("/api/lite-pos/v1/sales/purchase")
    JSONObject purchase(@Body RequestBody requestBody);

    /**
     * 退款
     */
    @POST("/api/lite-pos/v1/sales/refund")
    JSONObject refund(@Body RequestBody requestBody);

    /**
     * 取消订单操作
     */
    @POST("/api/lite-pos/v1/sales/void")
    JSONObject cancelOrderOperation(@Body RequestBody requestBody);

    /**
     * 销售类结果查询
     */
    @POST("/api/lite-pos/v1/sales/query")
    JSONObject queryResult(@Body RequestBody requestBody);
    
}
