package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 联营分佣门店dto
 *
 * @author Zhang Fan
 * @date 2022/8/4 14:28
 */
@ApiModel("联营分佣门店dto")
@Data
public class DistributionJointVentureCommissionStoreDTO {

    @ApiModelProperty("联营分佣ID")
    @NotNull(message = "联营分佣ID不能为空")
    private Long jointVentureId;

    @ApiModelProperty("门店ID集合")
    private List<Long> storeIdList;
}
