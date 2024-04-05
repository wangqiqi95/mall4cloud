package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.vo.UserExtensionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户扩展信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("appUserExtensionController")
@RequestMapping("/user_extension")
@Api(tags = "app-用户扩展信息")
public class UserExtensionController {

    @Autowired
    private UserExtensionService userExtensionService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户扩展信息列表", notes = "分页获取用户扩展信息列表")
	public ServerResponseEntity<PageVO<UserExtensionVO>> page(@Valid PageDTO pageDTO) {
		PageVO<UserExtensionVO> userExtensionPage = userExtensionService.page(pageDTO);
		return ServerResponseEntity.success(userExtensionPage);
	}




}
