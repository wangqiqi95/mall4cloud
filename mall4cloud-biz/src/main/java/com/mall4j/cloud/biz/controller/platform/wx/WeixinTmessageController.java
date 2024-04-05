package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.dto.WeixinTmessageStatusDTO;
import com.mall4j.cloud.biz.model.WeixinTmessage;
import com.mall4j.cloud.biz.service.WeixinTmessageService;
import com.mall4j.cloud.biz.vo.TmessageSendVO;
import com.mall4j.cloud.biz.vo.WeixinTmessageAllVO;
import com.mall4j.cloud.biz.vo.WeixinTmessageVO;
import com.mall4j.cloud.biz.dto.WeixinTmessageDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 微信消息模板
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:11:00
 */
@RestController("platformWeixinTmessageController")
@RequestMapping("/p/weixin/tmessage")
@Api(tags = "微信消息模板")
public class WeixinTmessageController {

    @Autowired
    private WeixinTmessageService weixinTmessageService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信消息模板列表", notes = "分页获取微信消息模板列表")
	public ServerResponseEntity<PageVO<WeixinTmessageVO>> page(@Valid PageDTO pageDTO,
                                                               @ApiParam(value = "公众号/小程序appId",required = true)String appId,
                                                               @ApiParam(value = "模板用途：1公众号 2小程序",required = true)Integer dataSrc) {
		PageVO<WeixinTmessageVO> weixinTmessagePage = weixinTmessageService.page(pageDTO,appId,dataSrc);
		return ServerResponseEntity.success(weixinTmessagePage);
	}

    @GetMapping("/getAllPrivateTemplate")
    @ApiOperation(value = "选择微信消息模板", notes = "选择微信消息模板")
    public ServerResponseEntity<List<WxMpTemplate>> getAllPrivateTemplate(@RequestParam String appId) {
        List<WxMpTemplate> weixinTmessagePage = weixinTmessageService.getAllPrivateTemplate(appId);
        return ServerResponseEntity.success(weixinTmessagePage);
    }

	@GetMapping("/getById")
    @ApiOperation(value = "获取微信消息模板", notes = "根据id获取微信消息模板")
    public ServerResponseEntity<WeixinTmessage> getById(@RequestParam String id) {
        return ServerResponseEntity.success(weixinTmessageService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存微信消息模板", notes = "保存微信消息模板")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinTmessageDTO weixinTmessageDTO) {
        WeixinTmessage weixinTmessage = mapperFacade.map(weixinTmessageDTO, WeixinTmessage.class);
        weixinTmessage.setId(null);
        weixinTmessageService.save(weixinTmessage);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新微信消息模板", notes = "更新微信消息模板")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinTmessageDTO weixinTmessageDTO) {
        WeixinTmessage weixinTmessage = mapperFacade.map(weixinTmessageDTO, WeixinTmessage.class);
        weixinTmessageService.update(weixinTmessage);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "微信消息模板启用/禁用", notes = "微信消息模板启用/禁用")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody WeixinTmessageStatusDTO weixinTmessageDTO) {
        return weixinTmessageService.updateStatus(weixinTmessageDTO);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除微信消息模板", notes = "根据微信消息模板id删除微信消息模板")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinTmessageService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/testSendTemplateMsg")
    @ApiOperation(value = "测试发送模板消息", notes = "测试发送模板消息")
    public ServerResponseEntity<Void> sendTemplateMsg(@Valid @RequestBody TmessageSendVO tmessageSendVO) {
        weixinTmessageService.sendTemplateMsg(tmessageSendVO);
        return ServerResponseEntity.success();
    }
}
