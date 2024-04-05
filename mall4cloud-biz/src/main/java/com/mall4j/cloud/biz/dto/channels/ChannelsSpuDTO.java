package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 视频号4.0商品DTO
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
public class ChannelsSpuDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("视频号商品id")
    private Long outSpuId;

    @ApiModelProperty("品牌id，无品牌为“2100000000”")
    private Long brandId;

    @ApiModelProperty("标题，最少3字符，最多60字符。文本内容规则请看注意事项")
    private String title;

    @ApiModelProperty("副标题，最多18字符")
    private String subTitle;

    @ApiModelProperty("主图，多张，列表，最少1张，最多9张")
    private String headImgs;

    @ApiModelProperty("发货方式，若为无需快递（仅对部分类目开放），则无需填写运费模版id。0:快递发货；1:无需快递；默认0")
    private Integer deliverMethod;

    @ApiModelProperty("运费模板id")
    private String freightTemplate;

    @ApiModelProperty("类目ID")
    private String cats;

    @ApiModelProperty("审核备注")
    private String auditReason;

    @ApiModelProperty("0:初始值 5:上架 6:回收站 11:自主下架 13:违规下架/风控系统下架")
    private Integer status;

    @ApiModelProperty("0:初始值 1:编辑中 2:审核中 3:审核失败 4:审核成功 5:商品信息写入中")
    private Integer editStatus;

    @ApiModelProperty("是否在橱窗上架 0否1是")
    private Integer inWinodws;

    @ApiModelProperty("库存共享比例")
    private Integer stockShareRate;

    @ApiModelProperty("商家提交审核时间")
    private Date verifyTime;

    @ApiModelProperty("商品审核通过时间")
    private Date successTime;

    @ApiModelProperty("商品审核未通过时间")
    private Date failTime;

    @ApiModelProperty("违规下架时间")
    private Date shelfTime;

    @ApiModelProperty("平台撤销时间")
    private Date cancelTime;

    @ApiModelProperty("商家删除直播商品时间")
    private Date deleteTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getOutSpuId() {
		return outSpuId;
	}

	public void setOutSpuId(Long outSpuId) {
		this.outSpuId = outSpuId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getHeadImgs() {
		return headImgs;
	}

	public void setHeadImgs(String headImgs) {
		this.headImgs = headImgs;
	}

	public Integer getDeliverMethod() {
		return deliverMethod;
	}

	public void setDeliverMethod(Integer deliverMethod) {
		this.deliverMethod = deliverMethod;
	}

	public String getFreightTemplate() {
		return freightTemplate;
	}

	public void setFreightTemplate(String freightTemplate) {
		this.freightTemplate = freightTemplate;
	}

	public String getCats() {
		return cats;
	}

	public void setCats(String cats) {
		this.cats = cats;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEditStatus() {
		return editStatus;
	}

	public void setEditStatus(Integer editStatus) {
		this.editStatus = editStatus;
	}

	public Integer getInWinodws() {
		return inWinodws;
	}

	public void setInWinodws(Integer inWinodws) {
		this.inWinodws = inWinodws;
	}

	public Integer getStockShareRate() {
		return stockShareRate;
	}

	public void setStockShareRate(Integer stockShareRate) {
		this.stockShareRate = stockShareRate;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public Date getFailTime() {
		return failTime;
	}

	public void setFailTime(Date failTime) {
		this.failTime = failTime;
	}

	public Date getShelfTime() {
		return shelfTime;
	}

	public void setShelfTime(Date shelfTime) {
		this.shelfTime = shelfTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
	public String toString() {
		return "ChannelsSpuDTO{" +
				"id=" + id +
				",spuId=" + spuId +
				",outSpuId=" + outSpuId +
				",brandId=" + brandId +
				",title=" + title +
				",subTitle=" + subTitle +
				",headImgs=" + headImgs +
				",deliverMethod=" + deliverMethod +
				",freightTemplate=" + freightTemplate +
				",cats=" + cats +
				",auditReason=" + auditReason +
				",status=" + status +
				",editStatus=" + editStatus +
				",inWinodws=" + inWinodws +
				",stockShareRate=" + stockShareRate +
				",verifyTime=" + verifyTime +
				",successTime=" + successTime +
				",failTime=" + failTime +
				",shelfTime=" + shelfTime +
				",cancelTime=" + cancelTime +
				",deleteTime=" + deleteTime +
				'}';
	}
}
