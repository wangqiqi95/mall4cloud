package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.vo.TaskSonMediaVO;
import com.mall4j.cloud.user.model.GroupPushSonTaskMedia;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;

import java.util.List;

/**
 * <p>
 * 推送子任务素材表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushSonTaskMediaService extends IService<GroupPushSonTaskMedia> {

    /**
     * 导购根据子任务ID获取任务素材内容
     * @param sonTaskId 子任务ID
     * @return
     */
    List<TaskSonMediaVO> sonTaskMediaList(Long staffId,Long sonTaskId);

    /**
     * 导购根据子任务ID获取任务素材内容
     * @param sonTaskId 子任务ID
     * @param urlFlag 选择是否要url还是id
     * @return
     */
    List<Attachment> mediaAttachmentBySonTaskId(Long sonTaskId, Long staffId,boolean urlFlag);

}
