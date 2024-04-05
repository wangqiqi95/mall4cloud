package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityUpdateDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 佣金配置-活动佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@RestController("multishopDistributionCommissionActivityController")
@RequestMapping("/p/distribution_commission_activity")
@Api(tags = "平台端-佣金配置-活动佣金")
public class DistributionCommissionActivityController {

    @Autowired
    private DistributionCommissionActivityService distributionCommissionActivityService;

	@GetMapping("/page")
	@ApiOperation(value = "获取佣金配置-活动佣金列表", notes = "分页获取佣金配置-活动佣金列表")
	public ServerResponseEntity<PageVO<DistributionCommissionActivity>> page(@Valid PageDTO pageDTO, DistributionCommissionActivitySearchDTO commissionActivitySearchDTO) {
		PageVO<DistributionCommissionActivity> distributionCommissionActivityPage = distributionCommissionActivityService.page(pageDTO, commissionActivitySearchDTO);
		return ServerResponseEntity.success(distributionCommissionActivityPage);
	}

	@GetMapping("/getById")
    @ApiOperation(value = "获取佣金配置-活动佣金", notes = "根据id获取佣金配置-活动佣金")
    public ServerResponseEntity<DistributionCommissionActivity> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionCommissionActivityService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存佣金配置-活动佣金", notes = "保存佣金配置-活动佣金")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionCommissionActivityDTO commissionActivityDTO) {
        saveOrUpdateCheck(commissionActivityDTO, false);
        distributionCommissionActivityService.save(commissionActivityDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新佣金配置-活动佣金", notes = "更新佣金配置-活动佣金")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionCommissionActivityDTO commissionActivityDTO) {
        saveOrUpdateCheck(commissionActivityDTO, true);
        distributionCommissionActivityService.update(commissionActivityDTO);
        return ServerResponseEntity.success();
    }


    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新佣金配置-活动佣金状态", notes = "支持更新活动状态、活动比例状态")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO) {
	    if (distributionCommissionActivityUpdateDTO.getStatus() == null
                && distributionCommissionActivityUpdateDTO.getDevelopRateStatus() == null
                && distributionCommissionActivityUpdateDTO.getGuideRateStatus() == null
                && distributionCommissionActivityUpdateDTO.getShareRateStatus() == null) {
	        throw new LuckException("佣金状态不能为空");
        }
        distributionCommissionActivityService.updateStatus(distributionCommissionActivityUpdateDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateRate")
    @ApiOperation(value = "更新佣金配置-活动佣金比例", notes = "支持更新导购、微客、发展佣金")
    public ServerResponseEntity<Void> updateRate(@Valid @RequestBody DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO) {
	    if (distributionCommissionActivityUpdateDTO.getShareRate() == null
                && distributionCommissionActivityUpdateDTO.getGuideRate() == null
                && distributionCommissionActivityUpdateDTO.getDevelopRate() == null) {
            throw new LuckException("佣金比例不能为空");
        }
        distributionCommissionActivityService.updateRate(distributionCommissionActivityUpdateDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateActivityTime")
    @ApiOperation(value = "更新佣金配置-活动佣金有效期", notes = "更新佣金配置-活动佣金有效期")
    public ServerResponseEntity<Void> updateActivityTime(@Valid @RequestBody DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO) {
	    if (distributionCommissionActivityUpdateDTO.getStartTime() == null && distributionCommissionActivityUpdateDTO.getEndTime() == null) {
            throw new LuckException("活动佣金有效期不能为空");
        }
        distributionCommissionActivityService.updateActivityTime(distributionCommissionActivityUpdateDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除佣金配置-活动佣金", notes = "根据佣金配置-活动佣金id删除佣金配置-活动佣金")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionCommissionActivityService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updatePriority")
    @ApiOperation(value = "更新佣金配置-活动佣金优先级", notes = "更新佣金配置-活动佣金优先级")
    public ServerResponseEntity<Void> updatePriority(@RequestParam Long sourceId, @RequestParam Long targetId) {
        distributionCommissionActivityService.updatePriority(sourceId, targetId);
        return ServerResponseEntity.success();
    }

    private void saveOrUpdateCheck(DistributionCommissionActivityDTO distributionCommissionActivityDTO, boolean isUpdate) {
	    if (isUpdate && Objects.isNull(distributionCommissionActivityDTO.getId())) {
            throw new LuckException("活动ID不能为空");
        }
        if (distributionCommissionActivityDTO.getLimitTimeType() == 1) {
            if (Objects.isNull(distributionCommissionActivityDTO.getStartTime())
                    || Objects.isNull(distributionCommissionActivityDTO.getEndTime())) {
                throw new LuckException("活动开始时间或活动结束时间不能为空");
            }
        }
        if (distributionCommissionActivityDTO.getLimitStoreType() == 1) {
            if (CollectionUtils.isEmpty(distributionCommissionActivityDTO.getLimitStoreIdList())) {
                throw new LuckException("适用门店未指定门店");
            }
        }
        if (distributionCommissionActivityDTO.getLimitSpuType() == 1) {
            if (CollectionUtils.isEmpty(distributionCommissionActivityDTO.getLimitSpuIdList())) {
                throw new LuckException("商品范围未指定商品");
            }
        }
    }
}
