package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.coupon.constant.CouponType;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopDetailVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.product.dto.ProductSearchLimitDTO;
import com.mall4j.cloud.common.product.vo.search.ProductSearchVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.*;
import com.mall4j.cloud.coupon.dto.CouponDTO;
import com.mall4j.cloud.coupon.mapper.TCouponCommodityMapper;
import com.mall4j.cloud.coupon.mapper.TCouponSkuMapper;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.mapper.CouponMapper;
import com.mall4j.cloud.coupon.model.CouponUser;
import com.mall4j.cloud.coupon.model.TCouponCommodity;
import com.mall4j.cloud.coupon.model.TCouponSku;
import com.mall4j.cloud.coupon.service.CouponSpuService;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import com.mall4j.cloud.coupon.vo.CouponVO;
import io.seata.spring.annotation.GlobalTransactional;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
@Service
public class CouponServiceImpl implements CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);


    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponSpuService couponSpuService;
    @Autowired
    private CouponUserService couponUserService;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private OfflineHandleEventFeignClient offlineHandleEventFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    TCouponCommodityMapper tCouponCommodityMapper;
    @Autowired
    TCouponSkuMapper tCouponSkuMapper;


    @Override
    public PageVO<CouponVO> page(PageDTO pageDTO, CouponDTO couponDTO) {
        return PageUtil.doPage(pageDTO, () -> couponMapper.list(couponDTO));
    }

    @Override
    @Cacheable(cacheNames = CacheNames.COUPON_LIST_BY_SHOP_KEY, key = "#shopId")
    public List<CouponAppVO> getShopCouponList(Long shopId) {
        List<CouponAppVO> coupons = couponMapper.getShopCouponList(shopId);
        if (CollectionUtil.isEmpty(coupons)) {
            return coupons;
        }
        Set<Long> spuIds = new HashSet<>();
        CouponAppVO couponAppVO = coupons.get(0);
        ServerResponseEntity<EsShopDetailBO> shopResponse = shopDetailFeignClient.getShopByShopId(couponAppVO.getShopId());
        if (!Objects.equals(shopResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(shopResponse.getMsg());
        }
        String shopName = shopResponse.getData().getShopName();
        for (CouponAppVO coupon : coupons) {
            coupon.setShopName(shopName);
            spuIds.addAll(coupon.getSpuIds());
            coupon.setHasReceive(CouponAppVO.NOT_RECEIVE);
        }
        if (CollUtil.isEmpty(spuIds)) {
            return coupons;
        }
        if (CollectionUtil.isNotEmpty(spuIds)) {
            ServerResponseEntity<List<SpuSearchVO>> spusRespon = searchSpuFeignClient.listSpuBySpuIds(new ArrayList<>(spuIds));
            for (CouponAppVO coupon : coupons) {
                List<SpuSearchVO> spuList = spusRespon.getData()
                        .stream()
                        .filter(spuSearch -> CollUtil.isNotEmpty(coupon.getSpuIds()) && coupon.getSpuIds().contains(spuSearch.getSpuId()))
                        .collect(Collectors.toList());
                coupon.setSpus(spuList);
            }
        }
        coupons.sort(Comparator.comparingLong(CouponAppVO::getCashCondition));
        return coupons;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.COUPON_AND_SPU_DATA, key = "#couponId")
    public CouponVO getCouponAndProdData(Long couponId) {
        CouponVO coupon = couponMapper.getCouponAndCouponProdsByCouponId(couponId);
        if (Objects.isNull(coupon) || CollUtil.isEmpty(coupon.getSpuIds())) {
            return coupon;
        }
        ServerResponseEntity<List<SpuSearchVO>> spusRespon = searchSpuFeignClient.listSpuBySpuIds(coupon.getSpuIds());
        coupon.setSpus(spusRespon.getData());
        return coupon;
    }

    @Override
    public Coupon getByCouponId(Long couponId) {
        return couponMapper.getByCouponId(couponId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CouponDTO couponDTO) {
        checkCoupon(couponDTO);
        Coupon coupon = mapperFacade.map(couponDTO, Coupon.class);
        coupon.setTotalStock(coupon.getStocks());
        //判断为领取后生效类型时，开始时间和结束时间为空
        if(Objects.equals(coupon.getValidTimeType(), ValidTimeType.RECEIVE.value())){
            coupon.setEndTime(null);
            coupon.setStartTime(null);
        }
        // 结束时间小于等于当前时间
        Date nowTime = new Date();
        if (coupon.getValidTimeType() == 1 && coupon.getEndTime().getTime() < nowTime.getTime()) {
            coupon.setStatus(0);
            coupon.setPutonStatus(0);
        }
        couponMapper.save(coupon);
        if (Objects.isNull(coupon.getCouponId())) {
            throw new LuckException("优惠券数据保存异常，请刷新后重试");
        }
        if (Objects.equals(coupon.getSuitableProdType(), SuitableProdType.ASSIGN_SPU.value())) {
            // 保存优惠券商品关联信息
            couponSpuService.save(coupon.getCouponId(), couponDTO.getSpuIds());
        }
        removeSpuCache(coupon.getSuitableProdType(), coupon.getShopId(), couponDTO.getSpuIds());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CouponDTO couponDTO) {
        checkCoupon(couponDTO);
        CouponVO dbCoupon = couponMapper.getCouponAndCouponProdsByCouponId(couponDTO.getCouponId());
        if (!Objects.equals(dbCoupon.getStatus(), StatusEnum.ENABLE.value()) && !Objects.equals(dbCoupon.getStatus(), StatusEnum.DISABLE.value())) {
            couponDTO.setStatus(null);
        }
        if(!PutonStatus.offline(couponDTO.getPutonStatus()) && PutonStatus.offline(dbCoupon.getPutonStatus())) {
            throw new LuckException("优惠券已下线，不能修改状态");
        } else if (!Objects.equals(dbCoupon.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        } else if (Objects.isNull(couponDTO.getChangeStock())) {
            couponDTO.setChangeStock(0);
        }
        couponDTO.setStatus(null);
        // 结束时间小于等于当前时间
        if (Objects.equals(couponDTO.getValidTimeType(), ValidTimeType.FIXED.value())
                && !Objects.equals(dbCoupon.getStatus(), StatusEnum.DELETE.value())) {
            Date nowTime = new Date();
            if (couponDTO.getEndTime().getTime() < nowTime.getTime()) {
                couponDTO.setStatus(CouponStatus.OVERDUE.value());
                couponDTO.setPutonStatus(PutonStatus.WAIT_PUT_ON.value());
            } else {
                couponDTO.setStatus(CouponStatus.NO_OVERDUE.value());
            }
        }
        couponMapper.updateCouponAndStock(couponDTO);
        // 更新优惠券商品关联信息
        couponSpuService.update(couponDTO, dbCoupon);
        // 投放状态及商品列表没有改变， 不需要更新缓存
        boolean notRemoveCahce = Objects.equals(couponDTO.getPutonStatus(), dbCoupon.getPutonStatus()) &&
                couponDTO.getSpuIds().containsAll(dbCoupon.getSpuIds()) &&
                Objects.equals(couponDTO.getSuitableProdType(), dbCoupon.getSuitableProdType());
        if (notRemoveCahce) {
            return;
        }
        boolean cancelBinding = Objects.equals(dbCoupon.getShopId(), Constant.PLATFORM_SHOP_ID) &&
                !Objects.equals(couponDTO.getPutonStatus(), dbCoupon.getPutonStatus()) &&
                Objects.equals(dbCoupon.getPutonStatus(), PutonStatus.PUT_ON.value());
        if (cancelBinding) {
            ServerResponseEntity<Void> responseEntity = userFeignClient.cancelBindingCoupons(Collections.singletonList(couponDTO.getCouponId()));
            if(responseEntity.isFail()) {
                throw new LuckException(responseEntity.getMsg());
            }
        }
        // 清除关联商品缓存
        List<Long> spuIds = couponDTO.getSpuIds();
        if (!Objects.equals(couponDTO.getSuitableProdType(), dbCoupon.getSuitableProdType())) {
            spuIds.clear();
        } else if (Objects.equals(couponDTO.getSuitableProdType(), SuitableProdType.ASSIGN_SPU.value())) {
            spuIds = CollUtil.addAllIfNotContains(dbCoupon.getSpuIds(), couponDTO.getSpuIds());
        }
        removeSpuCache(couponDTO.getSuitableProdType(), couponDTO.getShopId(), spuIds);
    }

    @Override
    public void deleteById(Long couponId) {
        CouponVO dbCoupon = getCouponAndCouponProdsByCouponId(couponId);
        if (!Objects.equals(dbCoupon.getShopId(), AuthUserContext.get().getTenantId()) && !Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setStatus(StatusEnum.DELETE.value());
        couponMapper.update(coupon);
        removeSpuCache(dbCoupon.getSuitableProdType(), dbCoupon.getShopId(), dbCoupon.getSpuIds());
        if (Objects.equals(dbCoupon.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            ServerResponseEntity<Void> responseEntity = userFeignClient.cancelBindingCoupons(Collections.singletonList(dbCoupon.getCouponId()));
            if(responseEntity.isFail()) {
                throw new LuckException(responseEntity.getMsg());
            }
        }
    }

    @Override
    public CouponVO getCouponAndCouponProdsByCouponId(Long couponId) {
        CouponVO coupon = couponMapper.getCouponAndCouponProdsByCouponId(couponId);
        if (Objects.isNull(coupon) || Objects.equals(coupon.getSuitableProdType(), SuitableProdType.ALL_SPU.value())) {
            return coupon;
        }
        ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.listSpuBySpuIds(coupon.getSpuIds());
        coupon.setSpus(spuResponse.getData());
        return coupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchBindCouponByCouponIds(List<Long> couponIds, Long userId) {
        List<CouponUser> couponUsers = new ArrayList<>();
        List<Coupon> coupons = couponMapper.getListByCouponIds(couponIds);
        if(CollUtil.isEmpty(coupons)){
            return;
        }
        List<Coupon> updateCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            Date nowTime = new Date();
            // 当优惠券状态不为投放时
            if (Objects.equals(coupon.getStatus(), StatusEnum.DISABLE.value())
                    || !Objects.equals(coupon.getPutonStatus(), PutonStatus.PUT_ON.value())
                    || coupon.getStocks() < 1) {
                logger.warn("优惠券：" + coupon.getCouponId() + "无法被领取或者该券领完了!");
                continue;
            }
            int count = couponUserService.getUserHasCouponCount(coupon.getCouponId(),userId);
            if (count >= coupon.getLimitNum()) {
                logger.warn("用户：" + userId +"，的优惠券：" + coupon.getCouponId() + "已达个人领取上限，无法继续领取");
                continue;
            }
            CouponUser couponUser = new CouponUser();
            couponUser.setUserId(userId);
            couponUser.setStatus(StatusEnum.ENABLE.value());
            couponUser.setCouponId(coupon.getCouponId());
            couponUser.setReceiveTime(nowTime);
            // 生效时间类型为固定时间
            if (Objects.equals(coupon.getValidTimeType(), ValidTimeType.FIXED.value())) {
                couponUser.setUserStartTime(coupon.getStartTime());
                couponUser.setUserEndTime(coupon.getEndTime());
            }
            // 生效时间类型为领取后生效
            if (Objects.equals(coupon.getValidTimeType(), ValidTimeType.RECEIVE.value())) {
                if (coupon.getAfterReceiveDays() == null) {
                    coupon.setAfterReceiveDays(0);
                }
                if (coupon.getValidDays() == null) {
                    coupon.setValidDays(0);
                }
                couponUser.setUserStartTime(new Date());
                couponUser.setUserEndTime(DateUtils.addDays(DateUtil.endOfDay(new Date()), coupon.getAfterReceiveDays()));
            }
            couponUser.setIsDelete(0);
            couponUsers.add(couponUser);
            // 减少优惠券的库存
            if (couponMapper.updateCouponStocks(coupon) < 1) {
                continue;
            }
            // 清除缓存
            removeCacheByShopId(coupon.getShopId(),coupon.getCouponId());
            updateCoupons.add(coupon);
        }
        if (CollUtil.isNotEmpty(couponUsers)) {
            couponUserService.saveBatch(couponUsers);
        }
    }

    @Override
    public EsPageVO<ProductSearchVO> spuListByCouponId(PageDTO pageDTO, ProductSearchDTO productSearch, Long couponId) {
        CouponServiceImpl couponService = (CouponServiceImpl) AopContext.currentProxy();
        CouponVO coupon = couponService.getCouponAndProdData(couponId);
        // 全部商品
        if (Objects.equals(coupon.getSuitableProdType(), SuitableProdType.ALL_SPU.value()) && !Objects.equals(coupon.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            productSearch.setShopId(coupon.getShopId());
        }
        // 指定商品
        else {
            productSearch.setSpuIds(coupon.getSpuIds());
        }
        productSearch.setPageSize(pageDTO.getPageSize());
        productSearch.setPageNum(pageDTO.getPageNum());
        ServerResponseEntity<EsPageVO<ProductSearchVO>> spuPageResponse = searchSpuFeignClient.search(productSearch);
        return spuPageResponse.getData();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receive(Long couponId) {

        Coupon coupon = couponMapper.getByCouponId(couponId);
        CouponUser couponUser = getCouponUser(coupon, AuthUserContext.get().getUserId());
        couponUserService.save(couponUser);
        // 更新库存
        if (coupon.getStocks() < 1) {
            // 优惠券库存不足
            throw new LuckException("优惠券库存不足");
        }
        if (couponMapper.updateCouponStocks(coupon) < 1) {
            throw new LuckException("优惠券领取失败，请重新领取");
        }
    }

    @Override
    public PageVO<CouponAppVO> getUserCouponPage(PageDTO page, Long type, Integer status) {
        PageVO<CouponAppVO> couponPage = PageUtil.doPage(page, () -> couponMapper.getUserCouponList(AuthUserContext.get().getUserId(), type, status));
        List<CouponAppVO> couponDtoList = couponPage.getList();
        Set<Long> shopSet = couponDtoList.stream().map(CouponAppVO::getShopId).collect(Collectors.toSet());
        ServerResponseEntity<List<ShopDetailVO>> shopResponse = shopDetailFeignClient.listByShopIds(new ArrayList<>(shopSet));
        Map<Long, String> shopMap = shopResponse.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, ShopDetailVO::getShopName));
        for (CouponAppVO coupon : couponDtoList) {
            if (Objects.equals(coupon.getValidTimeType(), ValidTimeType.RECEIVE.value())) {
                Integer afterReceiveDays = coupon.getAfterReceiveDays();
                if (Objects.isNull(coupon.getAfterReceiveDays())) {
                    afterReceiveDays = 0;
                }
                Integer validDays = coupon.getValidDays();
                if (validDays == null) {
                    validDays = 0;
                }
                coupon.setStartTime(DateUtil.offsetDay(coupon.getReceiveTime(), afterReceiveDays));
                coupon.setEndTime(DateUtil.offsetDay(coupon.getStartTime(), validDays));
            }
            coupon.setShopName(shopMap.get(coupon.getShopId()));
        }
        return couponPage;
    }

    @Override
    public List<CouponAppVO> generalCouponList(Long userId) {
        return couponMapper.generalCouponList(userId);
    }

    @Override
    public PageVO<CouponAppVO> getProdCouponPage(PageDTO pageDTO, Long userId) {
        PageVO<CouponAppVO> couponPage = new PageVO<>();
        List<CouponAppVO> coupons = couponMapper.getProdCouponList(new PageAdapter(pageDTO), userId);
        couponPage.setTotal(couponMapper.getProdCouponListCount());
        couponPage.setPages(PageUtil.getPages(couponPage.getTotal(), pageDTO.getPageSize()));
        couponPage.setList(coupons);
        Set<Long> spuUnionShopIds = new HashSet<>();
        Set<Long> shopIds = new HashSet<>();
        Set<Long> spuIds = new HashSet<>();
        for (CouponAppVO couponVO : coupons) {
            shopIds.add(couponVO.getShopId());
            if (Objects.equals(couponVO.getSuitableProdType(), SuitableProdType.ALL_SPU.value())) {
                spuUnionShopIds.add(couponVO.getShopId());
                continue;
            }
            int start = 1;
            for (Long spuId : couponVO.getSpuIds()) {
                if (start > Constant.SIZE_OF_THREE) {
                    break;
                }
                start++;
                spuIds.add(spuId);
            }
        }
        loadSpuData(new ArrayList<>(spuUnionShopIds), new ArrayList<>(spuIds), new ArrayList<>(shopIds), coupons);
        return couponPage;
    }

    @Override
    public List<Coupon> getCouponListByCouponIds(List<Long> couponIds) {
        if (CollUtil.isEmpty(couponIds)) {
            return Collections.emptyList();
        }
        return couponMapper.getCouponListByCouponIds(couponIds,null);
    }

    @Override
    public List<CouponOrderVO> getCouponListByUserIdAndShopId(Long userId, Long shopId) {
        return couponMapper.getCouponListByUserIdAndShopId(userId, shopId);
    }

    @Override
    public List<CouponOrderVO> getCouponListByUserAndShop(Long userId, Long shopId) {
        List<CouponOrderVO> couponOrderVOS = couponMapper.getCouponListByUserAndShop(userId, shopId);

        if(CollUtil.isEmpty(couponOrderVOS)){
            return couponOrderVOS;
        }

        List<Long> couponIds = couponOrderVOS.stream().map(CouponOrderVO::getCouponId).collect(Collectors.toList());
        List<TCouponCommodity> couponCommodities = tCouponCommodityMapper.getListByCouponIds(couponIds);

        if(CollUtil.isEmpty(couponCommodities)){
            return couponOrderVOS;
        }

        Map<Long, List<TCouponCommodity>> mapCouponCommodities = couponCommodities.stream().collect(Collectors.groupingBy(TCouponCommodity::getCouponId));

        for (CouponOrderVO couponOrderVO : couponOrderVOS) {
            List<TCouponCommodity> tCouponCommodities = mapCouponCommodities.get(couponOrderVO.getCouponId());
            if(CollUtil.isNotEmpty(tCouponCommodities)){
                couponOrderVO.setSpuIds(tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList()));
            }
        }
        return couponOrderVOS;
    }

    @Override
    public List<CouponOrderVO> getCouponListByUserAndShop(Long userId, Long shopId, List<Long> spuIds) {
        //        List<CouponOrderVO> couponOrderVOS = couponMapper.getCouponListByUserAndShopAndSpuIds(userId, shopId,spuIds);

        List<CouponOrderVO> couponOrderVOS = couponMapper.getCouponListByUserAndShop(userId, shopId);
        if(CollUtil.isEmpty(couponOrderVOS)){
            return couponOrderVOS;
        }

        List<Long> couponIds = couponOrderVOS.stream().map(CouponOrderVO::getCouponId).collect(Collectors.toList());

        List<TCouponCommodity> couponCommodities = tCouponCommodityMapper.getListByCouponIds(couponIds);
//        if(CollUtil.isEmpty(couponCommodities)){
//            return couponOrderVOS;
//        }
        Map<Long, List<TCouponCommodity>> mapCouponCommodities = couponCommodities.stream().collect(Collectors.groupingBy(TCouponCommodity::getCouponId));

        List<TCouponSku> tCouponSkus = tCouponSkuMapper.getListByCouponIds(couponIds);
        Map<Long, List<TCouponSku>> mapSkus = tCouponSkus.stream().collect(Collectors.groupingBy(TCouponSku::getCouponId));

        for (CouponOrderVO couponOrderVO : couponOrderVOS) {
            List<TCouponCommodity> tCouponCommodities = mapCouponCommodities.get(couponOrderVO.getCouponId());
            if(CollUtil.isNotEmpty(tCouponCommodities)){
                couponOrderVO.setSpuIds(tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList()));
            }

            List<TCouponSku> skus = mapSkus.get(couponOrderVO.getCouponId());
            if(CollUtil.isNotEmpty(skus)){
                couponOrderVO.setPriceCodes(skus.stream().map(TCouponSku::getPriceCode).collect(Collectors.toList()));
            }

        }
        return couponOrderVOS;
    }

    @Override
    public void removeCacheByShopId(Long shopId, Long couponId) {
        List<String> keys = new ArrayList<>();
        if (Objects.nonNull(shopId)) {
            keys.add(CacheNames.COUPON_LIST_BY_SHOP_KEY + CacheNames.UNION + shopId);
        }
        if (Objects.nonNull(couponId)) {
            keys.add(CacheNames.COUPON_AND_SPU_DATA + CacheNames.UNION + couponId);
        }
        RedisUtil.deleteBatch(keys);

    }

    @Override
    public void changeCouponStatus(Long couponId, Integer status) {
        CouponVO dbCoupon = getCouponAndCouponProdsByCouponId(couponId);
        if (Objects.equals(dbCoupon.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        } else if (StatusEnum.offlineStatus(dbCoupon.getStatus())) {
            throw new LuckException("状态异常无法修改");
        }
        if (Objects.equals(dbCoupon.getStatus(), status)) {
            return;
        }
        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setStatus(status);
        couponMapper.update(coupon);
        removeSpuCache(coupon.getSuitableProdType(), coupon.getShopId(), dbCoupon.getSpuIds());
    }

    @Override
    public PageVO<CouponVO> adminPage(PageDTO pageDTO, CouponDTO couponDTO) {
        PageVO<CouponVO> page = PageUtil.doPage(pageDTO, () -> couponMapper.adminList(couponDTO));
        Set<Long> shopSet = page.getList().stream().map(CouponVO::getShopId).collect(Collectors.toSet());
        ServerResponseEntity<List<ShopDetailVO>> shopResponse = shopDetailFeignClient.listByShopIds(new ArrayList<>(shopSet));
        Map<Long, String> shopMap = shopResponse.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, ShopDetailVO::getShopName));
        for (CouponVO coupon : page.getList()) {
            coupon.setShopName(shopMap.get(coupon.getShopId()));
        }
        return page;
    }

    @Override
    public void changeCouponStatusAndPutOnStatus(Long couponId, Integer putOnStatus, Integer status) {
        couponMapper.changeCouponStatusAndPutOnStatus(couponId, putOnStatus, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void offline(OfflineHandleEventDTO offlineHandleEventDto) {
        OfflineHandleEventDTO offlineHandleEvent = new OfflineHandleEventDTO();
        offlineHandleEvent.setHandleId(offlineHandleEventDto.getHandleId());
        offlineHandleEvent.setHandleType(OfflineHandleEventType.COUPON.getValue());
        offlineHandleEvent.setOfflineReason(offlineHandleEventDto.getOfflineReason());
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.save(offlineHandleEvent);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新活动状态为下线状态
        changeCouponStatusAndPutOnStatus(offlineHandleEvent.getHandleId(), PutonStatus.OFFLINE.value(), null);
    }

    @Override
    public OfflineHandleEventVO getOfflineHandleEvent(Long couponId) {
        ServerResponseEntity<OfflineHandleEventVO> offlineHandleEventResponse =
                offlineHandleEventFeignClient.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.COUPON.getValue(), couponId);
        return offlineHandleEventResponse.getData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void audit(OfflineHandleEventDTO offlineHandleEventDto, Coupon coupon) {
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.auditOfflineEvent(offlineHandleEventDto);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 审核通过
        if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
//            if(Objects.equals(coupon.getStatus(), CouponStatus.NO_OVERDUE.value())) {
//                changeCouponStatusAndPutOnStatus(offlineHandleEventDto.getHandleId(), PutonStatus.WAIT_PUT_ON.value(), CouponStatus.NO_OVERDUE.value());
//            } else {
//                changeCouponStatusAndPutOnStatus(offlineHandleEventDto.getHandleId(), PutonStatus.CANCEL.value(), null);
//            }
            changeCouponStatusAndPutOnStatus(offlineHandleEventDto.getHandleId(), PutonStatus.CANCEL.value(), null);
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            changeCouponStatusAndPutOnStatus(offlineHandleEventDto.getHandleId(), PutonStatus.OFFLINE.value(), null);
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
        // 更新优惠券为待审核状态
        changeCouponStatusAndPutOnStatus(offlineHandleEventDto.getHandleId(), PutonStatus.WAIT_AUDIT.value(), null);
    }

    @Override
    public List<Coupon> getCouponListByCouponIdsAndPutOnStatus(List<Long> couponIds, Integer putOnStatus) {
        List<Coupon> couponList = couponMapper.getCouponListByCouponIds(couponIds, putOnStatus);
        return couponList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPut() {
        List<Long> couponIds = couponMapper.cancelPutPlatformCouponIds();
        ServerResponseEntity<Void> responseEntity = userFeignClient.cancelBindingCoupons(couponIds);
        if (responseEntity.isFail()) {
            throw new LuckException(responseEntity.getMsg());
        }
        couponMapper.cancelPut();
    }

    @Override
    public void putonCoupon() {
        couponMapper.putonCoupon();
    }

    @Override
    public void handleSpuOffline(List<Long> spuIds, List<Long> shopIds) {
        List<CouponVO> couponList = couponMapper.listCouponBySpuIds(spuIds, shopIds);
        if (CollUtil.isEmpty(couponList)) {
            return;
        }
        List<Long> discountIds = new ArrayList<>();
        List<Long> offlineList = new ArrayList<>();
        Set<Long> shopSet = new HashSet<>();
        for (CouponVO couponVO : couponList) {
            discountIds.add(couponVO.getCouponId());
            shopSet.add(couponVO.getShopId());
            // 活动不是启动状态，或 适用商品类型为：全部商品，则不进行后面的处理
            if (!Objects.equals(couponVO.getPutonStatus(), PutonStatus.PUT_ON.value())
                    || Objects.equals(SuitableProdType.ALL_SPU.value(), couponVO.getSuitableProdType())) {
                continue;
            }
            couponVO.getSpuIds().removeAll(spuIds);
            // 商品列表为空，则把活动添加到下线列表中
            if (CollUtil.isEmpty(couponVO.getSpuIds())) {
                offlineList.add(couponVO.getCouponId());
            }
        }
        // 删除商品关联信息
        couponSpuService.deleteBySpuIds(spuIds);
        // 商品列表为空的活动进行下线
        if (CollUtil.isNotEmpty(offlineList)) {
            couponMapper.batchOfflineByDiscountIdsAndStatus(offlineList);
        }
        // 清除缓存
        batchRemoveCacheByShopId(discountIds, shopSet);
    }

    private void batchRemoveCacheByShopId(List<Long> couponIds, Set<Long> shopIds) {
        if (CollUtil.isEmpty(couponIds) || CollUtil.isEmpty(shopIds)) {
            return;
        }
        List<String> keys = new ArrayList<>();
        for (Long couponId : couponIds) {
            keys.add(CacheNames.COUPON_LIST_BY_SHOP_KEY + CacheNames.UNION + couponId);

        }
        for (Long shopId : shopIds) {
            keys.add(CacheNames.COUPON_AND_SPU_DATA + CacheNames.UNION + shopId);
        }
        RedisUtil.deleteBatch(keys);
    }

    /**
     * 根据店铺、spuId，获取优惠券中的spu列表
     * @param spuUnionShopIds
     * @param spuIds
     * @param shopIds
     * @param coupons
     */
    private void loadSpuData(List<Long> spuUnionShopIds, List<Long> spuIds, List<Long> shopIds, List<CouponAppVO> coupons) {
        Map<Long, List<SpuSearchVO>> spuUnionShopMap = null;
        Map<Long, SpuSearchVO> spuMap = new HashMap<>(spuIds.size());
        if (CollUtil.isNotEmpty(spuUnionShopIds)) {
            ProductSearchLimitDTO productSearchLimitDTO = new ProductSearchLimitDTO(Constant.SIZE_OF_THREE);
            productSearchLimitDTO.setShopIds(spuUnionShopIds);
            ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.limitSpuList(productSearchLimitDTO);
            spuUnionShopMap = spuResponse.getData().stream().collect(Collectors.groupingBy(SpuSearchVO::getShopId));
        }
        if (CollUtil.isNotEmpty(spuIds)) {
            ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.listSpuBySpuIds(spuIds);
            spuMap.putAll(spuResponse.getData().stream().collect(Collectors.toMap(SpuSearchVO::getSpuId, s->s)));
        }
        ServerResponseEntity<List<ShopDetailVO>> shopResponse = shopDetailFeignClient.listByShopIds(shopIds);
        Map<Long, ShopDetailVO> shopMap = shopResponse.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, s->s));

        for (CouponAppVO coupon : coupons) {
            if (Objects.equals(coupon.getSuitableProdType(), SuitableProdType.ALL_SPU.value())) {
                coupon.setSpus(spuUnionShopMap.get(coupon.getShopId()));
            } else {
                coupon.setSpus(new ArrayList<>(Constant.SIZE_OF_THREE));
                int index = 1;
                for (Long spuId : coupon.getSpuIds()) {
                    if (index > Constant.SIZE_OF_THREE) {
                        break;
                    }
                    index++;
                    SpuSearchVO spuSearchVO = spuMap.get(spuId);
                    if (Objects.nonNull(spuSearchVO)) {
                        coupon.getSpus().add(spuSearchVO);
                    }
                }
            }
            ShopDetailVO shopDetailVO = shopMap.get(coupon.getShopId());
            if (Objects.nonNull(shopDetailVO)) {
                coupon.setShopName(shopDetailVO.getShopName());
                coupon.setShopLogo(shopDetailVO.getShopLogo());
            }
        }
    }

    /**
     * 获取用户要领取的优惠券的信息
     * @param coupon 优惠券信息
     * @param userId 用户id
     * @return 用户优惠券信息
     */
    private CouponUser getCouponUser(Coupon coupon, Long userId){
        Date nowTime = new Date();
        // 当优惠券状态不为投放时
        if (Objects.equals(coupon.getStatus(), StatusEnum.DISABLE.value()) ||
                !Objects.equals(coupon.getPutonStatus(), PutonStatus.PUT_ON.value())) {
            throw new LuckException("该优惠券券无法被领取");
        }
        // 当优惠券无库存时
        if (coupon.getStocks() == 0) {
            throw new LuckException("该优惠券已领完了");
        }
        int count = couponUserService.getCouponCountForUser(coupon.getCouponId(), userId);
        if (count >= coupon.getLimitNum()) {
            // 该券已达个人领取上限，无法继续领取
            throw new LuckException("该优惠券已达个人领取上限，无法继续领取");
        }
        CouponUser couponUser = new CouponUser();
        couponUser.setUserId(userId);
        couponUser.setCouponId(coupon.getCouponId());
        couponUser.setStatus(1);
        couponUser.setReceiveTime(nowTime);
        // 生效时间类型为固定时间
        if (Objects.equals(coupon.getValidTimeType(), ValidTimeType.FIXED.value())) {
            couponUser.setUserStartTime(coupon.getStartTime());
            couponUser.setUserEndTime(coupon.getEndTime());
        }
        // 生效时间类型为领取后生效
        else {
            if (Objects.isNull(coupon.getAfterReceiveDays())) {
                coupon.setAfterReceiveDays(0);
            }
            if (Objects.isNull(coupon.getValidDays())) {
                coupon.setValidDays(0);
            }
            couponUser.setUserStartTime(new Date());
            couponUser.setUserEndTime(DateUtils.addDays(DateUtil.endOfDay(new Date()), coupon.getAfterReceiveDays()));
        }
        couponUser.setIsDelete(0);
        return couponUser;
    }

    /**
     * 校验优惠券的数据
     * @param couponDTO 优惠券信息
     */
    private void checkCoupon(CouponDTO couponDTO) {
        if (Objects.equals(SuitableProdType.ASSIGN_SPU.value(), couponDTO.getSuitableProdType()) && CollUtil.isEmpty(couponDTO.getSpuIds())) {
            throw new LuckException("商品列表不能为空");
        }
        if (couponDTO.getLimitNum() <= 0) {
            throw new LuckException("优惠券限领数量需大于0");
        }

        if (Objects.isNull(couponDTO.getCashCondition()) || couponDTO.getCashCondition() <= 0) {
            throw new LuckException("使用条件不能为空，且大于0的整数");
        }
        if(Objects.equals(couponDTO.getCouponType(), CouponType.C2M.value())) {
            if (Objects.isNull(couponDTO.getReduceAmount()) || couponDTO.getReduceAmount() <= 0) {
                throw new LuckException("减免金额不能为空，且大于0的整数");
            }
            if (couponDTO.getCashCondition() <= couponDTO.getReduceAmount()) {
                throw new LuckException("减免金额必须小于使用条件");
            }
        } else if (Objects.equals(couponDTO.getCouponType(), CouponType.C2D.value())) {
            if (Objects.isNull(couponDTO.getCouponDiscount()) || couponDTO.getCouponDiscount() < CouponConstant.MIN_COUPON_DISCOUNT
                    || couponDTO.getCouponDiscount() > CouponConstant.MAX_COUPON_DISCOUNT) {
                throw new LuckException("折扣额度不能为空，且大于等于" + CouponConstant.MIN_COUPON_DISCOUNT + "小于等于" + CouponConstant.MAX_COUPON_DISCOUNT);
            }
        }

        boolean dateIsNull = Objects.isNull(couponDTO.getAfterReceiveDays()) || Objects.isNull(couponDTO.getValidDays());
        if (Objects.equals(couponDTO.getValidTimeType(), ValidTimeType.RECEIVE.value()) && dateIsNull) {
            throw new LuckException("请输入时间范围");
        } else if (Objects.equals(couponDTO.getValidTimeType(), ValidTimeType.FIXED.value())) {
            if (Objects.isNull(couponDTO.getStartTime()) || Objects.isNull(couponDTO.getEndTime())) {
                throw new LuckException("请输入时间范围");
            }
            if (couponDTO.getStartTime().getTime() > couponDTO.getEndTime().getTime()) {
                throw new LuckException("开始时间需大于结束时间");
            }
        }
    }


    /**
     * 清除满减关联的商品缓存
     * @param suitableSpuType
     */
    private void removeSpuCache(Integer suitableSpuType, Long shopId, List<Long> spuIds) {
        spuFeignClient.removeSpuActivityCache(shopId, spuIds);
    }
}
