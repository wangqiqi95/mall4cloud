package com.mall4j.cloud.distribution.controller.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.BizCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUpdateDTO;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivitySizesMapper;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.model.DistributionStoreActivitySizes;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityService;
import com.mall4j.cloud.distribution.service.DistributionStoreActivitySizesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
@Slf4j
@RestController("platformDistributionStoreActivityController")
@RequestMapping("/p/distribution_store_activity")
@Api(tags = "平台端-门店活动")
public class DistributionStoreActivityController {

    @Autowired
    private DistributionStoreActivityService distributionStoreActivityService;
    @Resource
    private DistributionStoreActivitySizesMapper distributionStoreActivitySizesMapper;
    @Autowired
    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Autowired
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取门店活动列表", notes = "分页获取门店活动列表")
	public ServerResponseEntity<PageVO<DistributionStoreActivity>> page(@Valid PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO) {
		PageVO<DistributionStoreActivity> distributionStoreActivityPage = distributionStoreActivityService.page(pageDTO, distributionStoreActivityQueryDTO);
		return ServerResponseEntity.success(distributionStoreActivityPage);
	}

	@GetMapping("/getById")
    @ApiOperation(value = "获取门店活动", notes = "根据id获取门店活动")
    public ServerResponseEntity<DistributionStoreActivityDTO> getById(@RequestParam Long id) {
        DistributionStoreActivity distributionStoreActivity = distributionStoreActivityService.getById(id);
        DistributionStoreActivityDTO distributionStoreActivityDTO = mapperFacade.map(distributionStoreActivity, DistributionStoreActivityDTO.class);
        if (distributionStoreActivity.getNeedClothes() == 1) {
            List<String> clothesSizes = distributionStoreActivitySizesMapper.selectList(new LambdaQueryWrapper<DistributionStoreActivitySizes>().eq(DistributionStoreActivitySizes::getActivityId, distributionStoreActivity.getId()).eq(DistributionStoreActivitySizes::getType, 1)).stream().map(DistributionStoreActivitySizes::getSize).collect(Collectors.toList());
            distributionStoreActivityDTO.setClothesSizes(clothesSizes);
        }
        if (distributionStoreActivity.getNeedShoes() == 1) {
            List<String> shoesSizes = distributionStoreActivitySizesMapper.selectList(new LambdaQueryWrapper<DistributionStoreActivitySizes>().eq(DistributionStoreActivitySizes::getActivityId, distributionStoreActivity.getId()).eq(DistributionStoreActivitySizes::getType, 2)).stream().map(DistributionStoreActivitySizes::getSize).collect(Collectors.toList());
            distributionStoreActivityDTO.setShoesSizes(shoesSizes);
        }
        return ServerResponseEntity.success(distributionStoreActivityDTO);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存门店活动", notes = "保存门店活动")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionStoreActivityDTO distributionStoreActivityDTO) {
	    log.info("门店活动入参：{}",JSONObject.toJSONString(distributionStoreActivityDTO));
        DistributionStoreActivity distributionStoreActivity = mapperFacade.map(distributionStoreActivityDTO, DistributionStoreActivity.class);
        log.info("门店活动保存参数：{}",JSONObject.toJSONString(distributionStoreActivity));
        distributionStoreActivity.setId(null);
        distributionStoreActivity.setOrgId(0l);
        if (distributionStoreActivity.getNeedClothes() == 1 && CollectionUtils.isEmpty(distributionStoreActivityDTO.getClothesSizes())) {
            throw new LuckException("需填写衣服尺码信息！");
        }
        if (distributionStoreActivity.getNeedShoes() == 1 && CollectionUtils.isEmpty(distributionStoreActivityDTO.getShoesSizes())) {
            throw new LuckException("需填写鞋子尺码信息！");
        }
        if (distributionStoreActivity.getStartRemind() != null) {
            LocalDateTime localDateTime = distributionStoreActivity.getStartTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime().plusHours(distributionStoreActivity.getStartRemind() * -1);
            distributionStoreActivity.setStartRemindTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
        if (distributionStoreActivity.getEndRemind() != null) {
            LocalDateTime localDateTime = distributionStoreActivity.getEndTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime().plusHours(distributionStoreActivity.getStartRemind());
            distributionStoreActivity.setEndRemindTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
        distributionStoreActivityService.save(distributionStoreActivity);
        if (distributionStoreActivity.getNeedClothes() == 1 && !CollectionUtils.isEmpty(distributionStoreActivityDTO.getClothesSizes())) {
            distributionStoreActivityDTO.getClothesSizes().forEach( v -> {
                DistributionStoreActivitySizes distributionStoreActivitySizes = new DistributionStoreActivitySizes();
                distributionStoreActivitySizes.setActivityId(distributionStoreActivity.getId());
                distributionStoreActivitySizes.setType(1);
                distributionStoreActivitySizes.setSize(v);
                distributionStoreActivitySizesMapper.insert(distributionStoreActivitySizes);
            });
        }

        if (distributionStoreActivity.getNeedShoes() == 1 && !CollectionUtils.isEmpty(distributionStoreActivityDTO.getShoesSizes())) {
            distributionStoreActivityDTO.getShoesSizes().forEach( v -> {
                DistributionStoreActivitySizes distributionStoreActivitySizes = new DistributionStoreActivitySizes();
                distributionStoreActivitySizes.setActivityId(distributionStoreActivity.getId());
                distributionStoreActivitySizes.setType(2);
                distributionStoreActivitySizes.setSize(v);
                distributionStoreActivitySizesMapper.insert(distributionStoreActivitySizes);
            });

        }

//        try {
//            // 订阅消息提醒
//            if (distributionStoreActivity.getStatus() == 1) {
//                ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getSubscriptTmessage(MaSendTypeEnum.ACTIVITY_NEW.getValue(),
//                        null);
//                log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
//                if (subscriptResp.isSuccess()) {
//                    WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
//                    List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
//                    if (!CollectionUtils.isEmpty(userRecords)) {
//                        List<WeixinMaSubscriptTmessageSendVO> notifyList = new ArrayList<>();
//                        for (WeixinMaSubscriptUserRecordVO userRecord : userRecords) {
//                            //判断是否给当前会员发送订阅消息
//                            if(!checkIsCanSend(userRecord.getUserId())){
//                                continue;
//                            }
//                            Map<String,String> paramMap = new HashMap();
//                            paramMap.put("{activityName}", distributionStoreActivity.getName());
//                            paramMap.put("{activityRemark}", distributionStoreActivity.getAddress());
//                            paramMap.put("{startTime}", DateUtils.dateToString(distributionStoreActivity.getStartTime()));
//                            paramMap.put("{endTime}", DateUtils.dateToString(distributionStoreActivity.getEndTime()));
//                            List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
//                                WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
//                                msgData.setName(t.getTemplateValueName());
//                                msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
//                                return msgData;
//                            }).collect(Collectors.toList());
//                            WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
//                            sendVO.setUserSubscriptRecordId(userRecord.getId());
//                            sendVO.setData(msgDataList);
//                            sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
//                            notifyList.add(sendVO);
//                        }
//
//                        sendMaSubcriptMessageTemplate.syncSend(notifyList);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("门店活动上新订阅通知发送异常",e);
//        }
        return ServerResponseEntity.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新门店活动", notes = "更新门店活动")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionStoreActivityDTO distributionStoreActivityDTO) {
        DistributionStoreActivity distributionStoreActivity = mapperFacade.map(distributionStoreActivityDTO, DistributionStoreActivity.class);
        if (distributionStoreActivity.getNeedClothes() == 1 && CollectionUtils.isEmpty(distributionStoreActivityDTO.getClothesSizes())) {
            throw new LuckException("需填写衣服尺码信息！");
        }
        if (distributionStoreActivity.getNeedShoes() == 1 && CollectionUtils.isEmpty(distributionStoreActivityDTO.getShoesSizes())) {
            throw new LuckException("需填写鞋子尺码信息！");
        }
        if (distributionStoreActivity.getStartRemind() != null) {
            LocalDateTime localDateTime = distributionStoreActivity.getStartTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime().plusHours(distributionStoreActivity.getStartRemind() * -1);
            distributionStoreActivity.setStartRemindTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
        if (distributionStoreActivity.getEndRemind() != null) {
            LocalDateTime localDateTime = distributionStoreActivity.getEndTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime().plusHours(distributionStoreActivity.getStartRemind());
            distributionStoreActivity.setEndRemindTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
        distributionStoreActivityService.update(distributionStoreActivity);

        if (distributionStoreActivity.getNeedClothes() == 1 && !CollectionUtils.isEmpty(distributionStoreActivityDTO.getClothesSizes())) {
            distributionStoreActivitySizesMapper.delete(new LambdaQueryWrapper<DistributionStoreActivitySizes>().eq(DistributionStoreActivitySizes::getActivityId,distributionStoreActivity.getId()).eq(DistributionStoreActivitySizes::getType,1));
            distributionStoreActivityDTO.getClothesSizes().forEach( v -> {
                DistributionStoreActivitySizes distributionStoreActivitySizes = new DistributionStoreActivitySizes();
                distributionStoreActivitySizes.setActivityId(distributionStoreActivity.getId());
                distributionStoreActivitySizes.setType(1);
                distributionStoreActivitySizes.setSize(v);
                distributionStoreActivitySizesMapper.insert(distributionStoreActivitySizes);
            });
        }

        if (distributionStoreActivity.getNeedShoes() == 1 && !CollectionUtils.isEmpty(distributionStoreActivityDTO.getShoesSizes())) {
            distributionStoreActivitySizesMapper.delete(new LambdaQueryWrapper<DistributionStoreActivitySizes>().eq(DistributionStoreActivitySizes::getActivityId,distributionStoreActivity.getId()).eq(DistributionStoreActivitySizes::getType,2));
            distributionStoreActivityDTO.getShoesSizes().forEach( v -> {
                DistributionStoreActivitySizes distributionStoreActivitySizes = new DistributionStoreActivitySizes();
                distributionStoreActivitySizes.setActivityId(distributionStoreActivity.getId());
                distributionStoreActivitySizes.setType(2);
                distributionStoreActivitySizes.setSize(v);
                distributionStoreActivitySizesMapper.insert(distributionStoreActivitySizes);
            });
        }

//        if(distributionStoreActivity.getStatus() == 1){
//            activityNewSendMaMessage(distributionStoreActivityDTO);
//        }
//        try {
//            // 订阅消息提醒
//            if (distributionStoreActivity.getStatus() == 1) {
//                ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getSubscriptTmessage(MaSendTypeEnum.ACTIVITY_NEW.getValue(),
//                        null);
//                log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
//                if (subscriptResp.isSuccess()) {
//                    WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
//                    List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
//                    if (!CollectionUtils.isEmpty(userRecords)) {
//                        List<WeixinMaSubscriptTmessageSendVO> notifyList = new ArrayList<>();
//                        for (WeixinMaSubscriptUserRecordVO userRecord : userRecords) {
//                            //判断是否给当前会员发送订阅消息
//                            if(!checkIsCanSend(userRecord.getUserId())){
//                                continue;
//                            }
//                            Map<String,String> paramMap = new HashMap();
//                            paramMap.put("{activityName}", distributionStoreActivity.getName());
//                            paramMap.put("{activityRemark}", distributionStoreActivity.getAddress());
//                            paramMap.put("{startTime}", DateUtils.dateToString(distributionStoreActivity.getStartTime()));
//                            paramMap.put("{endTime}", DateUtils.dateToString(distributionStoreActivity.getEndTime()));
//                            List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
//                                WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
//                                msgData.setName(t.getTemplateValueName());
//                                msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
//                                return msgData;
//                            }).collect(Collectors.toList());
//                            WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
//                            sendVO.setUserSubscriptRecordId(userRecord.getId());
//                            sendVO.setData(msgDataList);
//                            sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
//                            notifyList.add(sendVO);
//                        }
//                        sendMaSubcriptMessageTemplate.syncSend(notifyList);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("门店活动上新订阅通知发送异常",e);
//        }
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新门店活动状态", notes = "更新门店活动状态")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody DistributionStoreActivityUpdateDTO distributionStoreActivityUpdateDTO) {
        distributionStoreActivityService.updateStatus(distributionStoreActivityUpdateDTO);

//        if(distributionStoreActivityUpdateDTO.getStatus() == 1){
//            activityNewSendMaMessage(distributionStoreActivityUpdateDTO);
//        }
        return ServerResponseEntity.success();
    }

    private void activityNewSendMaMessage(DistributionStoreActivityUpdateDTO distributionStoreActivityUpdateDTO){
        try {
            DistributionStoreActivity distributionStoreActivity = distributionStoreActivityService.getById(distributionStoreActivityUpdateDTO
                    .getIdList().get(0));
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getSubscriptTmessage(MaSendTypeEnum.ACTIVITY_NEW.getValue(),
                    null);
            log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
            if (subscriptResp.isSuccess()) {
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
                if (!CollectionUtils.isEmpty(userRecords)) {
                    List<WeixinMaSubscriptTmessageSendVO> notifyList = new ArrayList<>();
                    for (WeixinMaSubscriptUserRecordVO userRecord : userRecords) {
                        //判断是否给当前会员发送订阅消息
                        if(!checkIsCanSend(userRecord.getUserId())){
                            continue;
                        }
                        Map<String, String> paramMap = new HashMap();
                        paramMap.put("{activityName}", distributionStoreActivity.getName());
                        paramMap.put("{activityRemark}", distributionStoreActivity.getAddress());
                        paramMap.put("{startTime}", DateUtils.dateToString(distributionStoreActivity.getStartTime()));
                        paramMap.put("{endTime}", DateUtils.dateToString(distributionStoreActivity.getEndTime()));
                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());
                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(userRecord.getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                        notifyList.add(sendVO);
                    }
                    sendMaSubcriptMessageTemplate.syncSend(notifyList);
                }
            }
        } catch (Exception e) {
            log.error("门店活动上新订阅通知发送异常",e);
        }
    }

    /**
     * 活动上新，一天，一个会员只可以发送一次订阅消息
     * @param userId
     * @return
     */
    public Boolean checkIsCanSend(Long userId){
        String today = DateUtil.today();
        String userSendKey = BizCacheNames.MA_MESSAGE_ACTIVITY_NEW + today + ":" + userId + ":";
        //不为空，说明当前会员已经发送过订阅消息。
        if(StrUtil.isNotEmpty(RedisUtil.get(userSendKey))){
            return false;
        }
        RedisUtil.set(userSendKey,userId,60*60*24);
        return true;
    }


    @PostMapping("/importExcel")
    @ApiOperation(value = "导入尺码")
    public ServerResponseEntity<List<String>> importExcel(@RequestParam("file") MultipartFile file) {
	    try {
            return ServerResponseEntity.success(distributionStoreActivityService.importExcel(file));
        } catch (Exception e) {
            throw new LuckException("导入失败");
        }
    }
}
