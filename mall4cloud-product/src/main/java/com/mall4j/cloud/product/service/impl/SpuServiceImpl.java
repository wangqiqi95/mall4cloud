package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.feign.ChannelsSpuFeignClient;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.discount.feign.DiscountFeignClient;
import com.mall4j.cloud.api.distribution.feign.DistributionFeignClient;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.flow.feign.FlowFeignClient;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.OpenCommodityDTO;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.constant.ShopStatus;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopDetailVO;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.dto.*;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.product.vo.ScoreProductVO;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.api.product.vo.StdPushSpuVO;
import com.mall4j.cloud.api.seckill.feign.SeckillFeignClient;
import com.mall4j.cloud.api.seckill.vo.SeckillApiVO;
import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.bo.*;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.*;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import com.mall4j.cloud.common.product.vo.app.SpuAttrValueAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuAdminVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtils;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.constant.SpuStatus;
import com.mall4j.cloud.product.dto.*;
import com.mall4j.cloud.product.mapper.CategoryNavigationRelationMapper;
import com.mall4j.cloud.product.mapper.CategoryNavigationSpuRelationMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Slf4j
@Service
@RefreshScope
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {

    @Value("${mall4cloud.product.priceSyncFlag:1}")
    @Setter
    private int priceSyncFlag=1;

    @Value("${mall4cloud.product.proSyncFlag:1}")
    @Setter
    private int proSyncFlag=1;

    @Value("${mall4cloud.product.stockSyncFlag:1}")
    @Setter
    private int stockSyncFlag=1;

    @Value("${mall4cloud.product.redisCacheSpu:120}")
    @Setter
    private int REDIS_CACHE_SPU=120;

    @Value("${mall4cloud.product.redisCacheSpuFlag:false}")
    @Setter
    private boolean REDIS_CACHE_SPU_FLAG=false;

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuOfflineService spuOfflineService;
    @Autowired
    private SpuDetailService spuDetailService;
    @Autowired
    private SpuExtensionService spuExtensionService;
    @Autowired
    private SpuCommService spuCommService;
    @Autowired
    private SpuTagReferenceService spuTagReferenceService;
    @Autowired
    private SpuAttrValueService spuAttrValueService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuLangService spuLangService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryShopService categoryShopService;
    @Autowired
    private CategoryBrandService categoryBrandService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandShopService brandShopService;
    @Autowired
    private FlowFeignClient flowFeignClient;
    @Autowired
    private DiscountFeignClient discountFeignClient;
    @Autowired
    private CouponFeignClient couponFeignClient;
    @Autowired
    private OfflineHandleEventFeignClient offlineHandleEventFeignClient;
    @Autowired
    private GroupFeignClient groupFeignClient;
    @Autowired
    private SeckillFeignClient seckillFeignClient;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private DistributionFeignClient distributionFeignClient;
    @Autowired
    private SkuStoreService skuStoreService;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private SkuStockService skuStockService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private SpuSkuAttrValueService spuSkuAttrValueService;
    @Autowired
    private SpuStoreService spuStoreService;
    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;
    @Autowired
    private TagActivityService tagActivityService;
    @Autowired
    private ExcelUploadFeignClient excelUploadFeignClient;
    @Autowired
    private SpuPriceService spuPriceService;
    @Autowired
    private SpuSkuPricingPriceService spuSkuPricingPriceService;

    @Autowired
    private OnsMQTemplate scoreProductAsyncTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ChannelsSpuFeignClient channelsSpuFeignClient;
    @Autowired
    OnsMQTemplate ecZeroSetProductStockTemplate;

    @Autowired
    private OnsMQTemplate syncZhlsProductTemplate;

    @Autowired
    private ProductConfigProperties productConfigProperties;

    @Autowired
    private CategoryNavigationRelationMapper categoryNavigationRelationMapper;

    @Autowired
    private CategoryNavigationSpuRelationMapper categoryNavigationSpuRelationMapper;

    @Override
    public PageVO<SpuVO> page(PageDTO pageDTO, SpuPageSearchDTO spuDTO) {
        PageVO<SpuVO> spuPage = PageUtil.doPage(pageDTO, () -> spuMapper.listSpu(spuDTO));
        return spuPage;
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.SPU_KEY, key = "#spuId", sync = true)
    public SpuVO getBySpuId(Long spuId) {
        SpuVO spu = spuMapper.getBySpuId(spuId);
        if (Objects.isNull(spu)) {
            throw new LuckException(ResponseEnum.SPU_NOT_EXIST);
        }
        return spu;
    }

    @Override
//    @Cacheable(cacheNames = CacheNames.SPU_EXTENSION_KEY, key = "#spuId", sync = true)
    public SpuExtension getSpuExtension(Long spuId) {
        return spuExtensionService.getBySpuId(spuId);
    }

    @Override
    public void batchRemoveSpuActivityCache(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return;
        }
        RedisUtil.deleteBatch(CacheNames.SPU_ACTIVITY_KEY, spuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void changeSpuStatus(Long spuId, Integer status) {
        batchChangeSpuStatus(Collections.singletonList(spuId), status);
//        spuMapper.changeSpuStatus(spuId, status);
//        if (Objects.equals(status, StatusEnum.DISABLE.value()) || Objects.equals(status, StatusEnum.OFFLINE.value())) {
//            // 删掉满减、优惠券的商品关联信息
//            handleSpuStatusChange(Collections.singletonList(spuId));
//            seckillFeignClient.offlineSeckillBySpuIds(Collections.singletonList(spuId));
//        }
    }

    @Override
    public void batchChangeSpuStatus(List<Long> spuIds, Integer status) {
        log.info("商品上下架--批量更新商品上下架 操作人【{}】 更新状态【{}】 更新商品id【{}】",
                Optional.ofNullable(AuthUserContext.get()).orElse(new UserInfoInTokenBO()).getUsername(),status,JSON.toJSONString(spuIds));
        spuMapper.batchChangeSpuStatus(spuIds, status);
        // 更新品牌信息
        List<Long> brandIds = brandService.listBrandIdBySpuIds(spuIds);
        brandService.updateSpuCountByBrandIds(brandIds);
        if (Objects.equals(status, StatusEnum.DISABLE.value()) || Objects.equals(status, StatusEnum.OFFLINE.value())) {
            // 更新商品分组信息
            handleStatusChange(spuIds);
            // T删掉满减、优惠券的商品关联信息
            handleSpuStatusChange(spuIds);
            // 下线掉关联商品的活动 (在线下活动之前已经修改了商品的状态，下面的方法就查询不到已有的商品了，所以就需要带着修改后的商品去状态去查询)
            spuOfflineService.offlineSpuStatusAndActivity(1, spuIds, null, null, status);
        }

        //有数同步上下架
        if(productConfigProperties.getSyncZhlsData()) {
            if (SpuStatus.PUT_SHELF.value().equals(status)) {
                SendResult sendResult = syncZhlsProductTemplate.syncSend(spuIds, RocketMqConstant.SYNC_ZHLS_PUT_SHELF_TAG);
                log.info("商品上架同步有数,msgId:{},入参：{}", sendResult.getMessageId(), JSON.toJSONString(spuIds));
            } else if (SpuStatus.OFF_SHELF.value().equals(status)) {
                SendResult sendResult = syncZhlsProductTemplate.syncSend(spuIds, RocketMqConstant.SYNC_ZHLS_PUT_OFF_SHELF_TAG);
                log.info("商品下架同步有数,msgId:{},入参：{}", sendResult.getMessageId(), JSON.toJSONString(spuIds));
            }
        }

    }

    private void handleSpuStatusChange(List<Long> spuIds) {
        List<Long> shopIds;
        if (Objects.equals(Optional.ofNullable(AuthUserContext.get()).orElse(new UserInfoInTokenBO()).getTenantId(), Constant.PLATFORM_SHOP_ID)) {
            shopIds = new ArrayList<>();
        } else {
            shopIds = spuMapper.spuShopIdsBySpuIds(spuIds);
        }
        if (CollUtil.isNotEmpty(spuIds)) {
            ServerResponseEntity<Void> discountResponseEntity = discountFeignClient.handleSpuOffline(spuIds, shopIds);
            if (discountResponseEntity.isFail()) {
                throw new LuckException(discountResponseEntity.getMsg());
            }
            ServerResponseEntity<Void> couponResponseEntity = couponFeignClient.handleSpuOffline(spuIds, shopIds);
            if (couponResponseEntity.isFail()) {
                throw new LuckException(couponResponseEntity.getMsg());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpuDTO spuDTO) {
        checkSaveOrUpdateInfo(spuDTO, true);
        Spu spu = mapperFacade.map(spuDTO, Spu.class);
        // 1.保存商品信息
        spu.setShopId(Constant.PLATFORM_SHOP_ID);
        spu.setBrandId(Objects.equals(spuDTO.getBrandId(), 0L) ? null : spuDTO.getBrandId());
        spu.setScoreFee(Objects.isNull(spuDTO.getScoreFee()) ? Constant.ZERO_LONG : spuDTO.getScoreFee());
        spuMapper.saveSpu(spu);
        // 2.保存商品属性信息
//        spuAttrValueService.save(spu.getSpuId(), spu.getCategoryId(), spuDTO.getSpuAttrValues());
        // 3.国际化信息、商品详情
        spuDTO.setSpuId(spu.getSpuId());
        loadSpuLangInfo(spuDTO, Boolean.TRUE);
        // 4.商品扩展信息
        SpuExtension spuExtension = new SpuExtension();
        spuExtension.setSpuId(spu.getSpuId());
        spuExtension.setActualStock(spuDTO.getTotalStock());
        spuExtension.setStock(spuDTO.getTotalStock());
        spuExtensionService.save(spuExtension);
        // 5.保存sku信息
        skuService.save(spu.getSpuId(), spuDTO.getSkuList());
        // 6.更新品牌信息
        brandService.updateSpuCount(spu.getBrandId());
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_KEY, key = "#spuDTO.spuId")
    @Transactional(rollbackFor = Exception.class)
    public void updateSpu(SpuDTO spuDTO) {

        SpuVO spuDb = this.getBySpuId(spuDTO.getSpuId());
        if (!Objects.equals(spuDb.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }

        log.info("进入商品详情发布--> 商品id【{}】 操作人【{}】 工号【{}】",
                spuDTO.getSpuId(),
                AuthUserContext.get().getUsername(),
                AuthUserContext.get().getUserId());

        //商品允许编辑项
        //商品分类 商品导航 排序 商品轮播图 商品视频 销售价 运费设置 商品标题 ，商品卖点
        checkSaveOrUpdateInfo(spuDTO, false);
//        Spu spu = mapperFacade.map(spuDTO, Spu.class);
//        SpuVO spuDb = spuMapper.getBySpuId(spu.getSpuId());
//        spu.setBrandId(Objects.isNull(spuDTO.getBrandId()) ? Constant.ZERO_LONG : spuDTO.getBrandId());
        // 1.修改商品信息
        this.lambdaUpdate().set(Spu::getPrimaryCategoryId, spuDTO.getPrimaryCategoryId())
                .set(Spu::getSecondaryCategoryId, spuDTO.getSecondaryCategoryId())
                .set(Spu::getCategoryId, spuDTO.getCategoryId())
                .set(Spu::getShopCategoryId, spuDTO.getShopCategoryId())
                .set(Spu::getSeq, spuDTO.getSeq())
                .set(Spu::getMainImgUrl, spuDTO.getMainImgUrl())
                .set(Spu::getVideo, spuDTO.getVideo())
                .set(StrUtil.isNotBlank(spuDTO.getImgUrls()), Spu::getImgUrls, spuDTO.getImgUrls())
                .set(StrUtil.isNotBlank(spuDTO.getVideo()), Spu::getVideo, spuDTO.getVideo())
                .set(Spu::getDeliveryMode, spuDTO.getDeliveryMode())
                .set(Spu::getDeliveryTemplateId, spuDTO.getDeliveryTemplateId())
                .set(Spu::getSellingPoint, spuDTO.getSellingPoint())
                .set(Spu::getPriceFee, spuDTO.getPriceFee())
                .set(Spu::getName, spuDTO.getName())
                .set(Spu::getIphStatus, 1)
                .set(StrUtil.isNotBlank(spuDTO.getAbbr()),Spu::getAbbr,spuDTO.getAbbr())
                .eq(Spu::getSpuId, spuDTO.getSpuId())
                .update();

        spuStoreService.lambdaUpdate().set(SpuStore::getPriceFee, spuDTO.getPriceFee())
                .eq(SpuStore::getSpuId, spuDTO.getSpuId()).update();
        //更新商品详情
        SpuDetail spuDetail = spuDetailService.getOne(new LambdaQueryWrapper<SpuDetail>()
                .eq(SpuDetail::getSpuId, spuDTO.getSpuId()), false);
        if(Objects.isNull(spuDetail)){
            spuDetail=new SpuDetail();
            spuDetail.setSpuId(spuDTO.getSpuId());
            spuDetail.setDetail(spuDTO.getDetail());
            spuDetailService.save(spuDetail);
        }else{
            spuDetailService.lambdaUpdate()
                    .set(SpuDetail::getDetail, spuDTO.getDetail())
                    .eq(SpuDetail::getSpuId, spuDTO.getSpuId())
                    .update();
        }

        // 更新商品库存及sku信息
        updateSku(spuDTO);

        //更新商品基础信息
        spuAttrValueService.updateSpuAttrValue(spuDTO.getSpuAttrValues(), spuDTO.getSpuId());

        //重置商品导航商品排序(排序至最前)
        if(Objects.nonNull(spuDTO.getShopCategoryId()) && (Objects.isNull(spuDb.getShopCategoryId()) || spuDb.getShopCategoryId()!=spuDTO.getShopCategoryId())){
            log.info("商品详情编辑->重置商品导航商品排序 spuId:{} shopCategoryId:{}",spuDb.getSpuId(),spuDTO.getShopCategoryId());
            SpuCategoryUpdateDTO updateDTO=new SpuCategoryUpdateDTO();
            updateDTO.setCategoryId(spuDTO.getShopCategoryId());
            updateDTO.setSpuIdList(ListUtil.toList(spuDTO.getSpuId()));
            updateShopCategorys(updateDTO);
        }

        // 批量删除与sku关联的缓存信息
        RedisUtil.deleteBatch(Arrays.asList(CacheNames.SKU_WITH_ATTR_LIST_KEY +CacheNames.UNION+ spuDTO.getSpuId()));
    }

    /**
     * @param
     */
    private void loadSpuLangInfo(SpuDTO spuDTO, Boolean isSave) {
        List<SpuDetail> spuDetailList = new ArrayList<>();
        List<SpuLang> spuLangList = new ArrayList<>();
        boolean dataCorrect = false;
        for (SpuLangDTO spuLangDTO : spuDTO.getSpuLangList()) {
            SpuLang spuLang = mapperFacade.map(spuLangDTO, SpuLang.class);
            spuLang.setSpuId(spuDTO.getSpuId());
            boolean isBlank = StrUtil.isBlank(spuLang.getSpuName()) && StrUtil.isBlank(spuLang.getSellingPoint());
            if (!isBlank) {
                spuLangList.add(spuLang);
            }
            if (StrUtil.isNotBlank(spuLangDTO.getDetail())) {
                SpuDetail spuDetail = new SpuDetail();
                spuDetail.setSpuId(spuDTO.getSpuId());
                spuDetail.setLang(spuLangDTO.getLang());
                spuDetail.setDetail(spuLangDTO.getDetail());
                spuDetailList.add(spuDetail);
            }
            if (!isBlank && Objects.equals(spuLang.getLang(), Constant.DEFAULT_LANG)) {
                dataCorrect = true;
            }

        }

        // 商品名称、卖点，商品详情，是否包含默认语言信息
        if (CollUtil.isEmpty(spuLangList) || !dataCorrect) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        if (isSave) {
            if (spuDetailList.size() != 0) {
                spuDetailService.batchSave(spuDetailList);
            }
            if (spuLangList.size() != 0) {
                spuLangService.batchSave(spuLangList);
            }
            return;
        }
        SpuVO spuDb = getBySpuId(spuDTO.getSpuId());
        spuDetailService.update(spuDetailList, spuDb.getDetailList(), spuDb.getSpuId());
        spuLangService.update(spuLangList, spuDb.getSpuId());
    }


    /**
     * 更新sku列表信息
     *
     * @param spuDTO 商品信息
     */
    private void updateSku(SpuDTO spuDTO) {

        log.info("进入商品详情发布-->更新商品库存及sku信息1 商品id【{}】 操作人【{}】 工号【{}】",
                spuDTO.getSpuId(),
                AuthUserContext.get().getUsername(),
                AuthUserContext.get().getUserId());

        boolean notAllChange = false;
        Integer stock = 0;
        for (SkuDTO skuDTO : spuDTO.getSkuList()) {
            stock = Objects.isNull(skuDTO.getStock()) ? 0 : skuDTO.getStock() + stock;
            if (!notAllChange && Objects.nonNull(skuDTO.getSkuId())) {
                notAllChange = true;
            }
            if (Objects.isNull(skuDTO.getSkuId())) {
                if (spuDTO.getSpuType() != null && spuDTO.getSpuType().equals(SpuType.GROUP.value())) {
                    // 商品正在参与拼团活动，无法新增规格
                    throw new LuckException("商品正在参与拼团活动，无法新增规格");
                }
                if (spuDTO.getSpuType() != null && spuDTO.getSpuType().equals(SpuType.SECKILL.value())) {
                    // 商品正在参与秒杀活动，无法新增规格
                    throw new LuckException("商品正在参与秒杀活动，无法新增规格");
                }
            }
        }
        // 商品库存、商品sku
        SpuExtension spuExtension = new SpuExtension();
        spuExtension.setSpuId(spuDTO.getSpuId());
        log.info("进入商品详情发布-->更新商品库存及sku信息1 notAllChange【{}】 商品id【{}】 操作人【{}】 工号【{}】",
                notAllChange,
                spuDTO.getSpuId(),
                AuthUserContext.get().getUsername(),
                AuthUserContext.get().getUserId());
        // sku更新
        if (notAllChange) {
            skuService.update(spuDTO.getSpuId(), spuDTO.getSkuList());
            spuExtensionService.updateStock(spuDTO.getSpuId());
            return;
        }
        // sku全部发生改变，删掉旧的数据，然后保存新的数据
        spuExtension.setStock(stock);
        spuExtension.setActualStock(stock);
        spuExtension.setLockStock(0);
        spuExtensionService.update(spuExtension);
        skuService.deleteBySpuId(spuDTO.getSpuId());
        skuService.save(spuDTO.getSpuId(), spuDTO.getSkuList());
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_KEY, key = "#spuId")
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long spuId) {
        SpuVO spuVO = getBySpuId(spuId);
        if (Objects.isNull(spuVO) || Objects.equals(spuVO.getStatus(), StatusEnum.DELETE.value())) {
            throw new LuckException("商品不存在或者已被删除！");
        }
        // 删除商品、sku信息(逻辑删除)
        spuMapper.updateStatusBySpuId(spuId);
        // 删除关联活动的信息
        handleSpuStatusChange(Collections.singletonList(spuId));
        // 更新品牌信息
        brandService.updateSpuCount(spuVO.getBrandId());
        // 移除关联的分组
        handleStatusChange(Collections.singletonList(spuId));
        // 清除商品的统计数据
        flowFeignClient.deleteSpuDataBySpuId(spuId);
    }

    @Override
    public void updateSpuOrSku(SpuDTO spuDTO) {
        Spu spu = new Spu();
        spu.setSpuId(spuDTO.getSpuId());
        // 修改商品名
        if (CollUtil.isNotEmpty(spuDTO.getSpuLangList())) {
            for (SpuLangDTO spuLangDTO : spuDTO.getSpuLangList()) {
                spuLangDTO.setSpuId(spuDTO.getSpuId());
            }
            spuLangService.update(mapperFacade.mapAsList(spuDTO.getSpuLangList(), SpuLang.class), spuDTO.getSpuId());
        }
        spu.setSeq(spuDTO.getSeq());
        if (CollUtil.isNotEmpty(spuDTO.getSkuList())) {
            skuService.updateAmountOrStock(spuDTO);
            spuExtensionService.updateStock(spuDTO.getSpuId());
        }
        spu.setPriceFee(spuDTO.getPriceFee());
        spuMapper.updateSpu(spu);
    }

    @Override
    public void updateSpuUpdateTime(List<Long> spuIds, List<Long> categoryIds, List<Long> shopIds) {
        if (CollUtil.isEmpty(spuIds) && CollUtil.isEmpty(categoryIds) && CollUtil.isEmpty(shopIds)) {
            return;
        }
        spuMapper.updateSpuUpdateTime(spuIds, categoryIds, shopIds);
    }

    @Override
    public EsProductBO loadEsProductBO(Long spuId) {
        // 获取商品
        EsProductBO esProductBO = spuMapper.loadEsProductBO(spuId);
        if (Objects.equals(esProductBO.getSpuStatus(), StatusEnum.ENABLE.value())) {
            esProductBO.setAppDisplay(Boolean.TRUE);
        } else {
            esProductBO.setAppDisplay(Boolean.FALSE);
        }
        // 获取分类、品牌、评论、库存、属性信息
        setEsProductInfo(esProductBO);
        // 团购活动商品，添加活动时间
        if (Objects.equals(esProductBO.getSpuType(), SpuType.GROUP.value())) {
            ServerResponseEntity<Date> responseEntity = groupFeignClient.getActivityStartTime(esProductBO.getActivityId());
            if (Objects.nonNull(responseEntity.getData())) {
                esProductBO.setActivityStartTime(responseEntity.getData().getTime());
            }
        }
        // 秒杀活动商品，添加活动时间,秒杀分类,并添加批次信息
        if (Objects.equals(esProductBO.getSpuType(), SpuType.SECKILL.value())) {
            ServerResponseEntity<SeckillApiVO> responseEntity = seckillFeignClient.getSeckillInfoById(esProductBO.getActivityId());
            SeckillApiVO seckillApiVO = responseEntity.getData();
            esProductBO.setActivityStartTime(seckillApiVO.getStartTime().getTime());
            esProductBO.setSelectedLot(seckillApiVO.getSelectedLot());
            esProductBO.setSeckillCategoryId(seckillApiVO.getCategoryId());
        }
        // 分销信息，添加是否属于分销商品，添加分销参数
        try {
            ServerResponseEntity<DistributionSpuVO> distributionRes = distributionFeignClient.getBySpuId(spuId);
            if (!distributionRes.isSuccess()) {
                esProductBO.setDistributionSpu(Boolean.FALSE);
                return esProductBO;
            }
            DistributionSpuVO distributionProdVO = distributionRes.getData();
            if (Objects.isNull(distributionProdVO)) {
                esProductBO.setDistributionSpu(Boolean.FALSE);
            } else {
                esProductBO.setDistributionSpu(Boolean.TRUE);
                EsDistributionInfoBO esDistributionInfoBO = new EsDistributionInfoBO();
                esDistributionInfoBO.setDistributionSpuId(distributionProdVO.getDistributionSpuId());
                esDistributionInfoBO.setAwardMode(distributionProdVO.getAwardMode());
                esDistributionInfoBO.setState(distributionProdVO.getState());
                esDistributionInfoBO.setParentAwardSet(distributionProdVO.getParentAwardSet());
                esDistributionInfoBO.setAwardNumbers(distributionProdVO.getAwardNumbers());
                esDistributionInfoBO.setParentAwardNumbers(distributionProdVO.getParentAwardNumbers());
                esProductBO.setDistributionInfo(esDistributionInfoBO);
            }
        } catch (Exception e) {
            return esProductBO;
        }
        return esProductBO;
    }

    /**
     * 处理分类数据
     *
     * @param category
     * @return
     */
    private EsCategoryBO handleCategory(CategoryVO category) {
        EsCategoryBO categoryBO = new EsCategoryBO();
        categoryBO.setCategoryId(category.getCategoryId());
        Map<Integer, String> categoryMap = category.getCategoryLangList().stream().collect(Collectors.toMap(CategoryLangVO::getLang, CategoryLangVO::getName));
        categoryBO.setCategoryNameZh(categoryMap.get(LanguageEnum.LANGUAGE_ZH_CN.getLang()));
        categoryBO.setCategoryNameEn(categoryMap.get(LanguageEnum.LANGUAGE_EN.getLang()));
        return categoryBO;
    }

    @Override
    public List<Long> getSpuIdsBySpuUpdateDTO(List<Long> shopCategoryIds, List<Long> categoryIds, Long brandId, Long shopId) {
        if (CollUtil.isEmpty(shopCategoryIds) && CollUtil.isEmpty(categoryIds) && Objects.isNull(brandId) && Objects.isNull(shopId)) {
            return new ArrayList<>();
        }
        return spuMapper.getSpuIdsBySpuUpdateDTO(shopCategoryIds, categoryIds, brandId, shopId);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SPU_ACTIVITY_KEY, key = "#spuId", sync = true)
    public SpuActivityAppVO spuActivityBySpuId(Long shopId, Long spuId) {
        SpuActivityAppVO spuActivity = new SpuActivityAppVO();
        // 满减活动
        ServerResponseEntity<List<SpuDiscountAppVO>> discountResponse = discountFeignClient.spuDiscountList(shopId, spuId);
        if (!Objects.equals(discountResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(discountResponse.getMsg());
        }
        spuActivity.setDiscountList(discountResponse.getData());

        // 优惠券
        ServerResponseEntity<List<SpuCouponAppVO>> couponResponse = couponFeignClient.couponOfSpuDetail(shopId, spuId);
        if (!Objects.equals(couponResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(couponResponse.getMsg());
        }
        spuActivity.setCouponList(couponResponse.getData());
        return spuActivity;
    }

    @Override
    public List<Long> listSpuIdsByShopId(Long shopId) {
        return spuMapper.getSpuIdsBySpuUpdateDTO(null, null, null, shopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offlineSpuByShopId(Long shopId) {
        spuMapper.offlineSpuByShopId(shopId);
        // 查询出秒杀or团购的商品进行下线
        // 下线掉关联商品的活动
        spuOfflineService.offlineSpuStatusAndActivity(3, null, shopId, null, null);
    }

    @Override
    public List<SpuVO> list(SpuPageSearchDTO spu) {
        return spuMapper.listSpu(spu);
    }

    @Override
    public void updateSpu(Spu spu) {
        spuMapper.updateSpu(spu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void offline(OfflineHandleEventDTO offlineHandleEventDto) {
        OfflineHandleEventDTO offlineHandleEvent = new OfflineHandleEventDTO();
        offlineHandleEvent.setHandleId(offlineHandleEventDto.getHandleId());
        offlineHandleEvent.setHandleType(OfflineHandleEventType.PROD.getValue());
        offlineHandleEvent.setOfflineReason(offlineHandleEventDto.getOfflineReason());
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.save(offlineHandleEvent);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新活动状态为下线状态
//        changeSpuStatus(offlineHandleEvent.getHandleId(), StatusEnum.OFFLINE.value());
    }

    @Override
    public OfflineHandleEventVO getOfflineHandleEvent(Long couponId) {
        ServerResponseEntity<OfflineHandleEventVO> offlineHandleEventResponse =
                offlineHandleEventFeignClient.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.PROD.getValue(), couponId);
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
        Long spuId = offlineHandleEventDto.getHandleId();
        // 审核通过
        if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
            changeSpuStatus(spuId, StatusEnum.DISABLE.value());
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            changeSpuStatus(spuId, StatusEnum.OFFLINE.value());
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
        changeSpuStatus(offlineHandleEventDto.getHandleId(), StatusEnum.WAIT_AUDIT.value());
    }

    @Override
    public List<SpuVO> listCanSeckillProd(Long spuId, Long shopId) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(0);
        pageDTO.setPageSize(PageDTO.MAX_PAGE_SIZE);
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setShopId(shopId);
        spuDTO.setSpuId(spuId);
        return spuMapper.listCanSeckillProd(new PageAdapter(pageDTO), spuDTO, I18nMessage.getLang());
    }

//    @Override
//    public PageVO<SpuVO> pageCanSeckillProd(PageDTO pageDTO, SpuDTO spuDTO) {
////        return PageUtil.doPage(pageDTO, () -> spuMapper.listCanSeckillProd(spuDTO));
//        PageVO<SpuVO> pageVO = new PageVO<>();
//        pageVO.setList(spuMapper.listCanSeckillProd(new PageAdapter(pageDTO), spuDTO));
//        pageVO.setTotal(spuMapper.countCanSeckillProd(spuDTO));
//        return pageVO;
//    }

    @Override
    public Integer countByTransportId(Long transportId) {

        return spuMapper.countByTransportId(transportId);
    }

    @Override
    public void changeToNormalSpu(List<Long> spuIds) {
        spuMapper.changeToNormalSpu(spuIds);
    }

    @Override
    public int countByCategoryAndShopId(Long categoryId, Long shopId) {
        return spuMapper.countByCategoryAndShopId(categoryId, shopId);
    }

    @Override
    public void handleStatusChange(List<Long> spuIds) {
        // 删除商品的分组信息
        spuTagReferenceService.deleteSpuData(spuIds);
    }

    @Override
    public Integer countByUserId(Long userId) {
        return spuMapper.countByUserId(userId);
    }

    @Override
    public List<SpuVO> listSpuNameBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return new ArrayList<>();
        }
        List<SpuVO> spuVOList = spuMapper.listSpuNameBySpuIds(spuIds);
        ProductLangUtil.spuList(spuVOList);
        return spuVOList;
    }

    @Override
    public void batchChangeSpuStatusByCidListAndShopId(List<Long> cidList, Integer status, Long shopId) {
        if (Objects.isNull(cidList) || cidList.size() == 0) {
            return;
        }
        List<Long> spuIds = spuMapper.listIdByCidAndShopId(cidList, shopId);
        if (Objects.isNull(spuIds) || spuIds.size() == 0) {
            return;
        }
        this.batchChangeSpuStatus(spuIds, status);
    }

    @Override
    public List<SpuVO> listSpuBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return null;
        }
        List<SpuVO> spuList = spuMapper.listSpuBySpuIds(spuIds);
        ProductLangUtil.spuList(spuList);
        if (CollectionUtil.isEmpty(spuList)) {
            return spuList;
        }
        List<Long> shopIds = spuList.stream().map(SpuVO::getShopId).collect(Collectors.toList());
        ServerResponseEntity<List<ShopDetailVO>> shopResponse = shopDetailFeignClient.listByShopIds(shopIds);
        if (!shopResponse.isSuccess()) {
            throw new LuckException(shopResponse.getMsg());
        }
        List<ShopDetailVO> shopDetailList = shopResponse.getData();
        Map<Long, String> shopMap = shopDetailList.stream().collect(Collectors.toMap(ShopDetailVO::getShopId, ShopDetailVO::getShopName));
        for (SpuVO spuVO : spuList) {
            if (!shopMap.containsKey(spuVO.getShopId())) {
                continue;
            }
            spuVO.setShopName(shopMap.get(spuVO.getShopId()));
        }
        return spuList;
    }

    @Override
    public PageVO<SpuVO> pageList(SpuListDTO param) {
        PageVO<SpuVO> spuPage = PageUtil.doPage(param, () -> spuMapper.pageList(param));
        return spuPage;
    }

    /**
     * 根据商品code列表获取商品列表
     *
     * @param spuCodes
     * @return
     */
    @Override
    public List<SpuCodeVo> listSpuBySpuCodes(List<String> spuCodes) {
        if (CollUtil.isEmpty(spuCodes)) {
            return null;
        }
        List<SpuCodeVo> spuCodeVos = new ArrayList<>(spuCodes.size());
        int size = spuCodes.size();
        int inSize = 1000;
        if (size > inSize) {
            int pages = size % inSize == 0 ? size / inSize : size / inSize + 1;
            for (int i = 0; i < pages; i++) {

                int start = i * inSize;
                int end = (i + 1) * inSize;
                end = end > size ? size : end;
                List<String> strings = spuCodes.subList(start, end);
                List<SpuCodeVo> spuCodeVoList = spuMapper.listSpuBySpuCodes(new ArrayList<>(strings));
                if (!CollectionUtils.isEmpty(spuCodeVoList)) {
                    spuCodeVos.addAll(spuCodeVoList);
                }
            }
        } else {
            List<SpuCodeVo> spuCodeVoList = spuMapper.listSpuBySpuCodes(spuCodes);
            if (!CollectionUtils.isEmpty(spuCodeVoList)) {
                spuCodeVos.addAll(spuCodeVoList);
            }
        }
        return spuCodeVos;
    }

    @Override
    public void verifySpuData() {
        Date currentTime = new Date();
        // 获取数据库商品更新的数量
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        Long spuNum = spuMapper.getNotDeleteSpuNum(currentTime);
        productSearchDTO.setPageSize(1);
        productSearchDTO.setPageNum(1);
        // 数量相同，不需要更新
        if (verifySpuNum(currentTime, productSearchDTO, spuNum)) {
            return;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(Constant.MAX_PAGE_SIZE);
        pageDTO.setPageNum(1);
        List<Long> updateList = new ArrayList<>();
        ProductSearchDTO simpleDTO = new ProductSearchDTO();
        while (spuNum > 0) {
            // 每十次循环判断下，数据库与es中的商品数量是否一致了, 一致则不需要再更新
            boolean notUpdate = pageDTO.getPageNum() % 10 == 0 && verifySpuNum(currentTime, productSearchDTO, null) && CollUtil.isEmpty(updateList);
            if (notUpdate) {
                return;
            }
            PageVO<Spu> page = PageUtil.doPage(pageDTO, () -> spuMapper.listNotDeleteSpu(currentTime));
            if (CollUtil.isEmpty(page.getList())) {
                break;
            }
            List<Long> spuIds = new ArrayList<>();
            for (Spu spu : page.getList()) {
                spuIds.add(spu.getSpuId());
            }
            simpleDTO.setSpuIds(spuIds);
            ServerResponseEntity<List<SpuSearchVO>> spuResponseEntity = searchSpuFeignClient.simpleList(simpleDTO);
            if (spuResponseEntity.isFail()) {
                throw new LuckException(spuResponseEntity.getMsg());
            }
            Map<Long, Date> spuMap = spuResponseEntity.getData().stream().collect(Collectors.toMap(SpuSearchVO::getSpuId, SpuSearchVO::getUpdateTime));
            for (Spu spu : page.getList()) {
                boolean isSame = spuMap.containsKey(spu.getSpuId()) && DateUtil.isSameTime(spu.getUpdateTime(), spuMap.get(spu.getSpuId()));
                if (isSame) {
                    continue;
                }
                updateList.add(spu.getSpuId());
            }
            if (updateList.size() > Constant.MAX_DATA_HANDLE_NUM) {
                spuMapper.updateSpuUpdateTime(updateList, null, null);
                updateList.clear();
            }
            spuNum = spuNum - Constant.MAX_PAGE_SIZE;
            pageDTO.setPageNum(pageDTO.getPageNum() + 1);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            spuMapper.updateSpuUpdateTime(updateList, null, null);
        }
    }

    @Override
    public SpuAppVO prodInfo(Long spuId, Long storeId) {
        SpuServiceImpl spuService = (SpuServiceImpl) AopContext.currentProxy();
        // 商品信息
        SpuVO spu = spuService.getBySpuIdAndStoreId(spuId, storeId);
        SpuAppVO spuAppVO = mapperFacade.map(spu, SpuAppVO.class);
        spuAppVO.setSpuName(spu.getName());
        //基础属性
        List<SpuAttrValueAppVO> spuAttrsBySpuId = spuAttrValueService.getSpuAttrValueAppVOBySpuId(spuId);
        spuAppVO.setSpuAttrValues(spuAttrsBySpuId);

        // 商品销量
        SpuExtension spuExtension = spuService.getSpuExtension(spuId);
        spuAppVO.setTotalStock(spuExtension.getStock());
        spuAppVO.setSaleNum(spuExtension.getSaleNum() + spuExtension.getWaterSoldNum());
        spuAppVO.setCommentNum(spuExtension.getCommentNum());
        return spuAppVO;
    }


    @Override
    public void offlineSpuByShopIds(Integer type, List<Long> shopIds) {
        if (CollUtil.isEmpty(shopIds)) {
            return;
        }
        List<Long> spuIds = spuMapper.listSpuIdByShopIdsAndStatus(shopIds, StatusEnum.ENABLE.value());
        if (CollUtil.isEmpty(spuIds)) {
            return;
        }
        spuOfflineService.offlineSpuStatusAndActivity(type, spuIds, null, null, null);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_KEY, key = "#spuId")
    public void toTopBySpuId(Long spuId) {
        spuMapper.toTopBySpuId(spuId);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_KEY, key = "#spuId")
    public void removeSpuCacheBySpuId(Long spuId) {

    }

    @Override
    public PageVO<Spu> pageLibrary(Long storeId, SpuPageSearchDTO searchDTO) {
        Integer pageNum = searchDTO.getPageNum();
        Integer pageSize = searchDTO.getPageSize();


        Page<Spu> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> this.list(searchDTO));

        PageVO<Spu> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(page.getResult());
        return pageVO;
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public void sync(ErpSyncDTO dto) {

        if(proSyncFlag==0){
            log.info("中台商品基础数据同步数据proSyncFlag {}", proSyncFlag);
            return;
        }

        long startTime = System.currentTimeMillis();
        log.info("开始执行中台商品基础数据更新 总条数【{}】", dto.getDtoList().size());

        //筛选同步商品的 商品集合
        List<SpuErpSyncDTO> dtoList = dto.getDtoList();
        List<String> spuCodeList = dtoList.stream().map(SpuErpSyncDTO::getSpuCode).collect(Collectors.toList());
        //筛选同步商品的sku集合
        ArrayList<String> skuCodeList = new ArrayList<>();

        dtoList.forEach(spuErpSyncDTO -> {
            List<String> skuCode = spuErpSyncDTO.getSkuList().stream().map(SkuErpSyncDTO::getSkuCode).collect(Collectors.toList());
            skuCodeList.addAll(skuCode);
        });
        //校验商品信息是否存在
        List<Spu> spuList = this.lambdaQuery().in(Spu::getSpuCode, spuCodeList).list();
        //检验sku信息是否存在
        List<Sku> skuList = skuService.lambdaQuery().in(Sku::getSkuCode, skuCodeList).list();
        Map<String, List<Spu>> spuMap = spuList.stream().collect(Collectors.groupingBy(Spu::getSpuCode));
        Map<String, List<Sku>> skuMap = skuList.stream().collect(Collectors.groupingBy(Sku::getSkuCode));
        Date now = new Date();

        //
        List<Spu> updateSpus=new ArrayList<>();

        dtoList.forEach(spuErpSyncDTO -> {
            List<Spu> spus = spuMap.get(spuErpSyncDTO.getSpuCode());
            Long spuId = null;
            Spu spuContent = null;
            if (CollectionUtil.isEmpty(spus)) {
                //商品不存在 封装商品信息并保存
                Spu spu = new Spu();
                spu.setSpuCode(spuErpSyncDTO.getSpuCode());
                spu.setName(spuErpSyncDTO.getSpuCode());
                spu.setPriceFee(spuErpSyncDTO.getPriceFee());
                spu.setMarketPriceFee(spuErpSyncDTO.getMarketPriceFee());
                spu.setSpuType(0);
                spu.setStatus(0);
                spu.setStyleCode(spuErpSyncDTO.getStyleCode());
                spu.setSex(spuErpSyncDTO.getSex());
//                spu.setShopId(Constant.MAIN_SHOP);
                spu.setShopId(Constant.PLATFORM_SHOP_ID);
                spu.setIphStatus(0);
                spu.setCreateTime(now);
                spu.setUpdateTime(now);
                spu.setChannelName(spuErpSyncDTO.getChannelName());
                spu.setChannelDiscount(spuErpSyncDTO.getDiscount());
                boolean save = this.save(spu);
                //处理sku信息
                spuId = spu.getSpuId();
            } else {
                spuContent = spus.get(0);
                spuId = spus.get(0).getSpuId();
                //需要更新渠道、渠道折扣等级
                Spu spu=new Spu();
                spu.setSpuId(spuId);
                spu.setChannelName(spuErpSyncDTO.getChannelName());
                spu.setChannelDiscount(spuErpSyncDTO.getDiscount());
                spu.setStyleCode(spuErpSyncDTO.getStyleCode());
                spu.setSex(spuErpSyncDTO.getSex());
                updateSpus.add(spu);

            }
            if(CollectionUtil.isNotEmpty(updateSpus)){

                log.info("开始执行中台商品基础数据更新 更新spu渠道信息【{}】",updateSpus);
                spuMapper.batchSpusChannelBySpuId(updateSpus);

                //更新sku（渠道、 折扣等级）
                List<Sku> skuUpdateList = new ArrayList<>();
                Long finalSpuId = spuId;
                spuErpSyncDTO.getSkuList().forEach(skuErpSyncDTO -> {
                    List<Sku> skus = skuMap.get(skuErpSyncDTO.getSkuCode());
                    if (CollectionUtil.isNotEmpty(skus)) {
                        Sku sku = skus.get(0);
                        sku.setSpuId(finalSpuId);
                        sku.setChannelName(skuErpSyncDTO.getChannelName());
                        sku.setChannelDiscount(skuErpSyncDTO.getDiscount());
                        sku.setSex(skuErpSyncDTO.getSex());
                        skuUpdateList.add(sku);
                    }
                });
                log.info("开始执行中台商品基础数据更新 更新spu渠道信息【{}】",skuUpdateList.size());
                skuService.updateBatchById(skuUpdateList);
            }

//            if (Objects.nonNull(spuContent) && Objects.nonNull(spuContent.getIphStatus()) && spuContent.getIphStatus() == 1) {
//                log.info("商品已经爱普货了 不执行往下操作--->" + spuContent.getSpuCode());
//                return;
//            }

            //校验sku信息 并处理
            List<SkuErpSyncDTO> skuErpSyncDTOList = spuErpSyncDTO.getSkuList();
            List<Sku> skuSaveList = new ArrayList<>();
            List<Sku> sku_spuskuattrs = new ArrayList<>();//保存商品规格、属性 spu_sku_attr_value
            List<Sku> skuUpdateList = new ArrayList<>();
            Long finalSpuId = spuId;
            skuErpSyncDTOList.forEach(skuErpSyncDTO -> {
                List<Sku> skus = skuMap.get(skuErpSyncDTO.getSkuCode());
                if (CollectionUtil.isEmpty(skus)) {
                    Sku sku = new Sku();
                    sku.setSpuId(finalSpuId);
                    sku.setName(skuErpSyncDTO.getSkuName());
                    sku.setSkuCode(skuErpSyncDTO.getSkuCode());
                    sku.setPriceFee(skuErpSyncDTO.getPriceFee());
                    sku.setPhPrice(skuErpSyncDTO.getPriceFee());
                    sku.setActivityPrice(skuErpSyncDTO.getPriceFee());
                    sku.setMarketPriceFee(skuErpSyncDTO.getMarketPriceFee());
                    sku.setCreateTime(now);
                    sku.setUpdateTime(now);
                    sku.setColorCode(skuErpSyncDTO.getColorCode());
                    sku.setStatus(skuErpSyncDTO.getStatus());
                    sku.setIntscode(skuErpSyncDTO.getIntsCode());
                    sku.setForcode(skuErpSyncDTO.getForcode());
                    sku.setPriceCode(skuErpSyncDTO.getPriceCode());
                    sku.setStyleCode(skuErpSyncDTO.getStyleCode());
                    sku.setSex(skuErpSyncDTO.getSex());
                    sku.setSizeName(skuErpSyncDTO.getSizeName());
                    sku.setColorName(skuErpSyncDTO.getColorName());
                    sku.setChannelName(skuErpSyncDTO.getChannelName());
                    sku.setChannelDiscount(skuErpSyncDTO.getDiscount());
                    skuSaveList.add(sku);
                } else {
                    Sku sku = skus.get(0);
                    sku.setSpuId(finalSpuId);
                    sku.setName(StrUtil.isNotBlank(sku.getName())?sku.getName():skuErpSyncDTO.getSkuName());
                    sku.setColorCode(skuErpSyncDTO.getColorCode());
                    sku.setStyleCode(skuErpSyncDTO.getStyleCode());
                    sku.setSex(skuErpSyncDTO.getSex());
                    sku.setSizeName(skuErpSyncDTO.getSizeName());
                    sku.setColorName(skuErpSyncDTO.getColorName());
                    sku.setChannelName(skuErpSyncDTO.getChannelName());
                    sku.setChannelDiscount(skuErpSyncDTO.getDiscount());
                    sku.setUpdateTime(now);
                    //最初开始同步基础数据没有保存forcode字段，所以这里判断处理更新
                    if(StrUtil.isNotBlank(skuErpSyncDTO.getForcode())){
                        sku.setForcode(skuErpSyncDTO.getForcode());
                    }
                    if(StrUtil.isNotBlank(skuErpSyncDTO.getIntsCode())){
                        sku.setIntscode(skuErpSyncDTO.getIntsCode());
                    }
                    skuUpdateList.add(sku);
                }
            });
            skuService.saveBatch(skuSaveList);
            skuService.updateBatchById(skuUpdateList);

            //只保存新增加的sku到 spu_sku_attr_value（必须）
            sku_spuskuattrs.addAll(skuSaveList);

            skuSaveList.addAll(skuUpdateList);

            //写入sku总库存表数据
            skuStockService.resetStock(skuSaveList);
            //写入门店总店数据
            SpuStore spuStore = spuStoreService.getOne(new LambdaQueryWrapper<SpuStore>().eq(SpuStore::getSpuId, spuId).eq(SpuStore::getStoreId, Constant.MAIN_SHOP), false);
            Sku sku = skuSaveList.stream().min(Comparator.comparing(Sku::getPriceFee)).get();
            if (Objects.isNull(spuStore)) {
                spuStore = new SpuStore();
                spuStore.setSpuId(spuId);
                spuStore.setStoreId(Constant.MAIN_SHOP);
                spuStore.setCreateTime(now);
                spuStore.setUpdateTime(now);
                spuStore.setMarketPriceFee(sku.getMarketPriceFee());
                spuStore.setPriceFee(sku.getPriceFee());
                spuStoreService.save(spuStore);
            } else {
                spuStore.setMarketPriceFee(sku.getMarketPriceFee());
                spuStore.setPriceFee(sku.getPriceFee());
                spuStore.setUpdateTime(now);
                spuStoreService.updateById(spuStore);
            }
            //总店sku数据
            List<SkuStore> list = skuStoreService.lambdaQuery()
//                    .eq(SkuStore::getStatus, 1)
                    .eq(SkuStore::getStoreId, Constant.MAIN_SHOP).eq(SkuStore::getSpuId, spuId).list();
            Map<String, SkuStore> skuStoreMap = list.stream()
                    .collect(Collectors.toMap(SkuStore::getSkuCode,
                            storeCodeVO -> storeCodeVO,(k1, k2)->k1));

            List<SkuStore> skuStoreList = skuSaveList.stream().map(sku1 -> {
                SkuStore skuStore;
                if (skuStoreMap.containsKey(sku1.getSkuCode())) {
                    skuStore = skuStoreMap.get(sku1.getSkuCode());
                } else {
                    skuStore = new SkuStore();
                    BeanUtils.copyProperties(sku1, skuStore);
                    skuStore.setStoreId(Constant.MAIN_SHOP);
                    skuStore.setStock(0);
                }

                return skuStore;
            }).collect(Collectors.toList());
            skuStoreService.saveOrUpdateBatch(skuStoreList);

            spuSkuAttrValueSync(sku_spuskuattrs);

            //设置默认商品库存
            SpuExtension spuExtension = spuExtensionService.getBySpuId(spuId);
            if (Objects.isNull(spuExtension)) {
                spuExtension = new SpuExtension();
                spuExtension.setSpuId(spuId);
                spuExtension.setCreateTime(now);
                spuExtension.setUpdateTime(now);
                spuExtension.setActualStock(0);
                spuExtension.setCommentNum(0);
                spuExtension.setWaterSoldNum(0);
                spuExtension.setLockStock(0);
                spuExtension.setSaleNum(0);
                spuExtension.setStock(0);
                spuExtension.setChannelsStock(0);
                spuExtensionService.save(spuExtension);
            }


            // 批量删除与sku关联的缓存信息
            Map<String, List<Sku>> skuMaps = skuSaveList.stream().collect(Collectors.groupingBy(Sku::getPriceCode));
            for(Map.Entry<String,List<Sku>> entry:skuMaps.entrySet()){
                RedisUtil.deleteBatch(Arrays.asList(CacheNames.SKU_KEY_PRICECODE +CacheNames.UNION+ entry.getKey()));
                entry.getValue().stream().forEach(skuV -> {
                    RedisUtil.deleteBatch(Arrays.asList(CacheNames.SKU_KEY +CacheNames.UNION+ skuV.getSkuCode()));
                });
            }
        });

        log.info("结束执行中台商品基础数据更新，耗时：{}ms   总条数【{}】", System.currentTimeMillis() - startTime, dto.getDtoList().size());
        return;
    }


    private void spuSkuAttrValueSync(List<Sku> skuSaveList) {
        /**p
         * 增加 spu_sku_attr_value 表 数据写入
         * skuSaveList 存入 spu_sku_attr_value 颜色、尺码
         *
         * 取消 默认规格属性参数  spu_sku_attr_value 缺少数据
         * 默认属性： 颜色 ，尺寸
         * 颜色：colorName
         * 规格：sizeName
         * spu_sku_attr_value 对应 每个sku 两条数据
         * attr_name= 规格名 （颜色 ，尺寸）
         * attr_value_name = 具体属性（colorName，sizeName）
         * spu_sku_attr_value 查询条件 ： sku_id，status = 1
         * 中台同步：不存在 - 写入 ，存在 - 不处理
         * 爱铺货  和 中台默认规格 规格名不匹配 需要如何处理更新（每次删除现有 重新插入最新）
         */
        //筛选同步商品的sku集合
        if (CollectionUtil.isNotEmpty(skuSaveList)) {
            ArrayList<Long> skuIdList = new ArrayList<>();
            skuSaveList.forEach(sku -> {
                skuIdList.add(sku.getSkuId());
            });

            //根据sku_id，status = 1 查询是否不存在的写入
            List<SpuSkuAttrValue> spuSkuAttrValues = spuSkuAttrValueService.lambdaQuery()
                    .eq(SpuSkuAttrValue::getStatus, 1)
                    .in(SpuSkuAttrValue::getSkuId, skuIdList).list();

            Map<Long, SpuSkuAttrValue> skuStoreMap = spuSkuAttrValues.stream().collect(Collectors.toMap(SpuSkuAttrValue::getSkuId, spuSkuAttrValue -> spuSkuAttrValue));

            List<SpuSkuAttrValue> saveSpuSkuAttrValues = new ArrayList<>();
            skuSaveList.forEach(sku -> {
                if (!skuStoreMap.containsKey(sku.getSkuId())) {
                    //颜色
                    if (StrUtil.isNotBlank(sku.getColorName())) {
                        SpuSkuAttrValue spuSkuAttrValue = new SpuSkuAttrValue();
                        spuSkuAttrValue.setSkuId(sku.getSkuId());
                        spuSkuAttrValue.setSpuId(sku.getSpuId());
                        spuSkuAttrValue.setCreateTime(new Date());
                        spuSkuAttrValue.setStatus(1);
                        spuSkuAttrValue.setAttrName("颜色");
                        spuSkuAttrValue.setAttrValueName(sku.getColorName());
                        saveSpuSkuAttrValues.add(spuSkuAttrValue);
                    }
                    //尺寸
                    if (StrUtil.isNotBlank(sku.getSizeName())) {
                        SpuSkuAttrValue spuSkuAttrValue = new SpuSkuAttrValue();
                        spuSkuAttrValue.setSkuId(sku.getSkuId());
                        spuSkuAttrValue.setSpuId(sku.getSpuId());
                        spuSkuAttrValue.setCreateTime(new Date());
                        spuSkuAttrValue.setStatus(1);
                        spuSkuAttrValue.setAttrName("尺寸");
                        spuSkuAttrValue.setAttrValueName(sku.getSizeName());
                        saveSpuSkuAttrValues.add(spuSkuAttrValue);
                    }
                }
            });

            //写入 spu_sku_attr_value
            if (CollectionUtil.isNotEmpty(saveSpuSkuAttrValues)) {
                spuSkuAttrValueService.saveBatch(saveSpuSkuAttrValues);
            }
        }

    }

    @Override
    public void priceSyncNew(ErpPriceDTO erpPriceDto) {

        if(priceSyncFlag==0){
            log.info("中台价格同步数据priceSyncFlag:{}", priceSyncFlag);
            return;
        }

        long startTime = System.currentTimeMillis();
        log.info("开始执行中台价格数据更新 总条数【{}】", erpPriceDto.getSkuPriceDTOList().size());
        Date now = new Date();
        //价格类型 1-吊牌价 2-保护价 3-活动价

        //同时需要保存门店sku_store 库存（根据priceCode）数据从官店获取
        spuPriceService.skuStoreSync(erpPriceDto.getSkuPriceDTOList(),now);

        //吊牌价
        List<ErpSkuPriceDTO> marketPriceDTOList = erpPriceDto.getSkuPriceDTOList().stream()
                .filter(skuPriceDTO -> skuPriceDTO.getPriceType() == 1)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(marketPriceDTOList)) {
            spuPriceService.marketPriceSync(marketPriceDTOList,now);
        }

        //活动价
        List<ErpSkuPriceDTO> activityPriceDTOList = erpPriceDto.getSkuPriceDTOList().stream()
                .filter(skuPriceDTO -> skuPriceDTO.getPriceType() == 3)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(activityPriceDTOList)) {
            spuPriceService.activityPriceSync(activityPriceDTOList,now);
        }

        //保护价
        List<ErpSkuPriceDTO> protectPriceDTOList = erpPriceDto.getSkuPriceDTOList().stream()
                .filter(skuPriceDTO -> skuPriceDTO.getPriceType() == 2)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(protectPriceDTOList)) {
            spuPriceService.protectPriceSync(protectPriceDTOList,now);
        }

        //TODO 保存价格日志
        spuPriceService.savePirceLog(erpPriceDto.getSkuPriceDTOList(),now);

        log.info("结束执行中台价格数据更新，耗时：{}ms   总条数【{}】", System.currentTimeMillis() - startTime, erpPriceDto.getSkuPriceDTOList().size());
    }

    private Long getPriceFee(ErpSkuPriceDTO skuPriceDTO) {
        List<Sku> list = skuService.lambdaQuery().eq(Sku::getPriceCode, skuPriceDTO.getPriceCode()).eq(Sku::getStatus, 1).list();
        Sku sku = list.stream().min(Comparator.comparing(Sku::getMarketPriceFee)).get();
        Long marketPrice = sku.getMarketPriceFee();
        if (Objects.nonNull(skuPriceDTO.getMarketPrice())) {
            marketPrice = NumberUtil.min(sku.getMarketPriceFee(), skuPriceDTO.getMarketPrice());
        }

        Long priceFee = marketPrice;
        if (Objects.nonNull(skuPriceDTO.getActivityPrice())) {
            priceFee = marketPrice * 3 / 10;
            priceFee = NumberUtil.max(skuPriceDTO.getActivityPrice(), priceFee);
        }
        return priceFee;
    }

    @Override
    public void stockSync(ErpStockDTO erpStockDTO) {

        if(stockSyncFlag==0){
            log.info("中台库存同步数据stockSyncFlag {}", stockSyncFlag);
            return;
        }

        long startTime = System.currentTimeMillis();
        log.info("开始执行中台库存数据更新 总条数【{}】", erpStockDTO.getStockDTOList().size());

        List<ErpSkuStockDTO> stockDTOList = erpStockDTO.getStockDTOList();
        List<String> storeCodeList = stockDTOList.stream().map(ErpSkuStockDTO::getStoreCode).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(storeCodeList)){//去重复
            storeCodeList=new ArrayList<>(new HashSet<>(storeCodeList));
        }
        List<StoreCodeVO> byStoreCodes = storeFeignClient.findByStoreCodes(storeCodeList);

        Map<String, Long> storeCodeMap = byStoreCodes.stream().collect(Collectors.toMap(StoreCodeVO::getStoreCode,StoreCodeVO::getStoreId,
                (storeCodeVO1,storeCodeVO2) -> storeCodeVO2));

        Map<Long,Long> spuIdMaps=new HashMap<>();
        Set<ScoreProductVO> scoreProductSet = new HashSet<>();

        Date now = new Date();
        Map<String,String> skuStoresInsertMap=new HashMap<>();//去重复
        stockDTOList.forEach(erpSkuStockDTO -> {
            //库存类型(1-共享库存 2-门店库存)
            Sku sku = skuService.getOne(new LambdaQueryWrapper<Sku>().eq(Sku::getSkuCode, erpSkuStockDTO.getSkuCode()), false);
            erpSkuStockDTO.setStoreId(Constant.MAIN_SHOP);
            if(Objects.isNull(sku)){
                erpSkuStockDTO.setRemark("条码未找到");
                return;
            }
            if (erpSkuStockDTO.getStockType() == 1) {//共享库存
                if(!spuIdMaps.containsKey(sku.getSpuId())){
                    spuIdMaps.put(sku.getSpuId(),sku.getSpuId());
                }
                Long skuId = sku.getSkuId();
                //更新总库存及总店库存
                skuStockService.lambdaUpdate().set(SkuStock::getStock, erpSkuStockDTO.getAvailableStockNum())
                        .set(SkuStock::getActualStock, erpSkuStockDTO.getAvailableStockNum())
                        .set(SkuStock::getUpdateTime, now)
                        .eq(SkuStock::getSkuId, skuId)
                        .update();

                int count = skuStockService.count(Wrappers.lambdaQuery(SkuStock.class)
                        .eq(SkuStock::getSkuId, skuId)
                        .apply("channels_stock > stock"));
                if (count > 0) {
//                    channelsSpuFeignClient.zeroSetProductStock(skuId);
                    //如果视频号4.0可售库存大于小程序总库存，这里清空视频号那边的可售库存。
                    ecZeroSetProductStockTemplate.syncSend(skuId);
                }

                SkuStore skuMainStoreDb = skuStoreService.getOne(new LambdaQueryWrapper<SkuStore>()
                                .eq(SkuStore::getSkuCode, erpSkuStockDTO.getSkuCode())
                                .eq(SkuStore::getStoreId, Constant.MAIN_SHOP)
                        , false);
                if (Objects.nonNull(skuMainStoreDb)) {
                    skuStoreService.lambdaUpdate().set(SkuStore::getStock, erpSkuStockDTO.getAvailableStockNum())
                            .set(SkuStore::getUpdateTime, now)
                            .eq(SkuStore::getSkuId, skuId)
//                            .eq(SkuStore::getStatus, 1)
                            .eq(SkuStore::getStoreId, Constant.MAIN_SHOP).update();
                }else{
                    if(!skuStoresInsertMap.containsKey(sku.getSkuCode()+Constant.MAIN_SHOP)){
                        skuStoresInsertMap.put(sku.getSkuCode()+Constant.MAIN_SHOP,sku.getSkuCode());
                        SkuStore skuStoreMainShop = new SkuStore();
                        BeanUtils.copyProperties(sku, skuStoreMainShop);
                        skuStoreMainShop.setStoreId(Constant.MAIN_SHOP);
                        skuStoreMainShop.setStock(erpSkuStockDTO.getAvailableStockNum());
                        skuStoreMainShop.setCreateTime(now);
                        skuStoreMainShop.setUpdateTime(now);
                        skuStoreMainShop.setEm("s");
                        skuStoreService.save(skuStoreMainShop);
                    }
                }

            } else {
                //门店库存
                Long storeId = storeCodeMap.get(erpSkuStockDTO.getStoreCode());
                erpSkuStockDTO.setStoreId(storeId);
                if (Objects.nonNull(storeId)) {
                    //查询门店商品是否存在，不存在复制总店商品信息并新增门店库存
                    SkuStore skuStoreDb = skuStoreService.getOne(new LambdaQueryWrapper<SkuStore>()
                                    .eq(SkuStore::getSkuCode, erpSkuStockDTO.getSkuCode())
                                    .eq(SkuStore::getStoreId, storeId)
                            , false);
                    if (Objects.nonNull(skuStoreDb)) {

                        if(!spuIdMaps.containsKey(skuStoreDb.getSpuId())){
                            spuIdMaps.put(skuStoreDb.getSpuId(),skuStoreDb.getSpuId());
                        }

                        skuStoreService.lambdaUpdate()
                                .set(SkuStore::getStock, erpSkuStockDTO.getAvailableStockNum())
                                .set(SkuStore::getUpdateTime, now)
                                .eq(SkuStore::getSkuCode, erpSkuStockDTO.getSkuCode())
                                .eq(SkuStore::getStoreId, storeId)
//                                .eq(SkuStore::getStatus, 1)
                                .update();
                    } else {
                        SkuStore skuMainStoreDb = skuStoreService.getOne(new LambdaQueryWrapper<SkuStore>()
                                        .eq(SkuStore::getSkuCode, erpSkuStockDTO.getSkuCode())
                                        .eq(SkuStore::getStoreId, Constant.MAIN_SHOP)
                                , false);
                        if (Objects.nonNull(skuMainStoreDb)) {

                            if(!spuIdMaps.containsKey(skuMainStoreDb.getSpuId())){
                                spuIdMaps.put(skuMainStoreDb.getSpuId(),skuMainStoreDb.getSpuId());
                            }
                            //门店
                            if(!skuStoresInsertMap.containsKey(sku.getSkuCode()+storeId)){
                                skuStoresInsertMap.put(sku.getSkuCode()+storeId,sku.getSkuCode());
                                SkuStore skuStore = new SkuStore();
                                BeanUtils.copyProperties(skuMainStoreDb, skuStore, "skuStoreId");
                                skuStore.setStoreId(storeId);
                                skuStore.setStock(erpSkuStockDTO.getAvailableStockNum());
                                skuStore.setPhPrice(0L);
                                skuStore.setPriceFee(sku.getMarketPriceFee());
                                skuStore.setChannelPrice(0L);
                                skuStore.setActivityPrice(0L);
                                skuStore.setCreateTime(now);
                                skuStore.setUpdateTime(now);
                                skuStore.setEm("s");
                                skuStoreService.save(skuStore);
                            }

                        }else{
                            //官店
                            if(!skuStoresInsertMap.containsKey(sku.getSkuCode()+Constant.MAIN_SHOP)){
                                skuStoresInsertMap.put(sku.getSkuCode()+Constant.MAIN_SHOP,sku.getSkuCode());
                                SkuStore skuStoreMainShop = new SkuStore();
                                BeanUtils.copyProperties(sku, skuStoreMainShop);
                                skuStoreMainShop.setStoreId(Constant.MAIN_SHOP);
                                skuStoreMainShop.setStock(0);
                                skuStoreMainShop.setCreateTime(now);
                                skuStoreMainShop.setUpdateTime(now);
                                skuStoreMainShop.setEm("s");
                                skuStoreService.save(skuStoreMainShop);
                            }
                            //门店
                            if(!skuStoresInsertMap.containsKey(sku.getSkuCode()+storeId)){
                                skuStoresInsertMap.put(sku.getSkuCode()+storeId,sku.getSkuCode());
                                SkuStore skuStore = new SkuStore();
                                BeanUtils.copyProperties(sku, skuStore);
                                skuStore.setStoreId(storeId);
                                skuStore.setStock(erpSkuStockDTO.getAvailableStockNum());
                                skuStore.setPriceFee(sku.getMarketPriceFee());
                                skuStore.setPhPrice(0L);
                                skuStore.setChannelPrice(0L);
                                skuStore.setActivityPrice(0L);
                                skuStore.setCreateTime(now);
                                skuStore.setUpdateTime(now);
                                skuStore.setEm("s");
                                skuStoreService.save(skuStore);
                            }
                        }
                    }
                }else{
                    erpSkuStockDTO.setRemark("门店未找到");
                }
                try{
                    //库存数量为正数并且同步状态是 1
                    if( Objects.nonNull(erpSkuStockDTO.getSyncType()) && erpSkuStockDTO.getSyncType()== 1 && erpSkuStockDTO.getAvailableStockNum() > 0 ){
                        ScoreProductVO scoreProductVO = new ScoreProductVO();
                        scoreProductVO.setSpuId(sku.getSpuId());
                        scoreProductVO.setStoreCode(erpSkuStockDTO.getStoreCode());
                        String scoreProductVOstr = Json.toJsonString(scoreProductVO);
                        //缓存中存在就添加
                        if ( StrUtil.isNotBlank(scoreProductVOstr) && redisTemplate.opsForSet().isMember(ProductCacheNames.SCORE_PRODUCT_LIST, scoreProductVOstr)){
                            scoreProductSet.add(scoreProductVO);
                        }

                    }
                }catch (Exception e){
                    log.info("积分礼品库存数据获取失败,异常信息为：{},{}",e,e.getMessage());
                }
            }
        });

        if(CollectionUtil.isNotEmpty(scoreProductSet) && scoreProductSet.size() > 0){
            scoreProductAsyncTemplate.asyncSend(scoreProductSet, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {}
                @Override
                public void onException(OnExceptionContext onExceptionContext) {}
            });
        }

        //处理商品主数据 : spu_extension -->stock 库存
        if(CollectionUtil.isNotEmpty(spuIdMaps)){
            List<Long> spuIds=new ArrayList<>(spuIdMaps.values());
            //统计商品总库存
            List<SpuStockVO>  spuStockVOS=skuStockService.sumStockBySpuIds(spuIds);
            if(CollectionUtil.isNotEmpty(spuStockVOS)){
                List<SpuExtensionStockDTO> spuExtensionStockDTOS=new ArrayList<>();
                spuStockVOS.forEach(item->{
                    SpuExtensionStockDTO spuExtensionStockDTO=new SpuExtensionStockDTO();
                    spuExtensionStockDTO.setSpuId(item.getSpuId());
                    spuExtensionStockDTO.setStock(item.getStock());//累计的总库存
                    spuExtensionStockDTOS.add(spuExtensionStockDTO);
                });
                //TODO 批量更新商品可售库存
                //批量更新商品可售库存
                spuExtensionService.updateStocks(spuExtensionStockDTOS);
            }
        }

        log.info("结束执行中台库存数据更新，耗时：{}ms   总条数【{}】", System.currentTimeMillis() - startTime, erpStockDTO.getStockDTOList().size());
    }

    @Override
    public PageVO<SpuPageVO> spuPage(Long storeId, SpuPageSearchDTO searchDTO) {
        Integer pageNum = searchDTO.getPageNum();
        Integer pageSize = searchDTO.getPageSize();

        if(Objects.nonNull(searchDTO.getCusPageSize()) && searchDTO.getCusPageSize()>0){
            pageSize=searchDTO.getCusPageSize();
        }

        ServerResponseEntity<Boolean> responseEntity=storeFeignClient.isInviteStore(storeId);

        searchDTO.setStoreId(storeId);
        Page<SpuPageVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> this.listSpu(searchDTO));

        //商品组件导入排序，按照前端传入spuIds集合顺序排序
        if(CollectionUtil.isNotEmpty(searchDTO.getSpuIds()) && CollectionUtil.isNotEmpty(page.getResult())){
            Map<Long,Integer> spuIdsMap=new LinkedHashMap<>();
            int seq=0;
            for(Long spuId:searchDTO.getSpuIds()){
                seq++;
                spuIdsMap.put(spuId,seq);
            }
            page.getResult().forEach(spuPageVO -> {
                if(spuIdsMap.containsKey(spuPageVO.getSpuId())){
                    spuPageVO.setSeq(spuIdsMap.get(spuPageVO.getSpuId()));
                }
            });
            Collections.sort(page.getResult(), Comparator.comparingInt(SpuPageVO::getSeq));
        }
        if(responseEntity.isSuccess() && responseEntity.getData()){
            page.getResult().forEach(item ->{
                item.setPriceFee(item.getMarketPriceFee());
            });
        }

        PageVO<SpuPageVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(page.getResult());
        return pageVO;
    }

//    @HystrixCommand
    @Override
    public PageVO<SpuAppPageVO> appPage(SpuPageSearchDTO searchDTO) {
        if(CollectionUtil.isNotEmpty(searchDTO.getAttrValues()) && searchDTO.getAttrValues().size()>5){
            throw new LuckException("尺码和颜色筛选条件最多支持5个");
        }
        // 分类导航逻辑
        if (Objects.nonNull(searchDTO.getCategoryNavigationId())) {
            Long categoryNavigationId = searchDTO.getCategoryNavigationId();
            List<Long> descendantCategoryIdList = categoryNavigationRelationMapper.selectListByAncestorCategoryId(categoryNavigationId).stream().map(CategoryNavigationRelation::getDescendantCategoryId).collect(Collectors.toList());
            if (descendantCategoryIdList.isEmpty()) {
                PageVO<SpuAppPageVO> vo = new PageVO<>();
                vo.setPages(0);
                vo.setTotal(0L);
                vo.setList(new ArrayList<>());
                return vo;
            }
            searchDTO.setShopCategoryNavigationIdList(descendantCategoryIdList);
            // 分类赛选逻辑只有一个，原逻辑置空
            searchDTO.setCategoryId(null);
        }

        String rediskey= ProductCacheNames.APP_PRODUCT_LIST+DigestUtil.md5Hex(JSON.toJSONString(searchDTO));
        if(RedisUtil.hasKey(rediskey) && REDIS_CACHE_SPU_FLAG){
            log.info("cache appPage data--> {}",rediskey);
            PageVO<SpuAppPageVO> pageVO=JSONObject.parseObject(RedisUtil.get(rediskey),PageVO.class);
            return pageVO;
        }
        if(StrUtil.isNotBlank(searchDTO.getSearchActivityType())){
            OpenCommodityDTO openCommodityDTO=new OpenCommodityDTO(Arrays.asList(searchDTO.getSearchActivityType().split(",")),searchDTO.getStoreId());
            ServerResponseEntity<List<Long>> response=timeDiscountFeignClient.getOpenCommoditys(openCommodityDTO);
            log.info("营销活动筛选条件 条件:{} 获得数据:{}",JSONObject.toJSONString(openCommodityDTO), JSONObject.toJSONString(response.getData()));
            if(response.isSuccess()){
                if(CollectionUtil.isEmpty(response.getData())){
                    PageVO<SpuAppPageVO> pageVO = new PageVO<>();
                    pageVO.setPages(0);
                    pageVO.setTotal(0L);
                    pageVO.setList(new ArrayList<>());
                    return pageVO;
                }
                searchDTO.setSpuIds(response.getData());
            }
        }
        //查询门店可售商品列表
        Integer pageNum = searchDTO.getPageNum();
        Integer pageSize = searchDTO.getPageSize();
        searchDTO.setIphStatus(1);
        searchDTO.setStatus(1);

        //规格属性筛选
        if(Objects.nonNull(searchDTO.getSpuSearchAttr()) && CollectionUtil.isNotEmpty(searchDTO.getSpuSearchAttr().getSpuGroupAttrs())){
            searchDTO.setAttrFilterSpuIds(getAttrFilterSpuIds(searchDTO.getSpuSearchAttr()));
        }

        Page<SpuAppPageVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> this.appList(searchDTO));

        //查询当前列表所有sku 信息
        List<SpuAppPageVO> result = page.getResult();
        List<Long> spuIdList = result.stream().map(SpuAppPageVO::getSpuId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(spuIdList)) {
            //查询所有sku满足限时调价的集合
            List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, searchDTO.getStoreId());
            List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(appSkuPriceBySkuIdList,SpuSkuPriceDTO.class);
            //增加价取价逻辑(小程序配置的活动价)
            List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(searchDTO.getStoreId(),skuPriceDTOS);
            HashMap<Long, Long> spuMinPriceMap = new HashMap<>();
            if(activityVOList.size()>0){
                Map<Long, List<SkuTimeDiscountActivityVO>> skuMap = activityVOList.stream().collect(Collectors.groupingBy(SkuTimeDiscountActivityVO::getSkuId));
                appSkuPriceBySkuIdList.forEach(appSkuPriceDTO -> {
                    if (Objects.nonNull(skuMap.get(appSkuPriceDTO.getSkuId()))) {
                        appSkuPriceDTO.setPriceFee(skuMap.get(appSkuPriceDTO.getSkuId()).get(0).getPrice());
                    }
                });
            }
            //渠道sku最低价
            Map<Long, List<SkuPriceDTO>> spuMap = appSkuPriceBySkuIdList.stream().collect(Collectors.groupingBy(SkuPriceDTO::getSpuId));
            spuMap.forEach((k, v) -> {
                SkuPriceDTO skuPriceDTO = v.stream().min(Comparator.comparing(SkuPriceDTO::getPriceFee)).get();
                spuMinPriceMap.put(skuPriceDTO.getSpuId(), NumberUtil.min(skuPriceDTO.getPriceFee()));
            });

            Map<Long, Boolean> memberPriceFlagMap = activityVOList.stream().filter(SkuTimeDiscountActivityVO::isMemberPriceFlag)
                    .collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSpuId, SkuTimeDiscountActivityVO::isMemberPriceFlag, (v1, v2) -> v2));

            //与商品售价进行比较，进行赋值
            result.forEach(spuAppPageVO -> {
                //营销标签赋
                spuAppPageVO.setTagActivity(tagActivityService.getTagBySpuId(spuAppPageVO.getSpuId(), searchDTO.getStoreId()));
                Long spuMinPrice = spuMinPriceMap.get(spuAppPageVO.getSpuId());
                if (Objects.nonNull(spuMinPrice)) {
                    spuAppPageVO.setPriceFee(spuMinPrice);
                }

                //增加商品会员日标识
                if(Objects.nonNull(memberPriceFlagMap.get(spuAppPageVO.getSpuId()))){
                    spuAppPageVO.setMemberPriceFlag(true);
                }
            });
        }

        //商品组件导入排序，按照前端传入spuIds集合顺序排序
        if(CollectionUtil.isNotEmpty(searchDTO.getSpuIds()) && CollectionUtil.isNotEmpty(page.getResult())){
            Map<Long,Integer> spuIdsMap=new LinkedHashMap<>();
            int seq=0;
            for(Long spuId:searchDTO.getSpuIds()){
                seq++;
                spuIdsMap.put(spuId,seq);
            }
            page.getResult().forEach(spuPageVO -> {
                if(spuIdsMap.containsKey(spuPageVO.getSpuId())){
                    spuPageVO.setSeq(spuIdsMap.get(spuPageVO.getSpuId()));
                }
            });
            Collections.sort(page.getResult(), Comparator.comparingInt(SpuAppPageVO::getSeq));
        }

        if(Objects.nonNull(searchDTO.getPriceFeeSort())){
            //当前价排序 0：倒序 1：顺序
            if(searchDTO.getPriceFeeSort()==0){
                Collections.sort(result, Comparator.comparingLong(SpuAppPageVO::getPriceFee).reversed());
            }else if(searchDTO.getPriceFeeSort()==1){
                Collections.sort(result, Comparator.comparingLong(SpuAppPageVO::getPriceFee));
            }
        }

        PageVO<SpuAppPageVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(result);

        if(REDIS_CACHE_SPU_FLAG && CollectionUtil.isNotEmpty(result)){
            RedisUtil.set(rediskey,JSONObject.toJSONString(pageVO),REDIS_CACHE_SPU);
        }

        return pageVO;
    }

    /**
     * C端有效spus查询
     * @param searchDTO
     * @return
     */
    @Override
    public List<Long> validSpus(SpuPageSearchDTO searchDTO) {
        //查询门店可售商品列表
        searchDTO.setIphStatus(1);
        searchDTO.setStatus(1);
        List<SpuAppPageVO> spuAppPageVOS=this.appList(searchDTO);

        //商品组件导入排序，按照前端传入spuIds集合顺序排序
        if(CollectionUtil.isNotEmpty(searchDTO.getSpuIds()) && CollectionUtil.isNotEmpty(spuAppPageVOS)){
            Map<Long,Integer> spuIdsMap=new LinkedHashMap<>();
            int seq=0;
            for(Long spuId:searchDTO.getSpuIds()){
                seq++;
                spuIdsMap.put(spuId,seq);
            }
            spuAppPageVOS.forEach(spuPageVO -> {
                if(spuIdsMap.containsKey(spuPageVO.getSpuId())){
                    spuPageVO.setSeq(spuIdsMap.get(spuPageVO.getSpuId()));
                }
            });
            Collections.sort(spuAppPageVOS, Comparator.comparingInt(SpuAppPageVO::getSeq));
        }

        List<Long> results = spuAppPageVOS.stream().map(SpuAppPageVO::getSpuId).collect(Collectors.toList());

        return results;
    }

    @Override
    public SpuVO getBySpuIdAndStoreId(Long spuId, Long storeId) {
        return spuMapper.getBySpuIdAndStoreId(spuId, storeId);
    }

    /**
     * 分页获取分销推广-推荐商品列表
     * @param productSearch
     * @return
     */
    @DS("slave")
//    @HystrixCommand
    @Override
    public PageVO<SpuCommonVO> commonPage(ProductSearchDTO productSearch) {

        String rediskey= ProductCacheNames.APP_PRODUCT_COMMONPAGE+DigestUtil.md5Hex(JSON.toJSONString(productSearch));
        if(RedisUtil.hasKey(rediskey) && REDIS_CACHE_SPU_FLAG){
            log.info("cache commonPage data--> {}",rediskey);
            return RedisUtil.get(rediskey);
        }

        Integer pageNum = productSearch.getPageNum();
        Integer pageSize = productSearch.getPageSize();
        log.info("commonPage 传参storeId【{}】",productSearch.getStoreId());

        if(StrUtil.isNotBlank(productSearch.getSearchActivityType())){
            OpenCommodityDTO openCommodityDTO=new OpenCommodityDTO(Arrays.asList(productSearch.getSearchActivityType().split(",")),productSearch.getStoreId());
            ServerResponseEntity<List<Long>> response=timeDiscountFeignClient.getOpenCommoditys(openCommodityDTO);
            if(response.isSuccess()){
                log.info("营销活动筛选条件 条件:{} 获得数据:{}",JSONObject.toJSONString(openCommodityDTO), JSONObject.toJSONString(response.getData()));
                if(CollectionUtil.isEmpty(response.getData())){
                    PageVO<SpuCommonVO> pageVO = new PageVO<>();
                    pageVO.setPages(0);
                    pageVO.setTotal(0L);
                    pageVO.setList(new ArrayList<>());
                    return pageVO;
                }
                productSearch.setSpuIds(response.getData());
            }
        }

        //规格属性筛选
        if(Objects.nonNull(productSearch.getSpuSearchAttr()) && CollectionUtil.isNotEmpty(productSearch.getSpuSearchAttr().getSpuGroupAttrs())){
            productSearch.setAttrFilterSpuIds(getAttrFilterSpuIds(productSearch.getSpuSearchAttr()));
        }
        Page<SpuCommonVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> this.commonList(productSearch));

        //查询当前列表所有sku 信息
        List<SpuCommonVO> result = page.getResult();
        List<Long> spuIdList = result.stream().map(SpuCommonVO::getSpuId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(spuIdList)) {
            //查询所有sku满足限时调价的集合
            List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, productSearch.getStoreId());
            List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(appSkuPriceBySkuIdList,SpuSkuPriceDTO.class);
            //增加价取价逻辑(小程序配置的活动价)
            List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(productSearch.getStoreId(),skuPriceDTOS);
            HashMap<Long, Long> spuMinPriceMap = new HashMap<>();
            if(activityVOList.size()>0){
                Map<Long, List<SkuTimeDiscountActivityVO>> skuMap = activityVOList.stream().collect(Collectors.groupingBy(SkuTimeDiscountActivityVO::getSkuId));
                appSkuPriceBySkuIdList.forEach(appSkuPriceDTO -> {
                    if (Objects.nonNull(skuMap.get(appSkuPriceDTO.getSkuId()))) {
                        appSkuPriceDTO.setPriceFee(skuMap.get(appSkuPriceDTO.getSkuId()).get(0).getPrice());
                    }
                });
            }
            //取出sku最低价用于展示
            Map<Long, List<SkuPriceDTO>> spuMap = appSkuPriceBySkuIdList.stream().collect(Collectors.groupingBy(SkuPriceDTO::getSpuId));
            spuMap.forEach((k, v) -> {
                SkuPriceDTO skuPriceDTO = v.stream().min(Comparator.comparing(SkuPriceDTO::getPriceFee)).get();
                spuMinPriceMap.put(skuPriceDTO.getSpuId(), NumberUtil.min(skuPriceDTO.getPriceFee()));
            });

            Map<Long, Boolean> memberPriceFlagMap = activityVOList.stream().filter(SkuTimeDiscountActivityVO::isMemberPriceFlag)
                    .collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSpuId, SkuTimeDiscountActivityVO::isMemberPriceFlag, (v1, v2) -> v2));

            //与商品售价进行比较，进行赋值
            result.forEach(spuAppPageVO -> {
                //营销标签赋
//                spuAppPageVO.setTagActivity(tagActivityService.getTagBySpuId(spuAppPageVO.getSpuId(), productSearch.getStoreId()));

                Long spuMinPrice = spuMinPriceMap.get(spuAppPageVO.getSpuId());
                if (Objects.nonNull(spuMinPrice)) {
                    spuAppPageVO.setPriceFee(spuMinPrice);
                }

                //增加商品会员日标识
                if(Objects.nonNull(memberPriceFlagMap.get(spuAppPageVO.getSpuId()))){
                    spuAppPageVO.setMemberPriceFlag(true);
                }
            });
        }

        PageVO<SpuCommonVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(page.getResult());

        if(REDIS_CACHE_SPU_FLAG && CollectionUtil.isNotEmpty(result)){
            RedisUtil.set(rediskey,pageVO,REDIS_CACHE_SPU);
        }

        return pageVO;
    }

    @Override
    public PageVO<SpuCommonVO> couponSearch(ProductSearchDTO productSearch) {
        return this.commonPage(productSearch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void iphSync(SpuDTO spuDTO) {

        log.info("进入爱铺货数据----->iphSync spucode【{}】", spuDTO.getSpuCode());

        //检查Spu 及sku 是否存在
        Spu spu = this.getOne(new LambdaQueryWrapper<Spu>().eq(Spu::getSpuCode, spuDTO.getSpuCode()));
        if (Objects.isNull(spu)) {
            throw new LuckException("商品数据不存在");
        }
        //是否爱普货: 0否 1是
        if (Objects.nonNull(spu.getIphStatus()) && spu.getIphStatus() == 1) {
            log.debug("商品已经爱普货了 不执行往下操作--->" + spu.getSpuCode());
            throw new LuckException("商品已经爱普货");
        }
        long startTime = System.currentTimeMillis();
        log.info("开始执行爱铺货数据更新 spucode【{}】", spuDTO.getSpuCode());

        Date now = new Date();
        //售卖规格
        List<SkuDTO> skuList = spuDTO.getSkuList();
        //展示数据
        Long spuId = spu.getSpuId();
        this.lambdaUpdate().set(Objects.nonNull(spuDTO.getShopCategoryId()), Spu::getShopCategoryId, spuDTO.getShopCategoryId())
                .set(StrUtil.isNotBlank(spuDTO.getName()), Spu::getName, spuDTO.getName())
                .set(Objects.nonNull(spuDTO.getShopCategoryId()), Spu::getShopCategoryId, spuDTO.getShopCategoryId())
                .set(Objects.nonNull(spuDTO.getStatus()), Spu::getStatus, spuDTO.getStatus())
                .set(Objects.nonNull(spuDTO.getSellingPoint()), Spu::getSellingPoint, spuDTO.getSellingPoint())
                .set(Objects.nonNull(spuDTO.getMainImgUrl()), Spu::getMainImgUrl, spuDTO.getMainImgUrl())
                .set(Objects.nonNull(spuDTO.getImgUrls()), Spu::getImgUrls, spuDTO.getImgUrls())
                .set(Objects.nonNull(spuDTO.getHasSkuImg()), Spu::getHasSkuImg, spuDTO.getHasSkuImg())
                .set(Spu::getUpdateTime,now)
                .set(Spu::getIphStatus, 1)//设置为已爱普货
                .eq(Spu::getSpuId, spuId).update();
        //更新商品详情
        spuDetailService.deleteById(spuId);
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setDetail(spuDTO.getDetail());
        spuDetail.setSpuId(spuId);
        spuDetailService.save(spuDetail);
        //根据 spuId 更新基础属性
        spuAttrValueService.deleteBySpuId(spuId);
        //组装属性数据
        List<SpuAttrValue> spuAttrValueList = spuDTO.getSpuAttrValues().stream().map(spuAttrValueDTO -> {
            SpuAttrValue spuAttrValue = new SpuAttrValue();
            spuAttrValue.setSpuId(spuId);
            spuAttrValue.setAttrName(spuAttrValueDTO.getAttrName());
            spuAttrValue.setAttrValueName(spuAttrValueDTO.getAttrValueName());
            return spuAttrValue;
        }).collect(Collectors.toList());
        //批量保存
        spuAttrValueService.saveBatch(spuAttrValueList);

        List<String> skuCodeList = skuList.stream().map(SkuDTO::getSkuCode).collect(Collectors.toList());
        List<Sku> skuDbList = skuService.lambdaQuery().in(Sku::getSkuCode, skuCodeList).list();
        Map<String, Long> skuIdMap = skuDbList.stream().collect(Collectors.toMap(Sku::getSkuCode, sku -> sku.getSkuId()));


        List<String> openSkuStores=new ArrayList<>();
        //更新sku数据
        for (SkuDTO skuDto : skuList) {
            //检查skuCode是否存在
            Long skuId = skuIdMap.get(skuDto.getSkuCode());
            if (Objects.isNull(skuId)) {
                continue;
            }
            if(!openSkuStores.contains(skuDto.getSkuCode())){
                openSkuStores.add(skuDto.getSkuCode());
            }
            skuService.lambdaUpdate()
                    .set(Objects.nonNull(skuDto.getImgUrl()), Sku::getImgUrl, skuDto.getImgUrl())
                    .set(Objects.nonNull(skuDto.getSkuName()), Sku::getName, skuDto.getSkuName())
                    .set(Sku::getUpdateTime,now)
                    .set(Sku::getStatus, 1)
                    .eq(Sku::getSkuId, skuId)
                    .update();
            //处理sku售卖属性
            spuSkuAttrValueService.lambdaUpdate().set(SpuSkuAttrValue::getStatus, -1).eq(SpuSkuAttrValue::getSkuId, skuId).update();
            List<SpuSkuAttrValueDTO> spuSkuAttrValues = skuDto.getSpuSkuAttrValues();
            List<SpuSkuAttrValue> spuSkuAttrValueList = spuSkuAttrValues.stream().map(spuSkuAttrValueDTO -> {
                SpuSkuAttrValue spuSkuAttrValue = new SpuSkuAttrValue();
                spuSkuAttrValue.setAttrValueName(spuSkuAttrValueDTO.getAttrValueName());
                spuSkuAttrValue.setAttrName(spuSkuAttrValueDTO.getAttrName());
                spuSkuAttrValue.setSkuId(skuId);
                spuSkuAttrValue.setSpuId(spuId);
                spuSkuAttrValue.setStatus(1);
                spuSkuAttrValue.setImgUrl(spuSkuAttrValueDTO.getImgUrl());
                return spuSkuAttrValue;
            }).collect(Collectors.toList());

            spuSkuAttrValueService.saveBatch(spuSkuAttrValueList);
        }

        skuStoreService.openSkuStore(openSkuStores);

        //根据spuId 及 爱铺货同步skuId集合 查询 sku表 当前spuId下铺货不存在的数据
        List<Long> skuIds = skuDbList.stream().map(Sku::getSkuId).collect(Collectors.toList());
        List<Sku> skuDelete = skuService.lambdaQuery().eq(Sku::getSpuId, spuId).notIn(Sku::getSkuId, skuIds).list();
        if (CollectionUtil.isNotEmpty(skuDelete)) {

            List<Long> skuIddels = skuDelete.stream().map(Sku::getSkuId).collect(Collectors.toList());

            //根据查询出的skuid 信息 进行 sku， sku_store 的数据删除
            skuService.lambdaUpdate()
                    .set(Sku::getStatus, -1)
                    .set(Sku::getUpdateTime,now)
                    .in(Sku::getSkuId, skuIddels)
                    .update();
            //删除 sku_store
            skuStoreService.deleteBySpuIdAndSkuId(skuIddels);

        }

        ////重置商品导航商品排序(排序至最前)
        if(Objects.nonNull(spuDTO.getShopCategoryId()) && (Objects.isNull(spu.getShopCategoryId()) || spu.getShopCategoryId()!=spuDTO.getShopCategoryId())){
            log.info("爱铺货->重置商品导航商品排序 spuId:{} shopCategoryId:{}",spuId,spuDTO.getShopCategoryId());
            SpuCategoryUpdateDTO updateDTO=new SpuCategoryUpdateDTO();
            updateDTO.setCategoryId(spuDTO.getShopCategoryId());
            updateDTO.setSpuIdList(ListUtil.toList(spuId));
            updateShopCategorys(updateDTO);
        }

        // 批量删除与sku关联的缓存信息
        RedisUtil.deleteBatch(Arrays.asList(CacheNames.SKU_WITH_ATTR_LIST_KEY +CacheNames.UNION+ spuId));

        if(CollectionUtil.isNotEmpty(skuDbList)){
            // 批量删除与sku关联的缓存信息
            Map<String, List<Sku>> skuMaps = skuDbList.stream().collect(Collectors.groupingBy(Sku::getPriceCode));
            for(Map.Entry<String,List<Sku>> entry:skuMaps.entrySet()){
                RedisUtil.deleteBatch(Arrays.asList(CacheNames.SKU_KEY_PRICECODE +CacheNames.UNION+ entry.getKey()));
                entry.getValue().stream().forEach(skuV -> {
                    RedisUtil.deleteBatch(Arrays.asList(CacheNames.SKU_KEY +CacheNames.UNION+ skuV.getSkuCode()));
                });
            }
        }

        log.info("结束执行爱铺货数据更新，耗时：{}ms   spucode【{}】", System.currentTimeMillis() - startTime, spuDTO.getSpuCode());

    }

    /**
     * 获取分销推广-推荐商品列表
     * @param productSearch
     * @return
     */
    private List<SpuCommonVO> commonList(ProductSearchDTO productSearch) {
        if (Objects.isNull(productSearch.getStoreId()) || productSearch.getStoreId() <= 0) {
            productSearch.setStoreId(Constant.MAIN_SHOP);
        }
        if (Objects.isNull(productSearch.getStatus())) {//默认筛选已上架商品(导购分销、猜你喜欢商品列表)
            productSearch.setStatus(1);
        }
        if (Objects.isNull(productSearch.getIphStatus())) {//默认筛选已铺货商品(导购分销、猜你喜欢商品列表)
            productSearch.setIphStatus(1);
        }
        //规格属性筛选
        if(Objects.nonNull(productSearch.getSpuSearchAttr()) && CollectionUtil.isNotEmpty(productSearch.getSpuSearchAttr().getSpuGroupAttrs())){
            productSearch.setAttrFilterSpuIds(getAttrFilterSpuIds(productSearch.getSpuSearchAttr()));
        }
        log.info("分页获取分销推广-推荐商品列表4 传参storeId【{}】",productSearch.getStoreId());
        return spuMapper.commonList(productSearch);
    }

    private List<SpuAppPageVO> appList(SpuPageSearchDTO searchDTO) {
        return spuMapper.appList(searchDTO);
    }

    @Override
    public List<SpuPageVO> listSpu(SpuPageSearchDTO searchDTO) {
        return spuMapper.listPageVO(searchDTO);
    }

    @Override
    public List<SpuSimpleBO> listSimple(SpuSimpleBO spuSimpleBO) {
        return spuMapper.listSimple(spuSimpleBO);
    }

    private Boolean verifySpuNum(Date currentTime, ProductSearchDTO productSearchDTO, Long spuNum) {
        if (Objects.isNull(spuNum)) {
            spuNum = spuMapper.getNotDeleteSpuNum(currentTime);
        }
        ServerResponseEntity<EsPageVO<SpuAdminVO>> responseEntity = searchSpuFeignClient.adminPage(productSearchDTO);
        if (responseEntity.isFail()) {
            throw new LuckException(responseEntity.getMsg());
        }
        Long total = responseEntity.getData().getTotal();
        // 数量相同，不需要更新
        return Objects.equals(spuNum, total);
    }


    /**
     * 获取分类、品牌、评论、库存、属性信息
     *
     * @param esProductBO 商品信息
     */
    private void setEsProductInfo(EsProductBO esProductBO) {
        // 获取sku信息
        List<SkuVO> skuList = skuService.listSkuWithAttrBySpuId(esProductBO.getSpuId(), Constant.MAIN_SHOP);
        // 商品编码、商品条形码列表信息
        Set<String> partyCodes = new HashSet<>();
        Set<String> modelIds = new HashSet<>();
        for (SkuVO skuVO : skuList) {
            if (StrUtil.isNotBlank(skuVO.getPartyCode())) {
                partyCodes.add(skuVO.getPartyCode());
            }
            if (StrUtil.isNotBlank(skuVO.getModelId())) {
                modelIds.add(skuVO.getModelId());
            }
        }
        esProductBO.setPartyCodes(partyCodes);
        esProductBO.setModelIds(modelIds);
        // 获取品牌
        EsBrandBO brand = brandService.getEsBrandBO(esProductBO);
        esProductBO.setBrand(brand);
        // 获取平台分类数据
        CategoryVO category = categoryService.getInfo(esProductBO.getCategoryId());
        esProductBO.setPrimaryCategoryId(category.getCategories().get(0).getCategoryId());
        esProductBO.setSecondaryCategoryId(category.getCategories().get(1).getCategoryId());
        // 平台三级分类信息
        esProductBO.setCategory(handleCategory(category));
        if (esProductBO.getAppDisplay() && !Objects.equals(category.getStatus(), StatusEnum.ENABLE.value())) {
            esProductBO.setAppDisplay(Boolean.FALSE);
        }
        // 获取店铺分类数据
        if (Objects.nonNull(esProductBO.getShopCategoryId()) && !Objects.equals(esProductBO.getShopCategoryId(), Constant.DEFAULT_ID)) {
            CategoryVO shopCategory = categoryService.getInfo(esProductBO.getShopCategoryId());
            esProductBO.setShopPrimaryCategoryId(shopCategory.getCategories().get(0).getCategoryId());
            // 店铺二级分类信息
            esProductBO.setShopCategory(handleCategory(shopCategory));

            if (esProductBO.getAppDisplay() && !Objects.equals(shopCategory.getStatus(), StatusEnum.ENABLE.value())) {
                esProductBO.setAppDisplay(Boolean.FALSE);
            }
        }


        // 好评数量
        esProductBO.setGoodReviewNum(spuCommService.countGoodReview(esProductBO.getSpuId()));
        // 商品库存
        SpuExtension spuExtension = spuExtensionService.getBySpuId(esProductBO.getSpuId());
        esProductBO.setStock(spuExtension.getStock());
        esProductBO.setSaleNum(spuExtension.getSaleNum() + spuExtension.getWaterSoldNum());
        esProductBO.setActualSoldNum(spuExtension.getSaleNum());
        esProductBO.setWaterSoldNum(spuExtension.getWaterSoldNum());
        esProductBO.setCommentNum(spuExtension.getCommentNum());
        // 是否有库存
        esProductBO.setHasStock(Objects.nonNull(spuExtension.getStock()) && spuExtension.getStock() > 0);

        // 获取属性
        List<SpuAttrValueVO> spuAttrsBySpuId = spuAttrValueService.getSpuAttrsBySpuId(esProductBO.getSpuId());
        List<SpuAttrValueVO> attrs = spuAttrsBySpuId.stream().filter(spuAttrValueVO ->
                Objects.nonNull(spuAttrValueVO.getSearchType()) && Objects.equals(spuAttrValueVO.getSearchType(), 1)).collect(Collectors.toList());
        List<EsAttrBO> esAttrBOList = new ArrayList<>();
        for (SpuAttrValueVO attr : attrs) {
            EsAttrBO esAttrBO = new EsAttrBO();
            esAttrBO.setAttrId(attr.getAttrId());
            esAttrBO.setAttrValueId(attr.getAttrValueId());
            // 国际化信息
            Map<Integer, SpuAttrValueLangVO> spuAttrValueLangMap = attr.getSpuAttrValueLangList().stream()
                    .collect(Collectors.toMap(SpuAttrValueLangVO::getLang, s -> s));
            // 中文信息
            SpuAttrValueLangVO spuAttrValueZh = spuAttrValueLangMap.get(LanguageEnum.LANGUAGE_ZH_CN.getLang());
            esAttrBO.setAttrNameZh(spuAttrValueZh.getAttrName());
            esAttrBO.setAttrValueNameZh(spuAttrValueZh.getAttrValueName());
            // 英文信息
            SpuAttrValueLangVO spuAttrValueEn = spuAttrValueLangMap.get(LanguageEnum.LANGUAGE_EN.getLang());
            if (Objects.nonNull(spuAttrValueEn)) {
                esAttrBO.setAttrNameEn(spuAttrValueEn.getAttrName());
                esAttrBO.setAttrValueNameEn(spuAttrValueEn.getAttrValueName());
            }
            esAttrBOList.add(esAttrBO);
        }
        esProductBO.setAttrs(esAttrBOList);
    }

    private void checkShopStatusIsOpen(Long shopId) {
        Date now = new Date();
        ServerResponseEntity<EsShopDetailBO> shopDetailRes = shopDetailFeignClient.getShopByShopId(shopId);
        if (shopDetailRes.isFail()) {
            throw new LuckException(shopDetailRes.getMsg());
        }
        EsShopDetailBO esShopDetailBO = shopDetailRes.getData();
        if (Objects.equals(esShopDetailBO.getShopStatus(), ShopStatus.OPEN.value())) {
            return;
        }
        if (Objects.equals(esShopDetailBO.getShopStatus(), ShopStatus.OFFLINE.value()) || Objects.equals(esShopDetailBO.getShopStatus(), ShopStatus.OFFLINE_AWAIT_AUDIT.value())) {
            throw new LuckException("店铺已下线");
        }
        if (Objects.equals(esShopDetailBO.getShopStatus(), ShopStatus.STOP.value())) {
            if (now.compareTo(esShopDetailBO.getContractStartTime()) < 0) {
                throw new LuckException("店铺未开始营业");
            } else {
                throw new LuckException("店铺已停止营业");
            }
        }
    }

    /**
     * 校验spu新增或更新信息
     *
     * @param spuDTO
     * @param saveOrUpdate true: 保存，false: 更新
     */
    private void checkSaveOrUpdateInfo(SpuDTO spuDTO, boolean saveOrUpdate) {
        Long shopId = AuthUserContext.get().getTenantId();
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, shopId) && Objects.isNull(spuDTO.getShopCategoryId())) {
            throw new LuckException("店铺分类不能为空");
        }
        if (Objects.isNull(spuDTO.getCategoryId())) {
            throw new LuckException("平台分类不能为空");
        }
        if (Objects.isNull(spuDTO.getSeq())) {
            spuDTO.setSeq(0);
        }
        if (Objects.isNull(spuDTO.getBrandId())) {
            spuDTO.setBrandId(0L);
        }
        if (Objects.isNull(spuDTO.getVideo())) {
            spuDTO.setVideo("");
        }
    }

    @Override
    public SpuExcelImportDataVO importParseSpus(MultipartFile multipartFile) {
        File file = FileUtil.transferFile(multipartFile);
        InputStream inputStream = null;
        List<SpuExcelImportVO> parseList = new ArrayList<SpuExcelImportVO>();
        Map<Long,SpuExcelImportVO> parseMaps = new HashMap<>();
        SpuExcelImportDataVO spuExcelImportDataVO = new SpuExcelImportDataVO();
        try {
            inputStream = new FileInputStream(file);
            ExcelUtils<ActivitySkuExportDTO> excelUtil = new ExcelUtils(ActivitySkuExportDTO.class);
            List<ActivitySkuExportDTO> list = excelUtil.importExcel("批量导入数据", inputStream);

            List<ActivitySkuExportLogDTO> logDTOS = new ArrayList<>();
            List<ActivitySkuExportDTO> newList = cleanExcelData(list, logDTOS);

            List<ActivitySkuExportDTO> importList = new ArrayList<>();
            importList.addAll(newList);

            log.info("经过校验后,当前导入数据为:{}", importList);
            for (ActivitySkuExportDTO skuExportDTO : importList) {
                if (StrUtil.isNotEmpty(skuExportDTO.getSkuBarcode())) {
                    SkuVO skuVO = skuService.getSkuByCode(skuExportDTO.getSkuBarcode());
                    if (skuVO == null) {
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品条形码在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    //判断3折兜底
                    Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
                    if(discountPrice < marketDis3Price){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
                        continue;
                    }
                    if(discountPrice > skuVO.getMarketPriceFee()){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
                        continue;
                    }
                    SkuExcelImportVO skuImportVO = mapperFacade.map(skuVO, SkuExcelImportVO.class);
                    skuImportVO.setDiscountPrice(skuExportDTO.getDiscountPrice());

                    SpuVO spuVO = getBySpuId(skuVO.getSpuId());
                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                    importSpuVO.setParticipationMode(1);
                    importSpuVO.setSeq(skuExportDTO.getSeq());

                    List<SkuExcelImportVO> skus = new ArrayList<>();
                    skus.add(skuImportVO);
                    importSpuVO.setSkus(skus);
                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                    }else{
                        parseMaps.get(importSpuVO.getSpuId()).getSkus().add(skuImportVO);
                    }

                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");

                    continue;
                }
                if (StrUtil.isNotEmpty(skuExportDTO.getSpuBarcode())) {
                    SpuVO spuVO = spuMapper.getSpuByCode(skuExportDTO.getSpuBarcode());
                    if (spuVO == null) {
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品货号在数据库中不存在，请检查后重新导入");
                        continue;
                    }

                    List<SkuVO> skus = skuService.getSkuBySpuId(spuVO.getSpuId());
                    if(CollectionUtil.isEmpty(skus)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品条形码在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    Long marketPriceFee = spuVO.getMarketPriceFee();
                    if(CollectionUtil.isNotEmpty(skus)){
                        marketPriceFee=skus.get(0).getMarketPriceFee();
                    }
                    //判断3折兜底
                    Long marketDis3Price = marketPriceFee * 3 / 10;
                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
                    if(discountPrice < marketDis3Price){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
                        continue;
                    }
                    if(discountPrice > marketPriceFee){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
                        continue;
                    }

                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                    importSpuVO.setParticipationMode(0);
                    importSpuVO.setDiscountPrice(skuExportDTO.getDiscountPrice());
                    importSpuVO.setSeq(skuExportDTO.getSeq());
                    List<SkuExcelImportVO> importSkus = mapperFacade.mapAsList(skus, SkuExcelImportVO.class);
                    importSpuVO.setSkus(importSkus);
                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                    }
                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");
                }
                // 校验商品款色
                if (StrUtil.isNotEmpty(skuExportDTO.getSkcBarcode())) {
                    log.info("商品信息导入,进入商品款色校验方法，当前数据为:{}",skuExportDTO);
                    // 判断表格内是否存在商品款色信息，如果存在就到数据库进行一次校验。确认是否有效
                    List<SkuVO> skus = skuService.getSkcByCode(skuExportDTO.getSkcBarcode());
                    if(CollectionUtil.isEmpty(skus)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品skuCode在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    Long marketPriceFee = skus.get(0).getMarketPriceFee();
                    if(Objects.isNull(marketPriceFee)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品吊牌价为空");
                        continue;
                    }
                    //判断3折兜底
                    Long marketDis3Price = marketPriceFee * 3 / 10;
                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
                    if(discountPrice < marketDis3Price){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
                        continue;
                    }
                    if(discountPrice > marketPriceFee){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
                        continue;
                    }

                    Spu spuVO = spuMapper.selectById(skus.get(0).getSpuId());
                    if(Objects.isNull(spuVO)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                    importSpuVO.setParticipationMode(2);
                    importSpuVO.setSeq(skuExportDTO.getSeq());
                    List<SkuExcelImportVO> importSkus = mapperFacade.mapAsList(skus, SkuExcelImportVO.class);
                    importSkus.forEach(item ->{
                        item.setDiscountPrice(skuExportDTO.getDiscountPrice());
                    });
                    importSpuVO.setSkus(importSkus);
                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                    }else{
                        parseMaps.get(importSpuVO.getSpuId()).getSkus().addAll(importSkus);
                    }
                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");
                }
            }

            //导入日志
            if (CollectionUtil.isNotEmpty(logDTOS)) {

                //排序
                Collections.sort(logDTOS, new Comparator<ActivitySkuExportLogDTO>() {
                    public int compare(ActivitySkuExportLogDTO o1, ActivitySkuExportLogDTO o2) {
                        //升序
                        if(Objects.nonNull(o1.getSeq()) && Objects.nonNull(o2.getSeq())){
                            return o1.getSeq().compareTo(o2.getSeq());
                        }
                        return 0;
                    }
                });

                ExcelUploadDTO excelUploadDTO = new ExcelUploadDTO(null,
                        logDTOS,
                        ActivitySkuExportLogDTO.EXCEL_NAME,
                        ActivitySkuExportLogDTO.MERGE_ROW_INDEX,
                        ActivitySkuExportLogDTO.MERGE_COLUMN_INDEX,
                        ActivitySkuExportLogDTO.class);
                ServerResponseEntity<String> response = excelUploadFeignClient.createAnduploadExcel(excelUploadDTO);
                if (response.isSuccess()) {
                    List<ActivitySkuExportLogDTO> errorLogs=logDTOS.stream().filter(logDTO -> StrUtil.isNotBlank(logDTO.getImportStatus()) && logDTO.getImportStatus().equals("失败")).collect(Collectors.toList());
                    spuExcelImportDataVO.setDate(response.getData());
                    if(CollectionUtil.isNotEmpty(errorLogs)){
                        spuExcelImportDataVO.setCount(""+errorLogs.size());
                    }else{
                        spuExcelImportDataVO.setCount("0");
                    }
                }
            }

        } catch (LuckException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导入限时调价商品模版文件 操作失败--->",e,e.getMessage());
            throw new LuckException("操作失败，请检查模板及内容是否正确");
        }

        if(CollectionUtil.isNotEmpty(parseMaps)){
            parseList=new ArrayList<>(parseMaps.values());

            //排序
            Collections.sort(parseList, new Comparator<SpuExcelImportVO>() {
                public int compare(SpuExcelImportVO o1, SpuExcelImportVO o2) {
                    //升序
                    if(Objects.nonNull(o1.getSeq()) && Objects.nonNull(o2.getSeq())){
                        return o1.getSeq().compareTo(o2.getSeq());
                    }
                    return 0;
                }
            });
        }
        log.info("限时调价导入获取商品数量 【{}】",parseList.size());
        spuExcelImportDataVO.setParseList(parseList);
        return spuExcelImportDataVO;
    }

    @Override
    public SpuExcelImportDataVO importSkuCodeTemplateExcel(MultipartFile multipartFile) {
        File file = FileUtil.transferFile(multipartFile);
        InputStream inputStream = null;
        List<SpuExcelImportVO> parseList = new ArrayList<>();

        Map<Long,SpuExcelImportVO> parseMaps = new HashMap<>();
        SpuExcelImportDataVO spuExcelImportDataVO = new SpuExcelImportDataVO();
        try {
            inputStream = new FileInputStream(file);
            ExcelUtils<CouponSkuImportDTO> excelUtil = new ExcelUtils(CouponSkuImportDTO.class);
            List<CouponSkuImportDTO> list = excelUtil.importExcel("批量导入数据", inputStream);
            List<CouponSkuImportDTO> importList = new ArrayList<>();

            for (CouponSkuImportDTO couponSkuImportDTO : list) {
                if(StrUtil.isNotEmpty(couponSkuImportDTO.getPriceCode())){
                    importList.add(couponSkuImportDTO);
                }
            }

            if(CollUtil.isEmpty(importList)){
                Assert.faild("有效数据为空，请重新维护后导入模版数据。");
            }

            for (CouponSkuImportDTO skuExportDTO : importList) {
                List<Sku> skus= skuService.list(new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getPriceCode,skuExportDTO.getPriceCode())
                        .eq(Sku::getStatus,1));
                if(CollectionUtil.isEmpty(skus)){
                    continue;
                }
//                Long marketPriceFee =skus.get(0).getMarketPriceFee();
//                if(Objects.isNull(marketPriceFee)){
//                    continue;
//                }
                Spu spuVO = this.getById(skus.get(0).getSpuId());
                if(Objects.isNull(spuVO)){
                    continue;
                }
                SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                importSpuVO.setSpuId(spuVO.getSpuId());
                importSpuVO.setSeq(skuExportDTO.getSeq());

                List<SkuExcelImportVO> importSkus = new ArrayList<>();
                SkuExcelImportVO skuExcelImportVO = new SkuExcelImportVO();
                skuExcelImportVO.setPriceCode(skuExportDTO.getPriceCode());
                importSkus.add(skuExcelImportVO);

                importSpuVO.setSkus(importSkus);
                if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                    parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                }else{
                    parseMaps.get(importSpuVO.getSpuId()).getSkus().addAll(importSkus);
                }
            }

        } catch (LuckException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导入限时调价商品模版文件 操作失败--->",e,e.getMessage());
            throw new LuckException("操作失败，请检查模板及内容是否正确");
        }

        if(CollectionUtil.isNotEmpty(parseMaps)){
            parseList=new ArrayList<>(parseMaps.values());
            //排序
//            Collections.sort(parseList, new Comparator<SpuExcelImportVO>() {
//                public int compare(SpuExcelImportVO o1, SpuExcelImportVO o2) {
//                    //升序
//                    if(Objects.nonNull(o1.getSeq()) && Objects.nonNull(o2.getSeq())){
//                        return o1.getSeq().compareTo(o2.getSeq());
//                    }
//                    return 0;
//                }
//            });
        }
        log.info("优惠券商品导入获取商品数量 【{}】",parseList.size());
        spuExcelImportDataVO.setParseList(parseList);
        return spuExcelImportDataVO;
    }

    //    public SpuExcelImportDataVO importParseSpus(MultipartFile multipartFile) {
//        File file = FileUtil.transferFile(multipartFile);
//        InputStream inputStream = null;
//        List<SpuExcelImportVO> parseList = new ArrayList<SpuExcelImportVO>();
//        Map<Long,SpuExcelImportVO> parseMaps = new HashMap<>();
//        SpuExcelImportDataVO spuExcelImportDataVO = new SpuExcelImportDataVO();
//        try {
//            inputStream = new FileInputStream(file);
//            ExcelUtils<ActivitySkuExportDTO> excelUtil = new ExcelUtils(ActivitySkuExportDTO.class);
//            List<ActivitySkuExportDTO> list = excelUtil.importExcel("批量导入数据", inputStream);
//
//            List<ActivitySkuExportLogDTO> logDTOS = new ArrayList<>();
//            List<ActivitySkuExportDTO> newList = cleanExcelData(list, logDTOS);
//
//            List<ActivitySkuExportDTO> importList = new ArrayList<>();
//            //根据 spubarcode去重复
//            List<ActivitySkuExportDTO> newList_spu = newList.stream()
//                    .filter(item -> StrUtil.isNotBlank(item.getSpuBarcode()))
//                    .collect(Collectors.collectingAndThen(
//                    Collectors.toCollection(() -> new TreeSet<>(
//                            Comparator.comparing(
//                                    ActivitySkuExportDTO::getSpuBarcode))), ArrayList::new));
//
//            importList.addAll(newList_spu);
//
//            //根据 skubarcode去重复
//            List<ActivitySkuExportDTO> newList_sku = newList.stream()
//                    .filter(item -> StrUtil.isNotBlank(item.getSkuBarcode()) && StrUtil.isBlank(item.getSpuBarcode()))
//                    .collect(Collectors.collectingAndThen(
//                    Collectors.toCollection(() -> new TreeSet<>(
//                            Comparator.comparing(
//                                    ActivitySkuExportDTO::getSkuBarcode))), ArrayList::new));
//
//            importList.addAll(newList_sku);
//
//            for (ActivitySkuExportDTO skuExportDTO : importList) {
//                if (StrUtil.isNotEmpty(skuExportDTO.getSkuBarcode())) {
//                    SkuVO skuVO = skuService.getSkuByCode(skuExportDTO.getSkuBarcode());
//                    if (skuVO == null) {
//                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品条形码在数据库中不存在，请检查后重新导入");
//                        continue;
//                    }
//                    //判断3折兜底
//                    Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
//                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
//                    if(discountPrice < marketDis3Price){
//                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
//                        continue;
//                    }
//                    if(discountPrice > skuVO.getMarketPriceFee()){
//                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
//                        continue;
//                    }
//                    SkuExcelImportVO skuImportVO = mapperFacade.map(skuVO, SkuExcelImportVO.class);
//                    skuImportVO.setDiscountPrice(skuExportDTO.getDiscountPrice());
//
//                    SpuVO spuVO = getBySpuId(skuVO.getSpuId());
//                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
//                    importSpuVO.setParticipationMode(1);
//
//                    List<SkuExcelImportVO> skus = new ArrayList<>();
//                    skus.add(skuImportVO);
//                    importSpuVO.setSkus(skus);
//                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
//                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
//                    }else{
//                        parseMaps.get(importSpuVO.getSpuId()).getSkus().add(skuImportVO);
//                    }
//
//                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");
//
//                    continue;
//                }
//                if (StrUtil.isNotEmpty(skuExportDTO.getSpuBarcode())) {
//                    SpuVO spuVO = spuMapper.getSpuByCode(skuExportDTO.getSpuBarcode());
//                    if (spuVO == null) {
//                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品货号在数据库中不存在，请检查后重新导入");
//                        continue;
//                    }
//
//                    List<SkuVO> skus = skuService.getSkuBySpuId(spuVO.getSpuId());
//                    Long marketPriceFee = spuVO.getMarketPriceFee();
//                    if(CollectionUtil.isNotEmpty(skus)){
//                        marketPriceFee=skus.get(0).getMarketPriceFee();
//                    }
//                    //判断3折兜底
//                    Long marketDis3Price = marketPriceFee * 3 / 10;
//                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
//                    if(discountPrice < marketDis3Price){
//                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
//                        continue;
//                    }
//                    if(discountPrice > marketPriceFee){
//                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
//                        continue;
//                    }
//
//                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
//                    importSpuVO.setParticipationMode(0);
//                    importSpuVO.setDiscountPrice(skuExportDTO.getDiscountPrice());
//                    List<SkuExcelImportVO> importSkus = mapperFacade.mapAsList(skus, SkuExcelImportVO.class);
//                    importSpuVO.setSkus(importSkus);
//                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
//                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
//                    }
//
//                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");
//                }
//            }
//
//            //导入日志
//            if (CollectionUtil.isNotEmpty(logDTOS)) {
//                ExcelUploadDTO excelUploadDTO = new ExcelUploadDTO(null,
//                        logDTOS,
//                        ActivitySkuExportLogDTO.EXCEL_NAME,
//                        ActivitySkuExportLogDTO.MERGE_ROW_INDEX,
//                        ActivitySkuExportLogDTO.MERGE_COLUMN_INDEX,
//                        ActivitySkuExportLogDTO.class);
//                ServerResponseEntity<String> response = excelUploadFeignClient.createAnduploadExcel(excelUploadDTO);
//                if (response.isSuccess()) {
//                    List<ActivitySkuExportLogDTO> errorLogs=logDTOS.stream().filter(logDTO -> StrUtil.isNotBlank(logDTO.getImportStatus()) && logDTO.getImportStatus().equals("失败")).collect(Collectors.toList());
//                    spuExcelImportDataVO.setDate(response.getData());
//                    if(CollectionUtil.isNotEmpty(errorLogs)){
//                        spuExcelImportDataVO.setCount(""+errorLogs.size());
//                    }else{
//                        spuExcelImportDataVO.setCount("0");
//                    }
//                }
//            }
//
//        } catch (LuckException e) {
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if(CollectionUtil.isNotEmpty(parseMaps)){
//            parseList=new ArrayList<>(parseMaps.values());
//        }
//        log.info("限时调价导入获取商品数量 【{}】",parseList.size());
//        spuExcelImportDataVO.setParseList(parseList);
//        return spuExcelImportDataVO;
//    }

    @Override
    public List<SpuVO> listGiftSpuBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return null;
        }
        List<SpuVO> spuList = spuMapper.listSpuBySpuIds(spuIds);
        if (CollectionUtil.isEmpty(spuList)) {
            return spuList;
        }
        spuList.forEach(spuVO -> {
            List<SkuVO> skuVOS = skuService.listSkuWithAttrBySpuId(spuVO.getSpuId(), Constant.MAIN_SHOP);
            skuVOS.forEach(skuVO -> {
                skuVO.setProperties(skuService.spliceProperties(skuVO.getSpuSkuAttrValues()));
            });
            spuVO.setSkus(skuVOS);
        });
        return spuList;
    }

    /**
     * 批量修改商品铺货状态
     * @param spuIphStatusDTOS
     */
    @Override
    public void updateIphStatus(List<SpuIphStatusDTO> spuIphStatusDTOS) {
        if(CollectionUtil.isNotEmpty(spuIphStatusDTOS)){
            spuIphStatusDTOS.forEach(spuIphStatusDTO -> {
                if(Objects.nonNull(spuIphStatusDTO.getId()) && Objects.nonNull(spuIphStatusDTO.getIphStatus())){
                    this.lambdaUpdate().eq(Spu::getSpuId,spuIphStatusDTO.getId())
                            .set(Spu::getIphStatus,spuIphStatusDTO.getIphStatus())
                            .set(Spu::getUpdateTime,new Date())
                            .update();
                }
            });
        }
    }

    /**
     * 校验商品渠道
     * @param spuSkuRDTO
     * @return
     */
    @Override
    public List<Long> isSpuSkuChannel(SpuSkuRDTO spuSkuRDTO) {
        if(CollectionUtil.isEmpty(spuSkuRDTO.getSpuIds())){
            return null;
        }
        if(StrUtil.isBlank(spuSkuRDTO.getChannel())){
            spuSkuRDTO.setChannel(SpuChannelEnums.CHANNEL_R.getCode());
        }
        //获取商品sku数据
        List<Sku> skus=skuService.list(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getStatus,1)
                .in(Sku::getSpuId,spuSkuRDTO.getSpuIds()));
        if(CollectionUtil.isEmpty(skus)){
            return null;
        }
        Map<Long, List<Sku>> spuSkuMap = skus.stream().collect(Collectors.groupingBy(Sku::getSpuId));
        List<Long> rSpu=new ArrayList<>();
        for(Map.Entry<Long,List<Sku>> entry : spuSkuMap.entrySet()){
            List<Sku> r_list = entry.getValue().stream()
                    .filter(sku -> StrUtil.isNotBlank(sku.getChannelName()) && sku.getChannelName().equals(spuSkuRDTO.getChannel()))
                    .collect(Collectors.toList());
            int rCount=CollectionUtil.isNotEmpty(r_list)?r_list.size():0;//R渠道总数
            log.info(" 商品id【{}】 获取到sku总数【{}】 R渠道【{}】",entry.getKey(),entry.getValue().size(),rCount);
            if(rCount>=entry.getValue().size()){
                if(!rSpu.contains(entry.getKey())){
                    rSpu.add(entry.getKey());
                }
            }
        }
        return rSpu;
    }

    @Override
    public List<StdPushSpuVO> pushStdSpus(List<String> spuCodes) {
        return spuMapper.pushStdSpus(spuCodes);
    }

    /**
     * 批量修改商品导航
     * 同时重新排序
     */
    @Override
    public boolean updateShopCategorys(SpuCategoryUpdateDTO dto) {
        if(Objects.isNull(dto.getCategoryId()) || CollectionUtil.isEmpty(dto.getSpuIdList())){
            return true;
        }
        List<Spu> spus=this.list(new LambdaQueryWrapper<Spu>().eq(Spu::getShopCategoryId,dto.getCategoryId()).orderByAsc(Spu::getSeq));
        List<SpuSeq> spuList=mapperFacade.mapAsList(spus,SpuSeq.class);
        if(CollectionUtil.isNotEmpty(dto.getSpuIdList())){
            Map<Long, SpuSeq> spuMaps = spuList.stream().collect(Collectors.toMap(SpuSeq::getSpuId, spu -> spu));
            dto.getSpuIdList().forEach(spuId ->{
                if(spuMaps.containsKey(spuId)){
                    spuMaps.remove(spuId);
                }
            });
            spuList=new ArrayList<>(spuMaps.values());
            Collections.sort(spuList, Comparator.comparingInt(SpuSeq::getSeq));
        }
        //最新的排在最前(新变更的商品)
        int seq=1;
        List<SpuSeq> addSpuList=new ArrayList<>();
        for(Long spuid:dto.getSpuIdList()){
            addSpuList.add(new SpuSeq(spuid,seq++,dto.getCategoryId()));
        }
        //现有数据排序后重新按照顺序1,2,3,4.....排列导航对应商品排序数据
        for(SpuSeq spuSeq:spuList){
            spuSeq.setSeq(seq++);
            spuSeq.setUpdateTime(new Date());
        }
        spuList.addAll(0,addSpuList);//必须在现有数据重置排序后添加新的商品导航排序
        List<Spu> updateSpuList=mapperFacade.mapAsList(spuList,Spu.class);
        this.updateBatchById(updateSpuList);
        return true;
    }

    @Override
    public void batchZeroSetChannelsStock(ZeroSetStockDto dto) {
        skuStockService.update(Wrappers.lambdaUpdate(SkuStock.class)
                .set(SkuStock::getChannelsStock, 0)
                .in(SkuStock::getSkuId, dto.getSkuId()));

        spuExtensionService.zeroSetChannelsStock(dto.getSpuId());
    }

    @Override
    public void updateChannelsStock(Long spuId, List<UpdateChannelsSkuStockDto> skuStockDtoList) {
        int coutStock = 0;
        for (UpdateChannelsSkuStockDto dto : skuStockDtoList) {
            Integer stock = dto.getStock();
            Integer type = dto.getType();
            coutStock += dto.getStock();
            if (Objects.equals(3, type)){
                SkuStock skuStock = skuStockService.getOne(Wrappers.lambdaQuery(SkuStock.class)
                        .eq(SkuStock::getSkuId, dto.getSkuId()));
                Integer channelsStock = skuStock.getChannelsStock();
                coutStock = coutStock - channelsStock;
                skuStockService.update(Wrappers.lambdaUpdate(SkuStock.class)
                        .set(SkuStock::getChannelsStock, stock)
                        .eq(SkuStock::getSkuId, dto.getSkuId()));
            } else {
                skuStockService.updateSkuChannelsStock(dto.getSkuId(), dto.getStock());
            }
        }
        spuExtensionService.updateChannelsStock(spuId, coutStock);
    }

    @Override
    public Set<Long> getAttrFilterSpuIds(SpuSearchAttrDTO spuSearchAttr) {
        String redisKey=ProductCacheNames.ATTR_FILTER_SPU_ID+":"+DigestUtil.md5Hex(JSON.toJSONString(spuSearchAttr));
        if(RedisUtil.hasKey(redisKey)){
            return RedisUtil.get(redisKey);
        }
        List<String> vagueAttrGroup= Lists.newArrayList();
        List<String> exactAttrGroup= Lists.newArrayList();
        Map<Boolean, List<SpuSearchAttrDTO.SpuGroupAttr>> attrValueMap = spuSearchAttr.getSpuGroupAttrs().stream().collect(Collectors.groupingBy(SpuSearchAttrDTO.SpuGroupAttr::getVague));
        attrValueMap.forEach((k,v)->{
            if(k){
                if(CollectionUtil.isNotEmpty(v)) {
                    for (SpuSearchAttrDTO.SpuGroupAttr spuGroupAttr : v) {
                        if(CollectionUtil.isNotEmpty(spuGroupAttr.getAttrValue())) {
                            vagueAttrGroup.addAll(spuGroupAttr.getAttrValue());
                        }
                    }
                }
            }else{
                if(CollectionUtil.isNotEmpty(v)) {
                    for (SpuSearchAttrDTO.SpuGroupAttr spuGroupAttr : v) {
                        if(CollectionUtil.isNotEmpty(spuGroupAttr.getAttrValue())){
                            exactAttrGroup.addAll(spuGroupAttr.getAttrValue());
                        }
                    }
                }
            }
        });

        Set<Long> spuIDs=spuMapper.getAttrFilterSpuIds(spuSearchAttr,exactAttrGroup,vagueAttrGroup);
        if(CollectionUtil.isEmpty(spuIDs)){
            spuIDs= Sets.newHashSet(-1L);
        }
        RedisUtil.set(redisKey,spuIDs,60*30);
        return spuIDs;
    }

    private List<ActivitySkuExportDTO> cleanExcelData(List<ActivitySkuExportDTO> list, List<ActivitySkuExportLogDTO> logDTOS) {

        Map<String, List<ActivitySkuExportDTO>> spuMaps = list.stream().filter(spu->StrUtil.isNotBlank(spu.getSpuBarcode())).collect(Collectors.groupingBy(ActivitySkuExportDTO::getSpuBarcode));
        Map<String, List<ActivitySkuExportDTO>> skuMaps = list.stream().filter(sku->StrUtil.isNotBlank(sku.getSkuBarcode())).collect(Collectors.groupingBy(ActivitySkuExportDTO::getSkuBarcode));
        Map<String, List<ActivitySkuExportDTO>> skcMaps = list.stream().filter(sku->StrUtil.isNotBlank(sku.getSkcBarcode())).collect(Collectors.groupingBy(ActivitySkuExportDTO::getSkcBarcode));

        List<ActivitySkuExportDTO> newList = new ArrayList<>();
        for (ActivitySkuExportDTO skuExportDTO : list) {

            //手动判断去除空行
            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSpuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSkcBarcode())) {
                continue;
            }
            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSkuName())  && StrUtil.isEmpty(skuExportDTO.getSkcBarcode())
                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode()) && skuExportDTO.getDiscountPrice() == null && skuExportDTO.getPrice() == null) {
                continue;
            }
            if(Objects.isNull(skuExportDTO.getSeq())){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品序号不能为空，请检查数据重新上传");
                continue;
            }
            if(CollectionUtil.isNotEmpty(spuMaps)
                    && spuMaps.containsKey(skuExportDTO.getSpuBarcode())
                    && spuMaps.get(skuExportDTO.getSpuBarcode()).size()>1){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品货号重复，请检查数据重新上传");
                continue;
            }
            if(CollectionUtil.isNotEmpty(skuMaps)
                    && skuMaps.containsKey(skuExportDTO.getSkuBarcode())
                    && skuMaps.get(skuExportDTO.getSkuBarcode()).size()>1){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品条形码重复，请检查数据重新上传");
                continue;
            }
            if(CollectionUtil.isNotEmpty(skcMaps)
                    && skcMaps.containsKey(skuExportDTO.getSkcBarcode())
                    && skcMaps.get(skuExportDTO.getSkcBarcode()).size()>1){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品款色重复，请检查数据重新上传");
                continue;
            }
            if (skuExportDTO.getDiscountPrice() == null || skuExportDTO.getDiscountPrice().equals(BigDecimal.ZERO)) {
//                Assert.faild("商品调价金额不能为空，请检查数据重新上传");
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品调价金额不能为空，请检查数据重新上传");
                continue;
            }
            if (NumberUtil.isLess(skuExportDTO.getDiscountPrice(), BigDecimal.ZERO)) {
//                Assert.faild("商品调价金额不能小于0，请检查数据重新上传");
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品调价金额不能小于0，请检查数据重新上传");
                continue;
            }
            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSkcBarcode())
                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode())) {
//                Assert.faild("货号或者条形码不允许都为空，请检查数据重新上传");
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "货号、条形码、款色不允许都为空，请检查数据重新上传");
                continue;
            }
            //如果条码和货号同时存在，直接报错提示。
            if(StrUtil.isNotEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isNotEmpty(skuExportDTO.getSkcBarcode())
                    && StrUtil.isNotEmpty(skuExportDTO.getSpuBarcode())){
                Assert.faild(StrUtil.format("上传数据不允许货号、条码、款色同时存在，货号:{},条码:{},款色:{}，请检查数据重新上传",skuExportDTO.getSkuBarcode(), skuExportDTO.getSpuBarcode(), skuExportDTO.getSkcBarcode()));
            }
            newList.add(skuExportDTO);
        }
        return newList;
    }

    private void loadErrorData(List<ActivitySkuExportLogDTO> logVOS, ActivitySkuExportDTO excelVO, String importStatus, String importRemarks) {
        ActivitySkuExportLogDTO logVO = new ActivitySkuExportLogDTO();
        mapperFacade.map(excelVO, logVO);
        logVO.setImportStatus(importStatus);
        logVO.setImportRemarks(importRemarks);
        logVOS.add(logVO);
    }

//    private List<ActivitySkuExportDTO> cleanExcelData(List<ActivitySkuExportDTO> list, List<ActivitySkuExportLogDTO> logDTOS) {
//        List<ActivitySkuExportDTO> newList = new ArrayList<>();
//        for (ActivitySkuExportDTO skuExportDTO : list) {
//
//            //手动判断去除空行
//            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSpuBarcode())) {
//                continue;
//            }
//            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSkuName())
//                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode()) && skuExportDTO.getDiscountPrice() == null && skuExportDTO.getPrice() == null) {
//                continue;
//            }
//            if (skuExportDTO.getDiscountPrice() == null || skuExportDTO.getDiscountPrice().equals(BigDecimal.ZERO)) {
////                Assert.faild("商品调价金额不能为空，请检查数据重新上传");
//                this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品调价金额不能为空，请检查数据重新上传");
//            }
//            if (NumberUtil.isLess(skuExportDTO.getDiscountPrice(), BigDecimal.ZERO)) {
////                Assert.faild("商品调价金额不能小于0，请检查数据重新上传");
//                this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品调价金额不能小于0，请检查数据重新上传");
//            }
//            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode())
//                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode())) {
////                Assert.faild("货号或者条形码不允许都为空，请检查数据重新上传");
//                this.loadErrorData(logDTOS, skuExportDTO, "失败", "货号或者条形码不允许都为空，请检查数据重新上传");
//            }
//            //如果条码和货号同时存在，直接报错提示。
//            if(StrUtil.isNotEmpty(skuExportDTO.getSkuBarcode())
//                    && StrUtil.isNotEmpty(skuExportDTO.getSpuBarcode())){
//                Assert.faild(StrUtil.format("上传数据不允许货号和条码同时存在，货号:{},条码:{}，请检查数据重新上传",skuExportDTO.getSkuBarcode(),skuExportDTO.getSpuBarcode()));
//            }
//            newList.add(skuExportDTO);
//        }
//        return newList;
//    }

//    private void loadErrorData(List<ActivitySkuExportLogDTO> logVOS, ActivitySkuExportDTO excelVO, String importStatus, String importRemarks) {
//        ActivitySkuExportLogDTO logVO = new ActivitySkuExportLogDTO();
//        mapperFacade.map(excelVO, logVO);
//        logVO.setImportStatus(importStatus);
//        logVO.setImportRemarks(importRemarks);
//        if (!logVOS.contains(logVO)) {
//            logVOS.add(logVO);
//        }
//    }


}
