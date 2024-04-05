package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.dto.WeixinAutoMsgPutDTO;
import com.mall4j.cloud.biz.dto.WeixinTemplateManagerDTO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.service.WeiXinTemplateManagerService;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.biz.vo.WeixinAutoMsgVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoMsg;
import com.mall4j.cloud.biz.mapper.WeixinAutoMsgMapper;
import com.mall4j.cloud.biz.service.WeixinAutoMsgService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 微信消息自动回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:52:24
 */
@Service
public class WeixinAutoMsgServiceImpl implements WeixinAutoMsgService {

    @Autowired
    private WeixinAutoMsgMapper weixinAutoMsgMapper;

    @Autowired
    private WeiXinTemplateManagerService templateManagerService;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Override
    public PageVO<WeixinAutoMsg> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinAutoMsgMapper.list());
    }

    @Override
    public WeixinAutoMsg getById(Long id) {
        return weixinAutoMsgMapper.getById(id);
    }

    @Override
    public void save(WeixinAutoMsg weixinAutoMsg) {
        weixinAutoMsgMapper.save(weixinAutoMsg);
    }

    @Override
    public void update(WeixinAutoMsg weixinAutoMsg) {
        weixinAutoMsgMapper.update(weixinAutoMsg);
    }

    @Override
    public void deleteById(Long id) {
        weixinAutoMsgMapper.deleteById(id);
    }

    @Override
    public WeixinAutoMsg getWeixinAutoMsg(String appId) {
        return weixinAutoMsgMapper.getWeixinAutoMsg(appId);
    }

    /**
     * 获取回复内容
     * @param appId
     * @param msgType
     * @return
     */
    @Override
    public ServerResponseEntity<WeixinAutoMsgVO> getWeixinAutoMsgVO(String appId, String msgType) {
        WeixinAutoMsg autoMsg=getWeixinAutoMsg(appId);
        if(autoMsg==null){
            return ServerResponseEntity.success();
        }
        WeixinAutoResponse autoResponse=autoResponseService.getWeixinAuto(autoMsg.getId(),WxAutoDataSrcType.AUTO_MSG.value(), msgType);
        if(autoResponse==null){
            return ServerResponseEntity.success();
        }
        WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(autoResponse.getTemplateId(),msgType);

        WeixinAutoMsgVO autoMsgVO=new WeixinAutoMsgVO();
        BeanUtils.copyProperties(managerVO,autoMsgVO);
        autoMsgVO.setId(autoMsg.getId());
        autoMsgVO.setMsgType(msgType);
        autoMsgVO.setAppId(appId);
        return ServerResponseEntity.success(autoMsgVO);
    }

    /**
     * 保存默认回复内容
     * @param autoMsgPutDTO
     */
    @Override
    public void saveWeixinAutoMsg(WeixinAutoMsgPutDTO autoMsgPutDTO) {
        String appId=autoMsgPutDTO.getAppId();
        String msgType=autoMsgPutDTO.getMsgType();
        Long userId= AuthUserContext.get().getUserId();
        WeixinAutoMsg autoMsg=getWeixinAutoMsg(appId);
        //保存回复配置信息
        if(autoMsg==null){
            autoMsg=new WeixinAutoMsg();
            autoMsg.setId(String.valueOf(RandomUtil.getUniqueNum()));
            autoMsg.setCreateTime(new Date());
            autoMsg.setAppId(appId);
            autoMsg.setCreateBy(String.valueOf(userId));
            autoMsg.setDelFlag(0);
            this.save(autoMsg);
        }
        String dataId=autoMsg.getId();
        Integer dataSrc=WxAutoDataSrcType.AUTO_MSG.value();

        //保存-素材模板信息
        WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
        BeanUtils.copyProperties(autoMsgPutDTO,templateManagerDTO);
        templateManagerDTO.setDataId(dataId);
        templateManagerDTO.setDataSrc(dataSrc);
        templateManagerService.saveTemplatesAndAutoResponses(templateManagerDTO);
//        BeanUtils.copyProperties(autoMsgPutDTO,templateManagerDTO);
//        WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
//
//        //保存或更新-微信公共回复内容
//        WeixinAutoResponse autoResponse=autoResponseService.getWeixinAuto(dataId,dataSrc,msgType);
//        if(autoResponse==null){
//            autoResponse=new WeixinAutoResponse();
//            autoResponse.setId(RandomUtil.getUniqueNumStr());
//            autoResponse.setMsgType(msgType);
//            autoResponse.setDataId(dataId);
//            autoResponse.setDataSrc(dataSrc);
//            autoResponse.setTemplateId(managerVO.getTemplateId());
//            autoResponse.setTemplateName(managerVO.getTemplateName());
//            autoResponse.setCreateBy(String.valueOf(userId));
//            autoResponse.setCreateTime(new Date());
//            autoResponse.setDelFlag(0);
//            autoResponseService.save(autoResponse);
//        }else{
//            autoResponse.setTemplateId(managerVO.getTemplateId());
//            autoResponse.setTemplateName(managerVO.getTemplateName());
//            autoResponse.setUpdateBy(String.valueOf(userId));
//            autoResponse.setUpdateTime(new Date());
//            autoResponseService.update(autoResponse);
//        }
    }
}
