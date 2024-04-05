package com.mall4j.cloud.user.controller.multishop;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserGrowthLogDTO;
import com.mall4j.cloud.user.model.UserGrowthLog;
import com.mall4j.cloud.user.service.UserGrowthLogService;
import com.mall4j.cloud.user.vo.UserGrowthLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户成长值记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("multishopUserGrowthLogController")
@RequestMapping("/m/user_growth_log")
@Api(tags = "店铺-用户成长值记录")
public class UserGrowthLogController {

    @Autowired
    private UserGrowthLogService userGrowthLogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户成长值记录列表", notes = "分页获取用户成长值记录列表")
	public ServerResponseEntity<PageVO<UserGrowthLogVO>> page(@Valid PageDTO pageDTO) {
		PageVO<UserGrowthLogVO> userGrowthLogPage = userGrowthLogService.page(pageDTO);
		return ServerResponseEntity.success(userGrowthLogPage);
	}

	@GetMapping
    @ApiOperation(value = "获取用户成长值记录", notes = "根据logId获取用户成长值记录")
    public ServerResponseEntity<UserGrowthLogVO> getByLogId(@RequestParam Long logId) {
        UserGrowthLogVO userGrowthLogVO = mapperFacade.map(userGrowthLogService.getByLogId(logId), UserGrowthLogVO.class);
        return ServerResponseEntity.success(userGrowthLogVO);
    }

    @PostMapping
    @ApiOperation(value = "保存用户成长值记录", notes = "保存用户成长值记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserGrowthLogDTO userGrowthLogDTO) {
        UserGrowthLog userGrowthLog = mapperFacade.map(userGrowthLogDTO, UserGrowthLog.class);
        userGrowthLog.setLogId(null);
        userGrowthLogService.save(userGrowthLog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户成长值记录", notes = "更新用户成长值记录")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserGrowthLogDTO userGrowthLogDTO) {
        UserGrowthLog userGrowthLog = mapperFacade.map(userGrowthLogDTO, UserGrowthLog.class);
        userGrowthLogService.update(userGrowthLog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户成长值记录", notes = "根据用户成长值记录id删除用户成长值记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long logId) {
        userGrowthLogService.deleteById(logId);
        return ServerResponseEntity.success();
    }
}
