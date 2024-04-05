package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeUser;
import com.mall4j.cloud.biz.model.cp.CpCodeChannel;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.CpTaskUserRef;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;

import java.util.List;
import java.util.Map;

@Data
public class CpChatBusinessDTO {

    List<CpAutoGroupCodeUser> groupCodeUsers;

    List<CpTaskUserRef> taskUserRefs;

    WxCpUserExternalGroupChatInfo.GroupChat groupChat;

    Map<String, CpCodeChannel> cpCodeChannelMaps;

    CpCustGroup custGroup;

    String quitScene;

    WxCpXmlMessage wxMessage;
}
