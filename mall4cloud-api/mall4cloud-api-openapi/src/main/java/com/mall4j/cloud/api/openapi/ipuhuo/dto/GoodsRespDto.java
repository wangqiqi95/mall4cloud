package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * 类描述：获取商品详细信息 响应信息
 */
public class GoodsRespDto implements Serializable, BaseResultDto {

    @ApiModelProperty(value = "商品序号，商城范围内唯一标识", required = true)
    private String itemid;

    @ApiModelProperty(value = "商品标题", required = true)
    private String title;

    @ApiModelProperty("商品英文标题")
    private String title_en;

    @ApiModelProperty("短标题")
    private String shorttitle;

    @ApiModelProperty("短标题")
    private String shorttitle_en;

    @ApiModelProperty("货号")
    private String itemno;

    @ApiModelProperty("商品所属叶子类目Id")
    private String cid;

    @ApiModelProperty("商品所属各级类目")
    private Cat[] cats;

    @ApiModelProperty("商品分组名称，以逗号分隔")
    private String tags;

    @ApiModelProperty(value = "商品详情html", required = true)
    private String desc;

    @ApiModelProperty("商品英文版详情html")
    private String desc_en;

    @ApiModelProperty("手机端详情html")
    private String wapdesc;

    @ApiModelProperty("手机端英文版详情html")
    private String wapdesc_en;

    /**
     * {@link com.mall4j.cloud.api.openapi.ipuhuo.enums.ProductStatus}
     */
    @ApiModelProperty(value = "商品状态(销售中=onsale，下架=instock，售罄=soldout)", required = true)
    private String status;

    @ApiModelProperty("商品原价，即划线价")
    private BigDecimal originalprice;

    @ApiModelProperty(value = "商品价格，单位：元，保留两个小数，精确到分", required = true)
    private BigDecimal price;

    @ApiModelProperty("货单单位，默认为人民币")
    private String currencyunit;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("品牌英文名称")
    private String brand_en;

    @ApiModelProperty(value = "商品详情链接", required = true)
    private String url;

    @ApiModelProperty("商家编码")
    private String outercode;

    @ApiModelProperty(value = "总库存", required = true)
    private Integer quantity;

    @ApiModelProperty("商品属性集合")
    private AddGoodsReqDto.Prop[] props;

    @ApiModelProperty("长度 单位 mm")
    private Integer length;

    @ApiModelProperty("宽度 单位mm")
    private Integer width;

    @ApiModelProperty("高度 单位mm")
    private Integer height;

    @ApiModelProperty("重量 单位g")
    private Integer weight;

    @ApiModelProperty("商品级别条码")
    private String barcode;

    @ApiModelProperty("商品卖点")
    private String sellpoint;

    @ApiModelProperty("透明素材图")
    private String transparentimg;

    @ApiModelProperty(value = "商品主图，第一张为默认的主图", required = true)
    private AddGoodsReqDto.ItemImgs[] itemimgs;

    @ApiModelProperty("Sku属性图片")
    private AddGoodsReqDto.SkuPropImg[] propimgs;

    @ApiModelProperty("Sku列表")
    private AddGoodsReqDto.Sku[] skus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间(格式:yyyy-MM-dd HH:mm:ss)", required = true)
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间(格式:yyyy-MM-dd HH:mm:ss)")
    private Date updatetime;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getShorttitle() {
        return shorttitle;
    }

    public void setShorttitle(String shorttitle) {
        this.shorttitle = shorttitle;
    }

    public String getShorttitle_en() {
        return shorttitle_en;
    }

    public void setShorttitle_en(String shorttitle_en) {
        this.shorttitle_en = shorttitle_en;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Cat[] getCats() {
        return cats;
    }

    public void setCats(Cat[] cats) {
        this.cats = cats;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc_en() {
        return desc_en;
    }

    public void setDesc_en(String desc_en) {
        this.desc_en = desc_en;
    }

    public String getWapdesc() {
        return wapdesc;
    }

    public void setWapdesc(String wapdesc) {
        this.wapdesc = wapdesc;
    }

    public String getWapdesc_en() {
        return wapdesc_en;
    }

    public void setWapdesc_en(String wapdesc_en) {
        this.wapdesc_en = wapdesc_en;
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

    public String getCurrencyunit() {
        return currencyunit;
    }

    public void setCurrencyunit(String currencyunit) {
        this.currencyunit = currencyunit;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand_en() {
        return brand_en;
    }

    public void setBrand_en(String brand_en) {
        this.brand_en = brand_en;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public AddGoodsReqDto.Prop[] getProps() {
        return props;
    }

    public void setProps(AddGoodsReqDto.Prop[] props) {
        this.props = props;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSellpoint() {
        return sellpoint;
    }

    public void setSellpoint(String sellpoint) {
        this.sellpoint = sellpoint;
    }

    public String getTransparentimg() {
        return transparentimg;
    }

    public void setTransparentimg(String transparentimg) {
        this.transparentimg = transparentimg;
    }

    public AddGoodsReqDto.ItemImgs[] getItemimgs() {
        return itemimgs;
    }

    public void setItemimgs(AddGoodsReqDto.ItemImgs[] itemimgs) {
        this.itemimgs = itemimgs;
    }

    public AddGoodsReqDto.SkuPropImg[] getPropimgs() {
        return propimgs;
    }

    public void setPropimgs(AddGoodsReqDto.SkuPropImg[] propimgs) {
        this.propimgs = propimgs;
    }

    public AddGoodsReqDto.Sku[] getSkus() {
        return skus;
    }

    public void setSkus(AddGoodsReqDto.Sku[] skus) {
        this.skus = skus;
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
        return "GoodsRespDto{" + "itemid='" + itemid + '\'' + ", title='" + title + '\'' + ", title_en='" + title_en + '\'' + ", shorttitle='" + shorttitle
                + '\'' + ", shorttitle_en='" + shorttitle_en + '\'' + ", itemno='" + itemno + '\'' + ", cid='" + cid + '\'' + ", cats=" + Arrays.toString(cats)
                + ", tags='" + tags + '\'' + ", desc='" + desc + '\'' + ", desc_en='" + desc_en + '\'' + ", wapdesc='" + wapdesc + '\'' + ", wapdesc_en='"
                + wapdesc_en + '\'' + ", status='" + status + '\'' + ", originalprice=" + originalprice + ", price=" + price + ", currencyunit='" + currencyunit
                + '\'' + ", brand='" + brand + '\'' + ", brand_en='" + brand_en + '\'' + ", url='" + url + '\'' + ", outercode='" + outercode + '\''
                + ", quantity=" + quantity + ", props=" + Arrays.toString(props) + ", length=" + length + ", width=" + width + ", height=" + height
                + ", weight=" + weight + ", barcode='" + barcode + '\'' + ", sellpoint='" + sellpoint + '\'' + ", transparentimg='" + transparentimg + '\''
                + ", itemimgs=" + Arrays.toString(itemimgs) + ", propimgs=" + Arrays.toString(propimgs) + ", skus=" + Arrays.toString(skus) + ", createtime="
                + createtime + ", updatetime=" + updatetime + '}';
    }

    public static class Cat {
        private String id;

        private String name;

        private String name_en;

        private String parentid;

        private Integer level;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_en() {
            return name_en;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return "Cat{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", name_en='" + name_en + '\'' + ", parentid='" + parentid + '\'' + ", level="
                    + level + '}';
        }
    }
}
