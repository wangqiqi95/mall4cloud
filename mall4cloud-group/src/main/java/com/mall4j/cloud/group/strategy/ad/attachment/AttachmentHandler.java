package com.mall4j.cloud.group.strategy.ad.attachment;

import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;

public interface AttachmentHandler {

    void extraction(PopUpAdContainerVO container, AttachmentBO attachmentBO);
}
