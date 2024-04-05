package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionCommissionUnityDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionUnity;
import com.mall4j.cloud.distribution.service.DistributionCommissionService;
import com.mall4j.cloud.distribution.service.DistributionCommissionUnityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

/**
 * 佣金配置-统一佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@RestController("multishopDistributionCommissionUnityController")
@RequestMapping("/p/distribution_commission_unity")
@Api(tags = "平台端-佣金配置-统一佣金")
public class DistributionCommissionUnityController {

    @Autowired
    private DistributionCommissionUnityService distributionCommissionUnityService;
    @Autowired
    private DistributionCommissionService distributionCommissionService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/get")
    @ApiOperation(value = "获取佣金配置-统一佣金", notes = "获取佣金配置-统一佣金")
    public ServerResponseEntity<DistributionCommissionUnity> get() {
        return ServerResponseEntity.success(distributionCommissionUnityService.get());
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存or更新佣金配置-统一佣金", notes = "保存or更新佣金配置-统一佣金")
    public ServerResponseEntity<Void> saveOrUpdate(@Valid @RequestBody DistributionCommissionUnityDTO commissionUnityDTO) {
        DistributionCommissionUnity distributionCommissionUnity  = distributionCommissionUnityService.get();
        if (Objects.isNull(distributionCommissionUnity)) {
            distributionCommissionUnity = mapperFacade.map(commissionUnityDTO, DistributionCommissionUnity.class);
            distributionCommissionUnity.setId(null);
            distributionCommissionUnity.setOrgId(0l);
            distributionCommissionUnityService.save(distributionCommissionUnity);
        } else {
            distributionCommissionUnity.setGuideRate(commissionUnityDTO.getGuideRate());
            distributionCommissionUnity.setShareRate(commissionUnityDTO.getShareRate());
            distributionCommissionUnity.setDevelopRate(commissionUnityDTO.getDevelopRate());
            distributionCommissionUnityService.update(distributionCommissionUnity);
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/getDistributionCommissionRate")
    public ServerResponseEntity<Map<Long, DistributionCommissionRateVO>> getDistributionCommissionRate(
            @Valid @RequestBody DistributionCommissionRateDTO distributionCommissionRateDTO) {
        return ServerResponseEntity.success(distributionCommissionService.getDistributionCommissionRate(distributionCommissionRateDTO));
    }

}
