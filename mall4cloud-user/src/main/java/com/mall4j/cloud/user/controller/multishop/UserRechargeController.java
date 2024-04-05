package com.mall4j.cloud.user.controller.multishop;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserRechargeDTO;
import com.mall4j.cloud.user.model.UserRecharge;
import com.mall4j.cloud.user.service.UserRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@RestController("multishopUserRechargeController")
@RequestMapping("/m/user_recharge")
@Api(tags = "余额充值级别表")
public class UserRechargeController {

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取余额充值级别表列表", notes = "分页获取余额充值级别表列表")
	public ServerResponseEntity<PageVO<UserRecharge>> page(@Valid PageDTO pageDTO) {
		PageVO<UserRecharge> userRechargePage = userRechargeService.page(pageDTO);
		return ServerResponseEntity.success(userRechargePage);
	}


    @PostMapping
    @ApiOperation(value = "保存余额充值级别表", notes = "保存余额充值级别表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserRechargeDTO userRechargeDTO) {
        UserRecharge userRecharge = mapperFacade.map(userRechargeDTO, UserRecharge.class);
        userRecharge.setRechargeId(null);
        userRechargeService.save(userRecharge);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新余额充值级别表", notes = "更新余额充值级别表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserRechargeDTO userRechargeDTO) {
        UserRecharge userRecharge = mapperFacade.map(userRechargeDTO, UserRecharge.class);
        userRechargeService.update(userRecharge);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除余额充值级别表", notes = "根据余额充值级别表id删除余额充值级别表")
    public ServerResponseEntity<Void> delete(@RequestParam Long rechargeId) {
        userRechargeService.deleteById(rechargeId);
        return ServerResponseEntity.success();
    }
}
