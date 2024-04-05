package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 客户标签配置表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签ID")
    private Long id;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("所属标签组")
    private Integer groupId;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

    @ApiModelProperty("是否被选中 1 选中 0 未选中")
    private Integer isSelect;

}
