package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 素材表DTO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
public class MaterialDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("素材名称")
	@NotBlank(message = "素材名称不能为空")
    private String matName;

    @ApiModelProperty("素材分类id")
	@NotNull(message = "素材分类不能为空")
    private Integer matType;

	@ApiModelProperty("是否置顶，0否 1是")
	private Integer isTop;

	@ApiModelProperty("开始时间(素材有效期)")
	private Date createTime;

	@ApiModelProperty("开始时间(素材有效期)")
//	@NotNull(message = "素材有效期不能为空")
	private Date validityCreateTime;

	@ApiModelProperty("截止时间(素材有效期)")
//	@NotNull(message = "素材有效期不能为空")
	private Date validityEndTime;

	@ApiModelProperty("门店列表")
	private List<Long> storeIds;

	@ApiModelProperty("是否是全部商店")
	private Integer  isAllShop;

	@ApiModelProperty("消息列表")
	@NotEmpty(message = "消息列表不能为空")
	private List<MaterialMsgDTO> msgList;

	@ApiModelProperty("是否开启互动雷达 0否1是")
	private Integer interactiveRadar;

	@ApiModelProperty("素材标签")
	private String matLabal;

	@ApiModelProperty("素材互动雷达标签")
//	@NotEmpty(message = "素材互动雷达标签不能为空")
	private List<CpMaterialLableDTO> lableList;

	@ApiModelProperty("话术内容")
	private String scriptContent;


}
