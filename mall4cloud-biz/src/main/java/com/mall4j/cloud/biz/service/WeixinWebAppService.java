package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.WeixinWebAppVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinWebApp;

import java.util.List;

/**
 * 微信公众号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 10:13:40
 */
public interface WeixinWebAppService {

    /**
     * 分页获取微信公众号表列表
     *
     * @param pageDTO 分页参数
     * @return 微信公众号表列表分页数据
     */
    PageVO<WeixinWebAppVO> page(PageDTO pageDTO);

    List<WeixinWebApp> getWxMpFollowDatas(String appId);

    /**
     * 根据微信公众号表id获取微信公众号表
     *
     * @param id 微信公众号表id
     * @return 微信公众号表
     */
    WeixinWebApp getById(String id);

    /**
     * 保存微信公众号表
     *
     * @param weixinWebApp 微信公众号表
     */
    void save(WeixinWebApp weixinWebApp);

    /**
     * 更新微信公众号表
     *
     * @param weixinWebApp 微信公众号表
     */
    void update(WeixinWebApp weixinWebApp);

    /**
     * 根据微信公众号表id删除微信公众号表
     *
     * @param id 微信公众号表id
     */
    void deleteById(String id);

    WeixinWebApp queryByAppid(String appid);

    String getAccessTokenByAppid(String appid);

    /**
     * 根据标识查询微信应用标识（AppId-Wechat端）
     *
     * @param ids 本地应用标识
     * @return AppId-Wechat端
     */
    List<String> listWechatAppIdByIds(List<Long> ids);

}
