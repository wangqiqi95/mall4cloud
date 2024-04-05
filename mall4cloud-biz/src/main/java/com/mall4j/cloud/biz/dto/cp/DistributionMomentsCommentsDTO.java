package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.WxCpGetMomentComments;

import java.util.List;

@Data
public class DistributionMomentsCommentsDTO {

    private DistributionMomentsSendRecord sendRecord;

    private List<WxCpGetMomentComments.CommentLikeItem> commentList;

    private List<WxCpGetMomentComments.CommentLikeItem> likeList;

}
