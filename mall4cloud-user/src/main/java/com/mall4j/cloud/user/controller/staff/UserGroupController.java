package com.mall4j.cloud.user.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.UserGroupDTO;
import com.mall4j.cloud.user.dto.UserGroupSelectDTO;
import com.mall4j.cloud.user.model.UserGroup;
import com.mall4j.cloud.user.service.UserGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 用户分组阶段配置
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
@Slf4j
@RestController("StaffUserGroupController")
@RequestMapping("/s/user/group")
@Api(tags = "导购端 用户分组阶段配置")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
	private MapperFacade mapperFacade;

    @GetMapping("/groupList")
    @ApiOperation(value = "获取用户分组列表", notes = "获取用户分组列表")
    public ServerResponseEntity<List<UserGroup>> groupList(@RequestParam(required = false) String groupName) {
        return ServerResponseEntity.success( userGroupService.getGroupList(groupName));
    }

	@GetMapping("/page")
	@ApiOperation(value = "获取用户分组阶段配置列表", notes = "分页获取用户分组阶段配置列表")
	public ServerResponseEntity<PageVO<UserGroup>> page(@Valid PageDTO pageDTO, UserGroupSelectDTO dto) {
//        if(Objects.isNull(dto.getParentId())){
//            throw new LuckException("分组ID不能为空");
//        }
//        dto.setType(1);
		PageVO<UserGroup> userGroupPage = userGroupService.page(pageDTO,dto);
		return ServerResponseEntity.success(userGroupPage);
	}

	@GetMapping
    @ApiOperation(value = "获取用户分组阶段配置", notes = "根据id获取用户分组阶段配置")
    public ServerResponseEntity<UserGroup> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(userGroupService.getById(id));
    }

}
