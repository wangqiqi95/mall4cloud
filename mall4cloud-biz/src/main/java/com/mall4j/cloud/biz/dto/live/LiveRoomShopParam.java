package com.mall4j.cloud.biz.dto.live;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cg666
 */
@Data
public class LiveRoomShopParam {

    @ApiModelProperty(value = "直播间id", required = true)
    private Long roomId;

    @ApiModelProperty(value = "是否关联所有开播门店：0-否，1-是", required = true)
    private Integer isAllShop;

    @ApiModelProperty(value = "开播门店id数组-当isAllShop=1时 可不传")
    @TableField(exist = false)
    private List<Long> shopIds;

    @ApiModelProperty(value = "是否关联所有分销门店：0-否，1-是", required = true)
    private Integer isAllSaleShop;

    @ApiModelProperty(value = "分销门店id数组-isAllSaleShop=1时 可不传")
    @TableField(exist = false)
    private List<Long> saleShopIds;
}
