package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class CustDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("客户名称")
    private String custName;

    @ApiModelProperty("客户企业微信Id")
    private String userId;

    @ApiModelProperty("客户手机号码")
    private String mobile;

    @ApiModelProperty("客户等级")
    private String level;
    @ApiModelProperty("群人数")
    private Integer totalCust ;

}
