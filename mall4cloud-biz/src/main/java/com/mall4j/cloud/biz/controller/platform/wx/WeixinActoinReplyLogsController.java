package com.mall4j.cloud.biz.controller.platform.wx;

import com.mall4j.cloud.biz.model.WeixinActoinReplyLogs;
import com.mall4j.cloud.biz.service.WeixinActoinReplyLogsService;
import com.mall4j.cloud.biz.vo.WeixinActoinReplyLogsVO;
import com.mall4j.cloud.biz.dto.WeixinActoinReplyLogsDTO;

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
 * 公众号事件推送回复日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:36
 */
@RestController("platformWeixinActoinReplyLogsController")
@RequestMapping("/p/weixin/arlogs")
@Api(tags = "公众号事件推送回复日志")
public class WeixinActoinReplyLogsController {

    @Autowired
    private WeixinActoinReplyLogsService weixinActoinReplyLogsService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取公众号事件推送回复日志列表", notes = "分页获取公众号事件推送回复日志列表")
	public ServerResponseEntity<PageVO<WeixinActoinReplyLogs>> page(@Valid PageDTO pageDTO) {
		PageVO<WeixinActoinReplyLogs> weixinActoinReplyLogsPage = weixinActoinReplyLogsService.page(pageDTO);
		return ServerResponseEntity.success(weixinActoinReplyLogsPage);
	}

	@GetMapping
    @ApiOperation(value = "获取公众号事件推送回复日志", notes = "根据id获取公众号事件推送回复日志")
    public ServerResponseEntity<WeixinActoinReplyLogs> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(weixinActoinReplyLogsService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存公众号事件推送回复日志", notes = "保存公众号事件推送回复日志")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WeixinActoinReplyLogsDTO weixinActoinReplyLogsDTO) {
        WeixinActoinReplyLogs weixinActoinReplyLogs = mapperFacade.map(weixinActoinReplyLogsDTO, WeixinActoinReplyLogs.class);
        weixinActoinReplyLogs.setId(null);
        weixinActoinReplyLogsService.save(weixinActoinReplyLogs);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新公众号事件推送回复日志", notes = "更新公众号事件推送回复日志")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WeixinActoinReplyLogsDTO weixinActoinReplyLogsDTO) {
        WeixinActoinReplyLogs weixinActoinReplyLogs = mapperFacade.map(weixinActoinReplyLogsDTO, WeixinActoinReplyLogs.class);
        weixinActoinReplyLogsService.update(weixinActoinReplyLogs);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除公众号事件推送回复日志", notes = "根据公众号事件推送回复日志id删除公众号事件推送回复日志")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        weixinActoinReplyLogsService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
