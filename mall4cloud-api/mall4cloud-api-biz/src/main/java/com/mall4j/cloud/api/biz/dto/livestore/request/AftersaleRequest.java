
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AftersaleRequest {

    @JsonProperty("create_time")
    private String createTime;

    /**
     * 0:订单可继续售后, 1:订单无继续售后
     */
    @JsonProperty("finish_all_aftersale")
    private Long finishAllAftersale;
    /**
     * 用户的openid
     */
    private String openid;
    /**
     * 商家自定义售后ID
     */
    @JsonProperty("out_aftersale_id")
    private String outAftersaleId;
    /**
     * 商家自定义订单ID
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    /**
     * 商家小程序该售后单的页面path，不存在则使用订单path
     */
    private String path;
    /**
     * 退货相关商品列表
     */
    @JsonProperty("product_infos")
    private List<ProductInfo> productInfos;
    /**
     * 退款金额,单位：分
     */
    private Long refund;
    /**
     * 0:未受理,1:用户取消,2:商家受理中,3:商家逾期未处理,4:商家拒绝退款,5:商家拒绝退货退款,
     * 6:待买家退货,7:退货退款关闭,8:待商家收货,11:商家退款中,12:商家逾期未退款,13:退款完成,
     * 14:退货退款完成,15:换货完成,16:待商家发货,17:待用户确认收货,18:商家拒绝换货,19:商家已收到货
     */
    private Long status;
    /**
     * 售后类型，1:退款,2:退款退货,3:换货
     */
    private Long type;

}
