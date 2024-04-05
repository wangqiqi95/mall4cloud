package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.CpWelcomeTimeState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 个人欢迎语 分时段欢迎语列表DTO
 *
 * @author FrozenWatermelon
 * @date 2023-11-06 17:02:34
 */
@Data
public class CpWelcomeTimeStateDTO extends CpWelcomeTimeState {

    @ApiModelProperty("默认附件列表")
    private List<AttachMentBaseDTO> attachMentBaseDTOS;


}
