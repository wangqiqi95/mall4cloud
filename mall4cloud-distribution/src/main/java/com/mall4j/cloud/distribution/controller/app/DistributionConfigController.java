package com.mall4j.cloud.distribution.controller.app;


import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分销设置信息
 * @author cl
 * @date 2021-08-12 09:46:26
 */
@RestController("appDistributionConfigController")
@RequestMapping("/distribution_set")
@Api(tags = "分销设置信息")
public class DistributionConfigController {

    @Autowired
    private DistributionConfigService distributionConfigService;

    @GetMapping("/info")
    @ApiOperation(value = "获取分销设置信息" ,notes = "获取分销设置信息")
    public ServerResponseEntity<DistributionConfigApiVO> info(){
        return ServerResponseEntity.success(distributionConfigService.getDistributionConfig());
    }

}
