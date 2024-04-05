

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
public class LiveUsableNumParam implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 店铺可用次数
     */
    private Integer shopNum;

    /**
     * 平台可用次数
     */
    private Integer totalNum;

    /**
     * 店铺可用次数
     */
    private Integer deleteShopNum;

    /**
     * 平台可用次数
     */
    private Integer deleteTotalNum;

}
