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
import java.util.Date;

/**
 * 种草信息分类
 *
 * @author cg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendCate extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long recommendCateId;

    /**
     * 创建人uid
     */
    private Long userId;

    /**
     * 创建人name
     */
    private String userName;

    /**
     * 分类name
     */
    private String name;

    /**
     * 是否显示：0-否 | 1-是
     */
    private Integer isShow;

    /**
     * 是否默认：0-否 | 1-是
     */
    private Integer isDefault;

    /**
     * 种草数量
     */
    private Integer recommendCount;

    /**
     * 封面
     */
    private String coverUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除：0-否 |1-是
     */
    private Integer disable;

}
