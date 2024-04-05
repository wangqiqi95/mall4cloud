package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinNewsTemplate;
import com.mall4j.cloud.biz.service.WeixinNewsTemplateService;
import com.mall4j.cloud.biz.vo.WeixinNewsTemplateVO;
import com.mall4j.cloud.biz.dto.WeixinNewsTemplateDTO;

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
 * 微信图文模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
//@RestController("platformWeixinNewsTemplateController")
//@RequestMapping("/m/weixin_news_template")
//@Api(tags = "微信图文模板表")
public class WeixinNewsTemplateController {

    @Autowired
    private WeixinNewsTemplateService weixinNewsTemplateService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信图文模板表列表", notes = "分页获取微信图文模板表列表")
	public ServerResponseEntity<PageVO<WeixinNewsTemplate>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinNewsTemplate> weixinNewsTemplatePage = weixinNewsTemplateService.page(pageDTO);
		return ServerResponseEntity.success(weixinNewsTemplatePage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信图文模板表", notes = "根据id获取微信图文模板表")
    public ServerResponseEntity<WeixinNewsTemplate> getById(@RequestParam String id) {
        return ServerResponseEntity.success(weixinNewsTemplateService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信图文模板表", notes = "保存微信图文模板表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinNewsTemplateDTO weixinNewsTemplateDTO) {
        WeixinNewsTemplate weixinNewsTemplate = mapperFacade.map(weixinNewsTemplateDTO, WeixinNewsTemplate.class);
        weixinNewsTemplate.setId(null);
        weixinNewsTemplateService.save(weixinNewsTemplate);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信图文模板表", notes = "更新微信图文模板表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinNewsTemplateDTO weixinNewsTemplateDTO) {
        WeixinNewsTemplate weixinNewsTemplate = mapperFacade.map(weixinNewsTemplateDTO, WeixinNewsTemplate.class);
        weixinNewsTemplateService.update(weixinNewsTemplate);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信图文模板表", notes = "根据微信图文模板表id删除微信图文模板表")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinNewsTemplateService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
