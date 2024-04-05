package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：获取商品列表请求响应
 */
public class GoodListRespDto implements Serializable, BaseResultDto {

    private static final long serialVersionUID = -3499530588529886697L;
    @ApiModelProperty(value = "商品序号，商城范围内唯一标识", required = true)
    private String itemid;

    @ApiModelProperty("货号")
    private String itemno;

    @ApiModelProperty(value = "商品标题", required = true)
    private String title;

    @ApiModelProperty(value = "商品状态(销售中=onsale，下架=instock，售罄=soldout)", required = true)
    private String status;

    @ApiModelProperty("商品原价，即划线价")
    private BigDecimal originalprice;

    @ApiModelProperty(value = "商品价格，单位：元，保留两个小数，精确到分", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "商品详情链接", required = true)
    private String url;

    @ApiModelProperty(value = "商品预览图片", required = true)
    private String imageurl;

    @ApiModelProperty("商家编码")
    private String outercode;

    @ApiModelProperty(value = "总库存", required = true)
    private Integer quantity;

    @ApiModelProperty(value = "创建时间(格式:yyyy-MM-dd HH:mm:ss)", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @ApiModelProperty("更新时间(格式:yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(BigDecimal originalprice) {
        this.originalprice = originalprice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getOutercode() {
        return outercode;
    }

    public void setOutercode(String outercode) {
        this.outercode = outercode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "GoodListRespDto{" + "itemid='" + itemid + '\'' + ", itemno='" + itemno + '\'' + ", title='" + title + '\'' + ", status='" + status + '\''
                + ", originalprice=" + originalprice + ", price=" + price + ", url='" + url + '\'' + ", imageurl='" + imageurl + '\'' + ", outercode='"
                + outercode + '\'' + ", quantity=" + quantity + ", createtime=" + createtime + ", updatetime=" + updatetime + '}';
    }
}
