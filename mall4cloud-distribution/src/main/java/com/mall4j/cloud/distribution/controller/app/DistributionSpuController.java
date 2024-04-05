package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.distribution.service.DistributionSpuService;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分销商品关联信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@RestController("appDistributionSpuController")
@RequestMapping("/ua/distribution_spu")
@Api(tags = "app-分销商品关联信息")
public class DistributionSpuController {

    @Autowired
    private DistributionSpuService distributionSpuService;

	@GetMapping
    @ApiOperation(value = "获取分销商品关联信息", notes = "根据distributionSpuId获取分销商品关联信息")
    public ServerResponseEntity<DistributionSpuVO> getByDistributionSpuId(@RequestParam Long distributionSpuId) {
        return ServerResponseEntity.success(distributionSpuService.getByDistributionSpuId(distributionSpuId));
    }

    @GetMapping("/is_state")
    @ApiOperation(value = "根据商品id与状态查看该分销商品是处于该状态", notes = "根据商品id与状态查看该分销商品是处于该状态")
    public ServerResponseEntity<Boolean> isStateBySpuIdAndState(@RequestParam("spuId") Long spuId, @RequestParam("state") Integer state) {
	    return ServerResponseEntity.success(distributionSpuService.isStateBySpuIdAndState(spuId, state));
    }
}
