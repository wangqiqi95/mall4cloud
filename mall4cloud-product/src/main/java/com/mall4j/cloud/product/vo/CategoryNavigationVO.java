package com.mall4j.cloud.product.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航分类VO
 * @date 2023/6/9
 */
@Data
public class CategoryNavigationVO {

    /**
     * 分类id
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    @NotNull(message = "name not null")
    private String name;

    /**
     * 父ID
     */
    @ApiModelProperty("父类ID")
    @NotNull(message = "parentId not null")
    private Long parentId;

    /**
     * 分类描述
     */
    @ApiModelProperty("分类描述")
    private String desc;

    /**
     * 分类地址{parent_id}-{child_id},...
     */
    @ApiModelProperty("分类地址")
    private String path;

    /**
     * 分类地址字符信息，已转为name
     */
    @ApiModelProperty("分类地址 name形式")
    private String pathStr;

    /**
     * 分类图标
     */
    @ApiModelProperty("分类图标")
    private String icon;

    /**
     * 分类的显示图片
     */
    @ApiModelProperty("分类图片")
    private String imgUrl;

    /**
     * 分类层级 从0开始
     */
    @ApiModelProperty("分类层级")
    @NotNull(message = "level not null")
    private Integer level;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    @NotNull(message = "seq not null")
    private Integer seq;

    /**
     * 是否最后一级
     */
    @ApiModelProperty("是否最后一级")
    @NotNull(message = "isLastLevel not null")
    private Integer isLastLevel;

    /**
     * 状态 1:enable, 0:disable
     */
    @ApiModelProperty("是否启用")
    @NotNull(message = "status not null")
    private Integer status;

    /**
     * 是否下级为末级
     */
    @ApiModelProperty("是否下级为末级")
    private Integer isLastLevelChild = 0;

    @ApiModelProperty("子级")
    private List<CategoryNavigationVO> children;

    public void addChildren(CategoryNavigationVO children) {
        this.children.add(children);
    }
}
