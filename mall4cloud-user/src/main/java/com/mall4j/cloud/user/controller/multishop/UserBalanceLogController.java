package com.mall4j.cloud.user.controller.multishop;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserBalanceRechargeOrderDTO;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 余额记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@RestController("multishopUserBalanceLogController")
@RequestMapping("/m/user_balance_log")
@Api(tags = "余额记录")
public class UserBalanceLogController {

    @Autowired
    private UserBalanceLogService userBalanceLogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取余额记录列表", notes = "分页获取余额记录列表")
	public ServerResponseEntity<PageVO<UserBalanceLog>> page(@Valid PageDTO pageDTO) {
		PageVO<UserBalanceLog> userBalanceLogPage = userBalanceLogService.page(pageDTO);
		return ServerResponseEntity.success(userBalanceLogPage);
	}

	@GetMapping
    @ApiOperation(value = "获取余额记录", notes = "根据balanceLogId获取余额记录")
    public ServerResponseEntity<UserBalanceLog> getByBalanceLogId(@RequestParam Long balanceLogId) {
        return ServerResponseEntity.success(userBalanceLogService.getByBalanceLogId(balanceLogId));
    }

    @PostMapping
    @ApiOperation(value = "保存余额记录", notes = "保存余额记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserBalanceRechargeOrderDTO userBalanceRechargeOrderDTO) {
        UserBalanceLog userBalanceLog = mapperFacade.map(userBalanceRechargeOrderDTO, UserBalanceLog.class);
        userBalanceLog.setBalanceLogId(null);
        userBalanceLogService.save(userBalanceLog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新余额记录", notes = "更新余额记录")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserBalanceRechargeOrderDTO userRechargeLogDTO) {
        UserBalanceLog userBalanceLog = mapperFacade.map(userRechargeLogDTO, UserBalanceLog.class);
        userBalanceLogService.update(userBalanceLog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除余额记录", notes = "根据余额记录id删除余额记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long balanceLogId) {
        userBalanceLogService.deleteById(balanceLogId);
        return ServerResponseEntity.success();
    }
}
