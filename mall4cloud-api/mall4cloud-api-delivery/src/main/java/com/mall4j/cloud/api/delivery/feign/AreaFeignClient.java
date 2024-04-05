package com.mall4j.cloud.api.delivery.feign;


import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author YXF
 * @date 2021/06/02
 */
@FeignClient(value = "mall4cloud-delivery",contextId ="area")
public interface AreaFeignClient {

	/**
	 * 获取省信息列表
	 * @return 省信息列表
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/area/listProvinceArea")
	ServerResponseEntity<List<AreaVO>> listProvinceArea();

    /**
     * 获取全部省市区数据
     * @return 省信息列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/area/allArea")
    ServerResponseEntity<List<AreaVO>> allArea();
	
	/**
	 *  根据areaId获取省市区地区信息
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/area/getByAreaId")
	ServerResponseEntity<AreaVO> getByAreaId(@RequestParam("areaId") Long areaId);
}
