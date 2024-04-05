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
 * 种草信息用户收藏
 *
 * @author cg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendCollect extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long recommendCollectId;

    /**
     * 种草id
     */
    private Long recommendId;

    /**
     * 用户uid
     */
    private Long userId;

    /**
     * 取消收藏：0-否 |1-是
     */
    private Integer cancel;

}
