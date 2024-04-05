package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.WxCpGetMomentComments;

import java.util.List;

@Data
public class DistributionMomentsCommentsPageDTO {

    @ApiModelProperty("0评论/1点赞")
    private Integer type;

    @ApiModelProperty(value = "员工发送记录id",required = true)
    private Long staffSendRecordId;

}
