package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户群表VO
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Data
public class CustGroupCountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总群聊数")
    private Long groupTotal;

    @ApiModelProperty("总客户数")
    private Long custTotal;

}
