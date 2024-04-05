package com.mall4j.cloud.biz.dto.cp.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户浏览素材参数对象
 */
@Data
public class MeterialBrowseDTO {
    @ApiModelProperty("唯一id 传相同id视为增加浏览时长，否则为新的一次浏览记录")
    private String browseId;

    @ApiModelProperty("unionid")
    private String unionid;

    @ApiModelProperty("meterialId")
    private Long meterialId;

    @ApiModelProperty("员工id")
    private Long staffId;

}
