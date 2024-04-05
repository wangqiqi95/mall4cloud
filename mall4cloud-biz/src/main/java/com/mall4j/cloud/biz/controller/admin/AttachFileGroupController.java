package com.mall4j.cloud.biz.controller.admin;

import com.mall4j.cloud.biz.dto.AttachFileGroupDTO;
import com.mall4j.cloud.biz.model.AttachFileGroup;
import com.mall4j.cloud.biz.service.AttachFileGroupService;
import com.mall4j.cloud.biz.vo.AttachFileGroupVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author YXF
 * @date 2020-12-04 16:15:02
 */
@RestController("adminAttachFileGroupController")
@RequestMapping("/mp/attach_file_group")
@Api(tags = "admin-文件分组")
public class AttachFileGroupController {
    @Autowired
    private AttachFileGroupService attachFileGroupService;
    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/list")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<List<AttachFileGroupVO>> list(@RequestParam(value = "type", defaultValue = "1") Integer type) {
		List<AttachFileGroupVO> attachFileGroupPage = attachFileGroupService.list(type);
		return ServerResponseEntity.success(attachFileGroupPage);
	}

	@GetMapping
    @ApiOperation(value = "获取", notes = "根据attachFileGroupId获取")
    public ServerResponseEntity<AttachFileGroupVO> getByAttachFileGroupId(@RequestParam Long attachFileGroupId) {
        return ServerResponseEntity.success(attachFileGroupService.getByAttachFileGroupId(attachFileGroupId));
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody AttachFileGroupDTO attachFileGroupDTO) {
        AttachFileGroup attachFileGroup = mapperFacade.map(attachFileGroupDTO, AttachFileGroup.class);
        attachFileGroup.setAttachFileGroupId(null);
        attachFileGroupService.save(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AttachFileGroupDTO attachFileGroupDTO) {
	    attachFileGroupDTO.setShopId(AuthUserContext.get().getTenantId());
        AttachFileGroup attachFileGroup = mapperFacade.map(attachFileGroupDTO, AttachFileGroup.class);
        attachFileGroupService.update(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除", notes = "根据id删除")
    public ServerResponseEntity<Void> delete(@RequestParam Long attachFileGroupId) {
        AttachFileGroupVO dbAttachFileGroup = attachFileGroupService.getByAttachFileGroupId(attachFileGroupId);
        if (!Objects.equals(dbAttachFileGroup.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        attachFileGroupService.deleteById(attachFileGroupId);
        return ServerResponseEntity.success();
    }
}
