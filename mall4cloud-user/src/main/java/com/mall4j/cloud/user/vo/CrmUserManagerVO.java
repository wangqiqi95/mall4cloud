package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.crm.response.UserTag;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CrmUserManagerVO {

    @ApiModelProperty("好友关联关系对象")
    private UserStaffCpRelation userStaffCpRelation;
    @ApiModelProperty("阶段名称")
    private String stageName;
    @ApiModelProperty("分组名称")
    private String groupName;
    @ApiModelProperty("分组id")
    private Long groupId;

    @ApiModelProperty("员工对象")
    private StaffVO staffVO;

    @ApiModelProperty("数云接口返回的会员标签")
    private List<UserTag> userTags;

    @ApiModelProperty("积分余额")
    private double point;

    @ApiModelProperty("发送消息总数/会话总数")
    private Integer sendSum;

//    @ApiModelProperty("当前会员添加的员工明细")
//    private List<UserStaffCpRelationVO> userStaffCpRelations;

    @ApiModelProperty("当前会员添加的员工总数")
    private Integer userStaffCpRelationCount;

//    @ApiModelProperty("当前会员添加的群聊明细")
//    private List<UserJoinCustGroupVO>  userJoinCustGroupVOs;

    @ApiModelProperty("当前会员添加的群聊总数")
    private Integer userJoinCustGroupCount;

}
