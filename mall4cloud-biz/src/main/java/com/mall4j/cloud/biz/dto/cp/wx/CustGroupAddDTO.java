package com.mall4j.cloud.biz.dto.cp.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 客户群表DTO
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Data
public class CustGroupAddDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("群名")
    private String groupName;

    @ApiModelProperty("群主名称")
    private String ownerName;

    @ApiModelProperty("建群时间开始")
    private String createTimeStart;

	@ApiModelProperty("建群时间截止")
	private String createTimeEnd;

    @ApiModelProperty("拉群方式：0企微群活码/1自建群活码")
    private Integer groupType;

    @ApiModelProperty("二维码")
    private String qrCode;

    @ApiModelProperty("人数上限")
    private Integer total;


}
