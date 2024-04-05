package com.mall4j.cloud.api.docking.dto.zhls.recommend;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 获取推荐商品入参
 * @Author axin
 * @Date 2023-03-09 14:39
 **/
@Data
public class RecommendGetReqDto {
    @ApiModelProperty(value = "2 - openid",required = true)
    @NotNull(message = "用户类型不能为空")
    private Integer uid_type;

    @ApiModelProperty(value = "小程序传用户的openid",required = true)
    @NotBlank(message = "用户id不能为空")
    private String user_id;

    @ApiModelProperty(value = "推荐场景id(推荐位id)",required = true)
    @NotBlank(message = "推荐场景id不能为空")
    private String channel_id;

    @ApiModelProperty(value = "分页快照标志，第一次为空，后续都是上一次返回数据里的sequence_id，第一次之后必填")
    private String sequence_id;

    @ApiModelProperty(value = "单店版：没有上报external_store_id的话，默认填[0]，多店版：对应external_store_id",required = true)
    private List<String> store_ids= Lists.newArrayList("0");

    @ApiModelProperty(value = "相关性策略类型时必填，传商品的skuid或spuid，我们将为您推荐该商品的相似商品")
    private String item_id;

    @ApiModelProperty(value = "排除的商品ID列表，对这些商品不做推荐")
    private List<String> exclude_item_ids;

    @ApiModelProperty(value = "拉取数据条目，最大支持100",required = true)
    @Max(value = 100,message = "页码超出范围")
    private Integer page_size=10;

    @ApiModelProperty(value = "拉取页码位置 1 开始)",required = true)
    @Min(value = 1,message = "页码超出范围")
    private Integer page_no=1;

    @ApiModelProperty(value = "请求热度推荐（true则返回热度，false则正常返回个性化结果，默认为false）")
    private Boolean req_hot_recommend=false;
}
