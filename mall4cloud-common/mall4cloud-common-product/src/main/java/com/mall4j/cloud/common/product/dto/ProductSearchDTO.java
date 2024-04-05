package com.mall4j.cloud.common.product.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author FrozenWatermelon
 * @date 2020/11/16
 */
@Data
public class ProductSearchDTO {

    @ApiModelProperty(value = "页面传递过来的全文匹配关键字")
    private String keyword;

    @ApiModelProperty(value = "品牌id,可以多选")
    private String brandIds;

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "商家一级分类id")
    private Long shopPrimaryCategoryId;

    @ApiModelProperty(value = "商家二级分类id")
    private Long shopSecondaryCategoryId;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty(value = "平台三级分类id")
    private Long categoryId;

    @ApiModelProperty(value = "商家三级分类id")
    private Long shopCategoryId;

    /**
     * 参考EsProductSortEnum
     */
    @ApiModelProperty(value = "排序：1新品,2销量倒序,3销量正序,4商品价格倒序,5商品价格正序,6评论")
    private Integer sort;

    @ApiModelProperty(value = "自营店 1：自营店 2：非自营店")
    private Integer selfShop;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer spuType;

    @ApiModelProperty(value = "状态 -1:删除, 0:下架, 1:上架, 2:平台下架, 3: 等待审核")
    private Integer status;

    @ApiModelProperty("是否爱普货: 0否 1是")
    private Integer iphStatus;

    @ApiModelProperty("所选批次(0-23，一个数字代表一个时间段,每个时间段两小时，共24个)")
    private Integer selectedLot;

    @ApiModelProperty("秒杀分类id")
    private Integer seckillCategoryId;

    @ApiModelProperty(value = "是否显示有货")
    private Integer hasStock;

    @ApiModelProperty(value = "价格区间查询-最低价")
    private Long minPrice;

    @ApiModelProperty(value = "价格区间查询-最高价")
    private Long maxPrice;

    @ApiModelProperty(value = "销量区间查询-最低销量")
    private Long minSaleNum;

    @ApiModelProperty(value = "销量区间查询-最高销量")
    private Long maxSaleNum;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺id列表")
    private List<Long> shopIds;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品状态")
    private Integer spuStatus;

    @ApiModelProperty(value = "属性值ids(属性之间用^拼接；属性于属性值id用_拼接；多个属性值id间用,拼接)")
    private String attrIds;

    @ApiModelProperty(value = "spuId列表")
    private List<Long> spuIds;

    @ApiModelProperty(value = "不为该spuId列表")
    private List<Long> spuIdsExclude;

    @ApiModelProperty(value = "spu编码")
    private List<String> spuCodeList;

    @ApiModelProperty(value = "分组id")
    private Long tagId;

    @ApiModelProperty(value = "活动id(关联prod_type)")
    private Long activityId;

    @ApiModelProperty(value = "是否为组合商品   0:普通商品，1:组合商品")
    private Integer isCompose;

    @ApiModelProperty(value = "是否需要活动信息  1:需要  0:不需要")
    private Integer needActivity;

    @ApiModelProperty(value = "商品编码列表（逗号分隔）")
    private String partyCodes;

    @ApiModelProperty(value = "商品编码列表（逗号分隔）-新加")
    private String spuCodes;

    @ApiModelProperty(value = "商品条形码列表（逗号分隔）")
    private String modelIds;

    @ApiModelProperty(value = "0.全部  1.销售中  2.已售罄  3.已下架")
    private Integer dataType;

    @ApiModelProperty(value = "活动时间")
    private Long activityTime;

    @ApiModelProperty(value = "是否属于分销商品, true: 是 false: 否")
    private Boolean distributionSpu;

    @ApiModelProperty(value = "分销状态(0:商家下架 1:商家上架 2:违规下架 3:平台审核)")
    private Integer distributionState;

    @ApiModelProperty(value = "奖励方式(0 按比例 1 按固定数值)")
    private Integer awardMode;

    @ApiModelProperty("活动筛选类型(多选逗号分隔): 1限时调价 2会员日 3拼团")
    private String searchActivityType;

    @ApiModelProperty("市场价筛选：最高价(单位分)")
    private Long marketPriceFeeStart;
    @ApiModelProperty("市场价筛选：最低价(单位分)")
    private Long marketPriceFeeEnd;

    @ApiModelProperty("属性筛选")
    private List<String> attrValues;

    @ApiModelProperty("中台分类")
    private List<String> erpCategorys;

    /**
     * 非该平台一级分类id
     */
    private Long notPrimaryCategoryId;

    /**
     * 非商品模板使用
     */
    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    /**
     * 非商品模板使用
     */
    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    /**
     * 0.非用户端搜索，1:用户端搜索
     */
    private Boolean appDisplay;

    /**
     * 响应数据字段数组
     */
    private String[] fetchSource;

    /**
     * 搜索属性信息
     */
    private Map<String, List<String>> attrMap;
    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("门店id")
    private String erpCategory;

    @ApiModelProperty("spu规格属性搜索")
    private SpuSearchAttrDTO spuSearchAttr;

    @ApiModelProperty("规格属性筛选spuid")
    private Set<Long> attrFilterSpuIds;

    public String getSpuCodes() {
        return spuCodes;
    }

    public void setSpuCodes(String spuCodes) {
        this.spuCodes = spuCodes;
    }

    public Integer getDistributionState() {
        return distributionState;
    }

    public void setDistributionState(Integer distributionState) {
        this.distributionState = distributionState;
    }

    public Integer getAwardMode() {
        return awardMode;
    }

    public void setAwardMode(Integer awardMode) {
        this.awardMode = awardMode;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public Boolean getDistributionSpu() {
        return distributionSpu;
    }

    public void setDistributionSpu(Boolean distributionSpu) {
        this.distributionSpu = distributionSpu;
    }

    public Long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Long activityTime) {
        this.activityTime = activityTime;
    }

    public Integer getSeckillCategoryId() {
        return seckillCategoryId;
    }

    public void setSeckillCategoryId(Integer seckillCategoryId) {
        this.seckillCategoryId = seckillCategoryId;
    }

    public Integer getSelectedLot() {
        return selectedLot;
    }

    public void setSelectedLot(Integer selectedLot) {
        this.selectedLot = selectedLot;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getShopPrimaryCategoryId() {
        return shopPrimaryCategoryId;
    }

    public void setShopPrimaryCategoryId(Long shopPrimaryCategoryId) {
        this.shopPrimaryCategoryId = shopPrimaryCategoryId;
    }

    public Long getShopSecondaryCategoryId() {
        return shopSecondaryCategoryId;
    }

    public void setShopSecondaryCategoryId(Long shopSecondaryCategoryId) {
        this.shopSecondaryCategoryId = shopSecondaryCategoryId;
    }

    public Long getPrimaryCategoryId() {
        return primaryCategoryId;
    }

    public void setPrimaryCategoryId(Long primaryCategoryId) {
        this.primaryCategoryId = primaryCategoryId;
    }

    public Long getNotPrimaryCategoryId() {
        return notPrimaryCategoryId;
    }

    public void setNotPrimaryCategoryId(Long notPrimaryCategoryId) {
        this.notPrimaryCategoryId = notPrimaryCategoryId;
    }

    public Long getSecondaryCategoryId() {
        return secondaryCategoryId;
    }

    public void setSecondaryCategoryId(Long secondaryCategoryId) {
        this.secondaryCategoryId = secondaryCategoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSelfShop() {
        return selfShop;
    }

    public void setSelfShop(Integer selfShop) {
        this.selfShop = selfShop;
    }

    public Integer getSpuType() {
        return spuType;
    }

    public void setSpuType(Integer spuType) {
        this.spuType = spuType;
    }

    public Integer getHasStock() {
        return hasStock;
    }

    public void setHasStock(Integer hasStock) {
        this.hasStock = hasStock;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getMinSaleNum() {
        return minSaleNum;
    }

    public void setMinSaleNum(Long minSaleNum) {
        this.minSaleNum = minSaleNum;
    }

    public Long getMaxSaleNum() {
        return maxSaleNum;
    }

    public void setMaxSaleNum(Long maxSaleNum) {
        this.maxSaleNum = maxSaleNum;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getSpuStatus() {
        return spuStatus;
    }

    public void setSpuStatus(Integer spuStatus) {
        this.spuStatus = spuStatus;
    }

    public String getAttrIds() {
        return attrIds;
    }

    public void setAttrIds(String attrIds) {
        this.attrIds = attrIds;
    }

    public List<Long> getSpuIds() {
        return spuIds;
    }

    public void setSpuIds(List<Long> spuIds) {
        this.spuIds = spuIds;
    }

    public List<Long> getSpuIdsExclude() {
        return spuIdsExclude;
    }

    public void setSpuIdsExclude(List<Long> spuIdsExclude) {
        this.spuIdsExclude = spuIdsExclude;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getIsCompose() {
        return isCompose;
    }

    public void setIsCompose(Integer isCompose) {
        this.isCompose = isCompose;
    }

    public Integer getNeedActivity() {
        return needActivity;
    }

    public void setNeedActivity(Integer needActivity) {
        this.needActivity = needActivity;
    }

    public String getPartyCodes() {
        return partyCodes;
    }

    public void setPartyCodes(String partyCodes) {
        this.partyCodes = partyCodes;
    }

    public String getModelIds() {
        return modelIds;
    }

    public void setModelIds(String modelIds) {
        this.modelIds = modelIds;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Map<String, List<String>> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, List<String>> attrMap) {
        this.attrMap = attrMap;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getAppDisplay() {
        return appDisplay;
    }

    public void setAppDisplay(Boolean appDisplay) {
        this.appDisplay = appDisplay;
    }

    public String[] getFetchSource() {
        return fetchSource;
    }

    public void setFetchSource(String[] fetchSource) {
        this.fetchSource = fetchSource;
    }

    public String getSearchActivityType() {
        return searchActivityType;
    }

    public void setSearchActivityType(String searchActivityType) {
        this.searchActivityType = searchActivityType;
    }

    public Long getMarketPriceFeeStart() {
        return marketPriceFeeStart;
    }

    public void setMarketPriceFeeStart(Long marketPriceFeeStart) {
        this.marketPriceFeeStart = marketPriceFeeStart;
    }

    public Long getMarketPriceFeeEnd() {
        return marketPriceFeeEnd;
    }

    public void setMarketPriceFeeEnd(Long marketPriceFeeEnd) {
        this.marketPriceFeeEnd = marketPriceFeeEnd;
    }

    public List<String> getAttrValues() {
        return attrValues;
    }

    public void setAttrValues(List<String> attrValues) {
        this.attrValues = attrValues;
    }

    @Override
    public String toString() {
        return "ProductSearchDTO{" +
                "keyword='" + keyword + '\'' +
                ", brandIds='" + brandIds + '\'' +
                ", spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", shopPrimaryCategoryId=" + shopPrimaryCategoryId +
                ", shopSecondaryCategoryId=" + shopSecondaryCategoryId +
                ", primaryCategoryId=" + primaryCategoryId +
                ", secondaryCategoryId=" + secondaryCategoryId +
                ", categoryId=" + categoryId +
                ", sort=" + sort +
                ", selfShop=" + selfShop +
                ", spuType=" + spuType +
                ", selectedLot=" + selectedLot +
                ", seckillCategoryId=" + seckillCategoryId +
                ", hasStock=" + hasStock +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minSaleNum=" + minSaleNum +
                ", maxSaleNum=" + maxSaleNum +
                ", shopId=" + shopId +
                ", shopIds=" + shopIds +
                ", shopName='" + shopName + '\'' +
                ", spuStatus=" + spuStatus +
                ", attrIds='" + attrIds + '\'' +
                ", spuIds=" + spuIds +
                ", spuIdsExclude=" + spuIdsExclude +
                ", tagId=" + tagId +
                ", activityId=" + activityId +
                ", isCompose=" + isCompose +
                ", needActivity=" + needActivity +
                ", partyCodes='" + partyCodes + '\'' +
                ", modelIds='" + modelIds + '\'' +
                ", dataType=" + dataType +
                ", activityTime=" + activityTime +
                ", distributionSpu=" + distributionSpu +
                ", distributionState=" + distributionState +
                ", awardMode=" + awardMode +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", appDisplay=" + appDisplay +
                ", fetchSource=" + Arrays.toString(fetchSource) +
                ", attrMap=" + attrMap +
                '}';
    }
}
