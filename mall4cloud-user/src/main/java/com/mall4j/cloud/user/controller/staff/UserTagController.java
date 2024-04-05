package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserTagService;
import com.mall4j.cloud.user.vo.UserTagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author ZengFanChang
 * @Date 2022/01/09
 */
@RestController("staffUserTagController")
@RequestMapping("/s/user_tag")
@Api(tags = "staff-客户标签")
public class UserTagController {

    @Autowired
    private UserTagService userTagService;

    @GetMapping("/page")
    @ApiOperation(value = "获取客户标签列表", notes = "分页获取客户标签列表")
    public ServerResponseEntity<PageVO<UserTagVO>> page(@Valid PageDTO pageDTO) {
        PageVO<UserTagVO> userTagPage = userTagService.page(pageDTO);
        return ServerResponseEntity.success(userTagPage);
    }

}
