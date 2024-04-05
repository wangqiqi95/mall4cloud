package com.mall4j.cloud.biz.vo.channels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ChannelsBrandVO {
	
	@ApiModelProperty("品牌ID")
	private String brandId;
	
	//@ApiModelProperty(value = "品牌商标中文名")
	//private String chName;
	//
	//@ApiModelProperty(value = "品牌商标英文名")
	//private String enName;
	
	@ApiModelProperty("品牌名称")
	private String brandName;
	
	@ApiModelProperty("审核状态 1审核中,2审核失败,3已生效,4已撤回,5即将过期(不影响商品售卖),6已过期")
	private Integer status;
	
	@ApiModelProperty("申请时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@ApiModelProperty("驳回原因")
	private String reason;
	
}
