package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinTmessageSendlog;
import com.mall4j.cloud.biz.service.WeixinTmessageSendlogService;
import com.mall4j.cloud.biz.vo.WeixinTmessageSendlogVO;
import com.mall4j.cloud.biz.dto.WeixinTmessageSendlogDTO;

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
 * 微信模板消息推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
//@RestController("platformWeixinTmessageSendlogController")
//@RequestMapping("/p/weixin/tmessage/sendlog")
//@Api(tags = "微信模板消息推送日志")
public class WeixinTmessageSendlogController {

    @Autowired
    private WeixinTmessageSendlogService weixinTmessageSendlogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取微信模板消息推送日志列表", notes = "分页获取微信模板消息推送日志列表")
	public ServerResponseEntity<PageVO<WeixinTmessageSendlog>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinTmessageSendlog> weixinTmessageSendlogPage = weixinTmessageSendlogService.page(pageDTO);
		return ServerResponseEntity.success(weixinTmessageSendlogPage);
	}

	@GetMapping
    @ApiOperation(value = "获取微信模板消息推送日志", notes = "根据id获取微信模板消息推送日志")
    public ServerResponseEntity<WeixinTmessageSendlog> getById(@RequestParam String id) {
        return ServerResponseEntity.success(weixinTmessageSendlogService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存微信模板消息推送日志", notes = "保存微信模板消息推送日志")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinTmessageSendlogDTO weixinTmessageSendlogDTO) {
        WeixinTmessageSendlog weixinTmessageSendlog = mapperFacade.map(weixinTmessageSendlogDTO, WeixinTmessageSendlog.class);
        weixinTmessageSendlog.setId(null);
        weixinTmessageSendlogService.save(weixinTmessageSendlog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新微信模板消息推送日志", notes = "更新微信模板消息推送日志")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinTmessageSendlogDTO weixinTmessageSendlogDTO) {
        WeixinTmessageSendlog weixinTmessageSendlog = mapperFacade.map(weixinTmessageSendlogDTO, WeixinTmessageSendlog.class);
        weixinTmessageSendlogService.update(weixinTmessageSendlog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除微信模板消息推送日志", notes = "根据微信模板消息推送日志id删除微信模板消息推送日志")
    public ServerResponseEntity<Void> delete(@RequestParam String id) {
        weixinTmessageSendlogService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
