package com.mall4j.cloud.user.vo.scoreConvert;

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
@ApiModel(value = "积分换券兑换记录(小程序)")
public class UserCouponLogVO implements Serializable {

    @ApiModelProperty(value = "兑换记录id")
    private Long id;

    @ApiModelProperty(value = "积分活动id")
    private Long convertId;

    @ApiModelProperty(value = "标题")
    private String convertTitle;

    @ApiModelProperty(value = "活动封面")
    private String convertUrl;

    @ApiModelProperty(value = "兑换积分数")
    private Long convertScore;

    @ApiModelProperty(value = "兑换时间")
    private Date createTime;

    @ApiModelProperty(value = "优惠券id")
    private List<Long> couponIds;

    @ApiModelProperty("商品名称")
    private String commodityName;

    /**
     * 礼品描述
     */
    @ApiModelProperty("礼品描述")
    private String commodityDes;
    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券/2:兑礼到家）
     */
    @ApiModelProperty("积分换券活动类型（0：积分兑礼/1：积分换券/2:兑礼到家）")
    private Short type;

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
}
