

package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 微信直播商品响应参数
 * @author yami
 */
@Data
public class WxLiveProdResponse<T> implements Serializable {


    private int errcode;

    private Integer total;

    private String msg;

    private T goods;

    private Long roomId;

    public boolean isSuccess(){
        return Objects.equals(0, this.errcode);
    }


}
