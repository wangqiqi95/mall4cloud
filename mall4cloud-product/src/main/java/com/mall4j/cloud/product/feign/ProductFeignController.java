package com.mall4j.cloud.product.feign;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.constant.ShopStatus;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.product.dto.ErpPriceDTO;
import com.mall4j.cloud.api.product.dto.ErpStockDTO;
import com.mall4j.cloud.api.product.dto.ErpSyncDTO;
import com.mall4j.cloud.api.product.dto.SpuErpSyncDTO;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.bo.EsProductBO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.SpuTagReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/11/27
 */
@RestController
public class ProductFeignController implements ProductFeignClient {

    @Autowired
    private SpuService spuService;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private SpuTagReferenceService spuTagReferenceService;

    @Override
    public ServerResponseEntity<EsProductBO> loadEsProductBO(Long spuId) {
        EsProductBO esProductBO = spuService.loadEsProductBO(spuId);
        List<Long> spuTagIds = spuTagReferenceService.tagListBySpuId(spuId);
        if (CollUtil.isNotEmpty(spuTagIds)) {
            esProductBO.setTagIds(spuTagIds);
        }
        // 获取店铺信息
        if (!Objects.equals(esProductBO.getShopId(), Constant.ZERO_LONG)) {
            ServerResponseEntity<EsShopDetailBO> shopDetailResponse = shopDetailFeignClient.getShopByShopId(esProductBO.getShopId());
            if (shopDetailResponse.isFail()) {
                throw new LuckException(shopDetailResponse.getMsg());
            }
            EsShopDetailBO shopDetail = shopDetailResponse.getData();
            if (esProductBO.getAppDisplay() && !Objects.equals(shopDetail.getShopStatus(), ShopStatus.OPEN.value())) {
                esProductBO.setAppDisplay(Boolean.FALSE);
            }
            esProductBO.setShopName(shopDetail.getShopName());
            esProductBO.setShopImg(shopDetail.getShopLogo());
            esProductBO.setShopType(shopDetail.getType());
        }
        if (Objects.isNull(esProductBO.getCommentNum())) {
            esProductBO.setCommentNum(0);
        }
        if (Objects.isNull(esProductBO.getSaleNum())) {
            esProductBO.setSaleNum(0);
        }
        if(CollUtil.isEmpty(esProductBO.getPartyCodes())) {
            esProductBO.setPartyCodes(null);
        }
        if(CollUtil.isEmpty(esProductBO.getModelIds())) {
            esProductBO.setModelIds(null);
        }
        return ServerResponseEntity.success(esProductBO);
    }

    @Override
    public ServerResponseEntity<List<Long>> getSpuIdsByShopCategoryIds(List<Long> shopCategoryIds) {
        return getSpuIdsBySpuUpdateDTO(shopCategoryIds, null, null, null);
    }

    @Override
    public ServerResponseEntity<List<Long>> getSpuIdsByCategoryIds(List<Long> categoryIds) {
        return getSpuIdsBySpuUpdateDTO(null, categoryIds, null, null);
    }

    @Override
    public ServerResponseEntity<List<Long>> getSpuIdsByBrandId(Long brandId) {
        return getSpuIdsBySpuUpdateDTO(null, null, brandId, null);
    }

    @Override
    public ServerResponseEntity<List<Long>> getSpuIdsByShopId(Long shopId) {
        return getSpuIdsBySpuUpdateDTO(null, null, null, shopId);
    }

    @Override
    public ServerResponseEntity<List<Long>> getSpuTagBySpuId(Long spuId) {
        return ServerResponseEntity.success(spuTagReferenceService.tagListBySpuId(spuId));
    }

    @Override
    public ServerResponseEntity<Void> handleStatusChange(Long spuId) {
        spuService.handleStatusChange(Collections.singletonList(spuId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> erpSync(ErpSyncDTO dto) {
        spuService.sync(dto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> priceSync(ErpPriceDTO erpPriceDto) {
        spuService.priceSyncNew(erpPriceDto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> stockSync(ErpStockDTO erpStockDTO) {
        spuService.stockSync(erpStockDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageVO<SpuCommonVO>> commonSearch(ProductSearchDTO productSearch) {
        PageVO<SpuCommonVO> page = spuService.commonPage(productSearch);
        return ServerResponseEntity.success(page);
    }

    @Override
    public ServerResponseEntity<PageVO<SpuCommonVO>> couponSearch(ProductSearchDTO productSearch) {
        PageVO<SpuCommonVO> page = spuService.couponSearch(productSearch);
        return ServerResponseEntity.success(page);
    }

    @Override
    public ServerResponseEntity<Void> iphSync(SpuDTO spuDTO) {
        spuService.iphSync(spuDTO);
        return ServerResponseEntity.success();
    }

//    @Override
//    public ServerResponseEntity<List<ProdEffectRespVO>> getSpuListByParam(ProdEffectDTO param, Set<Long> spuIds) {
//        return ServerResponseEntity.success(spuService.getSpuListByParam(param,spuIds));
//    }
//
//    @Override
//    public ServerResponseEntity<Long> getSpuListTotal(ProdEffectDTO param, Set<Long> spuIds) {
//        return ServerResponseEntity.success(spuService.getSpuListTotal(param,spuIds));
//    }

    /**
     * 获取spuId列表
     * @param shopCategoryIds 店铺分类id列表
     * @param categoryIds 平台分类Id列表
     * @param brandId 品牌id
     * @param shopId 店铺id
     * @return
     */
    public ServerResponseEntity<List<Long>> getSpuIdsBySpuUpdateDTO(List<Long> shopCategoryIds, List<Long> categoryIds, Long brandId, Long shopId) {
        List<Long> spuIds = spuService.getSpuIdsBySpuUpdateDTO(shopCategoryIds, categoryIds, brandId, shopId);
        return ServerResponseEntity.success(spuIds);
    }
}
