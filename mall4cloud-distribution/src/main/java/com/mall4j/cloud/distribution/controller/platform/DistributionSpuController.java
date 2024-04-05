package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.service.DistributionSpuService;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
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
@RestController("platformDistributionSpuController")
@RequestMapping("/p/distribution_spu")
@Api(tags = "platform-分销商品关联信息")
public class DistributionSpuController {

    @Autowired
    private DistributionSpuService distributionSpuService;

	@GetMapping
    @ApiOperation(value = "获取分销商品关联信息", notes = "根据distributionSpuId获取分销商品关联信息")
    public ServerResponseEntity<DistributionSpuVO> getByDistributionSpuId(@RequestParam Long distributionSpuId) {
        return ServerResponseEntity.success(distributionSpuService.getByDistributionSpuId(distributionSpuId));
    }

    @PostMapping("/offline")
    @ApiOperation(value = "下线分销商品", notes = "下线分销商品")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEventDTO offlineHandleEventDTO) {
	    distributionSpuService.offline(offlineHandleEventDTO);
	    return ServerResponseEntity.success();
    }

    @PostMapping("/audit")
    @ApiOperation(value = "审核分销商品", notes = "审核分销商品")
    public ServerResponseEntity<Void> audit(@RequestBody OfflineHandleEventDTO offlineHandleEventDTO) {
	    distributionSpuService.audit(offlineHandleEventDTO);
	    return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event")
    @ApiOperation(value = "获取分销商品下线信息", notes = "获取分销商品下线信息")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@RequestParam(value = "distributionSpuId") Long distributionSpuId) {
	    return ServerResponseEntity.success(distributionSpuService.getOfflineHandleEvent(distributionSpuId));
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销商品关联信息", notes = "根据分销商品关联信息id删除分销商品关联信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long distributionSpuId) {
        distributionSpuService.deleteByIdAndShopId(distributionSpuId, null);
        return ServerResponseEntity.success();
    }

}
