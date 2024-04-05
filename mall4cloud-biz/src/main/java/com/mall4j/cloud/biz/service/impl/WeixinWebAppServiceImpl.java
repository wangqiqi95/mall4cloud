package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.vo.WeixinWebAppVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.mapper.WeixinWebAppMapper;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 微信公众号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 10:13:40
 */
@Slf4j
@Service
public class WeixinWebAppServiceImpl implements WeixinWebAppService {

    @Autowired
    private WeixinWebAppMapper weixinWebAppMapper;
    @Autowired
    private WxConfig wxConfig;

    @Override
    public PageVO<WeixinWebAppVO> page(PageDTO pageDTO) {
        PageVO<WeixinWebAppVO> pageVO=PageUtil.doPage(pageDTO, () -> weixinWebAppMapper.getList());
        for (WeixinWebAppVO weixinWebAppVO : pageVO.getList()) {
            weixinWebAppVO.setIsSecret(false);
            if(StrUtil.isNotEmpty(weixinWebAppVO.getWeixinAppSecret())){
                weixinWebAppVO.setIsSecret(true);
                weixinWebAppVO.setWeixinAppSecret(null);
            }
        }
        return pageVO;
    }

    @Override
    public List<WeixinWebApp> getWxMpFollowDatas(String appId) {
        return weixinWebAppMapper.getWxMpFollowDatas(appId);
    }

    @Override
    public WeixinWebApp getById(String id) {
        return weixinWebAppMapper.getById(id);
    }

    @Override
    public void save(WeixinWebApp weixinWebApp) {
        weixinWebAppMapper.save(weixinWebApp);
    }

    @Override
    public void update(WeixinWebApp weixinWebApp) {
        weixinWebAppMapper.update(weixinWebApp);
    }

    @Override
    public void deleteById(String id) {
        weixinWebAppMapper.deleteById(id);
    }

    @Override
    public WeixinWebApp queryByAppid(String appid) {
        WeixinWebApp weixinWebApp = weixinWebAppMapper.queryByAppid(appid);
        if (weixinWebApp == null) {
            weixinWebApp = weixinWebAppMapper.queryByWeixinAppid(appid);
        }
        return weixinWebApp;
    }

    @Override
    public String getAccessTokenByAppid(String appid) {
        //获取授权公众号信息
        WeixinWebApp weixinWebApp = this.queryByAppid(appid);
        if (weixinWebApp == null) {
            log.info("获取公众号AccessToken失败 公众号未授权，appid:【{}】 --->", appid);
            return "";
        }

        WxMp wxMp = new WxMp();
        wxMp.setAppId(weixinWebApp.getWeixinAppId());
        wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
        String accessToken = "";
        try {
            accessToken = wxConfig.getWxMpService(wxMp).getAccessToken();
        } catch (WxErrorException e) {
            log.error("获取accessToken异常，appid：【{}】", appid);
            e.printStackTrace();
        }
        return accessToken;
    }

    @Override
    public List<String> listWechatAppIdByIds(List<Long> ids) {
        return weixinWebAppMapper.listWechatAppIdByIds(ids);
    }
}
