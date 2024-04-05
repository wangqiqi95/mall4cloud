package com.mall4j.cloud.product.feign;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.dto.*;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.api.product.vo.StdPushSpuVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuAttrValueService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.SpuSkuPricingPriceService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class SpuFeignController implements SpuFeignClient {

    @Autowired
    private SpuService spuService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SpuSkuPricingPriceService spuSkuPricingPriceService;

    @Autowired
    private SpuAttrValueService spuAttrValueService;

    @Override
    public ServerResponseEntity<SpuVO> getById(Long spuId) {
        SpuVO spuVO = spuService.getBySpuId(spuId);
        ProductLangUtil.spu(spuVO);
        SpuVO spu = new SpuVO();
        spu.setSpuCode(spuVO.getSpuCode());
        spu.setSpuId(spuVO.getSpuId());
        spu.setName(spuVO.getName());
        spu.setSellingPoint(spuVO.getSellingPoint());
        spu.setMainImgUrl(spuVO.getMainImgUrl());
        return ServerResponseEntity.success(spu);
    }

    @Override
    public ServerResponseEntity<SpuVO> insiderGetById(Long spuId) {
        return ServerResponseEntity.success(spuService.getBySpuId(spuId));
    }

    @Override
    public ServerResponseEntity<SpuVO> getDetailById(Long spuId) {
        SpuVO spuVO = spuService.getBySpuId(spuId);
        ProductLangUtil.spu(spuVO);
        return ServerResponseEntity.success(spuVO);
    }

    @Override
    public ServerResponseEntity<SpuAndSkuVO> getSpuAndSkuById(Long spuId, Long skuId, Long storeId) {
        SpuVO spu = spuService.getBySpuIdAndStoreId(spuId,storeId);
        SkuVO sku = skuService.getSkuBySkuIdAndStoreId(spuId,skuId,storeId);

        // 当商品状态不正常时，不能添加到购物车
        boolean spuIsNotExist = Objects.isNull(spu) || Objects.isNull(sku) || !Objects.equals(spu.getStatus(), StatusEnum.ENABLE.value()) || !Objects.equals(sku.getStatus(), StatusEnum.ENABLE.value()) || !Objects.equals(sku.getSpuId(), spu.getSpuId());
        if (spuIsNotExist) {
            // 当返回商品不存在时，前端应该将商品从购物车界面移除
            return ServerResponseEntity.fail(ResponseEnum.SPU_NOT_EXIST);
        }

        SpuAndSkuVO spuAndSku = new SpuAndSkuVO();
        spuAndSku.setSku(sku);
        spuAndSku.setSpu(spu);
        return ServerResponseEntity.success(spuAndSku);
    }

    @Override
    public ServerResponseEntity<SpuAndSkuVO> getEcSpuAndSkuById(Long spuId, Long skuId, Long storeId) {
        SpuVO spu = spuService.getBySpuIdAndStoreId(spuId,storeId);
        SkuVO sku = skuService.getSkuBySkuIdAndStoreId(spuId,skuId,storeId);

        SpuAndSkuVO spuAndSku = new SpuAndSkuVO();
        spuAndSku.setSku(sku);
        spuAndSku.setSpu(spu);
        return ServerResponseEntity.success(spuAndSku);
    }

    @Override
    public ServerResponseEntity<List<SpuVO>> listCanSeckillProd(Long spuId,Long shopId) {
        return ServerResponseEntity.success(spuService.listCanSeckillProd(spuId,shopId));
    }

    @Override
    public ServerResponseEntity<SpuActivityAppVO> spuActivityBySpuId(Long shopId, Long spuId) {
        return ServerResponseEntity.success(spuService.spuActivityBySpuId(shopId, spuId));
    }

    @Override
    public ServerResponseEntity<Void> offlineSpuByShopId(Long shopId) {
        spuService.offlineSpuByShopId(shopId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<SpuVO> getSpuAndSkuBySpuId(Long spuId) {
        SpuVO spuVO = spuService.getBySpuId(spuId);
        ProductLangUtil.spu(spuVO);
        spuVO.setSkus(skuService.listSkuAllInfoBySpuId(spuId, false, Constant.MAIN_SHOP));
        return ServerResponseEntity.success(spuVO);
    }

    @Override
    public ServerResponseEntity<Void> changeSpuType(SpuDTO spuDTO) {
        Spu spu = mapperFacade.map(spuDTO, Spu.class);
        spuService.updateSpu(spu);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Integer> countByTransportId(Long transportId) {

        return ServerResponseEntity.success(spuService.countByTransportId(transportId));
    }

    @Override
    public void removeSpuActivityCache(Long shopId, List<Long> spuIds) {
        if (Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            shopId = null;
        }
        if (CollUtil.isEmpty(spuIds) && !Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            spuIds = spuService.listSpuIdsByShopId(shopId);
        }
        spuService.batchRemoveSpuActivityCache(spuIds);
    }

    @Override
    public void changeToNormalSpu(List<Long> spuIds) {
        spuService.changeToNormalSpu(spuIds);
    }

    @Override
    public ServerResponseEntity<Integer> countByUserId(Long userId) {
        return ServerResponseEntity.success(spuService.countByUserId(userId));
    }

    @Override
    public ServerResponseEntity<List<SpuVO>> listSpuNameBySpuIds(List<Long> spuIds) {
        return ServerResponseEntity.success(spuService.listSpuNameBySpuIds(spuIds));
    }

    @Override
    public ServerResponseEntity<List<SpuVO>> listSpuNameBypBySpuIds(List<Long> spuIds) {
        return ServerResponseEntity.success(spuService.listSpuNameBySpuIds(spuIds));
    }

    @Override
    public ServerResponseEntity<List<SpuVO>> listSpuBySpuIds(List<Long> spuIds) {
        return  ServerResponseEntity.success(spuService.listSpuBySpuIds(spuIds));
    }

    @Override
    public ServerResponseEntity<PageVO<SpuVO>> pageList(SpuListDTO param) {
        return ServerResponseEntity.success(spuService.pageList(param));
    }

    @Override
    public ServerResponseEntity<List<SpuCodeVo>> listSpuBySpuCodes(List<String> spuCodes) {
        return ServerResponseEntity.success(spuService.listSpuBySpuCodes(spuCodes));
    }

    @Override
    public ServerResponseEntity<Void> updateSpuUpdateTimeByShopIds(List<Long> shopIds) {
        spuService.updateSpuUpdateTime(null, null, shopIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> offlineSpuActivityByShopIds(List<Long> shopIds) {
        spuService.offlineSpuByShopIds(1, shopIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<SpuSimpleBO>> listSimple(SpuSimpleBO spuSimpleBO) {
        List<SpuSimpleBO> spus = spuService.listSimple(spuSimpleBO);
        return ServerResponseEntity.success(spus);
    }

    @Override
    public ServerResponseEntity<List<SkuVO>> listSkuPriceByIds(List<Long> skuIds) {
        return ServerResponseEntity.success(skuService.listSkuPriceByIds(skuIds));
    }

    @Override
    public ServerResponseEntity<List<SpuVO>> listGiftSpuBySpuIds(List<Long> spuIdList) {
        return ServerResponseEntity.success(spuService.listGiftSpuBySpuIds(spuIdList));
    }

    @Override
    public ServerResponseEntity<List<Long>> isSpuSkuChannel(SpuSkuRDTO spuSkuRDTO) {
        return ServerResponseEntity.success(spuService.isSpuSkuChannel(spuSkuRDTO));
    }

    @Override
    public ServerResponseEntity<List<StdPushSpuVO>> pushStdSpus(List<String> spuCodes) {
        return ServerResponseEntity.success(spuService.pushStdSpus(spuCodes));
    }

    @Override
    public ServerResponseEntity<List<SkuTimeDiscountActivityVO>> getStoreSpuAndSkuPrice(Long storeId, List<SpuSkuPriceDTO> skuPriceDTOs) {
        return ServerResponseEntity.success(spuSkuPricingPriceService.getStoreSpuAndSkuPrice(storeId,skuPriceDTOs));
    }

    @Override
    public ServerResponseEntity<Void> batchChangeSpuStatus(ChangeSpuStatusDto dto) {
        spuService.batchChangeSpuStatus(dto.getSpuIds(),dto.getStatus());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> batchZeroSetChannelsStock(ZeroSetStockDto dto) {
        spuService.batchZeroSetChannelsStock(dto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateChannelsStock(UpdateChannelsStockDto dto) {
        spuService.updateChannelsStock(dto.getSpuId(), dto.getSkuStockDtos());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<SpuVO> getEcSpuSkuInfoListBySpuId(Long spuId){
        SpuVO spuVO = spuService.getBySpuId(spuId);
        ProductLangUtil.spu(spuVO);
        spuVO.setSpuAttrValues(spuAttrValueService.getSpuAttrsBySpuId(spuId));
        spuVO.setSkus(skuService.listSkuAllInfoBySpuId(spuId, false, Constant.MAIN_SHOP));
        return ServerResponseEntity.success(spuVO);
    }

}
