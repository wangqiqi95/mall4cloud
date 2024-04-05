package com.mall4j.cloud.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopDetailVO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.api.order.vo.OrderProdEffectRespVO;
import com.mall4j.cloud.api.order.vo.OrderRefundProdEffectRespVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.ProdEffectDTO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.vo.ProdEffectRespVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.flow.constant.FlowVisitEnum;
import com.mall4j.cloud.flow.dto.FlowLogDTO;
import com.mall4j.cloud.flow.mapper.ProductAnalyseMapper;
import com.mall4j.cloud.flow.mapper.ProductAnalyseUserMapper;
import com.mall4j.cloud.flow.model.ProductAnalyse;
import com.mall4j.cloud.flow.model.ProductAnalyseUser;
import com.mall4j.cloud.flow.service.FlowService;
import com.mall4j.cloud.flow.service.ProductAnalyseService;
import com.mall4j.cloud.flow.vo.FlowProdEffectRespVO;
import com.mall4j.cloud.flow.vo.ShopFlowInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 流量分析—商品分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Service
@Slf4j
public class ProductAnalyseServiceImpl implements ProductAnalyseService {

    @Autowired
    private ProductAnalyseMapper productAnalyseMapper;
    @Autowired
    private ProductAnalyseUserMapper productAnalyseUserMapper;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private OrderRefundFeignClient orderRefundFeignClient;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private FlowService flowService;
    @Autowired
    private StoreFeignClient storeFeignClient;

    private final Long VISIT_NUM = 1L;

    @Override
    public PageVO<ProductAnalyse> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> productAnalyseMapper.list());
    }


    @Override
    public ProductAnalyse getByProductAnalyseId(Long productAnalyseId) {
        return productAnalyseMapper.getByProductAnalyseId(productAnalyseId);
    }

    @Override
    public void save(ProductAnalyse productAnalyse) {
        productAnalyseMapper.save(productAnalyse);
    }

    @Override
    public void update(ProductAnalyse productAnalyse) {
        productAnalyseMapper.update(productAnalyse);
    }

    @Override
    public void deleteById(Long productAnalyseId) {
        productAnalyseMapper.deleteById(productAnalyseId);
    }

    @Override
    public void statisticalProduct(List<FlowLogDTO> flowLogList) {
        // 根据系统类型统计各系统之间的数据
        Map<Date, List<FlowLogDTO>> dateMap = flowLogList.stream().collect(Collectors.groupingBy(FlowLogDTO::getCreateTime));
        for (Date date : dateMap.keySet()) {
            statisticalSpu(date, dateMap.get(date));
        }
    }

    private void statisticalSpu(Date date, List<FlowLogDTO> flowLogDTOList) {
        log.info("入参数据：{},{}", JSONObject.toJSONString(date),JSONObject.toJSONString(flowLogDTOList));
        Map<Integer, List<FlowLogDTO>> sysTypeMap = flowLogDTOList.stream().collect(Collectors.groupingBy(FlowLogDTO::getSystemType));
        Set<Long> spuIdSet = flowLogDTOList.stream().map(flowLogDTO -> Long.valueOf(flowLogDTO.getBizData())).collect(Collectors.toSet());

        /**
         * 未登录状态 用户标识为uuid
         * 登录状态   用户标识为userid
         * 查询数据库中已经存在的用户商品标识，传入用户，只查询当前用户的
         */
        Set<String> uuidSet = flowLogDTOList.stream().filter(flowLogDTO -> Objects.nonNull(flowLogDTO.getUuid())).map(flowLogDTO -> flowLogDTO.getUuid()).collect(Collectors.toSet());
        Set<String> userIdSet = flowLogDTOList.stream().filter(flowLogDTO -> Objects.nonNull(flowLogDTO.getUserId())).map(FlowLogDTO::getUserId).collect(Collectors.toSet());
        userIdSet.addAll(uuidSet);

        List<ProductAnalyse> productAnalyses = productAnalyseMapper.listByDate(date, spuIdSet,userIdSet);
        log.info("productAnalyses数据：{}",JSONObject.toJSONString(productAnalyses));
        Map<Integer, List<ProductAnalyse>> sysTypeDbMap = new HashMap<>();
        if (productAnalyses != null || productAnalyses.size() > 0){
            sysTypeDbMap = productAnalyses.stream().collect(Collectors.groupingBy(ProductAnalyse::getSystemType));
        }
        // 获取还未保存的商品信息（未存到数据库）
//        Map<Long, SpuSearchVO> spuSearchMap = getSpuMap(spuIdSet);
//        log.info("spuSearchMap数据：{}",JSONObject.toJSONString(spuSearchMap));
        // 根据系统统计对应的商品数据
        for (Integer sysType : sysTypeMap.keySet()) {
            // 数据库中已有的商品信息
            Map<Long, ProductAnalyse> spuDbMap;
            if (sysTypeDbMap.containsKey(sysType)) {
                spuDbMap = sysTypeDbMap.get(sysType).stream().collect(Collectors.toMap(ProductAnalyse::getSpuId, p -> p,(k1, k2)->k1 ));
            } else {
                spuDbMap = new HashMap<>(0);
            }
            // 获取当前系统的用户操作数据
            List<FlowLogDTO> productList = sysTypeMap.get(sysType).stream().collect(Collectors.toList());
            log.info("productList数据：{}",JSONObject.toJSONString(productList));
            // 根据商品id进行分组
            Map<String, List<FlowLogDTO>> spuMap = productList.stream().collect(Collectors.groupingBy(FlowLogDTO::getBizData));
            log.info("spuMap数据：{}",JSONObject.toJSONString(spuMap));
            // 根据商品统计各个商品的信息
            log.info("spuDbMap数据：{}",JSONObject.toJSONString(spuDbMap));
            for (String key : spuMap.keySet()) {
                Long spuId = Long.valueOf(key);
                // 获取该商品存储在数据库中的统计数据
                log.info("spuId数据：{}",spuId);
                ProductAnalyse productAnalyse = spuDbMap.get(spuId);
                log.info("productAnalyse数据：{}",JSONObject.toJSONString(productAnalyse));
                // 若数据库还没该商品的统计数据，则初始化一个
                if (Objects.isNull(productAnalyse)) {
//                    // 若没有该商品信息，则不进行统计
//                    if (Objects.isNull(spuMap.get(spuId))) {
//                        log.info("没有该商品信息，则不进行统计");
//                        continue;
//                    }
                    productAnalyse = productAnalyseInit(date);
                    log.info("初始化productAnalyse数据：{}",JSONObject.toJSONString(productAnalyse));
                    productAnalyse.setSystemType(sysType);
                    spuDbMap.put(spuId, productAnalyse);
                    productAnalyses.add(productAnalyse);
                }
                // 根据用户id进行分组
                Map<String, ProductAnalyseUser> userMap = new HashMap<String, ProductAnalyseUser>();
                if (productAnalyse != null && productAnalyse.getProductAnalyseUsers() != null && productAnalyse.getProductAnalyseUsers().size() > 0) {
                    userMap = productAnalyse.getProductAnalyseUsers().stream().collect(Collectors.toMap(ProductAnalyseUser::getUserId, p -> p,(p1,p2) -> p2));
                }
                // 获取用户在该商品中的所有操作信息
                List<FlowLogDTO> spuFlowLogs = spuMap.get(key);
                // 处理用户在商品中操作的信息
                for (FlowLogDTO flowLogDTO: spuFlowLogs) {
                    handleUserOperation(productAnalyse, flowLogDTO, userMap);
                }
            }
        }
        // 保存、更新统计的信息
        saveOrUpdate(productAnalyses);
    }

    /**
     * 保存、更新统计的信息
     * @param productAnalyses
     */
    private void saveOrUpdate(List<ProductAnalyse> productAnalyses) {
        List<ProductAnalyse> productSaveList = new ArrayList<>();
        List<ProductAnalyse> productUpdateList = new ArrayList<>();
        // 商品数据处理
        for (ProductAnalyse productAnalysis : productAnalyses) {
            if (Objects.isNull(productAnalysis.getProductAnalyseId())) {
                productSaveList.add(productAnalysis);
                continue;
            }
            productUpdateList.add(productAnalysis);
        }
        List<ProductAnalyseUser> userSaveList = new ArrayList<>();
        List<ProductAnalyseUser> userUpdateList = new ArrayList<>();
        if (CollUtil.isNotEmpty(productSaveList)) {
            productAnalyseMapper.saveBatch(productSaveList);
        }
        if (CollUtil.isNotEmpty(productUpdateList)) {
            productAnalyseMapper.updateBatch(productUpdateList);
        }

        // 用户的商品操作数据处理
        for (ProductAnalyse productAnalyse : productAnalyses) {
            for (ProductAnalyseUser productAnalyseUser : productAnalyse.getProductAnalyseUsers()) {
                if (Objects.isNull(productAnalyseUser.getProductAnalyseUserId())) {
                    productAnalyseUser.setCreateDate(productAnalyse.getCreateDate());
                    productAnalyseUser.setProductAnalyseId(productAnalyse.getProductAnalyseId());
                    userSaveList.add(productAnalyseUser);
                    continue;
                }
                userUpdateList.add(productAnalyseUser);
            }
        }
        if (CollUtil.isNotEmpty(userSaveList)) {
            productAnalyseUserMapper.saveBatch(userSaveList);
        }
        if (CollUtil.isNotEmpty(userUpdateList)) {
            productAnalyseUserMapper.updateBatch(userUpdateList);
        }
    }

    /**
     * 处理用户操作信息
     * @param productAnalyse
     * @param flowLogDTO
     * @param userMap
     */
    private void handleUserOperation(ProductAnalyse productAnalyse, FlowLogDTO flowLogDTO, Map<String, ProductAnalyseUser> userMap) {
        ProductAnalyseUser productAnalyseUser;
        if (productAnalyse != null) {
            productAnalyse.setSpuId(Long.parseLong(flowLogDTO.getBizData()));
            productAnalyse.setShopId(flowLogDTO.getShopId());
        }
        if (userMap.containsKey(flowLogDTO.getUuid())) {
            productAnalyseUser = userMap.get(flowLogDTO.getUuid());
            // 用户有userId(登陆了)
            if (StrUtil.isNotBlank(flowLogDTO.getUserId())) {
                userMap.remove(flowLogDTO.getUuid());
                mergerUserOperation(flowLogDTO.getUserId(), userMap, productAnalyseUser);
            }
        }  else if (userMap.containsKey(flowLogDTO.getUserId())) {
            productAnalyseUser = userMap.get(flowLogDTO.getUserId());
        } else {
            productAnalyseUser = productAnalyseUserInit(flowLogDTO);
            if (productAnalyse != null && productAnalyse.getSpuId() != null) {
                productAnalyseUser.setSpuId(productAnalyse.getSpuId());
                productAnalyse.getProductAnalyseUsers().add(productAnalyseUser);
            }
            userMap.put(productAnalyseUser.getUserId(), productAnalyseUser);
        }

        if (productAnalyse != null) {
            // 商品操作信息统计
            if (Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.VISIT.value()) || Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.SHARE.value())) {
                productAnalyse.setVisis(productAnalyse.getVisis() + VISIT_NUM);
                if (!Objects.equals(productAnalyseUser.getIsVisit(), 1)) {
                    productAnalyseUser.setIsVisit(1);
                }
            } else if (Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.CLICK.value())) {
                productAnalyse.setClick(productAnalyse.getClick() + flowLogDTO.getNums());
                if (!Objects.equals(productAnalyseUser.getIsClick(), 1)) {
                    productAnalyseUser.setIsClick(1);
                }
            } else if (Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.SHOP_CAT.value())) {
                productAnalyse.setPlusShopCart(productAnalyse.getPlusShopCart() + flowLogDTO.getNums());
                if (!Objects.equals(productAnalyseUser.getIsPlusShopCart(), 1)) {
                    productAnalyseUser.setIsPlusShopCart(1);
                }
            }
        }
    }

    /**
     * 合并用户操作数据
     * @param userId
     * @param userMap
     * @param productAnalyseUser
     */
    private void mergerUserOperation(String userId, Map<String, ProductAnalyseUser> userMap, ProductAnalyseUser productAnalyseUser) {
        // 若未有该用户信息，则直接插入
        if (!userMap.containsKey(userId)) {
            productAnalyseUser.setUserId(userId);
            userMap.put(userId, productAnalyseUser);
            return;
        }
        // 若已有该用户信息，则合并信息
        ProductAnalyseUser productAnalyseUserNew = userMap.get(userId);
        if (Objects.equals(productAnalyseUser.getIsClick(), 1)) {
            productAnalyseUserNew.setIsClick(1);
        }
        if (Objects.equals(productAnalyseUser.getIsVisit(), 1)) {
            productAnalyseUserNew.setIsVisit(1);
        }
        if (Objects.equals(productAnalyseUser.getIsPlusShopCart(), 1)) {
            productAnalyseUserNew.setIsPlusShopCart(1);
        }
    }


    /**
     * 初始化商品用户操作类
     * @param flowLogDTO
     * @return
     */
    private ProductAnalyseUser productAnalyseUserInit(FlowLogDTO flowLogDTO) {
        ProductAnalyseUser productAnalyseUser = new ProductAnalyseUser();
        if (StrUtil.isNotBlank(flowLogDTO.getUserId())) {
            productAnalyseUser.setUserId(flowLogDTO.getUserId());
        } else {
            productAnalyseUser.setUserId(flowLogDTO.getUuid());
        }
        productAnalyseUser.setIsVisit(0);
        productAnalyseUser.setIsClick(0);
        productAnalyseUser.setIsPlusShopCart(0);
        return productAnalyseUser;
    }

    /**
     * 获取还未保存的商品信息（未存到数据库）
     * @param spuIds 所有商品的id
     * @return
     */
    private Map<Long, SpuSearchVO> getSpuMap(Set<Long> spuIds) {
        Map<Long, SpuSearchVO> spuMap = null;
        if (spuIds != null && spuIds.size() > 0) {
            ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.listSpuBySpuIds(new ArrayList<>(spuIds));
            if (spuResponse != null && spuResponse.getData() != null && spuResponse.getData().size() > 0) {
                spuMap = spuResponse.getData().stream().collect(Collectors.toMap(SpuSearchVO::getSpuId, s -> s));
            }
        }
        return spuMap;
    }

    private ProductAnalyse productAnalyseInit(Date date) {
        Long defaultValue = 0L;
        ProductAnalyse productAnalyse = new ProductAnalyse();
//        productAnalyse.setSpuId(spuVO.getSpuId());
//        productAnalyse.setShopId(spuVO.getShopId());
        productAnalyse.setCreateDate(date);
        productAnalyse.setVisis(defaultValue);
        productAnalyse.setClick(defaultValue);
        productAnalyse.setShareVisit(defaultValue);
        productAnalyse.setPlusShopCart(0);
        productAnalyse.setProductAnalyseUsers(new ArrayList<>());
        return productAnalyse;
    }

    @Override
    public PageVO<ProdEffectRespVO> getProductEffect(PageDTO pageDTO,ProdEffectDTO prodEffectDTO) {
        flowService.statisticalProduct();
        Long startTime = prodEffectDTO.getStartTime().getTime();
        Long endTime = prodEffectDTO.getEndTime().getTime();
        PageVO<ProdEffectRespVO> flowSpuPage = getFlowSpuId(pageDTO,startTime, endTime, prodEffectDTO.getShopId());
        List<Long> spuIds = flowSpuPage.getList().stream().map(ProdEffectRespVO::getSpuId).collect(Collectors.toList());
        if (CollUtil.isEmpty(spuIds)){
            return flowSpuPage;
        }
        //根据参数获取商品信息
        List<SpuVO> listByParam = spuFeignClient.listSpuBySpuIds(spuIds).getData();
        //根据参数获取商品相关数据
        List<FlowProdEffectRespVO> flowProdList = productAnalyseMapper.getProdEffectByParam(spuIds, new Date(startTime), new Date(endTime));
        //根据参数获取商品订单数据分析
        List<OrderProdEffectRespVO> prodOrderList = orderFeignClient.getProdEffectByDateAndProdIds(spuIds, startTime, endTime).getData();
        //根据参数获取商品退款订单数据分析
        List<OrderRefundProdEffectRespVO> prodOrderRefundList = orderRefundFeignClient.getProdRefundEffectByDateAndProdIds(spuIds, startTime, endTime).getData();
        Map<Long, SpuVO> spuMap = listByParam.stream().collect(Collectors.toMap(SpuVO::getSpuId, s -> s));
        Map<Long, FlowProdEffectRespVO> flowProdMap = flowProdList.stream().collect(Collectors.toMap(FlowProdEffectRespVO::getSpuId, p -> p));
        Map<Long, OrderProdEffectRespVO> prodOrderMap = prodOrderList.stream().collect(Collectors.toMap(OrderProdEffectRespVO::getSpuId, p -> p));
        Map<Long, OrderRefundProdEffectRespVO> prodOrderRefundMap = prodOrderRefundList.stream().collect(Collectors.toMap(OrderRefundProdEffectRespVO::getSpuId, p -> p));
        Iterator<ProdEffectRespVO> iterator = flowSpuPage.getList().iterator();
        while (iterator.hasNext()) {
            ProdEffectRespVO param = iterator.next();
            if (!spuMap.containsKey(param.getSpuId())) {
                iterator.remove();
                continue;
            }
            loadProdEffectRespData(spuMap, flowProdMap, prodOrderMap, prodOrderRefundMap, param);
        }
        return flowSpuPage;
    }

    private void loadProdEffectRespData(Map<Long, SpuVO> spuMap, Map<Long, FlowProdEffectRespVO> flowProdMap, Map<Long, OrderProdEffectRespVO> prodOrderMap, Map<Long, OrderRefundProdEffectRespVO> prodOrderRefundMap, ProdEffectRespVO param) {
        //商品属性
        SpuVO spuVO = spuMap.get(param.getSpuId());
        if (Objects.nonNull(spuVO)){
            //商品名称
            param.setSpuName(spuVO.getName());
            //商品图片
            param.setSpuUrl(spuVO.getMainImgUrl());
            //商品价格
            param.setPrice(spuVO.getPriceFee());
        }
        //商品相关数据
        FlowProdEffectRespVO flowProdEffectRespVO = flowProdMap.get(param.getSpuId());
        if (Objects.nonNull(flowProdEffectRespVO)){
            //曝光人数
            param.setExposePersonNum(flowProdEffectRespVO.getExposePersonNum());
            //加购人数
            param.setAddCartPerson(flowProdEffectRespVO.getAddCartPerson());
            //曝光次数
            param.setExpose(flowProdEffectRespVO.getExpose());
            //加购件数
            param.setAddCart(flowProdEffectRespVO.getAddCart());
        }
        //订单相关数据
        OrderProdEffectRespVO orderProdEffectRespVO = prodOrderMap.get(param.getSpuId());
        if (Objects.nonNull(orderProdEffectRespVO)){
            //下单人数
            param.setPlaceOrderPerson(orderProdEffectRespVO.getPlaceOrderPerson());
            //支付人数
            param.setPayPerson(orderProdEffectRespVO.getPayPerson());
            //下单件数
            param.setPlaceOrderNum(orderProdEffectRespVO.getPlaceOrderNum());
            //支付件数
            param.setPayNum(orderProdEffectRespVO.getPayNum());
            //下单金额
            param.setPlaceOrderAmount(orderProdEffectRespVO.getPlaceOrderAmount());
            //支付金额
            param.setPayAmount(orderProdEffectRespVO.getPayAmount());
        }
        //退款订单相关数据
        OrderRefundProdEffectRespVO orderRefundProdEffectRespVO = prodOrderRefundMap.get(param.getSpuId());
        if (Objects.nonNull(orderRefundProdEffectRespVO)) {
            //申请退款订单数
            param.setRefundNum(orderRefundProdEffectRespVO.getRefundNum());
            //申请退款人数
            param.setRefundPerson(orderRefundProdEffectRespVO.getRefundPerson());
            //成功退款订单数
            param.setRefundSuccessNum(orderRefundProdEffectRespVO.getRefundSuccessNum());
            //成功退款人数
            param.setRefundSuccessPerson(orderRefundProdEffectRespVO.getRefundSuccessPerson());
            //成功退款金额
            param.setRefundSuccessAmount(orderRefundProdEffectRespVO.getRefundSuccessAmount());
            //退款率 = 成功退款订单数/支付成功数
            if (Objects.nonNull(orderProdEffectRespVO)){
                param.setRefundSuccessRate(divAverage(param.getRefundSuccessNum(),orderProdEffectRespVO.getPayOrderNum(),4));
            } else {
                param.setRefundSuccessRate((double) 0);
            }

        }
    }

    @Override
    public List<ShopFlowInfoVO> listShopRankIngByFlow(DateTime endTime, Integer dayCount, Integer limit) {
        Date startTime = DateUtils.getBeforeDay(endTime, -dayCount);
        List<ShopFlowInfoVO> shopFlowInfoVOList = productAnalyseMapper.listShopRankIngByFlow(startTime, endTime, limit);
        if (CollUtil.isEmpty(shopFlowInfoVOList)) {
            return shopFlowInfoVOList;
        }
        List<Long> shopIds = shopFlowInfoVOList.stream().map(ShopFlowInfoVO::getShopId).collect(Collectors.toList());
//        ServerResponseEntity<List<ShopDetailVO>> listServerResponseEntity = shopDetailFeignClient.listByShopIds(shopIds);
        ServerResponseEntity<List<StoreVO>> listServerResponseEntity = storeFeignClient.listByStoreIdList(shopIds);
        if(listServerResponseEntity==null || listServerResponseEntity.getData()==null || listServerResponseEntity.getData().size()==0){
            Assert.faild("获取门店列表失败。");
        }
        // 赋值店铺名称
        Map<Long, StoreVO> shopDetailMap = listServerResponseEntity.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));
        Iterator<ShopFlowInfoVO> iterator = shopFlowInfoVOList.iterator();
        while (iterator.hasNext()) {
            ShopFlowInfoVO shopFlowInfoVO = iterator.next();
            if (Objects.isNull(shopDetailMap.get(shopFlowInfoVO.getShopId()))) {
                iterator.remove();
                continue;
            }
            shopFlowInfoVO.setShopName(shopDetailMap.get(shopFlowInfoVO.getShopId()).getName());
        }
        return shopFlowInfoVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpuDataBySpuId(Long spuId) {
        if (Objects.isNull(spuId)) {
            return;
        }
        productAnalyseUserMapper.deleteSpuDataBySpuId(spuId);
        productAnalyseMapper.deleteSpuDataBySpuId(spuId);
    }

    private Double divAverage(Integer a, Integer b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
    }

    private PageVO<ProdEffectRespVO> getFlowSpuId(PageDTO pageDTO,Long startTime,Long endTime,Long shopId){
        return PageUtil.doPage(pageDTO, () -> productAnalyseMapper.getProdEffectRespByTime(new Date(startTime), new Date(endTime), shopId));
    }
}
