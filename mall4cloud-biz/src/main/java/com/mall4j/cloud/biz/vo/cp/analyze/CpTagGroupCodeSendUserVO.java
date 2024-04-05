package com.mall4j.cloud.biz.vo.cp.analyze;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * VO
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Data
public class CpTagGroupCodeSendUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("好友姓名")
    private String userName;

    private String nickName;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("执行状态：0未执行/1已完成/3解除好友关系")
    private Integer status;

    @ApiModelProperty(value = "0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;

    @ApiModelProperty("是否入群：0否/1是")
    private Integer joinGroup;

    @ApiModelProperty("群聊名称")
    private String groupName;

    @ApiModelProperty("客户企微userid")
    private String qiWeiUserId;

    @ApiModelProperty("客户手机号")
    private String phone;

    @ApiModelProperty("企微头像")
    private String avatar;

}
