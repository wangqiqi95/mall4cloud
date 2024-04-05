package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VO
 *
 * @author lt
 * @date 2022-01-17
 */
@Data
@ApiModel("视频号物流")
public class LiveDeliveryVO extends BaseVO {

    @ApiModelProperty("快递公司ID.")
    private String deliveryId;

    @ApiModelProperty("微信快递公司ID.")
    private String wxDeliveryId;

    @ApiModelProperty("微信快递公司名称.")
    private String wxDeliveryName;

    @ApiModelProperty("快递公司联系方式")
    private String phone;
}
