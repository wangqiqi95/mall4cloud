package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 任务对象详情
 */
@Data
public class TaskInfoVO {
    @ApiModelProperty("任务id")
    private Long id;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户")
    private Integer taskType;
    @ApiModelProperty("分享方式 1企微单聊 2企微群发 3发朋友圈 4群发客户群。task_type=3时有效")
    private Integer shareType;
    @ApiModelProperty("任务客户类型 1全部客户 2指定标签 3导入客户")
    private Integer taskClientType;
    @ApiModelProperty("任务客户群类型 1全部客户群 2指定客户群")
    private Integer taskClientGroupType;
    @ApiModelProperty("分配数量")
    private Integer allocatedQuantity;
    @ApiModelProperty("执行方式 1导购执行 2系统执行")
    private Integer implementationType;
    @ApiModelProperty("话术")
    private String speechSkills;
    @ApiModelProperty("企业id")
    private String companyId;
    @ApiModelProperty("任务门店类型 1全部门店 2指定门店")
    private Integer taskStoreType;
    @ApiModelProperty("任务导购类型 1全部导购 2指定导购")
    private Integer taskShoppingGuideType;
    @ApiModelProperty("任务目标。部分任务不存在比例，该字段存储")
    private String taskTarget;
    @ApiModelProperty("任务目标比例")
    private Double taskTargetScale;
    @ApiModelProperty("任务频率 1单次任务 2周期任务")
    private Integer taskFrequency;
    @ApiModelProperty("回访结果提交 1是 0否")
    private Integer visitResultsType;

    @ApiModelProperty("任务开始时间")
    private Date taskStartTime;
    @ApiModelProperty("任务结束时间")
    private Date taskEndTime;
    @ApiModelProperty("任务提醒配置。以选择的场景为主，选择3个场景就有3条数据")
    private List<TaskRemindInfoVO> taskRemindInfos;

    @ApiModelProperty("任务客户标签。指定标签客户时存在")
    private List<String> clientTagIds;
    @ApiModelProperty("任务客户。导入客户时存在")
    private List<TaskClientInfoVO> taskClientInfos;
    @ApiModelProperty("任务客户群。全部客户群时无值，指定客户群时存在")
    private List<String> taskClientGroupIds;
    @ApiModelProperty("任务门店。全部门店时无值，指定门店时存在")
    private List<String> taskStoreIds;
    @ApiModelProperty("任务导购。全部导购时无值，指定导购时存在")
    private List<String> shoppingGuideIds;
    @ApiModelProperty("提醒员工时选择了指定员工，该值存在")
    private List<String> remindShoppingGuideIds;
    @ApiModelProperty("素材信息")
    private List<TaskMaterialInfoVO> taskMaterialInfo;
}
