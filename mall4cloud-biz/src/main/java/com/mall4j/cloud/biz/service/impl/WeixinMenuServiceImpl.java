package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoMsgType;
import com.mall4j.cloud.biz.wx.wx.constant.WxMpMenuType;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeiXinTemplateManagerService;
import com.mall4j.cloud.biz.service.WeixinAutoResponseService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.biz.vo.*;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMenu;
import com.mall4j.cloud.biz.mapper.WeixinMenuMapper;
import com.mall4j.cloud.biz.service.WeixinMenuService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信菜单表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@Service
public class WeixinMenuServiceImpl implements WeixinMenuService {

    @Autowired
    private WeixinMenuMapper weixinMenuMapper;

    @Autowired
    private WeiXinTemplateManagerService templateManagerService;

    @Autowired
    private WeixinAutoResponseService autoResponseService;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private WeixinWebAppService weixinWebAppService;

    @Override
    public PageVO<WeixinMenu> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinMenuMapper.list());
    }

    /**
     * 菜单列表
     * @param appId
     * @return
     */
    @Override
    public List<WeixinMenuTreeVO> menuTrees(String appId) {
        List<WeixinMenuTreeVO> trees=weixinMenuMapper.quryMenuTreeList(appId);

        trees.stream().peek(item->{
            List<WeixinMenuTreeChildVO> treeChilds=weixinMenuMapper.quryMenuTreeChildList(item.getId(),appId);
            item.setMenuChilds(treeChilds);
        }).collect(Collectors.toList());

        return trees;
    }

    @Override
    public WeixinMenu getById(String id) {
        return weixinMenuMapper.getById(id);
    }

    @Override
    public void save(WeixinMenu weixinMenu) {
        weixinMenuMapper.save(weixinMenu);
    }

    @Override
    public void update(WeixinMenu weixinMenu) {
        weixinMenuMapper.update(weixinMenu);
    }

    @Override
    public void deleteById(String id) {

        WeixinMenu weixinMenu=this.getById(id);
        String appId=weixinMenu.getAppId();
        weixinMenuMapper.deleteById(id);//删除父级
        weixinMenuMapper.deleteChildByFatherId(id);//删除所有子级

        if(weixinMenu!=null){
            if(StringUtils.isEmpty(weixinMenu.getFatherId())){
                //一级菜单重新排序
                List<WeixinMenuTreeVO> trees=weixinMenuMapper.quryMenuTreeList(appId);
                if(trees!=null){
                    Integer orders=trees.size()+1;
                    for(WeixinMenuTreeVO menuTreeVO:trees){
                        orders=orders-1;
                        WeixinMenu menu=new WeixinMenu();
                        menu.setId(menuTreeVO.getId());
                        menu.setUpdateTime(new Date());
                        menu.setOrders(orders);
                        this.update(menu);
                    }
                }
            }else{
                //二级菜单重新排序
                List<WeixinMenuTreeChildVO> treeChilds=weixinMenuMapper.quryMenuTreeChildList(weixinMenu.getFatherId(),appId);
                if(treeChilds!=null){
                    Integer orders=treeChilds.size()+1;
                    for(WeixinMenuTreeChildVO menuTreeChildVO:treeChilds){
                        orders=orders-1;
                        WeixinMenu menu=new WeixinMenu();
                        menu.setId(menuTreeChildVO.getId());
                        menu.setUpdateTime(new Date());
                        menu.setOrders(orders);
                        this.update(menu);
                    }
                }
            }
        }
    }

    /**
     * 菜单位置变更
     * @return
     */
    @Override
    public ServerResponseEntity<Void> updateMenuOrders(WeixinMenuUpdateOrdersDto ordersDto) {
        if(ordersDto.getMenuOrders()!=null){
            for(WeixinMenuUpdateOrderDto orderDto:ordersDto.getMenuOrders()){
                WeixinMenu weixinMenu=new WeixinMenu();
                weixinMenu.setId(orderDto.getId());
                weixinMenu.setOrders(orderDto.getOrders());
                weixinMenu.setUpdateTime(new Date());
                this.update(weixinMenu);
            }
        }
        return ServerResponseEntity.success();
    }

    /**
     * 保存菜单
     * @param menuPutDTO
     */
    @Override
    public ServerResponseEntity<Void> saveWeixinMenu(WeixinMenuPutDTO menuPutDTO) {
        try {
            //检查添加菜单上限
            if(StringUtils.isNotEmpty(menuPutDTO.getFatherId())){
                //二级菜单重新排序
                List<WeixinMenuTreeChildVO> treeChilds=weixinMenuMapper.quryMenuTreeChildList(menuPutDTO.getFatherId(),menuPutDTO.getAppId());
                if(treeChilds!=null){
                    if(treeChilds.size()+1>5){
                        return ServerResponseEntity.showFailMsg("二级菜单最多添加5个");
                    }
                }
            }else {
                //一级菜单重新排序
                List<WeixinMenuTreeVO> trees=weixinMenuMapper.quryMenuTreeList(menuPutDTO.getAppId());
                if(trees!=null){
                    if(trees.size()+1>3){
                        return ServerResponseEntity.showFailMsg("一级菜单最多添加3个");
                    }
                }
            }
            saveOrUpdate(menuPutDTO);
            return ServerResponseEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作失败");
        }

    }

    /**
     * 修改菜单
     * @param menuPutDTO
     * @return
     */
    @Override
    public ServerResponseEntity<Void> updateWeixinMenu(WeixinMenuPutDTO menuPutDTO) {
        try {
            if(getById(menuPutDTO.getId())==null){
                return ServerResponseEntity.showFailMsg("操作失败,菜单项未找到");
            }
            saveOrUpdate(menuPutDTO);
            return ServerResponseEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    //保存或者修改菜单
    private ServerResponseEntity saveOrUpdate(WeixinMenuPutDTO menuPutDTO){
        String appId=menuPutDTO.getAppId();
        String fatherId=menuPutDTO.getFatherId();
        WeixinMenu weixinMenu=null;
        if(!StringUtils.isNotEmpty(fatherId)){
            weixinMenu=weixinMenuMapper.queryByFatherId(fatherId,appId);
            if(weixinMenu!=null){
                return ServerResponseEntity.showFailMsg("未获取到上级菜单");
            }
        }
        weixinMenu=menuPutDTO.getId()!=null?getById(menuPutDTO.getId()):null;

        if(weixinMenu==null){
            weixinMenu=new WeixinMenu();
            BeanUtils.copyProperties(menuPutDTO,weixinMenu);

            Integer orders=0;
            if(StringUtils.isNotEmpty(weixinMenu.getFatherId())){
                orders=queryMaxOrdersByFatherId(weixinMenu.getFatherId());
            }else{
                orders=queryMaxOrders(appId);
            }
            orders++;
            weixinMenu.setOrders(orders);
            weixinMenu.setId(RandomUtil.getUniqueNumStr());
            weixinMenu.setMenuKey(weixinMenu.getId());
            weixinMenu.setDelFlag(0);
            weixinMenu.setCreateTime(new Date());
            weixinMenu.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
            this.save(weixinMenu);
        }else{
            BeanUtils.copyProperties(menuPutDTO,weixinMenu);
            weixinMenu.setUpdateTime(new Date());
            this.update(weixinMenu);
        }
        return ServerResponseEntity.success();
    }

    private int queryMaxOrders(String appId){
        Integer maxOrders= weixinMenuMapper.queryMaxOrders(appId);
        return maxOrders==null?0:maxOrders;
    }

    private int queryMaxOrdersByFatherId(String fatherId){
        Integer maxOrders= weixinMenuMapper.queryMaxOrdersByFatherId(fatherId);
        return maxOrders==null?0:maxOrders;
    }

    /**
     * 保存菜单内容
     * @param contentPutDTO
     * @return
     */
    @Override
    public ServerResponseEntity<Void> saveWeixinMenuContent(WeixinMenuContentPutDTO contentPutDTO) {

        try {
            WeixinMenu weixinMenu=this.getById(contentPutDTO.getId());
            if(weixinMenu==null){
                return ServerResponseEntity.showFailMsg("操作失败");
            }
            String dataId=contentPutDTO.getId();
            Integer dataSrc= WxAutoDataSrcType.MENU.value();
            String appId=weixinMenu.getAppId();

            String msgType=null;
            String menuType=WxMpMenuType.CLICK.value();

            autoResponseService.deleteByDataId(dataId);

            if(contentPutDTO.getTextTemplate()!=null){//消息回复：click
                //保存-素材模板信息(文字)
                msgType= WxAutoMsgType.TEXT.value();
                menuType=WxMpMenuType.CLICK.value();
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                BeanUtils.copyProperties(contentPutDTO,templateManagerDTO);
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(msgType);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
            }
            if(contentPutDTO.getImgTemplate()!=null){//消息回复：click
                //保存-素材模板信息（图片）
                msgType=WxAutoMsgType.IMAGE.value();
                menuType=WxMpMenuType.CLICK.value();
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                BeanUtils.copyProperties(contentPutDTO,templateManagerDTO);
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(msgType);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
            }
            if(contentPutDTO.getMaTemplate()!=null){//跳转：miniprogram
                //保存-素材模板信息（小程序）
                msgType=WxAutoMsgType.WXMA.value();
                menuType=WxMpMenuType.MINIPROGRAM.value();
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                BeanUtils.copyProperties(contentPutDTO,templateManagerDTO);
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(msgType);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
            }
            if(contentPutDTO.getShopMallTemplate()!=null){//跳转：miniprogram
                //保存-素材模板信息（小程序 --商城功能）
                msgType=WxAutoMsgType.SHOP_MALL.value();
                menuType=WxMpMenuType.MINIPROGRAM.value();
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                BeanUtils.copyProperties(contentPutDTO,templateManagerDTO);
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(msgType);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
            }
            if(contentPutDTO.getNewsTemplate()!=null){//消息回复：click
                //保存-素材模板信息（图文）
                msgType=WxAutoMsgType.NEWS.value();
                menuType=WxMpMenuType.CLICK.value();
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                BeanUtils.copyProperties(contentPutDTO,templateManagerDTO);
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(msgType);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
            }
            if(contentPutDTO.getLinksucai()!=null){//跳转: view
                //保存-素材模板信息(外部链接)
                msgType= WxAutoMsgType.EXTERNAL_LINK.value();
                menuType=WxMpMenuType.VIEW.value();
                WeixinTemplateManagerDTO templateManagerDTO=new WeixinTemplateManagerDTO();
                BeanUtils.copyProperties(contentPutDTO,templateManagerDTO);
                templateManagerDTO.setAppId(appId);
                templateManagerDTO.setMsgType(msgType);
                WeixinTemplageManagerVO managerVO=templateManagerService.saveTemplate(templateManagerDTO);
                //保存或更新-微信公共回复内容
                autoResponseService.saveWeixinAutoResponse(dataId,dataSrc,managerVO);
            }

            weixinMenu=new WeixinMenu();
            weixinMenu.setId(contentPutDTO.getId());
            weixinMenu.setMsgtype(msgType);
            weixinMenu.setMenuType(menuType);
            weixinMenu.setUpdateTime(new Date());
            this.update(weixinMenu);

            return ServerResponseEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作失败");
        }

    }

    /**
     * 获取菜单内容
     * @param id
     * @return
     */
    @Override
    public ServerResponseEntity<WeixinMenuContentVo> getWeixinMenuContent(String id) {
        WeixinMenu weixinMenu=getById(id);
        if(weixinMenu==null){
            return ServerResponseEntity.success();
        }
        List<WeixinAutoResponse> autoResponse=autoResponseService.getWeixinAutos(weixinMenu.getId(), WxAutoDataSrcType.MENU.value());
        if(autoResponse==null || autoResponse.isEmpty()){
            return ServerResponseEntity.success();
        }

        WeixinMenuContentVo menuContentVo=new WeixinMenuContentVo();
        menuContentVo.setId(id);

        autoResponse.stream().peek(item->{
            if(item.getMsgType().equals(WxAutoMsgType.TEXT.value())){
                WeixinTemplageManagerVO managerVO_text=templateManagerService.getTemplate(item.getTemplateId(), WxAutoMsgType.TEXT.value());
                menuContentVo.setTextTemplate(managerVO_text.getTextTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.IMAGE.value())){
                WeixinTemplageManagerVO managerVO_image=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.IMAGE.value());
                menuContentVo.setImgTemplate(managerVO_image.getImgTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.WXMA.value())){
                WeixinTemplageManagerVO managerVO_wxma=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.WXMA.value());
                menuContentVo.setMaTemplate(managerVO_wxma.getMaTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.NEWS.value())){
                WeixinTemplageManagerVO managerVO_news=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.NEWS.value());
                menuContentVo.setNewsTemplate(managerVO_news.getNewsTemplate());
            }
            if(item.getMsgType().equals(WxAutoMsgType.EXTERNAL_LINK.value())){
                WeixinTemplageManagerVO managerVO_link=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.EXTERNAL_LINK.value());
                menuContentVo.setLinksucai(managerVO_link.getLinksucai());
            }
            if(item.getMsgType().equals(WxAutoMsgType.SHOP_MALL.value())){
                WeixinTemplageManagerVO managerVO_shopmall=templateManagerService.getTemplate(item.getTemplateId(),WxAutoMsgType.SHOP_MALL.value());
                menuContentVo.setShopMallTemplate(managerVO_shopmall.getShopMallTemplate());
            }
        }).collect(Collectors.toList());
        return ServerResponseEntity.success(menuContentVo);
    }

    /**
     * 发布菜单
     * @param appId
     * @return
     */
    @Override
    public ServerResponseEntity<Void> doSyncMenu(String appId) {
        WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(appId);
        if(weixinWebApp==null){
            return ServerResponseEntity.showFailMsg("公众号未授权");
        }
        if(StringUtils.isEmpty(weixinWebApp.getWeixinAppSecret())){
            return ServerResponseEntity.showFailMsg("公众号 secret 未配置");
        }
        try {
            //主菜单
            List<WeixinMenuTreeVO> trees=weixinMenuMapper.quryMenuTreeList(appId);

            if(CollectionUtil.isEmpty(trees)){
                return ServerResponseEntity.showFailMsg("请先添加菜单");
            }

            WxMenu menu = new WxMenu();

            trees.stream().peek(item->{
                //一级级菜单
                WxMenuButton wxMenuButton1=new WxMenuButton();
                wxMenuButton1.setAppId(item.getAppId());
                wxMenuButton1.setKey(item.getMenuKey());
                wxMenuButton1.setName(item.getMenuName());
                if(StringUtils.isNotEmpty(item.getMenuType())){
                    wxMenuButton1.setType(item.getMenuType());
                }
                if(StringUtils.isNotEmpty(item.getUrl())){
                    wxMenuButton1.setUrl(item.getUrl());
                }
                //二级菜单
                List<WeixinMenuTreeChildVO> treeChilds=weixinMenuMapper.quryMenuTreeChildList(item.getId(),appId);

                //添加回复内容
                addMenuContent(item.getId(),wxMenuButton1,true,treeChilds.size());

                if(!treeChilds.isEmpty()){
                    treeChilds.stream().peek(item2->{
                        WxMenuButton wxMenuButton2=new WxMenuButton();
                        wxMenuButton2.setAppId(item2.getAppId());
                        wxMenuButton2.setKey(item2.getMenuKey());
                        wxMenuButton2.setType(item2.getMenuType());
                        wxMenuButton2.setName(item2.getMenuName());
                        wxMenuButton2.setUrl(item2.getUrl());
                        //添加回复内容
                        addMenuContent(item2.getId(),wxMenuButton2,false,0);
                        //添加二级菜单
                        wxMenuButton1.getSubButtons().add(wxMenuButton2);
                    }).collect(Collectors.toList());
                }

                //添加一级菜单
                menu.getButtons().add(wxMenuButton1);
            }).collect(Collectors.toList());

            WxMp wxMp=new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            wxConfig.getWxMpService(wxMp).getMenuService().menuCreate(menu);
            return ServerResponseEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("菜单发布失败: "+e.getMessage());
        }

    }


    /**
     * 菜单内容
     * @param dataId
     * @param wxMenuButton
     */
    private void addMenuContent(String dataId,WxMenuButton wxMenuButton,boolean isParent,int childSize){
        if((isParent&&childSize<=0) || !isParent){
            if(StringUtils.isEmpty(wxMenuButton.getType())){
                throw new LuckException("菜单："+wxMenuButton.getName()+" 消息类型不正确或者回复内容为空");
            }
        }
        List<WeixinAutoResponse> autoResponse=autoResponseService.getWeixinAutos(dataId, WxAutoDataSrcType.MENU.value());

        if(CollectionUtil.isEmpty(autoResponse)
                && !isParent
                && (wxMenuButton.getType().equals(WxMpMenuType.MINIPROGRAM) || wxMenuButton.getType().equals(WxMpMenuType.VIEW))){
            throw new LuckException("菜单："+wxMenuButton.getName()+" 内容配置为空");
        }

        autoResponse.stream().peek(itemContent->{
            String templateId=itemContent.getTemplateId();
            if(itemContent.getMsgType().equals(WxAutoMsgType.TEXT.value())){//消息回复
//                WeixinTemplageManagerVO managerVO_text=templateManagerService.getTemplate(templateId, WxAutoMsgType.TEXT.value());
            }
            if(itemContent.getMsgType().equals(WxAutoMsgType.IMAGE.value())){//消息回复
//                WeixinTemplageManagerVO managerVO_image=templateManagerService.getTemplate(templateId,WxAutoMsgType.IMAGE.value());
            }
            if(itemContent.getMsgType().equals(WxAutoMsgType.WXMA.value())){//菜单点击跳转
                WeixinTemplageManagerVO managerVO_wxma=templateManagerService.getTemplate(templateId,WxAutoMsgType.WXMA.value());
                if(managerVO_wxma==null || managerVO_wxma.getMaTemplate()==null){
                    throw new LuckException("菜单："+wxMenuButton.getName()+" 小程序配置信息为空");
                }
                WeixinMaTemplateVO weixinMaTemplateVO=managerVO_wxma.getMaTemplate();
                if(StringUtils.isEmpty(weixinMaTemplateVO.getMaAppId()) || StringUtils.isEmpty(weixinMaTemplateVO.getMaAppPath())){
                    throw new LuckException("菜单："+wxMenuButton.getName()+" 小程序配置信息有误");
                }
                wxMenuButton.setAppId(weixinMaTemplateVO.getMaAppId());
                wxMenuButton.setPagePath(weixinMaTemplateVO.getMaAppPath());
                wxMenuButton.setUrl(StringUtils.isNotEmpty(weixinMaTemplateVO.getMaUrl())?weixinMaTemplateVO.getMaUrl(): WechatConstants.WEIXIN_DOMAIN);
            }
            if(itemContent.getMsgType().equals(WxAutoMsgType.NEWS.value())){//消息回复
//                WeixinTemplageManagerVO managerVO_news=templateManagerService.getTemplate(templateId,WxAutoMsgType.NEWS.value());
            }
            if(itemContent.getMsgType().equals(WxAutoMsgType.EXTERNAL_LINK.value())){//菜单点击跳转
                WeixinTemplageManagerVO managerVO_link=templateManagerService.getTemplate(templateId,WxAutoMsgType.EXTERNAL_LINK.value());
                if(managerVO_link==null || managerVO_link.getLinksucai()==null){
                    throw new LuckException("菜单："+wxMenuButton.getName()+" 跳转外链内容为空");
                }
                if(StringUtils.isEmpty(managerVO_link.getLinksucai().getOuterLink())){
                    throw new LuckException("菜单："+wxMenuButton.getName()+" 跳转外链内容为空");
                }
                wxMenuButton.setUrl(managerVO_link.getLinksucai().getOuterLink());
            }
            if(itemContent.getMsgType().equals(WxAutoMsgType.SHOP_MALL.value())){//商城功能
                WeixinTemplageManagerVO managerVO_wxma=templateManagerService.getTemplate(templateId,WxAutoMsgType.SHOP_MALL.value());
                if(managerVO_wxma==null || managerVO_wxma.getShopMallTemplate()==null){
                    throw new LuckException("菜单："+wxMenuButton.getName()+" 商城功能配置信息为空");
                }
                WeixinShopMallTemplateVo shopMallTemplateVo=managerVO_wxma.getShopMallTemplate();
                if(StringUtils.isEmpty(shopMallTemplateVo.getMaAppId()) || StringUtils.isEmpty(shopMallTemplateVo.getMaAppPath())){
                    throw new LuckException("菜单："+wxMenuButton.getName()+" 商城功能配置信息有误");
                }
                wxMenuButton.setAppId(shopMallTemplateVo.getMaAppId());
                wxMenuButton.setPagePath(shopMallTemplateVo.getMaAppPath());
                wxMenuButton.setUrl(StringUtils.isNotEmpty(shopMallTemplateVo.getMaUrl())?shopMallTemplateVo.getMaUrl(): WechatConstants.WEIXIN_DOMAIN);
            }
        }).collect(Collectors.toList());
    }


}
