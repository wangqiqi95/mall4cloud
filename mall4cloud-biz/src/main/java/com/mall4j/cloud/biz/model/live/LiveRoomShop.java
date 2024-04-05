

package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhd
 * @date 2020-08-06 16:29:53
 */
@Data
@ApiModel("直播间关联门店")
public class LiveRoomShop implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "直播间id")
    private Long roomId;
}
