package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-16 15:28
 * @Version: 1.0
 */
@Data
public class GroupCodePageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("创建人名称")
    private String createName;

    @ApiModelProperty("创建人店id数组")
    private List<Long> storeIds;

    @ApiModelProperty("ids")
    private List<Long> ids;

    @ApiModelProperty(value = "分组id",required = false)
    private List<Long> groupIds;

    private String createTimeStart;
    private String createTimeEnd;
}
