package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowVO;
import com.mall4j.cloud.user.model.UserWeixinAccountFollow;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.user.vo.UserWeixinAccountFollowVo;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * 用户微信公众号关注记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-01 15:35:00
 */
public interface UserWeixinAccountFollowMapper extends BaseMapper<UserWeixinAccountFollow> {

    /**
     * 获取用户微信公众号关注记录列表
     *
     * @return 用户微信公众号关注记录列表
     */
    List<UserWeixinAccountFollowVo> list(@Param("dto") UserWeixinccountFollowSelectDTO dto);

    List<UserWeixinAccountFollowDataListVo> followData(@Param("dto") UserWeixinccountFollowSelectDTO dto);

    List<UserWeixinAccountFollowDataListVo> unFollowData(@Param("dto") UserWeixinccountFollowSelectDTO dto);

    List<UserWeixinAccountFollowVo> getUserFollowList(@Param("unionId") String unionId);

    List<UserWeixinAccountFollow> getListByAppId(@Param("appId") String appId);

    /**
     * 根据用户微信公众号关注记录id获取用户微信公众号关注记录
     *
     * @param id 用户微信公众号关注记录id
     * @return 用户微信公众号关注记录
     */
    UserWeixinAccountFollow getById(@Param("id") Long id);

    UserWeixinAccountFollowVO getUserFollowByUnionIdAndAppid(@Param("unionId") String unionId, @Param("appId") String appId);

    /**
     * 保存用户微信公众号关注记录
     *
     * @param userWeixinAccountFollow 用户微信公众号关注记录
     */
    void save(@Param("userWeixinAccountFollow") UserWeixinAccountFollow userWeixinAccountFollow);

    /**
     * 更新用户微信公众号关注记录
     *
     * @param userWeixinAccountFollow 用户微信公众号关注记录
     */
    void update(@Param("userWeixinAccountFollow") UserWeixinAccountFollow userWeixinAccountFollow);

    /**
     * 根据用户微信公众号关注记录id删除用户微信公众号关注记录
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);

    UserWeixinAccountFollow getByUnionIdAndType(@Param("unionId") String unionId, @Param("type") Integer type);

    UserWeixinAccountFollow getByOpenId(@Param("openId") String openId);

    void updateStatusByOpneId(@Param("openId") String openId,
                              @Param("status") Integer status,
                              @Param("unionId") String unionId,
                              @Param("unFollowTime") Date unFollowTime,
                              @Param("createTime") Date createTime,
                              @Param("updateTime") Date updateTime);

    List<UserWeixinAccountFollow> listByUnionId(@Param("unionId") String unionId);

    /**
     * 根据集中标识（UnionId）查询用户 已关注 的公众号
     *
     * @param unionId 集中标识
     * @return 微信官方应用标识
     */
    List<String> listFollowUpAppIdByUnionId(@Param("unionId") String unionId);
}
