package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.biz.dto.cp.CpCodeGroupDTO;
import com.mall4j.cloud.biz.model.cp.CpCodeGroup;
import com.mall4j.cloud.biz.service.cp.CpCodeGroupService;
import com.mall4j.cloud.biz.vo.cp.CpCodeGroupVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
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
 * 企微活码分组
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
@Slf4j
@RestController("CpCodeGroupController")
@RequestMapping("/p/cp/code/group")
@Api(tags = "企微活码分组")
public class CpCodeGroupController {

    @Autowired
    private CpCodeGroupService cpCodeGroupService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/list")
	@ApiOperation(value = "获取企微活码分组列表", notes = "分页获取企微活码分组列表")
	public ServerResponseEntity<List<CpCodeGroupVO>> list(@RequestParam(required = false) Integer type,
                                                          @RequestParam(required = false) String name) {
        List<CpCodeGroupVO> cpCodeGroupPage = cpCodeGroupService.list(type,name);
		return ServerResponseEntity.success(cpCodeGroupPage);
	}

	@GetMapping
    @ApiOperation(value = "获取企微活码分组", notes = "根据id获取企微活码分组")
    public ServerResponseEntity<CpCodeGroup> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpCodeGroupService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存企微活码分组", notes = "保存企微活码分组")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CpCodeGroupDTO cpCodeGroupDTO) {
        CpCodeGroup cpCodeGroup = mapperFacade.map(cpCodeGroupDTO, CpCodeGroup.class);
        cpCodeGroup.setIsDeleted(0);
        cpCodeGroup.setCreateBy(AuthUserContext.get().getUsername());
        cpCodeGroup.setCreateTime(new Date());
        cpCodeGroupService.saveTo(cpCodeGroup);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新企微活码分组", notes = "更新企微活码分组")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CpCodeGroupDTO cpCodeGroupDTO) {
        CpCodeGroup cpCodeGroup = mapperFacade.map(cpCodeGroupDTO, CpCodeGroup.class);
        cpCodeGroupService.update(cpCodeGroup);
        cpCodeGroup.setUpdateBy(AuthUserContext.get().getUsername());
        cpCodeGroup.setUpdateTime(new Date());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除企微活码分组", notes = "根据企微活码分组id删除企微活码分组")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        cpCodeGroupService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
