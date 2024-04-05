package com.mall4j.cloud.user.controller.platform;


import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.QueryUserTagOperationPageDTO;
import com.mall4j.cloud.user.service.UserTagOperationService;
import com.mall4j.cloud.user.vo.UserTagOperationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/p/user/tag/operation")
@Api(tags = "platform-会员标签操作日志API")
public class PlatformUserTagOperationController {

    @Autowired
    private UserTagOperationService userTagOperationService;

    @GetMapping("/page/by/vip")
    @ApiOperation("查看会员标签修改日志")
    public ServerResponseEntity<PageVO<UserTagOperationVO>> export(QueryUserTagOperationPageDTO userTagOperationPageDTO) {
        return userTagOperationService.getOperationByBeVipCode(userTagOperationPageDTO);
    }
}