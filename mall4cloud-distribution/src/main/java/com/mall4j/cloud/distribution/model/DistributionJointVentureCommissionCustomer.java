package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 联营分佣客户
 *
 * @author Zhang Fan
 * @date 2022/8/4 10:44
 */
@Data
public class DistributionJointVentureCommissionCustomer extends BaseModel {

    /**
     * 客户id
     */
    private Long id;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证照片正面
     */
    private String idCardPhotoFront;

    /**
     * 身份证照片背面
     */
    private String idCardPhotoBack;

    /**
     * 地址
     */
    private String address;

    /**
     * 佣金比例
     */
    private Long commissionRate;

    /**
     * 适用门店类型 0-所有门店 1-指定门店
     */
    private Integer limitStoreType;

    /**
     * 门店数量
     */
    private Integer limitStoreNum;

    /**
     * 持卡人姓名
     */
    private String cardholderName;

    /**
     * 银行简称
     */
    private String bankSimpleName;

    /**
     * 支行全称
     */
    private String subbranchName;

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 状态 0禁用 1启用
     */
    private Integer status;

    /**
     * 第三方状态
     */
    private Integer thirdPartyStatus;

    /**
     * 是否删除 0-否 1-是
     */
    private Integer deleted;

    public void validate() {
        if (getThirdPartyStatus() != 1) {
            throw new LuckException("联营分佣客户第三方审核未通过");
        }
        if (StringUtils.isAnyEmpty(getCardholderName(), getBankSimpleName(), getSubbranchName(), getBankCardNo())) {
            throw new LuckException("联营分佣客户银行卡信息不完整");
        }
    }
}
