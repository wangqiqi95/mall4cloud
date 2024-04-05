

package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ServerResponse<T> implements Serializable {


    private int code;

    private String msg;

    private T obj;

    public boolean isSuccess(){
        return Objects.equals(-1, this.code);
    }


}
