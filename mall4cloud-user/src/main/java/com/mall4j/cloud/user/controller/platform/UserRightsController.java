package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponDataVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.dto.UserRightsDTO;
import com.mall4j.cloud.user.service.UserRightsService;
import com.mall4j.cloud.user.vo.UserRightsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户权益信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("platformUserRightsController")
@RequestMapping("/p/user_rights")
@Api(tags = "店铺-用户权益信息")
public class UserRightsController {

    @Autowired
    private UserRightsService userRightsService;
    @Autowired
    private CouponFeignClient couponFeignClient;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户权益信息列表", notes = "分页获取用户权益信息列表")
	public ServerResponseEntity<PageVO<UserRightsVO>> page(@Valid PageDTO pageDTO, UserRightsDTO userRightsDTO) {
		PageVO<UserRightsVO> userRightsPage = userRightsService.page(pageDTO, userRightsDTO);
		return ServerResponseEntity.success(userRightsPage);
	}


	@GetMapping
    @ApiOperation(value = "获取用户权益信息", notes = "根据rightsId获取用户权益信息")
    public ServerResponseEntity<UserRightsVO> getByRightsId(@RequestParam Long rightsId) {
        UserRightsVO userRightsVO = userRightsService.getByRightsId(rightsId);
        ServerResponseEntity<List<CouponDataVO>> couponListResponse = couponFeignClient.getCouponListByCouponIds(userRightsVO.getCouponIds());
        userRightsVO.setCouponList(couponListResponse.getData());
        return ServerResponseEntity.success(userRightsVO);
    }

    @PostMapping
    @ApiOperation(value = "保存用户权益信息", notes = "保存用户权益信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserRightsDTO userRightsDTO) {
        userRightsService.save(userRightsDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户权益信息", notes = "更新用户权益信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserRightsDTO userRightsDTO) {
        userRightsService.update(userRightsDTO);
        userRightsService.removeRightsCache(userRightsDTO.getRightsId());
        userRightsService.removeRightsByLevelTypeCache(LevelTypeEnum.ORDINARY_USER.value());
        userRightsService.removeRightsByLevelTypeCache(LevelTypeEnum.PAY_USER.value());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户权益信息", notes = "根据用户权益信息id删除用户权益信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long rightsId) {
        userRightsService.deleteById(rightsId);
        userRightsService.removeRightsCache(rightsId);
        userRightsService.removeRightsByLevelTypeCache(LevelTypeEnum.ORDINARY_USER.value());
        userRightsService.removeRightsByLevelTypeCache(LevelTypeEnum.PAY_USER.value());
        return ServerResponseEntity.success();
    }

    /**
     * 获取权益列表
     */
    @GetMapping("/list")
    public ServerResponseEntity<List<UserRightsVO>> list() {
        UserRightsDTO userRights = new UserRightsDTO();
        return ServerResponseEntity.success(userRightsService.list(userRights));
    }
}
