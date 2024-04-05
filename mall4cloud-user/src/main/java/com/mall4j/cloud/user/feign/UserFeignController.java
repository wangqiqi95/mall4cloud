package com.mall4j.cloud.user.feign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.dto.UserChangeServiceStoreDTO;
import com.mall4j.cloud.api.user.dto.UserDynamicCodeDTO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.api.user.dto.*;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.user.mapper.UserBalanceLogMapper;
import com.mall4j.cloud.user.mapper.UserExtensionMapper;
import com.mall4j.cloud.user.mapper.UserLevelMapper;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.UserLevel;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.UserExtensionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户地址feign连接
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Slf4j
@RestController
public class UserFeignController implements UserFeignClient {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Resource
    private UserExtensionMapper userExtensionMapper;
    @Resource
    private UserBalanceLogMapper userBalanceLogMapper;
    @Autowired
    private UserRightsService userRightsService;
    @Autowired
    private UserRechargeCouponService userRechargeCouponService;
    @Autowired
    private VeekerService veekerService;
    @Resource
    private UserLevelMapper userLevelMapper;
    @Autowired
    UserWeixinAccountFollowService userWeixinAccountFollowService;

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserByUserIds(List<Long> userIds) {
        List<UserApiVO> userList = userService.getUserByUserIds(userIds);
        return ServerResponseEntity.success(userList);
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserBypByUserIds(List<Long> userIds) {
        List<UserApiVO> userList = userService.getUserByUserIds(userIds);
        return ServerResponseEntity.success(userList);
    }

    @Override
    public ServerResponseEntity<UserApiVO> checkUserExist(String phone, String unionId) {
        return ServerResponseEntity.success(userService.checkUserExist(phone, unionId));
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserData(Long userId) {
        UserApiVO user = userService.getByUserId(userId);
        return ServerResponseEntity.success(user);
    }

    @Override
    public ServerResponseEntity<UserApiVO> getInsiderUserData(Long userId) {
        UserApiVO user = userService.getByUserId(userId);
        return ServerResponseEntity.success(user);
    }

    @Override
    public ServerResponseEntity<MemberOverviewVO> getUserAnalysis(MemberReqDTO param, Date startTime, Date endTime) {
        MemberOverviewVO res = new MemberOverviewVO();
        // 累积会员数
        param.setDateTime(endTime);
        res.setCurrentDay(DateUtils.dateToNumber(startTime));
        res.setTotalMember(userExtensionMapper.countMemberByParam(param));
        // 新增会员数
        param.setDateTime(startTime);
        Integer preAccumulate = userExtensionMapper.countMemberByParam(param);
        res.setNewMember(res.getTotalMember() - preAccumulate);
        // 筛选时间内，购买商品的会员人数
        param.setDateTime(null);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        res.setUserIds(getMeetConditionsUserIds(param).getData());
        // 获取储值会员数
        res.setStoredValue(userBalanceLogMapper.countByConditions(param));
        return ServerResponseEntity.success(res);
    }

    @Override
    public ServerResponseEntity<MemberOverviewVO> getMemberAnalysisByParam(MemberReqDTO param) {
        // 累积会员数和新增会员数
        MemberOverviewVO res = userExtensionMapper.countMemberDataByParam(param);
        // 筛选时间内，购买商品的会员人数,先获取当前符合条件的会员数(只是查询一天的应该数据量不会很大？)
        res.setUserIds(getMeetConditionsUserIds(param).getData());
        return ServerResponseEntity.success(res);
    }

    @Override
    public ServerResponseEntity<List<Long>> getMeetConditionsUserIds(MemberReqDTO param) {
        if (CollectionUtil.isEmpty(param.getUserIds())) {
            return ServerResponseEntity.success(null);
        }
        List<UserExtensionVO> userExtensionList = userExtensionMapper.countByMemberTypeConditions(param);
        List<Long> userIds = new ArrayList<>();
        Map<Integer, List<UserExtensionVO>> userInfoMap = userExtensionList.stream().collect(Collectors.groupingBy(UserExtensionVO::getLevelType));

        if (!Objects.equals(param.getMemberType(), 0) && userInfoMap.containsKey(param.getMemberType() - 1)) {
            userExtensionList = userInfoMap.get(param.getMemberType() - 1);
        }
        if (CollectionUtil.isNotEmpty(userExtensionList)) {
            userExtensionList.forEach(userExtensionVO -> userIds.add(userExtensionVO.getUserId()));
        }
        return ServerResponseEntity.success(userIds);
    }

    @Override
    public ServerResponseEntity<MemberContributeRespVO> getMemberContributeByParam(MemberReqDTO param) {
        Date endTime = param.getEndTime();
        param.setDateTime(endTime);
        // 先获取当前符合条件的所有会员数(只是查询一天的应该数据量不会很大？)
        List<UserExtensionVO> userExtensionList = userExtensionMapper.countByMemberTypeConditions(param);
        List<Long> userIds = new ArrayList<>();
        Map<Integer, List<UserExtensionVO>> userInfoMap = userExtensionList.stream().collect(Collectors.groupingBy(UserExtensionVO::getLevelType));

        MemberContributeRespVO respParam = new MemberContributeRespVO();
        // 普通会员
        MemberContributeValueVO publicMember = new MemberContributeValueVO();
        // 付费会员
        MemberContributeValueVO paidMember = new MemberContributeValueVO();

        // 普通会员
        param.setMemberType(1);
        publicMember.setTotalMember(userExtensionMapper.countMemberByParam(param));
        // 普通会员ids
        List<UserExtensionVO> memberList = userInfoMap.get(0);
        for (UserExtensionVO extensionVO : memberList) {
            userIds.add(extensionVO.getUserId());
        }
        respParam.setUserIds(userIds);
        // 付费会员
        param.setMemberType(2);
        param.setDateTime(endTime);
        paidMember.setTotalMember(userExtensionMapper.countMemberByParam(param));
        respParam.setPublicMember(publicMember);
        respParam.setPaidMember(paidMember);
        // 付费会员ids
        userIds = new ArrayList<>();
        memberList = userInfoMap.get(1);
        for (UserExtensionVO userExtensionVO : memberList) {
            userIds.add(userExtensionVO.getUserId());
        }
        respParam.setPaidUserIds(userIds);
        return ServerResponseEntity.success(respParam);
    }

    @Override
    public ServerResponseEntity<Integer> getMemberTrend(MemberReqDTO param) {
        return ServerResponseEntity.success(userExtensionMapper.countMemberByParam(param));
    }

    @Override
    public ServerResponseEntity<Integer> countUserByMobile(String mobile) {
        return ServerResponseEntity.success(userService.countUserByMobile(mobile));
    }

    @Override
    public ServerResponseEntity<Void> cancelBindingCoupons(List<Long> couponIds) {
        if (CollUtil.isNotEmpty(couponIds)) {
            userRechargeCouponService.cancelBindingCoupons(couponIds);
            userRightsService.cancelBindingCoupons(couponIds);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserAndOpenIdsByUserId(Long userId) {
        return ServerResponseEntity.success(userService.getUserAndOpenIdsByUserId(userId));
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserByUnionId(String unionId) {
        return ServerResponseEntity.success(userService.getByUnionId(unionId));
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserById(Long userId) {
        return ServerResponseEntity.success(userService.getByUserIdMp(userId));
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserByMobile(String mobile) {
        return ServerResponseEntity.success(userService.getUserByMobile(mobile));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> changeServiceStore(UserChangeServiceStoreDTO userChangeServiceStoreDTO) {
        userService.changeServiceStore(userChangeServiceStoreDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserByStoreId(Long storeId) {
        return ServerResponseEntity.success(userService.getUserByStoreId(storeId));
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserByStaffId(Long staffId) {
        return ServerResponseEntity.success(userService.getUserByStaffId(staffId));
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserByVipCode(String vipCode) {
        return ServerResponseEntity.success(userService.getUserByVipCode(vipCode));
    }

    @Override
    public ServerResponseEntity<DistributionUserVO> countUserNum(DistributionUserQueryDTO distributionUserQueryDTO) {
        return ServerResponseEntity.success(userService.countUserNum(distributionUserQueryDTO));
    }

    @Override
    public ServerResponseEntity<Integer> countStaffUser(UserQueryDTO userQueryDTO) {
        return ServerResponseEntity.success(userService.countStaffUser(userQueryDTO));
    }

    @Override
    public ServerResponseEntity<Integer> countStaffWeeker(UserQueryDTO userQueryDTO) {
        return ServerResponseEntity.success(veekerService.countStaffWeeker(userQueryDTO));
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> listStaffWeeker(UserQueryDTO userQueryDTO) {
        return ServerResponseEntity.success(veekerService.listStaffWeeker(userQueryDTO));
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> listUserByStaff(UserQueryDTO queryDTO) {
        return ServerResponseEntity.success(userService.listUserByStaff(queryDTO));
    }

    @Override
    public ServerResponseEntity<List<UserLevelVO>> listLevelByLevelType(Integer levelType) {
        List<UserLevel> userLevels = userLevelMapper.listByLevelType(levelType);
        List<UserLevelVO> userLevelVOS = Convert.toList(UserLevelVO.class, userLevels);
        return ServerResponseEntity.success(userLevelVOS);
    }

    @Override
    public ServerResponseEntity<Void> batchUpdateStaff(List<UserApiVO> userApiVOList) {
        if (CollectionUtil.isEmpty(userApiVOList)) {
            return ServerResponseEntity.success();
        }
        userApiVOList.forEach(userApiVO -> {
            User user = new User();
            user.setUserId(userApiVO.getUserId());
            user.setStaffId(userApiVO.getStaffId());
            user.setVeekerStatus(userApiVO.getVeekerStatus());
            userMapper.updateById(user);
        });
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateUserOpenId(UserApiVO userApiVO) {
        if (userApiVO == null) {
            return ServerResponseEntity.success();
        }
        User user = new User();
        user.setUserId(userApiVO.getUserId());
        user.setOpenId(userApiVO.getOpenId());
        userMapper.updateById(user);
        //修改完用户openid之后删除redis缓存。
        userService.removeUserCacheByUserId(userApiVO.getUserId());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> unBindStaffByStaffId(Long staffId) {
        List<Long> userIdList = userService.unBindStaffByStaffId(staffId);
        if (!CollectionUtils.isEmpty(userIdList)) {
            // 清除用户缓存
            userIdList.forEach(userId -> {
                userService.removeUserCacheByUserId(userId);
            });
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> refreshUserCacheByStaffId(Long staffId) {
        List<Long> userIdList = userService.findUserIdListByStaffId(staffId);
        if (!CollectionUtils.isEmpty(userIdList)) {
            log.info("清除用户缓存 {}", JSONObject.toJSONString(userIdList));
            // 清除用户缓存
            userIdList.forEach(userId -> {
                userService.removeUserCacheByUserId(userId);
            });
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> followWeixinAccount(UserWeixinccountFollowDTO userWeixinccountFollowDTO) {
        userWeixinAccountFollowService.follow(userWeixinccountFollowDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> followWeixinAccounts(UserWeixinccountFollowsDTO followsDTO) {
        userWeixinAccountFollowService.followData(followsDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<String>> listFollowUpAppIdByUnionId(String unionId) {
        return ServerResponseEntity.success(userWeixinAccountFollowService.listFollowUpAppIdByUnionId(unionId));
    }

    @Override
    public ServerResponseEntity<UserWeixinAccountFollowVO> getUserWeixinAccountFollow(String unionId, String appid) {
        return ServerResponseEntity.success(userWeixinAccountFollowService.getUserFollowByUnionIdAndAppid(unionId, appid));
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> findWeekerByKeyword(String keyword) {
        return ServerResponseEntity.success(userService.findWeekerByKeyword(keyword));
    }

    @Override
    public ServerResponseEntity<UserApiVO> getEcOrderUser(String openId) {
        UserApiVO userApiVO = userService.getByOpenId(openId);
        if(userApiVO!=null){
            return ServerResponseEntity.success(userApiVO);
        }
        //这里如果会员不存在，需要创建临时会员记录。
        return null;
    }

    @Override
    public boolean isRecEnabled(Long userId) {
        return userService.isRecEnabled(userId);
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> listByPhone(List<String> phoneList) {
        List<User> users = userMapper.selectList(Wrappers.lambdaQuery(User.class).in(User::getPhone, phoneList));
        List<UserApiVO> userApiVOList = BeanUtil.copyToList(users, UserApiVO.class);
        return ServerResponseEntity.success(userApiVOList);
    }

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserByUnionIdList(List<String> unionIdList) {
        List<UserApiVO> userApiVOList = userService.getUserByUnionIdList(unionIdList);
        return ServerResponseEntity.success(userApiVOList);
    }

    @Override
    public ServerResponseEntity<Void> cleanUserBindStaff(Long userId) {
        userService.cleanUserBindStaff(userId);
        return ServerResponseEntity.success();
    }
}
