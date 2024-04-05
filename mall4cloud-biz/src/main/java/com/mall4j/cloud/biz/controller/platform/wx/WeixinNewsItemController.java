package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinNewsItem;
import com.mall4j.cloud.biz.service.WeixinNewsItemService;
import com.mall4j.cloud.biz.vo.WeixinNewsItemVO;
import com.mall4j.cloud.biz.dto.WeixinNewsItemDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 微信图文模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:35
 */
//@RestController("platformWeixinNewsItemController")
//@RequestMapping("/m/weixin_news_item")
//@Api(tags = "微信图文模板素材表")
public class WeixinNewsItemController {

    @Autowired
    private WeixinNewsItemService weixinNewsItemService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信图文模板素材表列表", notes = "分页获取微信图文模板素材表列表")
	public ServerResponseEntity<PageVO<WeixinNewsItem>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinNewsItem> weixinNewsItemPage = weixinNewsItemService.page(pageDTO);
		return ServerResponseEntity.success(weixinNewsItemPage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信图文模板素材表", notes = "根据id获取微信图文模板素材表")
    public ServerResponseEntity<WeixinNewsItem> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(weixinNewsItemService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信图文模板素材表", notes = "保存微信图文模板素材表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinNewsItemDTO weixinNewsItemDTO) {
        WeixinNewsItem weixinNewsItem = mapperFacade.map(weixinNewsItemDTO, WeixinNewsItem.class);
        weixinNewsItem.setId(null);
        weixinNewsItemService.save(weixinNewsItem);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信图文模板素材表", notes = "更新微信图文模板素材表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinNewsItemDTO weixinNewsItemDTO) {
        WeixinNewsItem weixinNewsItem = mapperFacade.map(weixinNewsItemDTO, WeixinNewsItem.class);
        weixinNewsItemService.update(weixinNewsItem);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信图文模板素材表", notes = "根据微信图文模板素材表id删除微信图文模板素材表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        weixinNewsItemService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
