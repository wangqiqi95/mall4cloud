package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信菜单表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@Data
public class WeixinMenuUpdateOrderDto{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private String id;

    @ApiModelProperty("菜单位置")
    private Integer orders;

}
