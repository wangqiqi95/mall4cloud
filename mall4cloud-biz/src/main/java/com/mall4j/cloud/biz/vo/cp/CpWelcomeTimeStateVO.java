package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.CpWelcomeTimeState;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 个人欢迎语 分时段欢迎语列表VO
 *
 * @author FrozenWatermelon
 * @date 2023-11-06 17:02:34
 */
@Data
public class CpWelcomeTimeStateVO  extends CpWelcomeTimeState {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty("分时段欢迎语")
	private List<CpWelcomeAttachment> attachment;
}
