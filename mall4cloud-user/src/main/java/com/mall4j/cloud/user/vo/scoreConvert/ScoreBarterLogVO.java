package com.mall4j.cloud.user.vo.scoreConvert;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分换物兑换记录表
 *
 * @author shijing
 * @date 2021-12-10 17:18:04
 */

@Data
@ApiModel(value = "积分换物兑换记录")
public class ScoreBarterLogVO extends BaseModel implements Serializable {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

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
     * 发货方式（0：邮寄/1：门店自取）
     */
    @ApiModelProperty(value = "发货方式（0：邮寄/1：门店自取）")
    private Short deliveryType;

    /**
     * 兑换地址
     */
    @ApiModelProperty(value = "兑换地址")
    private String convertAddress;

    /**
     * 物流编码
     */
    @ApiModelProperty(value = "物流编码")
    private String courierCode;


    /**
     * 自提门店ID
     */
    @ApiModelProperty(value = "自提门店ID")
    private Long shopId;


    /**
     * 自提门店名称
     */
    @ApiModelProperty(value = "自提门店名称")
    private String shopName;

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

}
