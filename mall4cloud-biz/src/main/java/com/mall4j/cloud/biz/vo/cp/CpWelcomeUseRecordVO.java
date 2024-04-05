package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 欢迎语 使用记录VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
@Data
public class CpWelcomeUseRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("欢迎语id")
    private Long welId;

    @ApiModelProperty("导购编号")
    private Long staffId;

    @ApiModelProperty("导购名称")
    private String staffName;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("导购用户好友关联id")
    private Long userStaffCpRelationId;

    private String userUnionId;

    private Date createTime;
}
