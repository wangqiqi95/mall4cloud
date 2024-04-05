package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionUserBindDTO;
import com.mall4j.cloud.distribution.model.DistributionUserBind;
import com.mall4j.cloud.distribution.vo.DistributionUserBindInfoVO;
import com.mall4j.cloud.distribution.vo.DistributionUserBindVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销员绑定关系
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public interface DistributionUserBindMapper {

    /**
     * 获取分销员绑定关系列表
     * @param distributionUserBindDTO
     * @return 分销员绑定关系列表
     */
    List<DistributionUserBindVO> list(@Param("distributionUserBindDTO")DistributionUserBindDTO distributionUserBindDTO);

    /**
     * 根据分销员绑定关系id获取分销员绑定关系
     *
     * @param bindId 分销员绑定关系id
     * @return 分销员绑定关系
     */
    DistributionUserBind getByBindId(@Param("bindId") Long bindId);

    /**
     * 保存分销员绑定关系
     *
     * @param distributionUserBind 分销员绑定关系
     */
    void save(@Param("distributionUserBind") DistributionUserBind distributionUserBind);

    /**
     * 更新分销员绑定关系
     *
     * @param distributionUserBind 分销员绑定关系
     */
    void update(@Param("distributionUserBind") DistributionUserBind distributionUserBind);

    /**
     * 根据分销员绑定关系id删除分销员绑定关系
     *
     * @param bindId
     */
    void deleteById(@Param("bindId") Long bindId);

    /**
     * 获取分销员绑定关系
     *
     * @param userId 用户id
     * @param state  当前绑定关系状态
     * @return
     */
    DistributionUserBind getUserBindByUserIdAndState(@Param("userId") Long userId, @Param("state") Integer state);

    /**
     * 通过绑定的分销员id 更新 分销员绑定状态
     *
     * @param distributionUserId 绑定的分销员id
     * @param state              分销员绑定状态
     */
    void updateStateAndReasonByDistributionUserId(@Param("distributionUserId") Long distributionUserId, @Param("state") Integer state);

    /**
     * 获取绑定用户的列表
     *
     * @param userId 用户id
     * @return 绑定用户的列表
     */
    List<DistributionUserBindInfoVO> bindUserList(@Param("userId") Long userId);

    /**
     * 更新暂时封禁时的绑定用户,失效->生效
     *
     * @param distributionUserId 被解除封禁的分销员id
     * @return
     */
    List<DistributionUserBind> updateBindUserByDistributionUserId(@Param("distributionUserId") Long distributionUserId);

}
