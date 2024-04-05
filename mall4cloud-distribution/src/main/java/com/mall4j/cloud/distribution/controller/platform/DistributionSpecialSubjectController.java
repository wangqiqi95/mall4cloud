package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionSpecialSubjectDTO;
import com.mall4j.cloud.distribution.service.DistributionSpecialSubjectService;
import com.mall4j.cloud.distribution.vo.DistributionSpecialSubjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销推广-分销专题
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
@RestController("platformDistributionSpecialSubjectController")
@RequestMapping("/p/distribution_special_subject")
@Api(tags = "平台端-分销推广-分销专题")
public class DistributionSpecialSubjectController {

    @Autowired
    private DistributionSpecialSubjectService distributionSpecialSubjectService;


    @PostMapping("/page")
    @ApiOperation(value = "获取分销推广-分销专题列表", notes = "分页获取分销推广-分销专题列表")
    public ServerResponseEntity<PageVO<DistributionSpecialSubjectVO>> page(@Valid PageDTO pageDTO, @RequestBody DistributionSpecialSubjectDTO dto) {
        PageVO<DistributionSpecialSubjectVO> distributionSpecialSubjectPage = distributionSpecialSubjectService.page(pageDTO, dto);
        return ServerResponseEntity.success(distributionSpecialSubjectPage);
    }

    @GetMapping
    @ApiOperation(value = "获取分销推广-分销专题", notes = "根据id获取分销推广-分销专题")
    public ServerResponseEntity<DistributionSpecialSubjectDTO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionSpecialSubjectService.getSpecialSubjectById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-分销专题", notes = "保存分销推广-分销专题")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionSpecialSubjectDTO dto) {
        dto.setId(null);
        distributionSpecialSubjectService.save(dto);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-分销专题", notes = "更新分销推广-分销专题")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionSpecialSubjectDTO dto) {
        distributionSpecialSubjectService.update(dto);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-分销专题", notes = "根据分销推广-分销专题id删除分销推广-分销专题")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionSpecialSubjectService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/updateStatusBatch")
    @ApiOperation(value = "更新专题状态", notes = "更新分销推广-分销专题状态")
    public ServerResponseEntity<Void> updateStatusBatch(@RequestBody DistributionSpecialSubjectDTO dto) {
        distributionSpecialSubjectService.updateStatusBatch(dto.getIds(), dto.getStatus());
        return ServerResponseEntity.success();
    }

    @PostMapping("/specialSubjectTop")
    @ApiOperation(value = "分销专题置顶", notes = "分销专题置顶")
    public ServerResponseEntity<Void> specialSubjectTop(@RequestBody DistributionSpecialSubjectDTO dto) {
        distributionSpecialSubjectService.specialSubjectTop(dto.getId(), dto.getTop());
        return ServerResponseEntity.success();
    }
}
