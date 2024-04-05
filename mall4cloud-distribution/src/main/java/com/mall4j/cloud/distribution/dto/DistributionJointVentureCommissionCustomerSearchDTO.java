package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 联营分佣客户查询dto
 *
 * @author Zhang Fan
 * @date 2022/8/4 10:39
 */
@ApiModel("联营分佣客户查询dto")
@Data
public class DistributionJointVentureCommissionCustomerSearchDTO {

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("第三方审核状态")
    private Integer thirdPartyStatus;

    @ApiModelProperty("门店id列表")
    private List<Long> storeIdList;

}
