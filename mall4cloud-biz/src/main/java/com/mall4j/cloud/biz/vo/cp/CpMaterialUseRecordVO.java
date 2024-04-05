package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 素材 使用记录VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 10:03:14
 */
@Data
public class CpMaterialUseRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("导购编号")
    private Long staffId;

    @ApiModelProperty("导购名称")
    private String staffName;

}
