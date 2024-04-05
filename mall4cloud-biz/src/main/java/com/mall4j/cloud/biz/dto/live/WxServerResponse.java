

package com.mall4j.cloud.biz.dto.live;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
public class WxServerResponse<T> implements Serializable {


    private int errcode;

    private Integer total;

    private String msg;

    @SerializedName("list")
    private List<LiveUserRespInfo> list;

    private T roomInfo;

    private Long roomId;

    public boolean isSuccess(){
        return Objects.equals(-1, this.errcode);
    }



}
