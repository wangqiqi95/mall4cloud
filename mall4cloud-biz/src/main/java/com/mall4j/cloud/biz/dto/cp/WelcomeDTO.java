package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 欢迎语配置表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeDTO   extends AttachMentBaseDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("权重")
//    @NotNull(message = "权重不能为空")
    private Integer weight;

    @ApiModelProperty("欢迎语")
    @NotBlank(message = "欢迎语不能为空")
    private String slogan;

    @ApiModelProperty("场景 0未注册，1注册")
    private String scene;

    @ApiModelProperty("关联商店")
    private List<Long>  shops;

    @ApiModelProperty("关联员工")
    private List<Long>  staffIds;

    @ApiModelProperty("是否是全部商店(0部分门店 1全部门店)")
    private Integer  isAllShop;

    @ApiModelProperty("是否开启分时段欢迎语：0否/1是")
    private Integer welcomeTimeState;

    @ApiModelProperty("默认附件列表")
    private List<AttachMentBaseDTO>  attachMentBaseDTOS;


    @ApiModelProperty("分时段欢迎语列表")
    private List<CpWelcomeTimeStateDTO>  welcomeTimeStateDTOS;
}
