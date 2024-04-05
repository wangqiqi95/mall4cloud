package com.mall4j.cloud.platform.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lhd
 * @date 2020/9/8
 */
@Data
public class SysUserPageDTO extends PageDTO {

    @ApiModelProperty("用户姓名")
    private String nickName;

    @ApiModelProperty("联系方式")
    private String phoneNum;

    @ApiModelProperty("状态：0-禁用/1-启用")
    private Integer status;

    private List<Long> orgIds;

    private String path;

}
