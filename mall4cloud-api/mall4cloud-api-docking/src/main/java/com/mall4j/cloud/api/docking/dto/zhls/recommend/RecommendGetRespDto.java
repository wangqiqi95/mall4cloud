package com.mall4j.cloud.api.docking.dto.zhls.recommend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 推荐结果返回
 * @Author axin
 * @Date 2023-03-09 14:53
 **/
@Data
public class RecommendGetRespDto {
    @ApiModelProperty("每个商品具体的算法公共信息，随着技术迭代，参数可能会增加，请将整个common_outer_service跟随每个具体商品透传上报即可，这样不用因为具体字段的加减重新调试接口")
    private CommonOuterService common_outer_service;

    @ApiModelProperty("商品列表")
    private List<item> item_list;

    @Data
    public static class CommonOuterService {
        @ApiModelProperty("推荐服务")
        private RecommendService recommend_service;
    }

    @Data
    public static class item {
        @ApiModelProperty("返回商品的sku_id或spu_id，如果是sku维度则返回skuid，如果是spu维度则返回spuid")
        private String item_id;

        @ApiModelProperty("每个商品具体的算法策略信息，随着技术迭代，参数可能会增加，请将整个outer_service跟随每个具体商品透传上报即可，这样不用因为具体字段的加减重新")
        private OuterService outer_service;
    }

    @Data
    public static class RecommendService {
        @ApiModelProperty("标记同一个分页查询")
        private String sequence_id;

        @ApiModelProperty("请求标识")
        private String request_id;

        @ApiModelProperty("推荐场景标志")
        private Long channel_id;

        @ApiModelProperty("")
        private Integer alg_type;
    }

    @Data
    public static class OuterService {
        @ApiModelProperty("推荐服务")
        private OuterRecommendService recommend_service;
    }

    @Data
    public static class OuterRecommendService {
        @ApiModelProperty("返回列表的商品位置")
        private String position_id;
    }
}
