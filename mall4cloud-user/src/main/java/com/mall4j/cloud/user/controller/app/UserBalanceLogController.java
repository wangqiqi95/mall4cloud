package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import com.mall4j.cloud.user.vo.UserBalanceLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 余额记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@RestController("appUserBalanceLogController")
@RequestMapping("/user_balance_log")
@Api(tags = "余额记录")
public class UserBalanceLogController {

    @Autowired
    private UserBalanceLogService userBalanceLogService;

	@GetMapping("/page")
	@ApiOperation(value = "获取余额记录列表", notes = "分页获取余额记录列表")
	public ServerResponseEntity<PageVO<UserBalanceLogVO>> page(@Valid PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
		PageVO<UserBalanceLogVO> userBalanceLogPage = userBalanceLogService.getPageByUserId(pageDTO,userId);
		return ServerResponseEntity.success(userBalanceLogPage);
	}

    @DeleteMapping
    @ApiOperation(value = "删除余额记录", notes = "根据余额记录id删除余额记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long balanceLogId) {
        userBalanceLogService.deleteById(balanceLogId);
        return ServerResponseEntity.success();
    }
}
