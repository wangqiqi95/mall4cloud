package com.mall4j.cloud.api.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodeCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private List<StaffCodeRefPDTO> dtos=new ArrayList<>();

    @ApiModelProperty("0批量导入员工 1中台同步员工 2活码失败记录重新生成")
    private Integer fromType;

    private Long createById;
    private String createByName;

    public StaffCodeCreateDTO(){}

    public StaffCodeCreateDTO(Long createById,String createByName,Integer fromType,List<StaffCodeRefPDTO> dtos){
        this.createById=createById;
        this.createByName=createByName;
        this.fromType=fromType;
        this.dtos=dtos;
    }
}
