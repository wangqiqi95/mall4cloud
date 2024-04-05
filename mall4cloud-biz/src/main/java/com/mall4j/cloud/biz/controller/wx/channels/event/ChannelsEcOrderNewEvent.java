package com.mall4j.cloud.biz.controller.wx.channels.event;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.*;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.user.feign.UserRegisterFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.mall4j.cloud.biz.service.channels.EcOrderService;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *订单下单回调
 *
 */
@Slf4j
@Service
public class ChannelsEcOrderNewEvent implements INotifyEvent, InitializingBean {

    private static final String method = "channels_ec_order_new";
    @Autowired
    UserRegisterFeignClient userRegisterFeignClient;
    @Autowired
    EcOrderService ecOrderService;
    @Autowired
    ChannelsSharerService channelsSharerService;
    @Autowired
    TentacleContentFeignClient tentacleContentFeignClient;


    /**
     * https://developers.weixin.qq.com/doc/channels/API/order/callback/channels_ec_order_new.html
     *
     * {
     *     "ToUserName": "gh_*",
     *     "FromUserName": "OPENID",
     *     "CreateTime": 1662480000,
     *     "MsgType": "event",
     *     "Event": "channels_ec_order_new",
     *     "order_info": {
     *         "order_id": 3705115058471208928
     *     }
     * }
     *
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("订单下单回调，输入参数：{}", postData);

        JSONObject jsonObject =  JSONObject.parseObject(postData);
        String openid = jsonObject.getString("FromUserName");

        JSONObject order_infoJson = jsonObject.getJSONObject("order_info");
        String outOrderId = order_infoJson.getString("order_id");
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


        ShopCartOrderMergerVO shopCartOrderMergerVO = ecBuildOrder(userApiVO.getUserId(),ecOrderResponse);
        shopCartOrderMergerVO.setWechatOrderId(Long.parseLong(outOrderId));
        shopCartOrderMergerVO.setStoreId(Constant.MAIN_SHOP);
        return "success";
    }

    @Override
    public void register(String event, INotifyEvent notifyEvent) {
        INotifyEvent.super.register(event, notifyEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }

    /**
     * 构建订单信息
     * @param userId
     * @param ecOrderResponse
     * @return
     */
    private ShopCartOrderMergerVO ecBuildOrder(Long userId,EcOrderResponse ecOrderResponse){
        EcOrder order = ecOrderResponse.getOrder();
        EcOrderDetail orderDetail = order.getOrder_detail();

        ShopCartOrderMergerVO shopCartOrderMergerVO = new ShopCartOrderMergerVO();
        shopCartOrderMergerVO.setUserId(userId);
        // 订单来源视频号4.0
        shopCartOrderMergerVO.setOrderSource(OrderSource.CHANNELS.value());
        //订单类型
        shopCartOrderMergerVO.setOrderType(OrderType.ORDINARY);
        //订单收货地址
        UserAddrVO userAddrVO = ecBuildUserAddrVO(order.getOrder_detail().getDelivery_info());
        shopCartOrderMergerVO.setUserAddr(userAddrVO);
        //订单实付金额
        EcPriceInfo ecPriceInfo = orderDetail.getPrice_info();
        //订单总价
        shopCartOrderMergerVO.setTotal(ecPriceInfo.getOrder_price());
        //订单实付
        shopCartOrderMergerVO.setActualTotal(ecPriceInfo.getOrder_price());
        //订单优惠金额
        shopCartOrderMergerVO.setOrderReduce(ecPriceInfo.getDiscounted_price());
        //订单运费
        shopCartOrderMergerVO.setTotalTransfee(ecPriceInfo.getFreight());
        //订单行记录
        shopCartOrderMergerVO.setShopCartOrders(ecBuildOrderItems(ecOrderResponse,shopCartOrderMergerVO));

        return shopCartOrderMergerVO;
    }

    private List<ShopCartOrderVO> ecBuildOrderItems(EcOrderResponse ecOrderResponse,ShopCartOrderMergerVO shopCartOrderMergerVO){
        // 所有店铺的订单信息
        List<ShopCartOrderVO> shopCartOrders = new ArrayList<>();
        ShopCartOrderVO shopCartOrder = new ShopCartOrderVO();
        //订单类型 普通订单
        shopCartOrder.setOrderType(OrderType.ORDINARY.value());
        shopCartOrder.setShopId(Constant.MAIN_SHOP);
        //店铺优惠金额 = 优惠金额
        shopCartOrder.setShopReduce(0l);
        // 店铺的实付 = 购物车实付 + 运费
//        shopCartOrder.setActualTotal(product_info.getReal_price());
        // 运费
        shopCartOrder.setTransfee(0l);
        //用户订单备注
        shopCartOrder.setRemarks(ecOrderResponse.getOrder().getOrder_detail().getExt_info().getCustomer_notes());

        shopCartOrder.setTotal(shopCartOrderMergerVO.getTotal());
        shopCartOrder.setActualTotal(shopCartOrderMergerVO.getActualTotal());

        shopCartOrders.add(shopCartOrder);

        Integer totalCount = 0;

        List<ShopCartItemDiscountVO> shopCartItemDiscounts = new ArrayList<>();
        ShopCartItemDiscountVO shopCartItemDiscountVO = new ShopCartItemDiscountVO();
        List<ShopCartItemVO> shopCartItems = new ArrayList<>();
        for (EcProductInfo product_info : ecOrderResponse.getOrder().getOrder_detail().getProduct_infos()) {
            Long spuId = product_info.getOut_product_id();
            if(spuId==null){
                Assert.faild("外部订单编号为空，商品数据错误。");
            }

        }

        shopCartItemDiscountVO.setShopCartItems(shopCartItems);
        shopCartItemDiscounts.add(shopCartItemDiscountVO);
        shopCartOrder.setShopCartItemDiscounts(shopCartItemDiscounts);
        shopCartOrder.setTotalCount(totalCount);
        shopCartOrder.setTransfee(shopCartOrderMergerVO.getTotalTransfee());
        EcPriceInfo price_info = ecOrderResponse.getOrder().getOrder_detail().getPrice_info();
        shopCartOrder.setCouponReduce(Objects.nonNull(price_info.getIs_discounted()) && price_info.getIs_discounted() ? price_info.getDiscounted_price() : 0L);
        return shopCartOrders;
    }

    /**
     * 构建收获地址信息
     */
    public UserAddrVO ecBuildUserAddrVO(EcDeliveryInfo ecDeliveryInfo){
        EcAddressInfo ecAddressInfo = ecDeliveryInfo.getAddress_info();

        UserAddrVO userAddrVO = new UserAddrVO();
        userAddrVO.setProvince(ecAddressInfo.getProvince_name());
        userAddrVO.setCity(ecAddressInfo.getCity_name());
        userAddrVO.setArea(ecAddressInfo.getCounty_name());
        userAddrVO.setAddr(ecAddressInfo.getDetail_info());
        userAddrVO.setConsignee(ecAddressInfo.getUser_name());
        userAddrVO.setMobile(ecAddressInfo.getTel_number());
        return userAddrVO;
    }

}
