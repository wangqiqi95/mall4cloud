package com.mall4j.cloud.distribution.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserDTO;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivitySizesMapper;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.model.DistributionStoreActivitySizes;
import com.mall4j.cloud.distribution.model.DistributionStoreActivitySubscribe;
import com.mall4j.cloud.distribution.model.DistributionStoreActivityUser;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityService;
import com.mall4j.cloud.distribution.service.DistributionStoreActivitySizesService;
import com.mall4j.cloud.distribution.service.DistributionStoreActivitySubscribeService;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityUserService;
import com.mall4j.cloud.distribution.vo.StoreActivityProvinceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
@RestController("appDistributionStoreActivityController")
@RequestMapping("/distribution_store_activity")
@Api(tags = "商城-门店活动")
public class DistributionStoreActivityController {

    @Autowired
    private DistributionStoreActivityService distributionStoreActivityService;
	@Autowired
	private DistributionStoreActivityUserService distributionStoreActivityUserService;
	@Autowired
	private DistributionStoreActivitySubscribeService distributionStoreActivitySubscribeService;
	@Autowired
	private DistributionStoreActivitySizesMapper distributionStoreActivitySizesMapper;

	@GetMapping("/page")
	@ApiOperation(value = "活动列表", notes = "分页查询活动列表")
	public ServerResponseEntity<PageVO<DistributionStoreActivity>> appPageEffect(@Valid PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO) {
		return ServerResponseEntity.success(distributionStoreActivityService.appPageEffect(pageDTO, distributionStoreActivityQueryDTO));
	}

	@GetMapping("/provinceList")
	@ApiOperation(value = "活动城市列表", notes = "活动城市列表")
	public ServerResponseEntity<List<StoreActivityProvinceVO>> provinceList() {
		return ServerResponseEntity.success(distributionStoreActivityService.appProvinceEffect());
	}

	@GetMapping("/getById")
    @ApiOperation(value = "活动详情", notes = "通过活动id获取门店活动详情")
    public ServerResponseEntity<DistributionStoreActivity> getById(@RequestParam Long id) {
		DistributionStoreActivity distributionStoreActivity = distributionStoreActivityService.getById(id);
		if (Objects.nonNull(distributionStoreActivity)) {
			distributionStoreActivity.setApplyNum(distributionStoreActivityUserService.countByActivityIdAndStatus(id, 0));
			distributionStoreActivity.setApplyStatus(0);
			distributionStoreActivity.setSignStatus(0);
			DistributionStoreActivityUser distributionStoreActivityUser = distributionStoreActivityUserService.findByUserIdAndActivityId(AuthUserContext.get().getUserId(), id);
			if (Objects.nonNull(distributionStoreActivityUser)) {
				distributionStoreActivity.setDistributionStoreActivityUser(distributionStoreActivityUser);
				distributionStoreActivity.setApplyStatus(distributionStoreActivityUser.getStatus() == 0 ? 1 : 2);
				distributionStoreActivity.setSignStatus(distributionStoreActivityUser.getSignStatus());
			}
			if (distributionStoreActivity.getNeedClothes() == 1) {
				List<String> clothesSizes = distributionStoreActivitySizesMapper.selectList(new LambdaQueryWrapper<DistributionStoreActivitySizes>().eq(DistributionStoreActivitySizes::getActivityId, distributionStoreActivity.getId()).eq(DistributionStoreActivitySizes::getType, 1)).stream().map(DistributionStoreActivitySizes::getSize).collect(Collectors.toList());
				distributionStoreActivity.setClothesSizes(clothesSizes);
			}
			if (distributionStoreActivity.getNeedShoes() == 1) {
				List<String> shoesSizes = distributionStoreActivitySizesMapper.selectList(new LambdaQueryWrapper<DistributionStoreActivitySizes>().eq(DistributionStoreActivitySizes::getActivityId, distributionStoreActivity.getId()).eq(DistributionStoreActivitySizes::getType, 2)).stream().map(DistributionStoreActivitySizes::getSize).collect(Collectors.toList());
				distributionStoreActivity.setShoesSizes(shoesSizes);
			}
		}
        return ServerResponseEntity.success(distributionStoreActivity);
    }

	@PostMapping("/apply")
	@ApiOperation(value = "报名申请", notes = "门店活动-报名申请")
	public ServerResponseEntity<Void> apply(@RequestBody @Valid DistributionStoreActivityUserDTO distributionStoreActivityUserDTO) {
		distributionStoreActivityUserService.save(distributionStoreActivityUserDTO);
		return ServerResponseEntity.success();
	}

	@PostMapping("/applyCheck")
	@ApiOperation(value = "报名申请验证", notes = "门店活动-报名申请验证")
	public ServerResponseEntity<Void> applyCheck(@RequestParam Long activityId) {
		distributionStoreActivityUserService.applyCheck(activityId);
		return ServerResponseEntity.success();
	}

	@PostMapping("/cancel")
	@ApiOperation(value = "报名取消", notes = "门店活动-取消")
	public ServerResponseEntity<Void> cancel(@RequestParam Long activityId) {
		distributionStoreActivityUserService.cancel(AuthUserContext.get().getUserId(), activityId);
		return ServerResponseEntity.success();
	}

	@PostMapping("/sign")
	@ApiOperation(value = "签到", notes = "门店活动-签到")
	public ServerResponseEntity<Void> sign(@RequestParam Long activityId) {
		distributionStoreActivityUserService.sign(AuthUserContext.get().getUserId(), activityId);
		return ServerResponseEntity.success();
	}

	@PostMapping("/subscribe")
	@ApiOperation(value = "订阅", notes = "门店活动-订阅")
	public ServerResponseEntity<Void> subscribe() {
		distributionStoreActivitySubscribeService.subscribe(AuthUserContext.get().getUserId());
		return ServerResponseEntity.success();
	}

	@PostMapping("/cancelSubscribe")
	@ApiOperation(value = "取消订阅", notes = "门店活动-取消订阅")
	public ServerResponseEntity<Void> cancelSubscribe() {
		distributionStoreActivitySubscribeService.cancelSubscribe(AuthUserContext.get().getUserId());
		return ServerResponseEntity.success();
	}

	@PostMapping("/isSubscribe")
	@ApiOperation(value = "是否订阅", notes = "门店活动-是否订阅")
	public ServerResponseEntity<Boolean> isSubscribe() {
		DistributionStoreActivitySubscribe distributionStoreActivitySubscribe = distributionStoreActivitySubscribeService.getByUserId(AuthUserContext.get().getUserId());
		boolean flag = Objects.nonNull(distributionStoreActivitySubscribe) && distributionStoreActivitySubscribe.getIsSubscribe() == 1 ? true : false;
		return ServerResponseEntity.success(flag);
	}

}
