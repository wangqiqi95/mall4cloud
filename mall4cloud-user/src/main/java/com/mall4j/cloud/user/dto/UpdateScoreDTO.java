package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 修改用户积分
 *
 * @author shijing
 * @date 2022-03-30 18:18:04
 */
@Data
@ApiModel("修改用户积分")
public class UpdateScoreDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private List<Long> userIds;

    @ApiModelProperty("积分")
    private Integer score;

}
