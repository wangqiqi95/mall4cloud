package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MarkingTagDTO {

    @Size(min = 1,message = "至少选择一个标签")
    @ApiModelProperty("标签列表")
    List<TagGroupRelationDTO> groupRelationDTOList;

    @NotBlank(message = "会员码为必传项")
    @ApiModelProperty("会员码")
    private String vipCode;
}
