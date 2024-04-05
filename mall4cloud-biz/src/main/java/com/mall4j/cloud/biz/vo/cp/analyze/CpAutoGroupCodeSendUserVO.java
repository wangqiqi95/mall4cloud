package com.mall4j.cloud.biz.vo.cp.analyze;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VO
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Data
public class CpAutoGroupCodeSendUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String nickName;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("是否送达：0否/1是")
    private Integer sendStatus;

    @ApiModelProperty("是否入群：0否/1是")
    private Integer joinGroup;

    @ApiModelProperty("群聊名称")
    private String groupName;

    private String chatId;

    private Long groupId;

    @ApiModelProperty("加好友时间")
    private Date bindTime;

    @ApiModelProperty("入群时间")
    private Date joinGroupTime;

}
