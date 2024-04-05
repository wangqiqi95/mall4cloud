package com.mall4j.cloud.biz.dto.cp.wx.externalcontact;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.model.cp.CpCodeChannel;
import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.contact.FollowedUser;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;

import java.io.Serializable;
import java.util.List;

@Data
public class SendWelcomeDTO implements Serializable {

    private WxCpXmlMessage wxMessage;
    private WxCpExternalContactInfo wxCpExternalContactInfo;
    private CpCodeChannel cpCodeChannel;
    private StaffVO staffVO;
    private FollowedUser followedUser;

    /**
     * 欢迎语类型：0通用渠道/1默认员工/不发欢迎语
     */
    private Integer welcomeType;
}
