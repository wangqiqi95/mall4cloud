package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 视频号4.0 类目DTO
 *
 * @author FrozenWatermelon
 * @date 2023-02-15 16:01:16
 */
@Data
public class ChannelsCategoryDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("审核单id")
    private Long auditId;

	@ApiModelProperty("列表搜索名称")
	private String queryName;

    @ApiModelProperty("等级1")
    private Long level1;

    @ApiModelProperty("等级1")
    private String name1;

    @ApiModelProperty("等级2")
    private Long level2;

    @ApiModelProperty("等级2")
    private String name2;

    @ApiModelProperty("等级3")
    private Long level3;

    @ApiModelProperty("等级3")
    private String name3;

    @ApiModelProperty("审核状态, 1：审核中，3：审核成功，2：审核拒绝，12：主动取消申请单")
    private Integer status;

    @ApiModelProperty("如果审核拒绝，返回拒绝原因")
    private String rejectReason;

    @ApiModelProperty("阿里云地址链接，用英文逗号分割 资质材料，图片fileid，图片类型，最多不超过10张")
    private String certificate;

    /*@ApiModelProperty("视频号mediaids 用英文逗号分割 资质材料，图片fileid，图片类型，最多不超过10张")
    private String certificate_mediaids;*/

    @ApiModelProperty("报备函，图片fileid，图片类型，最多不超过10张")
    private String baobeihan;

    @ApiModelProperty("经营证明，图片fileid，图片类型，最多不超过10张")
    private String jingyingzhengming;

    @ApiModelProperty("带货口碑，图片fileid，图片类型，最多不超过10张")
    private String daihuokoubei;

    @ApiModelProperty("入住资质，图片fileid，图片类型，最多不超过10张")
    private String ruzhuzhizhi;

    @ApiModelProperty("经营流水，图片fileid，图片类型，最多不超过10张")
    private String jingyingliushui;

    @ApiModelProperty("补充材料，图片fileid，图片类型，最多不超过10张")
    private String buchongcailiao;

    @ApiModelProperty("经营平台，仅支持taobao，jd，douyin，kuaishou，pdd，other这些取值")
    private String jingyingpingtai;

    @ApiModelProperty("账号名称")
    private String zhanghaomingcheng;

    private String certificateUrl;
    private String baobeihanUrl;
    private String jingyingzhengmingUrl;
    private String daihuokoubeiUrl;
    private String ruzhuzhizhiUrl;
    private String jingyingliushuiUrl;
    private String buchongcailiaoUrl;
}
