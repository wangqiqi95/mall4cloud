package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcAfterSaleOrder {
    //after_sale_order_id
    private String after_sale_order_id;
    /**
     * AfterSaleStatus
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
     */
    private String status;
    //	买家身份标识
    private String openid;
    //买家在开放平台的唯一标识符，若当前视频号小店已绑定到微信开放平台帐号下会返回，详见UnionID 机制说明
    private String unionid;
    private String order_id;
    //售后相关商品信息
    private EcAfterSaleProductInfo product_info;

    private EcAfterSaleDetails details;
    //退款详情
    private EcRefundInfo refund_info;
    //用户退货信息
    private EcReturnInfo return_info;
    //商家上传的信息
    private EcMerchantUploadInfo merchant_upload_info;
    //售后单创建时间戳
    private Long create_time;
    //售后单更新时间戳
    private Long update_time;
    /**
     * AfterSaleReason
     * INCORRECT_SELECTION	拍错/多拍
     * NO_LONGER_WANT	不想要了
     * NO_EXPRESS_INFO	无快递信息
     * EMPTY_PACKAGE	包裹为空
     * REJECT_RECEIVE_PACKAGE	已拒签包裹
     * NOT_DELIVERED_TOO_LONG	快递长时间未送达
     * NOT_MATCH_PRODUCT_DESC	与商品描述不符
     * QUALITY_ISSUE	质量问题
     * SEND_WRONG_GOODS	卖家发错货
     * THREE_NO_PRODUCT	三无产品
     * FAKE_PRODUCT	假冒产品
     * OTHERS	其它
     */
    private String reason;
    //退款结果
    private EcRefundResp refund_resp;
    //售后类型。REFUND:退款；RETURN:退货退款。
    private String type;
    //纠纷id，该字段可用于获取纠纷信息
    private String complaint_id;
}
