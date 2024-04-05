package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowVO;
import lombok.Data;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

import java.io.Serializable;

@Data
public class WxCpMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String xml;
    private Boolean veryFirstTimeToFollowUp;
    private WxMpXmlMessage wxMessage;
    private UserWeixinAccountFollowVO userWeixinAccountFollowVO;
}
