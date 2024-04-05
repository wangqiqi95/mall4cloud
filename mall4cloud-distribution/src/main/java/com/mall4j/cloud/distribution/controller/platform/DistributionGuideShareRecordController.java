package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionGuideShareRecordDTO;
import com.mall4j.cloud.distribution.dto.GuideShareRecordStatistics;
import com.mall4j.cloud.distribution.model.DistributionGuideShareRecord;
import com.mall4j.cloud.distribution.service.DistributionGuideShareRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * 分销数据-导购分销效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@RestController("platformDistributionGuideShareRecordController")
@RequestMapping("/p/distribution_guide_share_record")
@Api(tags = "平台端-分销数据-导购分销效果")
public class DistributionGuideShareRecordController {

    @Autowired
    private DistributionGuideShareRecordService distributionGuideShareRecordService;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @PostMapping("/page")
    @ApiOperation(value = "获取分销数据-导购分销效果列表", notes = "分页获取分销数据-导购分销效果列表")
    public ServerResponseEntity<PageVO<DistributionGuideShareRecord>> page(@Valid PageDTO pageDTO, @RequestBody DistributionGuideShareRecordDTO dto) {
        PageVO<DistributionGuideShareRecord> distributionGuideShareRecordPage = distributionGuideShareRecordService.page(pageDTO, dto);
        return ServerResponseEntity.success(distributionGuideShareRecordPage);
    }

    @ApiOperation("导购分享数据导出")
    @PostMapping("/exportGuideShareRecord")
    public ServerResponseEntity<Void> exportGuideShareRecord(@RequestBody DistributionGuideShareRecordDTO dto){
        distributionGuideShareRecordService.exportGuideShareRecord(dto);
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "获取分销数据-导购分销效果", notes = "根据id获取分销数据-导购分销效果")
    public ServerResponseEntity<DistributionGuideShareRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionGuideShareRecordService.getById(id));
    }

    @ApiOperation("导购分享效果-导购统计")
    @PostMapping("/getGuideShareRecordStatistics")
    public ServerResponseEntity<GuideShareRecordStatistics> getGuideShareRecordStatistics(@RequestBody DistributionGuideShareRecordDTO dto) {
        GuideShareRecordStatistics shareRecordStatistics = new GuideShareRecordStatistics();
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        staffQueryDTO.setStoreId(dto.getStoreId());
        if(Objects.isNull(dto.getStatus())){
            staffQueryDTO.setStatus(0);
        }
        ServerResponseEntity<Integer> entity = staffFeignClient.countByStaffNum(staffQueryDTO);
        shareRecordStatistics.setShareStaffNum(distributionGuideShareRecordService.countStaffNum(dto));
        if (entity.isSuccess() && null != entity.getData()){
            shareRecordStatistics.setTotalStaffNum(entity.getData());
            shareRecordStatistics.setNotShareStaffNum(Optional.ofNullable(shareRecordStatistics.getTotalStaffNum()).orElse(0) - Optional.ofNullable(shareRecordStatistics.getShareStaffNum()).orElse(0));
        }
        return ServerResponseEntity.success(shareRecordStatistics);
    }

//    @PostMapping
//    @ApiOperation(value = "保存分销数据-导购分销效果", notes = "保存分销数据-导购分销效果")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionGuideShareRecordDTO distributionGuideShareRecordDTO) {
//        DistributionGuideShareRecord distributionGuideShareRecord = mapperFacade.map(distributionGuideShareRecordDTO, DistributionGuideShareRecord.class);
//        distributionGuideShareRecord.setId(null);
//        distributionGuideShareRecordService.save(distributionGuideShareRecord);
//        return ServerResponseEntity.success();
//    }

//    @PutMapping
//    @ApiOperation(value = "更新分销数据-导购分销效果", notes = "更新分销数据-导购分销效果")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionGuideShareRecordDTO distributionGuideShareRecordDTO) {
//        DistributionGuideShareRecord distributionGuideShareRecord = mapperFacade.map(distributionGuideShareRecordDTO, DistributionGuideShareRecord.class);
//        distributionGuideShareRecordService.update(distributionGuideShareRecord);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除分销数据-导购分销效果", notes = "根据分销数据-导购分销效果id删除分销数据-导购分销效果")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        distributionGuideShareRecordService.deleteById(id);
//        return ServerResponseEntity.success();
//    }
}
