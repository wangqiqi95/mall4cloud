package com.mall4j.cloud.distribution.constant;

/**
 * @Author ZengFanChang
 * @Date 2021/12/11
 */
public enum WithdrawProcessExportError {

    /**
     * 成功数量
     */
    SUCCESS_QUANTITY(1, "成功数量"),

    /**
     * 失败数量
     */
    FAIL_QUANTITY(2, "失败数量"),

    ;

    private final Integer value;

    private final String errorInfo;

    public Integer value(){return value;}

    public String errorInfo(){return errorInfo;}

    WithdrawProcessExportError(Integer value,String errorInfo){
        this.value = value;
        this.errorInfo = errorInfo;
    }

    public static WithdrawProcessExportError instance(Integer value){
        WithdrawProcessExportError[] enums = values();
        for (WithdrawProcessExportError withdrawProcessExportError : enums) {
            if (withdrawProcessExportError.value().equals(value)){
                return withdrawProcessExportError;
            }
        }
        return null;
    }

}
