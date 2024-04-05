package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.AttachmentExtApiDTO;
import com.mall4j.cloud.api.biz.feign.WxCpCustNotifyFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.service.cp.WxCpCustNotifyPlusService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WxCpCustNotifyFeignController implements WxCpCustNotifyFeignClient {

    @Autowired
    private WxCpCustNotifyPlusService wxCpCustNotifyService;
    @Autowired
    WxCpPushService wxCpPushService;

    @Override
    public ServerResponseEntity<String> defineStaffMiniProgram(AttachmentExtApiDTO attachmentExtApiDTO) {

        StaffVO staffVO = new StaffVO();
        staffVO.setId(attachmentExtApiDTO.getStaffId());
        staffVO.setStoreId(attachmentExtApiDTO.getStaffStoreId());
        return wxCpCustNotifyService.defineStaffMiniProgram(attachmentExtApiDTO, staffVO);
    }

    @Override
    public ServerResponseEntity<String> followNotify(String nickName,
                                                     String userName,
                                                     String staffName,
                                                     String createDate,
                                                     String relationId,
                                                     String staffUserId) {
        NotifyMsgTemplateDTO.FollowUp followUp=NotifyMsgTemplateDTO.FollowUp.builder()
                .nickName(nickName)
                .userName(userName)
                .staffName(staffName)
                .createDate(createDate)
                .build();
        wxCpPushService.followNotify(NotifyMsgTemplateDTO.builder().userId(relationId).qiWeiStaffId(staffUserId).followUp(followUp).build());
        return ServerResponseEntity.success();
    }
}
