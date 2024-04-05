package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 话术 使用记录VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 10:25:19
 */
@Data
public class CpChatScriptUseRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("话术id")
    private Long scriptId;

    @ApiModelProperty("导购编号")
    private Long staffId;

    @ApiModelProperty("导购名称")
    private String staffName;

}
