package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionMsgDTO;
import com.mall4j.cloud.distribution.model.DistributionMsg;
import com.mall4j.cloud.distribution.service.DistributionMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 分销公告信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
@RestController("platformDistributionMsgController")
@RequestMapping("/p/distribution_msg")
@Api(tags = "分销公告信息")
public class DistributionMsgController {

    @Autowired
    private DistributionMsgService distributionMsgService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销公告信息列表", notes = "分页获取分销公告信息列表")
	public ServerResponseEntity<PageVO<DistributionMsg>> page(@Valid PageDTO pageDTO,String msgTitle) {
		PageVO<DistributionMsg> distributionMsgPage = distributionMsgService.page(pageDTO,msgTitle);
		return ServerResponseEntity.success(distributionMsgPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销公告信息", notes = "根据msgId获取分销公告信息")
    public ServerResponseEntity<DistributionMsg> getByMsgId(@RequestParam Long msgId) {
        return ServerResponseEntity.success(distributionMsgService.getByMsgId(msgId));
    }

    @PostMapping
    @ApiOperation(value = "保存分销公告信息", notes = "保存分销公告信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionMsgDTO distributionMsgDTO) {
        DistributionMsg distributionMsg = mapperFacade.map(distributionMsgDTO, DistributionMsg.class);
        distributionMsg.setMsgId(null);
        distributionMsg.setUpdateTime(new Date());
        distributionMsgService.save(distributionMsg);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销公告信息", notes = "更新分销公告信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionMsgDTO distributionMsgDTO) {
        DistributionMsg distributionMsg = mapperFacade.map(distributionMsgDTO, DistributionMsg.class);
        distributionMsg.setUpdateTime(new Date());
        distributionMsgService.update(distributionMsg);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销公告信息", notes = "根据分销公告信息id删除分销公告信息")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> msgIds) {
        distributionMsgService.deleteBatch(msgIds);
        return ServerResponseEntity.success();
    }
}
