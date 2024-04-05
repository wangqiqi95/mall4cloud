package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.order.constant.BuyerReasonType;
import com.mall4j.cloud.order.mapper.OrderItemMapper;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.service.OrderStatisticsService;
import com.mall4j.cloud.order.vo.OrderCountVO;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lth
 */
@Service
public class OrderStatisticsServiceImpl implements OrderStatisticsService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;

    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;

    public static final Logger log = LoggerFactory.getLogger(OrderStatisticsServiceImpl.class);

    @Override
    public OrderCountVO getOrderCountOfStatusByShopId(Long shopId) {
        return orderMapper.getOrderCountOfStatusByShopId(shopId);
    }

    @Override
    public List<OrderRefundStatisticsVO> listOrderRefundInfoInDateRange(Long shopId, Date endTime, Integer dayCount) {
        Date startTime = this.getStartTime(endTime, dayCount);
        // 根据时间范围查询订单概况与退款数据
        List<OrderRefundStatisticsVO> orderRefundStatisticsVOList = orderRefundMapper.listOrderRefundInfoByShopIdAndDateRange(shopId, startTime, endTime, dayCount);
        List<OrderOverviewVO> orderOverviewVOList = orderMapper.listOrderOverviewInfoByShopIdAndDateRange(shopId, startTime, endTime, dayCount);
        Map<?, OrderRefundStatisticsVO> orderRefundStatisticsMap = orderRefundStatisticsVOList.stream().collect(Collectors.toMap(OrderRefundStatisticsVO::getRefundDateToString, o -> o));
        Map<?, OrderOverviewVO> orderOverviewMap = orderOverviewVOList.stream().collect(Collectors.toMap(OrderOverviewVO::getTimeData, o -> o));
        // 返回结果
        List<OrderRefundStatisticsVO> refundStatisticsVOList = new ArrayList<>();
        // 处理数据
        for (int i = 0; i < dayCount; i++) {
            OrderRefundStatisticsVO resItem = new OrderRefundStatisticsVO();
            // 日期key
            String dateKey = DateUtils.dateToStrYmd(startTime);
            OrderOverviewVO orderOverviewVO = orderOverviewMap.get(dateKey);
            OrderRefundStatisticsVO refundStatisticsVO = orderRefundStatisticsMap.get(dateKey);

            if (Objects.nonNull(orderOverviewVO)) {
                resItem.setTotalOrderCount(orderOverviewVO.getPayOrderCount());
                if (Objects.nonNull(refundStatisticsVO)) {
                    resItem.setRefundRate(this.getRatioRate(refundStatisticsVO.getRefundCount(), orderOverviewVO.getPayOrderCount()));
                    resItem.setPayActualTotal(refundStatisticsVO.getPayActualTotal());
                    resItem.setRefundCount(refundStatisticsVO.getRefundCount());
                } else {
                    resItem.setRefundRate(0.0);
                    resItem.setPayActualTotal(0L);
                    resItem.setRefundCount(0);
                }
            } else {
                resItem.setTotalOrderCount(0);
                if (Objects.nonNull(refundStatisticsVO)) {
                    resItem.setRefundRate(100.00);
                    resItem.setPayActualTotal(refundStatisticsVO.getPayActualTotal());
                    resItem.setRefundCount(refundStatisticsVO.getRefundCount());
                } else {
                    resItem.setRefundRate(0.0);
                    resItem.setPayActualTotal(0L);
                    resItem.setRefundCount(0);
                }
            }
            resItem.setRefundDateToString(dateKey);
            refundStatisticsVOList.add(resItem);
            startTime = getNextTime(startTime);
        }
        return refundStatisticsVOList;
    }

    @Override
    public List<OrderRefundStatisticsVO> listRefundRankingByProd(Long shopId, Date endTime, Integer dayCount) {
        Date startTime = this.getStartTime(endTime, dayCount);
        List<OrderRefundStatisticsVO> orderRefundStatisticsList = orderRefundMapper.listRefundRankingByProd(shopId, startTime, endTime);
        if (CollUtil.isEmpty(orderRefundStatisticsList)) {
            return orderRefundStatisticsList;
        }
        List<Long> spuIds = orderRefundStatisticsList.stream().map(OrderRefundStatisticsVO::getSpuId).collect(Collectors.toList());
        ServerResponseEntity<List<SpuVO>> spuRes = spuFeignClient.listSpuNameBySpuIds(spuIds);
        if (spuRes.isFail()) {
            throw new LuckException(spuRes.getMsg());
        }
        // 赋值商品名称
        Map<Long, SpuVO> spuMap = spuRes.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, p -> p));
        Iterator<OrderRefundStatisticsVO> iterator = orderRefundStatisticsList.iterator();
        while (iterator.hasNext()) {
            OrderRefundStatisticsVO orderRefundStatisticsVO = iterator.next();
            if (Objects.isNull(spuMap.get(orderRefundStatisticsVO.getSpuId()))) {
                iterator.remove();
                continue;
            }
            orderRefundStatisticsVO.setRefundProdName(spuMap.get(orderRefundStatisticsVO.getSpuId()).getName());
        }
        return orderRefundStatisticsList;
    }

    @Override
    public OrderOverviewVO getToDayInfoByHour(Long shopId, Date startTime, Date endTime) {
        OrderOverviewVO orderOverviewVO = this.getOrderStatisticsByShopIdAndDateRange(shopId, startTime, endTime);
        List<Double> actualList = this.getActualByHour(shopId, startTime, endTime);
        orderOverviewVO.setPayActualList(actualList);
        return orderOverviewVO;
    }

    @Override
    public OrderOverviewVO getCurrentMonthByDay(Long shopId, Date startTime, Date endTime) {
        OrderOverviewVO orderOverviewVO = this.getOrderStatisticsByShopIdAndDateRange(shopId, startTime, endTime);
        this.getOrderInfoByDayCount(shopId, endTime, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), orderOverviewVO);
        return orderOverviewVO;
    }

    @Override
    public List<OrderRefundStatisticsVO> listRefundRankingByReason(Long shopId, Date endTime, int dayCount) {
        Date startTime = this.getStartTime(endTime, dayCount);
        List<OrderRefundStatisticsVO> ranking = orderRefundMapper.listRefundRankingByReason(shopId, startTime, endTime);
        Integer totalRefundCount = 0;
        for (OrderRefundStatisticsVO orderRefundStatisticsVO : ranking) {
            totalRefundCount += orderRefundStatisticsVO.getRefundCount();
        }
        for (OrderRefundStatisticsVO orderRefundStatisticsVO : ranking) {
            try {
                BuyerReasonType reasonType = BuyerReasonType.instance(Integer.valueOf(orderRefundStatisticsVO.getBuyerReason()));
                if (Objects.nonNull(reasonType)) {
                    orderRefundStatisticsVO.setBuyerReason(reasonType.getCn());
                }
            } catch (NumberFormatException numberFormatException) {
                log.error(numberFormatException.getMessage(),numberFormatException);
            }
            orderRefundStatisticsVO.setRefundRate(Arith.div(orderRefundStatisticsVO.getRefundCount() * 100, totalRefundCount, 2));
        }
        return ranking;
    }

    @DS("slave")
    @Override
    public OrderOverviewVO getDetailInfoByHour(Long shopId) {
        Date now = new Date();
        // 获取今天的订单概况
        OrderOverviewVO toDayInfoByHour = this.getOrderStatisticsByShopIdAndDateRange(shopId, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        // 获取昨天的订单概况
        OrderOverviewVO yesToDayInfoByHour = this.getOrderStatisticsByShopIdAndDateRange(shopId, DateUtil.beginOfDay(DateUtils.getBeforeDay(now, -1)), DateUtil.endOfDay(DateUtils.getBeforeDay(now, -1)));
        // 获取今天的支付金额列表
        List<Double> toDayActualList = this.getActualByHour(shopId, DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        // 获取昨天的支付金额列表
        List<Double> yesToDayActualList = this.getActualByHour(shopId, DateUtil.beginOfDay(DateUtils.getBeforeDay(now, -1)), DateUtil.endOfDay(DateUtils.getBeforeDay(now, -1)));
        toDayInfoByHour.setPayActualList(toDayActualList);
        toDayInfoByHour.setYesterdayPayActualList(yesToDayActualList);
        // 计算今天与昨天数值变化的比率
        toDayInfoByHour.setYesterdayPayUserRate(this.getChangeRate(toDayInfoByHour.getPayUserCount(), yesToDayInfoByHour.getPayUserCount()));
        toDayInfoByHour.setYesterdayPayOrderRate(this.getChangeRate(toDayInfoByHour.getPayOrderCount(), yesToDayInfoByHour.getPayOrderCount()));
        toDayInfoByHour.setYesterdayRefundRate(this.getChangeRate(toDayInfoByHour.getRefund(), yesToDayInfoByHour.getRefund()));
        toDayInfoByHour.setYesterdayOnePriceRate(this.getChangeRate(toDayInfoByHour.getOnePrice(), yesToDayInfoByHour.getOnePrice()));
        toDayInfoByHour.setYesterdayPayActualRate(this.getChangeRate(toDayInfoByHour.getPayActual(), yesToDayInfoByHour.getPayActual()));
        return toDayInfoByHour;
    }

    @DS("slave")
    @Override
    public List<OrderOverviewVO> listSpuRankingByOrderCount(Long shopId, Date endTime, int dayCount, int limit) {
        Date startTime = this.getStartTime(endTime, dayCount);
        List<OrderOverviewVO> orderOverviews = orderItemMapper.listSpuRankingByOrderCount(shopId, startTime, endTime, limit);
        if (CollUtil.isEmpty(orderOverviews)) {
            return orderOverviews;
        }
        List<Long> spuIds = orderOverviews.stream().map(OrderOverviewVO::getSpuId).collect(Collectors.toList());
        ServerResponseEntity<List<SpuVO>> spusRes = spuFeignClient.listSpuNameBySpuIds(spuIds);
        if (spusRes.isFail()) {
            throw new LuckException(spusRes.getMsg());
        }
        if (CollUtil.isEmpty(spusRes.getData())) {
            return new ArrayList<>();
        }
        // 赋值商品名称
        Map<Long, SpuVO> spuMap = spusRes.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, p -> p));
        Iterator<OrderOverviewVO> iterator = orderOverviews.iterator();
        while (iterator.hasNext()) {
            OrderOverviewVO orderOverviewVO = iterator.next();
            if (Objects.isNull(spuMap.get(orderOverviewVO.getSpuId()))) {
                iterator.remove();
                continue;
            }
            orderOverviewVO.setSpuName(spuMap.get(orderOverviewVO.getSpuId()).getName());
        }
        return orderOverviews;
    }

    @Override
    @DS("slave")
    public List<OrderOverviewVO> listShopRankingByPayActual(Date endTime, int dayCount, int limit) {
        Date startTime = this.getStartTime(endTime, dayCount);

        List<OrderOverviewVO> orderOverviewVOS =  orderItemMapper.listShopRankingByPayActual(startTime, endTime, limit);


        /**
         * 查询店铺名称
         */
        List<Long> shopIds = orderOverviewVOS.stream().map(OrderOverviewVO::getShopId).collect(Collectors.toList());

        ServerResponseEntity<List<StoreVO>> storeResonse = storeFeignClient.listByStoreIdList(shopIds);
        if(storeResonse==null || storeResonse.getData()==null || storeResonse.getData().size()==0){
            Assert.faild("获取门店列表失败。");
        }

        Map<Long, StoreVO> storeMap = storeResonse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, list->list));

        for (OrderOverviewVO orderOverviewVO : orderOverviewVOS) {
            StoreVO storeVO = storeMap.get(orderOverviewVO.getShopId());
            orderOverviewVO.setShopName(storeVO==null?"":storeVO.getName());
        }

        return orderOverviewVOS;
    }

    @Override
    @DS("slave")
    public List<OrderRefundStatisticsVO> listShopRankingByRefundCount(Date endTime, Integer dayCount, Integer limit) {
        Date startTime = this.getStartTime(endTime, dayCount);
        List<OrderRefundStatisticsVO> orderRefundStatisticsVOList = orderRefundMapper.listShopRankingByRefundCount(startTime, endTime, limit);
        if (CollUtil.isEmpty(orderRefundStatisticsVOList)) {
            // 数据为空，直接返回
            return orderRefundStatisticsVOList;
        }
        List<Long> shopIds = orderRefundStatisticsVOList.stream().map(OrderRefundStatisticsVO::getShopId).collect(Collectors.toList());

        ServerResponseEntity<List<StoreVO>> storeResonse = storeFeignClient.listByStoreIdList(shopIds);
        if(storeResonse==null || storeResonse.getData()==null || storeResonse.getData().size()==0){
            Assert.faild("获取门店列表失败。");
        }

        Map<Long, StoreVO> storeMap = storeResonse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, list->list));

        for (OrderRefundStatisticsVO orderRefundStatisticsVO : orderRefundStatisticsVOList) {
            StoreVO storeVO =storeMap.get(orderRefundStatisticsVO.getShopId());
            String shopName = storeVO==null?"":storeVO.getName();
            orderRefundStatisticsVO.setShopName(shopName);
        }

        return orderRefundStatisticsVOList;
    }

    @DS("slave")
    @Override
    public OrderOverviewVO getOrderInfoByDayCountAndShopId(Long shopId, Integer dayCount) {
        Date endTime = DateUtil.endOfDay(DateUtil.date());
        Date startTime = this.getStartTime(endTime, dayCount);
        OrderOverviewVO orderOverviewVO = this.getOrderStatisticsByShopIdAndDateRange(shopId, startTime, endTime);
        this.getOrderInfoByDayCount(shopId, endTime, dayCount, orderOverviewVO);
        return orderOverviewVO;
    }

    /**
     * 根据店铺Id与时间范围获取订单概况信息
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    private OrderOverviewVO getOrderStatisticsByShopIdAndDateRange(Long shopId, Date startTime, Date endTime) {
        OrderOverviewVO orderOverviewVO = orderMapper.getOrderOverviewInfoByShopId(shopId, startTime, endTime);
        OrderRefundStatisticsVO orderRefundStatisticsVO = orderRefundMapper.getOrderRefundInfoByShopId(shopId, startTime, endTime);
        double onePrice = (orderOverviewVO.getPayUserCount() == 0) ?
                0 : (Arith.div(orderOverviewVO.getPayActual(), orderOverviewVO.getPayUserCount(), 2));
        orderOverviewVO.setOnePrice(onePrice);
        orderOverviewVO.setRefund(orderRefundStatisticsVO.getPayActualTotal());
        orderOverviewVO.setChargebackCount(orderRefundStatisticsVO.getRefundCount());
        return orderOverviewVO;
    }

    /**
     * 根据店铺Id与时间范围按小时分段获取数据支付金额列表
     * @param shopId
     * @param startTime
     * @param endTime
     */
    private List<Double> getActualByHour(Long shopId, Date startTime, Date endTime) {
        List<OrderOverviewVO> orderOverviewVOList = orderMapper.listActualByHour(shopId, startTime, endTime);
        Map<Integer, Double> payMap = new HashMap<>(30);
        for (OrderOverviewVO temp : orderOverviewVOList) {
            payMap.put(Integer.parseInt(temp.getTimeData()), temp.getPayActual());
        }
        List<Double> nowActual = new ArrayList<>();
        double sum = 0.00;
        for (int i = 0; i < Constant.MAX_HOUR_NUM_BY_DAY; i++) {
            if (payMap.get(i) != null) {
                sum = Arith.add(sum, Arith.div(payMap.get(i), 100, 2));
            }
            nowActual.add(Arith.round(sum, 2));
        }
        return nowActual;
    }

    /**
     * 根据店铺Id与时间范围按天分段获取支付金额列表
     * @param shopId
     * @param dayCount
     * @param endTime
     * @param orderOverviewVO
     */
    private void getOrderInfoByDayCount(Long shopId, Date endTime, Integer dayCount, OrderOverviewVO orderOverviewVO) {
        Date startTime = this.getStartTime(endTime, dayCount);
        // 获取支付金额列表
        List<OrderOverviewVO> orderOverviewVOList = orderMapper.listOrderOverviewInfoByShopIdAndDateRange(shopId, startTime, endTime, dayCount);
        Map<?, OrderOverviewVO> orderOverviewMap = orderOverviewVOList.stream().collect(Collectors.toMap(OrderOverviewVO::getTimeData, o -> o));

        List<Double> actualList = new ArrayList<>();
        List<String> dateToStringList = new ArrayList<>();

        for (int i = 0; i < dayCount; i++) {
            if (orderOverviewMap.containsKey(DateUtils.dateToStrYmd(startTime))) {
                OrderOverviewVO overviewVO = orderOverviewMap.get(DateUtils.dateToStrYmd(startTime));
                actualList.add(Arith.div(overviewVO.getPayActual(), 100, 2));
            } else {
                actualList.add(0.0);
            }
            dateToStringList.add(DateUtils.dateToStrYmd(startTime));
            startTime = getNextTime(startTime);
        }

        orderOverviewVO.setDateToStringList(dateToStringList);
        orderOverviewVO.setPayActualList(actualList);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        // 获取本月的第几天
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (dayCount <= monthDay) {
            orderOverviewVO.setCurrentMonthPayActual(orderOverviewVO.getPayActual());
        } else {
            double currentMonthPayActual = 0L;
            // 累加本月支付金额
            for (int i = actualList.size() - 1, j = 0; j < monthDay && i >= 0; j++, i--) {
                currentMonthPayActual = Arith.add(currentMonthPayActual, actualList.get(i));
            }
            orderOverviewVO.setCurrentMonthPayActual(currentMonthPayActual);
        }
    }

    /**
     * 根据结束时间与天数获取起始时间
     * @param endTime
     * @param dayCount
     * @return
     */
    private Date getStartTime(Date endTime, Integer dayCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.add(Calendar.DATE, -dayCount + 1);
        return DateUtil.beginOfDay(calendar.getTime());
    }

    private Double getChangeRate(double nowValue, double oldValue) {
        if (Objects.equals(oldValue, 0.0)) {
            return Objects.equals(nowValue, 0.0) ? 0.0 : 1.0;
        }
        double minus = Arith.sub(nowValue, oldValue);
        return Arith.div(minus, oldValue, 4);
    }

    /**
     * 获取下一次循环的开始时间，以及设置后台显示的时间字符串
     * @param startTime 开始时间
     * @return
     */
    private Date getNextTime(Date startTime){
        startTime = DateUtil.offsetDay(startTime, 1);
        return startTime;
    }

    /**
     * 计算两个数的百分比
     * @param dividend 被除数
     * @param divisor 除数
     * @return
     */
    private Double getRatioRate(Integer dividend, Integer divisor) {
        if (Objects.isNull(dividend) || dividend == 0.0) {
            return 0.0;
        }
        if (Objects.isNull(divisor) || divisor == 0.0) {
            return 100.0;
        }
        return Arith.mul(Arith.div(dividend, divisor, 4), 100.00);
    }
}
