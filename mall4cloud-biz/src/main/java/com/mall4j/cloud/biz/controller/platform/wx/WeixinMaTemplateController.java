package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.model.WeixinMaTemplate;
import com.mall4j.cloud.biz.service.WeixinMaTemplateService;
import com.mall4j.cloud.biz.service.WxMaApiService;
import com.mall4j.cloud.biz.vo.WeixinMaInfo;
import com.mall4j.cloud.biz.vo.WeixinMaTemplateVO;
import com.mall4j.cloud.biz.dto.WeixinMaTemplateDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.bean.WxMiniApp;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 微信小程序模板素材表
 *
 * @author gmq
 * @date 2022-01-18 10:53:56
 */
@RestController("platformWeixinMaTemplateController")
@RequestMapping("/p/weixin/wxma")
@Api(tags = "微信小程序模板")
public class WeixinMaTemplateController {

    @Autowired
    private WeixinMaTemplateService weixinMaTemplateService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private WxMaApiService wxMaApiService;

    @GetMapping("/getWxMaList")
	@ApiOperation(value = "获取微信小程序列表", notes = "获取微信小程序列表")
	public ServerResponseEntity<List<WeixinMaInfo>> getWxMaList() {
        List<WeixinMaInfo> list=new ArrayList<>();
        WxMiniApp wxMiniApp = feignShopConfig.getWxMiniApp();
        if(wxMiniApp!=null){
            WeixinMaInfo weixinMaInfo=new WeixinMaInfo();
            weixinMaInfo.setMaAppId(wxMiniApp.getAppId());
            list.add(weixinMaInfo);
        }
		return ServerResponseEntity.success(list);
	}

//	@GetMapping("/page")
//	@ApiOperation(value = "获取微信小程序模板素材表列表", notes = "分页获取微信小程序模板素材表列表")
//	public ServerResponseEntity<PageVO<WeixinMaTemplate>> page(@Valid PageDTO pageDTO) {
//		PageVO<WeixinMaTemplate> weixinMaTemplatePage = weixinMaTemplateService.page(pageDTO);
//		return ServerResponseEntity.success(weixinMaTemplatePage);
//	}
//
//	@GetMapping
//    @ApiOperation(value = "获取微信小程序模板素材表", notes = "根据id获取微信小程序模板素材表")
//    public ServerResponseEntity<WeixinMaTemplate> getById(@RequestParam String id) {
//        return ServerResponseEntity.success(weixinMaTemplateService.getById(id));
//    }
//
//    @PostMapping
//    @ApiOperation(value = "保存微信小程序模板素材表", notes = "保存微信小程序模板素材表")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinMaTemplateDTO weixinMaTemplateDTO) {
//        WeixinMaTemplate weixinMaTemplate = mapperFacade.map(weixinMaTemplateDTO, WeixinMaTemplate.class);
//        weixinMaTemplate.setId(null);
//        weixinMaTemplateService.save(weixinMaTemplate);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新微信小程序模板素材表", notes = "更新微信小程序模板素材表")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinMaTemplateDTO weixinMaTemplateDTO) {
//        WeixinMaTemplate weixinMaTemplate = mapperFacade.map(weixinMaTemplateDTO, WeixinMaTemplate.class);
//        weixinMaTemplateService.update(weixinMaTemplate);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除微信小程序模板素材表", notes = "根据微信小程序模板素材表id删除微信小程序模板素材表")
//    public ServerResponseEntity<Void> delete(@RequestParam String id) {
//        weixinMaTemplateService.deleteById(id);
//        return ServerResponseEntity.success();
//    }
}
