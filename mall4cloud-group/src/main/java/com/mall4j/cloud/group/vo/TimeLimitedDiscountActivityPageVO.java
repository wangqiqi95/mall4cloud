package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页返回对象
 *
 * @luzhengxiang
 * @create 2022-03-10 5:14 PM
 **/
@Data
public class TimeLimitedDiscountActivityPageVO extends TimeLimitedDiscountActivityVO implements Serializable{

    @ApiModelProperty("商品数量")
    private Integer spuCount;
    @ApiModelProperty("状态名称")
    private String statusName;

}
