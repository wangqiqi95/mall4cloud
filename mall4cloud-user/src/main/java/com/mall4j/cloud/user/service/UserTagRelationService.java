package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.model.UserTagRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 用户与标签关联表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface UserTagRelationService extends IService<UserTagRelation> {

    ServerResponseEntity marking(MarkingTagDTO markingTagDTO);

    ServerResponseEntity cancelMarking(Long relationId);


    ServerResponseEntity importAdd(ExportUserTagRelationDTO exportUserTagRelationDTO);

    ServerResponseEntity importRemove(ExportUserTagRelationDTO exportUserTagRelationDTO);


    ServerResponseEntity removeByTag(Long tagId);

    ServerResponseEntity<PageVO<MarkingUserPageVO>> getMarkingUserByTagPage(QueryMarkingUserPageDTO pageDTO);

    /**
     * 导购获取可对用户进行打标的标签信息【标签所在组状态包含导购可用】
     * @param staffGetVisibleUserTagDTO
     * @return
     */
    List<StaffGetVisibleUserTagVO> staffGetVisibleUserTag(StaffGetVisibleUserTagDTO staffGetVisibleUserTagDTO);

    /**
     * 导购对单个会员进行打标,如果该标签已经打过标那就进行取消打标
     * @param markingTagDTO
     * @return
     */
    ServerResponseEntity staffSaveUserTag(MarkingTagDTO markingTagDTO);


    ServerResponseEntity removeBySelectList(RemoveUserTagRelationBatchDTO removeBatchDTO);

    ServerResponseEntity removeByUserTagRelationId(Long userTagRelationId);

    ServerResponseEntity exportMarkingUser(Long tagId, HttpServletResponse response);


    ServerResponseEntity<List<UserTagListVO>> getTheTagByVipCode(String vipCode);

    void wrapperStoreAndStaff(List<MarkingUserPageVO> pageData);

    ServerResponseEntity<List<Long>> checkUserTagRelationByTagIdList(List<Long> tagIdList);

    /**
     * 判断会员是否符合标签  true符合  false 不符合
     * @param tagId
     * @param vipcode
     * @return
     */
    boolean isInTag(Long tagId,String vipcode);

    /**
     * 判断会员是否符合标签  true符合  false 不符合
     * @param tagIds
     * @param vipcode
     * @return
     */
    boolean isInTags(List<Long> tagIds,String vipcode);

    /**
     * 统计 tagIds 涉及的用户总数
     * @param tagIds 标签集合
     * @return count
     */
    Integer countByTagIds(List<Long> tagIds);

    /**
     * 根据 tagIds 获取涉及的用户Id
     * @param tagIds 标签ID
     * @return userId list
     */
    List<Long> listUserIdByTagIds(List<Long> tagIds);
}
