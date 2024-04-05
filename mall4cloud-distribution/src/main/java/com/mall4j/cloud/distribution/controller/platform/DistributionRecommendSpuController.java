package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuUpdateDTO;
import com.mall4j.cloud.distribution.service.DistributionRecommendSpuService;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
@RestController("platformDistributionRecommendSpuController")
@RequestMapping("/p/distribution_recommend_spu")
@Api(tags = "平台端-分销推广-推荐商品")
public class DistributionRecommendSpuController {

    @Autowired
    private DistributionRecommendSpuService distributionRecommendSpuService;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-推荐商品列表", notes = "分页获取分销推广-推荐商品列表")
	public ServerResponseEntity<PageVO<DistributionRecommendSpuVO>> page(@Valid PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
		PageVO<DistributionRecommendSpuVO> distributionRecommendSpuPage = distributionRecommendSpuService.page(pageDTO, distributionRecommendSpuQueryDTO);
		return ServerResponseEntity.success(distributionRecommendSpuPage);
	}

	@GetMapping("/getById")
    @ApiOperation(value = "获取分销推广-推荐商品", notes = "根据id获取分销推广-推荐商品")
    public ServerResponseEntity<DistributionRecommendSpuVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionRecommendSpuService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存分销推广-推荐商品", notes = "保存分销推广-推荐商品")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionRecommendSpuDTO distributionRecommendSpuDTO) {
        distributionRecommendSpuService.save(distributionRecommendSpuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新分销推广-推荐商品", notes = "更新分销推广-推荐商品")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionRecommendSpuDTO distributionRecommendSpuDTO) {
        distributionRecommendSpuService.update(distributionRecommendSpuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新分销推广-推荐商品-状态", notes = "更新分销推广-推荐商品-状态")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody DistributionRecommendSpuUpdateDTO distributionRecommendSpuUpdateDTO) {
        distributionRecommendSpuService.updateStatus(distributionRecommendSpuUpdateDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "导出", notes = "导出")
    @GetMapping("/export")
    public ServerResponseEntity<Void> export(HttpServletResponse response, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        distributionRecommendSpuService.export(response, distributionRecommendSpuQueryDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "已选择的商品id结合", notes = "已选择的商品id结合")
    @GetMapping("/selectSpuIdList")
    public ServerResponseEntity<List<Long>> selectSpuIdList() {
        DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO = new DistributionRecommendSpuQueryDTO();
	    return ServerResponseEntity.success(distributionRecommendSpuService.listSpuIdListByParam(distributionRecommendSpuQueryDTO));
    }
}
