package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.product.dto.SpuSearchAttrDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * spu信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuPageSearchDTO extends PageDTO {

	@ApiModelProperty("spuId")
	private Long spuId;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("品牌ID")
	private Long brandId;

	@ApiModelProperty(value = "商家一级分类id")
	private Long shopPrimaryCategoryId;

	@ApiModelProperty(value = "商家二级分类id")
	private Long shopSecondaryCategoryId;

	@ApiModelProperty(value = "C端分类")
	private Long shopCategoryId;

	@ApiModelProperty(value = "平台一级分类id")
	private Long primaryCategoryId;

	@ApiModelProperty(value = "平台二级分类id")
	private Long secondaryCategoryId;

	@ApiModelProperty("分类ID")
	private Long categoryId;

	@ApiModelProperty("分类导航ID")
	private Long categoryNavigationId;

	@ApiModelProperty("spu名称")
	private String name;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
	private Integer status;

	@ApiModelProperty("商品类型(0普通商品 1拼团 2秒杀 3积分)")
	private Integer spuType;

	@ApiModelProperty("是否为组合商品0普通商品，1组合商品")
	private Integer isCompose;

	@ApiModelProperty("spuId列表(商品上下架：批量操作时，用此参数)(批量处理参数)")
	private List<Long> spuIds;

	@ApiModelProperty("商品状态： 0.全部  1.销售中  2.已售罄  3.已下架 ")
	private Integer spuStatus;

	@ApiModelProperty("最低价")
	private Long minPrice;

	@ApiModelProperty("最高价")
	private Long maxPrice;

	@ApiModelProperty("最低销量")
	private Long minSaleNum;

	@ApiModelProperty("最高销量")
	private Long maxSaleNum;

	@ApiModelProperty("商品编码")
	private String partyCode;

	@ApiModelProperty("当前价排序 0：倒序 1：顺序")
	private Integer priceFeeSort;

	@ApiModelProperty("市场价排序 0：倒序 1：顺序")
	private Integer marketPriceFeeSort;

	@ApiModelProperty("销量排序 0：倒序 1：顺序")
	private Integer saleNumSort;

	@ApiModelProperty("库存排序 0：倒序 1：顺序")
	private Integer stockSort;

	@ApiModelProperty("序号排序 0：倒序 1：顺序")
	private Integer seqSort;

	@ApiModelProperty("商品id排序 0:无效 1:有效(商品组件排序，其他排序都不生效)")
	private Integer spuIdsSort=0;

	@ApiModelProperty("创建时间排序 0：倒序 1：顺序")
	private Integer createTimeSort;

	@ApiModelProperty("商品排除id")
	private List<Long> spuIdsExclude;

	@ApiModelProperty("excel商品spuCode集合")
	private List<String> spuCodeExcelList;

	@ApiModelProperty("是否爱普货: 0否 1是")
	private Integer iphStatus;

	@ApiModelProperty(value = "门店ID")
	private Long storeId;

	private String spuCode;

	private String skuCode;

	private String keyword;
	@ApiModelProperty("标签id")
	private Long tagId;

	@ApiModelProperty("活动筛选类型(多选逗号分隔): 4限时调价 5拼团 7会员日")
	private String searchActivityType;

	@ApiModelProperty("市场价筛选：最高价(单位分)")
	private Long marketPriceFeeStart;
	@ApiModelProperty("市场价筛选：最低价(单位分)")
	private Long marketPriceFeeEnd;

	@ApiModelProperty("属性筛选")
	private List<String> attrValues;


	@ApiModelProperty("自定义单页最大条数")
	private Integer cusPageSize;

	@ApiModelProperty("spu规格属性搜索")
	private SpuSearchAttrDTO spuSearchAttr;

	@ApiModelProperty("规格属性筛选spuid")
	private Set<Long> attrFilterSpuIds;

	/**
	 * mapper查询用参数
	 */
	@ApiModelProperty(hidden = true)
	private List<Long> shopCategoryNavigationIdList;
}
