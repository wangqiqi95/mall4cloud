package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodeIdDTO  {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ids")
    private Long[] ids;

}
