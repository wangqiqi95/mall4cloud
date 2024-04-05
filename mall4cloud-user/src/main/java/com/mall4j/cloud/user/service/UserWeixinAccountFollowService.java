package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowsDTO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataVo;
import com.mall4j.cloud.user.model.UserWeixinAccountFollow;
import com.mall4j.cloud.user.vo.UserWeixinAccountFollowVo;

import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-04-01 3:37 PM
 **/
public interface UserWeixinAccountFollowService extends IService<UserWeixinAccountFollow> {

    /**
     * 粉丝列表
     * @param pageDTO
     * @param dto
     * @return
     */
    PageVO<UserWeixinAccountFollowVo> pageUserFollowList(PageDTO pageDTO,UserWeixinccountFollowSelectDTO dto);

    /**
     * 粉丝分析
     * @param dto
     * @return
     */
    UserWeixinAccountFollowDataVo userFollowData(UserWeixinccountFollowSelectDTO dto);

    void follow(UserWeixinccountFollowDTO param);

    /**
     * 根据集中标识(UnionId)查询用户所关注的所有AppId标识
     *
     * @param unionId 集中标识
     * @return 微信官方应用Id(AppId)
     */
    List<String> listUserFollowUpAppId(String unionId);

    /**
     * 根据unionId同步用户公众号关注数据到CRM
     *
     * @param unionId
     */
    void syncFollw(String unionId);


    /**
     * 会员关注获取取消关注的公众号列表
     *
     * @param unionId
     * @return
     */
    List<UserWeixinAccountFollowVo> getUserFollowList(String unionId);

    UserWeixinAccountFollowVO getUserFollowByUnionIdAndAppid(String unionId, String appId);

    /**
     * 根据集中标识查询用户已关注的微信公众号标识
     *
     * @param unionId 集中标识
     * @return 已关注的微信公众号标识（AppId）
     */
    List<String> listFollowUpAppIdByUnionId(String unionId);

    void followData(UserWeixinccountFollowsDTO followsDTO);

}
