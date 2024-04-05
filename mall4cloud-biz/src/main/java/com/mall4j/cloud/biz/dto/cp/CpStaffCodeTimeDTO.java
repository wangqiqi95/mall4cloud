package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 渠道活码人员时间段DTO
 *
 * @author gmq
 * @date 2023-10-25 16:39:38
 */
@Data
public class CpStaffCodeTimeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("时间段：周期【1,2,3,4,5,6,7】")
    private String timeCycle;

    @ApiModelProperty("时间段：开始时间")
    private String timeStart;

    @ApiModelProperty("时间段：结束时间")
    private String timeEnd;

    /**
     * 源：0欢迎语/1渠道活码
     */
    private Integer sourceFrom;

	@ApiModelProperty("关联员工信息")
	@NotEmpty(message = "关联员工列表不能为空")
	private List<StaffCodeRefDTO> staffList;

}
