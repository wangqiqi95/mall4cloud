package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 会员规则
 * @author shijing
 * @date 2022-01-16
 */
@Data
public class MembershipRules implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 会员规则
     */
    private String content;

    /**
     * create_id
     */
    private Long createId;

    /**
     * create_name
     */
    private String createName;

    /**
     * create_time
     */
    private Timestamp createTime;

}
