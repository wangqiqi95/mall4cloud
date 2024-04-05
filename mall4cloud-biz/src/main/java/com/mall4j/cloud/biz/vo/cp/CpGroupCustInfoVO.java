package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客群客户的关联关系表VO
 *
 * @author gmq
 * @date 2023-11-10 11:39:44
 */
@Data
public class CpGroupCustInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("客群id")
    private String groupId;

    @ApiModelProperty("活码id")
    private Long codeId;

    @ApiModelProperty("客户id")
    private String userId;

    @ApiModelProperty("好友关系表id")
    private String cpUserRelId;

    @ApiModelProperty("1 - 企业成员,2 - 外部联系人")
    private Integer type;
    private String typeName;

    @ApiModelProperty("1 - 由群成员邀请入群（直接邀请入群）2 - 由群成员邀请入群（通过邀请链接入群）3 - 通过扫描群二维码入群")
    private Integer joinScene;
    private String joinSceneName;

    @ApiModelProperty("退群方式: 0 - 自己退群 / 1 - 群主/群管理员移出")
    private Integer quitScene;

    @ApiModelProperty("邀请者的userid")
    private String invitorUserId;
	@ApiModelProperty("邀请者的姓名")
	private String invitorUserName;

    @ApiModelProperty("在群里的昵称")
    private String groupNickname;

    @ApiModelProperty("客户昵称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("入群时间")
    private Date joinTime;

    @ApiModelProperty("邀请送达状态：0未送达/1送达")
    private Integer sendStatus;

    @ApiModelProperty("是否入群: 0否/1是")
    private Integer joinStatus;

    @ApiModelProperty("是否管理员：0否/1是")
    private Integer isAdmin;
    private String isAdminStr;

    @ApiModelProperty("手机号")
    private String phone;

}
