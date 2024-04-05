package com.mall4j.cloud.product.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.app.RecommendDetailVO;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.RecommendAdminDTO;
import com.mall4j.cloud.product.dto.RecommendAdminPageParamsDTO;
import com.mall4j.cloud.product.model.RecommendBannerPic;
import com.mall4j.cloud.product.service.RecommendBannerPicService;
import com.mall4j.cloud.product.service.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 种草信息
 *
 * @author cg
 */
@RestController("adminRecommendController")
@RequestMapping("/mp/recommend")
@Api(tags = "admin-种草")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendBannerPicService bannerPicService;

    @PostMapping("/save")
    @ApiOperation(value = "admin用户发布种草")
    public ServerResponseEntity<Void> save(@Valid @RequestBody RecommendAdminDTO recommendDTO) {
        recommendService.add(recommendDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/banner/save")
    @ApiOperation(value = "保存banner图")
    public ServerResponseEntity<Void> saveBannerPic(@RequestBody List<RecommendBannerPic> params) {
        if (CollectionUtils.isEmpty(params)) {
            throw new LuckException("参数错误");
        }
        // 清理原有的banner图
        bannerPicService.update(new LambdaUpdateWrapper<RecommendBannerPic>().set(RecommendBannerPic::getDisable, 1));
        List<RecommendBannerPic> list = new ArrayList<>();
        params.forEach(u -> {
            RecommendBannerPic bannerPic = new RecommendBannerPic();
            bannerPic.setUrl(u.getUrl());
            bannerPic.setDisable(0);
            list.add(bannerPic);
        });
        bannerPicService.saveBatch(list);
        return ServerResponseEntity.success();
    }

    @GetMapping("/banner/list")
    @ApiOperation(value = "banner列表")
    public ServerResponseEntity<List<RecommendBannerPic>> bannerList() {
        List<RecommendBannerPic> list = bannerPicService.list(new LambdaQueryWrapper<RecommendBannerPic>().eq(RecommendBannerPic::getDisable, 0));
        return ServerResponseEntity.success(list);
    }

    @PostMapping("/update")
    @ApiOperation(value = "admin用户修改种草")
    public ServerResponseEntity<Void> update(@Valid @RequestBody RecommendAdminDTO recommendDTO) {
        recommendService.adminUpdate(recommendDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "admin用户获取种草列表")
    public ServerResponseEntity<PageVO<RecommendVO>> page(@Valid PageDTO pageDTO, RecommendAdminPageParamsDTO params) {
        PageVO<RecommendVO> recommendList = recommendService.adminPageList(pageDTO, params);
        return ServerResponseEntity.success(recommendList);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "admin获取种草详情")
    @ApiImplicitParam(name = "recommendId", value = "recommendId", required = true)
    public ServerResponseEntity<RecommendDetailVO> detail(@RequestParam(value = "recommendId") Long recommendId) {
        RecommendDetailVO recommendDetailVO = recommendService.detail(recommendId);
        return ServerResponseEntity.success(recommendDetailVO);
    }

    @PostMapping("/examine")
    @ApiOperation(value = "admin审核种草")
    public ServerResponseEntity<Void> examine(@ApiParam(name = "recommendId", value = "种草id", required = true) @RequestParam("recommendId") Long recommendId,
                                              @ApiParam(name = "status", value = "2-审核通过 | 3-已驳回", required = true) @RequestParam("status") Integer status) {
        recommendService.examine(recommendId, status);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "admin删除种草")
    public ServerResponseEntity<Void> delete(@ApiParam(name = "recommendId", value = "种草id", required = true) @RequestParam("recommendId") Long recommendId) {
        recommendService.adminDelete(recommendId);
        return ServerResponseEntity.success();
    }
}
