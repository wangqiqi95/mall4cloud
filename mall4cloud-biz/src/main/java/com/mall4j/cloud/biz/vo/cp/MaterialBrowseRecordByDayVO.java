package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialBrowseRecordByDayVO {

    @ApiModelProperty("日期")
    private String day1;
    @ApiModelProperty("数量")
    private String num;

}
