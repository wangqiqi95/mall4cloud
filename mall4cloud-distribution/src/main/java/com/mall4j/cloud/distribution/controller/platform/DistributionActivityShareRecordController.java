package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionActivityShareRecord;
import com.mall4j.cloud.distribution.service.DistributionActivityShareRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销推广-活动分享效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@RestController("platformDistributionActivityShareRecordController")
@RequestMapping("/p/distribution_activity_share_record")
@Api(tags = "平台端-分销推广-活动分享效果")
public class DistributionActivityShareRecordController {

    @Autowired
    private DistributionActivityShareRecordService distributionActivityShareRecordService;


    @PostMapping("/page")
    @ApiOperation(value = "获取分销推广-活动分享效果列表", notes = "分页获取分销推广-活动分享效果列表")
    public ServerResponseEntity<PageVO<DistributionActivityShareRecord>> page(@Valid PageDTO pageDTO, @RequestBody DistributionActivityShareRecordDTO dto) {
        PageVO<DistributionActivityShareRecord> distributionActivityShareRecordPage = distributionActivityShareRecordService.page(pageDTO, dto);
        return ServerResponseEntity.success(distributionActivityShareRecordPage);
    }

    @GetMapping
    @ApiOperation(value = "获取分销推广-活动分享效果", notes = "根据id获取分销推广-活动分享效果")
    public ServerResponseEntity<DistributionActivityShareRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionActivityShareRecordService.getById(id));
    }


    @ApiOperation("导出活动分享效果列表")
    @PostMapping("/exportActivityShareRecord")
    public ServerResponseEntity<Void> exportActivityShareRecord(@RequestBody DistributionActivityShareRecordDTO dto){
        distributionActivityShareRecordService.exportActivityShareRecord(dto);
        return ServerResponseEntity.success();
    }

    @ApiOperation("导出活动分享效果导购明细")       
    @PostMapping("/exportActivityShareDetails")
    public ServerResponseEntity<Void> exportActivityShareDetails(@RequestBody DistributionActivityShareRecordDTO dto){
        distributionActivityShareRecordService.exportActivityShareDetails(dto);
        return ServerResponseEntity.success();
    }

//    @PostMapping
//    @ApiOperation(value = "保存分销推广-活动分享效果", notes = "保存分销推广-活动分享效果")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionActivityShareRecordDTO distributionActivityShareRecordDTO) {
//        DistributionActivityShareRecord distributionActivityShareRecord = mapperFacade.map(distributionActivityShareRecordDTO, DistributionActivityShareRecord.class);
//        distributionActivityShareRecord.setId(null);
//        distributionActivityShareRecordService.save(distributionActivityShareRecord);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新分销推广-活动分享效果", notes = "更新分销推广-活动分享效果")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionActivityShareRecordDTO distributionActivityShareRecordDTO) {
//        DistributionActivityShareRecord distributionActivityShareRecord = mapperFacade.map(distributionActivityShareRecordDTO, DistributionActivityShareRecord.class);
//        distributionActivityShareRecordService.update(distributionActivityShareRecord);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除分销推广-活动分享效果", notes = "根据分销推广-活动分享效果id删除分销推广-活动分享效果")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        distributionActivityShareRecordService.deleteById(id);
//        return ServerResponseEntity.success();
//    }
}
