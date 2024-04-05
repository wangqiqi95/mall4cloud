package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetRespDto;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 有数推荐商品
 * @Author axin
 * @Date 2023-05-08 12:12
 **/
@Data
public class ZhlsCommodityVO {

    @ApiModelProperty("总页数")
    private Integer pages;

    @ApiModelProperty("总条目数")
    private Long total;

    @ApiModelProperty("标记同一个分页查询，翻页请求时带上")
    private String sequenceId;

    private RecommendGetRespDto.CommonOuterService common_outer_service;

    @ApiModelProperty("结果集")
    private List<ZhlsCommodity> list;

    @Data
    public static class ZhlsCommodity extends SpuCommonVO{
        @ApiModelProperty("结果集")
        private RecommendGetRespDto.OuterService outer_service;
    }

}
