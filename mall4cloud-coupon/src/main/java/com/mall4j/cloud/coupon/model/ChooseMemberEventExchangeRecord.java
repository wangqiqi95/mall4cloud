package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ben
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_choose_member_event_exchange_record")
@ApiModel(value="ChooseMemberEventExchangeRecord对象", description="")
public class ChooseMemberEventExchangeRecord extends Model<ChooseMemberEventExchangeRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "兑换记录ID")
    @TableId(value = "exchange_record_id", type = IdType.AUTO)
    private Long exchangeRecordId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "活动ID（t_choose_member_event表ID）")
    private Long eventId;

    @ApiModelProperty(value = "所属店铺ID")
    private Long belongShopId;

    @ApiModelProperty(value = "所属商铺名称")
    private String belongShopName;

    @ApiModelProperty(value = "所属商铺编码")
    private String belongShopCode;

    @ApiModelProperty(value = "兑换个数")
    private Integer exchangeNum;

    @ApiModelProperty(value = "收货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "收件人")
    private String consignee;

    @ApiModelProperty(value = "用户手机号码")
    private String mobile;

    @ApiModelProperty(value = "发货状态：0未发货，1待发放，2已发放")
    private Integer deliveryStatus;

    @ApiModelProperty(value = "物流公司")
    private String logisticsCompany;

    @ApiModelProperty(value = "查询单号")
    private String trackingNumber;

    @ApiModelProperty(value = "收货人手机号码")
    private String deliveryMobile;

    @ApiModelProperty(value = "导入状态：0未确认，1确认")
    private Integer exportStatus;

    @ApiModelProperty(value = "会员卡号")
    private String userVipCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.exchangeRecordId;
    }

}
