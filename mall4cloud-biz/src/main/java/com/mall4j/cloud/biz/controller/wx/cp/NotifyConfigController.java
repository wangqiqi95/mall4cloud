package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.MessageDTO;
import com.mall4j.cloud.biz.model.cp.NotifyConfig;
import com.mall4j.cloud.biz.service.cp.NotifyConfigService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 应用消息配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@RestController("platformNotifyConfigController")
@RequestMapping("/p/cp/notify_config")
@Api(tags = "应用消息配置")
public class NotifyConfigController {

    private final NotifyConfigService notifyConfigService;

	private final MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取应用消息配置列表", notes = "分页获取应用消息配置列表")
	public ServerResponseEntity<PageVO<NotifyConfig>> page(@Valid PageDTO pageDTO) {
		PageVO<NotifyConfig> messagePage = notifyConfigService.page(pageDTO);
		return ServerResponseEntity.success(messagePage);
	}

	@GetMapping
    @ApiOperation(value = "获取应用消息配置", notes = "根据id获取应用消息配置")
    public ServerResponseEntity<NotifyConfig> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(notifyConfigService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存应用消息配置", notes = "保存应用消息配置")
    public ServerResponseEntity<Void> save(@Valid @RequestBody MessageDTO messageDTO) {
        NotifyConfig message = mapperFacade.map(messageDTO, NotifyConfig.class);
        message.setFlag(StatusType.WX.getCode());
        message.setStatus(StatusType.YX.getCode());
        message.setCreateBy(AuthUserContext.get().getUserId());
        message.setCreateTime(new Date());
        message.setUpdateTime(message.getCreateTime());
        notifyConfigService.save(message);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新应用消息配置", notes = "更新应用消息配置")
    public ServerResponseEntity<Void> update(@Valid @RequestBody MessageDTO messageDTO) {
        NotifyConfig message = mapperFacade.map(messageDTO, NotifyConfig.class);
        message.setUpdateTime(new Date());
        notifyConfigService.update(message);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除应用消息配置", notes = "根据应用消息配置id删除应用消息配置")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        notifyConfigService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
