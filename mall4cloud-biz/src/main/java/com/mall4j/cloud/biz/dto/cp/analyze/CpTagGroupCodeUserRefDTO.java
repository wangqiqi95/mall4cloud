package com.mall4j.cloud.biz.dto.cp.analyze;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 自动拉群活码表DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpTagGroupCodeUserRefDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否送达：0未执行/1已完成")
    private Integer status;

    @ApiModelProperty("是否入群：0否/1是")
    private Integer joinGroup;

    @ApiModelProperty(value = "标签筛选",required = false)
    private List<String> tagIds;

    @ApiModelProperty("标签建群任务id")
    private Long taskId;

    /**
     * TaskUserSendStatusEnum
     */
    @ApiModelProperty("触达状态")
    private List<Integer> cpSendStatus;

    @ApiModelProperty("关键字搜素")
    private String keyWord;

}
