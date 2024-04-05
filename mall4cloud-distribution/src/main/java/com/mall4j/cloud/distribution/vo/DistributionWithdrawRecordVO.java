package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 佣金管理-佣金提现记录VO
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
@Data
public class DistributionWithdrawRecordVO extends BaseVO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2威客")
    private Integer identityType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String username;

    @ApiModelProperty("认证姓名")
    private String authUsername;

    @ApiModelProperty("工号/卡号")
    private String userNumber;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("门店ID")
    private Long storeId;

	@ApiModelProperty("门店编码")
	private String storeCode;

	@ApiModelProperty("门店名称")
	private String storeName;

    @ApiModelProperty("提现单号")
    private String withdrawOrderNo;

    @ApiModelProperty("是否历史订单 0否 1是")
    private Integer historyOrder;

    @ApiModelProperty("提现金额")
    private Long withdrawAmount;

    @ApiModelProperty("提现需退还金额")
    private Long withdrawNeedRefundAmount;

    @ApiModelProperty("转账方式 1默认三方转账")
    private Integer transferType;

    @ApiModelProperty("申请时间")
    private Date applyTime;

    @ApiModelProperty("京东益世申请id")
    private String applyId;

    @ApiModelProperty("处理时间")
    private Date processTime;

    @ApiModelProperty("提现状态 0待处理 1提现中 2提现成功 3提现失败 4拒绝提现")
    private Integer withdrawStatus;

    @ApiModelProperty("拒绝原因")
    private String rejectReason;

    @ApiModelProperty("转账失败原因")
    private String transferFailReason;

    @ApiModelProperty("处理人ID")
    private Long reviewerId;

    @ApiModelProperty("处理人姓名")
    private String reviewerName;

}
