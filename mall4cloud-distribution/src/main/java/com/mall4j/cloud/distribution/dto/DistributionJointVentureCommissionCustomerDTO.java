package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 联营分佣客户dto
 *
 * @author Zhang Fan
 * @date 2022/8/4 10:52
 */
@ApiModel("联营分佣客户dto")
@Data
public class DistributionJointVentureCommissionCustomerDTO {

    @ApiModelProperty("客户id")
    @NotNull(message = "客户id不能为空", groups = PutMapping.class)
    private Long id;

    @ApiModelProperty(value = "客户姓名", required = true)
    @NotEmpty(message = "客户姓名不能为空")
    private String customerName;

    @ApiModelProperty("手机号")
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("身份证号")
    @NotEmpty(message = "身份证号不能为空")
    private String idCard;

    @ApiModelProperty("身份证照片正面")
    @NotEmpty(message = "身份证照片正面不能为空")
    private String idCardPhotoFront;

    @ApiModelProperty("身份证照片背面")
    @NotEmpty(message = "身份证照片背面不能为空")
    private String idCardPhotoBack;

    @ApiModelProperty("地址")
    @NotEmpty(message = "地址不能为空")
    private String address;

    @ApiModelProperty("佣金比例")
    @NotNull(message = "佣金比例不能为空")
    private Double commissionRate;

    @ApiModelProperty("适用门店类型 0-所有门店 1-指定门店")
    @NotNull(message = "适用门店类型不能为空")
    private Integer limitStoreType;

    @ApiModelProperty("适用门店id列表")
    private List<Long> limitStoreIdList;

    @ApiModelProperty("持卡人姓名")
    private String cardholderName;

    @ApiModelProperty("银行简称")
    private String bankSimpleName;

    @ApiModelProperty("支行全称")
    private String subbranchName;

    @ApiModelProperty("银行卡号")
    private String bankCardNo;

    @ApiModelProperty("状态 0禁用 1启用")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
