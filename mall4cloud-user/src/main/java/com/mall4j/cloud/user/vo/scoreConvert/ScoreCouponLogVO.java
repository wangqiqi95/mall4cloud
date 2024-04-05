package com.mall4j.cloud.user.vo.scoreConvert;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 积分换券兑换记录
 *
 * @author shijing
 */

@Data
@ApiModel(value = "积分换券兑换记录")
public class ScoreCouponLogVO extends BaseModel implements Serializable {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "积分活动id")
    private Long convertId;

    @ApiModelProperty(value = "积分活动名称")
    private String convertTitle;

    /**
     * 用户卡号
     */
    @ApiModelProperty(value = "用户卡号")
    private String userCardNumber;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    /**
     * 兑换积分数
     */
    @ApiModelProperty(value = "兑换积分数")
    private Long convertScore;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 兑换时间
     */
    @ApiModelProperty(value = "兑换时间")
    private Date createTime;

    @ApiModelProperty("中奖状态，0：待领取，1：待发放，2：已发放")
    private Integer status;
    @ApiModelProperty("用户收货地址")
    private String userAddr;
    @ApiModelProperty("用户手机号码")
    private String phone;
    @ApiModelProperty("发货物流单号")
    private String logisticsNo;
    @ApiModelProperty("发货物流公司")
    private String company;
    @ApiModelProperty("收件人姓名")
    private String userName;
    @ApiModelProperty("兑礼门店")
    private String storeName;
    @ApiModelProperty("导入状态，0：待确认，1：已确认")
    private Integer exportStatus;
    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券）
     */
    @ApiModelProperty("积分换券活动类型（0：积分兑礼/1：积分换券/2:兑礼到家）")
    private Short type;
    @ApiModelProperty(value = "优惠券id")
    private List<Long> couponIds;

    @ApiModelProperty("商品名称")
    private String commodityName;

}
