package com.mall4j.cloud.platform.controller.app;

import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.service.OrganizationService;
import com.mall4j.cloud.platform.vo.OrganizationVO;
import com.mall4j.cloud.platform.dto.OrganizationDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 组织结构表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
@RestController("appOrganizationController")
@RequestMapping("/organization")
@Api(tags = "组织结构表")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取组织结构表列表", notes = "分页获取组织结构表列表")
	public ServerResponseEntity<PageVO<Organization>> page(@Valid PageDTO pageDTO) {
		PageVO<Organization> organizationPage = organizationService.page(pageDTO);
		return ServerResponseEntity.success(organizationPage);
	}

    @GetMapping("/list")
    @ApiOperation(value = "查询片区、店群集合", notes = "查询片区、店群集合组织结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type" , value = "节点类型 1-品牌 2-片区，3-店群 ，4-门店"),
            @ApiImplicitParam(name = "keyword" , value = "关键字"),
    })
    public ServerResponseEntity<List<Organization>> list(@RequestParam(value = "type") Integer type , @RequestParam(value = "keyword",required = false) String keyword) {
        List<Organization> organizations =  organizationService.list(type,keyword);
        return ServerResponseEntity.success(organizations);
    }

    @GetMapping("/total")
    @ApiOperation(value = "查询组织节点", notes = "查询所有组织节点按所属结构分配")
    public ServerResponseEntity<List<OrganizationVO>> total() {
        List<OrganizationVO> organizations =  organizationService.total();
        return ServerResponseEntity.success(organizations);
    }

    @GetMapping("/child")
    @ApiOperation(value = "查询下级组织节点", notes = "查询下级组织节点")
    public ServerResponseEntity<List<Organization>> childList(@RequestParam(value = "parentId",defaultValue = "1")Long parentId) {
        List<Organization> organizations =  organizationService.childList(parentId);
        return ServerResponseEntity.success(organizations);
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询组织节点", notes = "查询所有组织节点")
    public ServerResponseEntity<List<Organization>> all() {
        List<Organization> organizations =  organizationService.all();
        return ServerResponseEntity.success(organizations);
    }


    @GetMapping
    @ApiOperation(value = "获取组织结构表", notes = "根据orgId获取组织结构表")
    public ServerResponseEntity<Organization> getByOrgId(@RequestParam Long orgId) {
        return ServerResponseEntity.success(organizationService.getByOrgId(orgId));
    }

    @PostMapping
    @ApiOperation(value = "保存组织结构表", notes = "保存组织结构表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody OrganizationDTO organizationDTO) {
        Organization organization = mapperFacade.map(organizationDTO, Organization.class);
        organizationService.save(organization);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新组织结构表", notes = "更新组织结构表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody OrganizationDTO organizationDTO) {
        Organization organization = mapperFacade.map(organizationDTO, Organization.class);
        organizationService.update(organization);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除组织结构表", notes = "根据组织结构表id删除组织结构表")
    public ServerResponseEntity<Void> delete(@RequestParam Long orgId) {
        organizationService.deleteById(orgId);
        return ServerResponseEntity.success();
    }
}
