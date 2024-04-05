package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Grady_Lu
 */
@Data
public class ScoreProductSubscriptRecordVO {

    @ApiModelProperty(value = "门店编码")
    private String storeCode;

    @ApiModelProperty(value = "消息订阅状态 0-未订阅，1-已订阅")
    private Integer messageStatus;

}
