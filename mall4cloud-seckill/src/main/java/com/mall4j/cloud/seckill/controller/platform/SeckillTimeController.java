
package com.mall4j.cloud.seckill.controller.platform;


import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.SysConfigApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.seckill.service.SeckillService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 秒杀相关配置
 * @author lhd
 */
@RestController
@RequestMapping("/p/seckill_time")
public class SeckillTimeController {

	@Autowired
	private ConfigFeignClient configFeignClient;

	@Autowired
	private SeckillService seckillService;

	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	public ServerResponseEntity<SysConfigApiVO> info(@PathVariable("id") String key){
		String value = configFeignClient.getConfig(key).getData();
		SysConfigApiVO sysConfigApiVO = new SysConfigApiVO();
		sysConfigApiVO.setParamKey(key);
		sysConfigApiVO.setParamValue(value);
		return ServerResponseEntity.success(sysConfigApiVO);
	}

	/**
	 * 保存or修改配置
	 */
	@PostMapping
	@ApiOperation(value = "保存or修改配置", notes = "显示已经结束的秒杀信息列表,specTime时间搜索")
	public ServerResponseEntity<Void> saveOrUpdate(@RequestBody SysConfigApiVO config){
		seckillService.saveSeckillTime(config);
		return ServerResponseEntity.success();
	}
}
