package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.chat.KeywordHitRecomdDTO;
import com.mall4j.cloud.biz.dto.cp.CpChatScriptPageDTO;
import com.mall4j.cloud.biz.dto.cp.MaterialPageDTO;
import com.mall4j.cloud.biz.mapper.chat.KeywordHitMapper;
import com.mall4j.cloud.biz.mapper.cp.CpChatScriptMapper;
import com.mall4j.cloud.biz.mapper.cp.MaterialMapper;
import com.mall4j.cloud.biz.service.chat.KeywordHitService;
import com.mall4j.cloud.biz.vo.chat.KeywordHitRecomdVO;
import com.mall4j.cloud.biz.vo.chat.KeywordHitStaffVO;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import com.mall4j.cloud.biz.vo.chat.TimeOutLogVO;
import com.mall4j.cloud.biz.vo.cp.CpChatScriptpageVO;
import com.mall4j.cloud.biz.vo.cp.MiniMaterialVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 命中关键词实现类
 *
 */
@Service
@Slf4j
public class KeywordHitServiceImpl implements KeywordHitService {

    @Autowired
    private KeywordHitMapper hitMapper;
    @Autowired
    private UserStaffCpRelationFeignClient relationFeignClient;
    @Autowired
    private StaffFeignClient feignClient;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private CpChatScriptMapper cpChatScriptMapper;

    @Override
    public PageVO<KeywordHitVO> page(PageDTO pageDTO, KeywordHitDTO dto) {

        if(!initSelect(dto)){
            PageVO<KeywordHitVO> pageVO=new PageVO<KeywordHitVO>();
            pageVO.setPages(1);
            pageVO.setTotal(0L);
            pageVO.setList(null);
            return pageVO;
        }

        PageVO<KeywordHitVO> VO = PageUtil.doPage(pageDTO, () -> hitMapper.list(dto));
        List<KeywordHitVO> list = VO.getList();
        addData(list);
        return PageUtil.mongodbPage(pageDTO, list,VO.getTotal());
    }

    private boolean  initSelect(KeywordHitDTO dto){
        //根据触发人名称搜索
        if(StrUtil.isNotEmpty(dto.getTrigger()) || StrUtil.isNotEmpty(dto.getPhone())){
            List<String> triggers=new ArrayList<>();

            UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
            userStaffCpRelationSearchDTO.initPage();
            userStaffCpRelationSearchDTO.setQiWeiNickName(dto.getTrigger());
            userStaffCpRelationSearchDTO.setUserPhone(dto.getPhone());
            ServerResponseEntity<List<UserStaffCpRelationListVO>> responseEntity=relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            List<String> userQiWeiIds=responseEntity.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
            triggers.addAll(userQiWeiIds);

            StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
            staffQueryDTO.setStaffName(dto.getTrigger());
            staffQueryDTO.setMobile(dto.getPhone());
            ServerResponseEntity<List<StaffVO>> responseEntityStaff=feignClient.findByStaffQueryDTO(staffQueryDTO);
            ServerResponseEntity.checkResponse(responseEntity);
            List<String> staffQiWeiIds=responseEntityStaff.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())).map(item->item.getQiWeiUserId()).distinct().collect(Collectors.toList());
            triggers.addAll(staffQiWeiIds);

            if(CollUtil.isEmpty(userQiWeiIds) && CollUtil.isEmpty(staffQiWeiIds)){
                return false;
            }
            dto.setTrigger(null);
            dto.setTriggers(triggers);
            return true;
        }
        return true;
    }

    @Override
    public List<KeywordHitVO> soldExcel(KeywordHitDTO dto) {

        if(!initSelect(dto)){
            return ListUtil.empty();
        }

        List<KeywordHitVO> list = hitMapper.list(dto);
        addData(list);
        for (KeywordHitVO keywordHitVO : list) {
            //类型 1:1对1 0:社群
            if(StrUtil.isNotEmpty(keywordHitVO.getType())){
                if(keywordHitVO.getType().equals("1")){
                    keywordHitVO.setType("1对1");
                }
                if(keywordHitVO.getType().equals("0")){
                    keywordHitVO.setType("社群");
                }
            }
            if(StrUtil.isNotBlank(keywordHitVO.getStaff())){//如果是当事人，查询员工姓名，否则用已有的提醒员工
                try {
                    List<KeywordHitStaffVO> hitStaffVOS=JSONArray.parseArray(keywordHitVO.getStaff(),KeywordHitStaffVO.class);
                    keywordHitVO.setStaff(CollUtil.isNotEmpty(hitStaffVOS)?hitStaffVOS.stream().map(item->item.getStaffName()).collect(Collectors.joining(",")):null);
                }catch (Exception e){
                    log.error("soldExcel-->staff:{} error:{}",keywordHitVO.getStaff(),e);
                }
            }
        }
        return list;
    }

    private void addData(List<KeywordHitVO> list){
        List<String> staffId = list.stream().map(KeywordHitVO:: getStaffId).collect(Collectors.toList());
        List<String> customId = list.stream().map(KeywordHitVO:: getTriggerId).collect(Collectors.toList());

        StaffQueryDTO staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(staffId);
        ServerResponseEntity<List<StaffVO>> staffList = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffList);
        Map<String,StaffVO> staffVOMap = LambdaUtils.toMap(staffList.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);

        staffDto = new StaffQueryDTO();
        staffDto.setQiWeiUserIds(customId);
        ServerResponseEntity<List<StaffVO>> staffs = feignClient.findByStaffQueryDTO(staffDto);
        ServerResponseEntity.checkResponse(staffs);
        Map<String,StaffVO> staffsVOMap = LambdaUtils.toMap(staffs.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),StaffVO::getQiWeiUserId);

        UserStaffCpRelationSearchDTO userStaffCpRelationSearchDTO = new UserStaffCpRelationSearchDTO();
        userStaffCpRelationSearchDTO.setPageSize(10);
        userStaffCpRelationSearchDTO.setPageNum(1);
        userStaffCpRelationSearchDTO.setQiWeiUserIds(customId);
        ServerResponseEntity<List<UserStaffCpRelationListVO>> cpListVO = relationFeignClient.getUserStaffRelBy(userStaffCpRelationSearchDTO);
        ServerResponseEntity.checkResponse(cpListVO);
        Map<String,UserStaffCpRelationListVO> relationListVOMap = LambdaUtils.toMap(cpListVO.getData().stream().filter(item->StrUtil.isNotEmpty(item.getQiWeiUserId())),UserStaffCpRelationListVO::getQiWeiUserId);


        for (KeywordHitVO hitVO:list) {
            if(staffVOMap.containsKey(hitVO.getStaffId())){
                if(StrUtil.isNotBlank(hitVO.getStaff()) && hitVO.getStaff().contains("[]")){//如果是当事人，查询员工姓名，否则用已有的提醒员工
//                    hitVO.setStaff("[{\"staffId\":"+hitVO.getStaffId()+",\"staffName\":\""+staffVOMap.get(hitVO.getStaffId()).getStaffName()+"\"}]");
                    hitVO.setStaff("[{\"staffId\":\""+hitVO.getStaffId()+"\",\"staffName\":\""+staffVOMap.get(hitVO.getStaffId()).getStaffName()+"\"}]");
                }
                //hitVO.setStaff(staffVOMap.get(hitVO.getStaffId()).getStaffName());
                hitVO.setStaffPhone(staffVOMap.get(hitVO.getStaffId()).getMobile());
            }

            if(staffsVOMap.containsKey(hitVO.getTriggerId())){
                hitVO.setTrigger(staffsVOMap.get(hitVO.getTriggerId()).getStaffName());
                hitVO.setTriggerPhone(staffsVOMap.get(hitVO.getTriggerId()).getMobile());
            }
            if(relationListVOMap.containsKey(hitVO.getTriggerId())){
                hitVO.setTrigger(relationListVOMap.get(hitVO.getTriggerId()).getQiWeiNickName());
                hitVO.setTriggerPhone(relationListVOMap.get(hitVO.getTriggerId()).getCpRemarkMobiles());
            }
        }
    }


    @Override
    public PageVO<KeywordHitRecomdVO> appRecomdPage(KeywordHitRecomdDTO recomdDTO) {
//        Long staffId =  AuthUserContext.get().getUserId();
//        ServerResponseEntity<StaffVO> staffResponseEntity=feignClient.getStaffById(staffId);
//        log.info("移动端推荐回复，员工信息{}：",JSON.toJSONString(staffResponseEntity));
//        if(staffResponseEntity.isFail()){
//            PageVO<KeywordHitRecomdVO> pageVO=new PageVO<KeywordHitRecomdVO>();
//            pageVO.inint();
//            return pageVO;
//        }
//        if(Objects.isNull(staffResponseEntity.getData()) || StrUtil.isEmpty(staffResponseEntity.getData().getQiWeiUserId())){
//            PageVO<KeywordHitRecomdVO> pageVO=new PageVO<KeywordHitRecomdVO>();
//            pageVO.inint();
//            return pageVO;
//        }
//        //根据员工过滤
//        recomdDTO.setStaffUserId(staffResponseEntity.getData().getQiWeiUserId());
        PageDTO pageDTO=new PageDTO();
        pageDTO.setPageNum(recomdDTO.getPageNum());
        pageDTO.setPageSize(recomdDTO.getPageSize());
        PageVO<KeywordHitRecomdVO> pageVO = PageUtil.doPage(pageDTO, () -> hitMapper.selectAppRecomds(recomdDTO));

        //处理推荐素材库内容
        List<String> mIds=pageVO.getList().stream().filter(item-> StrUtil.isNotEmpty(item.getMaterialId())).map(item->item.getMaterialId()).collect(Collectors.toList());
        Map<Long,MiniMaterialVO> materialVOMap=new HashMap<>();
        if(CollUtil.isNotEmpty(mIds)){
            List<String> ids=new ArrayList<>();
            for (String mId : mIds) {
                ids.addAll(Arrays.asList(mId.split(",")));
            }
            ids=ids.stream().distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(ids)){
                MaterialPageDTO materialPageDTO=new MaterialPageDTO();
                materialPageDTO.setIds(ids);
                List<MiniMaterialVO> miniMaterialVOS=materialMapper.selectRecomList(materialPageDTO);
                materialVOMap= LambdaUtils.toMap(miniMaterialVOS,MiniMaterialVO::getId);
            }
        }

        //处理推荐话术内容
        List<String> spcIds=pageVO.getList().stream().filter(item-> StrUtil.isNotEmpty(item.getSpeechcraftId())).map(item->item.getSpeechcraftId()).collect(Collectors.toList());
        Map<Long, CpChatScriptpageVO> chatScriptVOMap=new HashMap<>();
        if(CollUtil.isNotEmpty(spcIds)){
            List<String> ids=new ArrayList<>();
            for (String spcId : spcIds) {
                ids.addAll(Arrays.asList(spcId.split(",")));
            }
            ids=ids.stream().distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(ids)){
                CpChatScriptPageDTO cpChatScriptPageDTO=new CpChatScriptPageDTO();
                cpChatScriptPageDTO.setIds(ids);
                List<CpChatScriptpageVO> scriptpageVOS=cpChatScriptMapper.appRecomdPage(cpChatScriptPageDTO);
                chatScriptVOMap= LambdaUtils.toMap(scriptpageVOS,CpChatScriptpageVO::getId);
            }
        }

        for (KeywordHitRecomdVO keywordHitRecomdVO : pageVO.getList()) {
            keywordHitRecomdVO.setMaterials(new ArrayList<>());
            keywordHitRecomdVO.setScripts(new ArrayList<>());
            if(CollUtil.isNotEmpty(materialVOMap) && StringUtil.isNotEmpty(keywordHitRecomdVO.getMaterialId())){//素材库内容
                for (String mId : keywordHitRecomdVO.getMaterialId().split(",")) {
                    Long materid=Long.parseLong(mId);
                    if(materialVOMap.containsKey(materid)){
                        materialVOMap.get(materid).setContentType(2);
                        keywordHitRecomdVO.getMaterials().add(materialVOMap.get(materid));
                    }
                }
            }
            if(CollUtil.isNotEmpty(chatScriptVOMap) && StringUtil.isNotEmpty(keywordHitRecomdVO.getSpeechcraftId())){//话术内容
                for (String spcId : keywordHitRecomdVO.getSpeechcraftId().split(",")) {
                    Long speechcraftId=Long.parseLong(spcId);
                    if(chatScriptVOMap.containsKey(speechcraftId)){
                        MiniMaterialVO miniMaterialVO=new MiniMaterialVO();
                        miniMaterialVO.setType(""+chatScriptVOMap.get(speechcraftId).getType());
                        miniMaterialVO.setChatScriptType(chatScriptVOMap.get(speechcraftId).getType());
                        miniMaterialVO.setScriptContent(chatScriptVOMap.get(speechcraftId).getScriptContent());
                        miniMaterialVO.setScriptId(chatScriptVOMap.get(speechcraftId).getId());
                        miniMaterialVO.setContentType(2);
                        keywordHitRecomdVO.getMaterials().add(miniMaterialVO);
                    }
                }
            }
        }
        return pageVO;
    }

    @Override
    public List<KeywordHitVO> getTop(KeywordHitDTO dto) {
        return hitMapper.getTop(dto);
    }

}
