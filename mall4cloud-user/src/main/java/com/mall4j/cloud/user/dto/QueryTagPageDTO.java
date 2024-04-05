package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryTagPageDTO extends PageDTO {

//    @ApiModelProperty("条数")
//    @NotNull(message = "条数为必传项")
//    private Long pageSize;
//
//    @ApiModelProperty("页码")
//    @NotNull(message = "页码为必传项")
//    private Long pageNum;

    @ApiModelProperty("标签组ID")
    @NotNull(message = "标签组ID为必传项")
    private Long groupId;

    @ApiModelProperty("标签名")
    private String tagName;

    /**
     * 接口入口是否为导购端
     */
    private boolean isStaff;

}
