package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.coupon.bo.AddChooseMemberEventExchangeRecordBO;
import com.mall4j.cloud.coupon.constant.ChooseMemberEventEnableEnum;
import com.mall4j.cloud.coupon.constant.ChooseMemberEventExchangeTypeEnum;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.manager.*;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventMapper;
import com.mall4j.cloud.coupon.model.ChooseMemberEvent;
import com.mall4j.cloud.coupon.model.ChooseMemberEventMobileRelation;
import com.mall4j.cloud.coupon.service.ChooseMemberEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 指定会员活动表（提供最具价值会员活动表） 服务实现类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Service
public class ChooseMemberEventServiceImpl extends ServiceImpl<ChooseMemberEventMapper, ChooseMemberEvent> implements ChooseMemberEventService {

    @Autowired
    private ChooseMemberEventManager chooseMemberEventManager;

    @Autowired
    private ChooseMemberEventShopRelationManager chooseMemberEventShopRelationManager;

    @Autowired
    private ChooseMemberEventMobileRelationManager chooseMemberEventMobileRelationManager;

    @Autowired
    private ChooseMemberEventCouponRelationManager chooseMemberEventCouponRelationManager;

    @Autowired
    private ChooseMemberEventExchangeRecordManager chooseMemberEventExchangeRecordManager;

    @Autowired
    private TCouponUserService tCouponUserService;

    @Autowired
    private UserFeignClient userFeignClient;

    //默认每人可兑换数
    private static final int DEFAULT_RESTRICT_NUM = 1;

    /**
     * 新增高价值会员活动方法
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity add(AddChooseMemberEventDTO addChooseMemberEventDTO) {

        //保存活动本身数据
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        ChooseMemberEvent chooseMemberEvent = new ChooseMemberEvent();
        //复制变量
        BeanUtils.copyProperties(addChooseMemberEventDTO, chooseMemberEvent);
        //设置剩余库存量
        chooseMemberEvent.setLastStockNum(addChooseMemberEventDTO.getStockNum());
        //校验并设置默认库存数量
        if (Objects.isNull(addChooseMemberEventDTO.getRestrictNum()) || addChooseMemberEventDTO.getRestrictNum() < DEFAULT_RESTRICT_NUM){
            chooseMemberEvent.setRestrictNum(DEFAULT_RESTRICT_NUM);
        }
        //设置创建和编辑人相关信息
        chooseMemberEvent.setCreateUser(userInfoInTokenBO.getUserId());
        chooseMemberEvent.setCreateUserName(userInfoInTokenBO.getUsername());
        chooseMemberEvent.setUpdateUser(userInfoInTokenBO.getUserId());
        chooseMemberEvent.setUpdateUserName(userInfoInTokenBO.getUsername());

        //保存
        save(chooseMemberEvent);

        //保存活动和店铺的关系
//        if (!addChooseMemberEventDTO.getIsAllShop()
//                && CollectionUtil.isNotEmpty(addChooseMemberEventDTO.getShopList())){
//            chooseMemberEventShopRelationManager.addList(chooseMemberEvent.getChooseMemberEventId(),
//                    addChooseMemberEventDTO.getShopList());
//        }

        //去重
        List<String> mobileList =  addChooseMemberEventDTO.getMobileList().stream().distinct().collect(Collectors.toList());
        //新增指定用户关系
        chooseMemberEventMobileRelationManager.addEventUserRelationBatch(chooseMemberEvent.getChooseMemberEventId(),
                mobileList);

        //如果是兑礼到店活动
        if (addChooseMemberEventDTO.getExchangeType().equals(ChooseMemberEventExchangeTypeEnum.TO_SHOP.getExchangeType())){

            if (addChooseMemberEventDTO.getCouponIdList().size() > 1){
                return ServerResponseEntity.showFailMsg("无法选择一个以上的优惠券项目");
            }

            if (CollectionUtil.isNotEmpty(addChooseMemberEventDTO.getCouponIdList())){
                chooseMemberEventCouponRelationManager.saveBatch(chooseMemberEvent.getChooseMemberEventId(),
                        addChooseMemberEventDTO.getCouponIdList());
            }
        }

        return ServerResponseEntity.success();
    }



    /**
     * 获取高价值会员活动分页数据
     * */
    @Override
    public ServerResponseEntity<PageVO<ChooseMemberEventVO>> getChooseMemberEventPage(QueryChooseMemberEventPageDTO pageDTO){

        PageVO<ChooseMemberEventVO> pageData = chooseMemberEventManager.getChooseMemberEventManagerPage(pageDTO);

//        pageData.getList().stream().forEach(event -> {
//            if (!event.getIsAllShop()){
//                List<ChooseMemberEventShopRelation> chooseMemberEventShopRelationList = chooseMemberEventShopRelationManager
//                        .getEventShopRelationList(event.getChooseMemberEventId());
//                long shopNum = chooseMemberEventShopRelationList.stream().count();
//                event.setShopNum(shopNum);
//            }
//        });
        return ServerResponseEntity.success(pageData);
    }

    /**
     * 兑换高价值会员活动物品
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<PageVO<ChooseMemberEventVO>> exchange(ExchangeChooseMemberEventDTO exchangeChooseMemberEventDTO) {

        //从缓存中获取当前用户信息
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        ServerResponseEntity<UserApiVO> userApiVOServerResponseEntity = userFeignClient.getUserById(userInfoInTokenBO.getUserId());
        UserApiVO userApiVO;
        if (userApiVOServerResponseEntity.isFail() || Objects.isNull(userApiVO = userApiVOServerResponseEntity.getData())){
            return ServerResponseEntity.showFailMsg("会员信息获取失败，请重试");
        }

        //查询当前活动信息
        ChooseMemberEvent memberEvent = getById(exchangeChooseMemberEventDTO.getEventId());

        if (memberEvent.getEventEnabledStatus().equals(ChooseMemberEventEnableEnum.DISABLE.getEnableStatus())){
            return ServerResponseEntity.showFailMsg("活动已失效，请联系工作人员");
        }

        //校验活动时间范围
        chooseMemberEventManager.eventTimeValid(memberEvent.getEventStartTime(), memberEvent.getEventEndTime());

        //判断用户是否登录
        if (Objects.isNull(userInfoInTokenBO)){
            return ServerResponseEntity.showFailMsg(memberEvent.getNonMemberMessage());
        }

        //判断用户是否被选中用户
        ChooseMemberEventMobileRelation eventUserRelation = chooseMemberEventMobileRelationManager.checkEventUserRelation(exchangeChooseMemberEventDTO.getEventId(),
                userApiVO.getPhone());

        if (Objects.isNull(eventUserRelation)){
            return ServerResponseEntity.showFailMsg(memberEvent.getNonChooseMemberMessage());
        }

        //获取当前用户的已兑换个数
        Integer theUserEventExchangeNum = chooseMemberEventExchangeRecordManager.getTheUserEventExchangeNum(userInfoInTokenBO.getUserId()
                , memberEvent.getChooseMemberEventId());
        //兑换数量校验
        chooseMemberEventManager.exchangeNumValid(memberEvent, exchangeChooseMemberEventDTO, theUserEventExchangeNum);
        //校验当前活动是否为兑礼到家活动，并校验是否填写了收货地址等相关信息
        chooseMemberEventManager.recipientValid(memberEvent, exchangeChooseMemberEventDTO);

        //如果当前的活动的库存数量为有限制的数量，修改当前剩余库存数量
        if (memberEvent.getStockNum().compareTo(0) > 0){
            memberEvent.setLastStockNum(memberEvent.getLastStockNum() - exchangeChooseMemberEventDTO.getExchangeNum());
        }
        //更新活动
        updateById(memberEvent);

        AddChooseMemberEventExchangeRecordBO eventExchangeRecordBO = new AddChooseMemberEventExchangeRecordBO();
        BeanUtils.copyProperties(exchangeChooseMemberEventDTO, eventExchangeRecordBO);
        eventExchangeRecordBO.setUserId(userInfoInTokenBO.getUserId());
        eventExchangeRecordBO.setUserVipCode(userApiVO.getVipcode());

        //判断是不是兑礼到店活动（领券活动）
        if (memberEvent.getExchangeType().equals(ChooseMemberEventExchangeTypeEnum.TO_SHOP.getExchangeType())){

            eventExchangeRecordBO.setDeliveryStatus(2);

            //查询当前活动绑定的所有优惠券
            List<ChooseMemberEventCouponRelationVO> couponRelationList = chooseMemberEventCouponRelationManager
                    .getListBYEventId(memberEvent.getChooseMemberEventId());

            BatchReceiveCouponDTO batchReceiveCouponDTO = chooseMemberEventManager
                    .wrapperBatchReceiveCouponDTO(couponRelationList, memberEvent, userInfoInTokenBO.getUserId());

            //进行批量发券
            tCouponUserService.batchReceive(batchReceiveCouponDTO);
        }


        //新增兑换记录
        chooseMemberEventExchangeRecordManager.addExchangeRecord(eventExchangeRecordBO);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<String>> exportUser(MultipartFile file) {
        List<String> userIdList = chooseMemberEventManager.exportUser(file);
        return ServerResponseEntity.success(userIdList);
    }

    /**
     * 活动统计
     * @param eventId 活动ID
     * */
    @Override
    public ServerResponseEntity<ChooseMemberEventStatisticsVO> eventStatistics(Long eventId) {

        ChooseMemberEventStatisticsVO statisticsVO = chooseMemberEventManager.eventStatistics(eventId);

        return ServerResponseEntity.success(statisticsVO);
    }

    @Override
    public ServerResponseEntity addStockNum(UpdateChooseMemberEventStockDTO addStockDTO) {
        ChooseMemberEvent chooseMemberEvent = getById(addStockDTO.getEventId());
        Integer addCount = chooseMemberEvent.getStockNum() + addStockDTO.getAddStockNum();
        if (addCount.compareTo(0) < 0){
            return ServerResponseEntity.showFailMsg("无法修改库存余额为负数");
        }
        chooseMemberEvent.setStockNum(chooseMemberEvent.getStockNum()+addStockDTO.getAddStockNum());
        Integer lastCount = chooseMemberEvent.getLastStockNum() + addStockDTO.getAddStockNum();
        if (lastCount.compareTo(0)<0){
            chooseMemberEvent.setLastStockNum(0);
        }else {
            chooseMemberEvent.setLastStockNum(chooseMemberEvent.getLastStockNum()+addStockDTO.getAddStockNum());
        }
        updateById(chooseMemberEvent);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity enableOrDisable(Long eventId) {

        ChooseMemberEvent chooseMemberEvent = getById(eventId);
        if (chooseMemberEvent.getEventEnabledStatus().equals(ChooseMemberEventEnableEnum.ENABLE.getEnableStatus())){
            chooseMemberEvent.setEventEnabledStatus(ChooseMemberEventEnableEnum.DISABLE.getEnableStatus());
        }else {
            chooseMemberEvent.setEventEnabledStatus(ChooseMemberEventEnableEnum.ENABLE.getEnableStatus());
        }

        updateById(chooseMemberEvent);
        return ServerResponseEntity.success();
    }

    @Override
    public void exportEventStatistics(HttpServletResponse response, Long eventId) {
        ChooseMemberEventStatisticsVO statisticsVO = chooseMemberEventManager.eventStatistics(eventId);

        ExcelUtil.soleExcel(response, Arrays.asList(statisticsVO), ChooseMemberEventStatisticsVO.EXCEL_NAME, ChooseMemberEventStatisticsVO.MERGE_ROW_INDEX,
                ChooseMemberEventStatisticsVO.MERGE_COLUMN_INDEX, ChooseMemberEventStatisticsVO.class);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity edit(EditChooseMemberEventDTO editChooseMemberEventDTO) {

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        ChooseMemberEvent chooseMemberEvent = getById(editChooseMemberEventDTO.getChooseMemberEventId());
        chooseMemberEvent.setEventTitle(editChooseMemberEventDTO.getEventTitle());
        chooseMemberEvent.setEventEnabledStatus(editChooseMemberEventDTO.getEventEnabledStatus());
        chooseMemberEvent.setCoverImageUrl(editChooseMemberEventDTO.getCoverImageUrl());
        chooseMemberEvent.setGiftImageUrl(editChooseMemberEventDTO.getGiftImageUrl());
        chooseMemberEvent.setNonChooseMemberMessage(editChooseMemberEventDTO.getNonChooseMemberMessage());
        chooseMemberEvent.setNonMemberMessage(editChooseMemberEventDTO.getNonMemberMessage());
        chooseMemberEvent.setExchangeType(editChooseMemberEventDTO.getExchangeType());
        chooseMemberEvent.setPresentation(editChooseMemberEventDTO.getPresentation());
        chooseMemberEvent.setEventStartTime(editChooseMemberEventDTO.getEventStartTime());
        chooseMemberEvent.setEventEndTime(editChooseMemberEventDTO.getEventEndTime());

        //校验并设置默认库存数量
        if (Objects.isNull(editChooseMemberEventDTO.getRestrictNum()) || editChooseMemberEventDTO.getRestrictNum() < DEFAULT_RESTRICT_NUM){
            chooseMemberEvent.setRestrictNum(DEFAULT_RESTRICT_NUM);
        }else {
            chooseMemberEvent.setRestrictNum(editChooseMemberEventDTO.getRestrictNum());
        }


        chooseMemberEvent.setUpdateUser(userInfoInTokenBO.getUserId());
        chooseMemberEvent.setUpdateUserName(userInfoInTokenBO.getUsername());
        chooseMemberEvent.setUpdateTime(LocalDateTime.now());

        updateById(chooseMemberEvent);


        if (CollectionUtil.isNotEmpty(editChooseMemberEventDTO.getCouponIdList())){
            chooseMemberEventCouponRelationManager.removeRelationByEventId(editChooseMemberEventDTO.getChooseMemberEventId());
            chooseMemberEventCouponRelationManager.saveBatch(editChooseMemberEventDTO.getChooseMemberEventId(),
                    editChooseMemberEventDTO.getCouponIdList());
        }

        if (CollectionUtil.isNotEmpty(editChooseMemberEventDTO.getMobileList())){
            chooseMemberEventMobileRelationManager.removeRelationByEventId(editChooseMemberEventDTO.getChooseMemberEventId());
            //去重
            List<String> mobileList =  editChooseMemberEventDTO.getMobileList().stream().distinct().collect(Collectors.toList());
            //新增指定用户关系
            chooseMemberEventMobileRelationManager.addEventUserRelationBatch(chooseMemberEvent.getChooseMemberEventId(),
                    mobileList);
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<ChooseMemberEventVO> detail(Long eventId){

        ChooseMemberEvent chooseMemberEvent = getById(eventId);
        if (Objects.isNull(chooseMemberEvent)){
            return ServerResponseEntity.success(null);
        }

        ChooseMemberEventVO chooseMemberEventVO = new ChooseMemberEventVO();
        BeanUtils.copyProperties(chooseMemberEvent, chooseMemberEventVO);


        List<MemberEventCouponVO> couponList = chooseMemberEventCouponRelationManager
                .getTheCouponListByEventId(eventId);

        chooseMemberEventVO.setCouponList(couponList);

        Integer memberCount = chooseMemberEventMobileRelationManager.getMobileCountByEventId(eventId);

        chooseMemberEventVO.setMemberCount(memberCount);

        return ServerResponseEntity.success(chooseMemberEventVO);
    }

    @Override
    public ServerResponseEntity eventMemberValid(Long eventId) {

        try {
            ChooseMemberEvent memberEvent = getById(eventId);
            UserApiVO userApiVO = chooseMemberEventManager.eventMemberValid(memberEvent);
            ChooseMemberEventMobileRelation chooseMemberEventMobileRelation = chooseMemberEventMobileRelationManager
                    .checkEventUserRelation(eventId, userApiVO.getPhone());
            if (Objects.isNull(chooseMemberEventMobileRelation)){
                throw new LuckException(ResponseEnum.CHOOSE_MEMBER_EVENT_NON_CHOOSE_MEMBER_VALID,memberEvent.getNonChooseMemberMessage());
            }

            if (memberEvent.getEventEnabledStatus().equals(ChooseMemberEventEnableEnum.DISABLE.getEnableStatus())){
                throw new LuckException(ResponseEnum.CHOOSE_MEMBER_EVENT_ENABLED_STATUS_VALID);
            }

            //获取当前用户的已兑换个数
            Integer theUserEventExchangeNum = chooseMemberEventExchangeRecordManager.getTheUserEventExchangeNum(userApiVO.getUserId()
                    , memberEvent.getChooseMemberEventId());
            if (theUserEventExchangeNum.compareTo(memberEvent.getRestrictNum()) >= 0){
                return ServerResponseEntity.fail(ResponseEnum.CHOOSE_MEMBER_EVENT_RESTRICT_NUM_VALID);
            }

            //校验活动时间范围
            chooseMemberEventManager.eventTimeValid(memberEvent.getEventStartTime(), memberEvent.getEventEndTime());

            return ServerResponseEntity.success();
        }catch (LuckException e){
            return ServerResponseEntity.showDefinedMsg(e.getResponseEnum(),e.getMessage());
        }

    }

    @Override
    public ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> queryUserRecord(Integer pageNo, Integer pageSize) {
        return chooseMemberEventExchangeRecordManager.queryUserRecord(pageNo, pageSize);
    }
}
