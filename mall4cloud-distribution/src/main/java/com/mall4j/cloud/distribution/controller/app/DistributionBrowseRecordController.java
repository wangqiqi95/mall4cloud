package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionBrowseRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionBrowseRecord;
import com.mall4j.cloud.distribution.service.DistributionBrowseRecordService;
import com.mall4j.cloud.distribution.vo.DistributionBrowseRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分享推广-浏览记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@RestController("appDistributionBrowseRecordController")
@RequestMapping("/distribution_browse_record")
@Api(tags = "微客小程序-分享推广-浏览记录")
public class DistributionBrowseRecordController {

    @Autowired
    private DistributionBrowseRecordService distributionBrowseRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分享推广-浏览记录列表", notes = "分页获取分享推广-浏览记录列表")
	public ServerResponseEntity<PageVO<DistributionBrowseRecordVO>> page(@Valid PageDTO pageDTO, DistributionBrowseRecordDTO dto) {
        dto.setShareId(AuthUserContext.get().getUserId());
        dto.setShareType(2);
		PageVO<DistributionBrowseRecordVO> distributionBrowseRecordPage = distributionBrowseRecordService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionBrowseRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分享推广-浏览记录", notes = "根据id获取分享推广-浏览记录")
    public ServerResponseEntity<DistributionBrowseRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionBrowseRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分享推广-浏览记录", notes = "保存分享推广-浏览记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionBrowseRecordDTO distributionBrowseRecordDTO) {
        DistributionBrowseRecord distributionBrowseRecord = mapperFacade.map(distributionBrowseRecordDTO, DistributionBrowseRecord.class);
        distributionBrowseRecordService.save(distributionBrowseRecord);
        return ServerResponseEntity.success();
    }

    @GetMapping("/stat")
    @ApiOperation(value = "分享推广-浏览记录统计", notes = "分享推广-浏览记录统计")
    public ServerResponseEntity<DistributionPromotionStatVO> stat() {
        DistributionPromotionStatVO distributionPromotionStatVO = distributionBrowseRecordService.stat(2,
                AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(distributionPromotionStatVO);
    }


}
