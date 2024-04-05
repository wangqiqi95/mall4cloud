package com.mall4j.cloud.user.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ChangeStaffDTO {

    @ApiModelProperty("用户id集合")
    private List<Long> userId;
    @NotNull(message = "导购id不能为空")
    @ApiModelProperty("导购id")
    private Long staffId;
}
