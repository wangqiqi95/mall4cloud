package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodePlusDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("活动标题")
    @NotBlank(message = "活动标题不能为空")
    private String codeName;

    @ApiModelProperty("活码类型 0 批量单人 1 单人 2 多人")
    @NotNull(message = "活码类型不能为空")
    private Integer codeType;

    @ApiModelProperty("通过好友：0 自动通过， 1 手动通过")
    @NotNull(message = "通过好友不能为空")
    private Integer authType;

    @ApiModelProperty("欢迎语")
    private String slogan;

    /**
     * 自动备注：0否/1是
     */
    @ApiModelProperty("0否/1是")
    @NotNull(message = "自动备注不能为空")
    private Integer autoRemarkState;

    /**
     * 自动备注前缀
     */
    @ApiModelProperty("自动备注前缀")
    private String autoRemark;

    /**
     * 自动描述：0否/1是
     */
    @ApiModelProperty("0否/1是")
//    @NotNull(message = "自动描述不能为空")
    private Integer autoDescriptionState;

    /**
     * 自动描述
     */
    @ApiModelProperty("自动描述")
    private String autoDescription;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 二维码样式
     */
    @ApiModelProperty("二维码样式")
    private String codeStyle;

    /**
     * 欢迎语类型：0通用渠道/1默认员工/不发欢迎语
     */
    @ApiModelProperty("欢迎语类型：0通用渠道/1默认员工/不发欢迎语")
    private Integer welcomeType;

    @ApiModelProperty("标签信息")
    private String tags;

    @ApiModelProperty("关联员工信息")
    private List<CpStaffCodeTimeDTO> timeDTOS;

    @ApiModelProperty("关联员工信息[备用人员]")
    private List<StaffCodeRefDTO> standbyStaffs;

    @ApiModelProperty("欢迎语附件")
    private List<ChannelCodeWelcomeDTO> attachMents;

    private Long baseWelId;

    @ApiModelProperty("是否开启分时段欢迎语：0否/1是")
    private Integer welcomeTimeState;

    @ApiModelProperty("分组id")
    private Long groupId;

    @ApiModelProperty("接待时间类型：0全天接待/1分时段接待")
    private Integer receptionType;

}
