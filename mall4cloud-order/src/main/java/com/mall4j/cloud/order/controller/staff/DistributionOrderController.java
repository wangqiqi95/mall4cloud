package com.mall4j.cloud.order.controller.staff;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.TCouponUser;
import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.cache.constant.OrderCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.order.dto.app.DistributionOrderSearchDTO;
import com.mall4j.cloud.order.dto.app.DistributionRankingDTO;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.DevelopingSalesStatVO;
import com.mall4j.cloud.order.vo.DistributionOrderVO;
import com.mall4j.cloud.order.vo.DistributionSalesStatVO;
import io.seata.common.util.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController("staffDistributionOrderController")
@RequestMapping("/s/distribution_order")
@Api(tags = "导购小程序-分销订单")
@Slf4j
public class DistributionOrderController {

    @Value("${mall4cloud.order.flowCachTime:600}")
    @Setter
    private Long flowCachTime=600L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private TCouponFeignClient couponFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;



    @GetMapping("/settleOrders")
    @ApiOperation(value = "全部/待结算/已结算列表", notes = "全部/待结算/已结算列表")
    public ServerResponseEntity<List<DistributionOrderVO>> page(@Valid PageDTO pageDTO, @Valid DistributionOrderSearchDTO distributionOrderSearchDTO) {
        distributionOrderSearchDTO.setDistributionUserId(AuthUserContext.get().getUserId());
        distributionOrderSearchDTO.setDistributionUserType(1);
        List<DistributionOrderVO> list = orderService.listPageWithDistributionStaff(pageDTO, distributionOrderSearchDTO);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/sales")
    @ApiOperation(value = "个人业绩统计", notes = "今日&累计业绩&门店排名&全国排名")
    public ServerResponseEntity<DistributionSalesStatVO> sales() {

        Long distributionUserId = AuthUserContext.get().getUserId();

        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + StrUtil.toString(distributionUserId);
        if(RedisUtil.hasKey(rediskey)){
            log.info("个人业绩 读取缓存--> {}",rediskey);
            ServerResponseEntity<DistributionSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setDistributionUserId(distributionUserId);
        Long totalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        ZoneId zone = ZoneId.systemDefault();
        distributionSalesStatDTO.setStartTime(Date.from(todayStart.atZone(zone).toInstant()));
        distributionSalesStatDTO.setEndTime(Date.from(todayEnd.atZone(zone).toInstant()));
        Long todaySales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setTodaySales(todaySales);
        distributionSalesStatVO.setTotalSales(totalSales);
        distributionSalesStatDTO.setStartTime(null);
        distributionSalesStatDTO.setEndTime(null);
        Integer allRanking = orderService.countDistributionRanking(distributionSalesStatDTO);
        distributionSalesStatVO.setAllRanking(allRanking);
        distributionSalesStatDTO.setDistributionStoreId(AuthUserContext.get().getStoreId());
        Integer storeRanking = orderService.countDistributionRanking(distributionSalesStatDTO);
        distributionSalesStatVO.setStoreRanking(storeRanking);
        distributionSalesStatDTO.setDistributionStoreId(null);
        ServerResponseEntity<Long> areaId = storeFeignClient.getAreaOrgByStore(AuthUserContext.get().getStoreId());
        if (areaId.isSuccess() && null != areaId.getData()){
            ServerResponseEntity<List<StoreVO>> entity = storeFeignClient.listByOrgId(areaId.getData());
            if (entity.isSuccess()){
                if(CollectionUtils.isNotEmpty(entity.getData())){
                    distributionSalesStatDTO.setStoreIds(entity.getData().stream().map(StoreVO::getStoreId).distinct().collect(Collectors.toList()));
                    distributionSalesStatVO.setOrgRanking(orderService.countDistributionRanking(distributionSalesStatDTO));
                }
            }
        }

        ServerResponseEntity<DistributionSalesStatVO> serverResponse = ServerResponseEntity.success(distributionSalesStatVO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);

        return ServerResponseEntity.success(distributionSalesStatVO);
    }

    @GetMapping("/storeSales")
    @ApiOperation(value = "门店业绩统计", notes = "今日&累计业绩&门店排名&全国排名")
    public ServerResponseEntity<DistributionSalesStatVO> storeSales() {

        Long storeId = AuthUserContext.get().getStoreId();

        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + "storeSales:"+StrUtil.toString(storeId);
        if(RedisUtil.hasKey(rediskey)){
            log.info("门店业绩统计 读取缓存--> {}",rediskey);
            ServerResponseEntity<DistributionSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setDistributionStoreId(AuthUserContext.get().getStoreId());
        Long totalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        ZoneId zone = ZoneId.systemDefault();
        distributionSalesStatDTO.setStartTime(Date.from(todayStart.atZone(zone).toInstant()));
        distributionSalesStatDTO.setEndTime(Date.from(todayEnd.atZone(zone).toInstant()));
        Long todaySales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setTodaySales(todaySales);
        distributionSalesStatVO.setTotalSales(totalSales);
        Integer storeRanking = orderService.countDistributionStoreRanking(distributionSalesStatDTO);
        distributionSalesStatVO.setStoreRanking(storeRanking);
        Integer allRanking = orderService.countDistributionStoreRanking(distributionSalesStatDTO);
        distributionSalesStatVO.setAllRanking(allRanking);

        ServerResponseEntity<DistributionSalesStatVO> serverResponse = ServerResponseEntity.success(distributionSalesStatVO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);

        return serverResponse;
    }

    @GetMapping("/staffSales")
    @ApiOperation(value = "分销中心业绩统计", notes = "总业绩&分销业绩&发展业绩")
    public ServerResponseEntity<DistributionSalesStatVO> staffSales() {

        Long userId = AuthUserContext.get().getUserId();
        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + "staffSales:"+StrUtil.toString(userId);
        if(RedisUtil.hasKey(rediskey)){
            log.info("分销中心业绩统计 读取缓存--> {}",rediskey);
            ServerResponseEntity<DistributionSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setDistributionUserId(AuthUserContext.get().getUserId());
        Long totalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatDTO.setType(1);
        Long distributionTotalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatDTO.setType(2);
        Long developingTotalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setTotalSales(totalSales);
        distributionSalesStatVO.setDistributionTotalSales(distributionTotalSales);
        distributionSalesStatVO.setDevelopingTotalSales(developingTotalSales);

        //查询本月销售
        distributionSalesStatDTO.setType(null);
        distributionSalesStatDTO.setStartTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 0));
        distributionSalesStatDTO.setEndTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 1));
        Long monthTotalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setMonthSales(monthTotalSales);

        distributionSalesStatDTO.setType(1);
        Long distributionMonthSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatDTO.setType(2);
        Long developingMonthSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setDistributionMonthSales(distributionMonthSales);
        distributionSalesStatVO.setDevelopingMonthSales(developingMonthSales);


        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        ZoneId zone = ZoneId.systemDefault();
        distributionSalesStatDTO.setStartTime(Date.from(todayStart.atZone(zone).toInstant()));
        distributionSalesStatDTO.setEndTime(Date.from(todayEnd.atZone(zone).toInstant()));
        distributionSalesStatDTO.setType(null);
        Long todaySales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatDTO.setType(1);
        Long distributionTodaySales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatDTO.setType(2);
        Long developingTodaySales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setTodaySales(todaySales);
        distributionSalesStatVO.setDistributionTodaySales(distributionTodaySales);
        distributionSalesStatVO.setDevelopingTodaySales(developingTodaySales);

        ServerResponseEntity<DistributionSalesStatVO> serverResponse = ServerResponseEntity.success(distributionSalesStatVO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);

        return serverResponse;
    }


    @ApiOperation(value = "个人业绩收入分析", notes = "业绩&成功退单金额&订单数&成功退单数&下单会员")
    @PostMapping("/salesAnalyze")
    public ServerResponseEntity<DistributionSalesStatVO> salesAnalyze(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setDistributionUserId(AuthUserContext.get().getUserId());

        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + "salesAnalyze:"+DigestUtil.md5Hex(JSON.toJSONString(distributionSalesStatDTO));
        if(RedisUtil.hasKey(rediskey)){
            log.info("个人业绩收入分析 读取缓存--> {}",rediskey);
            ServerResponseEntity<DistributionSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        distributionSalesStatVO.setTotalSales(orderService.distributionSalesStat(distributionSalesStatDTO));
        distributionSalesStatVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
        distributionSalesStatVO.setRefundAmount(orderRefundService.countDistributionRefundAmount(distributionSalesStatDTO));
        distributionSalesStatVO.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
        distributionSalesStatVO.setUserNum(orderService.countDistributionOrderUserNum(distributionSalesStatDTO));

        ServerResponseEntity<DistributionSalesStatVO> serverResponse = ServerResponseEntity.success(distributionSalesStatVO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);
        return serverResponse;
    }


    @ApiOperation(value = "分销收益收入分析", notes = "业绩&成功退单金额&订单数&成功退单数&下单会员")
    @PostMapping("/distributionAnalyze")
    public ServerResponseEntity<DistributionSalesStatVO> distributionAnalyze(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setType(1);
        distributionSalesStatDTO.setDistributionUserId(AuthUserContext.get().getUserId());

        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + "distributionAnalyze:"+DigestUtil.md5Hex(JSON.toJSONString(distributionSalesStatDTO));
        if(RedisUtil.hasKey(rediskey)){
            log.info("分销收益收入分析 读取缓存--> {} {}",rediskey,RedisUtil.get(rediskey));
            ServerResponseEntity<DistributionSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        distributionSalesStatVO.setTotalSales(orderService.distributionSalesStat(distributionSalesStatDTO));
        distributionSalesStatVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
        distributionSalesStatVO.setRefundAmount(orderRefundService.countDistributionRefundAmount(distributionSalesStatDTO));
        distributionSalesStatVO.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
        distributionSalesStatVO.setUserNum(orderService.countDistributionOrderUserNum(distributionSalesStatDTO));
        ServerResponseEntity<DistributionSalesStatVO> serverResponse = ServerResponseEntity.success(distributionSalesStatVO);

        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);
        return serverResponse;
    }


    @ApiOperation(value = "发展收益收入分析", notes = "业绩&成功退单金额&订单数&成功退单数&下单会员")
    @PostMapping("/developingAnalyze")
    public ServerResponseEntity<DevelopingSalesStatVO> developingAnalyze(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionSalesStatDTO.setDistributionUserType(1);
        Long userId = AuthUserContext.get().getUserId();
        distributionSalesStatDTO.setDistributionUserId(userId);

        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + "developingAnalyze:"+DigestUtil.md5Hex(JSON.toJSONString(distributionSalesStatDTO));
        if(RedisUtil.hasKey(rediskey)){
            log.info("发展收益收入分析 读取缓存--> {}",rediskey);
            ServerResponseEntity<DevelopingSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DevelopingSalesStatVO developingSalesStatVO = new DevelopingSalesStatVO();
        distributionSalesStatDTO.setType(2);
        developingSalesStatVO.setTotalSales(orderService.distributionSalesStat(distributionSalesStatDTO));
        developingSalesStatVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setStaffId(userId);
        userQueryDTO.setStartTime(distributionSalesStatDTO.getStartTime());
        userQueryDTO.setEndTime(distributionSalesStatDTO.getEndTime());
        ServerResponseEntity<Integer> weekerNumData = userFeignClient.countStaffWeeker(userQueryDTO);
        if (weekerNumData.isSuccess() && null != weekerNumData.getData()){
            developingSalesStatVO.setWitkeyNum(weekerNumData.getData());
        }
        distributionSalesStatDTO.setDistributionRelation(1);
        developingSalesStatVO.setShareSales(orderService.distributionSalesStat(distributionSalesStatDTO));
        developingSalesStatVO.setWitkeySales(Optional.ofNullable(developingSalesStatVO.getTotalSales()).orElse(0L) - Optional.ofNullable(developingSalesStatVO.getShareSales()).orElse(0L));

        ServerResponseEntity<DevelopingSalesStatVO> serverResponse = ServerResponseEntity.success(developingSalesStatVO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);
        return serverResponse;
    }


    @ApiOperation(value = "门店业绩收入分析", notes = "业绩&成功退单金额&订单数&成功退单数&开单导购&客单价&客单量&折扣率")
    @PostMapping("/storeSalesAnalyze")
    public ServerResponseEntity<DistributionSalesStatVO> storeSalesAnalyze(@RequestBody DistributionSalesStatDTO distributionSalesStatDTO){
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setDistributionStoreId(AuthUserContext.get().getStoreId());

        String rediskey= OrderCacheNames.ORDER_STAFF_SALES_KEY + "storeSalesAnalyze:"+DigestUtil.md5Hex(JSON.toJSONString(distributionSalesStatDTO));
        if(RedisUtil.hasKey(rediskey)){
            log.info("门店业绩收入分析 读取缓存--> {}",rediskey);
            ServerResponseEntity<DistributionSalesStatVO> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        distributionSalesStatVO.setTotalSales(orderService.distributionSalesStat(distributionSalesStatDTO));
        distributionSalesStatVO.setOrderNum(orderService.countDistributionOrderNum(distributionSalesStatDTO));
        distributionSalesStatVO.setRefundAmount(orderRefundService.countDistributionRefundAmount(distributionSalesStatDTO));
        distributionSalesStatVO.setRefundNum(orderRefundService.countDistributionRefundNum(distributionSalesStatDTO));
        distributionSalesStatVO.setUserNum(orderService.countDistributionOrderUserNum(distributionSalesStatDTO));
        if (null != distributionSalesStatVO.getTodaySales() && distributionSalesStatVO.getTodaySales() > 0){
            distributionSalesStatVO.setCustomerOrderPrice(distributionSalesStatVO.getTotalSales() / distributionSalesStatVO.getUserNum());
            distributionSalesStatVO.setCustomerOrderNum(orderService.countDistributionOrderSpuNum(distributionSalesStatDTO) / distributionSalesStatVO.getUserNum());
            distributionSalesStatVO.setDiscountRate(String.valueOf(new BigDecimal(distributionSalesStatVO.getTotalSales()).divide(new BigDecimal(orderService.countDistributionOrderTotalPrice(distributionSalesStatDTO)), 2, BigDecimal.ROUND_HALF_UP)));
        }

        ServerResponseEntity<DistributionSalesStatVO> serverResponse = ServerResponseEntity.success(distributionSalesStatVO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(serverResponse),flowCachTime);
        return serverResponse;
    }


    @ApiOperation(value = "个人业绩线上订单明细")
    @PostMapping("/pageDistributionOrders")
    public ServerResponseEntity<PageVO<EsOrderBO>> pageDistributionOrders(@Valid PageDTO pageDTO, @RequestBody DistributionQueryDTO queryDTO){
        Long distributionUserId = AuthUserContext.get().getUserId();
        String dataKey=JSON.toJSONString(pageDTO)+JSON.toJSONString(queryDTO);
        String rediskey= OrderCacheNames.ORDER_STAFF_PAGEDISTRIBUTIONORDERS_KEY + StrUtil.toString(distributionUserId)+":"+DigestUtil.md5Hex(dataKey);
        if(RedisUtil.hasKey(rediskey)){
            log.info("个人发展线上订单明细 读取缓存--> {}",rediskey);
            PageVO<EsOrderBO> pageVO = JSONObject.parseObject(RedisUtil.get(rediskey),PageVO.class);
            return ServerResponseEntity.success(pageVO);
        }
        List<Integer> distributionUserTypes = new ArrayList<>();
        distributionUserTypes.add(1);
        List<Long> distributionUserIds = new ArrayList<>();
        distributionUserIds.add(AuthUserContext.get().getUserId());
        queryDTO.setDistributionUserTypes(distributionUserTypes);
        queryDTO.setDistributionUserIds(distributionUserIds);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(2);
        statusList.add(3);
        statusList.add(5);
        queryDTO.setStatusList(statusList);

        PageVO<EsOrderBO> pageVO=orderService.pageDistributionOrders(pageDTO, queryDTO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(pageVO),flowCachTime);

        return ServerResponseEntity.success(pageVO);
    }


    @ApiOperation(value = "个人发展线上订单明细")
    @PostMapping("/pageDevelopingOrders")
    public ServerResponseEntity<PageVO<EsOrderBO>> pageDevelopingOrders(@Valid PageDTO pageDTO, @RequestBody DistributionQueryDTO queryDTO){
        Long distributionUserId = AuthUserContext.get().getUserId();
        String dataKey=JSON.toJSONString(pageDTO)+JSON.toJSONString(queryDTO);
        String rediskey= OrderCacheNames.ORDER_STAFF_PAGEDEVELOPINGORDERS_KEY + StrUtil.toString(distributionUserId)+":"+DigestUtil.md5Hex(dataKey);
        if(RedisUtil.hasKey(rediskey)){
            log.info("个人发展线上订单明细 读取缓存--> {}",rediskey);
            PageVO<EsOrderBO> pageVO = JSONObject.parseObject(RedisUtil.get(rediskey),PageVO.class);
            return ServerResponseEntity.success(pageVO);
        }
        queryDTO.setDevelopingUserId(AuthUserContext.get().getUserId());
        List<Integer> statusList = new ArrayList<>();
        statusList.add(2);
        statusList.add(3);
        statusList.add(5);
        queryDTO.setStatusList(statusList);
        PageVO<EsOrderBO> pageVO=orderService.pageDistributionOrders(pageDTO, queryDTO);

        RedisUtil.set(rediskey,JSONObject.toJSONString(pageVO),flowCachTime);

        return ServerResponseEntity.success(pageVO);
    }


    @ApiOperation(value = "门店业绩线上订单明细")
    @PostMapping("/pageStoreDistributionOrders")
    public ServerResponseEntity<PageVO<EsOrderBO>> pageStoreDistributionOrders(@Valid PageDTO pageDTO, @RequestBody DistributionQueryDTO queryDTO){
        String dataKey=JSON.toJSONString(pageDTO)+JSON.toJSONString(queryDTO);
        String rediskey= OrderCacheNames.ORDER_STAFF_PAGESTOREDISTRIBUTIONORDERS_KEY + StrUtil.toString(AuthUserContext.get().getUserId())+":"+DigestUtil.md5Hex(dataKey);
        if(RedisUtil.hasKey(rediskey)){
            log.info("门店业绩线上订单明细 读取缓存--> {}",rediskey);
            PageVO<EsOrderBO> pageVO = JSONObject.parseObject(RedisUtil.get(rediskey),PageVO.class);
            return ServerResponseEntity.success(pageVO);
        }
        queryDTO.setDistributionStoreId(AuthUserContext.get().getStoreId());
        queryDTO.setDevelopingStoreId(AuthUserContext.get().getStoreId());
        PageVO<EsOrderBO> pageVO=orderService.pageDistributionOrders(pageDTO, queryDTO);

        RedisUtil.set(rediskey,JSONObject.toJSONString(pageVO),flowCachTime);

        return ServerResponseEntity.success(pageVO);
    }


    @ApiOperation("分销榜-我的排名")
    @GetMapping("/distributionSalesRand")
    public ServerResponseEntity<DistributionSalesStatVO> distributionSalesRand(){
        String rediskey= OrderCacheNames.ORDER_STAFF_DISTRIBUTIONSALESRAND_KEY + StrUtil.toString(AuthUserContext.get().getUserId());
        if(RedisUtil.hasKey(rediskey)){
            log.info("分销榜-我的排名 读取缓存--> {}",rediskey);
            DistributionSalesStatVO salesStatVO = JSONObject.parseObject(RedisUtil.get(rediskey),DistributionSalesStatVO.class);
            return ServerResponseEntity.success(salesStatVO);
        }

        DistributionSalesStatVO vo = new DistributionSalesStatVO();
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setDistributionUserType(1);
        distributionSalesStatDTO.setDistributionUserId(AuthUserContext.get().getUserId());
        distributionSalesStatDTO.setStartTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 0));
        distributionSalesStatDTO.setEndTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 1));
        Long monthSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        vo.setMonthSales(monthSales);
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        ServerResponseEntity<List<StaffVO>> staffListData = staffFeignClient.listStaffByStatus(staffQueryDTO);
        if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
            distributionSalesStatDTO.setStaffIds(staffListData.getData().stream().map(StaffVO::getId).distinct().collect(Collectors.toList()));
        }
        Integer allRanking = orderService.countDistributionRanking(distributionSalesStatDTO);
        vo.setAllRanking(allRanking);
        distributionSalesStatDTO.setDistributionStoreId(AuthUserContext.get().getStoreId());
        if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
            distributionSalesStatDTO.setStaffIds(staffListData.getData().stream().filter(staffVO -> staffVO.getStoreId().equals(AuthUserContext.get().getStoreId())).map(StaffVO::getId).distinct().collect(Collectors.toList()));
        }
        Integer storeRanking = orderService.countDistributionRanking(distributionSalesStatDTO);
        vo.setStoreRanking(storeRanking);
        distributionSalesStatDTO.setDistributionStoreId(null);
        ServerResponseEntity<Long> areaId = storeFeignClient.getAreaOrgByStore(AuthUserContext.get().getStoreId());
        if (areaId.isSuccess() && null != areaId.getData()){
            ServerResponseEntity<List<StoreVO>> entity = storeFeignClient.listByOrgId(areaId.getData());
            if (entity.isSuccess()){
                if(CollectionUtils.isNotEmpty(entity.getData())){
                    distributionSalesStatDTO.setStoreIds(entity.getData().stream().map(StoreVO::getStoreId).distinct().collect(Collectors.toList()));
                    if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
                        distributionSalesStatDTO.setStaffIds(staffListData.getData().stream().filter(staffVO -> distributionSalesStatDTO.getStoreIds().contains(staffVO.getStoreId())).
                                map(StaffVO::getId).distinct().collect(Collectors.toList()));
                    }
                    vo.setOrgRanking(orderService.countDistributionRanking(distributionSalesStatDTO));
                }
            }
        }

        RedisUtil.set(rediskey,JSONObject.toJSONString(vo),flowCachTime);

        return ServerResponseEntity.success(vo);
    }


    @ApiOperation("分销榜-业绩排行榜")
    @GetMapping("/pageDistributionRanking")
    public ServerResponseEntity<PageVO<DistributionRankingDTO>> pageDistributionRanking(@Valid PageDTO pageDTO, DistributionSalesStatDTO distributionSalesStatDTO){
        String dataKey=JSON.toJSONString(pageDTO)+JSON.toJSONString(distributionSalesStatDTO);
        String rediskey= OrderCacheNames.ORDER_STAFF_PAGEDISTRIBUTIONRANKING_KEY + StrUtil.toString( AuthUserContext.get().getUserId())+":"+DigestUtil.md5Hex(dataKey);
        if(RedisUtil.hasKey(rediskey)){
            log.info("分销榜-业绩排行榜 读取缓存--> {}",rediskey);
            PageVO<DistributionRankingDTO> pageVO = JSONObject.parseObject(RedisUtil.get(rediskey),PageVO.class);
            return ServerResponseEntity.success(pageVO);
        }
        distributionSalesStatDTO.setStoreType(Optional.ofNullable(distributionSalesStatDTO.getStoreType()).orElse(1));
        distributionSalesStatDTO.setStartTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 0));
        distributionSalesStatDTO.setEndTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 1));
        List<Long> storeIds = new ArrayList<>();
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        ServerResponseEntity<List<StaffVO>> staffListData = staffFeignClient.listStaffByStatus(staffQueryDTO);
        if (staffListData.isSuccess() && CollectionUtils.isNotEmpty(staffListData.getData())){
            distributionSalesStatDTO.setStaffIds(staffListData.getData().stream().map(StaffVO::getId).distinct().collect(Collectors.toList()));
        }
        if (distributionSalesStatDTO.getStoreType() == 1){
            storeIds.add(AuthUserContext.get().getStoreId());
            distributionSalesStatDTO.setStaffIds(staffListData.getData().stream().filter(staffVO -> staffVO.getStoreId().equals(AuthUserContext.get().getStoreId())).map(StaffVO::getId).distinct().collect(Collectors.toList()));
        } else if (distributionSalesStatDTO.getStoreType() == 2){
            ServerResponseEntity<Long> areaId = storeFeignClient.getAreaOrgByStore(AuthUserContext.get().getStoreId());
            if (areaId.isSuccess() && null != areaId.getData()){
                ServerResponseEntity<List<StoreVO>> entity = storeFeignClient.listByOrgId(areaId.getData());
                if (entity.isSuccess()){
                    if (CollectionUtils.isEmpty(entity.getData())){
                        return ServerResponseEntity.success(new PageVO<>());
                    }
                    storeIds.addAll(entity.getData().stream().map(StoreVO::getStoreId).distinct().collect(Collectors.toList()));
                    distributionSalesStatDTO.setStaffIds(staffListData.getData().stream().filter(staffVO -> storeIds.contains(staffVO.getStoreId())).map(StaffVO::getId).distinct().collect(Collectors.toList()));
                }
            }
        }
        distributionSalesStatDTO.setStoreIds(storeIds);
        PageVO<DistributionRankingDTO> pageVO = orderService.pageDistributionRanking(pageDTO, distributionSalesStatDTO);
        RedisUtil.set(rediskey,JSONObject.toJSONString(pageVO),flowCachTime);

        return ServerResponseEntity.success(pageVO);
    }


    @ApiOperation("导购个人中心佣金信息")
    @GetMapping("/getStaffCommission")
    public ServerResponseEntity<DistributionSalesStatVO> getStaffCommission(){
        String rediskey= OrderCacheNames.ORDER_STAFF_GETSTAFFCOMMISSION_KEY + StrUtil.toString(AuthUserContext.get().getUserId());
        if(RedisUtil.hasKey(rediskey)){
            log.info("导购个人中心佣金信息 读取缓存--> {}",rediskey);
            DistributionSalesStatVO salesStatVO = JSONObject.parseObject(RedisUtil.get(rediskey),DistributionSalesStatVO.class);
            return ServerResponseEntity.success(salesStatVO);
        }
        DistributionSalesStatVO vo = new DistributionSalesStatVO();
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setDistributionUserId(AuthUserContext.get().getUserId());
        Long totalCommission = orderService.countDistributionCommission(distributionSalesStatDTO);
        vo.setTotalCommission(totalCommission);
        distributionSalesStatDTO.setStartTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 0));
        distributionSalesStatDTO.setEndTime(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 1));
        Long monthCommission = orderService.countDistributionCommission(distributionSalesStatDTO);
        vo.setMonthCommission(monthCommission);
        Long groupCommission = 0L;
        ServerResponseEntity<List<TCouponUser>> entityData = couponFeignClient.selectOrderNo(distributionSalesStatDTO.getStartTime(), distributionSalesStatDTO.getEndTime());
        if (entityData.isSuccess() && CollectionUtils.isNotEmpty(entityData.getData())){
            log.info("导购个人中心佣金信息团购订单 order:{}", entityData.getData());
            distributionSalesStatDTO.setOrderIds(entityData.getData().stream().map(TCouponUser::getOrderNo).distinct().collect(Collectors.toList()));
            groupCommission = orderService.countDistributionCommission(distributionSalesStatDTO);
        }
        vo.setGroupCommission(groupCommission);
        vo.setCommonCommission(Optional.ofNullable(monthCommission).orElse(0L) - groupCommission);

        RedisUtil.set(rediskey,JSONObject.toJSONString(vo),flowCachTime);

        return ServerResponseEntity.success(vo);
    }

}
