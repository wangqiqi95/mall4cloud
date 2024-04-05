package com.mall4j.cloud.api.docking.skq_crm.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 同步积分兑换dto
 * @Author axin
 * @Date 2023-02-08 10:56
 **/
@Data
public class SyncPointConvertDataDto implements Serializable {
    @ApiModelProperty(value = "兑礼单号",required = true)
    private Long logId;

    @ApiModelProperty(value = "兑换活动ID",required = true)
    private Long convertId;

    @ApiModelProperty(value = "积分换券活动类型 0：兑礼到店 1：积分换券 2：兑礼到家",required = true)
    private Integer type;

    @ApiModelProperty(value = "用户ID",required = true)
    private Long userId;

    @ApiModelProperty(value = "用户卡号",required = true)
    private String userCardNumber;

    @ApiModelProperty(value = "用户手机号",required = true)
    private String userPhone;

    @ApiModelProperty(value = "兑换积分数",required = true)
    private Integer convertScore;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建人id",required = true)
    private Long createUserId;

    @ApiModelProperty(value = "兑换时间",required = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ApiModelProperty(value = "原始兑换积分数",required = true)
    private Integer originalConvertScore;

    @ApiModelProperty(value = "折扣积分兑换数")
    private Integer discountConvertScore;

    @ApiModelProperty(value = "积分活动ID")
    private Long scoreActivityId;

    @ApiModelProperty(value = "积分活动来源")
    private String scoreActivitySrc;

    @ApiModelProperty(value = "用户收货地址")
    private String userAddr;

    @ApiModelProperty(value = "收件人")
    private String userName;

    @ApiModelProperty(value = "收件人手机")
    private String phone;

    @ApiModelProperty(value = "用户实物收货状态 0：默认未发货待领取 1：待发放 2：已发放",required = true)
    private Integer status;

    @ApiModelProperty(value = "物流单号")
    private String logisticsNo;

    @ApiModelProperty(value = "物流公司")
    private String company;

    @ApiModelProperty(value = "导入状态")
    private Integer exportStatus;

    @ApiModelProperty(value = "会员访问门店名称")
    private String visitStoreName;

    @ApiModelProperty(value = "兑礼活动名称")
    private String convertName;

    @ApiModelProperty(value = "小程序优惠券项目ID")
    private Long externalCouponId;

    @ApiModelProperty(value = "小程序优惠券项目名称")
    private String externalCouponName;

    @ApiModelProperty(value = "CRM券ID")
    private Long crmCouponId;

    @ApiModelProperty(value = "CRM券项目名称")
    private String crmCouponName;

    @ApiModelProperty(value = "券码")
    private String instanceId;

    @ApiModelProperty(value = "兑换数量",required = true)
    private Integer quantity;

    @ApiModelProperty(value = "会员访问门店编号",required = true)
    private String visitStoreCode;

    @ApiModelProperty(value = "商品名称")
    private String productCode;


    /**
     * 下面的是预留字段，暂时未接入
     */
    @ApiModelProperty(value = "是否取消兑礼")
    private String ifCancelled;

    @ApiModelProperty(value = "取消兑礼时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    @ApiModelProperty(value = "兑礼数据回传导入时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date importTime;

    @ApiModelProperty(value = "礼品领取预约柜台编号")
    private String giftRsvnStoreCode;

    @ApiModelProperty(value = "礼品领取预约柜台编号")
    private String giftRsvnStoreName;

    @ApiModelProperty(value = "礼品领取预约时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date giftRsvnTime;
}
