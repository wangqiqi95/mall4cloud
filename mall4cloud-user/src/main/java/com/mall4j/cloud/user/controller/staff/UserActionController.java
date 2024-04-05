package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserActionQueryDTO;
import com.mall4j.cloud.user.service.UserActionService;
import com.mall4j.cloud.user.vo.UserActionListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("staffUserActionController")
@RequestMapping("/s/user_action")
@Api(tags = "导购小程序-会员行为记录")
public class UserActionController {

    @Autowired
    private UserActionService userActionService;

    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("/page")
    public ServerResponseEntity<PageVO<UserActionListVO>> page(@Valid PageDTO pageDTO,
                                                               @Valid UserActionQueryDTO queryDTO) {
        return ServerResponseEntity.success(userActionService.page(pageDTO, queryDTO));
    }
}
