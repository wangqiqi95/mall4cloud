package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.product.vo.app.RecommendCateVO;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.product.dto.RecommendCateDTO;
import com.mall4j.cloud.product.dto.RecommendDTO;
import com.mall4j.cloud.product.model.RecommendCate;
import com.mall4j.cloud.product.service.BrandService;
import com.mall4j.cloud.product.service.RecommendCateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * 种草信息分类
 *
 * @author cg
 */
@RestController("adminRecommendCateController")
@RequestMapping("/mp/recommend/cate")
@Api(tags = "admin-种草分类")
public class RecommendCateController {

    @Autowired
    private RecommendCateService recommendCateService;

    @PostMapping("/save")
    @ApiOperation(value = "admin用户新增种草分类")
    public ServerResponseEntity<Void> save(@Valid @RequestBody RecommendCateDTO recommendDTO) {
        recommendCateService.add(recommendDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "admin用户修改种草分类")
    public ServerResponseEntity<Void> update(@Valid @RequestBody RecommendCateDTO recommendDTO) {
        recommendCateService.adminUpdate(recommendDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "admin获取种草分类列表")
    public ServerResponseEntity<PageVO<RecommendCateVO>> page(@Valid PageDTO pageDTO
            , @ApiParam(name = "name", value = "分类名称") String name) {
        PageVO<RecommendCateVO> result = recommendCateService.pageList(pageDTO, name);
        return ServerResponseEntity.success(result);
    }

    @PostMapping("/show")
    @ApiOperation(value = "admin设置是否显示")
    public ServerResponseEntity<Void> show(@ApiParam(name = "recommendCateId", value = "种草分类主键id", required = true) @RequestParam("recommendCateId") Long recommendCateId,
                                           @ApiParam(name = "type", value = "0-否 |1-是", required = true) @RequestParam("type") Integer type) {
        RecommendCate build = RecommendCate.builder().recommendCateId(recommendCateId).isShow(type).build();
        recommendCateService.updateById(build);
        return ServerResponseEntity.success();
    }

    @PostMapping("/default")
    @ApiOperation(value = "admin设置是否默认")
    public ServerResponseEntity<Void> setDefault(@ApiParam(name = "recommendCateId", value = "种草分类主键id", required = true) @RequestParam("recommendCateId") Long recommendCateId,
                                                 @ApiParam(name = "type", value = "0-否 |1-是", required = true) @RequestParam("type") Integer type) {
        recommendCateService.setDefault(recommendCateId, type);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "admin删除种草分类")
    public ServerResponseEntity<Void> delete(@ApiParam(name = "recommendCateId", value = "种草分类主键id", required = true) @RequestParam("recommendCateId") Long recommendCateId) {
        RecommendCate build = RecommendCate.builder().recommendCateId(recommendCateId).disable(1).build();
        recommendCateService.updateById(build);
        return ServerResponseEntity.success();
    }
}
