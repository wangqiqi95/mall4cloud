package com.mall4j.cloud.common.product.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 种草VO
 *
 * @author cg
 */
@Data
public class RecommendVO {

    @ApiModelProperty("主键id")
    private Long recommendId;

    @ApiModelProperty("发布用户：1-B端用户 | 2-C端用户")
    private Integer userType;

    @ApiModelProperty("创建人nick")
    private String userNick;

    @ApiModelProperty("创建人头像")
    private String userPic;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("分类名称-B端用户查看需要")
    private String cateName;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("冗余商品id，逗号隔开")
    private String productIds;

    @ApiModelProperty("状态：0-默认 | 1-待审核 | 2-审核通过 | 3-已驳回")
    private Integer status;

    @ApiModelProperty("封面")
    private String coverUrl;

    @ApiModelProperty("点赞数")
    private Long praiseCount;

    @ApiModelProperty("收藏数")
    private Long collectCount;

    @ApiModelProperty("分享数")
    private Long shareCount;

    @ApiModelProperty("阅读数")
    private Long readCount;

    @ApiModelProperty("发布时间")
    private Date createTime;

    @ApiModelProperty("是否点赞：0-否 | 1-是")
    private Integer isPraise = 0;

    @ApiModelProperty("是否收藏：0-否 | 1-是")
    private Integer isCollect = 0;
}
