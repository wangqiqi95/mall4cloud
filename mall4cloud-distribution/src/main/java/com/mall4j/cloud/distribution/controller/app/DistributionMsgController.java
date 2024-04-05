package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.distribution.model.DistributionMsg;
import com.mall4j.cloud.distribution.service.DistributionMsgService;
import com.mall4j.cloud.distribution.vo.DistributionMsgVO;
import com.mall4j.cloud.distribution.dto.DistributionMsgDTO;

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
 * 分销公告信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
@RestController("appDistributionMsgController")
@RequestMapping("/distribution_msg")
@Api(tags = "app-分销公告信息")
public class DistributionMsgController {

    @Autowired
    private DistributionMsgService distributionMsgService;

    @GetMapping("/page")
    @ApiOperation(value = "获取分销公告信息列表", notes = "分页获取分销公告信息列表")
    public ServerResponseEntity<PageVO<DistributionMsg>> page(@Valid PageDTO pageDTO, @RequestParam(value = "isTop", required = false) Integer isTop) {
        PageVO<DistributionMsg> distributionMsgPage = distributionMsgService.pageApp(pageDTO,isTop);
        return ServerResponseEntity.success(distributionMsgPage);
    }

    @GetMapping
    @ApiOperation(value = "获取分销公告信息", notes = "根据msgId获取分销公告信息")
    public ServerResponseEntity<DistributionMsg> getByMsgId(@RequestParam Long msgId) {
        return ServerResponseEntity.success(distributionMsgService.getByMsgId(msgId));
    }
}
