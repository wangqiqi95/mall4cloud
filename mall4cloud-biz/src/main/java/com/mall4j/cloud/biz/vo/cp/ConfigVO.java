package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 企业微信配置表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("企业微信ID")
    private String cpId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("Token")
    private String token;

    @ApiModelProperty("EncodingAESKey")
    private String encodingaeskey;

    @ApiModelProperty("外部联系Secret")
    private String externalSecret;

    @ApiModelProperty("通讯录Secret")
    private String connetSecret;

    @ApiModelProperty("企业微信Secret")
    private String secret;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;


	@Override
	public String toString() {
		return "ConfigVO{" +
				"cpId=" + cpId +
				",companyName=" + companyName +
				",token=" + token +
				",encodingaeskey=" + encodingaeskey +
				",externalSecret=" + externalSecret +
				",connetSecret=" + connetSecret +
				",secret=" + secret +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
