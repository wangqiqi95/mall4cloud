package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 客户群表DTO
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Data
public class CustGroupDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("群名")
    private String groupName;

    @ApiModelProperty("群主名称")
    private String ownerName;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("建群时间开始")
    private String createTimeStart;

	@ApiModelProperty("建群时间截止")
	private String createTimeEnd;

    @ApiModelProperty("群主的店")
    private List<Long> storeIds;

    @ApiModelProperty("群标签id组")
    private List<Long> tags;

    @ApiModelProperty("排序字段")
    private String orderBy ="createTime";

    @ApiModelProperty("升序还是降序")
    private String orders = "desc";
    /**
     * 客户群跟进状态。
     * 0 - 跟进人正常
     * 1 - 跟进人离职
     * 2 - 离职继承中
     * 3 - 离职继承完成
     */
    private Integer adminStatus;
}
