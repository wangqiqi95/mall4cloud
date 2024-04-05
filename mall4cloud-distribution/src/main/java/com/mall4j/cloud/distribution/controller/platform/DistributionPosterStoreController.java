package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionPosterStore;
import com.mall4j.cloud.distribution.service.DistributionPosterStoreService;
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
 * 分销推广-海报门店
 *
 * @author ZengFanChang
 * @date 2022-01-03 20:00:28
 */
@RestController("platformDistributionPosterStoreController")
@RequestMapping("/p/distribution_poster_store")
@Api(tags = "分销推广-海报门店")
public class DistributionPosterStoreController {

    @Autowired
    private DistributionPosterStoreService distributionPosterStoreService;


    @GetMapping("/page")
    @ApiOperation(value = "获取分销推广-海报门店列表", notes = "分页获取分销推广-海报门店列表")
    public ServerResponseEntity<PageVO<DistributionPosterStore>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionPosterStore> distributionPosterStorePage = distributionPosterStoreService.page(pageDTO);
        return ServerResponseEntity.success(distributionPosterStorePage);
    }

    @GetMapping("/listPosterStoreByPosterId")
    @ApiOperation(value = "获取分销推广-海报门店", notes = "根据海报id获取分销推广-海报门店")
    public ServerResponseEntity<List<DistributionPosterStore>> listPosterStoreByPosterId(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionPosterStoreService.listByPosterId(id));
    }
}
