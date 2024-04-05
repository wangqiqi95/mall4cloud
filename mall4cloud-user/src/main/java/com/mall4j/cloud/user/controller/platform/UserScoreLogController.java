package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserScoreLogService;
import com.mall4j.cloud.user.vo.UserScoreLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户积分记录
 *
 * @author cl
 * @date 2020-05-06 10:16:53
 */
@RestController("platformUserScoreLogController")
@RequestMapping("/p/user_score_log")
@Api(tags = "店铺-用户积分记录")
public class UserScoreLogController {

    @Autowired
    private UserScoreLogService userScoreLogService;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户积分记录列表", notes = "分页获取用户积分记录列表")
    public ServerResponseEntity<PageVO<UserScoreLogVO>> page(@Valid PageDTO pageDTO, Long userId) {
        PageVO<UserScoreLogVO> userScoreLogPage = userScoreLogService.pageByUserId(pageDTO,userId);
		return ServerResponseEntity.success(userScoreLogPage);
	}

}
