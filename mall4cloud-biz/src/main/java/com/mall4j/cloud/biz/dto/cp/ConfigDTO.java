package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 企业微信配置表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class ConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("企业微信ID")
    @NotEmpty(message = "企业微信ID不能为空")
    private String cpId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("Token")
    private String token;

    @ApiModelProperty("通讯录Secret")
    private String secret;

    @ApiModelProperty("EncodingAESKey")
    private String encodingaeskey;

    @ApiModelProperty("外部联系TOKEN")
    private String externalToken;

    @ApiModelProperty("外部联系Secret")
    private String externalSecret;

    @ApiModelProperty("外部联系AesKey")
    private String externalAesKey;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

    @ApiModelProperty("agentId")
    private Long agentId;

    @ApiModelProperty("agentSecret")
    private String agentSecret;

    @ApiModelProperty("会话存档token")
    private String sessionToken;

    @ApiModelProperty("会话存档加密key")
    private String sessionEncodingAesKey;

    @ApiModelProperty("会话存档secret")
    private String sessionSecret;
}
