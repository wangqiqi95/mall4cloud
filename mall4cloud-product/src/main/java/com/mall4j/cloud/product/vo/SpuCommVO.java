package com.mall4j.cloud.product.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品评论VO
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpuCommVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long spuCommId;

    @ApiModelProperty("商品ID")
    private Long spuId;

    @ApiModelProperty("订单项ID")
    private Long orderItemId;

    @ApiModelProperty("评论用户ID")
    private Long userId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("掌柜回复")
    private String replyContent;

    @ApiModelProperty("记录时间")
    private Date createTime;

    @ApiModelProperty("回复时间")
    private Date replyTime;

    @ApiModelProperty("是否回复 0:未回复  1:已回复")
    private Integer replySts;

    @ApiModelProperty("IP来源")
    private String postip;

    @ApiModelProperty("商品得分，1-5分")
    private Integer score;

    @ApiModelProperty("店铺评分 1-5分")
    private Integer storeScore;

    @ApiModelProperty("物流评分 1-5分")
    private Integer logisticsScore;

    @ApiModelProperty("有用的计数")
    private Integer usefulCounts;

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("晒图的json字符串")
    private String pics;

    @ApiModelProperty("是否匿名(1:是  0:否)")
    private Integer isAnonymous;

    @ApiModelProperty("评价(0好评 1中评 2差评)")
    private Integer evaluate;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String userPic;

    @ApiModelProperty("商品图片")
	private String spuImg;

    @ApiModelProperty("商品规格")
	private String skuName;

    @ApiModelProperty("成交时间")
	private Date transactionTime;

	//--------------------------------------------------------
	@ApiModelProperty("追加评论内容")
	private String additContent;

	@ApiModelProperty("追加商家回复")
	private String additReplyContent;

	@ApiModelProperty("追加回复时间")
	private Date additReplyTime;

	@ApiModelProperty("追加时间")
	private Date additCreateTime;

	@ApiModelProperty("追加评论是否回复 0:未回复  1:已回复")
	private Integer additReplySts;

	@ApiModelProperty("是否有图片 0 没有 1 有")
	private Integer hasImages;

	@ApiModelProperty("客户姓名")
	private String userName;

	@ApiModelProperty("追加晒图的json字符串")
	private String additPics;

	@ApiModelProperty("客户手机号码")
	private String mobile;

	@ApiModelProperty("订单号")
	private Long orderId;

	@ApiModelProperty("订单编号")
	private String orderNumber;

	@ApiModelProperty("商品名称")
	private String prodName;

	@ApiModelProperty("商品图片")
	private String prodImage;

	@ApiModelProperty("状态")
	private Integer status;

	@ApiModelProperty("回复人信息")
	private String replyStaffName;

	@ApiModelProperty("追加回复人信息")
	private String additReplyStaffName;

	@Override
	public String toString() {
		return "SpuCommVO{" +
				"spuCommId=" + spuCommId +
				", spuId=" + spuId +
				", orderItemId=" + orderItemId +
				", userId=" + userId +
				", content='" + content + '\'' +
				", replyContent='" + replyContent + '\'' +
				", createTime=" + createTime +
				", replyTime=" + replyTime +
				", replySts=" + replySts +
				", postip='" + postip + '\'' +
				", score=" + score +
				", storeScore=" + storeScore +
				", logisticsScore=" + logisticsScore +
				", usefulCounts=" + usefulCounts +
				", pics='" + pics + '\'' +
				", isAnonymous=" + isAnonymous +
				", evaluate=" + evaluate +
				", nickName='" + nickName + '\'' +
				", userPic='" + userPic + '\'' +
				", spuImg='" + spuImg + '\'' +
				", skuName='" + skuName + '\'' +
				", transactionTime=" + transactionTime +
				'}';
	}
}
