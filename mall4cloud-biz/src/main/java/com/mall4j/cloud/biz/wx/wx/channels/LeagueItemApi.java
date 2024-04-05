package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.*;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.BatchAddResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.ItemGetResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.ItemUpdResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.ListGetResp;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Description 优选联盟商品
 * @Author axin
 * @Date 2023-02-13 14:52
 **/
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface LeagueItemApi {

    /**
     * 批量新增联盟商品 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_batchadditem.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/item/batchadd")
    BatchAddResp batchadd(@Query("access_token") String accessToken, @Body BatchAddReq req);

    /**
     * 更新联盟商品信息 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_upditem.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/item/upd")
    ItemUpdResp upd(@Query("access_token") String accessToken, @Body ItemUpdReq req);

    /**
     * 删除联盟商品 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_deleteitem.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/item/delete")
    BaseResponse delete(@Query("access_token") String accessToken, @Body ItemDeleteReq req);

    /**
     * 拉取联盟商品详情 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_getitem.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/item/get")
    ItemGetResp get(@Query("access_token") String accessToken, @Body ItemGetReq req);

    /**
     * 拉取联盟商品推广列表 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_getitemlist.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/item/list/get")
    ListGetResp getList(@Query("access_token") String accessToken, @Body ListGetReq req);


}
