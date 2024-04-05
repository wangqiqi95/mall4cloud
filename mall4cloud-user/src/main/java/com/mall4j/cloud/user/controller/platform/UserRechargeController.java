package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.api.coupon.constant.CouponPutOnStatus;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserRechargeDTO;
import com.mall4j.cloud.user.model.UserRecharge;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import com.mall4j.cloud.user.service.UserRechargeService;
import com.mall4j.cloud.user.vo.UserBalanceLogVO;
import com.mall4j.cloud.user.vo.UserRechargeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@RestController("platformUserRechargeController")
@RequestMapping("/p/user_recharge")
@Api(tags = "platform-余额充值级别表")
public class UserRechargeController {

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private UserBalanceLogService userBalanceLogService;

    @Autowired
	private MapperFacade mapperFacade;

    @GetMapping("/list" )
    @ApiOperation(value = "余额充值列表数据", notes = "余额充值列表数据")
    public ServerResponseEntity<List<UserRechargeVO>> getUserBalancePage() {
        List<UserRechargeVO> list = userRechargeService.list();
        return ServerResponseEntity.success(userRechargeService.list());
    }

	@GetMapping("/page")
	@ApiOperation(value = "获取余额充值级别表列表", notes = "分页获取余额充值级别表列表")
	public ServerResponseEntity<PageVO<UserRecharge>> page(@Valid PageDTO pageDTO) {
		PageVO<UserRecharge> userRechargePage = userRechargeService.page(pageDTO);
		return ServerResponseEntity.success(userRechargePage);
	}

	@GetMapping("/info")
    @ApiOperation(value = "获取余额充值级别表", notes = "根据rechargeId获取余额充值级别表")
    public ServerResponseEntity<UserRechargeVO> getByRechargeId(@RequestParam Long rechargeId) {
        UserRechargeVO userRechargeVO = userRechargeService.getRechargeByRechargeId(rechargeId, CouponPutOnStatus.PLACED.value());
        return ServerResponseEntity.success(userRechargeVO);
    }

    @PostMapping
    @ApiOperation(value = "保存余额充值级别表", notes = "保存余额充值级别表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserRechargeDTO userRechargeDTO) {
        userRechargeService.saveRecharge(userRechargeDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新余额充值级别表", notes = "更新余额充值级别表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserRechargeDTO userRechargeDTO) {
        userRechargeService.updateByRechargeId(userRechargeDTO);
        userRechargeService.removeCacheByRechargeId(userRechargeDTO.getRechargeId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除余额充值级别表", notes = "根据余额充值级别表id删除余额充值级别表")
    public ServerResponseEntity<Void> delete(@RequestParam Long rechargeId) {
        userRechargeService.deleteById(rechargeId);
        userRechargeService.removeCacheByRechargeId(rechargeId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_user_balance")
    @ApiOperation(value = "平台批量修改会员余额", notes = "平台批量修改会员余额")
    public ServerResponseEntity<Boolean> batchUpdateUserBalance(@RequestBody @Valid UserAdminDTO userAdminDTO) {
        return ServerResponseEntity.success(userRechargeService.batchUpdateUserBalance(userAdminDTO));
    }

    @GetMapping("/page_user_log" )
    @ApiOperation(value = "分页查询某个用户的余额明细", notes = "分页查询某个用户的余额明细")
    public ServerResponseEntity<PageVO<UserBalanceLogVO>> getPageByUserId(@Valid PageDTO pageDTO, Long userId) {
        PageVO<UserBalanceLogVO> res = userBalanceLogService.getPageByUserId(pageDTO,userId);
        return ServerResponseEntity.success(res);
    }

}
