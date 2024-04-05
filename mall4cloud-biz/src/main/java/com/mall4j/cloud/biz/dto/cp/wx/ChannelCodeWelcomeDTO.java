package com.mall4j.cloud.biz.dto.cp.wx;

import com.mall4j.cloud.biz.dto.cp.AttachMentBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class ChannelCodeWelcomeDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("源：0欢迎语/1渠道活码/2渠道活码时间段")
    private Integer sourceFrom;

    @ApiModelProperty("时间段：周期")
    private String timeCycle;

    @ApiModelProperty("时间段：开始时间")
    private String timeStart;

    @ApiModelProperty("时间段：结束时间")
    private String timeEnd;

    @ApiModelProperty("附件")
    private List<AttachMentBaseDTO> attachMents;

}
