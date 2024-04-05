package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.distribution.model.DistributionPurchaseRecord;
import com.mall4j.cloud.distribution.service.DistributionPurchaseRecordService;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import com.mall4j.cloud.distribution.vo.DistributionPurchaseRecordVO;
import com.mall4j.cloud.distribution.dto.DistributionPurchaseRecordDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 分销推广-加购记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@RestController("appDistributionPurchaseRecordController")
@RequestMapping("/distribution_purchase_record")
@Api(tags = "微客小程序-分销推广-加购记录")
public class DistributionPurchaseRecordController {

    @Autowired
    private DistributionPurchaseRecordService distributionPurchaseRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-加购记录列表", notes = "分页获取分销推广-加购记录列表")
	public ServerResponseEntity<PageVO<DistributionPurchaseRecordVO>> page(@Valid PageDTO pageDTO, DistributionPurchaseRecordDTO dto) {
        dto.setShareId(AuthUserContext.get().getUserId());
        dto.setShareType(2);
		PageVO<DistributionPurchaseRecordVO> distributionPurchaseRecordPage = distributionPurchaseRecordService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionPurchaseRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-加购记录", notes = "根据id获取分销推广-加购记录")
    public ServerResponseEntity<DistributionPurchaseRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionPurchaseRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-加购记录", notes = "保存分销推广-加购记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionPurchaseRecordDTO distributionPurchaseRecordDTO) {
        DistributionPurchaseRecord distributionPurchaseRecord = mapperFacade.map(distributionPurchaseRecordDTO, DistributionPurchaseRecord.class);
        distributionPurchaseRecordService.save(distributionPurchaseRecord);
        return ServerResponseEntity.success();
    }

    @GetMapping("/stat")
    @ApiOperation(value = "分享推广-加购记录统计", notes = "分享推广-下单记录统计")
    public ServerResponseEntity<DistributionPromotionStatVO> stat() {
        DistributionPromotionStatVO distributionPromotionStatVO = distributionPurchaseRecordService.stat(2,
                AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(distributionPromotionStatVO);
    }

}
