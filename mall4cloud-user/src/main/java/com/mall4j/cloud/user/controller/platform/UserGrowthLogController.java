package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserGrowthLogService;
import com.mall4j.cloud.user.vo.UserGrowthLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户成长值记录
 *
 * @author cl
 * @date 2021-05-08 20:16:53
 */
@RestController("platformUserGrowthLogController")
@RequestMapping("/p/user_growth_log")
@Api(tags = "platform-用户成长值记录")
public class UserGrowthLogController {

    @Autowired
    private UserGrowthLogService userGrowthLogService;


    @ApiOperation(value = "分页获取用户成长值记录列表", notes = "分页获取用户成长值记录列表")
    @GetMapping("/page" )
    public ServerResponseEntity<PageVO<UserGrowthLogVO>> getPageByUserId(@Valid PageDTO pageDTO, Long userId) {
        PageVO<UserGrowthLogVO> resPage = userGrowthLogService.getPageByUserId(pageDTO,userId);
        return ServerResponseEntity.success(resPage);
    }
}
