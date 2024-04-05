package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionSubjectStore;
import com.mall4j.cloud.distribution.service.DistributionSubjectStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("appDistributionSubjectStoreController")
@RequestMapping("/distribution_subject_store")
@Api(tags = "分销推广-专题门店")
public class DistributionSubjectStoreController {

    @Autowired
    private DistributionSubjectStoreService distributionSubjectStoreService;

    @GetMapping("/page")
    @ApiOperation(value = "获取分销推广-专题门店列表", notes = "分页获取分销推广-专题门店列表")
    public ServerResponseEntity<PageVO<DistributionSubjectStore>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionSubjectStore> distributionSubjectStorePage = distributionSubjectStoreService.page(pageDTO);
        return ServerResponseEntity.success(distributionSubjectStorePage);
    }

    @GetMapping
    @ApiOperation(value = "获取分销推广-专题门店", notes = "根据id获取分销推广-专题门店")
    public ServerResponseEntity<DistributionSubjectStore> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionSubjectStoreService.getById(id));
    }
}
