package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.dto.chat.KeywordDTO;
import com.mall4j.cloud.biz.mapper.chat.KeyCustomMapper;
import com.mall4j.cloud.biz.mapper.chat.KeyWordLabelMapper;
import com.mall4j.cloud.biz.mapper.chat.KeywordMapper;
import com.mall4j.cloud.biz.model.chat.KeyCustom;
import com.mall4j.cloud.biz.model.chat.KeyWordLabel;
import com.mall4j.cloud.biz.model.chat.KeyWordRecomd;
import com.mall4j.cloud.biz.model.chat.Keyword;
import com.mall4j.cloud.biz.service.chat.KeyWordRecomdService;
import com.mall4j.cloud.biz.service.chat.KeywordService;
import com.mall4j.cloud.biz.vo.chat.KeywordVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Struct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 会话关键词实现类
 *
 */
@Service
public class KeywordServiceImpl implements KeywordService {

    @Autowired
    private KeywordMapper keywordMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private KeyCustomMapper customMapper;
    @Autowired
    private KeyWordLabelMapper labelMapper;
    @Autowired
    private KeyWordRecomdService keyWordRecomdService;

    @Override
    public PageVO<KeywordVO> page(PageDTO pageDTO, KeywordDTO keyword) {
        PageVO<KeywordVO> VO = PageUtil.doPage(pageDTO, () -> keywordMapper.list(keyword));
        List<KeywordVO> list = VO.getList();
        List<Long> customId = list.stream().map(KeywordVO:: getCustom).collect(Collectors.toList());
        Map<Long,KeyCustom> customMap = new HashMap<>();
        if(customId != null && customId.size()>0){
            List<KeyCustom> customs = customMapper.getByIds(customId);
            customMap = customs.stream().collect(Collectors.toMap(KeyCustom::getId, Function.identity()));
        }
        for (KeywordVO v:list) {
            v.setCustomList(customMap.get(v.getCustom()));
            v.setLabelList(v.getTags());
            /*String label = v.getLabel();
            if(StringUtils.isNotBlank(label)){
                List<String> labelList = Arrays.asList(label.split(","));
                List<KeyWordLabel> listLab = labelMapper.getByTagId(labelList);
                v.setLabelList(listLab);
            }*/
        }
        return PageUtil.mongodbPage(pageDTO, list,VO.getTotal());
        //return PageUtil.doPage(pageDTO, () -> keywordMapper.list(keyword));
    }


    @Override
    public void update(KeywordDTO dto) {
        Keyword keyword = mapperFacade.map(dto, Keyword.class);
        KeyCustom custom = dto.getCustomList();
        if(custom != null){
            KeyCustom cust = customMapper.getByName(custom);
            if(cust == null){
                customMapper.save(custom);
            }else{
                custom = cust;
            }
        }
        keyword.setTags(dto.getLabelList());
        /*List<KeyWordLabel> labelList=dto.getLabelList();
        List<KeyWordLabel> addLabel = new ArrayList<>();
        for (KeyWordLabel lab:labelList) {
            KeyWordLabel wordLabel = labelMapper.getByName(lab);
            if(wordLabel == null){
                addLabel.add(lab);
            }
        }
        if(addLabel!=null && addLabel.size()>0){
            labelMapper.batchInsert(addLabel);
        }
        if(labelList !=null && labelList.size()>0){
            List<String> ab = labelList.stream().map(KeyWordLabel ::getTagId).collect(Collectors.toList());
            keyword.setLabel(String.join(",",ab));
        }*/
        if(custom != null){
            keyword.setCustom(custom.getId());
        }
        keyword.setCreateName(AuthUserContext.get().getUsername());
        keywordMapper.update(keyword);

        insertKeyWordRecomd(keyword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        keywordMapper.deleteById(id);
    }

    @Override
    public void saveKeyword(KeywordDTO dto) {
        Keyword keyword = mapperFacade.map(dto, Keyword.class);
        KeyCustom custom = dto.getCustomList();
        if(custom != null){
           KeyCustom cust = customMapper.getByName(custom);
            if(cust == null){
                 customMapper.save(custom);
            }else{
                custom = cust;
            }
        }
        /*List<KeyWordLabel> labelList=dto.getLabelList();
        List<KeyWordLabel> addLabel = new ArrayList<>();
        for (KeyWordLabel lab:labelList) {
            KeyWordLabel wordLabel = labelMapper.getByName(lab);
            if(wordLabel == null){
                addLabel.add(lab);
            }
        }
        if(addLabel!=null && addLabel.size()>0){
            labelMapper.batchInsert(addLabel);
        }
        if(labelList !=null && labelList.size()>0){
            List<String> ab = labelList.stream().map(KeyWordLabel ::getTagId).collect(Collectors.toList());
            keyword.setLabel(String.join(",",ab));
        }*/
        keyword.setTags(dto.getLabelList());
        if(custom != null){
            keyword.setCustom(custom.getId());
        }
        keyword.setCreateName(AuthUserContext.get().getUsername());
        keywordMapper.save(keyword);

        insertKeyWordRecomd(keyword);
    }

    /**
     * 保存关键词推荐回复内容
     * @param keyword
     */
    private void insertKeyWordRecomd(Keyword keyword){

        Long keyWordId=keyword.getId();
        keyWordRecomdService.deleteByKeyWordId(keyWordId);

        if(StrUtil.isNotEmpty(keyword.getMaterialId())){
            List<KeyWordRecomd> keyWordRecomds=new ArrayList<>();
            List<String> materialIds=Arrays.asList(keyword.getMaterialId().split(","));
            for (String materialId : materialIds) {
                KeyWordRecomd keyWordRecomd=new KeyWordRecomd();
                keyWordRecomd.setKeyWordId(keyWordId);
                keyWordRecomd.setCreateTime(new Date());
                keyWordRecomd.setRecomdId(materialId);
                keyWordRecomd.setRecomdType(1);
                keyWordRecomd.setCreateBy(AuthUserContext.get().getUsername());
                keyWordRecomds.add(keyWordRecomd);
            }
            keyWordRecomdService.saveBatch(keyWordRecomds);
        }
        if(StrUtil.isNotEmpty(keyword.getSpeechcraftId())){
            List<KeyWordRecomd> keyWordRecomds=new ArrayList<>();
            List<String> speechcraftIds=Arrays.asList(keyword.getSpeechcraftId().split(","));
            for (String speechcraftId : speechcraftIds) {
                KeyWordRecomd keyWordRecomd=new KeyWordRecomd();
                keyWordRecomd.setKeyWordId(keyWordId);
                keyWordRecomd.setCreateTime(new Date());
                keyWordRecomd.setRecomdId(speechcraftId);
                keyWordRecomd.setRecomdType(2);
                keyWordRecomd.setCreateBy(AuthUserContext.get().getUsername());
                keyWordRecomds.add(keyWordRecomd);
            }
            keyWordRecomdService.saveBatch(keyWordRecomds);
        }
    }

    @Override
    public KeywordVO getDetail(Long id) {
        KeywordVO vo = keywordMapper.getById(id);
        if(vo != null ){
            List<Long> customId = new ArrayList<>();
            customId.add(vo.getCustom());
            List<KeyCustom> customs = customMapper.getByIds(customId);
            Map<Long,KeyCustom> customMap = customs.stream().collect(Collectors.toMap(KeyCustom::getId, Function.identity()));
            vo.setCustomList(customMap.get(vo.getCustom()));
            vo.setLabelList(vo.getTags());
            /*if(StringUtils.isNotBlank(label)){
                List<String> labelList = Arrays.asList(label.split(","));
                List<KeyWordLabel> listLab = labelMapper.getByTagId(labelList);
                vo.setLabelList(listLab);
            }*/
        }
        return vo;
    }
}
