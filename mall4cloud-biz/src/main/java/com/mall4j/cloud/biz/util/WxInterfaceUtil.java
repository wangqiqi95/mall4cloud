
package com.mall4j.cloud.biz.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mall4j.cloud.biz.dto.live.*;
import com.mall4j.cloud.biz.dto.live.LiveGoodsInfoReqParam;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 微信相关工具类
 * @author LHD
 */
@Slf4j
@Component
public class WxInterfaceUtil {



    private String liveUrl = "https://api.weixin.qq.com";
    private String goodsUrl = "https://api.weixin.qq.com/wxaapi/broadcast/goods";
    private String goodsStatusUrl = "https://api.weixin.qq.com/wxa/business";
    private String goodsListUrl = "https://api.weixin.qq.com/wxaapi/broadcast/goods/getapproved?access_token=[ACCESS_TOKEN]";


    /**
     * 获取直播间列表接口
     * @return
     */
    public WxServerResponse<List<RoomDetailResponse>> pageLiveRoom(PageLiveRoomInfo wxLiveRoomInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("start",wxLiveRoomInfo.getStart());
        requestParam.put("limit", wxLiveRoomInfo.getLimit());

//        return signAndSend(wxLiveRoomInfo, requestParam,new TypeReference<WxServerResponse<List<RoomDetailResponse>>>() {});
        return null;
    }


//    /**
//     * 发送请求，获取响应结果
//     * @param requestParam
//     * @return
//     */
    private <T> WxServerResponse<T> signAndSend(WxInterfaceInfo wxInterfaceInfo, Map<String, Object> requestParam, TypeReference<WxServerResponse<T>> typeReference) {
        log.info(Json.toJsonString(requestParam));
        String str = liveUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        System.out.println("str: " + str);
        String body = HttpUtil.createPost(str)
                .body(Json.toJsonString(requestParam))
                .contentType("application/json")
                .execute().body();
        log.info(body);
        return JSON.parseObject(body, typeReference);
    }


    /**
     * 商品添加并提审
     * 商品添加到微信并且提交审核的接口
     */
    public GoodsInfoRespParam prodAddVerify(LiveGoodsInfoReqParam goodsInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("goodsInfo",goodsInfo);
        GoodsInfoRespParam goodsInfoRespParam = prodSignAndSend(goodsInfo, requestParam, new TypeReference<GoodsInfoRespParam>() {});
        log.info("goodsInfo: " + goodsInfoRespParam);
        if (!Objects.equals(goodsInfoRespParam.getErrcode(),0) ) {
            // 提交审核失败
//            throw new LuckException("yami.examine.fail");
        }
        return goodsInfoRespParam;
    }


    /**
     *  微信请求-商品管理请求
     *
     */
    private <T> T prodSignAndSend(WxInterfaceInfo wxInterfaceInfo, Map<String, Object> requestParam, TypeReference<T> typeReference) {
        log.info(Json.toJsonString(requestParam));
        String reqUrl = goodsUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        if (wxInterfaceInfo.getRequestUrl().contains("getgoodswarehouse")) {
            reqUrl = goodsStatusUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        }
        if (wxInterfaceInfo.getRequestUrl().contains("getapproved")) {
            reqUrl = goodsListUrl.replace("ACCESS_TOKEN",wxInterfaceInfo.getAccessToken());
        }
        log.info("url: " + reqUrl);
        String body = HttpUtil.createPost(reqUrl)
                .body(Json.toJsonString(requestParam))
                .contentType("application/json")
                .execute().body();
        log.info(body);
        if (StringUtils.contains(body,"errmsg")) {
            String message = I18nMessage.getMessage("yami.request.error");
            throw new LuckException(message+body);
        }
        return JSON.parseObject(body,typeReference);
    }
}
