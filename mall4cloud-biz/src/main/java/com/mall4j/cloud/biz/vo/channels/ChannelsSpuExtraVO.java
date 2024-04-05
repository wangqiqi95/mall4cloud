package com.mall4j.cloud.biz.vo.channels;

import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频号商品-附带额外信息
 * @date 2023/3/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelsSpuExtraVO extends ChannelsSpu {
    @ApiModelProperty("市场价，整数方式保存")
    private Long marketPriceFee;
}
