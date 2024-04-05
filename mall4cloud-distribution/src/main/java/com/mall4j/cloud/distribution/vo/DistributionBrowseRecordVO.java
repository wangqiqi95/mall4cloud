package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分享推广-浏览记录VO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Data
public class DistributionBrowseRecordVO extends BaseVO{

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("浏览人ID")
    private Long browseId;

    @ApiModelProperty("浏览人姓名")
    private String browseName;

	@ApiModelProperty("浏览人手机号")
	private String browseMobile;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer browseType;

    @ApiModelProperty("触点号")
    private String tentacleNo;

    @ApiModelProperty("分享人ID")
    private Long shareId;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer shareType;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("活动名称")
    private String activityName;

    private Integer totalNum;

}
