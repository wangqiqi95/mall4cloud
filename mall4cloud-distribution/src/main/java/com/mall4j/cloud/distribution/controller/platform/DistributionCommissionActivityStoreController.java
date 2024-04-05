package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.CommissionActivityStoreSearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityStoreDTO;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivityStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 佣金配置-活动佣金-门店
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@RestController("platformDistributionCommissionActivityStoreController")
@RequestMapping("/p/distribution_commission_activity_store")
@Api(tags = "平台端-佣金配置-活动佣金-门店")
public class DistributionCommissionActivityStoreController {

    @Autowired
    private DistributionCommissionActivityStoreService distributionCommissionActivityStoreService;

	@GetMapping("/findStoreIdList")
	@ApiOperation(value = "活动下适用门店id集合", notes = "通过活动id查询适用门店id集合")
	public ServerResponseEntity<List<Long>> findStoreIdList(@RequestParam Long activityId) {
		return ServerResponseEntity.success(distributionCommissionActivityStoreService.findStoreIdListByActivityId(activityId));
	}

    @PostMapping("/save")
    @ApiOperation(value = "佣金活动添加门店", notes = "佣金活动添加门店")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionCommissionActivityStoreDTO distributionCommissionActivityStoreDTO) {
        distributionCommissionActivityStoreService.save(distributionCommissionActivityStoreDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "佣金活动删除门店", notes = "佣金活动删除门店")
    public ServerResponseEntity<Void> delete(@RequestParam Long activityId, @RequestParam Long storeId) {
        distributionCommissionActivityStoreService.deleteByActivityIdAndStoreId(activityId, storeId);
        return ServerResponseEntity.success();
    }
}
