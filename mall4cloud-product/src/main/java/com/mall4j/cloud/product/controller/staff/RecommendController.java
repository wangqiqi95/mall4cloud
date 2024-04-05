package com.mall4j.cloud.product.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.app.RecommendDetailVO;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 种草信息
 *
 * @author cg
 */
@RestController("staffRecommendController")
@RequestMapping("/s/recommend")
@Api(tags = "导购小程序-种草信息")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/page")
    @ApiOperation(value = "获取种草列表")
    public ServerResponseEntity<PageVO<RecommendVO>> page(String title, @Valid PageDTO pageDTO) {
        PageVO<RecommendVO> recommendList = recommendService.pageList(pageDTO, 1, title);
        return ServerResponseEntity.success(recommendList);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取种草详情")
    @ApiImplicitParam(name = "recommendId", value = "recommendId", required = true)
    public ServerResponseEntity<RecommendDetailVO> detail(@RequestParam(value = "recommendId") Long recommendId) {
        RecommendDetailVO recommendDetailVO = recommendService.detail(recommendId);
        return ServerResponseEntity.success(recommendDetailVO);
    }

}
