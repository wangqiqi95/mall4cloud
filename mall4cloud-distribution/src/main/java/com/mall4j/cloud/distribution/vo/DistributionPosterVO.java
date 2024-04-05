package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分销推广-推广海报VO
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
@Data
public class DistributionPosterVO extends BaseVO{

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("海报类型 1商品 2微页面 3店铺 4发展")
    private Integer posterType;

    @ApiModelProperty("素材ID")
    private Long materialId;

    @ApiModelProperty("海报名称")
    private String posterName;

    @ApiModelProperty("海报权重")
    private Integer posterIndex;

    @ApiModelProperty("显示类型 1分享海报 2生日图文")
	private Integer showType;

    @ApiModelProperty("宣传文案")
    private String publicityText;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("宣传图地址")
    private String publicityImgUrl;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

}
