package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 素材表DTO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
public class MaterialPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long storeId;

	//部门ids
	private List<Long> orgIds;

	private List<String> ids;

	@ApiModelProperty("素材名称")
    private String matName;

    @ApiModelProperty("素材分类id")
    private Integer matType;

	@ApiModelProperty("状态")
	private Integer  status;

	@ApiModelProperty("开始时间")
	private String  createTimeStart;

	@ApiModelProperty("结束时间")
	private String  createTimeEnd;

	@ApiModelProperty("消息类型 text:文本,image:图片,video:视频,miniprogram:小程序 h5：H5页面 文件：file 文章：article")
	private String type;

	@ApiModelProperty("是否筛选正常素材(未过期/已启用)：1-是")
	private Integer selectNormal;
}
