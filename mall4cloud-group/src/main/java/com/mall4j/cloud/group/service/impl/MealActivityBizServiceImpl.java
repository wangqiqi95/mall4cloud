package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.order.dto.OrderActivityDTO;
import com.mall4j.cloud.api.order.dto.OrderConfirmDTO;
import com.mall4j.cloud.api.order.dto.OrderConfirmItemDTO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.dto.OrderDTO;
import com.mall4j.cloud.common.order.dto.ShopCartItemDTO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuAndSkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.MealActivity;
import com.mall4j.cloud.group.model.MealActivityShop;
import com.mall4j.cloud.group.model.MealCommodityPool;
import com.mall4j.cloud.group.model.MealCommodityPoolDetail;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.*;
import com.mall4j.cloud.group.vo.app.MealCommodityPoolAppVO;
import com.mall4j.cloud.group.vo.app.MealCommodityPoolDetailAppVO;
import com.mall4j.cloud.group.vo.app.MealInfoAppVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MealActivityBizServiceImpl implements MealActivityBizService {
    @Resource
    private MealActivityService mealActivityService;
    @Resource
    private MealCommodityPoolService mealCommodityPoolService;
    @Resource
    private MealCommodityPoolDetailService mealCommodityPoolDetailService;
    @Resource
    private ActivityCommodityBizService activityCommodityBizService;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private MealActivityShopService mealActivityShopService;
    @Resource
    private SpuFeignClient spuFeignClient;
    @Resource
    private OrderFeignClient orderFeignClient;
    @Resource
    private TimeLimitedDiscountActivityService timeLimitedDiscountActivityService;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    public ServerResponseEntity<Void> saveOrUpdateMealActivity(MealActivityDTO param) {
        Integer mealId = param.getId();
        List<MealCommodityPoolDTO> pools = param.getPools();
        Date activityEndTime = param.getActivityEndTime();
        Date activityBeginTime = param.getActivityBeginTime();

//        if (null != mealId) {
//            MealActivityVO mealActivityVO = mealActivityService.mealDetail(mealId);
//            Integer status = mealActivityVO.getStatus();
//            if (1 == status) {
//                List<Long> commodityIds = new ArrayList<>();
//                getCommodities(commodityIds, mealActivityVO);
//                activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(mealId),ActivityChannelEnums.MEAL_ACTIVITY.getCode());
//
//                List<Long> insertCommodityIds = new ArrayList<>();
//                pools.forEach(temp -> {
//                    List<MealCommodityPoolDetailDTO> details = temp.getDetails();
//                    List<Long> ids = details.stream().map(MealCommodityPoolDetailDTO::getCommodityId).collect(Collectors.toList());
//                    insertCommodityIds.addAll(ids);
//                });
//
//                String applyShopIds = param.getApplyShopIds();
//                List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
//                List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
//                ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIds,
//                        activityBeginTime,
//                        activityEndTime,
//                        ActivityChannelEnums.MEAL_ACTIVITY.getCode(),
//                        Long.valueOf(mealId),
//                        storeIds);
//                log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
//                if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
//                    String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
//                    throw new LuckException(msg, activityCommodityAddDTO);
//                }
//            }
//        }

        MealActivity mealActivity = BeanUtil.copyProperties(param, MealActivity.class);

        mealActivityService.saveOrUpdate(mealActivity);

        Integer id = mealActivity.getId();

        if (Objects.nonNull(param.getStatus()) && 1 == param.getStatus() && Objects.nonNull(id)) {//保存并且启用需要校验商品池

            List<Long> insertCommodityIds = new ArrayList<>();
            pools.forEach(temp -> {
                List<MealCommodityPoolDetailDTO> details = temp.getDetails();
                List<Long> ids = details.stream().map(MealCommodityPoolDetailDTO::getCommodityId).collect(Collectors.toList());
                insertCommodityIds.addAll(ids);
            });

            String applyShopIds = param.getApplyShopIds();
            List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
            List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIds,
                    activityBeginTime,
                    activityEndTime,
                    ActivityChannelEnums.MEAL_ACTIVITY.getCode(),
                    Long.valueOf(id),
                    storeIds);
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();

                mealActivity.setStatus(0);
                mealActivityService.updateById(mealActivity);

                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }

        String applyShopIds = param.getApplyShopIds();

        mealActivityShopService.remove(new LambdaQueryWrapper<MealActivityShop>().eq(MealActivityShop::getActivityId, id));
        if (StringUtils.isNotEmpty(applyShopIds)) {
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<MealActivityShop> mealActivityShops = new ArrayList<>();
            shopIds.forEach(temp -> {
                MealActivityShop mealActivityShop = MealActivityShop.builder()
                        .activityId(id)
                        .shopId(Long.valueOf(temp)).build();
                mealActivityShops.add(mealActivityShop);
            });
            mealActivityShopService.saveBatch(mealActivityShops);
        }

        //若为更新操作 则先删除之前的商品池
        if (null != mealId) {
            List<MealCommodityPool> list = mealCommodityPoolService.list(new LambdaQueryWrapper<MealCommodityPool>().eq(MealCommodityPool::getMealId, mealId));

            List<Integer> poolIds = list.stream().map(MealCommodityPool::getId).collect(Collectors.toList());

            mealCommodityPoolDetailService.remove(new LambdaQueryWrapper<MealCommodityPoolDetail>().in(MealCommodityPoolDetail::getCommodityPoolId, poolIds));
            mealCommodityPoolService.remove(new LambdaQueryWrapper<MealCommodityPool>().eq(MealCommodityPool::getMealId, mealId));
        }

        //添加商品池及商品
        pools.forEach(temp -> {
            MealCommodityPool mealCommodityPool = BeanUtil.copyProperties(temp, MealCommodityPool.class);
            mealCommodityPool.setMealId(id);
            mealCommodityPoolService.save(mealCommodityPool);
            Integer poolId = mealCommodityPool.getId();

            List<MealCommodityPoolDetailDTO> details = temp.getDetails();
            List<MealCommodityPoolDetail> detailList = Convert.toList(MealCommodityPoolDetail.class, details);

            List<MealCommodityPoolDetail> poolDetails = detailList.stream().peek(detail -> detail.setCommodityPoolId(poolId)).collect(Collectors.toList());
            mealCommodityPoolDetailService.saveBatch(poolDetails);
        });

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<MealActivityVO> detail(Integer id) {
        MealActivityVO mealActivityVO = mealActivityService.mealDetail(id);

        List<MealActivityShop> shops = mealActivityShopService.list(new LambdaQueryWrapper<MealActivityShop>().eq(MealActivityShop::getActivityId, id));
        mealActivityVO.setShops(shops);
        return ServerResponseEntity.success(mealActivityVO);
    }

    @Override
    public ServerResponseEntity<PageVO<MealActivityListVO>> page(MealActivityPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<MealActivityListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> mealActivityService.mealActivityList(param));

        List<MealActivityListVO> list = page.getResult();
        ;

        List<MealActivityListVO> resultList = list.stream().peek(temp -> {
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    Date activityBeginTime = temp.getActivityBeginTime();

                    if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                        if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                            temp.setStatus(ActivityStatusEnums.IN_PROGRESS.getCode());
                        } else if (date.compareTo(activityBeginTime) < 0) {
                            temp.setStatus(ActivityStatusEnums.NOT_START.getCode());
                        } else if (date.compareTo(activityEndTime) > 0) {
                            temp.setStatus(ActivityStatusEnums.END.getCode());
                        }
                    }
                }
        ).collect(Collectors.toList());

        PageVO<MealActivityListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<MealActivitySaveVO> enable(Integer id) {
        //校验当前商品是否在商品池内
        List<Long> commodityIds = new ArrayList<>();
        MealActivityVO mealActivityVO = mealActivityService.mealDetail(id);
        Date activityEndTime = mealActivityVO.getActivityEndTime();
        Date activityBeginTime = mealActivityVO.getActivityBeginTime();
        //组装商品id
        getCommodities(commodityIds, mealActivityVO);

        List<MealActivityShop> shops = mealActivityShopService.list(new LambdaQueryWrapper<MealActivityShop>().eq(MealActivityShop::getActivityId, id));
        List<Long> storeIds = CollectionUtil.isNotEmpty(shops)?shops.stream().map(temp -> temp.getShopId()).collect(Collectors.toList()):null;
        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(commodityIds,
                activityBeginTime,
                activityEndTime,
                ActivityChannelEnums.MEAL_ACTIVITY.getCode(),
                Long.valueOf(id),
                storeIds);
        log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
            String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
            throw new LuckException(msg, activityCommodityAddDTO);
        }

        //添加商品池并校验
//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(commodityIds, activityBeginTime, activityEndTime, ActivityChannelEnums.MEAL_ACTIVITY.getCode(), Long.valueOf(id));
//        List<String> failCommodityIds = activityCommodityAddDTO.getFailCommodityIds();
//        MealActivitySaveVO result = MealActivitySaveVO.builder().failCommodityIds(failCommodityIds).build();
//        if (CollectionUtil.isNotEmpty(failCommodityIds)) {
//            throw new LuckException("当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息", result);
//        }

        mealActivityService.update(new LambdaUpdateWrapper<MealActivity>()
                .set(MealActivity::getStatus, 1)
                .eq(MealActivity::getId, id));
        return ServerResponseEntity.success();
    }


    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        //校验当前商品是否在商品池内
        List<Long> commodityIds = new ArrayList<>();
        MealActivityVO mealActivityVO = mealActivityService.mealDetail(id);
        //组装商品id
        getCommodities(commodityIds, mealActivityVO);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),ActivityChannelEnums.MEAL_ACTIVITY.getCode());

        mealActivityService.update(new LambdaUpdateWrapper<MealActivity>()
                .set(MealActivity::getStatus, 0)
                .eq(MealActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        mealActivityService.update(new LambdaUpdateWrapper<MealActivity>()
                .set(MealActivity::getDeleted, 1)
                .eq(MealActivity::getId, id));

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),ActivityChannelEnums.MEAL_ACTIVITY.getCode());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<MealActivityShop>> getActivityShop(Integer activityId) {
        List<MealActivityShop> list = mealActivityShopService.list(new LambdaQueryWrapper<MealActivityShop>().eq(MealActivityShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<MealActivityShop> mealActivityShops = new ArrayList<>();
        shopIds.forEach(temp -> {
            MealActivityShop mealActivityShop = MealActivityShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            mealActivityShops.add(mealActivityShop);
        });

        mealActivityShopService.saveBatch(mealActivityShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId) {
        mealActivityShopService.remove(new LambdaQueryWrapper<MealActivityShop>()
                .eq(MealActivityShop::getActivityId, activityId)
                .eq(MealActivityShop::getShopId, shopId));

        List<MealActivityShop> list = mealActivityShopService.list(new LambdaQueryWrapper<MealActivityShop>().eq(MealActivityShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)) {
            mealActivityService.update(null, new LambdaUpdateWrapper<MealActivity>()
                    .set(MealActivity::getIsAllShop, 1)
                    .eq(MealActivity::getId, activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        mealActivityShopService.remove(new LambdaQueryWrapper<MealActivityShop>()
                .eq(MealActivityShop::getActivityId, activityId));
        mealActivityService.update(null, new LambdaUpdateWrapper<MealActivity>()
                .set(MealActivity::getIsAllShop, 1)
                .eq(MealActivity::getId, activityId));
        return ServerResponseEntity.success();
    }

    private void getCommodities(List<Long> commodityIds, MealActivityVO mealActivityVO) {
        List<MealCommodityPoolVO> tempPools = mealActivityVO.getPools();
        tempPools.forEach(temp -> {
            List<MealCommodityPoolDetailVO> details = temp.getDetails();
            List<Long> commodityIdsTemp = details.stream().map(MealCommodityPoolDetailVO::getCommodityId).collect(Collectors.toList());
            commodityIds.addAll(commodityIdsTemp);
        });
    }

    @Override
    public ServerResponseEntity<MealInfoAppVO> info(String shopId) {
        MealActivity mealActivity = mealActivityService.selectFirstActivity(shopId);

        if (null == mealActivity) {
            throw new LuckException("无可用套餐一口价活动");
        }

        Integer id = mealActivity.getId();

        MealInfoAppVO mealInfoAppVO = mealActivityService.mealInfo(id);

        List<MealCommodityPoolAppVO> pools = mealInfoAppVO.getPools();

        List<Long> commodityIdList = new ArrayList<>();

        pools.forEach(temp -> {
            List<MealCommodityPoolDetailAppVO> details = temp.getDetails();
            List<Long> commodityIds = details.stream().map(MealCommodityPoolDetailAppVO::getCommodityId).collect(Collectors.toList());
            commodityIdList.addAll(commodityIds);
        });

        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        productSearchDTO.setPageNum(1);
        productSearchDTO.setPageSize(600);
        productSearchDTO.setSpuIds(commodityIdList);
        productSearchDTO.setStoreId(Long.parseLong(shopId));
        ServerResponseEntity<PageVO<SpuCommonVO>> pageVOServerResponseEntity = productFeignClient.commonSearch(productSearchDTO);
        PageVO<SpuCommonVO> data = pageVOServerResponseEntity.getData();
        List<SpuCommonVO> list = data.getList();
        Map<Long, SpuCommonVO> searchVOMap = list.stream().collect(Collectors.toMap(SpuCommonVO::getSpuId, s -> s));

        pools.forEach(temp -> {
            List<MealCommodityPoolDetailAppVO> details = temp.getDetails();
            details.forEach(tempDetail ->
                    tempDetail.setCommodityInfo(searchVOMap.get(tempDetail.getCommodityId()))
            );
            temp.setDetails(details);
        });
        mealInfoAppVO.setPools(pools);
        return ServerResponseEntity.success(mealInfoAppVO);
    }

    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> submit(Integer activityId, OrderDTO param) {

        log.info("一口价订单提交信息 {}", JSON.toJSONString(param));
        Long storeId=param.getStoreId();

        Long userId = AuthUserContext.get().getUserId();
        MealActivityVO mealActivityVO = mealActivityService.mealDetail(activityId);
        List<MealCommodityPoolVO> pools = mealActivityVO.getPools();

        Date activityBeginTime = mealActivityVO.getActivityBeginTime();
        Date activityEndTime = mealActivityVO.getActivityEndTime();
        String activityName = mealActivityVO.getActivityName();
        Integer status = mealActivityVO.getStatus();
        if (1 != status) {
            throw new LuckException("活动未启用！");
        }
        if (!DateUtil.isIn(new Date(), activityBeginTime, activityEndTime)) {
            throw new LuckException("不在活动时间范围内！");
        }

        List<ShopCartItemDTO> shopCartItemList = param.getShopCartItemList();

        Integer sum = 0;
        int totalCount = shopCartItemList.stream().mapToInt(ShopCartItemDTO::getCount).sum();

        for (MealCommodityPoolVO pool :
                pools) {
            Integer commodityPoolNum = pool.getCommodityPoolNum();
            sum += commodityPoolNum;
        }

        if (!sum.equals(totalCount)) {
            throw new LuckException("商品总数与可选总数不符！");
        }

        Integer buyRepeatSwitch = mealActivityVO.getBuyRepeatSwitch();

        if (buyRepeatSwitch == 0) {
            if (totalCount != shopCartItemList.size()) {
                throw new LuckException("活动不可购买重复商品！");
            }
        }

        Map<Long, List<ShopCartItemDTO>> commodityPoolMaps = shopCartItemList.stream().collect(Collectors.groupingBy(ShopCartItemDTO::getId));
        pools.forEach(temp -> {
            Integer commodityPoolNum = temp.getCommodityPoolNum();
            int commodityPoolCount=commodityPoolMaps.get(temp.getId().longValue()).size();
            if (commodityPoolCount != commodityPoolNum) {
                throw new LuckException("商品池内可选商品数量不相符!");
            }
        });

        boolean isInviteStore=false;
        ServerResponseEntity<Boolean> inviteStoreResponse=storeFeignClient.isInviteStore(storeId);
        if(inviteStoreResponse.isSuccess() && inviteStoreResponse.getData()){
            isInviteStore=inviteStoreResponse.getData();
        }


        BigDecimal mealPrice = mealActivityVO.getMealPrice();
        long mealPriceLong = mealPrice.multiply(new BigDecimal("100")).longValue();
        OrderConfirmDTO orderConfirmDTO = BeanUtil.copyProperties(param, OrderConfirmDTO.class);
        orderConfirmDTO.setUseCoupon(mealActivityVO.getPreferenceType() == 0 ? 0 : 2);
        orderConfirmDTO.setOrderType(4);
        orderConfirmDTO.setActualTotal(mealPriceLong);
        orderConfirmDTO.setUserId(userId);
        //限时调价逻辑
        List<Long> skuIdList = shopCartItemList.stream().map(ShopCartItemDTO::getSkuId).collect(Collectors.toList());
        TimeDiscountActivityDTO timeDiscountActivityDTO = new TimeDiscountActivityDTO();
        timeDiscountActivityDTO.setShopId(orderConfirmDTO.getStoreId());
        timeDiscountActivityDTO.setSkuIds(skuIdList);
        List<TimeDiscountActivityVO> timeDiscountActivityVOS = timeLimitedDiscountActivityService.convertActivityPrice(timeDiscountActivityDTO);
        HashMap<Long, Long> skuPriceFeeMap = new HashMap<>();
        timeDiscountActivityVOS.forEach(timeDiscountActivityVO -> {
            skuPriceFeeMap.put(timeDiscountActivityVO.getSkuId(), timeDiscountActivityVO.getPrice());
        });
        long commodityTotalPrice = 0L;
        List<OrderConfirmItemDTO> orderConfirmItems = new ArrayList<>();
        for (ShopCartItemDTO shopCartItemDTO : shopCartItemList) {
//            Long shopId = shopCartItemDTO.getShopId();
            Long shopId = storeId;
            Long skuId = shopCartItemDTO.getSkuId();
            Long spuId = shopCartItemDTO.getSpuId();
            ServerResponseEntity<SpuAndSkuVO> spuAndSkuById = spuFeignClient.getSpuAndSkuById(spuId, skuId, shopId);
            SpuAndSkuVO data = spuAndSkuById.getData();
            SkuVO sku = data.getSku();
            SpuVO spu = data.getSpu();
            log.info("一口价订单提交信息 sku: {}",JSON.toJSONString(sku));
            Long timePrice = skuPriceFeeMap.get(skuId);
            if(isInviteStore){//虚拟门店取价
                if(sku.getSkuProtectPrice()>0){
                    timePrice=sku.getSkuProtectPrice();
                }
            }else{
                //剔除门店无库存(取官店价格)、门店有库存且有保护价(取保护价) -> sku,不参与活动价
                if(sku.getStoreSkuStock()<=0 || (sku.getStoreProtectPrice()>0 && sku.getStoreSkuStock()>0)){
                    timePrice=sku.getPriceFee();
                }
            }

            Long priceFee = Objects.nonNull(timePrice) ? timePrice : sku.getPriceFee();
            Integer count = shopCartItemDTO.getCount();
            long priceCommodity = priceFee * count;
            commodityTotalPrice = commodityTotalPrice + (priceCommodity);


            OrderConfirmItemDTO orderConfirmItemDTO = new OrderConfirmItemDTO();
            orderConfirmItemDTO.setPriceFee(priceFee);
            orderConfirmItemDTO.setCategoryId(spu.getCategoryId());
            orderConfirmItemDTO.setMarketPrice(sku.getMarketPriceFee());
            orderConfirmItemDTO.setTotalPriceFee(priceCommodity);
            orderConfirmItemDTO.setSkuId(skuId);
            orderConfirmItemDTO.setSkuName(sku.getSkuName());
            orderConfirmItemDTO.setSpuId(spuId);
            orderConfirmItemDTO.setSpuName(spu.getName());
            orderConfirmItemDTO.setUserId(userId);
            orderConfirmItemDTO.setCount(shopCartItemDTO.getCount());

            orderConfirmItems.add(orderConfirmItemDTO);
        }

        orderConfirmDTO.setTotal(commodityTotalPrice);
        orderConfirmDTO.setOrderReduce(commodityTotalPrice - mealPriceLong);
        orderConfirmDTO.setTotalCount(totalCount);

        OrderActivityDTO orderActivityDTO = new OrderActivityDTO();
        orderActivityDTO.setActivityId(Long.valueOf(activityId));
        orderActivityDTO.setActivityName(activityName);
        orderActivityDTO.setActivityReduceTotal(commodityTotalPrice - mealPriceLong);

        orderConfirmDTO.setActivityDTO(orderActivityDTO);

        long curPriceTotal = mealPriceLong;
        for (int i = 0; i < orderConfirmItems.size(); i++) {
            OrderConfirmItemDTO orderConfirmItemDTO = orderConfirmItems.get(i);
            Integer count = orderConfirmItemDTO.getCount();
            BigDecimal countDecimal = new BigDecimal(count);
            Long totalPriceFee = orderConfirmItemDTO.getTotalPriceFee();
            if (i != orderConfirmItems.size() - 1) {
                BigDecimal totalPriceFeeDecimal = new BigDecimal(totalPriceFee);
                BigDecimal commodityTotalPriceDecimal = new BigDecimal(commodityTotalPrice);
                BigDecimal mealPriceLongDecimal = new BigDecimal(mealPriceLong);
                long curPrice = ((totalPriceFeeDecimal.divide(commodityTotalPriceDecimal, 2, RoundingMode.HALF_DOWN)).multiply(mealPriceLongDecimal)).longValue();

                BigDecimal curPriceDecimal = new BigDecimal(curPrice);
//                orderConfirmItemDTO.setPriceFee(curPriceDecimal.divide(countDecimal, 0, RoundingMode.HALF_UP).longValue());
                orderConfirmItemDTO.setActualTotal(curPrice);
                orderConfirmItemDTO.setTotalReducePrice(totalPriceFee - curPrice);
                curPriceTotal = curPriceTotal - curPrice;
            } else {
                BigDecimal curPriceDecimal = new BigDecimal(curPriceTotal);
                orderConfirmItemDTO.setTotalReducePrice(totalPriceFee - curPriceTotal);
//                orderConfirmItemDTO.setPriceFee(curPriceDecimal.divide(countDecimal, 0, RoundingMode.HALF_UP).longValue());
                orderConfirmItemDTO.setActualTotal(curPriceTotal);
            }
        }
        orderConfirmDTO.setOrderConfirmItemDTOList(orderConfirmItems);


        ServerResponseEntity<ShopCartOrderMergerVO> confirm;
        try {
            log.info("提交一口价订单信息：{}",JSON.toJSONString(orderConfirmDTO));
            confirm = orderFeignClient.confirm(orderConfirmDTO);
        } catch (Exception e) {
            throw new LuckException("调用订单接口失败！");
        }

        return confirm;
    }
}
