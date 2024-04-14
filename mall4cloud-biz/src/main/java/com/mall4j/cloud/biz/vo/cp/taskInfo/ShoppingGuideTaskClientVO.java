package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 导购任务客户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingGuideTaskClientVO {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("客户id")
    private String clientId;
    @ApiModelProperty("客户昵称")
    private String clientNickname;
    @ApiModelProperty("客户电话")
    private String clientPhone;
    @ApiModelProperty("客户备注")
    private String clientRemark;
    @ApiModelProperty("任务类型为加企微好友时特有字段。0未添加 1已添加 2添加失败")
    private Integer addStatus;
    @ApiModelProperty("状态 1完成 0未完成")
    private Integer status;
    @ApiModelProperty("结束时间")
    private Date endTime;
}
