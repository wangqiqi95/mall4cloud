package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.discount.feign.DiscountFeignClient;
import com.mall4j.cloud.api.distribution.feign.DistributionFeignClient;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.flow.feign.FlowFeignClient;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.constant.ShopStatus;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopDetailVO;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.dto.*;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.seckill.feign.SeckillFeignClient;
import com.mall4j.cloud.api.seckill.vo.SeckillApiVO;
import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.bo.*;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.dto.SpuLangDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuAdminVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SkuStoreServiceImpl extends ServiceImpl<SkuStoreMapper, SkuStore> implements SkuStoreService {

    @Autowired
    private SkuStoreMapper skuStoreMapper;

    @Override
    public void deleteBySpuIdAndStoreId(Long spuId, long storeId) {
        skuStoreMapper.deleteBySpuIdAndStoreId(spuId,storeId);
    }

    @Override
    public void deleteBySpuIdAndSkuId(List<Long> skuIds) {
        skuStoreMapper.deleteBySpuIdAndSkuId(skuIds);
    }

    @Override
    public List<SkuStore> getSkuStoresByStoreId(long storeId) {
        return skuStoreMapper.getSkuStoresByStoreId(storeId);
    }

    @Override
    public List<SkuStore> getElSkuStoresByStoreId(long storeId) {
        return skuStoreMapper.getElSkuStoresByStoreId(storeId);
    }

    @Override
    public void openSkuStore(List<String> openSkuStores) {
        if(CollectionUtil.isNotEmpty(openSkuStores)){
            log.info("openSkuStore skucode--> openSkuStores：{}", JSONObject.toJSONString(openSkuStores));
            List<SkuStore> skuStores=this.list(new LambdaQueryWrapper<SkuStore>().ne(SkuStore::getStatus,1).in(SkuStore::getSkuCode,openSkuStores));
            if(CollectionUtil.isNotEmpty(skuStores)){
                List<SkuStore> updateSkuStores=new ArrayList<>();
                skuStores.stream().forEach(skuStore -> {
                    SkuStore updateSkuStore=new SkuStore();
                    updateSkuStore.setSkuStoreId(skuStore.getSkuStoreId());
                    updateSkuStore.setStatus(1);
                    updateSkuStore.setUpdateTime(new Date());
                    updateSkuStores.add(updateSkuStore);
                    log.info("openSkuStore skucode--> skucode:{} updateSkuStore：{}", skuStore.getSkuCode(),JSONObject.toJSONString(updateSkuStore));
                });
                log.info("openSkuStore skucode--> updateSkuStores.size：{}", updateSkuStores.size());
                this.updateBatchById(updateSkuStores);
            }
        }
    }
}
