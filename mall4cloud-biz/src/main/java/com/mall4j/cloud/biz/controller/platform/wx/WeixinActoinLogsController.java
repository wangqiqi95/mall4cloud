package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinActoinLogs;
import com.mall4j.cloud.biz.service.WeixinActoinLogsService;
import com.mall4j.cloud.biz.vo.WeixinActoinLogsVO;
import com.mall4j.cloud.biz.dto.WeixinActoinLogsDTO;

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
 * 公众号事件推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:16
 */
@RestController("platformWeixinActoinLogsController")
@RequestMapping("/p/weixin/alogs")
@Api(tags = "公众号事件推送日志")
public class WeixinActoinLogsController {

    @Autowired
    private WeixinActoinLogsService weixinActoinLogsService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取公众号事件推送日志列表", notes = "分页获取公众号事件推送日志列表")
	public ServerResponseEntity<PageVO<WeixinActoinLogs>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinActoinLogs> weixinActoinLogsPage = weixinActoinLogsService.page(pageDTO);
		return ServerResponseEntity.success(weixinActoinLogsPage);
	}

	@GetMapping
    @ApiOperation(value = "获取公众号事件推送日志", notes = "根据id获取公众号事件推送日志")
    public ServerResponseEntity<WeixinActoinLogs> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(weixinActoinLogsService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存公众号事件推送日志", notes = "保存公众号事件推送日志")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinActoinLogsDTO weixinActoinLogsDTO) {
        WeixinActoinLogs weixinActoinLogs = mapperFacade.map(weixinActoinLogsDTO, WeixinActoinLogs.class);
        weixinActoinLogs.setId(null);
        weixinActoinLogsService.save(weixinActoinLogs);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新公众号事件推送日志", notes = "更新公众号事件推送日志")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinActoinLogsDTO weixinActoinLogsDTO) {
        WeixinActoinLogs weixinActoinLogs = mapperFacade.map(weixinActoinLogsDTO, WeixinActoinLogs.class);
        weixinActoinLogsService.updateTo(weixinActoinLogs);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除公众号事件推送日志", notes = "根据公众号事件推送日志id删除公众号事件推送日志")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        weixinActoinLogsService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
