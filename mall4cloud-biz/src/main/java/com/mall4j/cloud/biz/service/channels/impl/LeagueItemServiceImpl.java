package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.constant.channels.LeagueItemOperateType;
import com.mall4j.cloud.api.biz.constant.channels.PromotionType;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.BatchAddReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.ItemDeleteReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.ItemUpdReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.item.ItemUpdReq.ExclusiveInfo;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.BatchAddResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.item.ItemUpdResp;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.biz.wx.wx.channels.LeagueItemApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.constant.LeagueItemStatus;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSpuMapper;
import com.mall4j.cloud.biz.mapper.channels.LeagueItemFinderMapper;
import com.mall4j.cloud.biz.mapper.channels.LeagueItemProductMapper;
import com.mall4j.cloud.biz.mapper.channels.LeaguePromoterMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.model.channels.LeagueItemFinder;
import com.mall4j.cloud.biz.model.channels.LeagueItemProduct;
import com.mall4j.cloud.biz.model.channels.LeaguePromoter;
import com.mall4j.cloud.biz.service.channels.LeagueItemService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description 优选联盟商品
 * @Author axin
 * @Date 2023-04-20 15:06
 **/
@Service
@Slf4j
public class LeagueItemServiceImpl implements LeagueItemService {

    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private LeagueItemApi leagueItemApi;

    @Autowired
    private LeagueItemProductMapper leagueItemProductMapper;

    @Autowired
    private LeagueItemFinderMapper leagueItemFinderMapper;

    @Autowired
    private LeaguePromoterMapper leaguePromoterMapper;

    @Autowired
    private ChannelsSpuMapper channelsSpuMapper;



    @Override
    public PageVO<ItemListPageRespDto> listPage(ItemListPageReqDto reqDto) {
        PageVO<ItemListPageRespDto> pageVO = new PageVO<>();
        PageHelper.startPage(reqDto.getPageNum(), reqDto.getPageSize());
        List<ItemListPageRespDto> listPageRespDtos = leagueItemProductMapper.queryList(reqDto);
        PageInfo<ItemListPageRespDto> pageInfo = new PageInfo<>(listPageRespDtos);

        List<Long> productIds = pageInfo.getList().stream().map(ItemListPageRespDto::getId).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(productIds)) {
            List<ItemListFinderRespDto> finderRespDtos = leagueItemFinderMapper.queryListByProductIds(productIds);
            Map<Long, List<ItemListFinderRespDto>> finderMaps = finderRespDtos.stream().collect(Collectors.groupingBy(ItemListFinderRespDto::getProductId));
            pageInfo.getList().forEach(model -> model.setFinder(finderMaps.get(model.getId())));
        }
        pageVO.setPages(pageInfo.getPages());
        pageVO.setTotal(pageInfo.getTotal());
        List<ItemListPageRespDto> voList = mapperFacade.mapAsList(pageInfo.getList(), ItemListPageRespDto.class);
        Date date = new Date();
        voList.forEach(vo->{
            if(LeagueItemStatus.LISTING.value().equals(vo.getStatus())) {
                boolean isEffective = Objects.nonNull(vo.getBeginTime()) && Objects.nonNull(vo.getEndTime()) && DateUtil.isIn(date, vo.getBeginTime(), vo.getEndTime());
                if (isEffective || vo.getIsForerver()) {
                    vo.setStatus(LeagueItemStatus.EFFECTIVE.value());
                } else if (Objects.nonNull(vo.getBeginTime()) && DateUtil.compare(vo.getBeginTime(), date) > 0) {
                    vo.setStatus(LeagueItemStatus.EXPIRED.value());
                }
                if(PromotionType.ORDINARY.getValue().equals(vo.getType())){
                    vo.setStatus(LeagueItemStatus.EFFECTIVE.value());
                }
            }
        });

        pageVO.setList(voList);
        return pageVO;
    }

    @Override
    public PageVO<ItemAllowPromotionListPageRespDto> allowPromotionListPageByType(ItemAllowPromotionListPageReqDto reqDto) {
        PageVO<ItemAllowPromotionListPageRespDto> pageVO = new PageVO<>();
        PageHelper.startPage(reqDto.getPageNum(), reqDto.getPageSize());
        List<ItemAllowPromotionListPageRespDto> list=leagueItemProductMapper.allowPromotionListPageByType(reqDto);
        PageInfo<ItemAllowPromotionListPageRespDto> pageInfo = new PageInfo<>(list);


        pageVO.setPages(pageInfo.getPages());
        pageVO.setTotal(pageInfo.getTotal());
        pageVO.setList(pageInfo.getList());
        return pageVO;
    }

    @Override
    public List<BatchAddResp.ResultInfo> add(AddItemReqDto reqDto) {
        checkParam(reqDto);
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到登录用户信息");
        }

        BatchAddReq batchAddReq = new BatchAddReq();
        batchAddReq.setType(reqDto.getType());
        List<BatchAddReq.ProductParam> productParams = Lists.newArrayList();
        for (AddItem item : reqDto.getItems()) {
            BatchAddReq.ProductParam productParam = new BatchAddReq.ProductParam();
            productParam.setProductId(item.getOutProductId());
            productParam.setRatio(item.getRatio());
            productParams.add(productParam);
        }
        batchAddReq.setList(productParams);

        List<LeaguePromoter> leaguePromoters = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(reqDto.getFinderIds())) {
            leaguePromoters = leaguePromoterMapper.selectList(Wrappers.lambdaQuery(LeaguePromoter.class).in(LeaguePromoter::getId, reqDto.getFinderIds()));
            batchAddReq.setFinderIds(leaguePromoters.stream().map(LeaguePromoter::getFinderId).collect(Collectors.toList()));
        }
        if(!PromotionType.ORDINARY.getValue().equals(reqDto.getType()) && (Objects.isNull(reqDto.getIsForerver()) || !reqDto.getIsForerver()) ){
            batchAddReq.setBeginTime(reqDto.getBeginTime().getTime() / 1000);
            batchAddReq.setEndTime(reqDto.getEndTime().getTime() / 1000);
        }
        batchAddReq.setIsForerver(reqDto.getIsForerver());
        log.info("批量添加联盟商品入参:{}", JSON.toJSONString(batchAddReq));
        BatchAddResp batchaddResp = leagueItemApi.batchadd(wxConfig.getWxEcTokenTest(), batchAddReq);
        log.info("批量添加联盟商品出参:{}",JSON.toJSONString(batchaddResp));
        if(batchaddResp.getErrcode()!=0){
            throw new LuckException(batchaddResp.getErrmsg());
        }
        if(CollectionUtils.isNotEmpty(batchaddResp.getResultInfoList())){
            Date date = new Date();
            Map<String, AddItem> addItemMap = reqDto.getItems().stream().collect(Collectors.toMap(AddItem::getOutProductId, v -> v));

            for (BatchAddResp.ResultInfo resultInfo : batchaddResp.getResultInfoList()) {
                if(resultInfo.getErrcode() ==0){
                    AddItem addItem = addItemMap.get(resultInfo.getProductId());
                    if(PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
                        leagueItemProductMapper.delete(Wrappers.lambdaUpdate(LeagueItemProduct.class)
                                .eq(LeagueItemProduct::getOutProductId,addItem.getOutProductId())
                                .eq(LeagueItemProduct::getType,PromotionType.ORDINARY.getValue()));
                    }
                    LeagueItemProduct leagueItemProduct = new LeagueItemProduct();
                    leagueItemProduct.setOutProductId(resultInfo.getProductId());
                    leagueItemProduct.setInfoId(resultInfo.getInfoId());
                    leagueItemProduct.setSpuId(addItem.getSpuId());
                    leagueItemProduct.setSpuCode(addItem.getSpuCode());
                    leagueItemProduct.setRatio(addItem.getRatio());
                    leagueItemProduct.setStatus(0);
                    leagueItemProduct.setType(reqDto.getType());
                    leagueItemProduct.setBeginTime(reqDto.getBeginTime());
                    leagueItemProduct.setEndTime(reqDto.getEndTime());
                    leagueItemProduct.setForerver(reqDto.getIsForerver());
                    leagueItemProduct.setCreatePerson(userInfoInTokenBO.getUsername());
                    leagueItemProduct.setUpdatePerson(userInfoInTokenBO.getUsername());
                    leagueItemProduct.setCreateTime(date);
                    leagueItemProduct.setUpdateTime(date);
                    leagueItemProductMapper.insert(leagueItemProduct);

                    if(CollectionUtils.isNotEmpty(reqDto.getFinderIds()) && !PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
                        Map<String, LeaguePromoter> promoterMap = leaguePromoters.stream().collect(Collectors.toMap(LeaguePromoter::getFinderId, v -> v));
                        List<String> reqFinderIds = leaguePromoters.stream().map(LeaguePromoter::getFinderId).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(resultInfo.getFailFinderIds())){
                            reqFinderIds.removeAll(resultInfo.getFailFinderIds());
                        }
                        for (String finderId : reqFinderIds) {
                            LeaguePromoter leaguePromoter = promoterMap.get(finderId);
                            LeagueItemFinder leagueItemFinder = new LeagueItemFinder();
                            leagueItemFinder.setProductId(leagueItemProduct.getId());
                            leagueItemFinder.setFinderId(leaguePromoter.getId());
                            leagueItemFinder.setCreateTime(date);
                            leagueItemFinder.setUpdateTime(date);
                            leagueItemFinderMapper.insert(leagueItemFinder);
                        }
                    }
                }
            }
            return batchaddResp.getResultInfoList();
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upd(UpdItemReqDto reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到登录用户信息");
        }
        LeagueItemProduct leagueItemProduct = leagueItemProductMapper.selectById(reqDto.getId());
        if(Objects.isNull(leagueItemProduct)){
            throw new LuckException("未找到记录");
        }
        boolean checkDate=Objects.isNull(reqDto.getIsForerver()) && Objects.isNull(reqDto.getBeginTime()) && Objects.isNull(reqDto.getEndTime());
        boolean isAddFinderNull=CollectionUtils.isEmpty(reqDto.getAddFinderIds());
        boolean isDelFinderNull=CollectionUtils.isEmpty(reqDto.getDelFinderIds());
        List<String> addFinderIds = Lists.newArrayList();
        List<String> delFinderIds = Lists.newArrayList();
        if(!PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
            if(checkDate){
                throw new LuckException("推广时间不能为空");
            }
            if(!isAddFinderNull){
                List<LeaguePromoter> leaguePromoters = leaguePromoterMapper.selectBatchIds(reqDto.getAddFinderIds());
                addFinderIds = leaguePromoters.stream().map(LeaguePromoter::getFinderId).collect(Collectors.toList());
            }
            if(!isDelFinderNull){
                List<LeaguePromoter> leaguePromoters = leaguePromoterMapper.selectBatchIds(reqDto.getDelFinderIds());
                delFinderIds = leaguePromoters.stream().map(LeaguePromoter::getFinderId).collect(Collectors.toList());
            }
        }

        ItemUpdReq itemUpdReq = new ItemUpdReq();
        if(!PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
            itemUpdReq.setInfoId(leagueItemProduct.getInfoId());
        }
        itemUpdReq.setProductId(leagueItemProduct.getOutProductId());
        itemUpdReq.setType(reqDto.getType());
        itemUpdReq.setOperateType(1);
        itemUpdReq.setRatio(reqDto.getRatio());
        ItemUpdReq.ExclusiveInfo exclusiveInfo = new ItemUpdReq.ExclusiveInfo();
        if(Objects.isNull(reqDto.getIsForerver()) || !reqDto.getIsForerver() ){
            exclusiveInfo.setBeginTime(Objects.nonNull(reqDto.getBeginTime())?reqDto.getBeginTime().getTime()/1000:null);
            exclusiveInfo.setEndTime(Objects.nonNull(reqDto.getEndTime())?reqDto.getEndTime().getTime()/1000:null);
        }
        exclusiveInfo.setAddFinderIds(addFinderIds);
        exclusiveInfo.setDelFinderIds(delFinderIds);
        exclusiveInfo.setIsForerver(reqDto.getIsForerver());
        itemUpdReq.setExclusiveInfo(exclusiveInfo);

        log.info("更新联盟商品入参:{}",JSON.toJSONString(itemUpdReq));
        ItemUpdResp updResp = leagueItemApi.upd(wxConfig.getWxEcTokenTest(), itemUpdReq);
        log.info("更新联盟商品出参:{}",JSON.toJSONString(itemUpdReq));

        if(updResp.getErrcode()!=0){
            throw new LuckException(updResp.getErrmsg());
        }

        leagueItemProduct.setInfoId(updResp.getInfoId());
        leagueItemProduct.setRatio(reqDto.getRatio());
        leagueItemProduct.setStatus(0);
        leagueItemProduct.setType(reqDto.getType());
        leagueItemProduct.setBeginTime(reqDto.getBeginTime());
        leagueItemProduct.setEndTime(reqDto.getEndTime());
        leagueItemProduct.setForerver(reqDto.getIsForerver());
        leagueItemProduct.setUpdatePerson(userInfoInTokenBO.getUsername());
        leagueItemProduct.setUpdateTime(new Date());

        leagueItemProductMapper.update(leagueItemProduct,
                Wrappers.lambdaUpdate(LeagueItemProduct.class)
                        .set(LeagueItemProduct::getBeginTime,reqDto.getBeginTime())
                        .set(LeagueItemProduct::getEndTime,reqDto.getEndTime())
                        .eq(LeagueItemProduct::getId,leagueItemProduct.getId()));

        Date date = new Date();
        if(!PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
            if(!isAddFinderNull){
                for (Long addFinderId : reqDto.getAddFinderIds()) {
                    LeagueItemFinder leagueItemFinder = new LeagueItemFinder();
                    leagueItemFinder.setProductId(leagueItemProduct.getId());
                    leagueItemFinder.setFinderId(addFinderId);
                    leagueItemFinder.setCreateTime(date);
                    leagueItemFinder.setUpdateTime(date);
                    leagueItemFinderMapper.insert(leagueItemFinder);
                }
            }
            if(!isDelFinderNull){
                leagueItemFinderMapper.delete(Wrappers.lambdaUpdate(LeagueItemFinder.class)
                        .eq(LeagueItemFinder::getProductId,leagueItemProduct.getId())
                        .in(LeagueItemFinder::getFinderId,reqDto.getDelFinderIds()));
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteItem reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到登录用户信息");
        }
        LeagueItemProduct leagueItemProduct = leagueItemProductMapper.selectById(reqDto.getId());
        if(Objects.isNull(leagueItemProduct)){
            throw new LuckException("未找到记录");
        }

        ItemDeleteReq itemDeleteReq = new ItemDeleteReq();
        itemDeleteReq.setProductId(leagueItemProduct.getOutProductId());
        itemDeleteReq.setInfoId(leagueItemProduct.getInfoId());
        itemDeleteReq.setType(leagueItemProduct.getType());

        log.info("删除联盟商品入参:{}",JSON.toJSONString(itemDeleteReq));
        BaseResponse deleteResp = leagueItemApi.delete(wxConfig.getWxEcTokenTest(), itemDeleteReq);
        log.info("删除联盟商品出参:{}",JSON.toJSONString(itemDeleteReq));
        if(deleteResp.getErrcode()!=0){
            throw new LuckException(deleteResp.getErrmsg());
        }

        leagueItemFinderMapper.delete(Wrappers.lambdaUpdate(LeagueItemFinder.class)
                .eq(LeagueItemFinder::getProductId,leagueItemProduct.getId()));

        leagueItemProductMapper.deleteById(leagueItemProduct.getId());


        //普通类型下架,定向的也需要同步下架
        if(PromotionType.ORDINARY.getValue().equals(leagueItemProduct.getType())){
            LeagueItemProduct updateSetParam = new LeagueItemProduct();
            updateSetParam.setStatus(1);

            LambdaUpdateWrapper<LeagueItemProduct> updateWrapper = Wrappers.lambdaUpdate(LeagueItemProduct.class)
                    .eq(LeagueItemProduct::getOutProductId, leagueItemProduct.getOutProductId())
                    .eq(LeagueItemProduct::getType,PromotionType.DIRECTIONAL.getValue());
            leagueItemProductMapper.update(updateSetParam,updateWrapper);
        }
    }

    @Override
    public void updProductStatus(DisableProductReqDto reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到登录用户信息");
        }
        LeagueItemProduct leagueItemProduct = leagueItemProductMapper.selectById(reqDto.getId());
        if(Objects.isNull(leagueItemProduct)){
            throw new LuckException("未找到记录");
        }

        ItemUpdReq itemUpdReq = new ItemUpdReq();
        itemUpdReq.setProductId(leagueItemProduct.getOutProductId());
        itemUpdReq.setInfoId(leagueItemProduct.getInfoId());
        itemUpdReq.setType(leagueItemProduct.getType());
        if(reqDto.getStatus()==0) {
            itemUpdReq.setOperateType(LeagueItemOperateType.DELISTING.getValue());
        }else{
            itemUpdReq.setOperateType(LeagueItemOperateType.LISTING.getValue());
        }
        itemUpdReq.setRatio(0);
        itemUpdReq.setExclusiveInfo(new ExclusiveInfo());

        log.info("更新联盟商品入参:{}",JSON.toJSONString(itemUpdReq));
        ItemUpdResp updResp = leagueItemApi.upd(wxConfig.getWxEcTokenTest(), itemUpdReq);
        log.info("更新联盟商品出参:{}",JSON.toJSONString(itemUpdReq));
        if(updResp.getErrcode()!=0){
            throw new LuckException(updResp.getErrmsg());
        }

        LeagueItemProduct updateItem = new LeagueItemProduct();
        updateItem.setId(leagueItemProduct.getId());
        updateItem.setStatus(reqDto.getStatus());
        updateItem.setUpdateTime(new Date());
        updateItem.setUpdatePerson(userInfoInTokenBO.getUsername());
        leagueItemProductMapper.updateById(updateItem);

        //普通类型下架,定向的也需要同步下架
        if(PromotionType.ORDINARY.getValue().equals(leagueItemProduct.getType()) && reqDto.getStatus()==1){
            LeagueItemProduct updateSetParam = new LeagueItemProduct();
            updateSetParam.setStatus(reqDto.getStatus());

            LambdaUpdateWrapper<LeagueItemProduct> updateWrapper = Wrappers.lambdaUpdate(LeagueItemProduct.class)
                    .eq(LeagueItemProduct::getOutProductId, leagueItemProduct.getOutProductId())
                    .eq(LeagueItemProduct::getType,PromotionType.DIRECTIONAL.getValue());
            leagueItemProductMapper.update(updateSetParam,updateWrapper);
        }

    }

    @Override
    public ItemListDetailRespDto get(Long id) {
        ItemListDetailRespDto respDto = new ItemListDetailRespDto();

        LeagueItemProduct leagueItemProduct = leagueItemProductMapper.selectById(id);
        if(Objects.isNull(leagueItemProduct)){
            throw new LuckException("未找到记录");
        }

        mapperFacade.map(leagueItemProduct,respDto);
        List<ItemListFinderRespDto> finderRespDtos = leagueItemFinderMapper.queryListByProductIds(Lists.newArrayList(leagueItemProduct.getId()));
        respDto.setFinder(finderRespDtos);

        List<ChannelsSpu> channelsSpus = channelsSpuMapper.selectList(Wrappers.lambdaQuery(ChannelsSpu.class).eq(ChannelsSpu::getOutSpuId, leagueItemProduct.getOutProductId()));
        ChannelsSpu channelsSpu = channelsSpus.get(0);
        respDto.setSpuId(channelsSpu.getSpuId());
        respDto.setSpuCode(channelsSpu.getSpuCode());
        respDto.setHeadImgs(channelsSpu.getHeadImgs());
        respDto.setTitle(channelsSpu.getTitle());
        respDto.setIsForerver(leagueItemProduct.getForerver());

        return respDto;
    }

    @Override
    public void handleExpiredItem() {
        int pageNo=1;
        int pageSize=500;
        Page<LeagueItemProduct> leagueItemProductPage;
        do {
            leagueItemProductPage = leagueItemProductMapper.selectPage(new Page<>(pageNo, pageSize),
                    Wrappers.lambdaQuery(LeagueItemProduct.class)
                            .eq(LeagueItemProduct::getStatus,LeagueItemStatus.LISTING.value())
                            .le(LeagueItemProduct::getEndTime,new Date()));

            List<LeagueItemProduct> records = leagueItemProductPage.getRecords();
            if(CollectionUtils.isNotEmpty(records)){
                records.forEach(record->{
                    DisableProductReqDto disableProductReqDto = new DisableProductReqDto();
                    disableProductReqDto.setId(record.getId());
                    disableProductReqDto.setStatus(LeagueItemStatus.DE_LISTING.value());
                    updProductStatus(disableProductReqDto);
                });
            }
            pageNo++;
        }while (pageNo < leagueItemProductPage.getPages());

    }

    @Override
    public void besedelisting(String outProductId, String spuCode, String reason) {
        log.info("商品基础数据下架，商品编号:{},supCode:{},下架原因：{}",outProductId,spuCode,reason);
        if(StringUtils.isNotBlank(outProductId)) {
            LeagueItemProduct leagueItemProduct = new LeagueItemProduct();
            leagueItemProduct.setStatus(LeagueItemStatus.DE_LISTING.value());
            leagueItemProductMapper.update(leagueItemProduct, Wrappers.lambdaUpdate(LeagueItemProduct.class)
                    .eq(LeagueItemProduct::getOutProductId, outProductId)
                    .eq(LeagueItemProduct::getStatus,LeagueItemStatus.LISTING.value())
            );
        }
    }

    /**
     * 校验添加商品
     * @param reqDto
     */
    private void checkParam(AddItemReqDto reqDto) {
        boolean checkDate=Objects.isNull(reqDto.getIsForerver()) && Objects.isNull(reqDto.getBeginTime()) && Objects.isNull(reqDto.getEndTime());
        boolean isFinderNull=CollectionUtils.isEmpty(reqDto.getFinderIds());
        if(PromotionType.ORDINARY.getValue().equals(reqDto.getType())){
            for (AddItem item : reqDto.getItems()) {
                if(item.getRatio()<1 || item.getRatio() >50){
                    throw new LuckException("普通推广商品佣金范围1%-50%");
                }
            }

        }else if(PromotionType.DIRECTIONAL.getValue().equals(reqDto.getType())){
            if(checkDate){
                throw new LuckException("推广时间不能为空");
            }
            if(isFinderNull){
                throw new LuckException("达人不能为空");
            }
        }else if(PromotionType.EXCLUSIVE.getValue().equals(reqDto.getType())){
            if(checkDate){
                throw new LuckException("推广时间不能为空");
            }
            if(isFinderNull){
                throw new LuckException("达人不能为空");
            }
        }else {
            throw new LuckException("推广计划类型不正确");
        }

    }
}
