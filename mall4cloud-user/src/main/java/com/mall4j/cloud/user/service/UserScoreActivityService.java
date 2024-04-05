package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.CountFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.PageFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.UserFriendAssistanceDto;
import com.mall4j.cloud.user.vo.UserScoreLogVO;

/**
 * @Description 用户积分活动
 * @Author axin
 * @Date 2022-10-17 14:22
 **/
public interface UserScoreActivityService {

    /**
     * 助力好友
     * @param dto
     */
    void friendAssistance(UserFriendAssistanceDto dto);

    /**
     * 积分清零活动好友助力明细列表
     * @param dto
     */
    PageVO<PageFriendAssistanceRespDto> pageFriendAssistance(PageDTO dto);

    /**
     * 统计好友助力人数及总积分
     * @return
     */
    CountFriendAssistanceRespDto countFriendAssistance();

    /**
     * 积分清零活动海报
     * @return
     */
    String friendAssistancePoster();

    /**
     * 是否已助力
     * @return
     */
    Boolean isFriendAssistance(Long inviterUserId);

    /**
     * 是否积分清零活动时间
     * @return
     */
    Boolean isClearingPointsActivityDate();
}
