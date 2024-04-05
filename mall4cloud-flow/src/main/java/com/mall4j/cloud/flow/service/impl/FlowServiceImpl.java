package com.mall4j.cloud.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.flow.constant.FlowLogPageEnum;
import com.mall4j.cloud.flow.constant.FlowVisitEnum;
import com.mall4j.cloud.flow.dto.FlowLogDTO;
import com.mall4j.cloud.flow.service.FlowService;
import com.mall4j.cloud.flow.service.ProductAnalyseService;
import com.mall4j.cloud.flow.service.UserAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * 流量分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Service
public class FlowServiceImpl implements FlowService {

    private static final Logger log = LoggerFactory.getLogger(FlowServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductAnalyseService productAnalyseService;
    @Autowired
    private UserAnalysisService userAnalysisService;


    @Override
    public void log(FlowLogDTO flowLogDTO) {
        flowLogDTO.setCreateTime(DateUtil.beginOfDay(new Date()));
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        // 登陆后，插入用户id
        if (Objects.nonNull(userInfoInTokenBO)) {
            flowLogDTO.setUserId(userInfoInTokenBO.getUserId().toString());
        } else {
            flowLogDTO.setUserId(null);
        }
        if (FlowVisitEnum.isVisitOrShare(flowLogDTO.getVisitType()) || Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.CLICK.value())) {
            flowLogDTO.setNums(1);
        }
        log.info("缓存数据：{}", JSONObject.toJSONString(flowLogDTO));
        // 用户数据
        redisTemplate.boundListOps(CacheNames.FLOW_FLOW_DATA).rightPush(flowLogDTO);

        // 商品数据
        if (Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PROD_INFO.value()) || Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.SHOP_CAT.value())) {
            log.info("商品数据添加");
            redisTemplate.boundListOps(CacheNames.FLOW_PRODUCT_DATA).rightPush(flowLogDTO);
        }
    }

    @Override
    public synchronized void statisticalUser() {
        BoundListOperations boundListOperations = redisTemplate.boundListOps(CacheNames.FLOW_FLOW_DATA);
        Long size = boundListOperations.size();
        long maxIndex;
        while (size > 0) {
            if (size > Constant.MAX_DATA_HANDLE_NUM) {
                maxIndex = Constant.MAX_DATA_HANDLE_NUM;
            } else {
                maxIndex = size;
            }
            size = size - Constant.MAX_DATA_HANDLE_NUM;
            // 获取范围内的用户操作信息
            List<FlowLogDTO> flowLogList = boundListOperations.range(0, maxIndex -1);
            // 订单延迟半个小时处理
            handlePayData(flowLogList, boundListOperations);
            userAnalysisService.statisticalUser(flowLogList);
            // 统计成功后，删除已统计的信息
            boundListOperations.trim(maxIndex, -1);
        }
    }

    @Override
    public synchronized  void statisticalProduct() {
        BoundListOperations boundListOperations = redisTemplate.boundListOps(CacheNames.FLOW_PRODUCT_DATA);
        Long size = boundListOperations.size();
        long maxIndex;
        while (size > 0) {
            if (size > Constant.MAX_DATA_HANDLE_NUM) {
                maxIndex = Constant.MAX_DATA_HANDLE_NUM;
            } else {
                maxIndex = size;
            }
            size = size - Constant.MAX_DATA_HANDLE_NUM;
            List<FlowLogDTO> flowLogList = boundListOperations.range(0, maxIndex -1);
            log.info("缓存数据：{}", JSONObject.toJSONString(flowLogList));
            productAnalyseService.statisticalProduct(flowLogList);
            boundListOperations.trim(maxIndex, -1);
        }
    }

    /**
     * 处理支付的信息
     * @param flowLogList
     * @param boundListOperations
     */
    private void handlePayData(List<FlowLogDTO> flowLogList, BoundListOperations boundListOperations) {
        long time = DateUtil.offsetMinute(new Date(), -Constant.CANCEL_ORDER).getTime();
        List<FlowLogDTO> handleList = new ArrayList<>();
        Iterator<FlowLogDTO> iterator = flowLogList.iterator();
        while (iterator.hasNext()) {
            try{
                FlowLogDTO flowLogDTO = iterator.next();
                // 订单信息需要等取消订单处理完后，再进行处理
                if (Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PAY.value()) && flowLogDTO.getCreateTime().getTime() > time) {
                    iterator.remove();
                    handleList.add(flowLogDTO);
                }
            }catch (Exception e){
                log.info("---handlePayData--error->>> {} {} ",e,e.getMessage());
                iterator.remove();
            }
        }
        if (CollUtil.isNotEmpty(handleList)) {
            boundListOperations.rightPushAll(handleList);
        }
    }
}
