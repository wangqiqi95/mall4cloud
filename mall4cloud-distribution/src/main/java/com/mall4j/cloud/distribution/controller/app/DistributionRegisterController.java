package com.mall4j.cloud.distribution.controller.app;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.UserShoppingDataVO;
import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.distribution.constant.DistributionUserStateEnum;
import com.mall4j.cloud.distribution.dto.DistributionUserRegisterDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 分销员注册接口
 * @author cl
 * @date 2021-08-16 10:00:21
 */
@RestController("appDistributionRegisterController")
@RequestMapping("/distribution_register")
@Api(tags = "分销员绑定关系")
public class DistributionRegisterController {

    private static final Logger logger = LoggerFactory.getLogger(DistributionRegisterController.class);

    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserService distributionUserService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 成为分销员
     */
    @PostMapping("/add_distribution_user")
    @ApiOperation(value = "申请成为分销员", notes = "申请成为分销员，返回分销员状态 0 待审核 1 正常")
    public ServerResponseEntity<Integer> addDistributionUser(@Valid @RequestBody DistributionUserRegisterDTO distributionUserRegisterDTO) {

        logger.debug(Json.toJsonString(distributionUserRegisterDTO));
        // 店铺分销员审核设置
        DistributionConfigApiVO distributionConfig = distributionConfigService.getDistributionConfig();
        // 判断传入的资料是否齐全
        if(distributionConfig == null || Objects.isNull(distributionConfig.getAutoCheck())){
            // 分销设置异常
            throw new LuckException("分销设置异常");
        }
        if(distributionConfig.getIdentityCardNumber()){
            if(!StrUtil.isNotBlank(distributionUserRegisterDTO.getIdentityCardNumber())){
                // 申请填写信息不全,需要身份证号
                throw new LuckException("申请填写信息不全,需要身份证号");
            }
        }
        if(distributionConfig.getIdentityCardPic()){
            if(!StrUtil.isNotBlank(distributionUserRegisterDTO.getIdentityCardPicBack())||!StrUtil.isNotBlank(distributionUserRegisterDTO.getIdentityCardPicFront())||!StrUtil.isNotBlank(distributionUserRegisterDTO.getIdentityCardPicHold())){
                // 申请填写信息不全,需要身份证照片
                throw new LuckException("申请填写信息不全,需要身份证照片");
            }
        }
        if(distributionConfig.getRealName()){
            if(!StrUtil.isNotBlank(distributionUserRegisterDTO.getRealName())){
                // 申请填写信息不全,需要真实姓名
                throw new LuckException("申请填写信息不全,需要真实姓名");
            }
        }
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = userInfoInTokenBO.getUserId();
        ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserData(userId);
        if (!userResponse.isSuccess()) {
            throw new LuckException(userResponse.getMsg());
        }
        UserApiVO user = userResponse.getData();

        DistributionUserVO distributionUserVO = distributionUserService.getByUserId(userId);
        // 检查分销员信息
        checkUserInfo(distributionUserRegisterDTO, distributionUserVO);

        DistributionUser distributionUser = new DistributionUser();
        distributionUser.setUserId(userId);

        // 设置分享人信息
        setSharer(distributionUserRegisterDTO, userId, distributionUser);

        if (distributionConfig.getAutoCheck() == 1) {
            autoCheck(distributionConfig, userId, distributionUser);
        } else {
            // 待审核
            distributionUser.setState(DistributionUserStateEnum.WAIT_AUDIT.value());
        }

        //设置信息
        distributionUser.setUserMobile(distributionUserRegisterDTO.getUserMobile());
        distributionUser.setRealName(distributionUserRegisterDTO.getRealName());
        distributionUser.setIdentityCardNumber(distributionUserRegisterDTO.getIdentityCardNumber());
        distributionUser.setIdentityCardPicFront(distributionUserRegisterDTO.getIdentityCardPicFront());
        distributionUser.setIdentityCardPicHold(distributionUserRegisterDTO.getIdentityCardPicHold());
        distributionUser.setIdentityCardPicBack(distributionUserRegisterDTO.getIdentityCardPicBack());

        //设置用户信息字段
        distributionUser.setNickName(user.getNickName());
        distributionUser.setPic(user.getPic());

        distributionUserService.registerDistributionUser(distributionUser);

        // 清除缓存
        distributionUserService.removeCacheByUserId(distributionUser.getUserId());
        return ServerResponseEntity.success(distributionUser.getState());
    }

    private void setSharer(DistributionUserRegisterDTO distributionUserRegisterDTO, Long userId, DistributionUser distributionUser) {
        String sharerDistributionUserId = distributionUserRegisterDTO.getSharer();
        if (StrUtil.isNotBlank(sharerDistributionUserId)) {
            DistributionUser parentUser = distributionUserService.getByDistributionUserId(Long.parseLong(sharerDistributionUserId));
            if (parentUser == null) {
                // 无法获取邀请人信息
                throw new LuckException("无法获取邀请人信息");
            }
            if (Objects.equals(parentUser.getUserId(), userId)) {
                // 自己无法邀请自己
                throw new LuckException("自己无法邀请自己");
            }
            if (!Objects.equals(parentUser.getState(), DistributionUserStateEnum.PER_BAN.value())
                    && !Objects.equals(parentUser.getState(), DistributionUserStateEnum.BAN.value())){
                //上级不能是暂时封禁或永久封禁状态
                //设置上级id
                distributionUser.setParentId(parentUser.getDistributionUserId());
                //设置上级ids
                if (parentUser.getParentId() == null) {
                    distributionUser.setParentIds(StringUtils.join(StrUtil.COMMA, parentUser.getDistributionUserId(), StrUtil.COMMA));
                } else {
                    distributionUser.setParentIds(StringUtils.join(parentUser.getParentIds(), parentUser.getDistributionUserId(), StrUtil.COMMA));
                }

                distributionUser.setGrade(parentUser.getGrade() + 1);
            }
        } else {
            distributionUser.setGrade(0);
        }
    }

    private void checkUserInfo(DistributionUserRegisterDTO distributionUserRegisterDTO, DistributionUserVO distributionUser) {
        if (distributionUser != null) {
            if (Objects.equals(distributionUser.getState(), DistributionUserStateEnum.WAIT_AUDIT.value())) {
                // 你已经提交过审核了,请耐心等待
                throw new LuckException("你已经提交过审核了,请耐心等待");
            }else if (Objects.equals(distributionUser.getState(), DistributionUserStateEnum.PER_BAN.value())) {
                // 该账户已被永久封禁
                throw new LuckException("该账户已被永久封禁");
            }
            else if (Objects.equals(distributionUser.getState(), DistributionUserStateEnum.BAN.value())) {
                // 已暂时封禁不可申请,请联系管理员
                throw new LuckException("已暂时封禁不可申请,请联系管理员");
            }
            else if (Objects.equals(distributionUser.getState(), DistributionUserStateEnum.NORMAL.value())) {
                // 你的审核已经通过,无需再次审核
                throw new LuckException("你的审核已经通过,无需再次审核");
            }
        }
        List<DistributionUser> distributionUsersDb = distributionUserService.getDistributionUserByIdCardNumberAndUserMobile(distributionUserRegisterDTO.getIdentityCardNumber(), distributionUserRegisterDTO.getUserMobile());
        for (DistributionUser distributionUserDb:distributionUsersDb){
            if (StrUtil.isNotBlank(distributionUserRegisterDTO.getIdentityCardNumber()) && StrUtil.equals(distributionUserDb.getIdentityCardNumber(), distributionUserRegisterDTO.getIdentityCardNumber())){
                // 该身份证号码已存在
                throw new LuckException("该身份证号码已存在");
            } else if (Objects.equals(distributionUserDb.getUserMobile(), distributionUserRegisterDTO.getUserMobile())){
                // 该手机号码已存在
                throw new LuckException("该手机号码已存在");
            }
        }
    }

    private void autoCheck(DistributionConfigApiVO distributionConfig, Long userId, DistributionUser distributionUser) {
        //判断是否需要购买置指定商品
        if (CollUtil.isNotEmpty(distributionConfig.getSpuIdList())) {
            boolean has = false;
            Long distributionUserUserId = distributionUser.getUserId();
            for (Long spu: distributionConfig.getSpuIdList()) {
                // 判断有没有没有购买过指定商品
                ServerResponseEntity<Long> orderResponse = orderFeignClient.hasBuySuccessProd(spu, distributionUserUserId);
                if (!orderResponse.isSuccess()) {
                    throw new LuckException(orderResponse.getMsg());
                }
                if (orderResponse.getData() > 0L) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                // 不符合申请条件，请联系客服进行申请
                throw new LuckException("不符合申请条件，请联系客服进行申请");
            }
        }
        // 获取用户金额统计数据
        ServerResponseEntity<UserShoppingDataVO> userShoppingDataResponse = orderFeignClient.calculateUserInShopData(userId);
        if (!userShoppingDataResponse.isSuccess()) {
            throw new LuckException(userShoppingDataResponse.getMsg());
        }
        UserShoppingDataVO userShoppingData = userShoppingDataResponse.getData();

        //判断是否需要消费金额条件
        if (distributionConfig.getExpenseAmount() != null) {
            if (userShoppingData.getSumOfConsumption() == null) {
                userShoppingData.setSumOfConsumption(0L);
            }
            // 转换成元
            BigDecimal sumOfConsumption = PriceUtil.toDecimalPrice(userShoppingData.getSumOfConsumption());
            if (sumOfConsumption.compareTo(distributionConfig.getExpenseAmount()) < 0) {
                // 消费金额不符合要求
                throw new LuckException("消费金额不符合要求");
            }
        }
        //判断是否需要达到消费笔数条件
        if (distributionConfig.getExpenseNumber() != null) {
            if (userShoppingData.getExpenseNumber() < distributionConfig.getExpenseNumber()) {
                // 消费笔数不符合要求
                throw new LuckException("消费笔数不符合要求");
            }
        }
        distributionUser.setState(DistributionUserStateEnum.NORMAL.value());
        distributionUser.setBindTime(new Date());
    }
}
