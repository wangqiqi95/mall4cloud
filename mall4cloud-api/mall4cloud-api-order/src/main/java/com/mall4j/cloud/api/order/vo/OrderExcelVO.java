package com.mall4j.cloud.api.order.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单excel信息
 *
 * @author YXF
 * @date 2020-3-9
 */
@Data
public class OrderExcelVO {

	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "订单信息";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40};




    @ExcelProperty(value = {"订单信息", "序号"}, index = 0)
	private String seq;

    @ExcelProperty(value = {"订单信息", "订单ID"}, index = 1)
    private String orderNumber;

    @ExcelIgnore
	private Long orderId;

	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	@ExcelProperty(value = {"订单信息", "下单时间"}, index = 2)
	private Date createTime;
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	@ExcelProperty(value = {"订单信息", "支付时间"}, index = 3)
	private Date payTime;

	@ExcelProperty(value = {"订单信息", "配送类型"}, index = 4)
	private String deliveryType;

	@ExcelProperty(value = {"订单信息", "收货人姓名"}, index = 5)
	private String consignee;

//	@ExcelProperty(value = {"订单信息", "收货人手机"}, index = 6)
//	private String mobile;
//
//	@ExcelProperty(value = {"订单信息", "收货地址"}, index = 7)
//	private String receivingAddr;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"订单信息", "收货时间"}, index = 6)
    private Date finallyTime;

	@ExcelProperty(value = {"订单信息", "订单总额"}, index = 7)
	private String total;

	@ExcelProperty(value = {"订单信息", "运费金额"}, index = 8)
	private String freightAmount;

	@ExcelProperty(value = {"订单信息", "优惠总额"}, index = 9)
	private String reduceAmount;

	@ExcelProperty(value = {"订单信息", "使用积分"}, index = 10)
	private String orderScore;

	@ExcelProperty(value = {"订单信息", "实际支付"}, index = 11)
	private String actualTotal;

	@ExcelProperty(value = {"订单信息", "支付类型"}, index = 12)
	private String payType;

	@ExcelProperty(value = {"订单信息", "订单状态"}, index = 13)
	private String status;

	@ExcelProperty(value = {"订单信息", "售后状态"}, index = 14)
	private String refundStatus;



    @ExcelProperty(value = {"订单信息", "直播id"}, index = 15)
    private String livingId;
    @ExcelProperty(value = {"订单信息", "会员姓名"}, index = 16)
    private String userName;
//    @ExcelProperty(value = {"订单信息", "会员卡号"}, index = 19)
//    private String userNo;
//    @ExcelProperty(value = {"订单信息", "会员手机号"}, index = 20)
//    private String userPhone;
    @ExcelProperty(value = {"订单信息", "下单门店"}, index = 17)
    private String storeName;
//    @ExcelProperty(value = {"订单信息", "订单退款总金额"}, index = 21)
//    private BigDecimal totalRefundAmount;
	@ExcelIgnore
    private String totalRefundAmount;
    @ExcelProperty(value = {"订单信息", "微客分销员"}, index = 18)
    private String weike;
//	@ExcelProperty(value = {"订单信息", "微客分销员工号"}, index = 23)
//	private String weikeNo;
	@ExcelProperty(value = {"订单信息", "微客分销员手机号"}, index = 19)
	private String weikePhone;
    @ExcelProperty(value = {"订单信息", "导购分销员"}, index = 20)
    private String daogou;
	@ExcelProperty(value = {"订单信息", "导购分销员手机号"}, index = 21)
	private String daogouMobile;
	
	@ExcelProperty(value = {"订单信息", "导购工号"}, index = 22)
	private String daogouNo;

    @ExcelProperty(value = {"订单信息", "优惠券名称"}, index = 23)
    private String couponName;
    @ExcelProperty(value = {"订单信息", "优惠券id"}, index = 24)
    private String couponId;
	@ExcelProperty(value = {"订单信息", "优惠券code"}, index = 25)
	private String couponCode;
	@ExcelProperty(value = {"订单信息", "优惠券来源"}, index = 26)
	private String couponSourceType;
	@ExcelProperty(value = {"订单信息", "优惠券优惠金额"}, index = 27)
	private String couponReduceAmount;
    @ExcelProperty(value = {"订单信息", "活动类型"}, index = 28)
    private String activityType;
//	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//	@ExcelProperty(value = {"订单信息", "支付时间"}, index = 29)
//	private String payTime;
	@ExcelProperty(value = {"订单信息", "下单门店编码"}, index = 29)
	private String storeCode;
	@ExcelProperty(value = {"订单信息", "支付单id"}, index = 30)
	private Long payId;
	@ExcelProperty(value = {"订单信息", "微信支付单号"}, index = 31)
	private String bizPayNo;
	@ExcelProperty(value = {"订单信息", "订单来源"}, index = 32)
	private String orderSourceName;
	@ExcelProperty(value = {"订单信息", "推广员唯一ID"}, index = 33)
	private String promoterId;
	@ExcelProperty(value = {"订单信息", "推广员视频号昵称"}, index = 34)
	private String finderNickname;
	@ExcelProperty(value = {"订单信息", "分享员openid"}, index = 35)
	private String sharerOpenid;
	@ExcelProperty(value = {"订单信息", "分享员名称"}, index = 36)
	private String sharerName;
	@ExcelProperty(value = {"订单信息", "视频号小店名称"}, index = 37)
	private String channelsShopName;
	@ExcelProperty(value = {"订单信息", "微信测订单编号"}, index = 38)
	private String wechatOrderId;
	@ExcelProperty(value = {"订单信息", "买家备注"}, index = 39)
	private String remarks;
	@ExcelProperty(value = {"订单信息", "平台备注"}, index = 40)
	private String platformRemarks;

    @ExcelProperty(value = {"订单项信息", "商品货号"})
    private String spuCode;
	@ExcelProperty(value = {"订单项信息", "商品条码"})
	private String skuCode;
	@ExcelProperty(value = {"订单项信息", "商品名称"})
	private String spuName;

	@ExcelProperty(value = {"订单项信息", "sku名称"})
	private String skuName;

	@ExcelProperty(value = {"订单项信息", "产品价格"})
	private String price;

	@ExcelProperty(value = {"订单项信息", "吊牌价"})
	private String marketPrice;

	@ExcelProperty(value = {"订单项信息", "商品数量"})
	private Integer count;

	@ExcelProperty(value = {"订单项信息", "商品总金额"})
	private String spuTotalAmount;

	@ExcelProperty(value = {"订单项信息", "优惠金额"})
	private String shareReduce;

	@ExcelProperty(value = {"订单项信息", "实际金额"})
	private String actualTotalItem;

	@ExcelProperty(value = {"订单项信息", "使用积分"})
	private Long useScore;

	@ExcelProperty(value = {"订单项信息", "售后状态"})
	private String refundStatusItem;

	@ExcelProperty(value = {"订单项信息", "代客下单人"})
	private String valetOrderStaffName;

	@ExcelProperty(value = {"订单项信息", "代客下单人手机号"})
	private String valetOrderStaffPhone;

	@ExcelIgnore
	private List<OrderItemLangVO> orderItemLangList;

//    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//    @ExcelProperty(value = {"订单信息", "收货时间"}, index = 7)
    @ExcelIgnore
    private Date receivingTime;

    @ExcelIgnore
    private Long userId;

    @ExcelIgnore
    private Long storeId;

    @ExcelIgnore
    private Long distributionUserId;

    @ExcelIgnore
    private Integer distributionUserType;

    @ExcelIgnore
    private Integer orderSource;
    @ExcelIgnore
    private String sourceId;
    @ExcelIgnore
    private Long spuId;
    @ExcelIgnore
    private Integer orderType;
	@ExcelIgnore
	private Long skuId;
	@ExcelIgnore
	private String shopCouponAmount;
	@ExcelIgnore
	private String platformCouponAmount;
	@ExcelIgnore
	private Long buyStaffId;

}
