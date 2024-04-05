package com.mall4j.cloud.distribution.controller.app;



import com.mall4j.cloud.api.platform.vo.DistributionRecruitConfigApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分销推广接口
 * @author cl
 */
@RestController("appDistributionRecruitConfigController")
@RequestMapping("/distribution_recruit")
@Api(tags = "分销推广信息")
public class DistributionRecruitConfigController {

    @Autowired
    private DistributionConfigService distributionConfigService;

    @GetMapping("/info")
    @ApiOperation(value = "获取分销推广配置信息" ,notes = "获取分销推广配置信息")
    public ServerResponseEntity<DistributionRecruitConfigApiVO> recruitInfo(){
        return ServerResponseEntity.success(distributionConfigService.getDistributionRecruitConfig());
    }

}
