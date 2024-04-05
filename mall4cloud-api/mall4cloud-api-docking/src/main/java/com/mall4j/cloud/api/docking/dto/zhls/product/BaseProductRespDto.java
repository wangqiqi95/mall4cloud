package com.mall4j.cloud.api.docking.dto.zhls.product;

import lombok.Data;

/**
 * @Description
 * @Author axin
 * @Date 2023-03-09 14:54
 **/
@Data
public class BaseProductRespDto<T> {
    private Integer retcode;
    private String errmsg;
    private T data;

    public boolean isSuccess(){
        return retcode.equals(0);
    }
}
