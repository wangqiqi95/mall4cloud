package com.mall4j.cloud.biz.dto.cp.analyze;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动拉群活码表DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpTagGroupCodeAnalyzeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活码名称",required = false)
    private String nickName;

    @ApiModelProperty("是否送达：0未执行/1已完成")
    private Integer status;

    @ApiModelProperty("是否入群：0否/1是")
    private Integer joinGroup;

    @ApiModelProperty(value = "分组id",required = false)
    private List<Long> groupIds;

    @ApiModelProperty(value = "接待人员",required = false)
    private List<Long> staffs=new ArrayList<>();

    @ApiModelProperty(value = "标签筛选",required = false)
    private List<String> tagIds;

    @ApiModelProperty("活码id")
    private Long codeId;

    @ApiModelProperty("标签建群任务id")
    private Long taskId;

    @ApiModelProperty(value = "客户昵称",required = false)
    private String userName;

    @ApiModelProperty(value = "客户id",required = false)
    private List<String> userIds;

    @ApiModelProperty(value = "客户企微id",required = false)
    private List<String> userQiWeiUserIds;

    private String staffName;

    private Integer sendStatus;

    /**
     * TaskUserSendStatusEnum
     */
    @ApiModelProperty("触达状态")
    private List<Integer> cpSendStatus;

    private String groupName;

    @ApiModelProperty("关键字搜素")
    private String keyWord;

}
