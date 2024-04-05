package com.mall4j.cloud.common.product.vo.search;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品分组信息
 *
 * @author YXF
 * @date 2021-06-18 15:27:24
 */
public class SearchSpuTagVO {

    @ApiModelProperty(value = "分组id")
    private Long tagId;

    @ApiModelProperty(value = "序号")
    private Integer seq;


    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "SpuTagVO{" +
                "tagId=" + tagId +
                ", seq=" + seq +
                '}';
    }
}
