package com.mall4j.cloud.biz.model.cp;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Data
public class DistributionMoments extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("标题")
    private String title;

	@ApiModelProperty("可见开始时间")
    private Date startTime;

	@ApiModelProperty("可见结束时间")
    private Date endTime;

	@ApiModelProperty("头像地址")
    private String avatarUrl;

	@ApiModelProperty("文案")
    private String descHtml;

	@ApiModelProperty("评论")
    private String comment;

	@ApiModelProperty("选择商品类型 0全部商品 1部分商品")
    private Integer productType;

	@ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

	@ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

	@ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

	@ApiModelProperty("0待发送/1已下发/2发送失败")
	private Integer sendStatus;

	@ApiModelProperty("是否置顶 1是 0否")
	private Integer top;

	@ApiModelProperty("置顶时间")
	private Date topTime;
	@ApiModelProperty("启用时间")
	private Date enableTime;
	@ApiModelProperty("异步任务id，最大长度为64字节，24小时有效")
	private String qwJobId;
	@ApiModelProperty("企微朋友圈ID")
	private String qwMomentsId;
	@ApiModelProperty("通过jobid查询的执行结果")
	private String sendResult;


//	@ApiModelProperty("朋友圈商品集合")
//	private List<DistributionMomentsProduct> productList;

}
