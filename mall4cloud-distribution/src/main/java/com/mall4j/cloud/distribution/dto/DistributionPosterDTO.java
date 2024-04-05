package com.mall4j.cloud.distribution.dto;

import com.mall4j.cloud.distribution.model.DistributionPosterStore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-推广海报DTO
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
@Data
public class DistributionPosterDTO{

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

	@ApiModelProperty("门店类型 0全部门店 1指定门店")
	private Integer storeType;

	@ApiModelProperty("开始时间")
	private Date startTime;

	@ApiModelProperty("结束时间")
	private Date endTime;

	@ApiModelProperty("指定作用门店集合")
	private List<DistributionPosterStore> storeList;

    private Integer distributionType;

    private Long currentUserId;

    private List<Long> ids;

    private Long queryStoreId;
}
