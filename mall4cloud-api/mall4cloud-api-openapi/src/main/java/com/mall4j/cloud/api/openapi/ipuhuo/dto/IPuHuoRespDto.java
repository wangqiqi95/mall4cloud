package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：响应Dto
 */
public class IPuHuoRespDto<T> implements Serializable {

    private static final long serialVersionUID = 913504308218709225L;
    @ApiModelProperty("响应码")
    private String code;

    @ApiModelProperty("响应描述")
    private String message;

    @ApiModelProperty("响应数据")
    private T result;

    @ApiModelProperty("商品总数量(这里返回的是符合条件的所有商品的总数，而不是当前页商品的数量，例如总数为51，请求参数中每页为30，则应返回51)")
    private Integer numtotal;

    private static final String succCode = "10000";
    private static final String errorCode = "40000";

    private static final String succMessage = "SUCCESS";

    public IPuHuoRespDto() {
    }

    public IPuHuoRespDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public IPuHuoRespDto(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public IPuHuoRespDto(String code, String message, T result, Integer numtotal) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.numtotal = numtotal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> IPuHuoRespDto<T> success() {
        return new IPuHuoRespDto<>(succCode, succMessage);
    }

    public static <T> IPuHuoRespDto<T> success(T result) {
        return new IPuHuoRespDto<>(succCode, succMessage, result);
    }

    public static <T> IPuHuoRespDto<T> fail() {
        return new IPuHuoRespDto<>(errorCode, "服务异常,请联系管理员");
    }

    public static <T> IPuHuoRespDto<T> fail(String errorMsg) {
        return new IPuHuoRespDto<>(errorCode, errorMsg);
    }

    public static <T> IPuHuoRespDto<T> fail(String errorMsg, T result) {
        return new IPuHuoRespDto<>(errorCode, errorMsg, result);
    }

    public static <T> IPuHuoRespDto<T> page(Integer numtotal, T result) {
        return new IPuHuoRespDto<>(succCode, succMessage, result, numtotal);
    }

    public static <T> IPuHuoRespDto<T> pageFail(Integer numtotal, String errorMsg, T result) {
        return new IPuHuoRespDto<>(errorCode, errorMsg, result, numtotal);
    }

    @Override public String toString() {
        return "IPuHuoRespDto{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", result=" + result + ", numtotal=" + numtotal + '}';
    }
}
