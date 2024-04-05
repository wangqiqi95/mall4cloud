package com.mall4j.cloud.platform.controller.platform;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.platform.dto.DistributionConfigDTO;
import com.mall4j.cloud.platform.dto.DistributionRecruitConfigDTO;
import com.mall4j.cloud.platform.model.SysConfig;
import com.mall4j.cloud.platform.service.SysConfigService;
import com.mall4j.cloud.platform.vo.DistributionConfigVO;
import com.mall4j.cloud.platform.vo.DistributionRecruitConfigVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cl
 * @date 2021-08-06 14:18:03
 */
@RestController
@RequestMapping("/p/distribution_config")
public class DistributionConfigController {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取分销配置信息
     */
    @GetMapping("/info")
    public ServerResponseEntity<DistributionConfigVO> info(){
        DistributionConfigVO distributionConfigVO = sysConfigService.getSysConfigObject(Constant.DISTRIBUTION_CONFIG, DistributionConfigVO.class);
        List<Long> spuIdList = distributionConfigVO.getSpuIdList();
        if (CollUtil.isEmpty(spuIdList)) {
            return ServerResponseEntity.success(distributionConfigVO);
        }
        return ServerResponseEntity.success(distributionConfigVO);
    }

    /**
     * 获取分销推广配置信息
     */
    @GetMapping("/recruit_info")
    public ServerResponseEntity<DistributionRecruitConfigVO> recruitInfo(){
        DistributionRecruitConfigVO recruitConfigVO = sysConfigService.getSysConfigObject(Constant.DISTRIBUTION_RECRUIT_CONFIG, DistributionRecruitConfigVO.class);
        return ServerResponseEntity.success(recruitConfigVO);
    }
    /**
     * 保存分销配置
     */
    @PostMapping
    public ServerResponseEntity<Void> save(@RequestBody @Valid DistributionConfigDTO distributionConfigDTO){
        SysConfig config = new SysConfig();
        String paramValue = Json.toJsonString(distributionConfigDTO);
        config.setParamKey(Constant.DISTRIBUTION_CONFIG);
        config.setParamValue(paramValue);
        config.setRemark(ConfigNameConstant.DISTRIBUTION_REMARKS);
        sysConfigService.saveOrUpdateSysConfig(config);
        return ServerResponseEntity.success();
    }

    /**
     * 保存分销配置
     */
    @PostMapping("/recruit")
    public ServerResponseEntity<Void> saveRecruit(@RequestBody @Valid DistributionRecruitConfigDTO distributionRecruitConfigDTO){
        SysConfig config = new SysConfig();
        String paramValue = Json.toJsonString(distributionRecruitConfigDTO);
        config.setParamKey(Constant.DISTRIBUTION_RECRUIT_CONFIG);
        config.setParamValue(paramValue);
        config.setRemark(ConfigNameConstant.DISTRIBUTION_RECRUIT_REMARKS);
        sysConfigService.saveOrUpdateSysConfig(config);
        return ServerResponseEntity.success();
    }



}
