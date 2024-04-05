package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 欢迎语 使用记录
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
@Data
public class CpWelcomeUseRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 欢迎语id
     */
    private Long welId;

    /**
     * 导购编号
     */
    private Long staffId;

    /**
     * 导购名称
     */
    private String staffName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 导购用户好友关联id
     */
    private Long userStaffCpRelationId;

    /**
     * 用户unionId
     */
    private String userUnionId;


}
