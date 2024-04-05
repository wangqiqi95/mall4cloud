
package com.mall4j.cloud.platform.controller.platform;


import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.SysConfig;
import com.mall4j.cloud.platform.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * 其他积分相关配置
 * @author lhd
 */
@RestController
@RequestMapping("/p/score/other_related")
public class ScoreOtherRelatedConfigController {

	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	public ServerResponseEntity<SysConfig> info(@PathVariable("id") String key){
		SysConfig config = sysConfigService.getByKey(key);
		if(Objects.isNull(config)){
			return ServerResponseEntity.success(new SysConfig());
		}
		return ServerResponseEntity.success(config);
	}

	/**
	 * 保存or修改配置
	 */
	@PostMapping
	public ServerResponseEntity<Void> saveOrUpdate(@RequestBody SysConfig config){
		sysConfigService.saveOrUpdateSysConfig(config);
		return ServerResponseEntity.success();
	}
}
