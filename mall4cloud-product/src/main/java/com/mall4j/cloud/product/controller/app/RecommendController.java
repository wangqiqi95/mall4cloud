package com.mall4j.cloud.product.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.app.RecommendDetailVO;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.RecommendDTO;
import com.mall4j.cloud.product.model.RecommendBannerPic;
import com.mall4j.cloud.product.service.RecommendBannerPicService;
import com.mall4j.cloud.product.service.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 种草信息
 *
 * @author cg
 */
@RestController("appRecommendController")
@RequestMapping("/recommend")
@Api(tags = "app-种草信息")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendBannerPicService bannerPicService;


    @PostMapping("/save")
    @ApiOperation(value = "app用户发布种草")
    public ServerResponseEntity<Void> save(@Valid @RequestBody RecommendDTO recommendDTO) {
        recommendService.add(recommendDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/banner/delete")
    @ApiOperation(value = "删除banner图")
    public ServerResponseEntity<Void> saveBannerPic(@ApiParam(name = "id", value = "图片主键id", required = true) @RequestParam("id") Long id) {
        bannerPicService.update(new LambdaUpdateWrapper<RecommendBannerPic>().eq(RecommendBannerPic::getId, id).set(RecommendBannerPic::getDisable, 1));
        return ServerResponseEntity.success();
    }

    @GetMapping("/banner/list")
    @ApiOperation(value = "banner列表")
    public ServerResponseEntity<List<RecommendBannerPic>> bannerList() {
        List<RecommendBannerPic> list = bannerPicService.list(new LambdaQueryWrapper<RecommendBannerPic>().eq(RecommendBannerPic::getDisable, 0));
        return ServerResponseEntity.success(list);
    }

    @PostMapping("/praise")
    @ApiOperation(value = "app用户点赞")
    public ServerResponseEntity<Void> praise(@ApiParam(name = "recommendId", value = "种草主键id", required = true) @RequestParam("recommendId") Long recommendId,
                                             @ApiParam(name = "type", value = "1-点赞 |0-取消点赞", required = true) @RequestParam("type") Integer type) {
        recommendService.praise(recommendId, type);
        return ServerResponseEntity.success();
    }

    @PostMapping("/collect")
    @ApiOperation(value = "app用户收藏")
    public ServerResponseEntity<Void> collect(@ApiParam(name = "recommendId", value = "种草主键id", required = true) @RequestParam("recommendId") Long recommendId,
                                              @ApiParam(name = "type", value = "1-收藏 |0-取消收藏", required = true) @RequestParam("type") Integer type) {
        recommendService.collect(recommendId, type);
        return ServerResponseEntity.success();
    }

    @PostMapping("/share")
    @ApiOperation(value = "app用户分享")
    public ServerResponseEntity<Void> share(@ApiParam(name = "recommendId", value = "种草主键id", required = true) @RequestParam("recommendId") Long recommendId) {
        recommendService.share(recommendId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/read")
    @ApiOperation(value = "app浏览种草")
    public ServerResponseEntity<Void> read(@ApiParam(name = "recommendId", value = "种草主键id", required = true) @RequestParam("recommendId") Long recommendId) {
        recommendService.read(recommendId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "app获取种草列表")
    @ApiImplicitParam(name = "type", value = "tab类型：1-最新 | 2-最热", required = true)
    public ServerResponseEntity<PageVO<RecommendVO>> page(@Valid PageDTO pageDTO, @RequestParam(value = "type") Integer type) {
        PageVO<RecommendVO> recommendList = recommendService.pageList(pageDTO, type, null);
        return ServerResponseEntity.success(recommendList);
    }

    @GetMapping("/my/page")
    @ApiOperation(value = "app获取我发布的种草列表")
    public ServerResponseEntity<PageVO<RecommendVO>> myPage(@Valid PageDTO pageDTO) {
        PageVO<RecommendVO> recommendList = recommendService.myPage(pageDTO);
        return ServerResponseEntity.success(recommendList);
    }

    @GetMapping("/collect/page")
    @ApiOperation(value = "app获取我收藏的种草列表")
    public ServerResponseEntity<PageVO<RecommendVO>> collectPage(@Valid PageDTO pageDTO) {
        PageVO<RecommendVO> recommendList = recommendService.collectPage(pageDTO);
        return ServerResponseEntity.success(recommendList);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "app获取种草详情")
    @ApiImplicitParam(name = "recommendId", value = "recommendId", required = true)
    public ServerResponseEntity<RecommendDetailVO> detail(@RequestParam(value = "recommendId") Long recommendId) {
        RecommendDetailVO recommendDetailVO = recommendService.detail(recommendId);
        return ServerResponseEntity.success(recommendDetailVO);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "app删除种草")
    public ServerResponseEntity<Void> delete(@ApiParam(name = "recommendId", value = "种草id", required = true)
                                             @RequestParam("recommendId") Long recommendId) {
        recommendService.appDelete(recommendId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "app修改种草")
    public ServerResponseEntity<Void> update(@Valid @RequestBody RecommendDTO recommendDTO) {
        recommendService.appUpdate(recommendDTO);
        return ServerResponseEntity.success();
    }
}
