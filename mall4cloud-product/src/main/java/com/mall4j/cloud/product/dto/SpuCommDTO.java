package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品评论DTO
 *
 * @author YXF
 * @date 2021-01-11 13:47:33
 */
public class SpuCommDTO{
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

    @ApiModelProperty("晒图的json字符串")
    private String pics;

    @ApiModelProperty("是否匿名(1:是  0:否)")
    private Integer isAnonymous;

    @ApiModelProperty("是否显示  -1:删除 0:不显示 1:显示 2 审核不通过 3.待审核")
    private Integer status;

    @ApiModelProperty("评价(0好评 1中评 2差评)")
    private Integer evaluate;

    @ApiModelProperty("是否追加评价 1 是 0 否")
    private Integer  isAddit;

	public Long getSpuCommId() {
		return spuCommId;
	}

	public void setSpuCommId(Long spuCommId) {
		this.spuCommId = spuCommId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Integer getReplySts() {
		return replySts;
	}

	public void setReplySts(Integer replySts) {
		this.replySts = replySts;
	}

	public String getPostip() {
		return postip;
	}

	public void setPostip(String postip) {
		this.postip = postip;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getStoreScore() {
		return storeScore;
	}

	public void setStoreScore(Integer storeScore) {
		this.storeScore = storeScore;
	}

	public Integer getLogisticsScore() {
		return logisticsScore;
	}

	public void setLogisticsScore(Integer logisticsScore) {
		this.logisticsScore = logisticsScore;
	}

	public Integer getUsefulCounts() {
		return usefulCounts;
	}

	public void setUsefulCounts(Integer usefulCounts) {
		this.usefulCounts = usefulCounts;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public Integer getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(Integer isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}

    public Integer getIsAddit() {
        return isAddit;
    }

    public void setIsAddit(Integer isAddit) {
        this.isAddit = isAddit;
    }

    @Override
	public String toString() {
		return "SpuCommDTO{" +
				"spuCommId=" + spuCommId +
				", spuId=" + spuId +
				", orderItemId=" + orderItemId +
				", userId=" + userId +
				", content='" + content + '\'' +
				", replyContent='" + replyContent + '\'' +
				", replySts=" + replySts +
				", postip='" + postip + '\'' +
				", score=" + score +
				", storeScore=" + storeScore +
				", logisticsScore=" + logisticsScore +
				", usefulCounts=" + usefulCounts +
				", pics='" + pics + '\'' +
				", isAnonymous=" + isAnonymous +
				", status=" + status +
				", evaluate=" + evaluate +
				'}';
	}
}
