package com.mall4j.cloud.distribution.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.service.DistributionRecommendActivityService;
import com.mall4j.cloud.distribution.service.DistributionRecommendSpuService;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuListVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
@Slf4j
@RestController("appDistributionRecommendSpuController")
@RequestMapping("/distribution_recommend_spu")
@Api(tags = "微客-分销推广-推荐商品")
public class DistributionRecommendSpuController {

    @Autowired
    private DistributionRecommendSpuService distributionRecommendSpuService;

    @GetMapping("/page")
    @ApiOperation(value = "获取分销推广-推荐商品列表", notes = "分页获取分销推广-推荐商品列表")
    public ServerResponseEntity<PageVO<DistributionRecommendSpuVO>> page(@RequestParam(value = "storeId", required = false) Long storeId,
                                                                         @Valid PageDTO pageDTO,
                                                                         DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        log.info("分页获取分销推广-推荐商品列表1 传参storeId【{}】", storeId);
        if (storeId == null) {
            distributionRecommendSpuQueryDTO.setStoreId(AuthUserContext.get().getStoreId());
        } else {
            distributionRecommendSpuQueryDTO.setStoreId(storeId);
        }
        log.info("分页获取分销推广-推荐商品列表2 传参storeId【{}】", storeId);
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
    public ServerResponseEntity<PageVO<DistributionRecommendSpuListVO>> listForShoppingGuide(@RequestParam(value = "storeId", required = false) Long storeId, @Valid PageDTO pageDTO, @RequestBody DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        if (storeId == null) {
            // 获取当前门店
            distributionRecommendSpuQueryDTO.setStoreId(AuthUserContext.get().getStoreId());
        } else {
            distributionRecommendSpuQueryDTO.setStoreId(storeId);
        }
        // 获取当前时间
        distributionRecommendSpuQueryDTO.setCurrentTime(new Date());
        // 展示佣金比率
        distributionRecommendSpuQueryDTO.setShowCommissionRate(true);
        // 活动状态启动
        distributionRecommendSpuQueryDTO.setStatus(1);

        if(CollectionUtil.isNotEmpty(distributionRecommendSpuQueryDTO.getAttrValues()) && distributionRecommendSpuQueryDTO.getAttrValues().size()>5){
            throw new LuckException("尺码和颜色筛选条件最多支持5个");
        }

        return ServerResponseEntity.success(recommendActivityService.listGoodsSpuForShoppingGuideAndWeike(pageDTO, distributionRecommendSpuQueryDTO));
    }


}
