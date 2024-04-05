package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.TagDTO;
import com.mall4j.cloud.biz.mapper.cp.TagGroupMapper;
import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.biz.model.cp.TagGroup;
import com.mall4j.cloud.biz.service.cp.TagGroupService;
import com.mall4j.cloud.biz.service.cp.TagService;
import com.mall4j.cloud.biz.vo.cp.TagGroupVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 标签组配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@Service
public class TagGroupServiceImpl implements TagGroupService {

    private final TagService tagService;
    private final TagGroupMapper tagGroupMapper;
    @Override
    public List<TagGroupVO> list(Long id) {
        return tagGroupMapper.list(id);
    }

    @Override
    public TagGroup getById(Long id) {
        return tagGroupMapper.getById(id);
    }

    @Override
    public void save(TagGroup tagGroup) {
        tagGroupMapper.save(tagGroup);
    }

    @Override
    public void update(TagGroup tagGroup) {
        tagGroupMapper.update(tagGroup);
    }

    @Override
    public void deleteById(Long id) {
        tagGroupMapper.deleteById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAndUpdate(TagGroup tagGroup, List<TagDTO> tags) {
        if(tagGroup.getId()==null){
             this.save(tagGroup);
        }else{
            this.update(tagGroup);
        }
        for(TagDTO tagDTO:tags){
            Tag tag = new Tag();
            tag.setGroupId(tagGroup.getId());
            tag.setTagName(tagDTO.getTagName());
            tag.setStatus(StatusType.YX.getCode());
            if(tagDTO.getStatus()!=null) {
                tag.setStatus(tagDTO.getStatus());
            }
            tag.setId(tagDTO.getId());
            if(tagDTO.getId()==null){
                tag.setFlag(StatusType.WX.getCode());
                tagService.save(tag);
                continue;
            }
            tagService.update(tag);
        }
    }

    @Override
    public void sort(Long id, int newSort,int oldSort, int front) {
        tagGroupMapper.sort(id,newSort,oldSort,front);
    }
}
