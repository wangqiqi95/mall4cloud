package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VO
 *
 */
@Data
public class CpStaffCodeLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;


    @ApiModelProperty("员工工号")
    private String staffNo;

    @ApiModelProperty("失败原因")
    private String logs;

    @ApiModelProperty("微信返回失败日志")
    private String wxerror;

    @ApiModelProperty("创建人")
    private String createBy;

}
