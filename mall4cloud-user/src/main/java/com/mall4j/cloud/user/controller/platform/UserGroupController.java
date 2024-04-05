package com.mall4j.cloud.user.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
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
import java.util.Objects;

/**
 * 用户分组阶段配置
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
@Slf4j
@RestController("UserGroupController")
@RequestMapping("/p/user/group")
@Api(tags = "用户分组阶段配置")
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

    @PostMapping
    @ApiOperation(value = "保存用户分组阶段配置", notes = "保存用户分组阶段配置")
    public ServerResponseEntity<Void> save(@Valid @RequestBody List<UserGroupDTO> userGroupDTO) {
        List<UserGroup> userGroups= mapperFacade.mapAsList(userGroupDTO, UserGroup.class);
        userGroupService.saveUserGroup(userGroups);
        return ServerResponseEntity.success();
    }

    @PostMapping("/saveGroup")
    @ApiOperation(value = "保存用户分组", notes = "保存用户分组")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserGroupDTO userGroupDTO) {
        UserGroup userGroup= mapperFacade.map(userGroupDTO, UserGroup.class);
        userGroup.setIsDelete(0);
        userGroup.setCreateBy(AuthUserContext.get().getUsername());
        userGroup.setCreateTime(new Date());
        userGroupService.save(userGroup);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户分组阶段配置", notes = "更新用户分组阶段配置")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserGroupDTO userGroupDTO) {
        UserGroup userGroup = mapperFacade.map(userGroupDTO, UserGroup.class);
        userGroupService.updateUserGroup(userGroup);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户分组阶段配置", notes = "根据用户分组阶段配置id删除用户分组阶段配置")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        userGroupService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
