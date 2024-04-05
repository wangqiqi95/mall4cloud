package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 群活码表VO
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Data
public class GroupCodeRefVO  {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("关联数量")
    private Integer groupTotal;

    @ApiModelProperty("剩余可用群数")
    private Integer enabledGroupTotal;

    @ApiModelProperty("剩余可邀请客户数")
    private Integer enabledCustTotal;

    @ApiModelProperty("群客户数")
    private Integer custTotal;

}
