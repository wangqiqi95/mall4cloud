package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.product.model.TagActRelationProd;
import com.mall4j.cloud.product.model.TagActRelationStore;
import com.mall4j.cloud.product.model.TagActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagActivityDetailVO {
    @ApiModelProperty("活动信息")
    private TagActivity tagActivity;
    @ApiModelProperty("关联商店列表")
    private List<TagActRelationStore> stores;
    @ApiModelProperty("关联商品列表")
    private List<TagActRelationProd> prodList;


}
