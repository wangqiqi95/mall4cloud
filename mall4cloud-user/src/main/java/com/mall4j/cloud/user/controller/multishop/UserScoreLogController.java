package com.mall4j.cloud.user.controller.multishop;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserScoreLogDTO;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserScoreLogService;
import com.mall4j.cloud.user.vo.UserScoreLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("multishopUserScoreLogController")
@RequestMapping("/m/user_score_log")
@Api(tags = "店铺-用户积分记录")
public class UserScoreLogController {

    @Autowired
    private UserScoreLogService userScoreLogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户积分记录列表", notes = "分页获取用户积分记录列表")
    public ServerResponseEntity<PageVO<UserScoreLogVO>> page(@Valid PageDTO pageDTO) {
        PageVO<UserScoreLogVO> userScoreLogPage = userScoreLogService.page(pageDTO);
		return ServerResponseEntity.success(userScoreLogPage);
	}

	@GetMapping
    @ApiOperation(value = "获取用户积分记录", notes = "根据logId获取用户积分记录")
    public ServerResponseEntity<UserScoreLogVO> getByLogId(@RequestParam Long logId) {
        UserScoreLogVO userScoreLogVO = mapperFacade.map(userScoreLogService.getByLogId(logId), UserScoreLogVO.class);
        return ServerResponseEntity.success(userScoreLogVO);
    }

    @PostMapping
    @ApiOperation(value = "保存用户积分记录", notes = "保存用户积分记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserScoreLogDTO userScoreLogDTO) {
        UserScoreLog userScoreLog = mapperFacade.map(userScoreLogDTO, UserScoreLog.class);
        userScoreLog.setLogId(null);
        userScoreLogService.save(userScoreLog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户积分记录", notes = "更新用户积分记录")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserScoreLogDTO userScoreLogDTO) {
        UserScoreLog userScoreLog = mapperFacade.map(userScoreLogDTO, UserScoreLog.class);
        userScoreLogService.update(userScoreLog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户积分记录", notes = "根据用户积分记录id删除用户积分记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long logId) {
        userScoreLogService.deleteById(logId);
        return ServerResponseEntity.success();
    }
}
