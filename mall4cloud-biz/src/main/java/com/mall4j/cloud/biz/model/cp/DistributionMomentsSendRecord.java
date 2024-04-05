package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 朋友圈 员工发送记录表
 *
 * @author FrozenWatermelon
 * @date 2023-11-03 14:22:45
 */
@Data
public class DistributionMomentsSendRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 是否发送状态 0否 1已经发送
     */
    private Integer status;

    /**
     * 异步任务id，最大长度为64字节，24小时有效
     */
    private String qwJobId;

    /**
     * 企微朋友圈ID
     */
    private String qwMomentsId;

    /**
     * 企微发表状态 0:未发表 1：已发表
     */
    private Integer qwPublishStatus;

    /**
     * 企微朋友圈评论数量
     */
    private Integer qwCommentNum;

    /**
     * 企微朋友圈点赞数量
     */
    private Integer qwLikeNum;

    /**
     * 发送时间
     */
    private Date sendTime;

    private String qiweiUserId;

}
