package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.MaterialStore;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class MiniMaterialVO extends MaterialVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("素材名称")
    private String matName;

    @ApiModelProperty("素材分类id")
    private Integer matType;

    @ApiModelProperty("素材内容")
    private String matContent;

	@ApiModelProperty("素材类型名称")
	private String typeName;

	@ApiModelProperty("消息类型  text-文本 image-图片 video-视频 miniprogram-小程序 h5-H5")
	private String type;

	@ApiModelProperty("微信的mediaId")
	private String mediaId;

	@ApiModelProperty("消息内容")
	private String content;

	@ApiModelProperty("消息图片url")
	private String url;

	@ApiModelProperty("消息图片url")
	private String appId;

	@ApiModelProperty("小程序APPid")
	private String appTitle;

	@ApiModelProperty("小程序标题")
	private String appPage;

	@ApiModelProperty("小程序封面url")
	private String appPic;

	@ApiModelProperty("开始时间(素材有效期)")
	private Date validityCreateTime;

	@ApiModelProperty("截止时间(素材有效期)")
	private Date validityEndTime;

	@ApiModelProperty("是否置顶 0：不置顶  1：置顶")
	private Integer isTop;

	@ApiModelProperty("是否开启互动雷达 0否1是")
	private Integer interactiveRadar;

	@ApiModelProperty("浏览多少秒开始统计")
	private Integer interactionSecond;

	@ApiModelProperty("互动达标记录标签")
	private String radarLabal;
	@ApiModelProperty("文章url")
	private String articleUrl;
	@ApiModelProperty("文章描述")
	private String articleDesc;
	@ApiModelProperty("话术内容")
	private String scriptContent;
	private String picUrl;
	@ApiModelProperty("话术内容")
	private Long scriptId;

	@ApiModelProperty("素材消息id")
	private Long matMsgId;

	@ApiModelProperty("内容类型：1-素材/2-话术")
	private Integer contentType;

	@ApiModelProperty("话术类型 0普通话术 1问答话术")
	private Integer chatScriptType;

	@ApiModelProperty("累计访客数")
	private Integer visitorCount;

	@ApiModelProperty("累计浏览数")
	private Integer browseCount;
}
