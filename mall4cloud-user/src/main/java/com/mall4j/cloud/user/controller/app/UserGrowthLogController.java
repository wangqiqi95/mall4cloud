package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.service.UserGrowthLogService;
import com.mall4j.cloud.user.vo.UserGrowthLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户成长值记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("appUserGrowthLogController")
@RequestMapping("/user_growth_log")
@Api(tags = "app-用户成长值记录")
public class UserGrowthLogController {

    @Autowired
    private UserGrowthLogService userGrowthLogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户成长值记录列表", notes = "分页获取用户成长值记录列表")
	public ServerResponseEntity<PageVO<UserGrowthLogVO>> getPageByUserId(@Valid PageDTO pageDTO) {
		Long userId = AuthUserContext.get().getUserId();
		PageVO<UserGrowthLogVO> userGrowthLogPage = userGrowthLogService.getPageByUserId(pageDTO,userId);
		return ServerResponseEntity.success(userGrowthLogPage);
	}

//    @PostMapping
//    @ApiOperation(value = "保存用户成长值记录", notes = "保存用户成长值记录")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody UserGrowthLogDTO userGrowthLogDTO) {
//        UserGrowthLog userGrowthLog = mapperFacade.map(userGrowthLogDTO, UserGrowthLog.class);
//        userGrowthLog.setLogId(null);
//        userGrowthLogService.save(userGrowthLog);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新用户成长值记录", notes = "更新用户成长值记录")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody UserGrowthLogDTO userGrowthLogDTO) {
//        UserGrowthLog userGrowthLog = mapperFacade.map(userGrowthLogDTO, UserGrowthLog.class);
//        userGrowthLogService.update(userGrowthLog);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除用户成长值记录", notes = "根据用户成长值记录id删除用户成长值记录")
//    public ServerResponseEntity<Void> delete(@RequestParam Long logId) {
//        userGrowthLogService.deleteById(logId);
//        return ServerResponseEntity.success();
//    }
}
