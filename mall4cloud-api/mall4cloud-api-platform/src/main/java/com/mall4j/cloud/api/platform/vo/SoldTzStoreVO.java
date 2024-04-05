package com.mall4j.cloud.api.platform.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
public class SoldTzStoreVO {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "门店信息";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;

	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};

	@ExcelProperty(value = {"门店编码"}, index = 0)
    private String storeCode;

	@ExcelProperty(value = {"门店名称"}, index = 1)
    private String stationName;

	//门店类型1-自营，2-经销，3-代销，4-电商，5-其他
	@ExcelProperty(value = {"门店类型"}, index = 2)
//    private String type;
    private String storenature;

	//门店状态: 0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败
	@ExcelProperty(value = {"门店状态"}, index = 3)
	private String status;

	//是否虚拟门店：0否 1是
	@ExcelProperty(value = {"虚拟门店"}, index = 4)
	private String storeInviteType;

	@ExcelProperty(value = {"所属店群"}, index = 5)
	private String  shopGroups;

	@ExcelProperty(value = {"所属片区"}, index = 6)
	private String  shopAres;

	//门店位置(省+市+区)
	@ExcelProperty(value = {"门店位置"}, index = 7)
	private String locationAddr;

	@ExcelProperty(value = {"门店地址"}, index = 8)
	private String addr;

	@ExcelProperty(value = {"联系人"}, index = 9)
	private String linkman;

	@ExcelProperty(value = {"联系电话"}, index = 10)
    private String phone;

	@ExcelProperty(value = {"企业微信code"}, index = 11)
	private String qwCode;

	@ExcelProperty(value = {"联系邮箱"}, index = 12)
	private String email;

	@ExcelProperty(value = {"首次开业日期"}, index = 13)
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date openDay;

	@ExcelProperty(value = {"营业时间"}, index = 14)
//	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private String businessTime;

	@ExcelProperty(value = {"备注"}, index = 15)
	private String remarks;

	@ExcelProperty(value = {"是否前端显示"}, index = 16)
	private String isShow;

}
