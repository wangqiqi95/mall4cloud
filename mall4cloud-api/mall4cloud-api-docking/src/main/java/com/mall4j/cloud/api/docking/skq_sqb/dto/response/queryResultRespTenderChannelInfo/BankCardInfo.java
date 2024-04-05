package com.mall4j.cloud.api.docking.skq_sqb.dto.response.queryResultRespTenderChannelInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ty
 * @ClassName BankCardInfo
 * @description
 * @date 2023/5/8 15:23
 */
@Data
public class BankCardInfo {

    /**
     * 凭证号
     */
    @JsonProperty("trace_no")
    private String traceNo;

    /**
     * 批次号
     */
    @JsonProperty("batch_no")
    private String batchNo;

    /**
     * 系统参考号
     */
    @JsonProperty("ref_no")
    private String refNo;

    /**
     * 授权码
     */
    @JsonProperty("auth_no")
    private String authNo;

    /**
     * 发卡行号
     */
    @JsonProperty("issuer_no")
    private String issuerNo;

    /**
     * 发卡行名称
     */
    @JsonProperty("issuer_name")
    private String issuerName;

    /**
     * 卡号（卡号中段为“*”号，已加密）
     */
    @JsonProperty("card_no")
    private String cardNo;

    /**
     * 借贷记卡标识
     * 0：借记；1：贷记
     */
    @JsonProperty("card_type_identity")
    private String cardTypeIdentity;

    /**
     * 内外卡标识
     * 0：内卡；1：外卡
     */
    @JsonProperty("abroad_card_type")
    private String abroadCardType;

}
