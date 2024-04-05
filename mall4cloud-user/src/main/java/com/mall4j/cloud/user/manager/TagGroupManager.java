package com.mall4j.cloud.user.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.user.dto.CheckVipCodeSingleTagCountDTO;
import com.mall4j.cloud.user.mapper.TagGroupMapper;
import com.mall4j.cloud.user.model.TagGroup;
import com.mall4j.cloud.user.vo.CheckVipCodeSingleTagCountVO;
import com.mall4j.cloud.user.vo.TagGroupVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TagGroupManager {


    @Autowired
    private TagGroupMapper tagGroupMapper;

    @Autowired
    private MapperFacade mapperFacade;

    public List<TagGroupVO> getTagGroupListByIdList(List<Long> groupIdList){
        List<TagGroup> tagGroupList = tagGroupMapper.selectList(
                new LambdaQueryWrapper<TagGroup>().in(TagGroup::getTagGroupId, groupIdList)
        );

        List<TagGroupVO> tagGroupVOList = mapperFacade.mapAsList(tagGroupList, TagGroupVO.class);

        return tagGroupVOList;
    }


    public TagGroupVO getTagGroupById(Long groupId){

        TagGroup tagGroup = tagGroupMapper.selectById(groupId);

        TagGroupVO tagGroupVO = new TagGroupVO();

        BeanUtils.copyProperties(tagGroup,tagGroupVO);

        return tagGroupVO;
    }

    public List<CheckVipCodeSingleTagCountVO> getSingleCountByVipCode(CheckVipCodeSingleTagCountDTO checkVipCodeSingleTagCountDTO){
        return tagGroupMapper.selectSingleCountByVipCode(checkVipCodeSingleTagCountDTO);
    }

    public List<Integer> authFlagArray(String authFlag){

        if (StringUtils.isNotEmpty(authFlag)){
            String[] split = authFlag.split(",");
            List<Integer> authFlagArrays = Arrays.stream(split).map(i -> Integer.valueOf(i)).collect(Collectors.toList());

            return authFlagArrays;
        }
        return null;
    }

}
