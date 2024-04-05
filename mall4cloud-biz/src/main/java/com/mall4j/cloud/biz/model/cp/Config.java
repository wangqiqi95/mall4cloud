package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class Config extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 企业微信ID
     */
    private String cpId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * Token
     */
    private String token;

    /**
     * EncodingAESKey
     */
    private String EncodingAesKey;

    /**
     * 外部联系Secret
     */
    private String externalSecret;

    /**
     * 通讯录Secret
     */
    private String connetSecret;

    /**
     * 企业微信Secret
     */
    private String secret;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;

    /**
     *
     */
    private String connectToken;

    /**
     *
     */
    private String connectAesKey;

    /**
     *
     */
    private String externalToken;

    /**
     *
     */
    private String externalAesKey;

    /**
     *
     */
    private Integer agentId;

    /**
     *
     */
    private String agentSecret;

    /**
     *
     */
    private String minAppId;

    /**
     *
     */
    private String picUrl;

    /**
     *
     */
    private String auth2RedirectUri;

    /**
     * 会话存档token
     */
    private String sessionToken;

    /**
     * 会话存档加密key
     */
    private String sessionEncodingAesKey;

    /**
     * 会话存档secret
     */
    private String sessionSecret;

    /**
     * 会话存档加密公钥
     */
    private String sessionResPublicKey;

    /**
     * 会话存档加密私钥
     */
    private String sessionResPrivateKey;

    /**
     * 会话存档企业id
     */
    private String sessionCpId;

    /**
     * 会话存档token超时时间
     */
    private Long sessionLoseTime;

    /**
     * 会话同意token
     */
    private String sessionAgreeToken;

    private String createBy;

    private String updateBy;

}
