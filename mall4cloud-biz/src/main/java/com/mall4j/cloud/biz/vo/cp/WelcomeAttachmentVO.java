package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 欢迎语附件列表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeAttachmentVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("欢迎语主表id")
    private Long welId;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("JSON数据")
    private String data;

    @ApiModelProperty("状态 1 有效 0 无效")
    private Integer status;

    @ApiModelProperty("删除标识  1 删除 0 未删除")
    private Integer flag;


	@Override
	public String toString() {
		return "WelcomeAttachmentVO{" +
				"id=" + id +
				",welId=" + welId +
				",type=" + type +
				",data=" + data +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
