package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.mapper.WeixinAutoResponseMapper;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 微信公共回复内容表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:03
 */
@Service
public class WeixinAutoResponseServiceImpl implements WeixinAutoResponseService {

    @Autowired
    private WeixinAutoResponseMapper weixinAutoResponseMapper;

    @Override
    public PageVO<WeixinAutoResponse> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinAutoResponseMapper.list());
    }

    @Override
    public WeixinAutoResponse getById(Long id) {
        return weixinAutoResponseMapper.getById(id);
    }

    @Override
    public void save(WeixinAutoResponse weixinAutoResponse) {
        weixinAutoResponseMapper.save(weixinAutoResponse);
    }

    @Override
    public void update(WeixinAutoResponse weixinAutoResponse) {
        weixinAutoResponseMapper.update(weixinAutoResponse);
    }

    @Override
    public void deleteById(Long id) {
        weixinAutoResponseMapper.deleteById(id);
    }

    @Override
    public void deleteByDataId(String dataId) {
        weixinAutoResponseMapper.deleteByDataId(dataId);
    }

    @Override
    public WeixinAutoResponse getWeixinAuto(String dataId, Integer dataSrc,String msgType) {
        return weixinAutoResponseMapper.getWeixinAuto(dataId,dataSrc,msgType);
    }

    @Override
    public List<WeixinAutoResponse> getWeixinAutos(String dataId, Integer dataSrc) {
        return weixinAutoResponseMapper.getWeixinAutos(dataId,dataSrc);
    }

    /**
     * 保存单个回复内容
     * @param dataId
     * @param dataSrc
     * @param templageManagerVO
     */
    @Override
    public void saveWeixinAutoResponse(String dataId, Integer dataSrc, WeixinTemplageManagerVO templageManagerVO) {
        String msgType=templageManagerVO.getMsgType();

        WeixinAutoResponse autoResponse=this.getWeixinAuto(dataId,dataSrc,msgType);

        saveToAutoResponse(dataId,dataSrc,templageManagerVO,autoResponse);
    }

    /**
     * 保存多个回复内容
     * @param dataId
     * @param dataSrc
     * @param templageManagerVO
     */
    @Override
    public void saveWeixinAutoResponseList(String dataId, Integer dataSrc, WeixinTemplageManagerVO templageManagerVO) {
        String msgType=templageManagerVO.getMsgType();
        String templateId=templageManagerVO.getTemplateId();
        WeixinAutoResponse autoResponse=weixinAutoResponseMapper.getWeixinAutoByTemplateId(dataId,dataSrc,msgType,templateId);

        saveToAutoResponse(dataId,dataSrc,templageManagerVO,autoResponse);
    }

    private void saveToAutoResponse(String dataId, Integer dataSrc, WeixinTemplageManagerVO templageManagerVO,WeixinAutoResponse autoResponse){
        String msgType=templageManagerVO.getMsgType();
        String templateId=templageManagerVO.getTemplateId();
        String templateName=templageManagerVO.getTemplateName();

        if(StringUtils.isEmpty(templateId)) return;

        if(autoResponse==null){
            autoResponse=new WeixinAutoResponse();
            autoResponse.setId(RandomUtil.getUniqueNumStr());
            autoResponse.setMsgType(msgType);
            autoResponse.setDataId(dataId);
            autoResponse.setDataSrc(dataSrc);
            autoResponse.setTemplateId(templateId);
            autoResponse.setTemplateName(templateName);
            autoResponse.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
            autoResponse.setCreateTime(new Date());
            autoResponse.setDelFlag(0);
            this.save(autoResponse);
        }else{
            autoResponse.setTemplateId(templateId);
            autoResponse.setTemplateName(templateName);
            autoResponse.setDelFlag(0);
            autoResponse.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
            autoResponse.setUpdateTime(new Date());
            this.update(autoResponse);
        }
    }
}
