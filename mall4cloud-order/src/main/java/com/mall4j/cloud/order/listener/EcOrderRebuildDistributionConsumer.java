package com.mall4j.cloud.order.listener;
import java.util.Date;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcAddressInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcCommissionInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcSharerInfo;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.PromoterResp;
import com.mall4j.cloud.api.biz.feign.ChannelsPromoterFeignClient;
import com.mall4j.cloud.api.biz.feign.ChannelsSharerFeign;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.dto.platform.request.SyncSharerInfoRequest;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderAddr;
import com.mall4j.cloud.order.service.OrderAddrService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 视频号4.0订单支付成功，延迟重算分销信息。
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.EC_ORDER_REBUILD_DISTRIBUTION_TOP,consumerGroup = "GID_"+RocketMqConstant.EC_ORDER_REBUILD_DISTRIBUTION_TOP)
public class EcOrderRebuildDistributionConsumer implements RocketMQListener<Long> {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderAddrService orderAddrService;
    @Autowired
    ChannlesFeignClient channlesFeignClient;
    @Autowired
    TentacleContentFeignClient tentacleContentFeignClient;
    @Autowired
    ChannelsSharerFeign channelsSharerFeign;
    @Autowired
    ChannelsPromoterFeignClient channelsPromoterFeignClient;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private OnsMQTemplate stdOrderNotifyTemplate;

    @Override
    public void onMessage(Long orderId) {
        log.info("视频号订单支付成功，重算分销信息，接收参数：{}",orderId);
        Order order = orderService.getByOrderId(orderId);

        ServerResponseEntity<EcOrderResponse> orderResponse = channlesFeignClient.getOrder(order.getWechatOrderId());
        if(orderResponse!=null && orderResponse.isSuccess() && orderResponse.getData() != null){
            //通过订单的分享员信息获取到 分享员的触点
            EcSharerInfo ecSharerInfo = orderResponse.getData().getOrder().getOrder_detail().getSharer_info();
            log.info("视频号订单支付成功，重算分销信息，查询的分销员信息：{}", JSONObject.toJSONString(ecSharerInfo));
            if(ecSharerInfo !=null && StrUtil.isNotEmpty(ecSharerInfo.getSharer_openid())){
                //更新订单的4.0分享员信息
                SyncSharerInfoRequest request = new SyncSharerInfoRequest();
                request.setOrderId(order.getOrderId());
                request.setSharerOpenid(ecSharerInfo.getSharer_openid());
                orderService.syncChannelsSharerInfo(request);

                ServerResponseEntity<Long> channelsServerResponse = channelsSharerFeign.getStaffId(ecSharerInfo.getSharer_openid());
                if(channelsServerResponse !=null && channelsServerResponse.isSuccess() && channelsServerResponse.getData()!=null && channelsServerResponse.getData()>0){
                    Long staffId = channelsServerResponse.getData();
                    TentacleContentDTO contentDTO = new TentacleContentDTO();
                    contentDTO.setBusinessId(staffId);
                    contentDTO.setTentacleType(4);
                    ServerResponseEntity<TentacleContentVO> serverResponse = tentacleContentFeignClient.findOrCreateByContent(contentDTO);
                    if(serverResponse!=null && serverResponse.isSuccess() && serverResponse.getData()!=null){
                        TentacleContentVO contentVO = serverResponse.getData();
                        //获取到当前分享员到触点信息
                        order.setTentacleNo(contentVO.getTentacleNo());
                    }
                }
            }

            try {
                //获取订单详情中达人信息
                List<EcCommissionInfo> ecCommissionInfos = orderResponse.getData().getOrder().getOrder_detail().getCommission_infos();
                log.info("视频号订单支付成功，重算分销信息，查询到的达人信息：{}", JSONObject.toJSONString(ecCommissionInfos));
                if(CollectionUtil.isNotEmpty(ecCommissionInfos)){
                    EcCommissionInfo ecCommissionInfo = ecCommissionInfos.stream().filter(c -> c.getType() ==0).findFirst().get();
                    if(ecCommissionInfo!=null && StrUtil.isNotEmpty(ecCommissionInfo.getFinder_id())){
                        ServerResponseEntity<PromoterResp> serverResponse = channelsPromoterFeignClient.getPromoterByFinderId(ecCommissionInfo.getFinder_id());
                        log.info("达人finder_id查询信息，查询参数:{},返回结果：{}",ecCommissionInfo.getFinder_id(),JSONObject.toJSONString(serverResponse));
                        if(serverResponse!=null && serverResponse.isSuccess() && serverResponse.getData()!=null
                                && serverResponse.getData().getStoreId()>0){
                            PromoterResp promoterResp =serverResponse.getData();
                            //更新订单的达人信息， 修改下单门店、达人id，达人昵称。
                            orderMapper.syncFinderOpenid(order.getOrderId(),promoterResp.getFinderId(),promoterResp.getFinderName(),promoterResp.getStoreId());
                        }
                    }
                }
            }catch (Exception e){
                log.error("更新订单达人信息失败。{} {}",e,e.getMessage());
                e.printStackTrace();
            }

            updateAddrInfo(order, orderResponse.getData().getOrder().getOrder_detail().getDelivery_info().getAddress_info());

        }

        orderService.reBuildDistributionInfo(order.getOrderId(),order.getTentacleNo());

        //计算完分享员达人信息后，重推一次订单到中台。
        stdOrderNotifyTemplate.syncSend(orderId, RocketMqConstant.ORDER_NOTIFY_STD_TOPIC_TAG);
    }

    /**
     * 重新更新收货信息,创建订单时有可能没有部分收货地址信息
     * @param order
     * @param ecAddressInfo
     */
    private void updateAddrInfo(Order order, EcAddressInfo ecAddressInfo) {
        try {
            OrderAddr orderAddr = new OrderAddr();
            orderAddr.setOrderAddrId(order.getOrderAddrId());
            orderAddr.setConsignee(ecAddressInfo.getUser_name());
            orderAddr.setProvince(ecAddressInfo.getProvince_name());
            orderAddr.setCity(ecAddressInfo.getCity_name());
            orderAddr.setArea(ecAddressInfo.getCounty_name());
            orderAddr.setAddr(ecAddressInfo.getDetail_info());
            orderAddr.setMobile(ecAddressInfo.getTel_number());
            orderAddr.setUpdateTime(new Date());
            orderAddrService.update(orderAddr);
        } catch (Exception e) {
            log.error("重新更新订单收货地址失败:{},{}",e.getMessage(),e);
        }
    }

}
