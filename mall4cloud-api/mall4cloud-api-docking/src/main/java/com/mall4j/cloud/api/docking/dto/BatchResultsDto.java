package com.mall4j.cloud.api.docking.dto;

import io.swagger.annotations.ApiModelProperty;

public class BatchResultsDto {
    @ApiModelProperty("错误码")
    private String errorCode;

    @ApiModelProperty("请求序列下标")
    private Integer index;

    @ApiModelProperty("消息")
    private String message;

    @ApiModelProperty("当前商品插入成功与否标识")
    private Boolean success;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BatchResultsDto{" + "errorCode='" + errorCode + '\'' + ", index=" + index + ", message='" + message + '\'' + ", success=" + success + '}';
    }
}
