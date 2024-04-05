package com.mall4j.cloud.api.order.constant;

/**
 * 收钱吧订单状态
 *     0：已取消，1：待操作，2：操作中，3：等待结果中，4：操作完成，5：部分完成，6：操作失败，7：已终止
 *
 */
public enum SQBOrderStatus {
	
	CANCELED("0","已取消"),
	
	WAIT_OPERATION("1","待操作"),
	
	IN_OPERATION("2","操作中"),
	
	WAIT_RESULT("3","等待结果中"),
	
	SUCCESS("4","操作完成"),
	
	PART_SUCCESS("5","部分完成"),
	
	FAIL("6","操作失败"),
	
	TERMINATED("7","已终止")
	
	;
	
	
	private final String value;
	
	private final String desc;
	
	public String value() {
		return value;
	}
	
	public String desc() {
		return desc;
	}
	
	SQBOrderStatus(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static SQBOrderStatus instance(String value) {
		SQBOrderStatus[] enums = values();
		for (SQBOrderStatus statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
