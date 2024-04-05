package com.mall4j.cloud.biz.vo.channels;

import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.common.product.vo.SpuVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频号商品内部系统商品信息
 * @date 2023/3/20
 */
@Data
@ApiModel("视频号商品内部系统商品信息")
public class ChannelsSpuProductSpuVO {

    @ApiModelProperty("视频号商品信息")
    private ChannelsSpu channelsSpu;

    @ApiModelProperty("内部商品信息")
    private SpuVO spuVO;
}
