package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.feign.MaterialFeignClient;
import com.mall4j.cloud.api.biz.vo.MaterialBrowseRecordApiVO;
import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.biz.service.cp.CpMaterialBrowseRecordService;
import com.mall4j.cloud.biz.service.cp.MaterialService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MaterialFeignClientController implements MaterialFeignClient {


    @Autowired
    CpMaterialBrowseRecordService materialBrowseRecordService;
    @Autowired
    private MaterialService materialService;

    @Override
    public ServerResponseEntity<List<MaterialBrowseRecordApiVO>> getBrowseRecord(String unionId, String startTime, String endTime) {
        return ServerResponseEntity.success(materialBrowseRecordService.listByUnionId(unionId,startTime,endTime));
    }

    @Override
    public ServerResponseEntity<MsgAttachment> useAndFindAttachmentById(Long id, Long staffId) {
//        return ServerResponseEntity.success(materialService.taskUseAndFindAttachmentById(id,staffId));
        return ServerResponseEntity.success(materialService.useAndFindAttachmentById(id,staffId));
    }


}
