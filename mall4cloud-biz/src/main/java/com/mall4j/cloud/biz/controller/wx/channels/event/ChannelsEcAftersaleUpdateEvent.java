package com.mall4j.cloud.biz.controller.wx.channels.event;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcAfterSaleOrder;
import com.mall4j.cloud.api.biz.dto.channels.EcReturnInfo;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.api.user.feign.UserRegisterFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.model.WechatLogisticsMappingDO;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.service.WechatLiveLogisticService;
import com.mall4j.cloud.biz.service.channels.ChannelsSkuService;
import com.mall4j.cloud.biz.service.channels.EcAftersaleService;
import com.mall4j.cloud.biz.service.channels.EcOrderService;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *售后单更新通知
 *
 */
@Slf4j
@Service
public class ChannelsEcAftersaleUpdateEvent implements INotifyEvent, InitializingBean {

    private static final String method = "channels_ec_aftersale_update";
    @Autowired
    UserRegisterFeignClient userRegisterFeignClient;
    @Autowired
    EcOrderService ecOrderService;
    @Autowired
    EcAftersaleService ecAftersaleService;
    @Autowired
    WechatLiveLogisticService wechatLiveLogisticService;
    @Autowired
    ChannelsSkuService channelsSkuService;

    /**
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/ec_callback/channels_ec_aftersale_update.html
     *
     * {
     *     "ToUserName": "gh_*",
     *     "FromUserName": "OpenID",
     *     "CreateTime": 1662480000,
     *     "MsgType": "event",
     *     "Event": "channels_ec_aftersale_update",
     *     "finder_shop_aftersale_status_update": {
     *         "status": "USER_WAIT_RETURN",
     *         "after_sale_order_id": "1234567",
     *         "order_id":"12345"
     *     }
     * }
     *
     * 枚举-EcAftersaleInfoStatus
     * 值	说明
     * USER_CANCELD	用户取消申请
     * MERCHANT_PROCESSING	商家受理中
     * MERCHANT_REJECT_REFUND	商家拒绝退款
     * MERCHANT_REJECT_RETURN	商家拒绝退货退款
     * USER_WAIT_RETURN	待买家退货
     * RETURN_CLOSED	退货退款关闭
     * MERCHANT_WAIT_RECEIPT	待商家收货
     * MERCHANT_OVERDUE_REFUND	商家逾期未退款
     * MERCHANT_REFUND_SUCCESS	退款完成
     * MERCHANT_RETURN_SUCCESS	退货退款完成
     * PLATFORM_REFUNDING	平台退款中
     * PLATFORM_REFUND_FAIL	平台退款失败
     * USER_WAIT_CONFIRM	待用户确认
     * MERCHANT_REFUND_RETRY_FAIL	商家打款失败，客服关闭售后
     * MERCHANT_FAIL	售后关闭
     *
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("售后单更新通知回调，输入参数：{}", postData);

        JSONObject postDataJson =  JSONObject.parseObject(postData);
        JSONObject finderShopAftersaleStatusUpdateJson = postDataJson.getJSONObject("finder_shop_aftersale_status_update");

        String outOrderId = finderShopAftersaleStatusUpdateJson.getString("order_id");
        EcOrderResponse ecOrderResponse = ecOrderService.get(outOrderId);
        String unionId =  ecOrderResponse.getOrder().getUnionid();
        if(StrUtil.isEmpty(unionId)){
            Assert.faild("unionId为空，请检查视频号小店是否绑定开放平台。");
        }

        ServerResponseEntity<UserApiVO> userResponse = userRegisterFeignClient.ecChannelRegister(unionId);
        if(userResponse==null || userResponse.isFail()){
            log.error("生成临时用户失败，error:{}",userResponse.getMsg());
            return "error";
        }

        UserApiVO userApiVO = userResponse.getData();

        String type = finderShopAftersaleStatusUpdateJson.getString("status");
        Long afterSaleOrderId = finderShopAftersaleStatusUpdateJson.getLongValue("after_sale_order_id");

        //商家受理中
        if(StrUtil.equals(type,"MERCHANT_PROCESSING")){
            createOrderRefund(postData);
        }else if(StrUtil.equals(type,"USER_CANCELD")){
            //用户取消申请
            // 根据售后单号查询售后数据
        }else if(StrUtil.equals(type,"MERCHANT_REJECT_REFUND")){
            //商家拒绝退款
            //这里只允许小程序端处理，所以这里回调不处理
            log.info(StrUtil.format("商家拒绝退款，请求参数：{}。",postData));
        }else if(StrUtil.equals(type,"MERCHANT_REJECT_RETURN")){
            //商家拒绝退货退款
            //这里只允许小程序端处理，所以这里回调不处理
            log.info(StrUtil.format("商家拒绝退货退款，请求参数：{}。",postData));
        }else if(StrUtil.equals(type,"USER_WAIT_RETURN")){
            //TODO 待买家退货
            log.info(StrUtil.format("待买家退货，请求参数：{}。",postData));
        }else if(StrUtil.equals(type,"RETURN_CLOSED")){
            //退货退款关闭
        }else if(StrUtil.equals(type,"MERCHANT_WAIT_RECEIPT")){
            //待商家收货
            //这里为买家发货填完物流信息的回调
            merchant_wait_receipt(postData);
        }else if(StrUtil.equals(type,"MERCHANT_OVERDUE_REFUND")){
            //商家逾期未退款
            log.info(StrUtil.format("商家逾期未退款，请求参数：{}。",postData));
        }else if(StrUtil.equals(type,"MERCHANT_REFUND_SUCCESS")){
            //退款完成
            //不管退单处于何种状态，此时都检查并修改退单为退款完成。
            refund_success(postData);
        }else if(StrUtil.equals(type,"MERCHANT_RETURN_SUCCESS")){
            //退货退款完成
            //不管退单处于何种状态，此时都检查并修改退单为退款完成。
            refund_success(postData);
        }else if(StrUtil.equals(type,"PLATFORM_REFUNDING")){
            //平台退款中
            log.info(StrUtil.format("平台退款中，请求参数：{}。",postData));
        }else if(StrUtil.equals(type,"PLATFORM_REFUND_FAIL")){
            //平台退款失败
            //转线下退款
            platform_refund_fail(postData);
        }else if(StrUtil.equals(type,"USER_WAIT_CONFIRM")){
            //待用户确认
            log.info(StrUtil.format("待用户确认，请求参数：{}。",postData));
        }else if(StrUtil.equals(type,"MERCHANT_REFUND_RETRY_FAIL")){
            //商家打款失败，客服关闭售后
            //退货退款关闭
        }else if(StrUtil.equals(type,"MERCHANT_FAIL")){
            //售后关闭
            log.info(StrUtil.format("售后关闭，请求参数：{}。",postData));
        }else {
            log.error(StrUtil.format("售后回调-event不匹配，请求参数：{}。",postData));
        }

        return "success";
    }

    /**
     * 平台退款失败，转线下退款
     *
     * @param postData
     */
    private void platform_refund_fail(String postData) {
        JSONObject postDataJson =  JSONObject.parseObject(postData);
        JSONObject finderShopAftersaleStatusUpdateJson = postDataJson.getJSONObject("finder_shop_aftersale_status_update");
        Long afterSaleOrderId = finderShopAftersaleStatusUpdateJson.getLongValue("after_sale_order_id");
    }

    /**
     * 退款成功
     * @param postData
     */
    private void refund_success(String postData) {
        JSONObject postDataJson =  JSONObject.parseObject(postData);
        JSONObject finderShopAftersaleStatusUpdateJson = postDataJson.getJSONObject("finder_shop_aftersale_status_update");
        Long afterSaleOrderId = finderShopAftersaleStatusUpdateJson.getLongValue("after_sale_order_id");

        EcGetaftersaleorderResponse ecGetaftersaleorderResponse = ecAftersaleService.getById(afterSaleOrderId);
        if(ecGetaftersaleorderResponse==null || ecGetaftersaleorderResponse.getErrcode()!=0 || ecGetaftersaleorderResponse.getAfter_sale_order()==null){
            Assert.faild("获取售后详情失败。");
        }
        EcAfterSaleOrder afterSaleOrder = ecGetaftersaleorderResponse.getAfter_sale_order();
        if(afterSaleOrder == null){
            Assert.faild("获取售后详情失败。");
        }
    }


    /**
     * 创建售后单
     * 创建前检查售后单是否存在。
     * 如果已经存在，则重置售后单据状态为待处理。
     * 如果不存在，则新增售后单据。
     * @param postData
     */
    private void createOrderRefund(String postData){
        JSONObject postDataJson =  JSONObject.parseObject(postData);
        JSONObject finderShopAftersaleStatusUpdateJson = postDataJson.getJSONObject("finder_shop_aftersale_status_update");
        Long afterSaleOrderId = finderShopAftersaleStatusUpdateJson.getLongValue("after_sale_order_id");
        Long wechatOrderId = finderShopAftersaleStatusUpdateJson.getLongValue("order_id");

        EcGetaftersaleorderResponse ecGetaftersaleorderResponse = ecAftersaleService.getById(afterSaleOrderId);
        if(ecGetaftersaleorderResponse==null || ecGetaftersaleorderResponse.getErrcode()!=0){
            Assert.faild("获取售后详情失败。");
        }
        EcAfterSaleOrder afterSaleOrder = ecGetaftersaleorderResponse.getAfter_sale_order();


        ChannelsSku channelsSku = channelsSkuService.getByOutSkuId(Long.parseLong(afterSaleOrder.getProduct_info().getSku_id()));
        Long skuId = channelsSku.getSkuId();
    }

    /**
     * 买家上传物流 同步物流信息
     *
     * @param postDate
     */
    private void merchant_wait_receipt(String postDate){
        JSONObject postDataJson =  JSONObject.parseObject(postDate);
        JSONObject finderShopAftersaleStatusUpdateJson = postDataJson.getJSONObject("finder_shop_aftersale_status_update");
        Long afterSaleOrderId = finderShopAftersaleStatusUpdateJson.getLongValue("after_sale_order_id");
        String outOrderId = "";

        EcGetaftersaleorderResponse ecGetaftersaleorderResponse = ecAftersaleService.getById(afterSaleOrderId);
        if(ecGetaftersaleorderResponse==null || ecGetaftersaleorderResponse.getErrcode()!=0 || ecGetaftersaleorderResponse.getAfter_sale_order()==null){
            Assert.faild("获取售后详情失败。");
        }
        EcAfterSaleOrder afterSaleOrder = ecGetaftersaleorderResponse.getAfter_sale_order();
        if(afterSaleOrder == null){
            Assert.faild("获取售后详情失败。");
        }

        EcReturnInfo ecReturnInfo = afterSaleOrder.getReturn_info();
        WechatLogisticsMappingDO mapping = wechatLiveLogisticService.getByWechatDeliveryId(ecReturnInfo.getDelivery_id());
        //如果没有找到，取一个默认的物流公司流程继续走下去。
        if(mapping==null){
            mapping = wechatLiveLogisticService.getDefualtWechatDelivery();
        }

    }



    @Override
    public void register(String event, INotifyEvent notifyEvent) {
        INotifyEvent.super.register(event, notifyEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
