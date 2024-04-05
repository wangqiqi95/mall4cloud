package com.mall4j.cloud.biz.controller.platform.wx;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinImgTemplate;
import com.mall4j.cloud.biz.service.WeixinImgTemplateService;
import com.mall4j.cloud.biz.vo.WeixinImgTemplateVO;
import com.mall4j.cloud.biz.dto.WeixinImgTemplateDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;

/**
 * 微信图片模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
//@RestController("platformWeixinImgTemplateController")
//@RequestMapping("/p/weixin/imgtemplate")
//@Api(tags = "微信图片模板")
@Slf4j
public class WeixinImgTemplateController {

    @Autowired
    private WeixinImgTemplateService weixinImgTemplateService;

    @Autowired
	private MapperFacade mapperFacade;

//	@GetMapping("/page")
//	@ApiOperation(value = "获取微信图片模板表列表", notes = "分页获取微信图片模板表列表")
//	public ServerResponseEntity<PageVO<WeixinImgTemplate>> page(@Valid PageDTO pageDTO) {
//		PageVO<WeixinImgTemplate> weixinImgTemplatePage = weixinImgTemplateService.page(pageDTO);
//		return ServerResponseEntity.success(weixinImgTemplatePage);
//	}
//
//	@GetMapping
//    @ApiOperation(value = "获取微信图片模板表", notes = "根据id获取微信图片模板表")
//    public ServerResponseEntity<WeixinImgTemplate> getById(@RequestParam String id) {
//        return ServerResponseEntity.success(weixinImgTemplateService.getById(id));
//    }
//
//    @PostMapping
//    @ApiOperation(value = "保存微信图片模板表", notes = "保存微信图片模板表")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinImgTemplateDTO weixinImgTemplateDTO) {
//        WeixinImgTemplate weixinImgTemplate = mapperFacade.map(weixinImgTemplateDTO, WeixinImgTemplate.class);
//        weixinImgTemplate.setId(null);
//        weixinImgTemplateService.save(weixinImgTemplate);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新微信图片模板表", notes = "更新微信图片模板表")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinImgTemplateDTO weixinImgTemplateDTO) {
//        WeixinImgTemplate weixinImgTemplate = mapperFacade.map(weixinImgTemplateDTO, WeixinImgTemplate.class);
//        weixinImgTemplateService.update(weixinImgTemplate);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除微信图片模板表", notes = "根据微信图片模板表id删除微信图片模板表")
//    public ServerResponseEntity<Void> delete(@RequestParam String id) {
//        weixinImgTemplateService.deleteById(id);
//        return ServerResponseEntity.success();
//    }
}
