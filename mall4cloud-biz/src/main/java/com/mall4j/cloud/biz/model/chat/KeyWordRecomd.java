package com.mall4j.cloud.biz.model.chat;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 会话关键词推荐回复内容
 *
 * @author gmq
 * @date 2024-01-05 15:19:52
 */
@Data
public class KeyWordRecomd extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 推荐内容id
     */
    private String recomdId;

    /**
     * 关键词id
     */
    private Long keyWordId;

    /**
     * 推荐内容类型: 1素材库/2话术/3问答话术
     */
    private Integer recomdType;

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

}
