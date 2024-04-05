package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.sharer.*;
import com.mall4j.cloud.api.biz.dto.channels.response.sharer.*;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Description 分享员相关接口
 * @Author axin
 * @Date 2023-02-13 11:30
 **/
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface SharerApi {

    /**
     * 邀请分享员 https://developers.weixin.qq.com/doc/channels/API/sharer/bindsharer.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/sharer/bind")
    SharerBindResp bind(@Query("access_token") String accessToken, @Body SharerBindReq req);

    /**
     * 获取绑定的分享员 https://developers.weixin.qq.com/doc/channels/API/sharer/search_sharer.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/sharer/search_sharer")
    SearchSharerResp searchSharer(@Query("access_token") String accessToken, @Body SearchSharerReq req);

    /**
     * 获取绑定的分享员列表 https://developers.weixin.qq.com/doc/channels/API/sharer/get_sharer_list.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/sharer/get_sharer_list")
    GetSharerListResp getSharerList(@Query("access_token") String accessToken, @Body GetSharerListReq req);

    /**
     * 获取分享员订单列表 https://developers.weixin.qq.com/doc/channels/API/sharer/get_sharer_order_list.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/sharer/get_sharer_order_list")
    GetSharerOrderListResp getSharerOrderList(@Query("access_token") String accessToken, @Body GetSharerOrderListReq req);

    /**
     * 解绑分享员 https://developers.weixin.qq.com/doc/channels/API/sharer/get_sharer_order_list.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/sharer/unbind")
    SharerUnbindResp unbind(@Query("access_token") String accessToken, @Body SharerUnbindReq req);




}
