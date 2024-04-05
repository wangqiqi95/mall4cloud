package com.mall4j.cloud.biz.dto.cp.analyze;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动拉群活码表DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpAutoGroupCodeAnalyzeDTO {
    private static final long serialVersionUID = 1L;

    private String nickName;

    @ApiModelProperty("是否送达：0否/1是")
    private Integer sendStatus;

    @ApiModelProperty("是否入群：0否/1是")
    private Integer joinGroup;

    @ApiModelProperty(value = "分组id",required = false)
    private List<Long> groupIds;

    @ApiModelProperty(value = "系统群活码id集合",required = false)
    private List<Long> groupCodeIds;

    @ApiModelProperty(value = "客群id集合",required = false)
    private List<String> chatIds;

    @ApiModelProperty(value = "接待人员",required = false)
    private List<Long> staffs=new ArrayList<>();

    private List<String> userIds;

    private List<String> userQiWeiUserIds;

    @ApiModelProperty("活码id")
    private Long codeId;

    private String staffName;

    private String groupName;
}
