package com.mall4j.cloud.user.controller.app;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.CountFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.PageFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.UserFriendAssistanceDto;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreBannerUrlDTO;
import com.mall4j.cloud.user.service.UserScoreActivityService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBannerService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import com.mall4j.cloud.user.vo.scoreConvert.BannerDetailVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponAppDetailVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreMallHomeVO;
import com.mall4j.cloud.user.vo.scoreConvert.UserCouponLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户积分活动
 *
 * @author shijing
 * @date 2020-01-28
 */
@RestController("appScoreActivityController")
@RequestMapping("/user_score_activity")
@Api(tags = "app-积分商城")
public class ScoreActivityController {

    @Resource
	private ScoreCouponService scoreCouponService;
    @Autowired
	private ScoreBannerService scoreBannerService;

	@Resource
	private UserScoreActivityService userScoreActivityService;

	@GetMapping("/score_home_page")
	@ApiOperation(value = "积分商城首页")
	public ServerResponseEntity<ScoreMallHomeVO> listForApp(@RequestParam Long shopId){
		return scoreCouponService.listForApp(shopId);
	}

	@GetMapping("/bannerData")
	@ApiOperation(value = "Banner图数据获取")
	public ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerData(@RequestParam Long shopId, @RequestParam Integer type){
		return scoreCouponService.bannerData(shopId,type);
	}

	@GetMapping("/bannerDetail")
	@ApiOperation(value = "Banner图详情")
	public ServerResponseEntity<BannerDetailVO> bannerDetail(@RequestParam Long id){
		return scoreBannerService.selectDetail(id);
	}

	@GetMapping("/score_activity_detail")
	@ApiOperation(value = "积分换券详情")
	public ServerResponseEntity<ScoreCouponAppDetailVO> detailForApp(@RequestParam Long id) {
		return scoreCouponService.detailForApp(id);
	}

	@GetMapping("/activityFlag")
	@ApiOperation(value = "当前会员等级是否支持参与活动")
	public ServerResponseEntity<Boolean> activityFlag(@RequestParam Long id, @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) {
		return scoreCouponService.activityFlag(id,storeId);
	}

	@GetMapping("/user_convert")
	@ApiOperation(value = "用户领取积分券")
	public ServerResponseEntity<Void> userConvert(@RequestParam Long id,
                                                  @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId, @RequestParam(value = "userAddr", required = false)  String userAddr,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value = "userName", required = false) String userName){
		return scoreCouponService.userConvert(id,storeId,userAddr,phone,userName);
	}

	@GetMapping("/user_convert_log")
	@ApiOperation(value = "兑换记录")
	public ServerResponseEntity<PageInfo<UserCouponLogVO>> userConvertLog(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam Short type) {
		return scoreCouponService.userConvertLog(pageNo,pageSize,type);
	}

	@GetMapping("/enable_notice")
	@ApiOperation(value = "开启积分上新提醒（商城小程序）")
	public ServerResponseEntity<Void> enableNotice(@RequestParam("formId") String formId) {
		return scoreCouponService.enableNotice(formId);
	}

	@GetMapping("/disable_notice")
	@ApiOperation(value = "关闭积分上新提醒（商城小程序）")
	public ServerResponseEntity<Void> disableNotice(@RequestParam("formId") String formId) {
		return scoreCouponService.disableNotice(formId);
	}

	@GetMapping("/enable_coupon_notice")
	@ApiOperation(value = "开启积分券过期提醒（商城小程序）")
	public ServerResponseEntity<Void> enableCouponNotice(@RequestParam("formId") String formId, @RequestParam("activityId") Long activityId) {
		return scoreCouponService.enableCouponNotice(formId,activityId);
	}

	@GetMapping("/disable_coupon_notice")
	@ApiOperation(value = "关闭积分券过期提醒（商城小程序）")
	public ServerResponseEntity<Void> disableCouponNotice(@RequestParam("formId") String formId, @RequestParam("activityId") Long activityId) {
		return scoreCouponService.disableCouponNotice(formId,activityId);
	}

	@PostMapping("/friend_assistance")
	@ApiOperation(value = "积分清零活动好友助力")
	public ServerResponseEntity<Void> friendAssistance(@Validated @RequestBody UserFriendAssistanceDto dto){
		userScoreActivityService.friendAssistance(dto);
		return ServerResponseEntity.success();
	}

	@PostMapping("/page_friend_assistance")
	@ApiOperation(value = "积分清零活动好友助力明细列表")
	public ServerResponseEntity<PageVO<PageFriendAssistanceRespDto>> pageFriendAssistance(@RequestBody PageDTO dto){
		return ServerResponseEntity.success(userScoreActivityService.pageFriendAssistance(dto));
	}

	@GetMapping("/count_friend_assistance")
	@ApiOperation(value = "积分清零活动统计助力人数及累计积分")
	public ServerResponseEntity<CountFriendAssistanceRespDto> countFriendAssistance(){
		return ServerResponseEntity.success(userScoreActivityService.countFriendAssistance());
	}

	@GetMapping("/ua/friend_assistance_poster")
	@ApiOperation(value = "积分清零活动统计海报")
	public ServerResponseEntity<String> friendAssistancePoster(){
		return ServerResponseEntity.success(userScoreActivityService.friendAssistancePoster());
	}

	@GetMapping("/is_friend_assistance")
	@ApiOperation(value = "积分清零活动是否已助力")
	public ServerResponseEntity<Boolean> isFriendAssistance(@ApiParam("邀请人ID") @RequestParam("inviterUserId") Long inviterUserId){
		return ServerResponseEntity.success(userScoreActivityService.isFriendAssistance(inviterUserId));
	}

	@GetMapping("/check_friend_assistance_activity_date")
	@ApiOperation(value = "积分清零活动是否在活动时间内")
	public ServerResponseEntity<Boolean> isClearingPointsActivityDate(){
		return ServerResponseEntity.success(userScoreActivityService.isClearingPointsActivityDate());
	}


	@GetMapping("to/shop/check")
	@ApiOperation(value = "0兑礼到店，1其他")
	public ServerResponseEntity<Integer> isToShop(@ApiParam("优惠券ID") @RequestParam("couponId") Long couponId){
		return scoreCouponService.isToShop(couponId);
	}
}
