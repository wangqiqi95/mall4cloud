package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.database.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MomentSendRecordPageVO {

    @ApiModelProperty("分页数据对象")
    private PageVO<DistributionMomentsSendRecordVO> pageVO;


    @ApiModelProperty("已经完成推送总数")
    private Integer dealSendCount;
    @ApiModelProperty("未完成推送总数")
    private Integer unSendCount;

    @ApiModelProperty("推送完成率")
    private BigDecimal pushRate;
}
