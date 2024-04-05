package com.mall4j.cloud.api.user.dto;
import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserStaffCpRelationSearchDTO  extends PageDTO  implements Serializable {

    @ApiModelProperty("unionId")
    private String unionId;
    @ApiModelProperty("客户姓名/昵称")
    private String userName;
    @ApiModelProperty("客户手机号")
    private String userPhone;
    @ApiModelProperty("客户等级类型")
    private Integer userLevel;
    @ApiModelProperty("服务关系 1-当前员工 2-非当前员工")
    private Integer serviceRelation;
    @ApiModelProperty("导购staffId")
    private Long staffId;
    @ApiModelProperty("绑定关系: 1-绑定 2-解绑中 3-删除客户")
    private Integer status;

    @ApiModelProperty("客户id")
    private Long relationId;

    @ApiModelProperty("导购staffId")
    private List<Long> staffIds;

    private Integer contactChangeType;

    private List<Integer> contactChangeTypes;

    @ApiModelProperty("渠道标识id")
    private Long codeChannelId;

    private List<String> qiWeiUserIds;

    @ApiModelProperty("阶段id")
    private Long stageId;
    @ApiModelProperty("阶段ids")
    private List<Long> stageIds;
    @ApiModelProperty("员工名称")
    private String staffName;

    @ApiModelProperty("未设置阶段or分组 传值1表示查询未设置阶段的会员列表")
    private Integer unSetStage;

    @ApiModelProperty("数云标签id")
    private List<String> tagId;
    @ApiModelProperty(value = "unionIds",hidden = true)
    private List<String> unionIds;

    @ApiModelProperty("筛选添加开始时间")
    private String cpCreateTimeStart;
    @ApiModelProperty("筛选添加结束时间")
    private String cpCreateTimeEnd;


    @ApiModelProperty("客户备注")
    private String cpRemark;

    @ApiModelProperty("")
    private String qiWeiNickName;

    @ApiModelProperty("搜索客户名称关键字")
    private String nameKeyWrod;

    @ApiModelProperty("是否去重：0否/1是")
    private Integer duplicateType;


    public void initPage(){
        this.setPageNum(1);
        this.setPageSize(10);
    }
}
