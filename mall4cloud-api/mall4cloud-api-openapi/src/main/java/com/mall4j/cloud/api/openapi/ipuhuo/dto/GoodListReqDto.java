package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：获取商品列表请求dto
 */
public class GoodListReqDto extends CommonReqDto implements Serializable {

    private static final long serialVersionUID = 4196221821244923215L;
    @ApiModelProperty(value = "需要返回的字段列表，可选值，为商品结构中的以下字段：itemid,itemno,title,status,originalprice,price,url,imageurl,outercode,quantity,createtime,updatetime", required = true)
    private String fields;

    @ApiModelProperty("商品名称，支持模糊搜索，大小写不敏感")
    private String title;

    @ApiModelProperty("商品序号，商城范围内唯一标识")
    private Integer itemid;

    @ApiModelProperty("货号")
    private String itemno;

    @ApiModelProperty("商品分组id，以逗号分隔")
    private String tags;

    /**
     * {@link com.mall4j.cloud.api.openapi.ipuhuo.enums.ProductStatus}
     */
    @ApiModelProperty("商品状态(销售中=onsale，下架=instock，售罄=soldout)")
    private String status;

    @ApiModelProperty("商家编码，支持批量，以逗号分隔，最多支持40个")
    private String outercode;

    @ApiModelProperty("开始时间(格式:yyyy-MM-dd HH:mm:ss)")
    private Date starttime;

    @ApiModelProperty("截止时间(格式:yyyy-MM-dd HH:mm:ss)")
    private Date endtime;

    @ApiModelProperty("时间类别(商品修改时间=1，商品创建时间= 2，商品上/下架时间=3)\n" + "\n" + "当status=onsale，timetype=3时，表示按上架时间范围搜索，\n" + "\n"
            + "当status=instock/soldout，timetype=3时，表示按下架时间范围搜索")
    private Integer timetype;

    @ApiModelProperty("排序方式。格式为column:asc/desc，排序字段：\n" + "\n" + "1.创建时间：createtime，2.更新时间：updatetime，3.价格：price，4.销量：soldnum")
    private String orderby;

    @ApiModelProperty("页码，从1开始")
    private String pageindex;

    @ApiModelProperty("每页条数,默认30，最大200")
    private String pagesize;

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutercode() {
        return outercode;
    }

    public void setOutercode(String outercode) {
        this.outercode = outercode;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getTimetype() {
        return timetype;
    }

    public void setTimetype(Integer timetype) {
        this.timetype = timetype;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPageindex() {
        return pageindex;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    @Override
    public String toString() {
        return super.toString() + ",GoodListReqDto{" + "fields='" + fields + '\'' + ", title='" + title + '\'' + ", itemid=" + itemid + ", itemno='" + itemno
                + '\'' + ", tags='" + tags + '\'' + ", status='" + status + '\'' + ", outercode='" + outercode + '\'' + ", starttime=" + starttime
                + ", endtime=" + endtime + ", timetype=" + timetype + ", orderby='" + orderby + '\'' + ", pageindex='" + pageindex + '\'' + ", pagesize='"
                + pagesize + '\'' + '}';
    }
}
