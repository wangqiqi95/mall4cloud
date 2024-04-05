package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.PromoterResp;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * @Description 视频号优选联盟
 * @Author axin
 * @Date 2023-02-14 17:51
 **/
public interface LeaguePromoterService {

    /**
     * 达人列表
     * @param reqDto
     * @return
     */
    PageVO<PromoterListRespDto> promoterList(PromoterListReqDto reqDto);

    /**
     * 达人详情
     * @param id
     * @return
     */
    PromoterDetailRespDto promoterDetail(Long id);

    /**
     * 添加达人
     * @param reqDto
     */
    void promoterAdd(PromoterAddReqDto reqDto);

    /**
     * 修改达人
     * @param reqDto
     */
    void promoterUpd(PromoterUpdReqDto reqDto);

    /**
     * 删除达人
     * @param reqDto
     */
    void promoterDel(PromoterDelReqDto reqDto);

    /**
     * 获取达人基础绑定信息
     * @param finderId
     * @return
     */
    PromoterResp getPromoterByFinderId(String finderId);
}
