package com.mall4j.cloud.order.constant;

/**
 * 订单导入错误信息原因
 * @author Pineapple
 * @date 2021/7/20 13:42
 */
public enum OrderExportError {
    /**
     * 订单id
     */
    ORDER_ID(1,"订单ID错误的序号为："),

    /**
     * 配送方式
     */
    DELIVERY_TYPE(2,"配送方式错误的序号为："),

    /**
     * 快递公司名称
     */
    DELIVERY_COMPANY_NAME(3,"快递公司名称错误的序号为："),

    /**
     * 快递单号
     */
    DELIVERY_NO(4,"快递单号错误的序号为："),

    /**
     * 收货人信息[暂无校验]
     */
    CONSIGNEE(5,"收货人信息错误的序号为："),

    /**
     * 已部分发货
     */
    BE_DELIVERY(6,"订单已被部分发货的序号为"),

    /**
     * 其他异常
     */
    OTHER(100,"订单信息错误的序号为："),

    /**
     * 校验表头数据
     */
    EXCEL_ERROR(101,"请导入正确的待发货订单文件")

    ;

    private final Integer value;

    private final String errorInfo;

    public Integer value(){return value;}

    public String errorInfo(){return errorInfo;}

    OrderExportError(Integer value,String errorInfo){
        this.value = value;
        this.errorInfo = errorInfo;
    }

    public static OrderExportError instance(Integer value){
        OrderExportError[] enums = values();
        for (OrderExportError orderExportError : enums) {
            if (orderExportError.value().equals(value)){
                return orderExportError;
            }
        }
        return null;
    }
}
