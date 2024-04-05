package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.dto.WeixinSubscribePutDTO;
import com.mall4j.cloud.biz.dto.WeixinTemplateManagerDTO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.service.WeiXinTemplateManagerService;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.biz.vo.WeixinSubscribeVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinSubscribe;
import com.mall4j.cloud.biz.mapper.WeixinSubscribeMapper;
import com.mall4j.cloud.biz.service.WeixinSubscribeService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 微信关注回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:59
 */
@Service
public class WeixinSubscribeServiceImpl implements WeixinSubscribeService {

    @Autowired
    private WeixinSubscribeMapper weixinSubscribeMapper;

    @Autowired
    private WeiXinTemplateManagerService templateManagerService;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Override
    public PageVO<WeixinSubscribe> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinSubscribeMapper.list());
    }

    @Override
    public WeixinSubscribe getById(Long id) {
        return weixinSubscribeMapper.getById(id);
    }

    @Override
    public void save(WeixinSubscribe weixinSubscribe) {
        weixinSubscribeMapper.save(weixinSubscribe);
    }

    @Override
    public void update(WeixinSubscribe weixinSubscribe) {
        weixinSubscribeMapper.update(weixinSubscribe);
    }

    @Override
    public void deleteById(Long id) {
        weixinSubscribeMapper.deleteById(id);
    }

    @Override
    public WeixinSubscribe getWeixinSubscribe(String appId, Integer replyType) {
        return weixinSubscribeMapper.getWeixinSubscribe(appId,replyType);
    }

    /**
     * 获取回复内容
     * @param appId
     * @param replyType
     * @param msgType
     * @return
     */
    @Override
    public ServerResponseEntity<WeixinSubscribeVO> getWeixinSubscribeVO(String appId, Integer replyType, String msgType) {
        WeixinSubscribe weixinSubscribe=getWeixinSubscribe(appId,replyType);
        if(weixinSubscribe==null){
            return ServerResponseEntity.success();
        }
        Integer dataSrc=replyType==0? WxAutoDataSrcType.SUBSCRIBE.value() : WxAutoDataSrcType.SUBSCRIBE_STORE.value();
        WeixinAutoResponse autoResponse=autoResponseService.getWeixinAuto(weixinSubscribe.getSubscribeId(),dataSrc,msgType);
        if(autoResponse==null){
            return ServerResponseEntity.success();
        }
        WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(autoResponse.getTemplateId(),msgType);

        WeixinSubscribeVO weixinSubscribeVO=new WeixinSubscribeVO();
        BeanUtils.copyProperties(managerVO,weixinSubscribeVO);
        weixinSubscribeVO.setSubscribeId(weixinSubscribe.getSubscribeId());
        weixinSubscribeVO.setMsgType(msgType);
        weixinSubscribeVO.setReplyType(replyType);
        weixinSubscribeVO.setAppId(appId);
        return ServerResponseEntity.success(weixinSubscribeVO);
    }

    /**
     * 保存回复内容
     * @param subscribeDTO
     */
    @Override
    public void saveWeixinSubscribe(WeixinSubscribePutDTO subscribeDTO) {
        String appId=subscribeDTO.getAppId();
        Integer replyType=0;
        String msgType=subscribeDTO.getMsgType();
        Long userId= AuthUserContext.get().getUserId();
        WeixinSubscribe weixinSubscribe=getWeixinSubscribe(appId,replyType);
        //保存回复配置信息
        if(weixinSubscribe==null){
            weixinSubscribe=new WeixinSubscribe();
            weixinSubscribe.setSubscribeId(String.valueOf(RandomUtil.getUniqueNum()));
            weixinSubscribe.setCreateTime(new Date());
            weixinSubscribe.setAppId(appId);
            weixinSubscribe.setReplyType(replyType);
            weixinSubscribe.setCreateBy(String.valueOf(userId));
            weixinSubscribe.setDelFlag(0);
            this.save(weixinSubscribe);
        }
        String dataId=weixinSubscribe.getSubscribeId();
        Integer dataSrc=replyType==0? WxAutoDataSrcType.SUBSCRIBE.value() : WxAutoDataSrcType.SUBSCRIBE_STORE.value();

        //保存-素材模板信息
        WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
        BeanUtils.copyProperties(subscribeDTO,templateManagerDTO);
        templateManagerDTO.setDataId(dataId);
        templateManagerDTO.setDataSrc(dataSrc);
        templateManagerService.saveTemplatesAndAutoResponses(templateManagerDTO);
//        WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);

        //保存或更新-微信公共回复内容
//        autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
    }
}
