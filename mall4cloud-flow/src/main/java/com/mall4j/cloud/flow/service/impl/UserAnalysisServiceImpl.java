package com.mall4j.cloud.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.FlowOrderVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.flow.constant.FlowLogPageEnum;
import com.mall4j.cloud.flow.constant.FlowUserTypeEnum;
import com.mall4j.cloud.flow.constant.FlowVisitEnum;
import com.mall4j.cloud.flow.dto.FlowLogDTO;
import com.mall4j.cloud.flow.mapper.UserVisitProdAnalysisMapper;
import com.mall4j.cloud.flow.model.UserAnalysis;
import com.mall4j.cloud.flow.mapper.UserAnalysisMapper;
import com.mall4j.cloud.flow.model.UserVisitProdAnalysis;
import com.mall4j.cloud.flow.service.UserAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 流量分析—用户数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Slf4j
@Service
public class UserAnalysisServiceImpl implements UserAnalysisService {

    @Autowired
    private UserAnalysisMapper userAnalysisMapper;
    @Autowired
    private UserVisitProdAnalysisMapper userVisitProdAnalysisMapper;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private AreaFeignClient areaFeignClient;
    @Autowired
    private DbSearcher dbSearcher;

    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");


    @Override
    public PageVO<UserAnalysis> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userAnalysisMapper.list());
    }

    @Override
    public UserAnalysis getByUserAnalysisId(Long userAnalysisId) {
        return userAnalysisMapper.getByUserAnalysisId(userAnalysisId);
    }

    @Override
    public void save(UserAnalysis userAnalysis) {
        userAnalysisMapper.save(userAnalysis);
    }

    @Override
    public void update(UserAnalysis userAnalysis) {
        userAnalysisMapper.update(userAnalysis);
    }

    @Override
    public void deleteById(Long userAnalysisId) {
        userAnalysisMapper.deleteById(userAnalysisId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statisticalUser(List<FlowLogDTO> flowLogList) {
        log.info(" UserAnalysisServiceImpl.statisticalUser---> {}",flowLogList.size());
        // 根据系统类型统计各系统之间的数据
        Map<Date, List<FlowLogDTO>> dateMap = flowLogList.stream().collect(Collectors.groupingBy(FlowLogDTO::getCreateTime));
        for (Date date : dateMap.keySet()) {
            List<FlowLogDTO> flowLogDTOList = dateMap.get(date);
            List<Long> deleteIds = new ArrayList<>();
            // 根据系统统计对应的用户操作数据
            List<UserAnalysis> userAnalysesList = statisticalUserData(date, flowLogDTOList, deleteIds);
            // 保存或更新用户统计信息
            saveOrUpdateUserAnalysis(userAnalysesList, deleteIds);
        }
    }

    /**
     * 保存或更新用户统计信息
     *
     * @param userAnalysesList 用户统计信息列表
     * @param deleteIds 需要删除的用户统计信息列表
     */
    private void saveOrUpdateUserAnalysis(List<UserAnalysis> userAnalysesList, List<Long> deleteIds) {
        List<UserAnalysis> saveList = new ArrayList<>();
        List<UserAnalysis> updateList = new ArrayList<>();
        for (UserAnalysis userAnalysis : userAnalysesList) {
            if (Objects.isNull(userAnalysis.getUserAnalysisId())) {
                saveList.add(userAnalysis);
            }
            updateList.add(userAnalysis);
        }
        List<UserVisitProdAnalysis> saveUserVisitProdAnalyses = new ArrayList<>();
        List<UserVisitProdAnalysis> updateVisitProdAnalyses = new ArrayList<>();
        // 新增用户统计信息
        if (CollUtil.isNotEmpty(saveList)) {
            userAnalysisMapper.saveBatch(saveList);
            for (UserAnalysis userAnalysis : saveList) {
                for (UserVisitProdAnalysis userVisitProdAnalysis : userAnalysis.getUserVisitProdAnalyses()) {
                    userVisitProdAnalysis.setUserAnalysisId(userAnalysis.getUserAnalysisId());
                }
                saveUserVisitProdAnalyses.addAll(userAnalysis.getUserVisitProdAnalyses());
            }
        }
        // 更新用户统计信息
        if (CollUtil.isNotEmpty(updateList)) {
            userAnalysisMapper.updateBatch(updateList);
            for (UserAnalysis userAnalysis : updateList) {
                for (UserVisitProdAnalysis userVisitProdAnalysis : userAnalysis.getUserVisitProdAnalyses()) {
                    if (Objects.isNull(userVisitProdAnalysis.getUserAnalysisId())) {
                        userVisitProdAnalysis.setUserAnalysisId(userAnalysis.getUserAnalysisId());
                        saveUserVisitProdAnalyses.add(userVisitProdAnalysis);
                    } else if (Objects.nonNull(userVisitProdAnalysis.getIsChange()) && userVisitProdAnalysis.getIsChange()) {
                        updateVisitProdAnalyses.add(userVisitProdAnalysis);
                    }
                }
            }
        }
        // 新增用户访问商品信息
        if (CollUtil.isNotEmpty(saveUserVisitProdAnalyses)) {
            userVisitProdAnalysisMapper.saveBatch(saveUserVisitProdAnalyses);
        }
        // 更新用户访问商品信息
        if (CollUtil.isNotEmpty(updateVisitProdAnalyses)) {
            userVisitProdAnalysisMapper.updateBatch(updateVisitProdAnalyses);
        }
        // 删除已合并的用户统计数据(删除需要在更新后面， 更新后剩余的数据再进行删除操作)
        if (CollUtil.isNotEmpty(deleteIds)) {
            userAnalysisMapper.deleteBatch(deleteIds);
            userVisitProdAnalysisMapper.deleteBatch(deleteIds);
        }
    }

    /**
     * 根据系统统计对应的用户数据
     * @param date
     * @param flowLogDTOList
     * @param deleteIds
     */
    private List<UserAnalysis> statisticalUserData(Date date, List<FlowLogDTO> flowLogDTOList, List<Long> deleteIds) {
        List<UserAnalysis> userAnalysesDb = new ArrayList<>();
        Map<Integer, List<FlowLogDTO>> sysTypeMap = flowLogDTOList.stream().collect(Collectors.groupingBy(FlowLogDTO::getSystemType));
        // 根据用户id及uuid（一个用户可能有多个会话），搜索用户统计信息
        Set<String> userIds = flowLogDTOList.stream().filter(flowLogDTO -> Objects.nonNull(flowLogDTO.getUserId())).map(FlowLogDTO::getUserId).collect(Collectors.toSet());
        userIds.addAll(flowLogDTOList.stream().filter(flowLogDTO -> Objects.nonNull(flowLogDTO.getUuid())).map(FlowLogDTO::getUuid).collect(Collectors.toSet()));
        // 搜索数据库中的用户统计信息
        if (CollUtil.isNotEmpty(userIds)) {
            userAnalysesDb = userAnalysisMapper.listByDate(date, userIds);
            List<Long> userAnalysisIds = userAnalysesDb.stream().map(UserAnalysis::getUserAnalysisId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(userAnalysisIds)) {
                List<UserVisitProdAnalysis> userVisitProdAnalysesList = userVisitProdAnalysisMapper.listByUserAnalysisId(userAnalysisIds);
                Map<Long, List<UserVisitProdAnalysis>> userVisitProdAnalysisMap = userVisitProdAnalysesList.stream().collect(Collectors.groupingBy(UserVisitProdAnalysis::getUserAnalysisId));
                for (UserAnalysis userAnalysis : userAnalysesDb) {
                    userAnalysis.setUserVisitProdAnalyses(userVisitProdAnalysisMap.get(userAnalysis.getUserAnalysisId()));
                }
            }
        }
        // 获取订单信息
        Map<String, List<FlowOrderVO>> flowOrderMap = getFlowOrderMap(flowLogDTOList, userIds);
        // 用户统计信息根据系统类型分组（Map<Integer（系统类型）, List<UserAnalysis>（用户统计信息列表）>）
        Map<Integer, List<UserAnalysis>> userAnalysisSysMapDb = userAnalysesDb.stream().collect(Collectors.groupingBy(UserAnalysis::getSystemType));
        // 获取未统计的用户信息
        Map<String, UserApiVO> userMap = getUserMap(userIds, userAnalysesDb);

        for (Integer sysType : sysTypeMap.keySet()) {
            if (!userAnalysisSysMapDb.containsKey(sysType)) {
                userAnalysisSysMapDb.put(sysType, new ArrayList<>());
            }
            Map<String, UserAnalysis> userAnalysisMapDb = userAnalysisSysMapDb.get(sysType).stream().collect(Collectors.toMap(UserAnalysis::getUserId, Function.identity(),(u1, u2)  -> u2));
            Map<String, List<FlowLogDTO>> userFlowMap = sysTypeMap.get(sysType).stream().collect(Collectors.groupingBy(FlowLogDTO::getUuid));
            // 根据用户进行统计
            for (String uuid : userFlowMap.keySet()) {
                UserAnalysis userAnalysis = getUserAnalysis(date, sysType, userFlowMap.get(uuid), userMap, userAnalysisMapDb, userAnalysesDb, deleteIds);
                // null则直接返回
                if (Objects.isNull(userAnalysis)) {
                    continue;
                }
                List<String> spuIdDbs = userAnalysis.getUserVisitProdAnalyses().stream().map(userVisitProdAnalysis -> userVisitProdAnalysis.getSpuId().toString()).collect(Collectors.toList());
                // 统计各个页面的信息
                for (FlowLogDTO flowLogDTO : userFlowMap.get(uuid)) {
                    statisticalUserOperation(userAnalysis, flowLogDTO, flowOrderMap);
                    if (Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PROD_INFO.value())
                            && !spuIdDbs.contains(flowLogDTO.getBizData())) {
                        UserVisitProdAnalysis userVisitProdAnalysis = new UserVisitProdAnalysis();
                        if (Objects.isNull(flowLogDTO.getBizData())) {
                            continue;
                        }
                        userVisitProdAnalysis.setSpuId(Long.valueOf(flowLogDTO.getBizData()));
                        userVisitProdAnalysis.setCreateDate(date);
                        userAnalysis.getUserVisitProdAnalyses().add(userVisitProdAnalysis);
                        spuIdDbs.add(flowLogDTO.getBizData());
                    }
                }
            }

        }
        return userAnalysesDb;
    }

    /**
     * 获取流量订单信息
     *
     * @param flowLogDTOList 用户操作记录列表
     * @param userIds 用户id列表
     * @return
     */
    private Map<String, List<FlowOrderVO>> getFlowOrderMap(List<FlowLogDTO> flowLogDTOList, Set<String> userIds) {
        if (CollUtil.isEmpty(flowLogDTOList)) {
            return new HashMap<>(0);
        }
        List<FlowLogDTO> payList = flowLogDTOList.stream().filter(flowLogDTO -> Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PAY.value())).collect(Collectors.toList());
        if (CollUtil.isEmpty(payList)) {
            return new HashMap<>(0);
        }
        ServerResponseEntity<List<FlowOrderVO>> orderResponse = orderFeignClient.listFlowOrderByOrderIds(getOrderIdByFlowList(payList));

        Map<String, List<FlowOrderVO>> orderMap = orderResponse.getData().stream().collect(Collectors.groupingBy(flowOrderVO -> flowOrderVO.getOrderId().toString()));
        userIds.addAll(orderResponse.getData().stream().map(flowOrderVO -> flowOrderVO.getUserId().toString()).collect(Collectors.toSet()));
        return orderMap;
    }

    /**
     * 获取用户的订单id列表
     *
     * @param payList 用户支付操作信息列表
     * @return 订单id列表
     */
    private Set<Long> getOrderIdByFlowList(List<FlowLogDTO> payList) {
        Set<Long> orderSet = new HashSet<>();
        for (FlowLogDTO flowLogDTO : payList) {
            if(NUMBER_PATTERN.matcher(flowLogDTO.getBizData()).matches()) {
                orderSet.add(Long.valueOf(flowLogDTO.getBizData()));
            }
        }
        return orderSet;
    }

    /**
     * 统计用户操作数据(点击、访问、加购、订单金额)
     *  @param userAnalysis 用户统计数据
     * @param flowLogDTO 用户操作记录map
     * @param flowOrderMap 订单信息map
     */
    private void statisticalUserOperation(UserAnalysis userAnalysis, FlowLogDTO flowLogDTO, Map<String, List<FlowOrderVO>> flowOrderMap) {
        if (FlowVisitEnum.isVisitOrShare(flowLogDTO.getVisitType())) {
            userAnalysis.setVisitNums(userAnalysis.getVisitNums() + flowLogDTO.getNums());
        } else if (Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.CLICK.value())) {
            userAnalysis.setClickNums(userAnalysis.getClickNums() + flowLogDTO.getNums());
        } else if (Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.SHOP_CAT.value())) {
            userAnalysis.setPlusShopCart(userAnalysis.getPlusShopCart() + flowLogDTO.getNums());
        }
        // 计算用户下单及支付的金额
        if (Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PAY.value())) {
            List<FlowOrderVO> flowOrders = flowOrderMap.get(flowLogDTO.getBizData());
            if (CollUtil.isEmpty(flowOrders)) {
                return;
            }
            for (FlowOrderVO flowOrder : flowOrders) {
                if (Objects.equals(flowOrder.getIsPayed(), 1)) {
                    userAnalysis.setPayAmount(userAnalysis.getPayAmount() + flowOrder.getOrderAmount());
                }
                userAnalysis.setPlaceOrderAmount(userAnalysis.getPlaceOrderAmount() + flowOrder.getOrderAmount());
            }
            flowOrderMap.remove(flowLogDTO.getBizData());
        }
    }

    /**
     * 获取用户统计数据（若还未统计，则初始化统计数据并返回）
     *
     * @param date 时间
     * @param sysType 系统类型
     * @param flowLogDTOList 用户操作记录列表
     * @param userMap 用户信息map
     * @param userAnalysisMapDb 已统计的用户信息map（新增的用户统计数据需要保存到此处）
     * @param userAnalysisList  已统计的用户信息列表（新增的用户统计数据需要保存到此处）
     * @param deleteIds
     * @return 用户统计数据
     */
    private UserAnalysis getUserAnalysis(Date date, Integer sysType, List<FlowLogDTO> flowLogDTOList, Map<String, UserApiVO> userMap,
                                         Map<String, UserAnalysis> userAnalysisMapDb, List<UserAnalysis> userAnalysisList, List<Long> deleteIds) {
        flowLogDTOList.sort(Comparator.comparing(FlowLogDTO::getStep).reversed());
        FlowLogDTO flowLogDTO = flowLogDTOList.get(0);
        UserAnalysis userAnalysis;
        // 合并数据-同时存在用户登陆与未登录的数据
        if (userAnalysisMapDb.containsKey(flowLogDTO.getUuid())) {
            userAnalysis = userAnalysisMapDb.get(flowLogDTO.getUuid());
            // 若已有该用户的统计信息
            if (userAnalysisMapDb.containsKey(flowLogDTO.getUserId())) {
                // 把用户两次会话的统计数据进行合并
                UserAnalysis userAnalysisDb = userAnalysisMapDb.get(flowLogDTO.getUserId());
                userAnalysisDb.setPlusShopCart(userAnalysisDb.getPlusShopCart() + userAnalysis.getPlusShopCart());
                userAnalysisDb.setPlaceOrderAmount(userAnalysisDb.getPlaceOrderAmount() + userAnalysis.getPlaceOrderAmount());
                userAnalysisDb.setPayAmount(userAnalysisDb.getPayAmount() + userAnalysis.getPayAmount());
                userAnalysisDb.setVisitNums(userAnalysisDb.getVisitNums() + userAnalysis.getVisitNums());
                userAnalysisDb.setClickNums(userAnalysisDb.getClickNums() + userAnalysis.getClickNums());
                List<Long> spuIds = userAnalysisDb.getUserVisitProdAnalyses().stream().map(UserVisitProdAnalysis::getSpuId).collect(Collectors.toList());
                // 合并浏览的商品
                for (UserVisitProdAnalysis userVisitProdAnalysis : userAnalysis.getUserVisitProdAnalyses()) {
                    if (!spuIds.contains(userVisitProdAnalysis.getSpuId())) {
                        userVisitProdAnalysis.setUserAnalysisId(userAnalysisDb.getUserAnalysisId());
                        userVisitProdAnalysis.setIsChange(Boolean.TRUE);
                        userAnalysisDb.getUserVisitProdAnalyses().add(userVisitProdAnalysis);
                    }
                }
                deleteIds.add(userAnalysis.getUserAnalysisId());
                userAnalysis = userAnalysisDb;
                userAnalysisMapDb.remove(flowLogDTO.getUuid());
            }
            // 用户已进行登陆--更改key为用户id
            else if (Objects.nonNull(flowLogDTO.getUserId())) {
                userAnalysisMapDb.remove(flowLogDTO.getUuid());
                userAnalysis.setUserId(flowLogDTO.getUserId());
                userAnalysis.setUserType(getUserType(date, userMap.get(flowLogDTO.getUserId())));
                userAnalysisMapDb.put(userAnalysis.getUserId(), userAnalysis);
            }
        }
        // 已存在用户统计数据， 且用户已登陆
        else if (userAnalysisMapDb.containsKey(flowLogDTO.getUserId())) {
            userAnalysis = userAnalysisMapDb.get(flowLogDTO.getUserId());
        }
        // 用户数据未统计，初始化统计数据
        else {
            userAnalysis = new UserAnalysis();
            // 已登陆用户
            if (StrUtil.isNotBlank(flowLogDTO.getUserId())) {
                userAnalysis.setUserId(flowLogDTO.getUserId());
                userAnalysis.setUserType(getUserType(date, userMap.get(flowLogDTO.getUserId())));
            }
            // 未登陆用户
            else {
                userAnalysis.setUserId(flowLogDTO.getUuid());
                userAnalysis.setUserType(FlowUserTypeEnum.NOT_LOGIN.value());
            }
            // 初始化数据
            userAnalysis.setCreateDate(date);
            userAnalysis.setPlusShopCart(Constant.ZERO_INTEGER);
            userAnalysis.setPlaceOrderAmount(Constant.ZERO_LONG);
            userAnalysis.setPayAmount(Constant.ZERO_LONG);
            userAnalysis.setVisitNums(Constant.ZERO_INTEGER);
            userAnalysis.setClickNums(Constant.ZERO_INTEGER);
            userAnalysis.setSystemType(sysType);
            userAnalysis.setSpuIds(new ArrayList<>());
            userAnalysis.setProvinceId(getProvinceIdByUserIp(flowLogDTO.getIp()));
            userAnalysisMapDb.put(userAnalysis.getUserId(), userAnalysis);
            userAnalysisList.add(userAnalysis);
        }
        if (Objects.isNull(userAnalysis.getUserVisitProdAnalyses())) {
            userAnalysis.setUserVisitProdAnalyses(new ArrayList<>());
        }
        return userAnalysis;
    }

    /**
     * 获取用户类型（0:旧用户, 1:新用户, 2.未登陆用户）
     *
     * @param date 时间
     * @param userApiVO 用户信息
     * @return
     */
    private Integer getUserType(Date date, UserApiVO userApiVO) {
        if (Objects.isNull(userApiVO)) {
            return FlowUserTypeEnum.NOT_LOGIN.value();
        }
        long nextTime = DateUtil.offsetDay(date, 1).getTime();
        long createTime = userApiVO.getCreateTime().getTime();
        // 用户创建时间大于等于date的开始时间，小于date的结束时间
        if (date.getTime() <= createTime && createTime < nextTime) {
            return FlowUserTypeEnum.NEW_USER.value();
        }
        return FlowUserTypeEnum.OLDER_USER.value();
    }

    /**
     * 获取未统计的用户信息
     *
     * @param userIds 用户id列表
     * @param userAnalysesDb 用户统计信息
     * @return 未统计的用户信息列表
     */
    private Map<String, UserApiVO> getUserMap(Set<String> userIds, List<UserAnalysis> userAnalysesDb) {
        Set<Long> userIdSet = new HashSet<>();
        for (String userId : userIds) {
            if(NUMBER_PATTERN.matcher(userId).matches()) {
                userIdSet.add(Long.valueOf(userId));
            }
        }
        List<String> userIdDb = userAnalysesDb.stream().map(UserAnalysis::getUserId).collect(Collectors.toList());
        userIds.removeAll(userIdDb);
        Map<String, UserApiVO> userMap = null;
        if (CollUtil.isNotEmpty(userIdSet)) {
            ServerResponseEntity<List<UserApiVO>> userResponse = userFeignClient.getUserByUserIds(new ArrayList<>(userIdSet));
            if (CollUtil.isNotEmpty(userResponse.getData())) {
                userMap = userResponse.getData().stream().collect(Collectors.toMap(userApiVO -> userApiVO.getUserId().toString(), u -> u));
            }
        }
        if (MapUtil.isEmpty(userMap)) {
            return new HashMap<>(0);
        }
        return userMap;
    }

    private Long getProvinceIdByUserIp(String ip) {
        ServerResponseEntity<List<AreaVO>> areaResponse = areaFeignClient.listProvinceArea();
        List<AreaVO> areaList = areaResponse.getData();
        String province = "";

        // 根据IP搜索地址信息
        try {
            // B树搜索（更快）
            Method method = dbSearcher.getClass().getMethod("memorySearch", String.class);
            DataBlock dataBlock  = (DataBlock) method.invoke(dbSearcher, ip);
            String region = dataBlock.getRegion();
            province = region.split("\\|")[2];
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        for (AreaVO area:areaList){
            if (area.getAreaName().indexOf(province) != -1){
                return area.getAreaId();
            }
        }
        return 0L;
    }

}
