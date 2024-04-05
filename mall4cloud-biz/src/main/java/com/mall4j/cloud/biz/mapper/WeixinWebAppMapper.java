package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.vo.WeixinWebAppVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信公众号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 10:13:40
 */
public interface WeixinWebAppMapper {

    /**
     * 获取微信公众号表列表
     *
     * @return 微信公众号表列表
     */
    List<WeixinWebApp> list();

    List<WeixinWebAppVO> getList();

    /**
     * 根据微信公众号表id获取微信公众号表
     *
     * @param id 微信公众号表id
     * @return 微信公众号表
     */
    WeixinWebApp getById(@Param("id") String id);

    /**
     * 保存微信公众号表
     *
     * @param weixinWebApp 微信公众号表
     */
    void save(@Param("weixinWebApp") WeixinWebApp weixinWebApp);

    /**
     * 更新微信公众号表
     *
     * @param weixinWebApp 微信公众号表
     */
    void update(@Param("weixinWebApp") WeixinWebApp weixinWebApp);

    /**
     * 根据微信公众号表id删除微信公众号表
     *
     * @param id
     */
    void deleteById(@Param("id") String id);

    WeixinWebApp queryByAppid(@Param("appid") String appid);

    WeixinWebApp queryByWeixinAppid(@Param("appid") String appid);

    /**
     * 根据本地应用标识 获取 微信端应用标识
     *
     * @param ids 本地应用标识
     * @return 微信端应用标识
     */
    List<String> listWechatAppIdByIds(@Param("ids") List<Long> ids);

    List<WeixinWebApp> getWxMpFollowDatas(@Param("appid")String appId);
}
