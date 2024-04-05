package com.mall4j.cloud.coupon.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.bo.ExportUserToCreateEventBO;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.ChooseMemberEventExchangeTypeEnum;
import com.mall4j.cloud.coupon.dto.AddChooseMemberEventDTO;
import com.mall4j.cloud.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.coupon.dto.ExchangeChooseMemberEventDTO;
import com.mall4j.cloud.coupon.dto.QueryChooseMemberEventPageDTO;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventExchangeRecordMapper;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventMapper;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventMobileRelationMapper;
import com.mall4j.cloud.coupon.model.ChooseMemberEvent;
import com.mall4j.cloud.coupon.model.ChooseMemberEventMobileRelation;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventCouponRelationVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventStatisticsVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChooseMemberEventManager {

    @Autowired
    private ChooseMemberEventMapper chooseMemberEventMapper;

    @Autowired
    private ChooseMemberEventMobileRelationMapper chooseMemberEventMobileRelationMapper;

    @Autowired
    private ChooseMemberEventExchangeRecordMapper chooseMemberEventExchangeRecordMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    public PageVO<ChooseMemberEventVO> getChooseMemberEventManagerPage(QueryChooseMemberEventPageDTO pageDTO){
        LambdaQueryWrapper<ChooseMemberEvent> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(pageDTO.getExchangeType())){
            queryWrapper
                    .eq(ChooseMemberEvent::getExchangeType, pageDTO.getExchangeType());
        }

        if (Objects.nonNull(pageDTO.getEventEnabledStatus())){
            queryWrapper
                    .eq(ChooseMemberEvent::getEventEnabledStatus, pageDTO.getEventEnabledStatus());
        }

        if (StringUtils.isNotEmpty(pageDTO.getEventTitle())){
            queryWrapper
                    .like(ChooseMemberEvent::getEventTitle, pageDTO.getEventTitle());
        }

        queryWrapper.orderByDesc(ChooseMemberEvent::getCreateTime);

        Page<ChooseMemberEvent> chooseMemberEventPage = chooseMemberEventMapper
                .selectPage(new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize()), queryWrapper);

        List<ChooseMemberEventVO> chooseMemberEventVOS = chooseMemberEventPage.getRecords()
                .stream()
                .map(memberEvent -> {
                    ChooseMemberEventVO chooseMemberEventVO = new ChooseMemberEventVO();
                    BeanUtils.copyProperties(memberEvent, chooseMemberEventVO);
                    return chooseMemberEventVO;
                }).collect(Collectors.toList());

        PageVO<ChooseMemberEventVO> pageData = new PageVO<>();
        pageData.setPages((int)chooseMemberEventPage.getPages());
        pageData.setTotal(chooseMemberEventPage.getTotal());
        pageData.setList(chooseMemberEventVOS);
        return pageData;
    }

    public List<String> exportUser(MultipartFile file){
        log.info("导入文件为：{}", file);
        if (file == null) {
            throw new LuckException("导入文件不能为空！");
        }

        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("获取输入流异常");
            e.printStackTrace();
        }

        //调用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //校验模板的正确性
        List<Object> objects = excelReader.readRow(0);
        log.info("模板表头信息：{}", JSONObject.toJSONString(objects));

        //列名和对象属性名一致
        excelReader.addHeaderAlias("手机号", "mobile");

        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
        List<ExportUserToCreateEventBO> importObjects = excelReader.readAll(ExportUserToCreateEventBO.class);
        List<String> mobileList = importObjects.stream()
                .map(ExportUserToCreateEventBO::getMobile)
                .collect(Collectors.toList());

        return mobileList;
    }

    public void eventTimeValid(LocalDateTime startTime, LocalDateTime endTime){
        long startMilli = startTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long endMilli = endTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        long currentMilli = new Date().getTime();

        if (currentMilli < startMilli){
            throw new LuckException(ResponseEnum.CHOOSE_MEMBER_EVENT_START_TIME_VALID);
        }

        if (currentMilli > endMilli){
            throw new LuckException(ResponseEnum.CHOOSE_MEMBER_EVENT_END_TIME_VALID);
        }
    }

    public void exchangeNumValid(ChooseMemberEvent memberEvent, ExchangeChooseMemberEventDTO exchangeChooseMemberEventDTO,
                                 Integer theUserEventExchangeNum){
        //判断剩余库存数量是否充足，stockNum为0时库存数量和剩余库存量为无限制
        if (memberEvent.getStockNum().compareTo(0) > 0
                && memberEvent.getLastStockNum().compareTo(exchangeChooseMemberEventDTO.getExchangeNum()) < 0){
            throw new LuckException("兑换库存不足，无法进行兑换");
        }

        //判断是否超过可兑换数量
        if (!memberEvent.getRestrictNum().equals(0) && memberEvent.getRestrictNum().compareTo(exchangeChooseMemberEventDTO.getExchangeNum()) < 0){
            throw new LuckException("每个用户的最大兑换数量为:"+memberEvent.getRestrictNum());
        }

        //计算剩余可兑数量
        Integer lastExchangeNum = memberEvent.getRestrictNum() - theUserEventExchangeNum;
        //计算用户兑换数量
        theUserEventExchangeNum += exchangeChooseMemberEventDTO.getExchangeNum();
        if (theUserEventExchangeNum.compareTo(memberEvent.getRestrictNum()) >0){
            throw new LuckException("抱歉，您当前领取数量已达上限");
        }
    }

    public void recipientValid(ChooseMemberEvent memberEvent, ExchangeChooseMemberEventDTO exchangeChooseMemberEventDTO){
        //判断当前活动是否为兑礼到家活动和当前用户是否填写了相关的收件信息
        if (memberEvent.getExchangeType().equals(ChooseMemberEventExchangeTypeEnum.TO_HOME.getExchangeType())){
            if (StringUtils.isEmpty(exchangeChooseMemberEventDTO.getConsignee())
                    || StringUtils.isEmpty(exchangeChooseMemberEventDTO.getDeliveryMobile())
                    || StringUtils.isEmpty(exchangeChooseMemberEventDTO.getDeliveryAddress())){
                throw new LuckException("当前活动需要填写收件人相关信息");
            }
        }
    }

    public BatchReceiveCouponDTO wrapperBatchReceiveCouponDTO(List<ChooseMemberEventCouponRelationVO> couponRelationList, ChooseMemberEvent memberEvent,
                                                              Long userId){
        //聚合优惠券ID
        List<Long> couponIdList = couponRelationList.stream()
                .map(ChooseMemberEventCouponRelationVO::getCouponId).collect(Collectors.toList());
        //拼装批量发券的参数
        BatchReceiveCouponDTO batchReceiveCouponDTO = new BatchReceiveCouponDTO();
        batchReceiveCouponDTO.setCouponIds(couponIdList);
        batchReceiveCouponDTO.setActivityId(memberEvent.getChooseMemberEventId());
        batchReceiveCouponDTO.setUserId(userId);
        batchReceiveCouponDTO.setActivitySource(ActivitySourceEnum.CHOOSE_MEMBER_EVENT.value());

        return batchReceiveCouponDTO;
    }

    public ChooseMemberEventStatisticsVO eventStatistics(Long eventId) {
        ChooseMemberEvent memberEvent = chooseMemberEventMapper.selectById(eventId);
        Integer chooseMemberCount = chooseMemberEventMobileRelationMapper.selectCount(
                new LambdaQueryWrapper<ChooseMemberEventMobileRelation>()
                        .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, eventId)
        );
        Integer exchangeMemberCount = chooseMemberEventExchangeRecordMapper.getExchangeMemberCountByEvent(eventId);
        Integer notExchangeMemberCount  = chooseMemberCount - exchangeMemberCount;
        BigDecimal exchangeRate = new BigDecimal(exchangeMemberCount)
                .divide(new BigDecimal(chooseMemberCount), 2, BigDecimal.ROUND_DOWN);

        ChooseMemberEventStatisticsVO statisticsVO = new ChooseMemberEventStatisticsVO();
        statisticsVO.setEventId(eventId);
        statisticsVO.setEventTitle(memberEvent.getEventTitle());
        statisticsVO.setChooseMemberCount(chooseMemberCount);
        statisticsVO.setExchangeMemberCount(exchangeMemberCount);
        statisticsVO.setNotExchangeMemberCount(notExchangeMemberCount);
        statisticsVO.setExchangeRate(exchangeRate);
        return statisticsVO;
    }

    public UserApiVO eventMemberValid(ChooseMemberEvent memberEvent){

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        //判断用户是否登录
        if (Objects.isNull(userInfoInTokenBO)){
            throw new LuckException(ResponseEnum.CHOOSE_MEMBER_EVENT_NON_MEMBER_VALID,memberEvent.getNonMemberMessage());
        }

        UserApiVO userApiVO;
        ServerResponseEntity<UserApiVO> responseEntity = userFeignClient.getUserById(userInfoInTokenBO.getUserId());
        if (responseEntity.isFail() || Objects.isNull(userApiVO = responseEntity.getData())){
            throw new LuckException("用户信息获取失败，请稍后重试");
        }

        return userApiVO;

    }

    public ChooseMemberEventVO getEventById(Long eventId){
        ChooseMemberEvent chooseMemberEvent = chooseMemberEventMapper.selectById(eventId);
        ChooseMemberEventVO chooseMemberEventVO = new ChooseMemberEventVO();
        BeanUtils.copyProperties(chooseMemberEvent, chooseMemberEventVO);
        return chooseMemberEventVO;
    }

}
