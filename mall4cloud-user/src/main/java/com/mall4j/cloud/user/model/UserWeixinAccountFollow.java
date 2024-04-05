package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 用户微信公众号关注记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-01 15:35:00
 */
@Data
public class UserWeixinAccountFollow extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 公众号原始id
     */
    private String appId;

    /**
     * 公众号类型 0成人公众号 1儿童公众号 2lifestyle公众号 3sport公众号
     */
    private Integer type;

    /**
     * 状态 0:关注 1:取消关注
     */
    private Integer status;

    /**
     * openid
     */
    private String openId;

    /**
     * unionid
     */
    private String unionId;

    private String ticket;

    /**
     * 取消关注时间
     */
    private Date unFollowTime;

    private Integer isDelete;

    private String subscribeScene;
}
