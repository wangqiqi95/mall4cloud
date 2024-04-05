package com.mall4j.cloud.biz.model.cp;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 朋友圈互动明细数据
 *
 * @date 2024-03-04 16:47:40
 */
@Data
public class DistributionMomentsComments extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private Long staffId;

    /**
     * 
     */
    private String staffUserId;

    /**
     * 
     */
    private Long momentsId;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * 0正常/1删除
     */
    private Integer isDelete;

    /**
     * 0评论/1点赞
     */
    private Integer type;

    private Long staffSendRecordId;
}
