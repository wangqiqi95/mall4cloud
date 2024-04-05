package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 标签关联员工VO
 *
 * @author gmq
 * @date 2022-09-13 12:01:45
 */
@Data
public class TzTagStaffVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("标签id")
    private Long tagId;

    @ApiModelProperty("员工id")
    private Long staffId;

	@ApiModelProperty("员工姓名")
	private String staffName;

    @ApiModelProperty("员工编码")
    private String staffNo;

    @ApiModelProperty("员工手机号")
    private String mobile;

}
