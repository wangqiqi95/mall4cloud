package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.coupon.constant.CouponActivityCentreSourceEnum;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreAddDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponActivityCentreFeignClient;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountActivityMapper;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountShopMapper;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountSkuMapper;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountSpuMapper;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountShopVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountSkuVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountSpuVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @luzhengxiang
 * @create 2022-03-10 2:38 PM
 **/
@Service
@Slf4j
public class TimeLimitedDiscountActivityBizServiceImpl implements TimeLimitedDiscountActivityBizService {
    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    TimeLimitedDiscountActivityService discountActivityService;
    @Autowired
    TimeLimitedDiscountShopService discountShopService;
    @Autowired
    TimeLimitedDiscountSkuService discountSkuService;
    @Autowired
    TimeLimitedDiscountSpuService discountSpuService;
    @Autowired
    TimeLimitedDiscountSkuMapper discountSkuMapper;
    @Autowired
    TimeLimitedDiscountSpuMapper discountSpuMapper;
    @Autowired
    TimeLimitedDiscountActivityMapper discountActivityMapper;
    @Autowired
    TimeLimitedDiscountShopMapper discountShopMapper;
    @Resource
    private ActivityCommodityBizService activityCommodityBizService;
    @Resource
    private CommodityPoolService commodityPoolService;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private TCouponActivityCentreFeignClient activityCentreFeignClient;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertActivity(TimeLimitedDiscountActivityDTO timeLimitedDiscountActivityDTO) {

        //检查调价是否低于吊牌价3折
        if(Objects.nonNull(timeLimitedDiscountActivityDTO.getType()) && timeLimitedDiscountActivityDTO.getType()!=3){
            checkDiscountPrice(timeLimitedDiscountActivityDTO);
        }

        TimeLimitedDiscountActivity timeLimitedDiscountActivity = mapperFacade.map(timeLimitedDiscountActivityDTO, TimeLimitedDiscountActivity.class);
        timeLimitedDiscountActivity.setId(null);
        timeLimitedDiscountActivity.setCreateUserId(AuthUserContext.get().getUserId());
        timeLimitedDiscountActivity.setCreateUserName(AuthUserContext.get().getUsername());
        timeLimitedDiscountActivity.setCreateTime(new Date());
        discountActivityService.save(timeLimitedDiscountActivity);

        if(CollectionUtil.isNotEmpty(timeLimitedDiscountActivityDTO.getShopIds())){
            discountShopService.bathcInsert(timeLimitedDiscountActivity.getId(),timeLimitedDiscountActivityDTO.getShopIds());
        }

        if(CollectionUtil.isNotEmpty(timeLimitedDiscountActivityDTO.getSpus())){
            for (TimeLimitedDiscountSpuDTO spus : timeLimitedDiscountActivityDTO.getSpus()) {
                //todo 同一个商品同一个时间段内只允许参加一个促销活动
                TimeLimitedDiscountSpu discountSpu = mapperFacade.map(spus, TimeLimitedDiscountSpu.class);
                discountSpu.setActivityId(timeLimitedDiscountActivity.getId());
                discountSpuService.save(discountSpu);
                //如果类型为按条码，并且sku列表不为空,则同时添加sku
                if(CollectionUtil.isNotEmpty(spus.getSkus())){
                    for (TimeLimitedDiscountSkuDTO skus : spus.getSkus()) {
                        skus.setActivityId(timeLimitedDiscountActivity.getId());
                    }
                    discountSkuMapper.insertBatch(spus.getSkus());
                    discountActivityService.removeCache(Long.valueOf(timeLimitedDiscountActivity.getId()));
                }
            }
        }
        //适用优惠券
        if(Objects.nonNull(timeLimitedDiscountActivityDTO.getType()) && timeLimitedDiscountActivityDTO.getType()==3){
            activityCentreFeignClient.saveTo(new TCouponActivityCentreAddDTO(timeLimitedDiscountActivity.getId().longValue(),
                    CouponActivityCentreSourceEnum.STORE_INVITE_PRICE.value(),
                    timeLimitedDiscountActivityDTO.getCouponIds()));
        }
        //商品池限制
        List<Long> spuIdList = new ArrayList<>();
        Set<Integer> uniqueValues = new HashSet<>();
        for (TimeLimitedDiscountSpuDTO timeLimitedDiscountSpuDTO : timeLimitedDiscountActivityDTO.getSpus()) {
            Integer spuId = timeLimitedDiscountSpuDTO.getSpuId();
            if (uniqueValues.add(spuId)) {
                spuIdList.add(spuId.longValue());
            }
        }
        if(Objects.nonNull(timeLimitedDiscountActivityDTO.getStatus()) && timeLimitedDiscountActivityDTO.getStatus()==1){//保存并且启用需要校验商品池
            Integer activityChannel=ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode();
            if(Objects.nonNull(timeLimitedDiscountActivityDTO.getType())){//1，限时调价。2，会员日活动调价
                activityChannel=getActivityChannel(timeLimitedDiscountActivityDTO.getType());
            }
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList,
                    timeLimitedDiscountActivityDTO.getActivityBeginTime(),
                    timeLimitedDiscountActivityDTO.getActivityEndTime(),
                    activityChannel,
                    Long.valueOf(timeLimitedDiscountActivity.getId()),
                    timeLimitedDiscountActivityDTO.getShopIds());
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }

        discountActivityService.removeCache(Long.valueOf(timeLimitedDiscountActivity.getId()));

    }

    private Integer getActivityChannel(Integer type){
        if(Objects.nonNull(type)){//1，限时调价。2，会员日活动调价
            if(type==1){
                return ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode();
            }else if(type==2){
                return ActivityChannelEnums.MEMBER_ACTIVITY.getCode();
            }else if(type==3){
                return ActivityChannelEnums.STORE_INVENT.getCode();
            }else{
                return ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode();
            }
        }
        return ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateActivity(TimeLimitedDiscountActivityDTO timeLimitedDiscountActivityDTO) {

        //检查调价是否低于吊牌价3折
        if(Objects.nonNull(timeLimitedDiscountActivityDTO.getType()) && timeLimitedDiscountActivityDTO.getType()!=3){
            checkDiscountPrice(timeLimitedDiscountActivityDTO);
        }

        TimeLimitedDiscountActivity timeLimitedDiscountActivity = mapperFacade.map(timeLimitedDiscountActivityDTO, TimeLimitedDiscountActivity.class);
        timeLimitedDiscountActivity.setUpdateUserId(AuthUserContext.get().getUserId());
        timeLimitedDiscountActivity.setUpdateUserName(AuthUserContext.get().getUsername());
        timeLimitedDiscountActivity.setUpdateTime(new Date());
        timeLimitedDiscountActivity.setCheckStatus(0);
        discountActivityService.update(timeLimitedDiscountActivity);

        //删除现有的参加商铺
        discountShopService.removeByActivityId(timeLimitedDiscountActivityDTO.getId());
        if(CollectionUtil.isNotEmpty(timeLimitedDiscountActivityDTO.getShopIds())){
            discountShopService.bathcInsert(timeLimitedDiscountActivity.getId(),timeLimitedDiscountActivityDTO.getShopIds());
        }

        //删除当前活动的参与商品信息
        discountSpuService.removeByActivity(timeLimitedDiscountActivityDTO.getId());
        discountSkuMapper.removeActivityId(timeLimitedDiscountActivityDTO.getId());
        if(CollectionUtil.isNotEmpty(timeLimitedDiscountActivityDTO.getSpus())){
            for (TimeLimitedDiscountSpuDTO spus : timeLimitedDiscountActivityDTO.getSpus()) {
                //todo 同一个商品同一个时间段内只允许参加一个促销活动
                if(timeLimitedDiscountActivity.getStatus()==1){

                }
                TimeLimitedDiscountSpu discountSpu = mapperFacade.map(spus, TimeLimitedDiscountSpu.class);
                discountSpu.setActivityId(timeLimitedDiscountActivityDTO.getId());
                discountSpuService.save(discountSpu);
                //如果类型为按条码，并且sku列表不为空,则同时添加sku
                if(CollectionUtil.isNotEmpty(spus.getSkus())){
                    for (TimeLimitedDiscountSkuDTO skus : spus.getSkus()) {
                        skus.setActivityId(timeLimitedDiscountActivityDTO.getId());
                    }
                    discountSkuMapper.insertBatch(spus.getSkus());
                }
            }
        }

        //适用优惠券
        if(Objects.nonNull(timeLimitedDiscountActivityDTO.getType()) && timeLimitedDiscountActivityDTO.getType()==3){
            activityCentreFeignClient.updateTo(new TCouponActivityCentreAddDTO(timeLimitedDiscountActivity.getId().longValue(),
                    CouponActivityCentreSourceEnum.STORE_INVITE_PRICE.value(),
                    timeLimitedDiscountActivityDTO.getCouponIds()));
        }

        //商品池限制
        List<Long> spuIdList = new ArrayList<>();
        Set<Integer> uniqueValues = new HashSet<>();
        for (TimeLimitedDiscountSpuDTO timeLimitedDiscountSpuDTO : timeLimitedDiscountActivityDTO.getSpus()) {
            Integer spuId = timeLimitedDiscountSpuDTO.getSpuId();
            if (uniqueValues.add(spuId)) {
                spuIdList.add(spuId.longValue());
            }
        }
        commodityPoolService.remove(new LambdaQueryWrapper<CommodityPool>().eq(CommodityPool::getActivityId,timeLimitedDiscountActivityDTO.getId()));

        if(Objects.nonNull(timeLimitedDiscountActivityDTO.getStatus()) && timeLimitedDiscountActivityDTO.getStatus()==1){//保存并且启用需要校验商品池
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList,
                    timeLimitedDiscountActivityDTO.getActivityBeginTime(),
                    timeLimitedDiscountActivityDTO.getActivityEndTime(),
                    ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode(),
                    Long.valueOf(timeLimitedDiscountActivity.getId()),
                    timeLimitedDiscountActivityDTO.getShopIds());
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }

        discountActivityService.removeCache(Long.valueOf(timeLimitedDiscountActivity.getId()));

//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList, timeLimitedDiscountActivityDTO.getActivityBeginTime(), timeLimitedDiscountActivityDTO.getActivityEndTime(), ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode(), Long.valueOf(timeLimitedDiscountActivity.getId()));
//        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
//            throw new LuckException("当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息", activityCommodityAddDTO);
//        }
    }

    //检查调价是否低于吊牌价3折
    private void checkDiscountPrice(TimeLimitedDiscountActivityDTO activityDTO){
        if(CollectionUtil.isNotEmpty(activityDTO.getSpus())){
            Map<String,String> errorPirceLog=new HashMap<>();
            for(TimeLimitedDiscountSpuDTO spuDTO:activityDTO.getSpus()){
                if(CollectionUtil.isNotEmpty(spuDTO.getSkus())){
                    Map<Long, Long> skuIds = spuDTO.getSkus().stream()
                            .collect(Collectors.toMap(TimeLimitedDiscountSkuDTO::getSkuId, discountSkuDTO -> discountSkuDTO.getSkuId()));
                    ServerResponseEntity<List<SkuVO>> response= skuFeignClient.listSkuCodeBypByIds(new ArrayList<>(skuIds.values()));
                    if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
                        Map<Long, TimeLimitedDiscountSkuDTO> discountSkuDTOMaps = spuDTO.getSkus().stream()
                                .collect(Collectors.toMap(TimeLimitedDiscountSkuDTO::getSkuId, spu -> spu,(k1, k2)->k1));
                        response.getData().stream().forEach(skuVO -> {
                            if(discountSkuDTOMaps.containsKey(skuVO.getSkuId())){
                                //判断3折兜底
                                Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
                                Long discountPrice= discountSkuDTOMaps.get(skuVO.getSkuId()).getPrice();
                                log.info("检查调价是否低于吊牌价3折--> marketPriceFee:{} marketDis3Price:{} discountPrice:{}",skuVO.getMarketPriceFee(),marketDis3Price,discountPrice);
                                if(discountPrice < marketDis3Price){
                                    String spuCode=skuVO.getSkuCode().split("-").length>1?skuVO.getSkuCode().substring(0, skuVO.getSkuCode().indexOf("-")):skuVO.getSkuCode();
                                    errorPirceLog.put(spuCode,"商品【"+spuCode+"】调价低于吊牌价3折," +
                                            "吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
                                }
                            }
                        });
                    }
                }
            }

            if(CollectionUtil.isNotEmpty(errorPirceLog)){
                throw new LuckException(errorPirceLog.values().toString());
            }
        }
    }

    @Override
    public TimeLimitedDiscountActivityVO detail(Integer id) {
        TimeLimitedDiscountActivityVO activityVO = discountActivityService.getById(id.longValue());
        Assert.isNull(activityVO,"当前活动不存在或已经删除。");

        List<TimeLimitedDiscountSpuVO> spus = discountSpuMapper.selectByActivityId(id);
        log.info("查询限时活动详情接口请求入参{},查询限时调价商品为{}", id, spus);
        if(CollectionUtil.isNotEmpty(spus)){
            List<Long> spuIds = spus.stream().map(s -> Long.valueOf(s.getSpuId())).collect(Collectors.toList());
            Map<Long, SkuVO> skuMaps=new HashMap<>();
            ServerResponseEntity<List<SkuVO>> responseSku=skuFeignClient.listSkusBySpuId(spuIds);
            log.info("调用商品服务获取商品数据结果为：{}", responseSku.getData());
            if(responseSku.isSuccess() && CollectionUtil.isNotEmpty(responseSku.getData())){
                skuMaps= responseSku.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId, a -> a,(k1, k2)->k1));
            }
            for (TimeLimitedDiscountSpuVO spuVO : spus) {
                List<TimeLimitedDiscountSkuVO> discountSkuVOS=discountSkuMapper.selectByActivityId(id,spuVO.getSpuId());
                //匹配skuCode
                if(CollectionUtil.isNotEmpty(discountSkuVOS) && CollectionUtil.isNotEmpty(skuMaps)){
                    for (TimeLimitedDiscountSkuVO discountSku:discountSkuVOS){
                        if(skuMaps.containsKey(discountSku.getSkuId())){
                            discountSku.setSkuCode(skuMaps.get(discountSku.getSkuId()).getSkuCode());
                        }
                    }
                }
                spuVO.setSkus(discountSkuVOS);
            }
        }
        activityVO.setSpus(spus);
        activityVO.setShops(discountShopMapper.selectByActivityId(id));
        return activityVO;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enable(Integer id) {
        TimeLimitedDiscountActivityVO activityVO = discountActivityService.getById(id.longValue());
        Assert.isNull(activityVO,"当前活动不存在或已经删除。");
        Assert.isTrue(activityVO.getStatus() == 1,"当前活动已经为启用状态，不允许重复操作");
        Assert.isTrue(activityVO.getCheckStatus() == 0,"当前活动未审核，不可启用");
        Assert.isTrue(activityVO.getCheckStatus() == 2,"当前活动审核未通过，不可启用");

        //todo 判断当前活动的产品跟其他产品是否冲突。
        List<TimeLimitedDiscountSpuVO> spus=discountSpuMapper.selectByActivityId(id);
        List<Long> spuIds = CollectionUtil.isNotEmpty(spus)?spus.stream().map(temp -> temp.getSpuId().longValue()).collect(Collectors.toList()):null;

        List<TimeLimitedDiscountShopVO> shops = activityVO.getIsAllShop()==0?discountShopMapper.selectByActivityId(id):null;
        List<Long> storeIds = CollectionUtil.isNotEmpty(shops)?shops.stream().map(temp -> temp.getShopId()).collect(Collectors.toList()):null;

        Integer activityChannel=ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode();
        if(Objects.nonNull(activityVO.getType())){//1，限时调价。2，会员日活动调价
            activityChannel=getActivityChannel(activityVO.getType());
        }

        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIds,
                activityVO.getActivityBeginTime(),
                activityVO.getActivityEndTime(),
                activityChannel,
                Long.valueOf(activityVO.getId()),
                storeIds);
        log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
            String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
            throw new LuckException(msg, activityCommodityAddDTO);
        }

        discountActivityMapper.enable(id.longValue());

        discountActivityService.removeCache(Long.valueOf(id.longValue()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disable(Integer id) {
        TimeLimitedDiscountActivityVO activityVO = discountActivityService.getById(id.longValue());
        Assert.isNull(activityVO,"当前活动不存在或已经删除。");
        Assert.isTrue(activityVO.getStatus() == 0,"当前活动已经为禁用状态，不允许重复操作");

        discountActivityMapper.disable(id.longValue());

        Integer activityChannel=ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode();
        if(Objects.nonNull(activityVO.getType())){//1，限时调价。2，会员日活动调价
            activityChannel=getActivityChannel(activityVO.getType());
        }

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),activityChannel);

        discountActivityService.removeCache(Long.valueOf(id.longValue()));
    }

    @Override
    public String checkSpuSkuPrice(Integer id) {
        TimeLimitedDiscountActivityVO activityVO = discountActivityService.getById(id.longValue());
        Assert.isNull(activityVO,"当前活动不存在或已经删除。");
        List<TimeLimitedDiscountSpuVO> spus = discountSpuMapper.selectByActivityId(id);
        String backMsg=null;
        if(CollectionUtil.isNotEmpty(spus)) {
            List<Long> spuIds = spus.stream().map(s -> Long.valueOf(s.getSpuId())).collect(Collectors.toList());
            List<TimeLimitedDiscountSkuVO> skuVOS=discountSkuMapper.selectByActivityAndSpuIds(id,spuIds);
            if(CollectionUtil.isNotEmpty(skuVOS)){
                Map<String,String> errorPirceLog=new HashMap<>();
                Map<Long, Long> skuIds = skuVOS.stream()
                        .collect(Collectors.toMap(TimeLimitedDiscountSkuVO::getSkuId, discountSkuDTO -> discountSkuDTO.getSkuId()));
                ServerResponseEntity<List<SkuVO>> response= skuFeignClient.listSkuCodeBypByIds(new ArrayList<>(skuIds.values()));
                if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
                    Map<Long, TimeLimitedDiscountSkuVO> discountSkuDTOMaps = skuVOS.stream()
                            .collect(Collectors.toMap(TimeLimitedDiscountSkuVO::getSkuId, spu -> spu,(k1, k2)->k1));
                    response.getData().stream().forEach(skuVO -> {
                        log.info("---> skucode:{} skuid:{} spuid:{}",skuVO.getSkuCode(),skuVO.getSkuId(),skuVO.getSpuId());
                        if(discountSkuDTOMaps.containsKey(skuVO.getSkuId())){
                            //判断3折兜底
                            Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
                            Long discountPrice= discountSkuDTOMaps.get(skuVO.getSkuId()).getPrice();
                            log.info("检查调价是否低于吊牌价3折--> marketPriceFee:{} marketDis3Price:{} discountPrice:{}",skuVO.getMarketPriceFee(),marketDis3Price,discountPrice);
                            if(discountPrice < marketDis3Price){
                                String spuCode=skuVO.getSkuCode().split("-").length>1?skuVO.getSkuCode().substring(0, skuVO.getSkuCode().indexOf("-")):skuVO.getSkuCode();
                                errorPirceLog.put(spuCode,"商品【"+spuCode+"】调价低于吊牌价3折," +
                                        "吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
                                return;
                            }
                        }
                    });
                }

                if(CollectionUtil.isNotEmpty(errorPirceLog) && errorPirceLog.size()>0){
                    backMsg=errorPirceLog.values().toString();
                }
            }
        }
        return backMsg;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addShop(TimeLimitedDiscountAddShopDTO addShopDTO) {
        Assert.isNull(addShopDTO.getActivityId(),"活动id不允许为空");
        Assert.isNull(addShopDTO.getShopId(),"活动添加门店不允许为空");
        TimeLimitedDiscountActivityVO activityVO = discountActivityMapper.getById(addShopDTO.getActivityId().longValue());
        Assert.isNull(activityVO,"活动不存在或已经删除。");

        for (Long shopId : addShopDTO.getShopId()) {
            //如果门店不存在，则新增该门店
            if(discountShopMapper.selectByActivityIdAndShopId(addShopDTO.getActivityId(),shopId.intValue())==null){
                TimeLimitedDiscountShop shop = new TimeLimitedDiscountShop();
                shop.setShopId(shopId);
                shop.setActivityId(addShopDTO.getActivityId());
                discountShopMapper.save(shop);
            }
        }
    }

    @Override
    public void deleteShop(Integer activityId, Integer shopId) {
        TimeLimitedDiscountActivityVO activityVO = discountActivityMapper.getById(activityId.longValue());
        Assert.isNull(activityVO,"活动不存在或已经删除。");
        discountShopMapper.deleteByActivityIdAndShopId(activityId,shopId);
    }
}
