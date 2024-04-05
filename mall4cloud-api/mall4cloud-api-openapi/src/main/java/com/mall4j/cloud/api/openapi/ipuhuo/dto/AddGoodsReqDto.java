package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 类描述：添加商品请求参数
 */
public class AddGoodsReqDto extends CommonReqDto implements Serializable {

    private static final long serialVersionUID = -6097860141106277745L;
    @ApiModelProperty(value = "商品所属叶子类目Id", required = true)
    private String cid;

    @ApiModelProperty(value = "商品所属叶子类目名称", required = true)
    private String cname;

    @ApiModelProperty("商品分组")
    private Integer[] tags;

    @ApiModelProperty(value = "商品标题", required = true)
    private String title;

    @ApiModelProperty("商品级别条码")
    private String barcode;

    @ApiModelProperty("商家编码")
    private String outercode;

    @ApiModelProperty(value = "商品状态(上架=onsale，下架=instock，草稿=draft)", required = true)
    private String status;

    @ApiModelProperty("商品重量")
    private Integer weight;

    @ApiModelProperty("划线价")
    private BigDecimal originalprice;

    @ApiModelProperty(value = "价格", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "总库存", required = true)
    private Integer quantity;

    @ApiModelProperty("商品卖点")
    private String sellpoint;

    @ApiModelProperty(value = "商品pc详情", required = true)
    private String desc;

    @ApiModelProperty("商品手机端详情")
    private String wapdesc;

    @ApiModelProperty(value = "商品主图", required = true)
    private ItemImgs[] itemimgs;

    @ApiModelProperty("sku属性图片")
    private SkuPropImg[] propimgs;

    @ApiModelProperty("商品属性")
    private Prop[] props;

    @ApiModelProperty("商品sku列表")
    private Sku[] skus;

    public static class ItemImgs {

        @ApiModelProperty(value = "商品主图url", required = true)
        private String url;

        @ApiModelProperty(value = "商品主图顺序", required = true)
        private Integer index;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "ItemImgs{" + "url='" + url + '\'' + ", index=" + index + '}';
        }
    }

    public static class SkuPropImg {
        @ApiModelProperty("sku属性图片url")
        private String url;

        private String pname;

        private String pname_en;

        private String pvalue;

        private String pvalue_en;

        private Integer index;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPvalue() {
            return pvalue;
        }

        public void setPvalue(String pvalue) {
            this.pvalue = pvalue;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getPname_en() {
            return pname_en;
        }

        public void setPname_en(String pname_en) {
            this.pname_en = pname_en;
        }

        public String getPvalue_en() {
            return pvalue_en;
        }

        public void setPvalue_en(String pvalue_en) {
            this.pvalue_en = pvalue_en;
        }

        @Override public String toString() {
            return "SkuPropImg{" + "url='" + url + '\'' + ", pname='" + pname + '\'' + ", pname_en='" + pname_en + '\'' + ", pvalue='" + pvalue + '\''
                    + ", pvalue_en='" + pvalue_en + '\'' + ", index=" + index + '}';
        }
    }

    public static class Prop {

        private String pname;

        private String pname_en;

        private String pvalue;

        private String pvalue_en;

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPname_en() {
            return pname_en;
        }

        public void setPname_en(String pname_en) {
            this.pname_en = pname_en;
        }

        public String getPvalue() {
            return pvalue;
        }

        public void setPvalue(String pvalue) {
            this.pvalue = pvalue;
        }

        public String getPvalue_en() {
            return pvalue_en;
        }

        public void setPvalue_en(String pvalue_en) {
            this.pvalue_en = pvalue_en;
        }

        @Override
        public String toString() {
            return "Prop{" + "pname='" + pname + '\'' + ", pname_en='" + pname_en + '\'' + ", pvalue='" + pvalue + '\'' + ", pvalue_en='" + pvalue_en + '\''
                    + '}';
        }
    }

    public static class Sku {
        private String outercode;
        private String barcode;
        private BigDecimal price;
        private BigDecimal originalprice;
        private Integer quantity;
        private Prop[] props;

        public String getOutercode() {
            return outercode;
        }

        public void setOutercode(String outercode) {
            this.outercode = outercode;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Prop[] getProps() {
            return props;
        }

        public void setProps(Prop[] props) {
            this.props = props;
        }

        public BigDecimal getOriginalprice() {
            return originalprice;
        }

        public void setOriginalprice(BigDecimal originalprice) {
            this.originalprice = originalprice;
        }

        @Override
        public String toString() {
            return "Sku{" + "outercode='" + outercode + '\'' + ", barcode='" + barcode + '\'' + ", price=" + price + ", originalprice=" + originalprice
                    + ", quantity=" + quantity + ", props=" + props + '}';
        }
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer[] getTags() {
        return tags;
    }

    public void setTags(Integer[] tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOutercode() {
        return outercode;
    }

    public void setOutercode(String outercode) {
        this.outercode = outercode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSellpoint() {
        return sellpoint;
    }

    public void setSellpoint(String sellpoint) {
        this.sellpoint = sellpoint;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWapdesc() {
        return wapdesc;
    }

    public void setWapdesc(String wapdesc) {
        this.wapdesc = wapdesc;
    }

    public ItemImgs[] getItemimgs() {
        return itemimgs;
    }

    public void setItemimgs(ItemImgs[] itemimgs) {
        this.itemimgs = itemimgs;
    }

    public SkuPropImg[] getPropimgs() {
        return propimgs;
    }

    public void setPropimgs(SkuPropImg[] propimgs) {
        this.propimgs = propimgs;
    }

    public Prop[] getProps() {
        return props;
    }

    public void setProps(Prop[] props) {
        this.props = props;
    }

    public Sku[] getSkus() {
        return skus;
    }

    public void setSkus(Sku[] skus) {
        this.skus = skus;
    }

    @Override
    public String toString() {
        return "AddGoodsReqDto{" + "cid='" + cid + '\'' + ", cname='" + cname + '\'' + ", tags=" + Arrays.toString(tags) + ", title='" + title + '\''
                + ", barcode='" + barcode + '\'' + ", outercode='" + outercode + '\'' + ", status='" + status + '\'' + ", weight=" + weight + ", originalprice="
                + originalprice + ", price=" + price + ", quantity=" + quantity + ", sellpoint='" + sellpoint + '\'' + ", desc='" + desc + '\'' + ", wapdesc='"
                + wapdesc + '\'' + ", itemimgs=" + Arrays.toString(itemimgs) + ", propimgs=" + Arrays.toString(propimgs) + ", props=" + Arrays.toString(props)
                + ", skus=" + Arrays.toString(skus) + '}';
    }
}
