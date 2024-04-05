package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.MaterialStore;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 素材表VO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("素材名称")
    private String matName;

    @ApiModelProperty("素材分类id")
    private Integer matType;

    @ApiModelProperty("素材内容")
    private String matContent;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createBy;

	@ApiModelProperty("创建人名称")
	private String createName;

	@ApiModelProperty("素材类型名称")
	private String typeName;

	@ApiModelProperty("商店列表")
	private List<MaterialStore> shops;

	@ApiModelProperty("是否是全部商店")
	private Integer  isAllShop;

	@ApiModelProperty("开始时间(素材有效期)")
	private Date validityCreateTime;

	@ApiModelProperty("截止时间(素材有效期)")
	private Date validityEndTime;

	@ApiModelProperty("是否置顶 0：不置顶  1：置顶")
	private Integer isTop;

	@ApiModelProperty("是否开启互动雷达 0否1是")
	private Integer interactiveRadar;

	@ApiModelProperty("素材标签")
	private String matLabal;

	@ApiModelProperty("使用次数")
	private Integer useNum;


	@ApiModelProperty("商店列表")
	private List<Long> shopIds;

	@ApiModelProperty("话术内容")
	private String scriptContent;

	@ApiModelProperty("素材文件类型 text:文本,image:图片,video:视频,miniprogram:小程序 h5：H5页面 文件：file")
	private String msgType;

	@ApiModelProperty("累计访客数")
	private Integer visitorCount;

	@ApiModelProperty("累计浏览数")
	private Integer browseCount;


}
