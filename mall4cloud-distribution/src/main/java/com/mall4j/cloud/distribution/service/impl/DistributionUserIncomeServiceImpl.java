package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.multishop.bo.ShopSimpleBO;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.OrderSimpleAmountInfoBO;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.constant.DistributionUserIncomeStateEnum;
import com.mall4j.cloud.distribution.constant.DistributionUserIncomeTypeEnum;
import com.mall4j.cloud.distribution.dto.DistributionUserIncomeDTO;
import com.mall4j.cloud.distribution.mapper.DistributionUserIncomeMapper;
import com.mall4j.cloud.distribution.model.DistributionUserIncome;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.service.DistributionUserIncomeService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletBillService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.vo.DistributionOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeVO;
import com.mall4j.cloud.distribution.vo.StatisticsDisUserIncomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分销收入记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
@Service
public class DistributionUserIncomeServiceImpl implements DistributionUserIncomeService {

    @Autowired
    private DistributionUserIncomeMapper distributionUserIncomeMapper;
    @Autowired
    private DistributionUserWalletService distributionUserWalletService;
    @Autowired
    private DistributionUserWalletBillService distributionUserWalletBillService;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private SpuFeignClient spuFeignClient;

    @Override
    public PageVO<DistributionUserIncome> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionUserIncomeMapper.list());
    }

    @Override
    public PageVO<DistributionUserIncomeVO> getDistributionUserIncomePage(PageDTO pageDTO, Long distributionUserId) {
        return PageUtil.doPage(pageDTO, () -> distributionUserIncomeMapper.getDistributionUserIncomeList(distributionUserId));
    }

    @Override
    public DistributionUserIncome getByIncomeId(Long incomeId) {
        return distributionUserIncomeMapper.getByIncomeId(incomeId);
    }

    @Override
    public void save(DistributionUserIncome distributionUserIncome) {
        distributionUserIncomeMapper.save(distributionUserIncome);
    }

    @Override
    public void update(DistributionUserIncome distributionUserIncome) {
        distributionUserIncomeMapper.update(distributionUserIncome);
    }

    @Override
    public void deleteById(Long incomeId) {
        distributionUserIncomeMapper.deleteById(incomeId);
    }

    @Override
    public PageVO<DistributionUserIncomeVO> effectPage(PageDTO pageDTO, DistributionUserIncomeDTO distributionUserIncomeDTO, String userMobile, Long shopId) {
        PageVO<DistributionUserIncomeVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionUserIncomeMapper.effectPage(distributionUserIncomeDTO, userMobile, shopId));
        List<DistributionUserIncomeVO> list = pageVO.getList();
        if (CollUtil.isEmpty(list)) {
            return pageVO;
        }
        // 查询order的状态
        List<Long> orderIds = list.stream().map(DistributionUserIncomeVO::getOrderId).collect(Collectors.toList());
        ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(orderIds);
        if (!ordersStatusResponse.isSuccess()) {
            throw new LuckException(ordersStatusResponse.getMsg());
        }
        List<OrderStatusBO> orderStatus = ordersStatusResponse.getData();
        Map<Long, OrderStatusBO> orderStatusMap = orderStatus.stream().collect(Collectors.toMap(OrderStatusBO::getOrderId, (k) -> k));
        for (DistributionUserIncomeVO incomeVO : list) {
            Long orderId = incomeVO.getOrderId();
            OrderStatusBO orderStatusBO = orderStatusMap.get(orderId);
            if (Objects.isNull(orderStatusBO)) {
                continue;
            }
            incomeVO.setOrderStatus(orderStatusBO.getStatus());
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public PageVO<DistributionUserIncomeVO> pageSalesRecord(PageDTO pageDTO, DistributionUserIncomeDTO distributionUserIncomeDTO) {
        PageVO<DistributionUserIncomeVO> res = new PageVO<>();
        List<ShopSimpleBO> shopSimpleList = null;
        List<SpuSimpleBO> spuSimpleList = null;
        ShopSimpleBO shopSimpleBO = new ShopSimpleBO();
        // shopId正序
        shopSimpleBO.setSeq(1);
        SpuSimpleBO spuSimpleBO = new SpuSimpleBO();
        // spuId正序
        spuSimpleBO.setSeq(1);
        spuSimpleBO.setLang(I18nMessage.getLang());
        Long shopId = AuthUserContext.get().getTenantId();
        distributionUserIncomeDTO.setShopId(shopId);
        if (Objects.equals(distributionUserIncomeDTO.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            // 平台端搜索，条件才有店铺名称
            if (StrUtil.isNotBlank(distributionUserIncomeDTO.getShopName())) {
                shopSimpleBO.setShopName(distributionUserIncomeDTO.getShopName());
                ServerResponseEntity<List<ShopSimpleBO>> shopRes = shopDetailFeignClient.listSimple(shopSimpleBO);
                shopSimpleList = shopRes.getData();
                if (CollUtil.isEmpty(shopSimpleList)) {
                    return res;
                }
            }
        }
        if (StrUtil.isNotBlank(distributionUserIncomeDTO.getSpuName())) {
            // 搜索条件存在商品名称
            spuSimpleBO.setSpuName(distributionUserIncomeDTO.getSpuName());
            if (!CollUtil.isEmpty(shopSimpleList)) {
                // 如果搜索条件存在店铺名称，则把shopIds也设置为查询参数
                spuSimpleBO.setShopIds(shopSimpleList.stream().map(ShopSimpleBO::getShopId).collect(Collectors.toList()));
            } else {
                if (!Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
                    // 如果是商家端查询，设置店铺id
                    spuSimpleBO.setShopId(shopId);
                }
            }
            ServerResponseEntity<List<SpuSimpleBO>> spuRes = spuFeignClient.listSimple(spuSimpleBO);
            spuSimpleList = spuRes.getData();
            if (CollUtil.isEmpty(spuSimpleList)) {
                return res;
            }
        }
        if (!CollUtil.isEmpty(shopSimpleList)) {
            distributionUserIncomeDTO.setShopIds(shopSimpleList.stream().map(ShopSimpleBO::getShopId).collect(Collectors.toList()));
        }
        if (!CollUtil.isEmpty(spuSimpleList)) {
            distributionUserIncomeDTO.setSpuIds(spuSimpleList.stream().map(SpuSimpleBO::getSpuId).collect(Collectors.toList()));
        }
        // 查询结果
        res = PageUtil.doPage(pageDTO, () -> distributionUserIncomeMapper.listSalesRecord(distributionUserIncomeDTO));
        List<DistributionUserIncomeVO> list = res.getList();
        if (CollUtil.isEmpty(list)) {
            return res;
        }
        if (CollUtil.isEmpty(shopSimpleList)) {
            // 店铺列表信息为空，则去查询获取店铺信息，不为空则使用已获取的
            shopSimpleBO.setShopIds(list.stream().map(DistributionUserIncomeVO::getShopId).collect(Collectors.toList()));
            ServerResponseEntity<List<ShopSimpleBO>> shopRes = shopDetailFeignClient.listSimple(shopSimpleBO);
            shopSimpleList = shopRes.getData();
        }
        if (CollUtil.isEmpty(spuSimpleList)) {
            // 商品列表信息为空，则去查询获取商品信息，不为空则使用已获取的
            spuSimpleBO.setSpuIds(list.stream().map(DistributionUserIncomeVO::getSpuId).collect(Collectors.toList()));
            ServerResponseEntity<List<SpuSimpleBO>> spuRes = spuFeignClient.listSimple(spuSimpleBO);
            spuSimpleList = spuRes.getData();
        }
        // 赋值店铺名称与商品名称
        for (DistributionUserIncomeVO distributionUserIncomeVO : list) {
            SpuSimpleBO spuInfo = this.getSpuInfo(distributionUserIncomeVO.getSpuId(), spuSimpleList);
            if (Objects.nonNull(spuInfo)) {
                distributionUserIncomeVO.setSpuName(spuInfo.getSpuName());
                distributionUserIncomeVO.setMainImgUrl(spuInfo.getMainImgUrl());
            }
            ShopSimpleBO shopInfo = this.getShopInfo(distributionUserIncomeVO.getShopId(), shopSimpleList);
            if (Objects.nonNull(shopInfo)) {
                distributionUserIncomeVO.setShopName(shopInfo.getShopName());
            }
        }
        res.setList(list);
        return res;
    }

    @Override
    public PageVO<DistributionOrderVO> getDistributionOrderByDistributionUserId(PageDTO pageDTO, Long distributionUserId) {
        PageVO<DistributionOrderVO> pageVO = PageUtil.doPage(pageDTO, () -> distributionUserIncomeMapper.getDistributionOrderByDistributionUserId(distributionUserId));
        List<DistributionOrderVO> list = pageVO.getList();
        if (CollUtil.isEmpty(list)) {
            return pageVO;
        }
        List<Long> orderItemIds = list.stream().map(DistributionOrderVO::getOrderItemId).collect(Collectors.toList());
        ServerResponseEntity<List<OrderItemVO>> orderItemsResponse = orderFeignClient.getOrderItems(orderItemIds);
        if (!orderItemsResponse.isSuccess()) {
            throw new LuckException(orderItemsResponse.getMsg());
        }
        List<OrderItemVO> orderItems = orderItemsResponse.getData();
        if (CollUtil.isEmpty(orderItems)) {
            return pageVO;
        }
        Map<Long, OrderItemVO> orderVoMap = orderItems.stream().collect(Collectors.toMap(OrderItemVO::getOrderItemId, (k) -> k));
        for (DistributionOrderVO distributionOrderVO : list) {
            Long orderItemId = distributionOrderVO.getOrderItemId();
            OrderItemVO orderItemVO = orderVoMap.get(orderItemId);
            if (Objects.isNull(orderItemVO)) {
                continue;
            }
            distributionOrderVO.setPic(orderItemVO.getPic());
            distributionOrderVO.setSpuId(orderItemVO.getSpuId());
            distributionOrderVO.setSpuName(orderItemVO.getSpuName());
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public StatisticsDisUserIncomeVO statisticsDistributionUserIncome(Long distributionUserId) {
        StatisticsDisUserIncomeVO statisticsDisUserIncomeVO = new StatisticsDisUserIncomeVO();
        Date now = new Date();
        Double todayAmount = distributionUserIncomeMapper.statisticsDisUserIncome(distributionUserId, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        Double monthAmount = distributionUserIncomeMapper.statisticsDisUserIncome(distributionUserId, DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now));
        statisticsDisUserIncomeVO.setTodayAmount(todayAmount);
        statisticsDisUserIncomeVO.setMonthAmount(monthAmount);
        return statisticsDisUserIncomeVO;
    }

    @Override
    public PageVO<DistributionUserIncomeOrderVO> getMyPromotionOrderByState(PageDTO pageDTO, Long distributionUserId, Integer state) {
        PageVO<DistributionUserIncomeOrderVO> promotionOrderPage = PageUtil.doPage(pageDTO, () -> distributionUserIncomeMapper.getMyPromotionOrderByState(distributionUserId, state));
        List<DistributionUserIncomeOrderVO> promotionOrderList = promotionOrderPage.getList();
        if (CollUtil.isEmpty(promotionOrderList)) {
            return promotionOrderPage;
        }
        List<Long> orderItemIds = promotionOrderList.stream().map(DistributionUserIncomeOrderVO::getOrderItemId).collect(Collectors.toList());
        List<Long> orderIds = promotionOrderList.stream().map(DistributionUserIncomeOrderVO::getOrderId).collect(Collectors.toList());
        ServerResponseEntity<List<OrderItemVO>> orderItemsResponse = orderFeignClient.getOrderItems(orderItemIds);
        ServerResponseEntity<List<OrderSimpleAmountInfoBO>> orderResponse = orderFeignClient.getOrdersSimpleAmountInfo(orderIds);
        if (!orderItemsResponse.isSuccess()) {
            throw new LuckException(orderItemsResponse.getMsg());
        }
        if (!orderResponse.isSuccess()) {
            throw new LuckException(orderResponse.getMsg());
        }
        List<OrderItemVO> orderItems = orderItemsResponse.getData();
        List<OrderSimpleAmountInfoBO> orderList = orderResponse.getData();
        if (CollUtil.isEmpty(orderItems) && CollUtil.isEmpty(orderList)) {
            return promotionOrderPage;
        }
        Map<Long, OrderItemVO> orderItemVoMap = orderItems.stream().collect(Collectors.toMap(OrderItemVO::getOrderItemId, (k) -> k));
        Map<Long, Long> orderVoMap = orderList.stream().collect(Collectors.toMap(OrderSimpleAmountInfoBO::getOrderId, OrderSimpleAmountInfoBO::getActualTotal));
        for (DistributionUserIncomeOrderVO incomeOrderVO : promotionOrderList) {
            Long orderItemId = incomeOrderVO.getOrderItemId();
            OrderItemVO orderItemVO = orderItemVoMap.get(orderItemId);
            if (Objects.isNull(orderItemVO)) {
                continue;
            }
            incomeOrderVO.setPic(orderItemVO.getPic());
            incomeOrderVO.setPrice(orderItemVO.getPrice());
            incomeOrderVO.setSpuCount(orderItemVO.getCount());
            incomeOrderVO.setSpuName(orderItemVO.getSpuName());
            incomeOrderVO.setSkuName(orderItemVO.getSkuName());
            Long orderId = incomeOrderVO.getOrderId();
            Long actualTotal = orderVoMap.get(orderId);
            if (Objects.isNull(actualTotal)) {
                continue;
            }
            incomeOrderVO.setActualTotal(actualTotal);
        }
        return promotionOrderPage;
    }

    @Override
    public int countByOrderIdAndOrderItemId(Long orderId, Long orderItemId, Integer state) {
        return distributionUserIncomeMapper.countByOrderIdAndOrderItemId(orderId, orderItemId, state);
    }

    @Override
    public List<DistributionUserIncome> getByOrderId(Long orderId) {
        return distributionUserIncomeMapper.getByOrderId(orderId);
    }

    @Override
    public int updateStateByIncomeIds(List<Long> incomeIds, Integer state) {
        return distributionUserIncomeMapper.updateStateByIncomeIds(incomeIds, state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commissionSettlementHandle(Date date) {
        // 查询需要处理的支付收入记录,15天前确认收货的订单
        // 本质上，我们是需要从订单中筛选出15天前已完结的订单，并且订单是分销相关的订单
        // 订单数据量很大，尽管可以从es中查询，我们也会查出，一些无用数据（比如：这些订单分销已经佣金结算处理过了，但是还是会查询出来, 并且这个数据会随着分销订单的增量越来越大）
        // 所以我们从分销流水记录的角度来处理。

        // 查询出未结算的分销流水
        List<DistributionUserIncome> distributionUserIncomeList = distributionUserIncomeMapper.listWaitCommissionSettlement();
        if (CollUtil.isEmpty(distributionUserIncomeList)) {
            return;
        }
        List<Long> orderIds = distributionUserIncomeList.stream().map(DistributionUserIncome::getOrderId).collect(Collectors.toList());
        // 查询出已结算的订单 is_settled = 1
        Integer settled = 1;
        ServerResponseEntity<List<EsOrderBO>> orderResponse = orderFeignClient.getListBySettledOrOrderIds(settled, orderIds);
        if (!orderResponse.isSuccess()) {
            throw new LuckException(orderResponse.getMsg());
        }
        List<EsOrderBO> data = orderResponse.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        List<Long> orderIdList = data.stream().map(EsOrderBO::getOrderId).collect(Collectors.toList());
        distributionUserIncomeList = distributionUserIncomeList.stream().filter(item -> orderIdList.contains(item.getOrderId())).collect(Collectors.toList());

        // 修改分销记录
        List<Long> updateIncomeIds = new ArrayList<>();
        // 分销员钱包流水
        List<DistributionUserWalletBill> saveBatchDistributionWalletBillList = new ArrayList<>();

        for (DistributionUserIncome distributionUserIncome : distributionUserIncomeList) {
            // 减少用户的待结算佣金，添加已结算金额
            DistributionUserWallet distributionUserWallet = distributionUserWalletService.getByWalletId(distributionUserIncome.getWalletId());
            if (distributionUserWallet == null) {
                // 未找到分销员信息
                throw new LuckException("未找到分销员信息");
            }

            // 添加分销钱包日志
            Long incomeAmount = distributionUserIncome.getIncomeAmount();
            distributionUserWallet.setWalletId(distributionUserWallet.getWalletId());
            distributionUserWallet.setSettledAmount(distributionUserWallet.getSettledAmount() + incomeAmount);
            // 待结算需要减去分销金额
            distributionUserWallet.setUnsettledAmount(distributionUserWallet.getUnsettledAmount() - incomeAmount);
            distributionUserWallet.setAccumulateAmount(distributionUserWallet.getAccumulateAmount() + incomeAmount);
            DistributionUserWallet updateWallet = new DistributionUserWallet();
            updateWallet.setWalletId(distributionUserWallet.getWalletId());
            updateWallet.setSettledAmount(incomeAmount);
            // 待结算需要减去分销金额
            updateWallet.setUnsettledAmount(-incomeAmount);
            updateWallet.setAccumulateAmount(incomeAmount);
            distributionUserWalletService.updateWalletAmount(updateWallet);

            // 更新收入状态
            updateIncomeIds.add(distributionUserIncome.getIncomeId());

            // 添加钱包变动日志
            if (Objects.equals(DistributionUserIncomeTypeEnum.AWARD_ONE.value(), distributionUserIncome.getIncomeType())) {
                saveBatchDistributionWalletBillList.add(new DistributionUserWalletBill(distributionUserWallet, "直推奖励", "Direct push reward", -incomeAmount, incomeAmount, Constant.DEFAULT_AMOUNT, incomeAmount, 0));
            } else if (Objects.equals(DistributionUserIncomeTypeEnum.AWARD_TWO.value(), distributionUserIncome.getIncomeType())) {
                saveBatchDistributionWalletBillList.add(new DistributionUserWalletBill(distributionUserWallet, "间推奖励", "Indirect reward", -incomeAmount, incomeAmount, Constant.DEFAULT_AMOUNT, incomeAmount, 0));
            } else {
                saveBatchDistributionWalletBillList.add(new DistributionUserWalletBill(distributionUserWallet, "邀请奖励", "Invitation reward", -incomeAmount, incomeAmount, Constant.DEFAULT_AMOUNT, incomeAmount, 0));
            }
        }
        // 批量更新分销收入状态
        if (CollUtil.isNotEmpty(updateIncomeIds)) {
            if (distributionUserIncomeMapper.updateStateByIncomeIds(updateIncomeIds, DistributionUserIncomeStateEnum.COMMISSION.value()) <= 0) {
                // 批量更新分销收入状态失败
                throw new LuckException("批量更新分销收入状态失败");
            }
        }

        // 批量添加钱包变动日志
        if (CollUtil.isNotEmpty(saveBatchDistributionWalletBillList)) {
            distributionUserWalletBillService.saveBatch(saveBatchDistributionWalletBillList);
        }
    }

    /**
     * 获取店铺信息
     *
     * @param shopId         店铺id
     * @param shopSimpleList shopId正序店铺列表
     * @return
     */
    private ShopSimpleBO getShopInfo(Long shopId, List<ShopSimpleBO> shopSimpleList) {
        if (CollUtil.isEmpty(shopSimpleList) || Objects.isNull(shopId)) {
            return null;
        }
        int leftIndex = 0;
        int rightIndex = shopSimpleList.size() - 1;
        int midIndex;
        while (leftIndex <= rightIndex) {
            midIndex = leftIndex + ((rightIndex - leftIndex) >>> 1);
            ShopSimpleBO shopSimpleBO = shopSimpleList.get(midIndex);
            if (Objects.equals(shopSimpleBO.getShopId(), shopId)) {
                return shopSimpleBO;
            }
            if (shopSimpleBO.getShopId() > shopId) {
                rightIndex = midIndex - 1;
            } else {
                leftIndex = midIndex + 1;
            }
        }
        return null;
    }

    /**
     * 获取商品信息
     *
     * @param spuId         商品id
     * @param spuSimpleList spuId正序商品列表
     * @return
     */
    private SpuSimpleBO getSpuInfo(Long spuId, List<SpuSimpleBO> spuSimpleList) {
        if (CollUtil.isEmpty(spuSimpleList) || Objects.isNull(spuId)) {
            return null;
        }
        int leftIndex = 0;
        int rightIndex = spuSimpleList.size() - 1;
        int midIndex;
        while (leftIndex <= rightIndex) {
            midIndex = leftIndex + ((rightIndex - leftIndex) >>> 1);
            SpuSimpleBO spuSimpleBO = spuSimpleList.get(midIndex);
            if (Objects.equals(spuSimpleBO.getSpuId(), spuId)) {
                return spuSimpleBO;
            }
            if (spuSimpleBO.getSpuId() > spuId) {
                rightIndex = midIndex - 1;
            } else {
                leftIndex = midIndex + 1;
            }
        }
        return null;
    }
}
