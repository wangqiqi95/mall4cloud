package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class SonTaskStaffPageVO {


    @ApiModelProperty("导购ID")
    private Long staffId;
    @ApiModelProperty("导购编号")
    private String staffNo;
    @ApiModelProperty("导购昵称")
    private String staffNickName;
    @ApiModelProperty("触达任务总数")
    private Integer pushIssueCount;
    @ApiModelProperty("触达任务完成数")
    private Integer pushIssueFinishCount;
   /* @ApiModelProperty("未加好友数")
    private Integer notAddFriendCount;
    @ApiModelProperty("是否完成触达")
    private Integer finalSend;
    @ApiModelProperty("是否完成触达")
    private String finalSendRemark;*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("触达完成时间")
    private LocalDateTime finishTime;

}
