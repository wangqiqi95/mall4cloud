package com.mall4j.cloud.user.controller.multishop;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserExtensionDTO;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.vo.UserExtensionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户扩展信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@RestController("multishopUserExtensionController")
@RequestMapping("/m/user_extension")
@Api(tags = "店铺-用户扩展信息")
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

	@GetMapping
    @ApiOperation(value = "获取用户扩展信息", notes = "根据userExtensionId获取用户扩展信息")
    public ServerResponseEntity<UserExtensionVO> getByUserExtensionId(@RequestParam Long userExtensionId) {
        UserExtensionVO userExtensionVO = mapperFacade.map(userExtensionService.getByUserExtensionId(userExtensionId), UserExtensionVO.class);
        return ServerResponseEntity.success(userExtensionVO);
    }

    @PostMapping
    @ApiOperation(value = "保存用户扩展信息", notes = "保存用户扩展信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserExtensionDTO userExtensionDTO) {
        UserExtension userExtension = mapperFacade.map(userExtensionDTO, UserExtension.class);
        userExtension.setSignDay(0);
        userExtension.setUserExtensionId(null);
        userExtensionService.save(userExtension);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户扩展信息", notes = "更新用户扩展信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserExtensionDTO userExtensionDTO) {
        UserExtension userExtension = mapperFacade.map(userExtensionDTO, UserExtension.class);
        userExtensionService.update(userExtension);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户扩展信息", notes = "根据用户扩展信息id删除用户扩展信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long userExtensionId) {
        userExtensionService.deleteById(userExtensionId);
        return ServerResponseEntity.success();
    }
}
