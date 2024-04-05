package com.mall4j.cloud.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.EcAttrInfo;
import com.mall4j.cloud.biz.dto.channels.ChannelsSkuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddChannlesSpuRequest {

    @ApiModelProperty(value = "id,编辑时传入")
    private Long id;

    @ApiModelProperty(value = "运费模板id")
    private String freightTemplate;

    @ApiModelProperty(value = "发货方式，若为无需快递（仅对部分类目开放），则无需填写运费模版id。0:快递发货；1:无需快递；默认0")
    private Integer deliverMethod;

    @ApiModelProperty(value = "类目ID")
    private String cats;

    @ApiModelProperty(value = "品牌")
    private Long brandId;

    @ApiModelProperty(value = "小程序商品id")
    private Long spuId;

    @ApiModelProperty(value = "视频号商品id",hidden = true)
    private Long outSpuId;

    @ApiModelProperty(value = "同步到视频号的sku")
    private List<ChannelsSkuDTO> skus;

    @ApiModelProperty(value = "商品参数")
    private List<EcAttrInfo> attrs;

    @ApiModelProperty(value = "是否是新增，影响保存并上架接口")
    private Boolean isAdd;

    @ApiModelProperty(hidden = true)
    /**
     * 是否快速上架（更新完直接上架）
     */
    private Boolean isQuestListing = Boolean.FALSE;
}
