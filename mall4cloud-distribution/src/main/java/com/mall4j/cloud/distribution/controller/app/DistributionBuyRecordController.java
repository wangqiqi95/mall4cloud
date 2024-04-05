package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.distribution.model.DistributionBuyRecord;
import com.mall4j.cloud.distribution.service.DistributionBuyRecordService;
import com.mall4j.cloud.distribution.vo.DistributionBuyRecordVO;
import com.mall4j.cloud.distribution.dto.DistributionBuyRecordDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 分销推广-下单记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@RestController("appDistributionBuyRecordController")
@RequestMapping("/distribution_buy_record")
@Api(tags = "微客小程序-分销推广-下单记录")
public class DistributionBuyRecordController {

    @Autowired
    private DistributionBuyRecordService distributionBuyRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-下单记录列表", notes = "分页获取分销推广-下单记录列表")
	public ServerResponseEntity<PageVO<DistributionBuyRecordVO>> page(@Valid PageDTO pageDTO, DistributionBuyRecordDTO dto) {
	    dto.setShareId(AuthUserContext.get().getUserId());
	    dto.setShareType(2);
		PageVO<DistributionBuyRecordVO> distributionBuyRecordPage = distributionBuyRecordService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionBuyRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-下单记录", notes = "根据id获取分销推广-下单记录")
    public ServerResponseEntity<DistributionBuyRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionBuyRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-下单记录", notes = "保存分销推广-下单记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionBuyRecordDTO distributionBuyRecordDTO) {
        DistributionBuyRecord distributionBuyRecord = mapperFacade.map(distributionBuyRecordDTO, DistributionBuyRecord.class);
        distributionBuyRecordService.save(distributionBuyRecord);
        return ServerResponseEntity.success();
    }

    @GetMapping("/stat")
    @ApiOperation(value = "分享推广-下单记录统计", notes = "分享推广-下单记录统计")
    public ServerResponseEntity<DistributionPromotionStatVO> stat() {
        DistributionPromotionStatVO distributionPromotionStatVO = distributionBuyRecordService.stat(2,
                AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(distributionPromotionStatVO);
    }

}
