package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 朋友圈 员工发送记录表VO
 *
 * @author FrozenWatermelon
 * @date 2023-11-03 14:22:45
 */
@Data
public class DistributionMomentsSendRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("朋友圈ID")
    private Long momentsId;

    @ApiModelProperty("员工id")
    private Long staffId;


    @ApiModelProperty("是否发送状态 0否 1已经发送")
    private Integer status;

    @ApiModelProperty("异步任务id，最大长度为64字节，24小时有效")
    private String qwJobId;

    @ApiModelProperty("企微朋友圈ID")
    private String qwMomentsId;

    @ApiModelProperty("企微发表状态 0:未发表 1：已发表")
    private Integer qwPublishStatus;

    @ApiModelProperty("企微朋友圈评论数量")
    private Integer qwCommentNum;

    @ApiModelProperty("企微朋友圈点赞数量")
    private Integer qwLikeNum;

    @ApiModelProperty("发送时间")
    private Date sendTime;

    @ApiModelProperty("员工名称")
    private String staffName;

    @ApiModelProperty("员工工号")
    private String staffCode;

//    @ApiModelProperty("员工所属部门")
//    private String storeName;

    @ApiModelProperty("员工所属部门")
    private List<StaffOrgVO> orgs;

    @ApiModelProperty("企微userid")
    private String qiweiUserId;
}
