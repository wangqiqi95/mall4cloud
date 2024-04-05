package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoCNMsgType;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoMsgType;
import com.mall4j.cloud.biz.dto.WeixinStoreSubscribeDTO;
import com.mall4j.cloud.biz.dto.WeixinStoreSubscribePutDTO;
import com.mall4j.cloud.biz.dto.WeixinStoresPutDTO;
import com.mall4j.cloud.biz.dto.WeixinTemplateManagerDTO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.service.WeiXinTemplateManagerService;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.biz.vo.WeixinStoreSubscribeVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinStoreSubscribe;
import com.mall4j.cloud.biz.mapper.WeixinStoreSubscribeMapper;
import com.mall4j.cloud.biz.service.WeixinStoreSubscribeService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信门店回复内容
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
@Service
public class WeixinStoreSubscribeServiceImpl implements WeixinStoreSubscribeService {

    @Autowired
    private WeixinStoreSubscribeMapper weixinStoreSubscribeMapper;

    @Autowired
    private WeiXinTemplateManagerService templateManagerService;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<WeixinStoreSubscribeVO>  page(PageDTO pageDTO, String appId,String storeName,String msgType) {

        Integer dataSrc=WxAutoDataSrcType.SUBSCRIBE_STORE.value();

        PageVO<WeixinStoreSubscribeVO> pageVO=PageUtil.doPage(pageDTO, () -> weixinStoreSubscribeMapper.getList(appId,
                storeName,
                msgType,
                dataSrc));

        pageVO.getList().stream().peek(item->{

            //门店信息
            if(StringUtils.isNotEmpty(item.getStoreId())){
                StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(item.getStoreId()));
                if(storeVO!=null){
                    item.setStoreName(storeVO.getName());
                }
            }

            if(StringUtils.isNotEmpty(item.getMsgType())){
                item.setMsgTypeName(WxAutoCNMsgType.value(item.getMsgType()));
            }

            //回复内容
//            autoResponseService.getWeixinAutos(item.getId(),dataSrc);
//            WeixinAutoResponse autoResponse=autoResponseService.getWeixinAuto(item.getId(),dataSrc,item.getMsgType());
//            if(autoResponse!=null){
//                WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(autoResponse.getTemplateId(),item.getMsgType());
//                if(managerVO.getTextTemplate()!=null) item.setTextTemplate(managerVO.getTextTemplate());
//                if(managerVO.getImgTemplate()!=null) item.setImgTemplate(managerVO.getImgTemplate());
//                if(managerVO.getMaTemplate()!=null) item.setMaTemplate(managerVO.getMaTemplate());
//                if(managerVO.getNewsTemplate()!=null) item.setNewsTemplate(managerVO.getNewsTemplate());
//            }

        }).collect(Collectors.toList());

        return pageVO;
    }

    @Override
    public WeixinStoreSubscribe getById(String id) {
        return weixinStoreSubscribeMapper.getById(id);
    }

    @Override
    public void save(WeixinStoreSubscribe weixinStoreSubscribe) {
        weixinStoreSubscribeMapper.save(weixinStoreSubscribe);
    }

    @Override
    public void update(WeixinStoreSubscribe weixinStoreSubscribe) {
        weixinStoreSubscribeMapper.update(weixinStoreSubscribe);
    }

    @Override
    public void deleteById(String id) {
        weixinStoreSubscribeMapper.deleteById(String.valueOf(AuthUserContext.get().getUserId()),id);
        //删除原有回复
        autoResponseService.deleteByDataId(id);
    }

    /**
     * 保存门店
     * @param storeSubscribePutDTO
     * @return
     */
    @Override
    public ServerResponseEntity<Void> saveWeixinStoreSubscribe(WeixinStoreSubscribePutDTO storeSubscribePutDTO) {
        String appId=storeSubscribePutDTO.getAppId();
        List<WeixinStoresPutDTO> stores=storeSubscribePutDTO.getStores();
        if(stores.isEmpty()){
            return ServerResponseEntity.showFailMsg("门店列表不能为空");
        }

        for(WeixinStoresPutDTO storesPutDTO:stores){
            //保存门店
            WeixinStoreSubscribe weixinStoreSubscribe = new WeixinStoreSubscribe();
            weixinStoreSubscribe.setStoreId(storesPutDTO.getStoreId());
//            weixinStoreSubscribe.setStoreName(storesPutDTO.getStoreName());
            weixinStoreSubscribe.setStoreName(storesPutDTO.getStationName());
            weixinStoreSubscribe.setId(RandomUtil.getUniqueNumStr());
            weixinStoreSubscribe.setDelFlag(0);
            weixinStoreSubscribe.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
            weixinStoreSubscribe.setCreateTime(new Date());
            weixinStoreSubscribe.setAppId(appId);

            String msgType=null;
            if(storeSubscribePutDTO.getTextTemplate()!=null) msgType= WxAutoMsgType.TEXT.value();
            if(storeSubscribePutDTO.getImgTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.IMAGE.value();
            if(storeSubscribePutDTO.getMaTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.WXMA.value();
            if(storeSubscribePutDTO.getNewsTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.NEWS.value();
            weixinStoreSubscribe.setMsgType(msgType);

            this.save(weixinStoreSubscribe);

            //保存-素材模板信息
            String dataId=weixinStoreSubscribe.getId();
            Integer dataSrc= WxAutoDataSrcType.SUBSCRIBE_STORE.value();
            WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
            BeanUtils.copyProperties(storeSubscribePutDTO,templateManagerDTO);
            templateManagerDTO.setDataId(dataId);
            templateManagerDTO.setDataSrc(dataSrc);
            templateManagerService.saveTemplatesAndAutoResponses(templateManagerDTO);

//            String msgType=null;
//            if(storeSubscribePutDTO.getTextTemplate()!=null) msgType= WxAutoMsgType.TEXT.value();
//            if(storeSubscribePutDTO.getImgTemplate()!=null) msgType= WxAutoMsgType.IMAGE.value();
//            if(storeSubscribePutDTO.getMaTemplate()!=null) msgType= WxAutoMsgType.WXMA.value();
//            if(storeSubscribePutDTO.getNewsTemplate()!=null) msgType= WxAutoMsgType.NEWS.value();

            //保存-素材模板信息
//            WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
//            BeanUtils.copyProperties(storeSubscribePutDTO,templateManagerDTO);
//            templateManagerDTO.setMsgType(msgType);
//            WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);

            //保存公共回复内容
//            String dataId=weixinStoreSubscribe.getId();
//            Integer dataSrc= WxAutoDataSrcType.SUBSCRIBE_STORE.value();
//            autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 更新门店回复内容
     * @param storeSubscribeDTO
     * @return
     */
    @Override
    public ServerResponseEntity<Void> updateWeixinStoreSubscribe(WeixinStoreSubscribeDTO storeSubscribeDTO) {
        WeixinStoreSubscribe weixinStoreSubscribe=getById(storeSubscribeDTO.getId());
        if(weixinStoreSubscribe==null){
            return ServerResponseEntity.showFailMsg("门店回复记录未找到");
        }
        String msgType=null;
        if(storeSubscribeDTO.getTextTemplate()!=null) msgType= WxAutoMsgType.TEXT.value();
        if(storeSubscribeDTO.getImgTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.IMAGE.value();
        if(storeSubscribeDTO.getMaTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.WXMA.value();
        if(storeSubscribeDTO.getNewsTemplate()!=null) msgType=msgType+","+ WxAutoMsgType.NEWS.value();
        weixinStoreSubscribe.setMsgType(msgType);
        this.update(weixinStoreSubscribe);

        //删除原有回复
        autoResponseService.deleteByDataId(weixinStoreSubscribe.getId());

        //保存-素材模板信息
        String dataId=weixinStoreSubscribe.getId();
        Integer dataSrc= WxAutoDataSrcType.SUBSCRIBE_STORE.value();
        WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
        BeanUtils.copyProperties(storeSubscribeDTO,templateManagerDTO);
        templateManagerDTO.setDataId(dataId);
        templateManagerDTO.setDataSrc(dataSrc);
        templateManagerService.saveTemplatesAndAutoResponses(templateManagerDTO);

        //
//        String msgType=null;
//        if(storeSubscribeDTO.getTextTemplate()!=null) msgType= WxAutoMsgType.TEXT.value();
//        if(storeSubscribeDTO.getImgTemplate()!=null) msgType= WxAutoMsgType.IMAGE.value();
//        if(storeSubscribeDTO.getMaTemplate()!=null) msgType= WxAutoMsgType.WXMA.value();
//        if(storeSubscribeDTO.getNewsTemplate()!=null) msgType= WxAutoMsgType.NEWS.value();

        //保存-素材模板信息
//        WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
//        BeanUtils.copyProperties(storeSubscribeDTO,templateManagerDTO);
//        templateManagerDTO.setMsgType(msgType);
//        WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
//
//        //保存公共回复内容
//        String dataId=weixinStoreSubscribe.getId();
//        Integer dataSrc= WxAutoDataSrcType.SUBSCRIBE_STORE.value();
//        autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);

        return ServerResponseEntity.success();
    }

    @Override
    public WeixinStoreSubscribeVO getStoreSubscribeByparam(String appId, String storeId) {
        return weixinStoreSubscribeMapper.getStoreSubscribeByparam(appId,storeId);
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    @Override
    public WeixinStoreSubscribeVO getStoreSubscribeById(String id) {
        WeixinStoreSubscribe weixinStoreSubscribe=getById(id);
        if(weixinStoreSubscribe==null) return null;
        WeixinStoreSubscribeVO subscribeVO=mapperFacade.map(weixinStoreSubscribe,WeixinStoreSubscribeVO.class);

        if(StringUtils.isNotEmpty(subscribeVO.getMsgType())){
            subscribeVO.setMsgTypeName(WxAutoCNMsgType.value(subscribeVO.getMsgType()));
        }

        //获取回复内容
        List<WeixinAutoResponse> autoResponses=autoResponseService.getWeixinAutos(id, WxAutoDataSrcType.SUBSCRIBE_STORE.value());
        if(CollectionUtil.isNotEmpty(autoResponses)){
            for(WeixinAutoResponse autoResponse:autoResponses){
                WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(autoResponse.getTemplateId(),autoResponse.getMsgType());
                if(managerVO.getTextTemplate()!=null) subscribeVO.setTextTemplate(managerVO.getTextTemplate());
                if(managerVO.getImgTemplate()!=null) subscribeVO.setImgTemplate(managerVO.getImgTemplate());
                if(managerVO.getMaTemplate()!=null) subscribeVO.setMaTemplate(managerVO.getMaTemplate());
                if(managerVO.getNewsTemplate()!=null) subscribeVO.setNewsTemplate(managerVO.getNewsTemplate());
            }
        }

        return subscribeVO;
    }
}
