package com.mall4j.cloud.user.vo.dictionary;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictionaryVO {

    @ApiModelProperty(value = "字典类型")
    private String dictionaryType;

    @ApiModelProperty(value = "字典描述")
    private String dictionaryDescription;

    @ApiModelProperty(value = "字典key")
    private String dictionaryKey;

    @ApiModelProperty(value = "字典值")
    private String dictionaryValue;

}
