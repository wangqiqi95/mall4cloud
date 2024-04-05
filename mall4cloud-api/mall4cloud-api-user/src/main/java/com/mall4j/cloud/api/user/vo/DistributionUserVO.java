package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2022/01/23
 */
@Data
public class DistributionUserVO {

    @ApiModelProperty("用户数量")
    private int userNum;

    @ApiModelProperty("用户ID集合")
    private List<Long> userIds;

}
