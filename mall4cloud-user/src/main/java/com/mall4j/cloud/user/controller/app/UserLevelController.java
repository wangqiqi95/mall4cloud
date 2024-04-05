package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.constant.LevelChangeReasonEnum;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.constant.RecruitStatusEnum;
import com.mall4j.cloud.user.dto.UserLevelRechargeOrderDTO;
import com.mall4j.cloud.user.mapper.UserLevelTermMapper;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserLevelLog;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 会员等级表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("appUserLevelController")
@RequestMapping("/user_level")
@Api(tags = "app-会员等级表")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private UserExtensionService userExtensionService;

    @Autowired
    private ConfigFeignClient configFeignClient;

    @Autowired
    private UserLevelLogService userLevelLogService;

    @Autowired
    private UserLevelTermService userLevelTermService;

    @Autowired
    private UserLevelTermMapper userLevelTermMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRightsService userRightsService;

    @GetMapping("/member_info")
    @ApiOperation(value = "获取用户会员中心信息", notes = "获取用户会员中心信息")
    public ServerResponseEntity<UserMemberInfoVO> getMemberInfoVO() {
        Long userId = AuthUserContext.get().getUserId();
        UserApiVO user = userService.getByUserId(userId);
        // 查询用户基本信息名称加头像
        UserMemberInfoVO userMemberInfoVO = new UserMemberInfoVO();
        userMemberInfoVO.setNickName(user.getNickName());
        userMemberInfoVO.setPic(user.getPic());
        userMemberInfoVO.setExpireTime(user.getVipEndTime());
        // 查询用户扩展信息
        UserExtension userExtension = userExtensionService.getByUserId(userId);
        userMemberInfoVO.setScore(userExtension.getScore());
        userMemberInfoVO.setGrowth(userExtension.getGrowth());
        //设置用户的会员等级信息
        setLevelInformation(userMemberInfoVO, userExtension);
        userMemberInfoVO.setSignInCount(userExtension.getSignDay());
        // 查询成长值配置信息
        String growthConfigValue = configFeignClient.getConfig(ConfigNameConstant.GROWTH_CONFIG).getData();
        GrowthCompleteConfigVO growthCompleteConfigVO = Json.parseObject(growthConfigValue, GrowthCompleteConfigVO.class);
        userMemberInfoVO.setShopScore(growthCompleteConfigVO.getBuyOrder());

        return ServerResponseEntity.success(userMemberInfoVO);
    }

    @GetMapping("/ua/list_level_by_type")
    @ApiOperation(value = "会员等级列表信息", notes = "会员等级列表信息")
    @ApiImplicitParam(name = "levelType", value = "会员等级类型0为根据会员自身情况 1为付费", required = true, dataType = "int")
    public ServerResponseEntity<List<UserLevelVO>> listLevel(Integer levelType) {
        List<UserLevelVO> userLevelVOList = userLevelService.list(levelType);
        return ServerResponseEntity.success(userLevelVOList);
    }

//    @GetMapping("/get_user_level")
//    @ApiOperation(value = "会员等级权益展示", notes = "会员等级权益展示")
//    public ServerResponseEntity<UserLevelVO> getUserLevel(){
//        Long userId = AuthUserContext.get().getUserId();
//        UserExtension userExtension = userExtensionService.getByUserId(userId);
//        UserLevelVO userLevelVO = userLevelService.getOneByTypeAndLevel(userExtension.getLevelType(), userExtension.getLevel());
//        return ServerResponseEntity.success(userLevelVO);
//    }

    /**
     * 等级页展示
     *
     * @return 等级页展示
     */
    @GetMapping("/get_level_show")
    @ApiOperation(value = "会员中心页展示", notes = "会员中心页展示")
    public ServerResponseEntity<String> getLevelShow() {
        String growthDescriptionValue = configFeignClient.getConfig(ConfigNameConstant.MEMBER_GROWTH_DESCRIPTION).getData();
        if (Objects.isNull(growthDescriptionValue)) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.success(growthDescriptionValue);
    }


    @GetMapping("/get_user_level_term")
    @ApiOperation(value = "会员等级期数表", notes = "会员等级期数表列表")
    @ApiImplicitParam(name = "userLevelId", value = "用户会员等级id", required = true, dataType = "long")
    public ServerResponseEntity<List<UserLevelTermVO>> getUserLevelTerm(Long userLevelId) {
        List<UserLevelTermVO> levelTermVOList = userLevelTermService.getAmountAndTypeByUserLevelId(userLevelId);
        int monthAmount = userLevelTermMapper.getMonthAmount(userLevelId);

        for (UserLevelTermVO levelTermVO : levelTermVOList) {
            switch (levelTermVO.getTermType()) {
                case 0:
                    levelTermVO.setOriginalPrice(monthAmount);
                    levelTermVO.setDiscount(Arith.div(levelTermVO.getNeedAmount(), levelTermVO.getOriginalPrice(), 2));
                    break;
                case 1:
                    levelTermVO.setOriginalPrice(monthAmount * 3);
                    levelTermVO.setDiscount(Arith.div(levelTermVO.getNeedAmount(), levelTermVO.getOriginalPrice(), 2));
                    break;
                case 2:
                    levelTermVO.setOriginalPrice(monthAmount * 12);
                    levelTermVO.setDiscount(Arith.div(levelTermVO.getNeedAmount(), levelTermVO.getOriginalPrice(), 2));
                    break;
                default:
                    break;
            }
        }
        return ServerResponseEntity.success(levelTermVOList);
    }

    @PostMapping("/buy_vip")
    @ApiOperation(value = "购买付费会员", notes = "付费 or 续费，成为付费会员")
    public ServerResponseEntity<Long> save(@Valid @RequestBody UserLevelRechargeOrderDTO userLevelRechargeOrderDTO) {

        // 从缓存获取用户充值信息
        UserLevelVO userLevelVO = userLevelService.getByUserLevelId(userLevelRechargeOrderDTO.getUserLevelId());
        if (userLevelVO == null || !Objects.equals(LevelTypeEnum.PAY_USER.value(), userLevelVO.getLevelType())) {
            return ServerResponseEntity.showFailMsg("订单异常，请刷新页面重试");
        }
        if (Objects.equals(userLevelVO.getRecruitStatus(), RecruitStatusEnum.STOP_RECRUIT.value())) {
            return ServerResponseEntity.showFailMsg("付费会员等级为：" + userLevelVO.getLevelName() + "，已停止招募");
        }

        List<UserLevelTermVO> userLevelTerms = userLevelVO.getUserLevelTerms();

        UserLevelTermVO userLevelTerm = null;
        for (UserLevelTermVO userLevelTermVo : userLevelTerms) {
            if (Objects.equals(userLevelTermVo.getLevelTermId(), userLevelRechargeOrderDTO.getLevelTermId())) {
                userLevelTerm = userLevelTermVo;
            }
        }
        if (userLevelTerm == null) {
            return ServerResponseEntity.showFailMsg("订单异常，请刷新页面重试");
        }

        Long userId = AuthUserContext.get().getUserId();
        // 缓存中获取用户信息
        UserApiVO user = userService.getByUserId(userId);
        // 已付费会员不能购买其他等级的会员，请到期后重新购买
        if (Objects.equals(user.getLevelType(), LevelTypeEnum.PAY_USER.value()) && !Objects.equals(user.getVipLevel(), userLevelVO.getLevel())) {
            return ServerResponseEntity.showFailMsg("已付费会员不能购买其他等级的会员，请到期后重新购买");
        }

        UserLevelLog userLevelLog = new UserLevelLog();
        userLevelLog.setUserId(userId);
        userLevelLog.setPayAmount(userLevelTerm.getNeedAmount());
        userLevelLog.setAfterLevel(userLevelVO.getLevel());
        userLevelLog.setAfterLevelType(userLevelVO.getLevelType());
        userLevelLog.setBeforeLevel(user.getVipLevel() == null ? user.getLevel() : user.getVipLevel());
        userLevelLog.setBeforeLevelType(user.getLevelType());
        userLevelLog.setIsPayed(0);
        userLevelLog.setLevelChangeReason(Objects.equals(user.getVipLevel(), userLevelVO.getLevel()) ? LevelChangeReasonEnum.RENEW_VIP.value() : LevelChangeReasonEnum.BUY_VIP.value());
        userLevelLog.setLevelIoType(Objects.equals(user.getVipLevel(), userLevelVO.getLevel()) ? 0 : 1);
        userLevelLog.setTermType(userLevelTerm.getTermType());
        userLevelLogService.save(userLevelLog);
        return ServerResponseEntity.success(userLevelLog.getLevelLogId());
    }

    @GetMapping("/list_rights_by_level_type")
    @ApiOperation(value = "根据会员类型查权益", notes = "0普通会员 1付费会员")
    @ApiImplicitParam(name = "levelType", value = " 会员等级类型0为根据会员自身情况 1为付费", dataType = "Integer", required = true)
    public List<UserRightsVO> listRightsByLevelType(Integer levelType) {
        return userRightsService.listRightsByLevelType(levelType);
    }

    private void setLevelInformation(UserMemberInfoVO userMemberInfoVO, UserExtension userExtension) {
        // 查询用户当前普通会员的等级信息
        UserLevelVO ordinaryLevel = userLevelService.getOneByTypeAndLevel(LevelTypeEnum.ORDINARY_USER.value(), userExtension.getLevel());
        userMemberInfoVO.setUserOrdinaryLevel(ordinaryLevel);
        //用户当前的付费会员信息
        if (Objects.nonNull(userExtension.getVipLevel())) {
            UserLevelVO payedLevel = userLevelService.getOneByTypeAndLevel(LevelTypeEnum.PAY_USER.value(), userExtension.getVipLevel());
            userMemberInfoVO.setUserPayedLevel(payedLevel);
        }
    }
}
