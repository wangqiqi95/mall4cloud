

package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author lhd
 * @date 2020-08-12 16:05:26
 */
@Data
public class ReturnLiveParam implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 是否实名认证 0.没有
     */
    private Integer isRealName;

    /**
     * 认证二维码
     */
    private String img;


}
