package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.request.league.promoter.LeaguePromoterListGetReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.promoter.LeaguePromoterReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.promoter.LeaguePromoterUpdReq;
import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.LeaguePromoterInfoListResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.LeaguePromoterInfoResp;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Description 优选联盟达人
 * @Author axin
 * @Date 2023-02-13 14:51
 **/
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface LeaguePromoterApi {

    /**
     * 新增达人 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_addpromoter.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/promoter/add")
    BaseResponse add(@Query("access_token") String accessToken, @Body LeaguePromoterReq req);

    /**
     * 删除达人 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_deletepromoter.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/promoter/delete")
    BaseResponse delete(@Query("access_token") String accessToken, @Body LeaguePromoterReq req);

    /**
     * 编辑达人 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_updpromoter.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/promoter/upd")
    BaseResponse upd(@Query("access_token") String accessToken, @Body LeaguePromoterUpdReq req);

    /**
     * 获取达人详情信息 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_getpromoter.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/promoter/get")
    LeaguePromoterInfoResp get(@Query("access_token") String accessToken, @Body LeaguePromoterReq req);

    /**
     * 拉取商店达人列表 https://developers.weixin.qq.com/doc/channels/API/league/ecleague_getpromoterlist.html
     * @param accessToken
     * @param req
     * @return
     */
    @POST("/channels/ec/league/promoter/list/get")
    LeaguePromoterInfoListResp getList(@Query("access_token") String accessToken, @Body LeaguePromoterListGetReq req);


}
