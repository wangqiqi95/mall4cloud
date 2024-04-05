package com.mall4j.cloud.payment.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExportUserToCreateConfigVO {
	
	@ExcelProperty(value = {"会员手机号"},index = 0)
	private String phone;
	
}
