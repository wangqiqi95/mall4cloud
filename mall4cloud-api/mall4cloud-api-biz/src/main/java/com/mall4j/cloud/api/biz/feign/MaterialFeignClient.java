package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.vo.MaterialBrowseRecordApiVO;
import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mall4cloud-biz", contextId = "material")
public interface MaterialFeignClient {


    /**
     * 获取素材浏览记录
     * @param unionId
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/material/getBrowseRecord")
    ServerResponseEntity<List<MaterialBrowseRecordApiVO>> getBrowseRecord(@RequestParam("unionId")String unionId, @RequestParam("startTime")String startTime, @RequestParam("endTime") String endTime);

    /**
     * 获取素材
     * @param id
     * @param staffId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/material/useAndFindAttachmentById")
    ServerResponseEntity<MsgAttachment> useAndFindAttachmentById(@RequestParam("id")Long id,
                                                                 @RequestParam("staffId")Long staffId);


}
