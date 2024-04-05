package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 种草信息相关资源
 *
 * @author cg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResource extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long recommendResourceId;

    /**
     * 种草id
     */
    private Long recommendId;

    /**
     * 资源类型：1-图片 | 2-视频
     */
    private Integer type;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 是否删除：0-否 |1-是
     */
    private Integer disable;
}
