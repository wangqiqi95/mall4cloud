package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分销推广-分销专题VO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
@Data
public class DistributionSpecialSubjectVO extends BaseVO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("微页面ID")
    private Long pageId;

    @ApiModelProperty("微页面名称")
    private String pageName;

    @ApiModelProperty("专题名称")
    private String subjectName;

    @ApiModelProperty("可见开始时间")
    private Date startTime;

    @ApiModelProperty("可见结束时间")
    private Date endTime;

    @ApiModelProperty("宣传图地址")
    private String publicityImgUrl;

    @ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

    @ApiModelProperty("选择商品类型 0全部商品 1部分商品")
    private Integer productType;

    @ApiModelProperty("宣传文案")
    private String publicityText;

    @ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("是否自定义 1是 0否")
    private Integer isRec;

    @ApiModelProperty("推荐颜色")
    private String recColor;

    @ApiModelProperty("自定义颜色")
    private String customizeColor;

	@ApiModelProperty("是否置顶 1是 0否")
	private Integer top;

	@ApiModelProperty("适用门店数量")
	private Integer storeNum;
}
