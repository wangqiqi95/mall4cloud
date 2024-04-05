package com.mall4j.cloud.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.vo.MediaJsonVO;
import com.mall4j.cloud.user.vo.TaskSonMediaVO;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMediaMapper;
import com.mall4j.cloud.user.model.GroupPushSonTaskMedia;
import com.mall4j.cloud.user.service.GroupPushSonTaskMediaService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 推送子任务素材表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Slf4j
@Service
public class GroupPushSonTaskMediaServiceImpl extends ServiceImpl<GroupPushSonTaskMediaMapper, GroupPushSonTaskMedia> implements GroupPushSonTaskMediaService {

    @Autowired
    private GroupPushTaskService groupPushTaskService;
    @Autowired
    private GroupPushSonTaskMediaMapper groupPushSonTaskMediaMapper;

    //群发素材缓存key前缀
    private final static String CP_EXTERN_CONTACT_ATTACHMENT_LIST = "mall4cloud:cp_external_attachment_list::";

    /**
     * 导购根据子任务ID获取任务素材内容
     * @param sonTaskId 子任务ID
     * @return
     */
    @Override
    public List<TaskSonMediaVO> sonTaskMediaList(Long staffId,Long sonTaskId) {
        List<TaskSonMediaVO> taskSonMediaVOS = groupPushSonTaskMediaMapper.sonTaskMediaList(sonTaskId);
        taskSonMediaVOS.forEach(media -> {
            MediaJsonVO mediaJsonVO = JSONObject.parseObject(media.getMedia(), MediaJsonVO.class);
            if(Objects.nonNull(mediaJsonVO.getMaterialId())){
                Attachment attachment=groupPushTaskService.getMaterialAttachment(mediaJsonVO.getMaterialId(),staffId);
                if(Objects.nonNull(attachment)){
                    mediaJsonVO.setMaterialJson(JSON.toJSONString(attachment));
                }
            }
            media.setMediaJsonVO(mediaJsonVO);
        });
        return taskSonMediaVOS;
    }

    @Override
    public List<Attachment> mediaAttachmentBySonTaskId(Long sonTaskId, Long staffId,boolean urlFlag) {
        //设置一个缓存临时素材的redisKey
        String redisKey = CP_EXTERN_CONTACT_ATTACHMENT_LIST + sonTaskId;
        List<Attachment> attachments = groupPushTaskService.getAttachments(sonTaskId, redisKey, staffId,urlFlag);

        log.info("导购根据子任务ID获取任务素材内容,当前查询的子任务ID为{},redisKey为{},获取的素材信息为{}", sonTaskId, redisKey, JSONObject.toJSONString(attachments));
        return attachments;
    }

}
