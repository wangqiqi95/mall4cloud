package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.utils.UserStaffRelUtils;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialBrowseRecordMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpMaterialBrowseRecordVO;
import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.mapper.cp.MaterialMapper;
import com.mall4j.cloud.biz.vo.cp.MaterialVO;
import com.mall4j.cloud.biz.vo.cp.MiniMaterialVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.cp.bean.external.msg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 素材表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    //互动雷达，转换成h5的链接地址
    @Value("${scrm.h5url}")
    private String h5url;


    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    MaterialMsgService msgService;
    @Autowired
    MaterialStoreService storeService;
    @Autowired
    CpMaterialLableServiceImpl cpMaterialLableService;
    @Autowired
    CpMaterialBrowseRecordMapper cpMaterialBrowseRecordMapper;
    @Autowired
    CpMaterialUseRecordService materialUseRecordService;
    @Autowired
    private CpMaterialMsgImgService cpMaterialMsgImgService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Override
    public PageVO<MaterialVO> page(PageDTO pageDTO, MaterialPageDTO request) {
        PageVO<MaterialVO> pageVO = PageUtil.doPage(pageDTO, () -> materialMapper.list(request));
        for (MaterialVO materialVO : pageVO.getList()) {
            List<MaterialStore> stores = storeService.listByMatId(materialVO.getId());

            List<Long> storeids = stores.stream().map(MaterialStore::getStoreId).collect(Collectors.toList());
            materialVO.setShopIds(storeids);
            materialVO.setShops(stores);
        }
        return pageVO;
    }

    @Override
    public PageVO<MiniMaterialVO> miniPage(PageDTO pageDTO,MaterialPageDTO request) {

        //根据当前员工部门筛选
        ServerResponseEntity<List<Long>>  responseEntity=staffFeignClient.getOrgAndChildByStaffIds(Arrays.asList(AuthUserContext.get().getUserId()));
        ServerResponseEntity.checkResponse(responseEntity);
        request.setOrgIds(responseEntity.getData());
        return  PageUtil.doPage(pageDTO, () -> materialMapper.miniList(request));
    }

    @Override
    public List<Material> getByIds(MaterialPageDTO dto) {
        return materialMapper.selectByIds(dto);
    }

    @Override
    public Material getById(Long id) {
        return materialMapper.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Material material, List<MaterialMsgDTO> msgList, List<Long> storeIds,List<CpMaterialLableDTO> lableList) {
        if(material.getId()==null) {
            materialMapper.save(material);
        }else{
            materialMapper.update(material);
            // 先将以前的删除
            msgService.deleteByMatId(material.getId(),OriginEumn.MAT);
            storeService.deleteByMatId(material.getId());
            cpMaterialLableService.deleteByMatId(material.getId());
        }

        //素材互动雷达标签
        if(material.getInteractiveRadar()==1 && !CollectionUtils.isEmpty(lableList)){
            for (CpMaterialLableDTO materialLableDTO : lableList) {
                CpMaterialLable cpMaterialLable = new CpMaterialLable();
                cpMaterialLable.setMatId(material.getId());
                cpMaterialLable.setInteractionSecond(materialLableDTO.getInteractionSecond());
                cpMaterialLable.setRadarLabalId(materialLableDTO.getRadarLabalId());
                cpMaterialLable.setRadarLabalName(materialLableDTO.getRadarLabalName());
                cpMaterialLable.setCreateTime(new Date());
                cpMaterialLable.setUpdateTime(new Date());
                cpMaterialLable.setRadarLableValue(materialLableDTO.getRadarLableValue());
                cpMaterialLableService.save(cpMaterialLable);
            }
        }

        for (MaterialMsgDTO dto:msgList){
            MaterialMsg msg = new MaterialMsg(dto,OriginEumn.MAT);
            msg.setMatId(material.getId());
            msg.setCreateBy(material.getCreateBy());
            msg.setCreateTime(material.getCreateTime());
            msg.setUpdateTime(material.getUpdateTime());
            msg.setMediaId(dto.getMediaId());
            msg.setArticleUrl(dto.getArticleUrl());
            msg.setArticleDesc(dto.getArticleDesc());
            msg.setFileName(dto.getFileName());
            msgService.save(msg);

            //ppt、pdf转图片保存
            log.info("ppt、pdf转图片保存 : {}",dto.isChangeFile());
            if(dto.isChangeFile()){
                cpMaterialMsgImgService.formtFileAndSaveTo(msg.getMatId(),msg.getId(),msg.getArticleUrl(),msg.getAppPic());
            }
        }
        if(!CollectionUtils.isEmpty(storeIds)) {
            for (Long storeId : storeIds) {
                MaterialStore store = new MaterialStore();
                store.setCreateTime(material.getCreateTime());
                store.setUpdateTime(material.getUpdateTime());
                store.setMatId(material.getId());
                store.setStoreId(storeId);
                storeService.save(store);
            }
        }
    }

    @Override
    public void update(Material material) {
        materialMapper.update(material);
    }

    @Override
    public void deleteById(Long id) {
        materialMapper.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        materialMapper.deleteByIds(ids);
    }

    @Override
    @Transactional
    public void syncCpMaterialStatus() {
        List<Long> expireMaterialIds = materialMapper.getExpireMaterialIds();
        if(CollectionUtil.isNotEmpty(expireMaterialIds)){
            materialMapper.updateMaterialStatusByIds(expireMaterialIds);
        }
    }

    @Override
    public PageVO<CpMaterialBrowseRecordVO> browsePage(PageDTO pageDTO, MaterialBrowsePageDTO request) {
        PageVO<CpMaterialBrowseRecordVO> pageVO = PageUtil.doPage(pageDTO, () -> cpMaterialBrowseRecordMapper.list(request));
        if(CollUtil.isNotEmpty(pageVO.getList())){
            List<String> unionIds=pageVO.getList().stream().map(item->item.getUnionId()).collect(Collectors.toList());
            UserStaffCpRelationSearchDTO dto=new UserStaffCpRelationSearchDTO();
            dto.setPageNum(1);
            dto.setPageSize(100);
            dto.setUnionIds(unionIds);
            //TODO 客户信息：昵称、备注、手机号、标签信息
            ServerResponseEntity<List<UserStaffCpRelationListVO>>  responseEntity=userStaffCpRelationFeignClient.getUserStaffRelAll(dto);
            ServerResponseEntity.checkResponse(responseEntity);
//            Map<String,UserStaffCpRelationListVO> relationListVOMap= LambdaUtils.toMap(responseEntity.getData(),UserStaffCpRelationListVO::getUserUnionId);
            Map<String,UserStaffCpRelationListVO> relationListVOMap = UserStaffRelUtils.mapByStaffIdAndUnionId(responseEntity.getData());
            for (CpMaterialBrowseRecordVO recordVO : pageVO.getList()) {
                String key=recordVO.getUnionId()+recordVO.getStaffId();
                if(relationListVOMap.containsKey(key)){
                    recordVO.setNickName(relationListVOMap.get(key).getQiWeiNickName());
                    recordVO.setRemark(relationListVOMap.get(key).getCpRemark());
                    recordVO.setPhone(relationListVOMap.get(key).getCpRemarkMobiles());
                }
            }
        }
        return pageVO;
    }

    @Override
    public List<CpMaterialBrowseRecordVO> soldBrowsePage(MaterialBrowsePageDTO request) {
        List<CpMaterialBrowseRecordVO> records=cpMaterialBrowseRecordMapper.list(request);
        return mapperFacade.mapAsList(records,CpMaterialBrowseRecordVO.class);
    }

    @Override
    public void changeMenu(MaterialChangeMenuDTO request) {
        materialMapper.changeMenu(request.getMatId(),request.getMatType());
    }


    @Override
    @Transactional
    public MsgAttachment useAndFindAttachmentById(Long id, Long staffId) {
        Material material = this.getById(id);
        if(material==null){
            return null;
        }
        List<MaterialMsg> msgList = msgService.listByMatId(id,OriginEumn.MAT);
        if(CollectionUtil.isEmpty(msgList)){
            return null;
        }
        MaterialMsg msg = msgList.get(0);
        //判断是否开启互动雷达，如果没有开启互动雷达，则直接组装附件对象返回
        MsgAttachment attachment = new MsgAttachment();
        attachment.setMsgType(msg.getType());
        attachment.setLocalUrl(msg.getUrl());
        attachment.setInteractiveRadar(material.getInteractiveRadar());
        attachment.setAttachmentData(JSONObject.parseObject(JSON.toJSONString(msg)));
        if(material.getInteractiveRadar()==0){
            addAttachment(msg,attachment,material);
        }else{
            //如果开启了素材雷达

            //替换 素材id，导购id字段
            String url = h5url.replace("MATERIALID",""+id).replace("STAFFID",""+staffId);
            Link link = new Link();
            if(msg.getType().equals("h5")){ //图像
                link.setMediaId(msg.getMediaId());
                link.setPicUrl(msg.getPicUrl());
                link.setTitle(msg.getAppTitle());
                link.setUrl(msg.getArticleUrl());
                link.setDesc(msg.getArticleDesc());
                attachment.setLink(link);
            }else{
                link.setTitle(material.getMatName());
                link.setUrl(url);
                attachment.setLink(link);
            }
        }
        materialUseRecordService.use(id,staffId);
        log.info("获取到素材库附件内容,useAndFindAttachmentById -> materialId:{}  attachment：{}",material.getId(),JSON.toJSONString(attachment));
        return attachment;
    }

    @Override
    public MsgAttachment taskUseAndFindAttachmentById(Long id, Long staffId) {
        Material material = this.getById(id);
        if(material==null){
            return null;
        }
        List<MaterialMsg> msgList = msgService.listByMatId(id,OriginEumn.MAT);
        if(CollectionUtil.isEmpty(msgList)){
            return null;
        }
        MaterialMsg msg = msgList.get(0);
        log.info("获取到素材库附件内容1,taskUseAndFindAttachmentById ->material:{}  MaterialMsg：{}",JSON.toJSONString(material),JSON.toJSONString(msg));
        //判断是否开启互动雷达，如果没有开启互动雷达，则直接组装附件对象返回
        MsgAttachment attachment = new MsgAttachment();
        attachment.setMsgType(msg.getType());
        attachment.setLocalUrl(msg.getUrl());
        attachment.setInteractiveRadar(material.getInteractiveRadar());
        if(material.getInteractiveRadar()==0){
            addAttachment(msg,attachment,material);
        }else{
            //如果开启了素材雷达
            MsgAttachment attachmentData=addAttachment(msg,attachment,material);
            attachmentData.init();
            attachment.setAttachmentData(JSONObject.parseObject(JSON.toJSONString(attachmentData)));
            //替换 素材id，导购id字段
            String url = h5url.replace("MATERIALID",""+id).replace("STAFFID",""+staffId);
            Link link = new Link();
            if(msg.getType().equals("h5")){ //图像
                link.setMediaId(msg.getMediaId());
                link.setPicUrl(msg.getPicUrl());
                link.setTitle(msg.getAppTitle());
                link.setUrl(msg.getArticleUrl());
                link.setDesc(msg.getArticleDesc());
                attachment.setLink(link);
            }else{
                link.setTitle(material.getMatName());
                link.setUrl(url);
                attachment.setLink(link);
            }
        }
        materialUseRecordService.use(id,staffId);
        log.info("获取到素材库附件内容2,taskUseAndFindAttachmentById -> materialId:{}  attachment：{}",material.getId(),JSON.toJSONString(attachment));
        return attachment;
    }

    private MsgAttachment addAttachment(MaterialMsg msg,MsgAttachment attachment,Material material){
        attachment.setMsgId(msg.getId());
        MsgAttachment attachmentData=null;
        if(msg.getType().equals("image")){ //图像
            Image image = new Image();
            image.setMediaId(msg.getMediaId());
            attachment.setImage(image);
        }else if(msg.getType().equals("video")){//视频
            Video video = new Video();
            video.setMediaId(msg.getMediaId());
            attachment.setVideo(video);
        }else if(msg.getType().equals("miniprogram")){//小程序
            MiniProgram miniProgram = new MiniProgram();
            miniProgram.setPicMediaId(msg.getMediaId());
            miniProgram.setAppid(msg.getAppId());
            miniProgram.setTitle(msg.getAppTitle());
            miniProgram.setPage(msg.getAppPage());
            attachment.setMiniProgram(miniProgram);
        }else if(msg.getType().equals("h5")){//h5
            Link link = new Link();
            link.setMediaId(msg.getMediaId());
            link.setPicUrl(msg.getPicUrl());
            link.setTitle(msg.getAppTitle());
            link.setUrl(msg.getArticleUrl());
            link.setDesc(msg.getArticleDesc());
            attachment.setLink(link);
        }else if(msg.getType().equals("file")){//文件
            File file = new File();
            file.setMediaId(msg.getMediaId());
            attachment.setFile(file);
        }else if(msg.getType().equals("article")){
            Link link = new Link();
            link.setTitle(material.getMatName());
            link.setUrl(msg.getArticleUrl());
            link.setDesc(msg.getArticleDesc());
            link.setPicUrl(msg.getPicUrl());
            attachment.setLink(link);
        }
        attachmentData=attachment;
        return attachmentData;
    }

    @Override
    public void used(Long id) {
        materialMapper.use(id);
    }
}
