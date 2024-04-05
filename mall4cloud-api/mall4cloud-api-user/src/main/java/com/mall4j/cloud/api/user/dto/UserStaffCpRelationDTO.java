package com.mall4j.cloud.api.user.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserStaffCpRelationDTO {

    @ApiModelProperty("会员unionid")
    private String userUnionId;

    @ApiModelProperty("会员企微id")
    private String qiWeiUserId;

    @ApiModelProperty("员工企微id")
    private String qiWeiStaffId;

    @ApiModelProperty("绑定关系: 1-绑定 2-解绑中 3-删除客户")
    private Integer status;

    @ApiModelProperty("变更员工企微id")
    private String changeQiWeiStaffId;

    @ApiModelProperty("企业微信昵称")
    private String qiWeiNickName;

    @ApiModelProperty("渠道标识id")
    private String codeChannelId;

    @ApiModelProperty("客户与员工执行事件(企微事件类型)")
    private String contactChangeType;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("外部联系人的类型: 1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户")
    private Integer type;

    @ApiModelProperty("0自动通过/1手动通过")
    private Integer autoType;

    @ApiModelProperty("该成员对此外部联系人的备注")
    private String cpRemark;

    @ApiModelProperty("该成员对此外部联系人的描述")
    private String cpDescription;

    @ApiModelProperty("该成员添加此外部联系人的时间")
    private Date cpCreateTime;

    @ApiModelProperty("该成员对此客户备注的手机号码")
    private String cpRemarkMobiles;

    //https://developer.work.weixin.qq.com/document/path/92114#%E6%9D%A5%E6%BA%90%E5%AE%9A%E4%B9%89
    @ApiModelProperty("该成员添加此客户的来源，具体含义详见来源定义")
    private String cpAddWay;

    private String cpOperUserId;

    @ApiModelProperty("企微渠道标识")
    private String cpState;

    @ApiModelProperty("渠道源分组id")
    private Long codeGroupId;

    @ApiModelProperty("外部联系人性别 0-未知 1-男性 2-女性")
    private Integer gender;

    @ApiModelProperty("外部联系人所在企业的简称")
    private String corpName;

    @ApiModelProperty("外部联系人所在企业的主体名称")
    private String corpFullName;

    @ApiModelProperty("成员对客户备注的企业名称")
    private String cpRemarkCorpName;

}
