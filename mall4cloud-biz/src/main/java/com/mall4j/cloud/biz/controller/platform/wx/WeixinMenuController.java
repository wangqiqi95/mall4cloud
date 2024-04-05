package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.dto.WeixinMenuContentPutDTO;
import com.mall4j.cloud.biz.dto.WeixinMenuPutDTO;
import com.mall4j.cloud.biz.dto.WeixinMenuUpdateOrdersDto;
import com.mall4j.cloud.biz.model.WeixinMenu;
import com.mall4j.cloud.biz.service.WeixinMenuService;
import com.mall4j.cloud.biz.vo.WeixinMenuContentVo;
import com.mall4j.cloud.biz.vo.WeixinMenuTreeVO;
import com.mall4j.cloud.biz.vo.WeixinMenuVO;
import com.mall4j.cloud.biz.dto.WeixinMenuDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 微信菜单
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@RestController("platformWeixinMenuController")
@RequestMapping("/p/weixin/menu")
@Api(tags = "微信自定义菜单")
public class WeixinMenuController {

    @Autowired
    private WeixinMenuService weixinMenuService;

    @Autowired
	private MapperFacade mapperFacade;

    @GetMapping("/menuTrees")
    @ApiOperation(value = "获取微信菜单树形列表", notes = "获取微信菜单树形列表")
    public ServerResponseEntity<List<WeixinMenuTreeVO>> page(@RequestParam String appId) {
        List<WeixinMenuTreeVO> trees = weixinMenuService.menuTrees(appId);
        return ServerResponseEntity.success(trees);
    }

//	@GetMapping("/page")
//	@ApiOperation(value = "获取微信菜单列表", notes = "分页获取微信菜单列表")
//	public ServerResponseEntity<PageVO<WeixinMenu>> page(@Valid PageDTO pageDTO) {
//		PageVO<WeixinMenu> weixinMenuPage = weixinMenuService.page(pageDTO);
//		return ServerResponseEntity.success(weixinMenuPage);
//	}

//	@GetMapping
//    @ApiOperation(value = "获取微信菜单", notes = "根据id获取微信菜单")
//    public ServerResponseEntity<WeixinMenu> getById(@RequestParam String id) {
//        return ServerResponseEntity.success(weixinMenuService.getById(id));
//    }

    @PostMapping("/saveMenu")
    @ApiOperation(value = "保存微信菜单", notes = "保存微信菜单")
    public ServerResponseEntity<Void> saveMenu(@Valid @RequestBody WeixinMenuPutDTO menuPutDTO) {
        return weixinMenuService.saveWeixinMenu(menuPutDTO);
    }

    @PutMapping("updateMenu")
    @ApiOperation(value = "更新微信菜单", notes = "更新微信菜单")
    public ServerResponseEntity<Void> updateMenu(@Valid @RequestBody WeixinMenuPutDTO menuPutDTO) {
        return weixinMenuService.updateWeixinMenu(menuPutDTO);
    }

    @PostMapping("/saveMenuContent")
    @ApiOperation(value = "保存微信菜单-回复内容", notes = "保存微信菜单-回复内容")
    public ServerResponseEntity<Void> saveMenuContent(@Valid @RequestBody WeixinMenuContentPutDTO contentPutDTO) {
        return weixinMenuService.saveWeixinMenuContent(contentPutDTO);
    }

    @GetMapping("/getMenuContent")
    @ApiOperation(value = "获取微信菜单-回复内容", notes = "获取微信菜单-回复内容")
    public ServerResponseEntity<WeixinMenuContentVo> getMenuContent(@ApiParam(value = "菜单id",required = true) @RequestParam String id) {
        return weixinMenuService.getWeixinMenuContent(id);
    }

    @PutMapping("updateMenuOrders")
    @ApiOperation(value = "变更微信菜单位置", notes = "变更微信菜单位置")
    public ServerResponseEntity<Void> updateMenuOrders(@Valid @RequestBody WeixinMenuUpdateOrdersDto ordersDto) {
        return weixinMenuService.updateMenuOrders(ordersDto);
    }

    @GetMapping("/doSyncMenu")
    @ApiOperation(value = "发布菜单", notes = "发布菜单")
    public ServerResponseEntity<Void> doSyncMenu(@ApiParam(value = "公众号appId",required = true) @RequestParam String appId) {
        return weixinMenuService.doSyncMenu(appId);
    }


    @DeleteMapping("/delete")
    @ApiOperation(value = "删除微信菜单", notes = "根据微信菜单id删除微信菜单")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinMenuService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
