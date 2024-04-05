package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionMomentsStore;
import com.mall4j.cloud.distribution.service.DistributionMomentsStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 分销推广-朋友圈门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("platformDistributionMomentsStoreController")
@RequestMapping("/p/distribution_moments_store")
@Api(tags = "平台端-分销推广-朋友圈门店")
public class DistributionMomentsStoreController {

    @Autowired
    private DistributionMomentsStoreService distributionMomentsStoreService;


    @GetMapping("/page")
    @ApiOperation(value = "获取分销推广-朋友圈门店列表", notes = "分页获取分销推广-朋友圈门店列表")
    public ServerResponseEntity<PageVO<DistributionMomentsStore>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionMomentsStore> distributionMomentsStorePage = distributionMomentsStoreService.page(pageDTO);
        return ServerResponseEntity.success(distributionMomentsStorePage);
    }

    @GetMapping("/listMomentsStoreByMomentsId")
    @ApiOperation(value = "获取分销推广-朋友圈门店", notes = "根据朋友圈id获取分销推广-朋友圈门店")
    public ServerResponseEntity<List<DistributionMomentsStore>> listMomentsStoreByMomentsId(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionMomentsStoreService.listByMomentsId(id));
    }
}
