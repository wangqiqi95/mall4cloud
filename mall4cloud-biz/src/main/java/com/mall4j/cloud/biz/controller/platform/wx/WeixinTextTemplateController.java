package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinTextTemplate;
import com.mall4j.cloud.biz.service.WeixinTextTemplateService;
import com.mall4j.cloud.biz.vo.WeixinTextTemplateVO;
import com.mall4j.cloud.biz.dto.WeixinTextTemplateDTO;

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
 * 微信文本模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:52:50
 */
//@RestController("platformWeixinTextTemplateController")
//@RequestMapping("/m/weixin_text_template")
//@Api(tags = "微信文本模板表")
public class WeixinTextTemplateController {

    @Autowired
    private WeixinTextTemplateService weixinTextTemplateService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信文本模板表列表", notes = "分页获取微信文本模板表列表")
	public ServerResponseEntity<PageVO<WeixinTextTemplate>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinTextTemplate> weixinTextTemplatePage = weixinTextTemplateService.page(pageDTO);
		return ServerResponseEntity.success(weixinTextTemplatePage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信文本模板表", notes = "根据id获取微信文本模板表")
    public ServerResponseEntity<WeixinTextTemplate> getById(@RequestParam String id) {
        return ServerResponseEntity.success(weixinTextTemplateService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信文本模板表", notes = "保存微信文本模板表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinTextTemplateDTO weixinTextTemplateDTO) {
        WeixinTextTemplate weixinTextTemplate = mapperFacade.map(weixinTextTemplateDTO, WeixinTextTemplate.class);
        weixinTextTemplate.setId(null);
        weixinTextTemplateService.save(weixinTextTemplate);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信文本模板表", notes = "更新微信文本模板表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinTextTemplateDTO weixinTextTemplateDTO) {
        WeixinTextTemplate weixinTextTemplate = mapperFacade.map(weixinTextTemplateDTO, WeixinTextTemplate.class);
        weixinTextTemplateService.update(weixinTextTemplate);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信文本模板表", notes = "根据微信文本模板表id删除微信文本模板表")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinTextTemplateService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
