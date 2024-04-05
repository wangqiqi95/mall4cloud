package com.mall4j.cloud.biz.service.channels.impl;
import com.mall4j.cloud.api.biz.constant.channels.LeagueItemOperateType;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.ItemUpdReq.ExclusiveInfo;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.constant.channels.PromotionType;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.BatchAddReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.ItemDeleteReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.ItemUpdReq;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.BatchAddResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.ItemUpdResp;
import com.mall4j.cloud.api.biz.vo.ChannelsSkuVO;
import com.mall4j.cloud.api.biz.vo.ChannelsSpuSkuVO;
import com.mall4j.cloud.biz.wx.wx.channels.LeagueItemApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.biz.mapper.channels.LeagueItemPlanFinderMapper;
import com.mall4j.cloud.biz.mapper.channels.LeagueItemPlanMapper;
import com.mall4j.cloud.biz.mapper.channels.LeagueItemPlanProductMapper;
import com.mall4j.cloud.biz.model.channels.LeagueItemPlan;
import com.mall4j.cloud.biz.model.channels.LeagueItemPlanFinder;
import com.mall4j.cloud.biz.model.channels.LeagueItemPlanProduct;
import com.mall4j.cloud.biz.service.channels.ChannelsSpuService;
import com.mall4j.cloud.biz.service.channels.LeagueItemPlanService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 优选联盟推广计划
 * @Author axin
 * @Date 2023-02-20 17:28
 **/
@Service
@Slf4j
@Deprecated
public class LeagueItemPlanServiceImpl  implements LeagueItemPlanService {
    @Autowired
    private LeagueItemPlanMapper leagueItemPlanMapper;
    @Autowired
    private LeagueItemPlanFinderMapper leagueItemPlanFinderMapper;
    @Autowired
    private LeagueItemPlanProductMapper leagueItemPlanProductMapper;
    @Autowired
    private LeagueItemApi leagueItemApi;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ChannelsSpuService channelsSpuService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addPlan(AddItemPlanReqDto reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到登录用户信息");
        }
        Date date = new Date();
        LeagueItemPlan leagueItemPlan = new LeagueItemPlan();
        leagueItemPlan.setCreatePerson(userInfoInTokenBO.getUserId().toString());
        leagueItemPlan.setUpdatePerson(userInfoInTokenBO.getUserId().toString());
        leagueItemPlan.setName(reqDto.getName());
        leagueItemPlan.setType(reqDto.getType());
        leagueItemPlan.setBeginTime(reqDto.getBeginTime());
        leagueItemPlan.setEndTime(reqDto.getEndTime());
        leagueItemPlan.setCreateTime(date);
        leagueItemPlan.setUpdateTime(date);
        leagueItemPlanMapper.insert(leagueItemPlan);

        return batchAddPlanProductOrFinder(leagueItemPlan,reqDto.getItems(),reqDto.getFinderIds(), date);
    }


    @Override
    public void updPlan(UpdItemPlanReqDto reqDto) {
        Date date = new Date();
        LeagueItemPlan leagueItemPlan = leagueItemPlanMapper.selectById(reqDto.getId());
        if(Objects.isNull(leagueItemPlan)){
            throw new LuckException("未找到推广计划");
        }

        leagueItemPlan.setName(reqDto.getName());
        leagueItemPlanMapper.updateById(leagueItemPlan);
        List<String> oldFinders = leagueItemPlanFinderMapper.selectFinderListByPlanId(reqDto.getId());

        //处理添加的商品
        if(CollectionUtils.isNotEmpty(reqDto.getAddItems())) {
            List<String> newFinders = Lists.newArrayList();
            newFinders.addAll(oldFinders);
            newFinders.addAll(reqDto.getAddFinderIds());
            //新增的商品单独处理，新增商品需要把历史的达人也加进去
            batchAddPlanProductOrFinder(leagueItemPlan, reqDto.getAddItems(), newFinders, date);
        }

        //处理更新的商品
        if(CollectionUtils.isNotEmpty(reqDto.getUpdItems())){
            List<LeagueItemPlanProduct> leagueItemPlanProducts = leagueItemPlanProductMapper.selectList(Wrappers.lambdaQuery(LeagueItemPlanProduct.class)
                    .in(LeagueItemPlanProduct::getProductId, reqDto.getUpdItems().stream().map(UpdItem::getProductId).collect(Collectors.toList()))
                    .eq(LeagueItemPlanProduct::getPlanId, reqDto.getId()));
            Map<String, LeagueItemPlanProduct> productMap = leagueItemPlanProducts.stream().collect(Collectors.toMap(LeagueItemPlanProduct::getProductId, k -> k));

            for (UpdItem updItem : reqDto.getUpdItems()) {
                ItemUpdReq itemUpdReq = new ItemUpdReq();
                if(!PromotionType.ORDINARY.getValue().equals(leagueItemPlan.getType())){
                    LeagueItemPlanProduct leagueItemPlanProduct = productMap.get(updItem.getProductId());
                    itemUpdReq.setInfoId(leagueItemPlanProduct.getInfoId());
                }else{
                    itemUpdReq.setProductId(updItem.getProductId());
                }
                itemUpdReq.setType(leagueItemPlan.getType());
                itemUpdReq.setOperateType(1);
                itemUpdReq.setRatio(updItem.getRatio());
                ExclusiveInfo exclusiveInfo = new ExclusiveInfo();
                exclusiveInfo.setBeginTime(leagueItemPlan.getBeginTime().getTime()/1000);
                exclusiveInfo.setEndTime(leagueItemPlan.getEndTime().getTime()/1000);
                exclusiveInfo.setAddFinderIds(reqDto.getAddFinderIds());
                exclusiveInfo.setDelFinderIds(reqDto.getDelFinderIds());
                exclusiveInfo.setIsForerver(true);
                itemUpdReq.setExclusiveInfo(exclusiveInfo);

                log.info("更新联盟商品入参:{}",JSON.toJSONString(itemUpdReq));
                ItemUpdResp updResp = leagueItemApi.upd(wxConfig.getWxEcTokenTest(), itemUpdReq);
                log.info("更新联盟商品出参:{}",JSON.toJSONString(itemUpdReq));

                if(updResp.getErrcode()!=0){
                    throw new LuckException(updResp.getErrmsg());
                }

                LeagueItemPlanProduct updProductParam = new LeagueItemPlanProduct();
                updProductParam.setRatio(updItem.getRatio());
                if(!PromotionType.ORDINARY.getValue().equals(leagueItemPlan.getType())) {
                    updProductParam.setInfoId(updResp.getInfoId());
                }
                LambdaUpdateWrapper<LeagueItemPlanProduct> updProductWrapper = Wrappers.lambdaUpdate(LeagueItemPlanProduct.class);
                updProductWrapper.eq(LeagueItemPlanProduct::getPlanId,leagueItemPlan.getId())
                        .eq(LeagueItemPlanProduct::getProductId,updItem.getProductId());
                leagueItemPlanProductMapper.update(updProductParam,updProductWrapper);
            }
        }

        //处理删除的商品
        if(CollectionUtils.isNotEmpty(reqDto.getDelItems())){
            List<LeagueItemPlanProduct> leagueItemPlanProducts = leagueItemPlanProductMapper.selectList(Wrappers.lambdaQuery(LeagueItemPlanProduct.class)
                    .in(LeagueItemPlanProduct::getProductId, reqDto.getDelItems().stream().map(UpdItem::getProductId).collect(Collectors.toList()))
                    .eq(LeagueItemPlanProduct::getPlanId, reqDto.getId()));
            Map<String, LeagueItemPlanProduct> productMap = leagueItemPlanProducts.stream().collect(Collectors.toMap(LeagueItemPlanProduct::getProductId, k -> k));

            for (UpdItem delItem : reqDto.getDelItems()) {
                ItemDeleteReq itemDeleteReq = new ItemDeleteReq();
                if(!PromotionType.ORDINARY.getValue().equals(leagueItemPlan.getType())){
                    LeagueItemPlanProduct leagueItemPlanProduct = productMap.get(delItem.getProductId());
                    itemDeleteReq.setInfoId(leagueItemPlanProduct.getInfoId());
                }
                itemDeleteReq.setProductId(delItem.getProductId());
                itemDeleteReq.setType(leagueItemPlan.getType());
                log.info("删除联盟商品入参:{}",JSON.toJSONString(itemDeleteReq));
                BaseResponse deleteResp = leagueItemApi.delete(wxConfig.getWxEcTokenTest(), itemDeleteReq);
                log.info("删除联盟商品入参:{}",JSON.toJSONString(itemDeleteReq));

                if(deleteResp.getErrcode() !=0){
                    throw new LuckException(deleteResp.getErrmsg());
                }
                LambdaQueryWrapper<LeagueItemPlanProduct> queryWrapper = Wrappers.lambdaQuery(LeagueItemPlanProduct.class);
                queryWrapper.eq(LeagueItemPlanProduct::getPlanId,reqDto.getId())
                        .eq(LeagueItemPlanProduct::getProductId,delItem.getProductId());
                leagueItemPlanProductMapper.delete(queryWrapper);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updProductStatus(DisableProductReqDto reqDto) {
        ItemUpdReq itemUpdReq = new ItemUpdReq();
        LambdaQueryWrapper<LeagueItemPlanProduct> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        objectLambdaQueryWrapper.eq(LeagueItemPlanProduct::getProductId,reqDto.getProductId());
        LeagueItemPlanProduct planProduct = leagueItemPlanProductMapper.selectOne(objectLambdaQueryWrapper);
        if(Objects.isNull(planProduct)){
            throw new LuckException("未找到商品信息");
        }
//        if(!PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
//            itemUpdReq.setInfoId(planProduct.getInfoId());
//        }else{
//            itemUpdReq.setProductId(reqDto.getProductId());
//        }

        if(reqDto.getStatus()==0){
            itemUpdReq.setOperateType(LeagueItemOperateType.DELISTING.getValue());
        }else{
            itemUpdReq.setOperateType(LeagueItemOperateType.LISTING.getValue());
        }
        planProduct.setStatus(reqDto.getStatus());
//        itemUpdReq.setType(reqDto.getType());
        log.info("下架联盟商品入参:{}", JSON.toJSONString(itemUpdReq));
        BaseResponse response = leagueItemApi.upd(wxConfig.getWxEcTokenTest(), itemUpdReq);
        log.info("下架联盟商品出参:{}", JSON.toJSONString(response));
        if(response.getErrcode()!=0){
            throw new LuckException(response.getErrmsg());
        }

        leagueItemPlanProductMapper.updateById(planProduct);
    }

    @Override
    public ItemPlanRespDto get(Long id) {
        LeagueItemPlan leagueItemPlan = leagueItemPlanMapper.selectById(id);
        ItemPlanRespDto itemPlanRespDto = mapperFacade.map(leagueItemPlan, ItemPlanRespDto.class);
        itemPlanRespDto.setTypeName(PromotionType.getDesc(itemPlanRespDto.getType()));

        LambdaQueryWrapper<LeagueItemPlanFinder> lambdaQueryWrapper = Wrappers.lambdaQuery(LeagueItemPlanFinder.class);
        lambdaQueryWrapper.eq(LeagueItemPlanFinder::getPlanId,id);
        List<LeagueItemPlanFinder> leagueItemPlanFinders = leagueItemPlanFinderMapper.selectList(lambdaQueryWrapper);

        itemPlanRespDto.setFinderIds(leagueItemPlanFinders.stream().map(LeagueItemPlanFinder::getFinderId).collect(Collectors.toList()));

        return itemPlanRespDto;
    }

    @Override
    public PageVO<ItemDetail> getProductPage(ProductPageReqDto reqDto) {
        LambdaQueryWrapper<LeagueItemPlanProduct> queryWrapper = Wrappers.lambdaQuery(LeagueItemPlanProduct.class);
        queryWrapper.eq(Objects.nonNull(reqDto.getId()),LeagueItemPlanProduct::getPlanId,reqDto.getId())
                    .in(CollectionUtils.isNotEmpty(reqDto.getProductIds()),LeagueItemPlanProduct::getProductId,reqDto.getProductIds());
        Page<LeagueItemPlanProduct> leagueItemPlanProductPage = leagueItemPlanProductMapper.selectPage(new Page<>(reqDto.getPageNum(), reqDto.getPageSize()), queryWrapper);

        PageVO<ItemDetail> respPage = new PageVO<>();
        respPage.setList(mapperFacade.mapAsList(leagueItemPlanProductPage.getRecords(), ItemDetail.class));

        List<Long> productIds = respPage.getList().stream().map(a-> Long.parseLong(a.getProductId())).collect(Collectors.toList());
        List<ChannelsSpuSkuVO> channelsSpuSkuVOS = channelsSpuService.listChannelsSpuSkuVO(productIds);
        Map<Long, ChannelsSpuSkuVO> spuSkuVOMap = channelsSpuSkuVOS.stream().collect(Collectors.toMap(k -> k.getChannelsSpuVO().getOutSpuId(), v -> v));

        respPage.getList().forEach(list->{
            ChannelsSpuSkuVO channelsSpuSkuVO = spuSkuVOMap.get(Long.parseLong(list.getProductId()));
            if(Objects.nonNull(channelsSpuSkuVO)){
                ChannelsSkuVO channelsSkuVO = channelsSpuSkuVO.getChannelsSkuVO().stream().min(Comparator.comparing(ChannelsSkuVO::getPrice)).get();
                list.setName(channelsSpuSkuVO.getChannelsSpuVO().getTitle());
                list.setSpuCode(channelsSpuSkuVO.getChannelsSpuVO().getSpuCode());
                list.setLivePriceFee(channelsSkuVO.getPrice());
                list.setMarketPriceFee(channelsSkuVO.getMarketPriceFee());
                list.setHeadImgs(channelsSpuSkuVO.getChannelsSpuVO().getHeadImgs());
            }
        });

        respPage.setPages((int)leagueItemPlanProductPage.getPages());
        respPage.setTotal(leagueItemPlanProductPage.getTotal());
        return respPage;
    }

    @Override
    public PageVO<ItemPlanListPageRespDto> planListPage(ItemPlanListPageReqDto reqDto) {
        LambdaQueryWrapper<LeagueItemPlan> lambdaQueryWrapper = Wrappers.lambdaQuery(LeagueItemPlan.class);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(reqDto.getName()),LeagueItemPlan::getName,reqDto.getName())
                .eq(Objects.nonNull(reqDto.getType()),LeagueItemPlan::getType,reqDto.getType());
        Page<LeagueItemPlan> leagueItemPlanPage = leagueItemPlanMapper.selectPage(new Page<>(reqDto.getPageNum(), reqDto.getPageSize()), lambdaQueryWrapper);
        PageVO<ItemPlanListPageRespDto> pageVO = new PageVO<>();
        pageVO.setList(mapperFacade.mapAsList(leagueItemPlanPage.getRecords(), ItemPlanListPageRespDto.class));

        List<Long> planIds = pageVO.getList().stream().map(ItemPlanListPageRespDto::getId).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(planIds)) {
            List<LeagueItemPlanProduct> products = leagueItemPlanProductMapper.selectList(Wrappers.lambdaQuery(LeagueItemPlanProduct.class).in(LeagueItemPlanProduct::getPlanId, planIds));
            Map<Long, List<LeagueItemPlanProduct>> productMaps = products.stream().collect(Collectors.groupingBy(LeagueItemPlanProduct::getPlanId));

            List<LeagueItemPlanFinder> finders = leagueItemPlanFinderMapper.selectList(Wrappers.lambdaQuery(LeagueItemPlanFinder.class).in(LeagueItemPlanFinder::getPlanId, planIds));
            Map<Long, List<LeagueItemPlanFinder>> finderMaps = finders.stream().collect(Collectors.groupingBy(LeagueItemPlanFinder::getPlanId));
            for (ItemPlanListPageRespDto itemPlanListPageRespDto : pageVO.getList()) {
                itemPlanListPageRespDto.setTypeName(PromotionType.getDesc(itemPlanListPageRespDto.getType()));
                itemPlanListPageRespDto.setProductIds(Optional.ofNullable(productMaps.get(itemPlanListPageRespDto.getId())).orElse(Lists.newArrayList()).stream().map(LeagueItemPlanProduct::getProductId).collect(Collectors.toList()));
                itemPlanListPageRespDto.setFinderIds(Optional.ofNullable(finderMaps.get(itemPlanListPageRespDto.getId())).orElse(Lists.newArrayList()).stream().map(LeagueItemPlanFinder::getFinderId).collect(Collectors.toList()));
            }
        }
        pageVO.setPages((int)leagueItemPlanPage.getPages());
        pageVO.setTotal(leagueItemPlanPage.getTotal());
        return pageVO;
    }


    /**
     * 批量添加计划商品和达人
     * @param leagueItemPlan
     * @param items
     * @param finderIds
     * @param date
     */
    @Transactional(rollbackFor = Exception.class)
    public String batchAddPlanProductOrFinder(LeagueItemPlan leagueItemPlan,List<AddItem> items,List<String> finderIds, Date date) {
        if(CollectionUtils.isNotEmpty(finderIds) && !PromotionType.ORDINARY.getValue().equals(leagueItemPlan.getType())) {
            List<LeagueItemPlanFinder> leagueItemPlanFinders = Lists.newArrayList();
            finderIds.forEach(finder->{
                LeagueItemPlanFinder leagueItemPlanFinder = new LeagueItemPlanFinder();
                leagueItemPlanFinder.setPlanId(leagueItemPlan.getId());
                leagueItemPlanFinder.setFinderId(finder);
                leagueItemPlanFinder.setCreateTime(date);
                leagueItemPlanFinder.setUpdateTime(date);
                leagueItemPlanFinders.add(leagueItemPlanFinder);
            });
            leagueItemPlanFinderMapper.insertBatch(leagueItemPlanFinders);
        }

        if(CollectionUtils.isNotEmpty(items)) {
            List<LeagueItemPlanProduct> list = Lists.newArrayList();
            items.forEach(item ->{
                LeagueItemPlanProduct leagueItemPlanProduct = new LeagueItemPlanProduct();
                leagueItemPlanProduct.setPlanId(leagueItemPlan.getId());
                leagueItemPlanProduct.setProductId(item.getOutProductId());
                leagueItemPlanProduct.setRatio(item.getRatio());
                leagueItemPlanProduct.setCreateTime(date);
                leagueItemPlanProduct.setUpdateTime(date);
                list.add(leagueItemPlanProduct);
            });
            leagueItemPlanProductMapper.insertBatch(list);
        }

        BatchAddReq batchAddReq = new BatchAddReq();
        batchAddReq.setType(leagueItemPlan.getType());
        List<BatchAddReq.ProductParam> productParams = mapperFacade.mapAsList(items, BatchAddReq.ProductParam.class);
        batchAddReq.setList(productParams);
        batchAddReq.setFinderIds(finderIds);
        batchAddReq.setBeginTime(leagueItemPlan.getBeginTime().getTime()/1000);
        batchAddReq.setEndTime(leagueItemPlan.getEndTime().getTime()/1000);
        log.info("批量添加联盟商品入参:{}",JSON.toJSONString(batchAddReq));
        BatchAddResp batchaddResp = leagueItemApi.batchadd(wxConfig.getWxEcTokenTest(), batchAddReq);
        log.info("批量添加联盟商品出参:{}",JSON.toJSONString(batchaddResp));
        if(batchaddResp.getErrcode()!=0){
            throw new LuckException(batchaddResp.getErrmsg());
        }

        StringBuilder toast = new StringBuilder();
        if(CollectionUtils.isNotEmpty(batchaddResp.getResultInfoList())){

            for (BatchAddResp.ResultInfo resultInfo : batchaddResp.getResultInfoList()) {
                toast.append(String.format("错误编码:%s,错误信息:%s,错误商品编码:%s,错误特殊商品计划id:%s,错误达人列表:%s",
                        resultInfo.getErrcode(),resultInfo.getErrmsg(),resultInfo.getProductId(),resultInfo.getInfoId(),resultInfo.getFailFinderIds()));
            }
        }

        if(!PromotionType.ORDINARY.getValue().equals(leagueItemPlan.getType())){
            List<BatchAddResp.ResultInfo> resultInfoList = batchaddResp.getResultInfoList();
            //处理特殊推广商品计划id
            for (BatchAddResp.ResultInfo resultInfo : resultInfoList) {
                LambdaUpdateWrapper<LeagueItemPlanProduct> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(LeagueItemPlanProduct::getPlanId, leagueItemPlan.getId())
                        .eq(LeagueItemPlanProduct::getProductId, resultInfo.getProductId());

                LeagueItemPlanProduct leagueItemPlanProduct = new LeagueItemPlanProduct();
                if(resultInfo.getErrcode()==0) {
                    leagueItemPlanProduct.setInfoId(resultInfo.getInfoId());
                }else{
                    leagueItemPlanProduct.setErrMsg(resultInfo.getErrmsg());
                    leagueItemPlanProduct.setErrCode(resultInfo.getErrcode());
                }
                leagueItemPlanProductMapper.update(leagueItemPlanProduct, updateWrapper);
            }
        }

        return toast.toString();
    }
}
