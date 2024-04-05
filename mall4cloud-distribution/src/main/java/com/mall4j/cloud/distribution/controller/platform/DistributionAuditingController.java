package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionAuditingDTO;
import com.mall4j.cloud.distribution.model.DistributionAuditing;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.service.DistributionAuditingService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionAuditingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 分销员申请信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
@RestController("platformDistributionAuditingController")
@RequestMapping("/p/distribution_auditing")
@Api(tags = "platform-分销员申请信息")
public class DistributionAuditingController {

    @Autowired
    private DistributionAuditingService distributionAuditingService;

    @Autowired
    private DistributionUserService distributionUserService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员申请信息列表", notes = "分页获取分销员申请信息列表")
	public ServerResponseEntity<PageVO<DistributionAuditingVO>> page(@Valid PageDTO pageDTO, DistributionAuditingDTO distributionAuditingDTO) {
        PageVO<DistributionAuditingVO> distributionAuditingPage = distributionAuditingService.pageDistributionAuditing(pageDTO, distributionAuditingDTO);
        return ServerResponseEntity.success(distributionAuditingPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销员申请信息", notes = "根据auditingId获取分销员申请信息")
    public ServerResponseEntity<DistributionAuditing> getByAuditingId(@RequestParam Long auditingId) {
        return ServerResponseEntity.success(distributionAuditingService.getByAuditingId(auditingId));
    }

    @PutMapping
    @ApiOperation(value = "审核分销员")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionAuditingDTO distributionAuditingDTO) {
        DistributionAuditing distributionAuditing = mapperFacade.map(distributionAuditingDTO, DistributionAuditing.class);
        distributionAuditing.setUpdateTime(new Date());
        distributionAuditing.setModifier(AuthUserContext.get().getUserId());
        distributionAuditingService.examine(distributionAuditing);
        // 清除缓存
        DistributionUser dbDistributionUser = distributionUserService.getByDistributionUserId(distributionAuditing.getDistributionUserId());
        distributionUserService.removeCacheByUserId(dbDistributionUser.getUserId());
        return ServerResponseEntity.success();
    }
}
