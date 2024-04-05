package com.mall4j.cloud.discount.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopDetailVO;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.DiscountCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.DiscountOrderVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.discount.constant.SuitableProdTypeEnum;
import com.mall4j.cloud.discount.dto.AppDiscountListDTO;
import com.mall4j.cloud.discount.dto.DiscountDTO;
import com.mall4j.cloud.discount.dto.DiscountItemDTO;
import com.mall4j.cloud.discount.mapper.DiscountItemMapper;
import com.mall4j.cloud.discount.mapper.DiscountMapper;
import com.mall4j.cloud.discount.mapper.DiscountShopMapper;
import com.mall4j.cloud.discount.mapper.DiscountSpuMapper;
import com.mall4j.cloud.discount.model.Discount;
import com.mall4j.cloud.discount.model.DiscountShop;
import com.mall4j.cloud.discount.model.DiscountSpu;
import com.mall4j.cloud.discount.service.DiscountService;
import com.mall4j.cloud.discount.vo.DiscountVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 满减满折优惠
 *
 * @author lhd
 * @date 2020-12-17 13:43:38
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountMapper discountMapper;
    @Autowired
    private DiscountItemMapper discountItemMapper;
    @Autowired
    private DiscountSpuMapper discountSpuMapper;
    @Autowired
    DiscountShopMapper discountShopMapper;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private OfflineHandleEventFeignClient offlineHandleEventFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDiscountAndItemAndSpu(DiscountDTO discount) {
        discountMapper.update(discount);

        discountItemMapper.deleteByDiscountId(discount.getDiscountId());

        discountSpuMapper.deleteByDiscountId(discount.getDiscountId());
        discountShopMapper.deleteByDiscountId(discount.getDiscountId());

        insertDiscountItemsAndDiscountSpu(discount);
    }

    /**
     * 插入满减项
     *
     * @param discount 满减、满减项、关联spu信息
     */
    private void insertDiscountItemsAndDiscountSpu(DiscountDTO discount) {
        List<DiscountItemDTO> discountItems = discount.getDiscountItemList();
        for (DiscountItemDTO discountItem : discountItems) {
            if (discountItem.getNeedAmount() == null || discountItem.getDiscount() == null) {
                throw new LuckException("请填写完整满减信息");
            }
            discountItem.setDiscountId(discount.getDiscountId());
        }
        discountItemMapper.insertDiscountItems(discountItems);

        if (discount.getSuitableSpuType() != 0) {
            List<Long> spuIds = discount.getSpuIds();
            if (CollectionUtil.isNotEmpty(spuIds)) {
                List<DiscountSpu> discountSpuList = new ArrayList<>();
                for (Long spuId : spuIds) {
                    DiscountSpu discountSpu = new DiscountSpu();
                    discountSpu.setDiscountId(discount.getDiscountId());
                    discountSpu.setSpuId(spuId);
                    discountSpuList.add(discountSpu);
                }
                discountSpuMapper.insertDiscountSpuList(discountSpuList);
            }
        }
        //如果选择的商铺列表不为空。批量插入商铺列表
        if (CollectionUtil.isNotEmpty(discount.getShopIds())) {
            List<DiscountShop> shops = new ArrayList<>();
            for (Long shopId : discount.getShopIds()) {
                DiscountShop shop = new DiscountShop();
                shop.setShopId(shopId);
                shop.setActivityId(discount.getDiscountId().intValue());
                shops.add(shop);
            }
            discountShopMapper.insertBatch(shops);
        }
    }

    @Override
    @Cacheable(cacheNames = DiscountCacheNames.DISCOUNT_BY_ID, key = "#discountId")
    public DiscountVO discountInfo(Long discountId,Long shopId) {
        DiscountVO discount = discountMapper.getDiscountAndSpu(discountId, StatusEnum.ENABLE.value());
        if (Objects.isNull(discount)) {
            throw new LuckException("当前活动已关闭");
        }
        ServerResponseEntity<EsShopDetailBO> shopResponEntity = shopDetailFeignClient.getShopByShopId(shopId);
        if (shopResponEntity.isFail() || Objects.isNull(shopResponEntity.getData())) {
            throw new LuckException("店铺信息不存在");
        }
        EsShopDetailBO shopDetail = shopResponEntity.getData();
        discount.setShopId(shopId);
        discount.setShopName(shopDetail.getShopName());
        discount.setShopLogo(shopDetail.getShopLogo());

        long now = System.currentTimeMillis();

        // 活动还没开始
        if (discount.getStartTime().getTime() > now) {
            // 距离活动开始还有
            discount.setStartIn((int) (discount.getStartTime().getTime() - now) / 1000);
        }
        // 活动还没结束
        if (discount.getEndTime().getTime() > now) {
            // 距离活动开始还有
            discount.setExpiresIn((int) (discount.getEndTime().getTime() - now) / 1000);
        }
        return discount;
    }

    @Override
    public DiscountVO getDiscountAndSpu(Long discountId) {
        DiscountVO discountVO = discountMapper.getDiscountAndSpu(discountId, null);
        Assert.isNull(discountVO,"活动不存在。");
        discountVO.setDiscountShops(discountShopMapper.selectByDiscountId(discountId));
        return discountVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.DISCOUNT_BY_SHOPID, key = "#discount.shopId")
    public void insertDiscountAndItemAndSpu(@Valid DiscountDTO discount) {
        discountMapper.save(discount);

        List<DiscountItemDTO> discountItems = discount.getDiscountItemList();
        if (CollectionUtil.isEmpty(discountItems)) {
            throw new LuckException("活动项不能为空，最少要有一个活动项");
        }
        insertDiscountItemsAndDiscountSpu(discount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDiscountsAndItemsAndSpuList(Long discountId, Long shopId) {
        int i = discountMapper.deleteDiscounts(discountId, shopId);
        if (i > 0) {
            discountItemMapper.deleteByDiscountId(discountId);
            discountSpuMapper.deleteByDiscountId(discountId);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.DISCOUNT_BY_ID, key = "#discountId"),
            @CacheEvict(cacheNames = CacheNames.DISCOUNT_BY_SHOPID, key = "#shopId")
    })
    public void removeDiscountCache(Long discountId, Long shopId) {
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.DISCOUNT_BY_SHOPID, key = "#shopId")
    public List<DiscountOrderVO> listDiscountsAndItemsByShopId(Long shopId) {
        return discountMapper.getDiscountsAndItemsByShopId(shopId);
    }

    @Override
    public List<DiscountVO> spuDiscountList(Long shopId, Long spuId) {
        List<DiscountVO> discountVOList = discountMapper.spuDiscountList(shopId, spuId);
        if (Objects.isNull(discountVOList)) {
            return new ArrayList<>();
        }
        return discountVOList;
    }

    @Override
    public PageVO<DiscountVO> getAppDiscountList(PageDTO page, AppDiscountListDTO discount) {
        PageVO<DiscountVO> pageVO = PageUtil.doPage(page, () -> discountMapper.getDiscountListByStoreId(discount.getShopId()));
//        Set<Long> shopIds = pageVO.getList().stream().map(DiscountVO::getShopId).collect(Collectors.toSet());
//        if (CollUtil.isEmpty(shopIds)) {
//            return pageVO;
//        }
        if(CollectionUtil.isEmpty(pageVO.getList()) ){
            return pageVO;
        }
        ArrayList<Long> storeList = new ArrayList<>();
        storeList.add(discount.getShopId());
        ServerResponseEntity<List<ShopDetailVO>> response = shopDetailFeignClient.listByShopIds(storeList);
        Map<Long, ShopDetailVO> shopMap = response.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, s -> s));
        long now = System.currentTimeMillis();

        Assert.isTrue(response.getData()==null || response.getData().size() ==0,"商铺id不存在。");

        ShopDetailVO shopDetailVO = response.getData().get(0);

        for (DiscountVO discountVO : pageVO.getList()) {

            discountVO.setShopId(discount.getShopId());
            discountVO.setShopName(shopDetailVO.getShopName());
            discountVO.setShopLogo(shopDetailVO.getShopLogo());

            // 活动还没开始
            if (discountVO.getStartTime().getTime() > now) {
                // 距离活动开始还有
                discountVO.setStartIn((int) (discountVO.getStartTime().getTime() - now) / 1000);
            }
            // 活动还没结束
            if (discountVO.getEndTime().getTime() > now) {
                // 距离活动开始还有
                discountVO.setExpiresIn((int) (discountVO.getEndTime().getTime() - now) / 1000);
            }
        }
        return pageVO;
    }

    @Override
    public Discount getDiscountByDiscountId(Long discountId) {
        return discountMapper.getDiscountByDiscountId(discountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> updateDiscountSpuByIds(List<Long> spuIds) {
        // 查询出所有为可用商品类型的满减活动的，包含需要处理商品id，进行删除
        List<DiscountSpu> discountSpu = discountItemMapper.listDiscountBySpuIds(spuIds);
        List<Long> spuIdsDb = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(discountSpu)) {
            for (DiscountSpu spu : discountSpu) {
                ids.add(spu.getDiscountId());
                spuIdsDb.add(spu.getSpuId());
            }
            discountSpuMapper.deleteBySpuIds(spuIdsDb);
        }
        return ids;
    }

    @Override
    public PageVO<DiscountVO> page(PageDTO pageDTO, DiscountDTO transportDTO) {
        return PageUtil.doPage(pageDTO, () -> discountMapper.list(transportDTO));
    }

    @Override
    public PageVO<DiscountVO> platformPage(PageDTO pageDTO, DiscountDTO transportDTO) {
        PageVO<DiscountVO> page = PageUtil.doPage(pageDTO, () -> discountMapper.list(transportDTO));
        Set<Long> shopIdSet = page.getList().stream().map(DiscountVO::getShopId).collect(Collectors.toSet());
        ServerResponseEntity<List<ShopDetailVO>> shopResponse = shopDetailFeignClient.listByShopIds(new ArrayList<>(shopIdSet));
        if (CollUtil.isEmpty(shopResponse.getData())) {
            return page;
        }
        Map<Long, String> shopMap = shopResponse.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, ShopDetailVO::getShopName));
        for (DiscountVO discountVO : page.getList()) {
            discountVO.setShopName(shopMap.get(discountVO.getShopId()));
        }
        return page;
    }

    @Override
    public void changeDiscountStatus(Long discountId, Integer status) {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountId(discountId);
        discountDTO.setStatus(status);
        discountMapper.update(discountDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void offline(OfflineHandleEventDTO offlineHandleEventDto) {
        OfflineHandleEventDTO offlineHandleEvent = new OfflineHandleEventDTO();
        offlineHandleEvent.setHandleId(offlineHandleEventDto.getHandleId());
        offlineHandleEvent.setHandleType(OfflineHandleEventType.DISCOUNT.getValue());
        offlineHandleEvent.setOfflineReason(offlineHandleEventDto.getOfflineReason());
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.save(offlineHandleEvent);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新活动状态为下线状态
        changeDiscountStatus(offlineHandleEvent.getHandleId(), StatusEnum.OFFLINE.value());
    }

    @Override
    public OfflineHandleEventVO getOfflineHandleEvent(Long discountId) {
        ServerResponseEntity<OfflineHandleEventVO> offlineHandleEventResponse =
                offlineHandleEventFeignClient.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.DISCOUNT.getValue(), discountId);
        return offlineHandleEventResponse.getData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void audit(OfflineHandleEventDTO offlineHandleEventDto) {
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.auditOfflineEvent(offlineHandleEventDto);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 审核通过
        if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
            // 更新活动为等待投放状态、过期状态为未过期
            changeDiscountStatus(offlineHandleEventDto.getHandleId(), StatusEnum.DISABLE.value());
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            changeDiscountStatus(offlineHandleEventDto.getHandleId(), StatusEnum.OFFLINE.value());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void auditApply(OfflineHandleEventDTO offlineHandleEventDto) {
        // 更新事件状态
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.updateToApply(offlineHandleEventDto);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新活动为待审核状态
        changeDiscountStatus(offlineHandleEventDto.getHandleId(), StatusEnum.WAIT_AUDIT.value());
    }

    @Override
    public void closeDiscountBySetStatus() {
        discountMapper.closeDiscountBySetStatus();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleSpuOffline(List<Long> spuIds, List<Long> shopIds) {
        List<DiscountVO> discountList = discountMapper.discountIdsBySpuIds(spuIds, shopIds);
        if (CollUtil.isEmpty(discountList)) {
            return;
        }
        List<Long> discountIds = new ArrayList<>();
        List<Long> offlineList = new ArrayList<>();
        Set<Long> shopSet = new HashSet<>();
        for (DiscountVO discountVO : discountList) {
            discountIds.add(discountVO.getDiscountId());
            shopSet.add(discountVO.getShopId());
            // 活动不是启动状态，或 适用商品类型为：全部商品，则不进行后面的处理
            if (!Objects.equals(discountVO.getStatus(), StatusEnum.ENABLE.value())
                    || Objects.equals(SuitableProdTypeEnum.ALL_SPU.value(), discountVO.getSuitableSpuType())) {
                continue;
            }
            discountVO.getSpuIds().removeAll(spuIds);
            // 商品列表为空，则把活动添加到下线列表中
            if (CollUtil.isEmpty(discountVO.getSpuIds())) {
                offlineList.add(discountVO.getDiscountId());
            }
        }
        // 删除商品关联信息
        discountSpuMapper.deleteBySpuIds(spuIds);
        // 商品列表为空的活动进行下线
        if (CollUtil.isNotEmpty(offlineList)) {
            discountMapper.batchOfflineByDiscountIdsAndStatus(offlineList);
        }
        // 清除缓存
        batchRemoveDiscountCache(discountIds, shopSet);
    }

    @Override
    public DiscountVO getDiscountInfoById(Long discountId, ProductSearchDTO productSearchDTO) {

        DiscountVO discount = discountMapper.getDiscountAndSpu(discountId, null);
        Assert.isNull(discount,"活动不存在或已经结束。");
        ServerResponseEntity<EsShopDetailBO> shopResponEntity = shopDetailFeignClient.getShopByShopId(productSearchDTO.getShopId());
        EsShopDetailBO shopDetail = shopResponEntity.getData();
        Assert.isTrue(shopResponEntity==null||shopResponEntity.getData()==null,"商铺不存在。");
        discount.setShopId(productSearchDTO.getShopId());
        discount.setShopName(shopDetail.getShopName());
        discount.setShopLogo(shopDetail.getShopLogo());

        long now = System.currentTimeMillis();

        // 活动还没开始
        if (discount.getStartTime().getTime() > now) {
            // 距离活动开始还有
            discount.setStartIn((int) (discount.getStartTime().getTime() - now) / 1000);
        }
        // 活动还没结束
        if (discount.getEndTime().getTime() > now) {
            // 距离活动开始还有
            discount.setExpiresIn((int) (discount.getEndTime().getTime() - now) / 1000);
        }
        return discount;
    }

    /**
     * 批量清除缓存
     *
     * @param discountIds 活动id列表
     * @param shopIds     店铺id列表
     */
    private void batchRemoveDiscountCache(List<Long> discountIds, Set<Long> shopIds) {
        if (CollUtil.isEmpty(discountIds) || CollUtil.isEmpty(shopIds)) {
            return;
        }
        List<String> keys = new ArrayList<>();
        for (Long discountId : discountIds) {
            keys.add(CacheNames.DISCOUNT_BY_ID + CacheNames.UNION + discountId);

        }
        for (Long shopId : shopIds) {
            keys.add(CacheNames.DISCOUNT_BY_SHOPID + CacheNames.UNION + shopId);
        }
        RedisUtil.deleteBatch(keys);
    }


}
