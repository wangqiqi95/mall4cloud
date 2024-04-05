package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.group.feign.vo.OrderGiftInfoAppVO;
import com.mall4j.cloud.api.group.feign.vo.OrderGiftStockAppVO;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.OrderGiftInfoVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.MealActivityShop;
import com.mall4j.cloud.group.model.OrderGift;
import com.mall4j.cloud.group.model.OrderGiftShop;
import com.mall4j.cloud.group.model.OrderGiftStock;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.OrderGiftListVO;
import com.mall4j.cloud.group.vo.OrderGiftSaveVO;
import com.mall4j.cloud.group.vo.OrderGiftStockVO;
import com.mall4j.cloud.group.vo.OrderGiftVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderGiftBizServiceImpl implements OrderGiftBizService {
    @Resource
    private OrderGiftService orderGiftService;
    @Resource
    private OrderGiftStockService orderGiftStockService;
    @Resource
    private ActivityCommodityBizService activityCommodityBizService;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private OrderGiftShopService orderGiftShopService;

    @Override
    public ServerResponseEntity<Void> saveOrUpdateOrderGiftActivity(OrderGiftDTO param) {
        Integer orderGiftId = param.getId();
        List<OrderGiftStockDTO> stocks = param.getStocks();
        Date activityEndTime = param.getActivityEndTime();
        Date activityBeginTime = param.getActivityBeginTime();

        OrderGift orderGift = BeanUtil.copyProperties(param, OrderGift.class);
        List<OrderGiftStock> collect = Convert.toList(OrderGiftStock.class, stocks);

        orderGiftService.saveOrUpdate(orderGift);
        Integer id = orderGift.getId();

        //若为修改则添加商品池并判断是否有商品在商品池中
        if (Objects.nonNull(param.getStatus()) && 1 == param.getStatus() && Objects.nonNull(id)) {//保存并且启用需要校验商品池

            String insertCommodityIds = param.getApplyCommodityIds();
            List<String> insertCommodityIdList = Arrays.asList(insertCommodityIds.split(StringPool.COMMA));
            List<Long> insertCommodityIdListLong = Convert.toList(Long.class, insertCommodityIdList);

            String applyShopIds = param.getApplyShopIds();
            List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
            List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdListLong,
                    activityBeginTime,
                    activityEndTime,
                    ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode(),
                    Long.valueOf(id),
                    storeIds);
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();

                orderGift.setStatus(0);
                orderGiftService.updateById(orderGift);

                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }


        String applyShopIds = param.getApplyShopIds();

        orderGiftShopService.remove(new LambdaQueryWrapper<OrderGiftShop>().eq(OrderGiftShop::getActivityId, id));
        if (StringUtils.isNotEmpty(applyShopIds)) {
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<OrderGiftShop> orderGiftShops = new ArrayList<>();
            shopIds.forEach(temp -> {
                OrderGiftShop orderGiftShop = OrderGiftShop.builder()
                        .activityId(id)
                        .shopId(Long.valueOf(temp)).build();
                orderGiftShops.add(orderGiftShop);
            });
            orderGiftShopService.saveBatch(orderGiftShops);
        }

        List<OrderGiftStock> orderGiftStocks = collect.stream().peek(temp -> temp.setOrderGiftId(id)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(orderGiftStocks)) {
            orderGiftStocks.forEach(temp -> {
                Long commodityId = temp.getCommodityId();
                String redisKey = "orderGift::" + id + "::" + commodityId;
                Integer commodityStock = temp.getCommodityMaxStock();
                RedisUtil.set(redisKey, commodityStock, -1);

                //商品当前库存不做更新，仅在下单后转赠累加用于显示已转赠数量
                OrderGiftStock orderGiftStock=orderGiftStockService.getOne(new LambdaQueryWrapper<OrderGiftStock>()
                        .eq(OrderGiftStock::getOrderGiftId,temp.getOrderGiftId())
                        .eq(OrderGiftStock::getCommodityId,temp.getCommodityId()),false);
                log.info("下单赠品设置 temp:{} orderGiftStock:{}",JSON.toJSONString(temp),Objects.nonNull(orderGiftStock)?JSON.toJSONString(orderGiftStock):"未获取到orderGiftStock");
                if(Objects.nonNull(orderGiftStock)){
                    //已赠送库存（活动扣减库存后再次编辑增加库存需要重新计算当前已赠送库存数量）
                    Integer giftNum=orderGiftStock.getCommodityMaxStock()-orderGiftStock.getCommodityStock();
                    log.info("下单赠品设置 giftNum：{}  计算后已赠送剩余库存{}", giftNum,(commodityStock-giftNum));
                    temp.setCommodityStock(commodityStock-giftNum);
                }else{
                    temp.setCommodityStock(commodityStock);
                }
            });
            orderGiftStockService.remove(new LambdaQueryWrapper<OrderGiftStock>().eq(OrderGiftStock::getOrderGiftId, id));
            orderGiftStockService.saveBatch(orderGiftStocks);
        }

        return ServerResponseEntity.success();
    }

//    @Override
//    public ServerResponseEntity<Void> saveOrUpdateOrderGiftActivity(OrderGiftDTO param) {
//        Integer orderGiftId = param.getId();
//        List<OrderGiftStockDTO> stocks = param.getStocks();
//        Date activityEndTime = param.getActivityEndTime();
//        Date activityBeginTime = param.getActivityBeginTime();
//
////        if (null != orderGiftId) {
////            OrderGift orderGift = orderGiftService.getById(orderGiftId);
////            Integer status = orderGift.getStatus();
////            if (1 == status) {
////                String applyCommodityIds = orderGift.getApplyCommodityIds();
////                List<String> commodityIds = Arrays.asList(applyCommodityIds.split(StringPool.COMMA));
////                activityCommodityBizService.removeActivityCommodity(commodityIds);
////
////                String insertCommodityIds = param.getApplyCommodityIds();
////                List<String> insertCommodityIdList = Arrays.asList(insertCommodityIds.split(StringPool.COMMA));
////                List<Long> insertCommodityIdListLong = Convert.toList(Long.class, insertCommodityIdList);
////
////                String applyShopIds = param.getApplyShopIds();
////                List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
////                List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
////                ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdListLong,
////                        activityBeginTime,
////                        activityEndTime,
////                        ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode(),
////                        Long.valueOf(orderGiftId),
////                        storeIds,
////                        false);
////                log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
////                if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
////                    String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
////                    throw new LuckException(msg, activityCommodityAddDTO);
////                }
//////                activityCommodityBizService.addActivityCommodity(insertCommodityIdListLong, activityBeginTime, activityEndTime, ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode(), Long.valueOf(orderGiftId));
////            }
////        }
//
//        OrderGift orderGift = BeanUtil.copyProperties(param, OrderGift.class);
//        List<OrderGiftStock> collect = Convert.toList(OrderGiftStock.class, stocks);
//
//        orderGiftService.saveOrUpdate(orderGift);
//        Integer id = orderGift.getId();
//
//        //若为修改则添加商品池并判断是否有商品在商品池中
//        if (Objects.nonNull(param.getStatus()) && 1 == param.getStatus() && Objects.nonNull(id)) {//保存并且启用需要校验商品池
//
//            String insertCommodityIds = param.getApplyCommodityIds();
//            List<String> insertCommodityIdList = Arrays.asList(insertCommodityIds.split(StringPool.COMMA));
//            List<Long> insertCommodityIdListLong = Convert.toList(Long.class, insertCommodityIdList);
//
//            String applyShopIds = param.getApplyShopIds();
//            List<String> shopIds = StrUtil.isNotBlank(applyShopIds)?Arrays.asList(applyShopIds.split(StringPool.COMMA)):null;
//            List<Long> storeIds = CollectionUtil.isNotEmpty(shopIds)?shopIds.stream().map(temp -> Long.parseLong(temp)).collect(Collectors.toList()):null;
//            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdListLong,
//                    activityBeginTime,
//                    activityEndTime,
//                    ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode(),
//                    Long.valueOf(id),
//                    storeIds);
//            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
//            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
//                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
//
//                orderGift.setStatus(0);
//                orderGiftService.updateById(orderGift);
//
//                throw new LuckException(msg, activityCommodityAddDTO);
//            }
//        }
//
//
//        String applyShopIds = param.getApplyShopIds();
//
//        orderGiftShopService.remove(new LambdaQueryWrapper<OrderGiftShop>().eq(OrderGiftShop::getActivityId, id));
//        if (StringUtils.isNotEmpty(applyShopIds)) {
//            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
//            List<OrderGiftShop> orderGiftShops = new ArrayList<>();
//            shopIds.forEach(temp -> {
//                OrderGiftShop orderGiftShop = OrderGiftShop.builder()
//                        .activityId(id)
//                        .shopId(Long.valueOf(temp)).build();
//                orderGiftShops.add(orderGiftShop);
//            });
//            orderGiftShopService.saveBatch(orderGiftShops);
//        }
//
//        List<OrderGiftStock> orderGiftStocks = collect.stream().peek(temp -> temp.setOrderGiftId(id)).collect(Collectors.toList());
//        if (CollectionUtil.isNotEmpty(orderGiftStocks)) {
//            orderGiftStocks.forEach(temp -> {
//                Long commodityId = temp.getCommodityId();
//                String redisKey = "orderGift::" + id + "::" + commodityId;
//                Integer commodityStock = temp.getCommodityMaxStock();
//                RedisUtil.set(redisKey, commodityStock, -1);
//
//                //商品当前库存不做更新，仅在下单后转赠累加用于显示已转赠数量
//                OrderGiftStock orderGiftStock=orderGiftStockService.getOne(new LambdaQueryWrapper<OrderGiftStock>()
//                        .eq(OrderGiftStock::getOrderGiftId,temp.getOrderGiftId())
//                        .eq(OrderGiftStock::getCommodityId,temp.getCommodityId()),false);
//                if(Objects.nonNull(orderGiftStock)){
//                    temp.setCommodityStock(orderGiftStock.getCommodityStock());
//                }else{
//                    temp.setCommodityStock(commodityStock);
//                }
//            });
//            orderGiftStockService.remove(new LambdaQueryWrapper<OrderGiftStock>().eq(OrderGiftStock::getOrderGiftId, id));
//            orderGiftStockService.saveBatch(orderGiftStocks);
//        }
//
//        return ServerResponseEntity.success();
//    }

    @Override
    public ServerResponseEntity<OrderGiftVO> detail(Integer id) {
        OrderGift orderGift = orderGiftService.getById(id);
        List<OrderGiftStock> list = orderGiftStockService.list(new LambdaQueryWrapper<OrderGiftStock>().eq(OrderGiftStock::getOrderGiftId, id));

        OrderGiftVO orderGiftVO = BeanUtil.copyProperties(orderGift, OrderGiftVO.class);
        List<OrderGiftStockVO> orderGiftStockVOS = Convert.toList(OrderGiftStockVO.class, list);

        orderGiftStockVOS.forEach(temp->
            temp.setGiftNum(temp.getCommodityMaxStock()-temp.getCommodityStock())
        );
        orderGiftVO.setStocks(orderGiftStockVOS);

        List<OrderGiftShop> shops = orderGiftShopService.list(new LambdaQueryWrapper<OrderGiftShop>().eq(OrderGiftShop::getActivityId, id));
        orderGiftVO.setShops(shops);
        return ServerResponseEntity.success(orderGiftVO);
    }

    @Override
    public ServerResponseEntity<PageVO<OrderGiftListVO>> page(OpenScreenAdPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<OrderGiftListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> orderGiftService.orderGiftList(param));

        List<OrderGiftListVO> list = page.getResult();

        List<OrderGiftListVO> resultList = list.stream().peek(temp -> {
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

        PageVO<OrderGiftListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        OrderGift orderGift = orderGiftService.getById(id);
        Date activityBeginTime = orderGift.getActivityBeginTime();
        Date activityEndTime = orderGift.getActivityEndTime();
        String applyCommodityIds = orderGift.getApplyCommodityIds();

        List<String> commodityIds = Arrays.asList(applyCommodityIds.split(StringPool.COMMA));
        List<Long> insertCommodityIdListLong = Convert.toList(Long.class, commodityIds);

        List<OrderGiftShop> shops = orderGift.getIsAllShop()==0?orderGiftShopService.list(new LambdaQueryWrapper<OrderGiftShop>().eq(OrderGiftShop::getActivityId, id)):null;
        List<Long> storeIds = CollectionUtil.isNotEmpty(shops)?shops.stream().map(temp -> temp.getShopId()).collect(Collectors.toList()):null;
        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdListLong,
                activityBeginTime,
                activityEndTime,
                ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode(),
                Long.valueOf(id),
                storeIds);
        log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
            String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
            throw new LuckException(msg, activityCommodityAddDTO);
        }

//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(insertCommodityIdListLong, activityBeginTime, activityEndTime, ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode(), Long.valueOf(id));
//        List<String> failCommodityIds = activityCommodityAddDTO.getFailCommodityIds();
//        OrderGiftSaveVO result = OrderGiftSaveVO.builder().failCommodityIds(failCommodityIds).build();
//        if (CollectionUtil.isNotEmpty(failCommodityIds)) {
//            throw new LuckException("当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息", result);
//        }

        orderGiftService.update(new LambdaUpdateWrapper<OrderGift>()
                .set(OrderGift::getStatus, 1)
                .eq(OrderGift::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
//        OrderGift orderGift = orderGiftService.getById(id);
//        String applyCommodityIds = orderGift.getApplyCommodityIds();
//        List<String> commodityIds = Arrays.asList(applyCommodityIds.split(StringPool.COMMA));
//        activityCommodityBizService.removeActivityCommodity(commodityIds);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode());

        orderGiftService.update(new LambdaUpdateWrapper<OrderGift>()
                .set(OrderGift::getStatus, 0)
                .eq(OrderGift::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        orderGiftService.update(new LambdaUpdateWrapper<OrderGift>()
                .set(OrderGift::getDeleted, 1)
                .eq(OrderGift::getId, id));

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id),ActivityChannelEnums.ORDER_GIFT_ACTIVITY.getCode());

        return ServerResponseEntity.success();
    }


    @Override
    public ServerResponseEntity<List<OrderGiftShop>> getActivityShop(Integer activityId) {
        List<OrderGiftShop> list = orderGiftShopService.list(new LambdaQueryWrapper<OrderGiftShop>().eq(OrderGiftShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<OrderGiftShop> orderGiftShops = new ArrayList<>();
        shopIds.forEach(temp -> {
            OrderGiftShop orderGiftShop = OrderGiftShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            orderGiftShops.add(orderGiftShop);
        });

        orderGiftShopService.saveBatch(orderGiftShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId) {
        orderGiftShopService.remove(new LambdaQueryWrapper<OrderGiftShop>()
                .eq(OrderGiftShop::getActivityId, activityId)
                .eq(OrderGiftShop::getShopId, shopId));

        List<OrderGiftShop> list = orderGiftShopService.list(new LambdaQueryWrapper<OrderGiftShop>().eq(OrderGiftShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)) {
            orderGiftService.update(null, new LambdaUpdateWrapper<OrderGift>()
                    .set(OrderGift::getIsAllShop, 1)
                    .eq(OrderGift::getId, activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        orderGiftShopService.remove(new LambdaQueryWrapper<OrderGiftShop>()
                .eq(OrderGiftShop::getActivityId, activityId));
        orderGiftService.update(null, new LambdaUpdateWrapper<OrderGift>()
                .set(OrderGift::getIsAllShop, 1)
                .eq(OrderGift::getId, activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public List<OrderGiftInfoVO> giftInfoBySpuIdAndStoreId(List<Long> spuIdList, Long storeId) {
        OrderGiftInfoAppDTO param = new OrderGiftInfoAppDTO();
        param.setShopId(storeId);
        ArrayList<OrderGift> orderGiftList = new ArrayList<>();
        spuIdList.forEach(spuId -> {
            param.setCommodityId(spuId);
            OrderGift orderGift = orderGiftService.selectFirstActivity(param);
            if (Objects.nonNull(orderGift)) {
                orderGiftList.add(orderGift);
            }
        });
        List<Integer> orderGiftIdList = orderGiftList.stream().map(OrderGift::getId).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderGiftIdList)){
            return new ArrayList<>();
        }
        Map<Integer, OrderGift> orderGiftMap = orderGiftList.stream().distinct().collect(Collectors.toMap(OrderGift::getId, orderGift -> orderGift));
        List<OrderGiftStock> orderGiftStocks = orderGiftStockService.list(new LambdaQueryWrapper<OrderGiftStock>().in(OrderGiftStock::getOrderGiftId, orderGiftIdList));
        List<OrderGiftInfoVO> orderGiftInfoVOS = orderGiftStocks.stream().map(orderGiftStock -> {
            OrderGiftInfoVO orderGiftInfoVO = new OrderGiftInfoVO();
            orderGiftInfoVO.setIsChoose(0);
            orderGiftInfoVO.setNum(orderGiftMap.get(orderGiftStock.getOrderGiftId()).getGiftLimit());
            orderGiftInfoVO.setSpuId(orderGiftStock.getCommodityId());
            orderGiftInfoVO.setStock(Objects.nonNull(orderGiftStock.getCommodityStock())?orderGiftStock.getCommodityStock():0);
            orderGiftInfoVO.setGiftActivityId(orderGiftStock.getOrderGiftId());
            return orderGiftInfoVO;
        }).collect(Collectors.toList());
        return orderGiftInfoVOS;
    }

    @Override
    public ServerResponseEntity<OrderGiftInfoAppVO> info(OrderGiftInfoAppDTO param) {
        OrderGift orderGift = orderGiftService.selectFirstActivity(param);

        if (null == orderGift) {
            throw new LuckException("该商品未参加下单送赠品活动");
        }

        Integer id = orderGift.getId();

        List<OrderGiftStock> orderGiftStocks = orderGiftStockService.list(new LambdaQueryWrapper<OrderGiftStock>().eq(OrderGiftStock::getOrderGiftId, id));

        List<Long> commodityIds = orderGiftStocks.stream().map(OrderGiftStock::getCommodityId).collect(Collectors.toList());

        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        productSearchDTO.setPageNum(1);
        productSearchDTO.setPageSize(600);
        productSearchDTO.setSpuIds(commodityIds);
        ServerResponseEntity<PageVO<SpuCommonVO>> pageVOServerResponseEntity = productFeignClient.commonSearch(productSearchDTO);
        PageVO<SpuCommonVO> data = pageVOServerResponseEntity.getData();
        List<SpuCommonVO> list = data.getList();
        Map<Long, SpuCommonVO> spuSearchVOMap = list.stream().collect(Collectors.toMap(SpuCommonVO::getSpuId, s -> s));

        List<OrderGiftStockAppVO> stockAppVOS = Convert.toList(OrderGiftStockAppVO.class, orderGiftStocks);

        List<OrderGiftStockAppVO> stockResult = stockAppVOS.stream().peek(temp -> temp.setCommodityInfo(spuSearchVOMap.get(temp.getCommodityId()))).collect(Collectors.toList());

        OrderGiftInfoAppVO result = BeanUtil.copyProperties(orderGift, OrderGiftInfoAppVO.class);

        result.setGifts(stockResult);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> reduce(List<OrderGiftReduceAppDTO> param) {
        Integer id = param.get(0).getActivityId();
        OrderGift orderGift = orderGiftService.getById(id);
        Integer giftLimit = orderGift.getGiftLimit();
        int sum = param.stream().mapToInt(OrderGiftReduceAppDTO::getNum).sum();

        if (sum > giftLimit) {
            throw new LuckException("超过赠品最大件数限制");
        }

        //删除这里判断库存redis的操作
//        boolean flag = false;
//        for (OrderGiftReduceAppDTO temp : param) {
//            Integer activityId = temp.getActivityId();
//            Long commodityId = temp.getCommodityId();
//            Integer num = temp.getNum();
//            String redisKey = "orderGift::" + activityId + "::" + commodityId;
//            Long decr = RedisUtil.decr(redisKey, num);
//            if (decr < 0) {
//                flag = true;
//            }
//        }
//        if (flag) {
//            for (OrderGiftReduceAppDTO temp : param) {
//                Integer activityId = temp.getActivityId();
//                Long commodityId = temp.getCommodityId();
//                Integer num = temp.getNum();
//                String redisKey = "orderGift::" + activityId + "::" + commodityId;
//                RedisUtil.incr(redisKey, num);
//            }
//            throw new LuckException("赠品库存不足");
//        }

        param.forEach(temp -> {
            Long commodityId = temp.getCommodityId();
            Integer num = temp.getNum();
            Integer orderGiftId = temp.getActivityId();
            orderGiftStockService.reduceStock(orderGiftId, commodityId, num);
        });

        return ServerResponseEntity.success();
    }


    @Override
    public ServerResponseEntity<Void> unlockStock(List<OrderGiftReduceAppDTO> param) {
        param.forEach(temp -> {
            Long commodityId = temp.getCommodityId();
            Integer num = temp.getNum();
            Integer orderGiftId = temp.getActivityId();
            orderGiftStockService.unlockStock(orderGiftId, commodityId, num);
        });
        return ServerResponseEntity.success();
    }
}
