package com.mall4j.cloud.biz.feign;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.ma.WxMaGenerateSchemeRequest;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageValueMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptUserBussinessRecordMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptUserRecordMapper;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserBussinessRecord;
import com.mall4j.cloud.biz.service.WxMaApiService;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Date 2022年02月14日, 0030 14:48
 * @Created by gmq
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class WxMaApiController implements WxMaApiFeignClient {

    @Autowired
    private WxMaApiService wxMaApiService;
    @Autowired
    WeixinMaSubscriptTmessageMapper weixinMaSubscriptTmessageMapper;
    @Autowired
    WeixinMaSubscriptTmessageValueMapper weixinMaSubscriptTmessageValueMapper;
    @Autowired
    WeixinMaSubscriptUserRecordMapper weixinMaSubscriptUserRecordMapper;
    @Autowired
    WeixinMaSubscriptUserBussinessRecordMapper weixinMaSubscriptUserBussinessRecordMapper;

    @Override
    public ServerResponseEntity<String> getPhoneNumber(String code, String encryptedData, String ivStr,Integer authType) {
        return wxMaApiService.getPhoneNumber(code,encryptedData,ivStr,authType);
    }

    @Override
    public ServerResponseEntity<WeixinMaSubscriptTmessageVO> getSubscriptTmessage(Integer sendType, List<String> bussinessIds) {

        WeixinMaSubscriptTmessageVO tmessageVO = weixinMaSubscriptTmessageMapper.getBySendType(sendType);
        tmessageVO.setTmessageValues(weixinMaSubscriptTmessageValueMapper.getByTmessageId(tmessageVO.getId()));

        List<WeixinMaSubscriptUserRecordVO> list = new ArrayList<>();
        if(CollectionUtil.isEmpty(bussinessIds)){
            list = weixinMaSubscriptUserRecordMapper.getByTemplateId(tmessageVO.getId()).stream()
                    .collect(Collectors.groupingBy(WeixinMaSubscriptUserRecordVO::getUserId))
                    .entrySet().stream().map(e -> e.getValue().stream().findFirst().get()
                    ).collect(Collectors.toList());
        }else{
            list = weixinMaSubscriptUserRecordMapper.getByTemplateIdAndBusIds(tmessageVO.getId(),bussinessIds).stream()
                    .collect(Collectors.groupingBy(WeixinMaSubscriptUserRecordVO::getUserId))
                    .entrySet().stream().map(e -> e.getValue().stream().findFirst().get()
                    ).collect(Collectors.toList());;
        }

        tmessageVO.setUserRecords(list);

        return ServerResponseEntity.success(tmessageVO);
    }

    @Override
    public ServerResponseEntity<WeixinMaSubscriptTmessageVO> getScoreProductSubscriptTmessage(Integer sendType, String bussinessId) {

        WeixinMaSubscriptTmessageVO tmessageVO = weixinMaSubscriptTmessageMapper.getBySendType(sendType);
        tmessageVO.setTmessageValues(weixinMaSubscriptTmessageValueMapper.getByTmessageId(tmessageVO.getId()));
        List<WeixinMaSubscriptUserRecordVO>  subscriptUserRecordVOList = weixinMaSubscriptUserRecordMapper.getByTemplateIdAndBusId(tmessageVO.getId(),bussinessId);

        List<WeixinMaSubscriptUserRecordVO> resultSubscriptUserRecordVOList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(subscriptUserRecordVOList)){

            List<WeixinMaSubscriptUserRecordVO> userRecordVOList  = subscriptUserRecordVOList.stream()
                    .collect(Collectors.groupingBy(WeixinMaSubscriptUserRecordVO::getUserId))
                    .entrySet().stream().map(e -> e.getValue().stream().findFirst().get())
                    .collect(Collectors.toList());

            userRecordVOList.forEach(weixinMaSubscriptUserRecordVO -> {
                List<WeixinMaSubscriptUserBussinessRecord> userBussinessRecordList = weixinMaSubscriptUserBussinessRecordMapper
                        .getUserBusRecordByUserRecordId(weixinMaSubscriptUserRecordVO.getId());
                List<Long> userRecordIds = userBussinessRecordList.stream()
                        .map(WeixinMaSubscriptUserBussinessRecord::getUserRecordId)
                        .collect(Collectors.toList());
                List<WeixinMaSubscriptUserRecordVO> dbUserRecordVOList = weixinMaSubscriptUserRecordMapper
                        .getUserRecordByUserRecordIds(userRecordIds);

                boolean flag = false;
                for (WeixinMaSubscriptUserRecordVO userRecord : dbUserRecordVOList) {
                    if (Objects.equals(userRecord.getSendStatus(), 1L)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    for (Long userRecordId : userRecordIds) {
                        weixinMaSubscriptUserRecordMapper.sendMessage(userRecordId);
                    }
                    weixinMaSubscriptUserRecordVO.setSendStatus(1L);
                }

            });

            resultSubscriptUserRecordVOList = userRecordVOList.stream()
                    .filter(message -> Objects.equals(message.getSendStatus(),0L))
                    .collect(Collectors.toList());
        }

        tmessageVO.setUserRecords(resultSubscriptUserRecordVOList);
        return ServerResponseEntity.success(tmessageVO);
    }

    @Override
    public ServerResponseEntity<WeixinMaSubscriptTmessageVO> getinsIderSubscriptTmessage(Integer sendType, List<String> bussinessIds) {
        return getSubscriptTmessage(sendType,bussinessIds);
    }

    @Override
    public ServerResponseEntity<Void> updateUserRecordId(Long userRecordId) {
        weixinMaSubscriptUserRecordMapper.sendMessageUpdateStatus(userRecordId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> cancelUserSubscriptRecord(Long userId, Integer sendType, Long bussinessIds) {
        weixinMaSubscriptUserRecordMapper.cancelUserSubscriptRecord(userId,sendType,bussinessIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<String> generateScheme(@RequestBody WxMaGenerateSchemeRequest request) {
        try {
            return ServerResponseEntity.success(wxMaApiService.generateScheme(request));
        } catch (WxErrorException e) {
            log.error("调用微信接口异常",e);
            throw new LuckException(e.getMessage());
        }
    }

}
