package com.mall4j.cloud.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryChuDaRenQunDetailDTO {

    @NotNull(message = "开始时间为必传项")
    @ApiModelProperty(value = "子任务id")
    private Long sonTaskId;

    @ApiModelProperty(value = "搜索文案")
    private String searchKey;

    @ApiModelProperty(value = "标签id集合")
    private List<String> tagIdList;

    @ApiModelProperty(value = "0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private List<Integer> sendStatus;
}
