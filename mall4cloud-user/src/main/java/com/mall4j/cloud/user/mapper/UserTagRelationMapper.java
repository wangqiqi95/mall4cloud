package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.user.bo.UserTagRelationBO;
import com.mall4j.cloud.user.dto.QueryMarkingUserPageDTO;
import com.mall4j.cloud.user.dto.StaffGetVisibleUserTagDTO;
import com.mall4j.cloud.user.model.UserTagRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.MarkingUserPageVO;
import com.mall4j.cloud.user.vo.StaffGetVisibleUserTagVO;
import com.mall4j.cloud.user.vo.UserTagListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户与标签关联表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
public interface UserTagRelationMapper extends BaseMapper<UserTagRelation> {

    void insertBatch(@Param("relationList")List<UserTagRelationBO> relationList);

    IPage<MarkingUserPageVO> getTheMarkingUser(@Param("page") IPage page, @Param("param")QueryMarkingUserPageDTO param);

    // 导购获取可对用户进行打标的标签信息【标签所在组状态包含导购可用】
    List<StaffGetVisibleUserTagVO> staffGetVisibleUserTag(@Param("param") StaffGetVisibleUserTagDTO param);

    List<String> selectVipCodeByTagId(@Param("tagId") Long tagId);

    // 导购根据【是否加好友状态】查询会员
    List<Long> staffGetUserIdByWeChatType(@Param("weChatType") Integer weChatType, @Param("staffId") Long staffId);

    List<UserTagListVO> selectTagByVipCode(@Param("vipCode") String vipCode);

    void insertBatchForImport(@Param("codeList") List<String> codeList, @Param("tagId") Long tagId, @Param("groupId") Long groupId,
                              @Param("groupTagRelationId") Long groupTagRelationId, @Param("createUser") Long createUser);

    /**
     * 统计 tagIds 涉及的用户总数
     * @param tagIds 标签集合
     * @return count
     */
    Integer selectCountByTagId(@Param("tagIds") List<Long> tagIds);

    /**
     * 根据 tagIds 获取涉及的用户userId
     * @param tagIds 标签ID集合
     * @return userId list
     */
    List<Long> selectListUserIdByTagIds(@Param("tagIds") List<Long> tagIds);
}
