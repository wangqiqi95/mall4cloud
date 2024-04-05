package com.mall4j.cloud.api.biz.dto.cp.externalcontact;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;

@Data
@NoArgsConstructor
public class AttachmentExtApiDTO {

    private Attachment attachment;

    private String localUrl;


    private Long staffId;
    private Long staffStoreId;

    public AttachmentExtApiDTO(Attachment attachment,String localUrl,Long staffId, Long staffStoreId){
        this.attachment =attachment;
        this.localUrl = localUrl;
        this.staffId = staffId;
        this.staffStoreId = staffStoreId;

    }
}
