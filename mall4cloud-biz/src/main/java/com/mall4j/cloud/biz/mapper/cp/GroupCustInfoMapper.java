package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.biz.dto.cp.PageQueryCustInfo;
import com.mall4j.cloud.biz.model.cp.CpGroupCustInfo;
import com.mall4j.cloud.biz.vo.cp.CpGroupCustInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客群客户信息表
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface GroupCustInfoMapper extends BaseMapper<CpGroupCustInfo> {

    /**
     * 获取客群客户表 列表
     * @return 客群客户表 列表
     */
    List<CpGroupCustInfoVO> list(@Param("et") PageQueryCustInfo filter);

    List<CpGroupCustInfo> listByGroupId(@Param("groupId") String groupId);

    /**
     * 根据客群客户表userId groupId获取
     * @param userId 客户外部联系人id
     * @param groupId 客群id
     * @return 客群客户表
     */
    CpGroupCustInfo getById(@Param("userId") String userId, @Param("groupId") String groupId);

    /**
     * 根据客群客户表 id删除客群客户表
     * @param userId 客户外部联系人id
     * @param groupId 客群id
     */
    void deleteById(@Param("userId") String userId,@Param("groupId") String groupId);

    void deleteByGroupId(@Param("groupId") String groupId,@Param("updateBy") String updateBy);

    List<UserJoinCustGroupVO> findCustGroupByQwUserId(@Param("qwUserId")String qwUserId);

    Integer countCustGroupByQwUserId(@Param("qwUserId")String qwUserId);
}
