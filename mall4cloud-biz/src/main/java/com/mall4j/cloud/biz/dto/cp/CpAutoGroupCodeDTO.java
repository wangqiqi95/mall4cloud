package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeStaff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 自动拉群活码表DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpAutoGroupCodeDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty(value = "活码名称",required = true)
    private String codeName;

    @ApiModelProperty(value = "标签集合",required = true)
    private String tags;

    @ApiModelProperty(value = "通过好友：0 自动通过， 1 手动通过",required = true)
    private Integer authType;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty(value = "欢迎语",required = true)
    private String slogan;

    @ApiModelProperty("")
    private String configId;

    @ApiModelProperty("")
    private String state;

    @ApiModelProperty("二维码链接")
    private String qrCode;

    @ApiModelProperty(value = "自动备注：0否/1是",required = true)
    private Integer autoRemarkState;

    @ApiModelProperty("二维码样式")
    private String codeStyle;

    @ApiModelProperty("自动备注前缀")
    private String autoRemark;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("引流链接")
    private String drainageUrl;

    @ApiModelProperty(value = "拉群方式：0企微群活码/1自建群活码",required = true)
    private Integer groupType;

    @ApiModelProperty(value = "分组id",required = true)
    private Long groupId;

    @ApiModelProperty(value = "接待人员",required = true)
    private List<CpAutoGroupCodeStaffDTO> staffs;

    @ApiModelProperty(value = "群组集合",required = true)
    private List<CpGroupCodeListDTO> codeList;

}
