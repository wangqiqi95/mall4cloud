package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.dto.TagActivityPageDTO;
import com.mall4j.cloud.product.mapper.TagActivityMapper;
import com.mall4j.cloud.product.model.TagActRelationProd;
import com.mall4j.cloud.product.model.TagActRelationStore;
import com.mall4j.cloud.product.model.TagActivity;
import com.mall4j.cloud.product.service.TagActRelationProdService;
import com.mall4j.cloud.product.service.TagActRelationStoreService;
import com.mall4j.cloud.product.service.TagActivityService;
import com.mall4j.cloud.product.vo.TagActRelationProdVO;
import com.mall4j.cloud.product.vo.TagActivityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagActivityServiceImpl implements TagActivityService {
    private  final TagActivityMapper tagActivityMapper;
    private  final TagActRelationStoreService storeService;
    private  final TagActRelationProdService prodService;
    @Override
    public PageVO<TagActivityVO> page(PageDTO pageDTO, TagActivityPageDTO request) {
        return PageUtil.doPage(pageDTO, () -> tagActivityMapper.list(request));
    }
    @Override
    public TagActivity getById(Long id) {
        return tagActivityMapper.getById(id);
    }

    @Override
    public void removeCache(Long id) {
        List<TagActRelationProd> prods=prodService.listsByActId(id);
        if(CollectionUtil.isNotEmpty(prods)){
            List<TagActRelationStore> stores=storeService.listsByActId(id);
            List<Long> spuIds = prods.stream().map(TagActRelationProd::getSpuId).distinct().collect(Collectors.toList());
            List<Long> storeIds = CollectionUtil.isNotEmpty(stores)?stores.stream().map(TagActRelationStore::getStoreId).distinct().collect(Collectors.toList()):new ArrayList<>();
            removeCategoryCache(spuIds,storeIds);
        }
    }

    @Override
    public void save(TagActivity tagActivity) {
        tagActivityMapper.save(tagActivity);
    }

    @Override
    public void update(TagActivity tagActivity) {
        tagActivityMapper.update(tagActivity);

        removeCache(tagActivity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        storeService.deleteByActId(id);
        prodService.deleteByActId(id);
        tagActivityMapper.deleteById(id);

        removeCache(id);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrUpdateTagActivity(TagActivity tagActivity, List<Long> stores, List<Long> spuList) {
        if(tagActivity.getId()==null){
             this.save(tagActivity);
        }else{
            storeService.deleteByActId(tagActivity.getId());
            prodService.deleteByActId(tagActivity.getId());
            this.update(tagActivity);
        }
        //新增商店关联
        if(!CollectionUtils.isEmpty(stores)){
            stores.forEach(storeId->storeService.save(new TagActRelationStore(tagActivity.getId(),storeId)));
        }
        spuList.forEach(spuId->prodService.save(new TagActRelationProd(tagActivity.getId(),spuId)));

        removeCache(tagActivity.getId());
    }

    @Override
    public TagActivity getTagBySpuId(Long spuId, Long storeId) {
        String rediskey= ProductCacheNames.APP_PRODUCT_TAG+spuId+storeId;
        if(RedisUtil.hasKey(rediskey)){
            log.info("cache getTagBySpuId data--> {}",rediskey);
            TagActivity tagActivity= JSONObject.parseObject(RedisUtil.get(rediskey),TagActivity.class);
            return tagActivity;
        }
        TagActivity tagActivity=tagActivityMapper.getTagBySpuId(spuId,storeId);
        if(Objects.nonNull(tagActivity)){
            RedisUtil.set(rediskey,JSONObject.toJSONString(tagActivity),1800);
        }
        return tagActivity;
    }

    @Override
    public void updateTagActivityStatus() {
        tagActivityMapper.updateTagActivityStatus();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addShops(Long id, List<Long> shops) {
        if(shops!=null) {
            shops.forEach(storeId->{
                TagActRelationStore store = new TagActRelationStore();
                store.setStoreId(storeId);
                store.setActId(id);
                store.setCreateTime(new Date());
                store.setUpdateTime(store.getCreateTime());
                storeService.save(store);
            });
        }else{
            storeService.deleteByActId(id);
            TagActivity tagActivity = new TagActivity();
            tagActivity.setId(id);
            tagActivity.setIsAllShop(1);
            this.update(tagActivity);
        }
        removeCache(id);
    }

    @Override
    public void removeCategoryCache(List<Long> spuList,List<Long> stores) {
        List<String> keyList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(spuList)){
            for (Long spuId:spuList) {
                String key=CacheNames.APP_PRODUCT_TAG +spuId.toString();
                keyList.add(key);
            }
        }
        if (CollUtil.isNotEmpty(keyList)) {
            RedisUtil.deleteBatch(keyList);
        }
    }


}
