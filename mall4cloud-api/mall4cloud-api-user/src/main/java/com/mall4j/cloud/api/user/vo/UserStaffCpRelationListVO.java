package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserStaffCpRelationListVO {

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("会员Id")
    private Long userId;

    @ApiModelProperty("会员unionid")
    private String userUnionId;

    @ApiModelProperty("会员企微id")
    private String qiWeiUserId;

    @ApiModelProperty("员工id")
    private Long staffId;

    private String staffName;

    @ApiModelProperty("员工企微id")
    private String qiWeiStaffId;

    @ApiModelProperty("绑定关系: 1-绑定 2-解绑中 3-删除客户")
    private Integer status;

    @ApiModelProperty("")
    private String qiWeiNickName;

    @ApiModelProperty("渠道标识id")
    private String codeChannelId;

    @ApiModelProperty("客户与员工执行事件(企微事件类型)")
    private String contactChange;

    @ApiModelProperty("")
    private Integer contactChangeType;

    @ApiModelProperty("企微头像")
    private String avatar;

    @ApiModelProperty("外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户")
    private Integer type;

    @ApiModelProperty("0自动通过/1手动通过")
    private Integer autoType;

    @ApiModelProperty("该成员对此外部联系人的备注")
    private String cpRemark;

    @ApiModelProperty("该成员添加此外部联系人的时间")
    private Date cpCreateTime;

    @ApiModelProperty("该成员对此客户备注的手机号码")
    private String cpRemarkMobiles;

    @ApiModelProperty("外部联系人所在企业的简称")
    private String corpName;

    @ApiModelProperty("外部联系人所在企业的主体名称")
    private String corpFullName;

    @ApiModelProperty("成员对客户备注的企业名称")
    private String cpRemarkCorpName;

    //https://developer.work.weixin.qq.com/document/path/92114#%E6%9D%A5%E6%BA%90%E5%AE%9A%E4%B9%89
    @ApiModelProperty("成员添加此客户的来源")
    private String cpAddWay;

    @ApiModelProperty("企微渠道标识")
    private String cpState;

    @ApiModelProperty("阶段id")
    private Long stageId;

    @ApiModelProperty("阶段Name")
    private String groupName;

    @ApiModelProperty("阶段父类分组Name")
    private String parentGroupName;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("服务关系 1-当前员工 2-非当前员工")
    private Integer serviceRelation;
    @ApiModelProperty("服务导购id")
    private Long serviceStaffId;
    @ApiModelProperty("服务导购名称")
    private String serviceStaffName;
    @ApiModelProperty("服务导购企业微信id")
    private String serviceQiWeiStaffId;
//    @ApiModelProperty("服务导购商店Id")
//    private Long serviceStoreId;
//    @ApiModelProperty("服务导购商店名称")
//    private String serviceStoreName;

    @ApiModelProperty("标签ID")
    private String tagId;
    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("该成员对此外部联系人的描述")
    private String cpDescription;

}
