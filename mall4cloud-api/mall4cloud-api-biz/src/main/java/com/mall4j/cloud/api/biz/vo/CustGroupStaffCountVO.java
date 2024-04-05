package com.mall4j.cloud.api.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 客户群表VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustGroupStaffCountVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("新增客户群")
    private Integer newGroupCount;

    @ApiModelProperty("客户总群")
    private Integer groupCount;

    @ApiModelProperty("新增入群人数")
    private Integer newGroupUserCount;


}
