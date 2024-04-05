package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.dto.*;
import com.mall4j.cloud.api.user.vo.*;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 用户信息feign连接
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@FeignClient(value = "mall4cloud-user", contextId = "user")
public interface UserFeignClient {

    /**
     * 根据用户id列表，获取用户信息
     *
     * @param userIds 用户id列表
     * @return 用户列表信息
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserByUserIds")
    ServerResponseEntity<List<UserApiVO>> getUserByUserIds(@RequestBody List<Long> userIds);

    /**
     * 根据用户id列表，获取用户信息
     *
     * @param userIds 用户id列表
     * @return 用户列表信息
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserBypByUserIds")
    ServerResponseEntity<List<UserApiVO>> getUserBypByUserIds(@RequestBody List<Long> userIds);


    /**
     * 校验用户是否存在
     *
     * @param phone   手机号码
     * @param unionId 用户unionId
     * @return 用户列表信息
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/checkUserExist")
    ServerResponseEntity<UserApiVO> checkUserExist(@RequestParam("phone") String phone, @RequestParam("unionId") String unionId);

    /**
     * 根据用户id获取用户详细信息
     *
     * @param userId 用户id
     * @return 用户详细信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserAndOpenIdsByUserId")
    ServerResponseEntity<UserApiVO> getUserAndOpenIdsByUserId(@RequestParam("userId") Long userId);

    /**
     * 根据unionId获取用户详细信息
     *
     * @param unionId
     * @return 用户详细信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserByUnionId")
    ServerResponseEntity<UserApiVO> getUserByUnionId(@RequestParam("unionId") String unionId);

    /**
     * 获取用户数据
     *
     * @param userId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserById")
    ServerResponseEntity<UserApiVO> getUserById(@RequestParam("userId") Long userId);

    /**
     * 获取用户数据
     *
     * @param userId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/user/getUserData")
    ServerResponseEntity<UserApiVO> getUserData(@RequestParam("userId") Long userId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserData")
    ServerResponseEntity<UserApiVO> getInsiderUserData(@RequestParam("userId") Long userId);

    /**
     * 获取用户分析数据
     *
     * @param param     筛选参数
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户分析数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserAnalysis")
    ServerResponseEntity<MemberOverviewVO> getUserAnalysis(@RequestBody MemberReqDTO param,
                                                           @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);

    /**
     * 获取用户分析数据
     *
     * @param param 筛选参数
     * @return 用户分析数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getMemberAnalysisByParam")
    ServerResponseEntity<MemberOverviewVO> getMemberAnalysisByParam(@RequestBody MemberReqDTO param);

    /**
     * 获取用户分析数据
     *
     * @param param 筛选参数
     * @return 用户分析数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getMeetConditionsUserIds")
    ServerResponseEntity<List<Long>> getMeetConditionsUserIds(@RequestBody MemberReqDTO param);

    /**
     * --------------数据分析相关sql--------------
     * 根据条件参数，统计会员数量
     *
     * @param param 参数
     * @return 数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/countPaidMemberByParam")
    ServerResponseEntity<MemberContributeRespVO> getMemberContributeByParam(@RequestBody MemberReqDTO param);

    /**
     * 筛选时间类的每一天注册的会员数数据
     *
     * @param param 参数
     * @return 会员数数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getMemberTrend")
    ServerResponseEntity<Integer> getMemberTrend(@RequestBody MemberReqDTO param);

    /**
     * 根据手机号码查询用户数量
     *
     * @param mobile 手机号码
     * @return 用户数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/countUserByMobile")
    ServerResponseEntity<Integer> countUserByMobile(@RequestParam("mobile") String mobile);

    /**
     * 取消过期优惠券的绑定
     *
     * @param couponIds 优惠券id列表
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/cancelBindingCoupons")
    ServerResponseEntity<Void> cancelBindingCoupons(@RequestParam("couponIds") List<Long> couponIds);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserByMobile")
    ServerResponseEntity<UserApiVO> getUserByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据门店id查询用户
     *
     * @param storeId 门店id
     * @return 结果集
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserByStoreId")
    ServerResponseEntity<List<UserApiVO>> getUserByStoreId(@RequestParam("storeId") Long storeId);

    /**
     * 根据导购id查询用户
     *
     * @param staffId 导购id
     * @return 结果集
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserByStaffId")
    ServerResponseEntity<List<UserApiVO>> getUserByStaffId(@RequestParam("staffId") Long staffId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/changeServiceStore")
    ServerResponseEntity<Void> changeServiceStore(@RequestBody UserChangeServiceStoreDTO userChangeServiceStoreDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserByVipCode")
    ServerResponseEntity<UserApiVO> getUserByVipCode(@RequestParam("vipCode") String vipCode);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/countUserNum")
    ServerResponseEntity<DistributionUserVO> countUserNum(@RequestBody DistributionUserQueryDTO distributionUserQueryDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/countStaffUser")
    ServerResponseEntity<Integer> countStaffUser(@RequestBody UserQueryDTO userQueryDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/countStaffWeeker")
    ServerResponseEntity<Integer> countStaffWeeker(@RequestBody UserQueryDTO userQueryDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/listStaffWeeker")
    ServerResponseEntity<List<UserApiVO>> listStaffWeeker(@RequestBody UserQueryDTO userQueryDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/listUserByStaff")
    ServerResponseEntity<List<UserApiVO>> listUserByStaff(@RequestBody UserQueryDTO queryDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/listLevelByType")
    ServerResponseEntity<List<UserLevelVO>> listLevelByLevelType(@RequestParam("levelType") Integer levelType);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/batchUpdateStaff")
    ServerResponseEntity<Void> batchUpdateStaff(@RequestBody List<UserApiVO> userApiVOList);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/updateUserOpenId")
    ServerResponseEntity<Void> updateUserOpenId(@RequestBody UserApiVO userApiVO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/unBindStaffByStaffId")
    ServerResponseEntity<Void> unBindStaffByStaffId(@RequestParam("staffId") Long staffId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/refreshUserCacheByStaffId")
    ServerResponseEntity<Void> refreshUserCacheByStaffId(@RequestParam("staffId") Long staffId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/followWeixinAccount")
    ServerResponseEntity<Void> followWeixinAccount(@RequestBody UserWeixinccountFollowDTO userWeixinccountFollowDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/followWeixinAccounts")
    ServerResponseEntity<Void> followWeixinAccounts(@RequestBody UserWeixinccountFollowsDTO followsDTO);

    /**
     * 根据集中标识（UnionId） 查询用户已关注的AppId
     *
     * @param unionId 集中标识
     * @return 已关注公众号AppId
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/follow/wechat/account/circumstances")
    ServerResponseEntity<List<String>> listFollowUpAppIdByUnionId(@RequestParam("unionId") String unionId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getUserWeixinAccountFollow")
    ServerResponseEntity<UserWeixinAccountFollowVO> getUserWeixinAccountFollow(@RequestParam("unionId") String unionId, @RequestParam("appid") String appid);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/findWeekerByKeyword")
    ServerResponseEntity<List<UserApiVO>> findWeekerByKeyword(@RequestParam("keyword") String keyword);

    /**
     * 通过openid获取用户信息，如果不存在，创建一个临时用户
     * @param openId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/getEcOrderUser")
    ServerResponseEntity<UserApiVO> getEcOrderUser(@RequestParam("openId") String openId);

    /**
     * 查询用户是否开启rec推荐
     * @param userId
     * @return true 开启  false 关闭
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/isRecEnabled")
    boolean isRecEnabled(@RequestParam("userId")Long userId);

    /**
     * 根据 phoneList 查询所有用户
     * @param phoneList  电话号码
     * @return list vo
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/listByPhone")
    ServerResponseEntity<List<UserApiVO>> listByPhone(@RequestBody List<String> phoneList);


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/get/by/unionId/list")
    ServerResponseEntity<List<UserApiVO>> getUserByUnionIdList(@RequestBody List<String> unionIdList);

    /**
     * 清除会员导购信息
     * @param userId 会员ID
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user/cleanUserBindStaff")
    ServerResponseEntity<Void> cleanUserBindStaff(@RequestParam("userId") Long userId);

}
