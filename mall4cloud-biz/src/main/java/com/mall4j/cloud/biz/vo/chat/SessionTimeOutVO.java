package com.mall4j.cloud.biz.vo.chat;

import com.mall4j.cloud.biz.model.chat.EndStatement;
import com.mall4j.cloud.biz.model.chat.WorkDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 视频号4.0地址管理VO
 */
@Data
public class SessionTimeOutVO {

	@ApiModelProperty("主键id")
	private Long id;

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("适用人员")
	private String suitPeople;
	@ApiModelProperty("场景类型")
	private String type;
	@ApiModelProperty("上班日期 ")
	//1：周一，2：周二，3：周三，4：周四，5：周五，6：周六，7：周日,多选用,分开
	private String workDate;
	@ApiModelProperty("上班开始时间")
	private String workStartDate;
	@ApiModelProperty("上班结束时间")
	private String workEndDate;
	@ApiModelProperty("超时时效")
	private String timeOut;
	@ApiModelProperty("结束语是否开启")
	//正常结束语是否开启0：关闭，1：开启
	private Integer normalEnd;
	@ApiModelProperty("正常结束语")
	private String conclusion;
	@ApiModelProperty("通知提醒开启")
	//通知提醒开启0：关闭，1：开启
	private Integer remind;
	@ApiModelProperty("提醒类型 1：当事人 2：提醒指定员工")
	private String remindPeople;
	@ApiModelProperty("提醒时间")
	//提醒时间0：立即上报，1：指定时间
	private Integer remindOpen;
	@ApiModelProperty("延迟上报时间")
	//延迟上报时间，以小时为单位
	private String remindTime;
	@ApiModelProperty(value = "工作时间集合")
	private List<WorkDate> workDateList;
	@ApiModelProperty(value = "结束语集合")
	private List<EndStatement> statementList;
	@ApiModelProperty(value = "提醒人")
	private String staff;
	private String staffId;
	@ApiModelProperty("适用人员名称")
	private String suitPeopleName;
	@ApiModelProperty("操作人")
	private String createName;
	@ApiModelProperty("提醒人名称")
	private String staffName;
	private String roomId;
}
