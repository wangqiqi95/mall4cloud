package com.mall4j.cloud.biz.controller.app.staff;

import com.mall4j.cloud.biz.dto.cp.CpChatScriptMenuDTO;
import com.mall4j.cloud.biz.model.cp.CpChatScriptMenu;
import com.mall4j.cloud.biz.service.cp.CpChatScriptMenuService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 话术分类表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@RestController("staffCpChatScriptMenuController")
@RequestMapping("/s/cp_chat_script_menu")
@Api(tags = "导购 话术分类表")
public class CpChatScriptMenuController {

    @Autowired
    private CpChatScriptMenuService cpChatScriptMenuService;
    @Autowired
	private MapperFacade mapperFacade;


//    @GetMapping("/listParent")
//    @ApiOperation(value = "查询全部的一级分类", notes = "查询全部的一级分类")
//    public ServerResponseEntity<List<CpChatScriptMenu>> listParent() {
//        return ServerResponseEntity.success(cpChatScriptMenuService.listParent());
//    }
//    @GetMapping("/listChildren")
//    @ApiOperation(value = "查询全部的二级分类", notes = "查询全部的二级分类")
//    public ServerResponseEntity<List<CpChatScriptMenu>> listChildren(@RequestParam Long id) {
//        return ServerResponseEntity.success(cpChatScriptMenuService.listChildren(id));
//    }
    @GetMapping("/listParentContainChildren")
    @ApiOperation(value = "返回一级类型 包含 子类型的list", notes = "返回一级类型 包含 子类型的list")
    public ServerResponseEntity<List<CpChatScriptMenu>> listParentContainChildren(@RequestParam Integer type) {
        Integer isShow=1;
        return ServerResponseEntity.success(cpChatScriptMenuService.listParentContainChildren(type,isShow));
    }

//	@GetMapping("/page")
//	@ApiOperation(value = "获取话术分类表列表", notes = "分页获取话术分类表列表")
//	public ServerResponseEntity<PageVO<CpChatScriptMenu>> page(@Valid PageDTO pageDTO) {
//		PageVO<CpChatScriptMenu> cpChatScriptMenuPage = cpChatScriptMenuService.page(pageDTO);
//		return ServerResponseEntity.success(cpChatScriptMenuPage);
//	}

	@GetMapping
    @ApiOperation(value = "获取话术分类表", notes = "根据id获取话术分类表")
    public ServerResponseEntity<CpChatScriptMenu> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpChatScriptMenuService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存话术分类表", notes = "保存话术分类表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CpChatScriptMenuDTO cpChatScriptMenuDTO) {
        CpChatScriptMenu cpChatScriptMenu = mapperFacade.map(cpChatScriptMenuDTO, CpChatScriptMenu.class);
        cpChatScriptMenuService.save(cpChatScriptMenu);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新话术分类表", notes = "更新话术分类表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CpChatScriptMenuDTO cpChatScriptMenuDTO) {
        CpChatScriptMenu cpChatScriptMenu = mapperFacade.map(cpChatScriptMenuDTO, CpChatScriptMenu.class);
        cpChatScriptMenuService.update(cpChatScriptMenu);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "逻辑删除话术分类", notes = "逻辑删除话术分类")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        cpChatScriptMenuService.logicDeleteById(id);
        return ServerResponseEntity.success();
    }
}
