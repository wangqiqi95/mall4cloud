
package com.mall4j.cloud.platform.controller.platform;


import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.platform.dto.GrowthConfigDTO;
import com.mall4j.cloud.platform.model.SysConfig;
import com.mall4j.cloud.platform.service.SysConfigService;
import com.mall4j.cloud.platform.vo.GrowthConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * 成长值基本配置信息
 * @author lhd
 */
@RestController
@RequestMapping("/p/growth_config")
public class GrowthConfigController {

	@Autowired
	private SysConfigService sysConfigService;


	/**
	 * 配置信息
	 */
	@GetMapping("/info/{key}")
	public ServerResponseEntity<GrowthConfigVO> info(@PathVariable("key") String key){
        GrowthConfigVO growthConfig = sysConfigService.getSysConfigObject(key,GrowthConfigVO.class);
		if(Objects.isNull(growthConfig)){
			return ServerResponseEntity.success();
		}
		return ServerResponseEntity.success(growthConfig);
	}

	/**
	 * 保存配置
	 */
	@PostMapping
	public ServerResponseEntity<Void> save(@RequestBody @Valid GrowthConfigDTO growthConfig){
		SysConfig config = new SysConfig();
		String paramValue = Json.toJsonString(growthConfig);
		config.setParamKey(ConfigNameConstant.GROWTH_CONFIG);
		config.setParamValue(paramValue);
		config.setRemark(ConfigNameConstant.GROWTH_REMARKS);
		sysConfigService.saveOrUpdateSysConfig(config);
        return ServerResponseEntity.success();
	}

}
