package com.mall4j.cloud.api.flow.feign;

import com.mall4j.cloud.api.flow.vo.UserAnalysisVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author YXF
 * @date 2021/07/01
 */
@FeignClient(value = "mall4cloud-flow",contextId ="flow")
public interface FlowFeignClient {

	/**
	 * 删除商品统计数据
	 * @param spuId 商品id
	 * @return void
	 */
	@DeleteMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/flow/deleteSpuDataBySpuId")
	ServerResponseEntity<Void> deleteSpuDataBySpuId(@RequestBody Long spuId);

	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/flow/listUserAnalysisByUserId")
	ServerResponseEntity<PageVO<UserAnalysisVO>> listUserAnalysisByUserId(@RequestParam("userId") Long userId, @RequestBody PageDTO pageDTO);
}
