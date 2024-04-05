package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserLabelUserDTO;
import com.mall4j.cloud.user.service.UserLabelUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 导购标签用户信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
@RestController("staffUserLabelUserController")
@RequestMapping("/s/user_label_user")
@Api(tags = "导购标签用户信息")
public class UserLabelUserController {

    @Autowired
    private UserLabelUserService userLabelUserService;

	@GetMapping("/page")
	@ApiOperation(value = "获取导购标签用户信息列表", notes = "分页获取导购标签用户信息列表")
	public ServerResponseEntity<PageVO<UserApiVO>> page(@Valid PageDTO pageDTO, UserLabelUserDTO userLabelUserDTO) {
		PageVO<UserApiVO> userLabelUserPage = userLabelUserService.page(pageDTO, userLabelUserDTO);
		return ServerResponseEntity.success(userLabelUserPage);
	}


}
