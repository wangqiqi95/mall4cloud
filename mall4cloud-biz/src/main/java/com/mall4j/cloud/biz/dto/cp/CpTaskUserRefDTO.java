package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 群发任务客户关联表DTO
 *
 * @author gmq
 * @date 2023-10-30 14:23:04
 */
@Data
public class CpTaskUserRefDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工id",required = true)
    private Long staffId;

    @ApiModelProperty(value = "员工企微id",required = true)
    private String saffQiWeiUserId;

    @ApiModelProperty(value = "客户id",required = true)
    private Long userId;

    @ApiModelProperty(value = "客户企微id",required = true)
    private String qiWeiUserId;

    @ApiModelProperty("客户姓名")
    private String userName;

    @ApiModelProperty("员工姓名")
    private String staffName;

}
