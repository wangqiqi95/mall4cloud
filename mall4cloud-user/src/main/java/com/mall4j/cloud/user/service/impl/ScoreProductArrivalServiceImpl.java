package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.user.vo.ScoreConvertVO;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.user.model.ScoreProductStockSynLog;
import com.mall4j.cloud.user.service.ScoreProductArrivalService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 积分兑礼商品到货通知
 * @author Grady_Lu
 */
@Service
public class ScoreProductArrivalServiceImpl implements ScoreProductArrivalService {
    private static final Logger log = LoggerFactory.getLogger(ScoreProductArrivalServiceImpl.class);
    @Resource
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Resource
    StoreFeignClient storeFeignClient;
//    @Resource
//    private OnsMQTemplate sendMaSubcriptMessageTemplate;

    @Autowired
    private ScoreCouponService scoreCouponService;


    @Override
    public void ScoreProductArrival(ScoreProductStockSynLog scoreProduct) {
        String bussinessId = scoreProduct.getStoreCode() + "-" + scoreProduct.getSpuId();
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getScoreProductSubscriptTmessage(MaSendTypeEnum.SCORE_PRODUCT_ARRIVAL.getValue(), bussinessId);
        log.info("积分礼品到货提醒订阅模版: {}", JSONObject.toJSONString(subscriptResp));

        if (subscriptResp.isSuccess()) {
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();

            //记录存在就发送订阅消息
            if (CollectionUtil.isNotEmpty(userRecords)){
                List<WeixinMaSubscriptTmessageSendVO> sendList = userRecords.stream().map(userRecord -> {

                    String[] str = userRecord.getBussinessId().split("-");
                    Long convertId = Long.valueOf(str[2]);
                    ServerResponseEntity<ScoreConvertVO> scoreConvertVO = scoreCouponService.getScoreConvertVO(convertId);
                    String convertTitle = scoreConvertVO.getData().getConvertTitle();

                    StoreCodeVO storeCodeVO = storeFeignClient.findByStoreCode(scoreProduct.getStoreCode());
                    Assert.isNull(storeCodeVO,"店铺不存在。");

                    /**
                     * 值替换
                     * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                     * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                     * 当前积分商城 场景下 门店名称{stationName}、礼品类型{spuType}、礼品名称{activityName}、温馨提示{remark}
                     * 构建参数map.
                     */
                    Map<String, String> paramMap = new HashMap();
                    paramMap.put("{stationName}",storeCodeVO.getStationName());
                    paramMap.put("{spuType}","会员积分商城");
                    paramMap.put("{activityName}",convertTitle);
                    paramMap.put("{remark}","您订阅的礼品已备货完成，请及时到店兑换~");
                    log.info("模板: "+weixinMaSubscriptTmessageVO.getTmessageValues().toString());

                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                        msgData.setName(t.getTemplateValueName());
                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                        return msgData;
                    }).collect(Collectors.toList());

                    wxMaApiFeignClient.updateUserRecordId(userRecord.getId());

                    WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                    sendVO.setUserSubscriptRecordId(userRecord.getId());
                    sendVO.setData(msgDataList);
                    sendVO.setPage(weixinMaSubscriptTmessageVO.getPage()+"?couponId="+convertId+"&type=1");
                    return sendVO;
                }).collect(Collectors.toList());

                log.info("积分礼品到货提醒消息参数："+JSONObject.toJSONString(sendList));
//                sendMaSubcriptMessageTemplate.syncSend(sendList);
            }

        }else {
            log.error("查询积分礼品到货提醒模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
        }

    }
}
