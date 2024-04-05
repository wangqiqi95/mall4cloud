package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Grady_Lu
 */
@Data
public class ScoreProductStockSynLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value="id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品spuId")
    private Long spuId;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
