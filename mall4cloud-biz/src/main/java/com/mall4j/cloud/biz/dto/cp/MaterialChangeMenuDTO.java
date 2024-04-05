package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MaterialChangeMenuDTO {

    @ApiModelProperty("素材id集合")
    private List<Long> matId;
    @ApiModelProperty("要修改的素材分类")
    private Integer matType;
}
