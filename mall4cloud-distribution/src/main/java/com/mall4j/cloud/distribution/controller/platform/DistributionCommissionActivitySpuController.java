package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.CommissionActivitySpuBatchUpdateDTO;
import com.mall4j.cloud.distribution.dto.CommissionActivitySpuSearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySpuDTO;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivitySpuService;
import com.mall4j.cloud.distribution.vo.DistributionCommissionActivitySpuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 佣金配置-活动佣金-商品
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@RestController("platformDistributionCommissionActivitySpuController")
@RequestMapping("/p/distribution_commission_activity_spu")
@Api(tags = "平台端-佣金配置-活动佣金-商品")
public class DistributionCommissionActivitySpuController {

    @Autowired
    private DistributionCommissionActivitySpuService distributionCommissionActivitySpuService;

	@GetMapping("/page")
	@ApiOperation(value = "分页查询活动佣金商品列表", notes = "分页查询活动佣金商品列表")
	public ServerResponseEntity<PageVO<DistributionCommissionActivitySpuVO>> page(
	        @Valid PageDTO pageDTO,
            @Valid CommissionActivitySpuSearchDTO searchDTO) {
		return ServerResponseEntity.success(distributionCommissionActivitySpuService.page(pageDTO, searchDTO));
	}

    @PostMapping("/save")
    @ApiOperation(value = "保存活动商品配置", notes = "保存活动商品配置")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionCommissionActivitySpuDTO
                                                       distributionCommissionActivitySpuDTO) {
        distributionCommissionActivitySpuService.save(distributionCommissionActivitySpuDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除活动商品", notes = "删除活动商品")
    public ServerResponseEntity<Void> delete(@Valid @RequestBody CommissionActivitySpuBatchUpdateDTO
                                                              activitySpuBatchUpdateDTO) {
        distributionCommissionActivitySpuService.delete(activitySpuBatchUpdateDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateCommissionRate")
    @ApiOperation(value = "更新活动商品佣金比率", notes = "更新活动商品佣金比率")
    public ServerResponseEntity<Void> updateCommissionRate(@Valid @RequestBody CommissionActivitySpuBatchUpdateDTO
                                                          activitySpuBatchUpdateDTO) {
        distributionCommissionActivitySpuService.updateCommissionRate(activitySpuBatchUpdateDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateCommissionRateStatus")
    @ApiOperation(value = "更新活动商品佣金状态", notes = "更新活动商品佣金状态")
    public ServerResponseEntity<Void> updateCommissionRateStatus(@Valid @RequestBody CommissionActivitySpuBatchUpdateDTO
                                                                        activitySpuBatchUpdateDTO) {
        distributionCommissionActivitySpuService.updateCommissionRateStatus(activitySpuBatchUpdateDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateActivityTime")
    @ApiOperation(value = "更新活动商品活动时间", notes = "更新活动商品活动时间")
    public ServerResponseEntity<Void> updateActivityTime(@Valid @RequestBody CommissionActivitySpuBatchUpdateDTO
                                                                              activitySpuBatchUpdateDTO) {
        distributionCommissionActivitySpuService.updateActivityTime(activitySpuBatchUpdateDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/findSpuIdListByActivityId")
    @ApiOperation(value = "获取佣金活动下商品id集合", notes = "获取佣金活动下商品id集合")
    public ServerResponseEntity<List<Long>> findSpuIdListByActivityId(@RequestParam Long activityId) {
        return ServerResponseEntity.success(distributionCommissionActivitySpuService.findSpuIdListByActivityId(activityId));
    }



}
