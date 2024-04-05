package com.mall4j.cloud.api.product.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.BrandLangVO;
import com.mall4j.cloud.common.product.vo.SpuLangVO;
import com.mall4j.cloud.common.product.vo.SpuSkuAttrValueVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * spu信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SoldSpuExcelVO extends ExcelModel {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "商品信息";
	public static final String SHEET_NAME = "商品";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};


	//spu
	@ExcelProperty(value = {"商品名称"}, index = 0)
	private String spuName;
	@ExcelProperty(value = {"商品货号"}, index = 1)
	private String spuCode;
	@ExcelProperty(value = {"商品卖点"}, index = 2)
	private String sellingPoint;


	//sku
	@ExcelProperty(value = {"销售属性"}, index = 3)
	private String properties;
	@ExcelProperty(value = {"销售属性值"}, index = 4)
	private String propertiesValues;
	@ExcelProperty(value = {"SKU编码(款+色)"}, index = 5)
	private String priceCode;
	@ExcelProperty(value = {"商品条形码"}, index = 6)
	private String skuCode;
	@ExcelProperty(value = {"外部编码"}, index = 7)
	private String modelId;
	@ExcelProperty(value = {"商品分类"}, index = 8)
	private String platformCategory;
	@ExcelProperty(value = {"商品导航"}, index = 9)
	private String shopCategory;
	@ExcelProperty(value = {"吊牌价"}, index = 10)
	private String marketPriceFee;
	@ExcelProperty(value = {"当前售价"}, index = 11)
	private String priceFee;
	@ExcelProperty(value = {"小程序库存"}, index = 12)
	private Integer stock;
	@ExcelProperty(value = {"小程序销量"}, index = 13)
	private Integer saleNum;
	@ExcelProperty(value = {"所属门店"}, index = 14)
	private String storeName;
	@ExcelProperty(value = {"所属门店编码"}, index = 15)
	private String storeCode;
	@ExcelProperty(value = {"小程序路径"}, index = 16)
	private String wxmapath;
	@ExcelProperty(value = {"商品状态"}, index = 17)
	private String status;
	@ExcelProperty(value = {"运费模板"}, index = 18)
	private String deliveryTemplate;
	@ExcelProperty(value = {"创建时间"}, index = 19)
	@DateTimeFormat("yyyy-MM-dd")
	private Date createTime;
	@ExcelProperty(value = {"上架时间"}, index = 20)
//	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat("yyyy-MM-dd")
	private Date updateTime;



	//	@ExcelProperty(value = {"商品编码"}, index = 0)
//	private String spuCode;
	@ExcelIgnore
	private String deliveryMode;
//	@ExcelProperty(value = {"商品sku名称"}, index = 8)
//	private String skuName;
//	@ExcelProperty(value = {"原价"}, index = 12)
//	private String priceFee;
//	@ExcelProperty(value = {"活动价"}, index = 13)
//	private String marketPriceFee;
//	@ExcelProperty(value = {"库存"}, index = 14)
//	private Integer stock;
//	@ExcelProperty(value = {"销量"}, index = 15)
//	private Integer saleNum;
	@ExcelIgnore
	private String seq;
	@ExcelIgnore
	private Long spuId;
	@ExcelIgnore
	private Long deliveryTemplateId;
	@ExcelIgnore
	private Long categoryId;
	@ExcelIgnore
	private Long shopCategoryId;
	@ExcelIgnore
	private String skucodeStatus;

}
