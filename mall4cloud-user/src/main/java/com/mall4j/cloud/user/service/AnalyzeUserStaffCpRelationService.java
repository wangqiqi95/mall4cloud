package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationDetailVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationExportVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.cp.AnalyzeUserStaffCpRelationVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.bo.GroupPushTaskCpRelationBO;
import com.mall4j.cloud.user.dto.analyze.AnalyzeUserStaffCpRelationDTO;
import com.mall4j.cloud.user.model.UserStaffCpRelation;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
public interface AnalyzeUserStaffCpRelationService extends IService<UserStaffCpRelation> {


    /**
     * 渠道活码详情-好友统计分页列表
     * @param pageDTO
     * @param dto
     * @return
     */
    PageVO<AnalyzeUserStaffCpRelationListVO> getAnalyzeUSRFPage(PageDTO pageDTO, AnalyzeUserStaffCpRelationDTO dto);

    List<AnalyzeUserStaffCpRelationExportVO> exportAnalyzeUSRFPage(AnalyzeUserStaffCpRelationDTO dto);

    /**
     * 渠道活码详情-新增客户折线图
     * @param dto
     * @return
     */
    AnalyzeUserStaffCpRelationDetailVO getAnalyzeNewUSRFList(AnalyzeUserStaffCpRelationDTO dto);

    /**
     * 渠道活码详情-所有客户折线图
     * @param dto
     * @return
     */
    List<AnalyzeUserStaffCpRelationVO> getAnalyzeAllUSRFList(AnalyzeUserStaffCpRelationDTO dto);

    /**
     * 渠道活码详情-数据明细
     * @param dto
     * @return
     */
    PageVO<AnalyzeUserStaffCpRelationVO> getAnalyzeUSRFDetailPage(PageDTO pageDTO, AnalyzeUserStaffCpRelationDTO dto);

}
