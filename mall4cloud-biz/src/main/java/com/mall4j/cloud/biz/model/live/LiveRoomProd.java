

package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhd
 * @date 2020-08-05 08:53:17
 */
@Data
@ApiModel("直播商品信息")
public class LiveRoomProd implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间商品信息
     */
    @ApiModelProperty("直播间商品信息")
    @TableId
    private Long roomProdId;

    /**
     * 商品库id
     */
    @ApiModelProperty("商品库id")
    private Long prodStoreId;

    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    private Long roomId;

}
