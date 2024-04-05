package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * @Description 视频号分享员
 * @Author axin
 * @Date 2023-02-23 13:59
 **/
@Data
@TableName("channels_sharer")
public class LiveStoreSharer extends BaseModel {
    private Long id;
    /**
     * 创建人
     */
    private Long createPerson;

    /**
     * 修改人
     */
    private Long updatePerson;

    /**
     * 分享员姓名
     */
    private String name;

    /**
     * 分享员类型 1普通分享员 2企业分享员
     */
    private Integer sharerType;

    /**
     * 二维码创建时间
     */
    private Date qrcodeImgCreateTime;

    /**
     * 二维码过期时间
     */
    private Date qrcodeImgExpireTime;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工工号
     */
    private String staffNo;

    /**
     * 二进制二维码
     */
    private byte[] qrcodeImg;

    /**
     * 二进制二维码base64
     */
    private String qrcodeImgBase64;

    /**
     * 分享员openid
     */
    private String openid;

    /**
     * 分享员unionid
     */
    private String unionid;

    /**
     * 分享员昵称
     */
    private String nickname;

    /**
     * 绑定时间
     */
    private Date bindTime;

    /**
     * 解绑时间
     */
    private Date unbindTime;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 绑定状态 0初始化 1待绑定：有邀请码 2失效：邀请码失效 3成功：有绑定时间 4已解绑
     */
    private Integer bindStatus;
}
