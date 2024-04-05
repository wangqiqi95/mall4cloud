package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 导购标签信息VO
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
@Data
public class UserLabelVO extends BaseVO{

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("标签名称")
    private String labelName;

    @ApiModelProperty("标签类型 1手动 2系统")
    private Integer labelType;

    @ApiModelProperty("系统标签ID")
    private Long tagId;

    @ApiModelProperty("会员数量")
	private int userNum;
}
