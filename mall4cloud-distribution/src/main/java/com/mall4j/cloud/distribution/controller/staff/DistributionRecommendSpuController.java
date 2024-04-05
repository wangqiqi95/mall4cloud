package com.mall4j.cloud.distribution.controller.staff;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionRecommendActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.service.DistributionRecommendActivityService;
import com.mall4j.cloud.distribution.service.DistributionRecommendSpuService;
import com.mall4j.cloud.distribution.vo.DistributionRecommendActivityVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuListVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
@RestController("staffDistributionRecommendSpuController")
@RequestMapping("/s/distribution_recommend_spu")
@Api(tags = "导购小程序-分销推广-推荐商品")
public class DistributionRecommendSpuController {

    @Autowired
    private DistributionRecommendSpuService distributionRecommendSpuService;

    @GetMapping("/page")
    @ApiOperation(value = "获取分销推广-推荐商品列表", notes = "分页获取分销推广-推荐商品列表")
    public ServerResponseEntity<PageVO<DistributionRecommendSpuVO>> page(@Valid PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        distributionRecommendSpuQueryDTO.setStoreId(AuthUserContext.get().getStoreId());
        distributionRecommendSpuQueryDTO.setCurrentTime(new Date());
        distributionRecommendSpuQueryDTO.setShowCommissionRate(true);
        distributionRecommendSpuQueryDTO.setStatus(1);
        PageVO<DistributionRecommendSpuVO> distributionRecommendSpuPage = distributionRecommendSpuService.page(pageDTO, distributionRecommendSpuQueryDTO);
        return ServerResponseEntity.success(distributionRecommendSpuPage);
    }

    private DistributionRecommendActivityService recommendActivityService;

    @Autowired
    public void setRecommendActivityService(DistributionRecommendActivityService recommendActivityService) {
        this.recommendActivityService = recommendActivityService;
    }

    @PostMapping("/page/v2")
    @ApiOperation(value = "获取分销推广-推荐商品列表-第二版", notes = "分页获取分销推广-推荐商品列表-第二版")
    public ServerResponseEntity<PageVO<DistributionRecommendSpuListVO>> listForShoppingGuide(@Valid PageDTO pageDTO, @RequestBody DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        // 获取当前门店
//        distributionRecommendSpuQueryDTO.setStoreId(1l);
        distributionRecommendSpuQueryDTO.setStoreId(AuthUserContext.get().getStoreId());
        // 获取当前时间
        distributionRecommendSpuQueryDTO.setCurrentTime(new Date());
        // 展示佣金比率
        distributionRecommendSpuQueryDTO.setShowCommissionRate(true);
        // 活动状态启动
        distributionRecommendSpuQueryDTO.setStatus(1);
        return ServerResponseEntity.success(recommendActivityService.listGoodsSpuForShoppingGuideAndWeike(pageDTO, distributionRecommendSpuQueryDTO));
    }

    @PostMapping("/staffRecommendActivity")
    @ApiOperation(value = "查询当前导购所在门店的主推商品活动", notes = "查询当前导购所在门店的主推商品活动")
    public ServerResponseEntity<PageVO<DistributionRecommendActivityVO>> staffRecommendActivity() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(1);
        pageDTO.setPageSize(500);

        DistributionRecommendActivityQueryDTO distributionRecommendActivityQueryDTO = new DistributionRecommendActivityQueryDTO();
        List<Long> stores = new ArrayList<>();
        stores.add(AuthUserContext.get().getStoreId());

        distributionRecommendActivityQueryDTO.setShopIds(Collections.emptyList());
        distributionRecommendActivityQueryDTO.setActivityStatus(2);
        distributionRecommendActivityQueryDTO.setShopIds(stores);
        return ServerResponseEntity.success(recommendActivityService.listActivity(pageDTO, distributionRecommendActivityQueryDTO));
    }


}
