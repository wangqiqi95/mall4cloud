package com.mall4j.cloud.api.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodeRefPDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工门店id")
    private Long storeId;

    @ApiModelProperty("员工名称")
    private String staffName;

    @ApiModelProperty("员工编号")
    private String staffNo;

    @ApiModelProperty("员工手机号")
    private String staffPhone;

    public StaffCodeRefPDTO(){}

    public StaffCodeRefPDTO(Long staffId,Long storeId,String staffName,String staffNo,String staffPhone){
        this.staffId=staffId;
        this.storeId=storeId;
        this.staffName=staffName;
        this.staffNo=staffNo;
        this.staffPhone=staffPhone;
    }
}
