package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.feign.AuthSocialFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.product.vo.ScoreProductVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.dto.ApplySProductSubscriptTMessageDTO;
import com.mall4j.cloud.biz.dto.ApplySubscriptTMessageDTO;
import com.mall4j.cloud.biz.dto.ApplySubscriptTMessageOpenIdDTO;
import com.mall4j.cloud.biz.dto.ScoreProductSubscriptRecordDTO;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageValueMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptUserBussinessRecordMapper;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptUserRecordMapper;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessage;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserBussinessRecord;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserRecord;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptUserRecordService;
import com.mall4j.cloud.biz.vo.ScoreProductSubscriptRecordVO;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 微信小程序用户订阅记录
 *
 * @author FrozenWatermelon
 * @date 2022-03-23 11:10:56
 */
@Service
public class WeixinMaSubscriptUserRecordServiceImpl implements WeixinMaSubscriptUserRecordService {

    @Autowired
    private WeixinMaSubscriptUserRecordMapper weixinMaSubscriptUserRecordMapper;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    WeixinMaSubscriptTmessageMapper weixinMaSubscriptTmessageMapper;

    @Autowired
    WeixinMaSubscriptUserBussinessRecordMapper weixinMaSubscriptUserBussinessRecordMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private AuthSocialFeignClient authSocialFeignClient;

    @Autowired
    private WeixinMaSubscriptTmessageValueMapper weixinMaSubscriptTmessageValueMapper;

    @Autowired
    private ConfigFeignClient configFeign;

    private final static String H5_ACTIVITY_MESSAGE_CONTENT="H5_ACTIVITY_MESSAGE_CONTENT";


    @Override
    public PageVO<WeixinMaSubscriptUserRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinMaSubscriptUserRecordMapper.list());
    }

    @Override
    public WeixinMaSubscriptUserRecord getById(Long id) {
        return weixinMaSubscriptUserRecordMapper.getById(id);
    }

    @Override
    public void save(WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord) {
        weixinMaSubscriptUserRecordMapper.save(weixinMaSubscriptUserRecord);
    }

    @Override
    public void update(WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord) {
        weixinMaSubscriptUserRecordMapper.update(weixinMaSubscriptUserRecord);
    }

    @Override
    public void deleteById(Long id) {
        weixinMaSubscriptUserRecordMapper.deleteById(id);
    }

    @Override
    public void applySubscriptTMessage(ApplySubscriptTMessageDTO applySubscriptTMessageDTO) {
        ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(applySubscriptTMessageDTO.getUserId());
        if(userResponse==null || !userResponse.isSuccess() || userResponse.getData()==null ){
            Assert.faild("获取用户openid失败");
        }
        WeixinMaSubscriptTmessage weixinMaSubscriptTmessage = weixinMaSubscriptTmessageMapper.getById(applySubscriptTMessageDTO.getTemplateId());
        Assert.isNull(weixinMaSubscriptTmessage,"模版id不存在。");
        WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord = new WeixinMaSubscriptUserRecord();
        weixinMaSubscriptUserRecord.setId(RandomUtil.getUniqueNum());
        weixinMaSubscriptUserRecord.setBussinessId(applySubscriptTMessageDTO.getBussinessId());
        weixinMaSubscriptUserRecord.setCreateTime(new Date());
        weixinMaSubscriptUserRecord.setSendType(weixinMaSubscriptTmessage.getSendType());
        weixinMaSubscriptUserRecord.setSendStatus(0);
        weixinMaSubscriptUserRecord.setTemplateId(applySubscriptTMessageDTO.getTemplateId());
        weixinMaSubscriptUserRecord.setUserId(applySubscriptTMessageDTO.getUserId());
        weixinMaSubscriptUserRecord.setOpenId(userResponse.getData().getOpenId());
        save(weixinMaSubscriptUserRecord);
    }

    @Override
    public void applySProductSubscriptTMessage(ApplySProductSubscriptTMessageDTO tMessageDto) {
        String storeCode = tMessageDto.getStoreCode();
        Assert.isNull(storeCode,"店铺编号不能为空。");
        Long convertId = tMessageDto.getConvertId();
        Assert.isNull(convertId,"活动编号不能为空。");
        List<Long> spuIdList = tMessageDto.getSpuIdList();
        Assert.isNull(spuIdList,"spuId集合不能为空。");

        if(CollectionUtil.isNotEmpty(spuIdList)){
            ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(tMessageDto.getUserId());
            if(userResponse==null || !userResponse.isSuccess() || userResponse.getData()==null ){
                Assert.faild("获取用户openid失败");
            }
            WeixinMaSubscriptTmessage weixinMaSubscriptTmessage = weixinMaSubscriptTmessageMapper.getById(tMessageDto.getTemplateId());
            Assert.isNull(weixinMaSubscriptTmessage,"模版id不存在。");
            Long bussinessRecordNo = RandomUtil.getUniqueNum();

            for (Long spuId : spuIdList) {
                StringBuilder builderBussinessId = new StringBuilder();
                builderBussinessId.append(tMessageDto.getStoreCode()).append("-").append(spuId).append("-").append(tMessageDto.getConvertId());

                WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord = new WeixinMaSubscriptUserRecord();
                weixinMaSubscriptUserRecord.setId(RandomUtil.getUniqueNum());
                weixinMaSubscriptUserRecord.setBussinessId(builderBussinessId.toString());
                weixinMaSubscriptUserRecord.setCreateTime(new Date());
                weixinMaSubscriptUserRecord.setSendType(weixinMaSubscriptTmessage.getSendType());
                weixinMaSubscriptUserRecord.setSendStatus(0);
                weixinMaSubscriptUserRecord.setTemplateId(tMessageDto.getTemplateId());
                weixinMaSubscriptUserRecord.setUserId(tMessageDto.getUserId());
                weixinMaSubscriptUserRecord.setOpenId(userResponse.getData().getOpenId());
                save(weixinMaSubscriptUserRecord);

                WeixinMaSubscriptUserBussinessRecord bussinessRecord = new WeixinMaSubscriptUserBussinessRecord();
                bussinessRecord.setUserRecordId(weixinMaSubscriptUserRecord.getId());
                bussinessRecord.setBussinessRecordNo(bussinessRecordNo);
                weixinMaSubscriptUserBussinessRecordMapper.save(bussinessRecord);

                //添加到缓存
                ScoreProductVO scoreProductVO = new ScoreProductVO();
                scoreProductVO.setStoreCode(tMessageDto.getStoreCode());
                scoreProductVO.setSpuId(spuId);
                redisTemplate.opsForSet().add(ProductCacheNames.SCORE_PRODUCT_LIST, Json.toJsonString(scoreProductVO));
            }
        }

    }

    /**
     * 查询用户是否订阅过积分礼品到货通知
     * @param subscriptRecordDTO
     * @return
     */
    @Override
    public List<ScoreProductSubscriptRecordVO> queryScoreProductSubscriptRecord(ScoreProductSubscriptRecordDTO subscriptRecordDTO) {

        List<String> storeCodeList = subscriptRecordDTO.getStoreCodeList();
        Assert.isNull(storeCodeList,"店铺编号集合不能为空。");
        List<Long> spuIdList = subscriptRecordDTO.getSpuIdList();
        Assert.isNull(spuIdList,"spuId集合不能为空。");
        Long convertId = subscriptRecordDTO.getConvertId();
        Assert.isNull(convertId,"活动编号不能为空。");

        //按照店铺为单位处理
        List<ScoreProductSubscriptRecordVO> RecordVOList = storeCodeList.stream().map( storeCode -> {
            ScoreProductSubscriptRecordVO scoreProductSubscriptRecordVO = new ScoreProductSubscriptRecordVO();
            scoreProductSubscriptRecordVO.setMessageStatus(0);
            scoreProductSubscriptRecordVO.setStoreCode(storeCode);

            List<String> bussinessIdList = new ArrayList<>();

            spuIdList.forEach (spuId -> {
                //组装bussinessId
                StringBuilder builderBussinessId = new StringBuilder();
                String bussinessId = builderBussinessId.append(storeCode).append("-").append(spuId).append("-").append(convertId).toString();
                bussinessIdList.add(bussinessId);
            });

            //根据用户id和业务id集合查询记录关联表数据
            if(CollectionUtil.isNotEmpty(bussinessIdList)){
                List<WeixinMaSubscriptUserBussinessRecord> dbUserBussinessRecordList = weixinMaSubscriptUserBussinessRecordMapper.getUserRecordByUserIdAndBusIds(subscriptRecordDTO.getUserId(),bussinessIdList);

                //按业务编号分组
                if(CollectionUtil.isNotEmpty(dbUserBussinessRecordList)){
                    Map<Long, List<WeixinMaSubscriptUserBussinessRecord>> bussinessRecordNoMap = dbUserBussinessRecordList.stream().collect(Collectors.groupingBy(WeixinMaSubscriptUserBussinessRecord::getBussinessRecordNo));

                    //判断是否订阅过，0-未订阅,1-已订阅
                    bussinessRecordNoMap.forEach( (key ,value) ->{
                        if(value.size() == bussinessIdList.size()){
                            scoreProductSubscriptRecordVO.setMessageStatus(1);
                        }else if(value.size() < bussinessIdList.size()){
                            //修改发送过订阅消息的订阅记录状态
                            List<Long> userRecordIds = value.stream().map(WeixinMaSubscriptUserBussinessRecord::getUserRecordId).collect(Collectors.toList());
                            for (Long userRecordId : userRecordIds) {
                                weixinMaSubscriptUserRecordMapper.sendMessage(userRecordId);
                            }
                        }
                    });

                }
            }
            return scoreProductSubscriptRecordVO;
        }).collect(Collectors.toList());

        return RecordVOList;
    }

    @Override
    public String applyActivitySubscriptTMessage(ApplySubscriptTMessageOpenIdDTO dto) {
        long userId=-1L;
        String unionid = "";
        String openid = "";
        if(Objects.nonNull(dto.getUserid())) {
            ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(dto.getUserid());
            if (userResponse == null || !userResponse.isSuccess() || userResponse.getData() == null) {
                Assert.faild("获取用户openid失败");
            }
            userId = userResponse.getData().getUserId();
            unionid = userResponse.getData().getUnionId();
            openid = userResponse.getData().getOpenId();
        }else{
            if(StringUtils.isBlank(dto.getTempUid())){
                Assert.faild("未获取到临时用户ID");
            }
            ServerResponseEntity<AuthSocialVO> socialVOServerResponseEntity = authSocialFeignClient.getByTempUid(dto.getTempUid());
            if(socialVOServerResponseEntity.isFail() || Objects.isNull(socialVOServerResponseEntity.getData())){
                Assert.faild("获取用户openid失败");
            }
            unionid=socialVOServerResponseEntity.getData().getBizUnionid();
            openid = socialVOServerResponseEntity.getData().getBizUserId();
        }

        if(StringUtils.isBlank(openid)){
            Assert.faild("获取用户openid失败");
        }

        if(dto.isSubscriptSucc()) {
            WeixinMaSubscriptTmessage weixinMaSubscriptTmessage = weixinMaSubscriptTmessageMapper.getById(dto.getTemplateId());
            Assert.isNull(weixinMaSubscriptTmessage, "模版id不存在。");
            WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord = new WeixinMaSubscriptUserRecord();
            weixinMaSubscriptUserRecord.setId(RandomUtil.getUniqueNum());
            weixinMaSubscriptUserRecord.setBussinessId(dto.getBussinessId());
            weixinMaSubscriptUserRecord.setCreateTime(new Date());
            weixinMaSubscriptUserRecord.setSendType(weixinMaSubscriptTmessage.getSendType());
            weixinMaSubscriptUserRecord.setSendStatus(0);
            weixinMaSubscriptUserRecord.setTemplateId(dto.getTemplateId());
            weixinMaSubscriptUserRecord.setUserId(userId);
            weixinMaSubscriptUserRecord.setOpenId(openid);
            save(weixinMaSubscriptUserRecord);
        }

        ServerResponseEntity<String> config = configFeign.getConfig(H5_ACTIVITY_MESSAGE_CONTENT);
        if(config.isSuccess() && StringUtils.isNotBlank(config.getData())){
            Map<String,String> configInfoMap = JSON.parseObject(config.getData(), Map.class);
            return configInfoMap.get("url")+unionid;
        }

        return "";
    }

    @Override
    public WeixinMaSubscriptTmessageVO queryMaSubscriptUserRecordBySendType(Integer sendType, Date beginTime, Date endTime) {
        WeixinMaSubscriptTmessageVO tmessageVO = weixinMaSubscriptTmessageMapper.getBySendType(sendType);
        tmessageVO.setTmessageValues(weixinMaSubscriptTmessageValueMapper.getByTmessageId(tmessageVO.getId()));
        List<WeixinMaSubscriptUserRecordVO> list = weixinMaSubscriptUserRecordMapper.getByTemplateIdAndBegTimeAndEndTime(tmessageVO.getId(),beginTime,endTime).stream()
                .collect(Collectors.groupingBy(WeixinMaSubscriptUserRecordVO::getOpenId))
                .entrySet().stream().map(e -> e.getValue().stream().findFirst().get()
                ).collect(Collectors.toList());

        tmessageVO.setUserRecords(list);
        return tmessageVO;
    }
}
