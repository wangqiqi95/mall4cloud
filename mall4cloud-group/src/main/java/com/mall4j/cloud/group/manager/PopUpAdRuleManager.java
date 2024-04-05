package com.mall4j.cloud.group.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.group.bo.AddPopUpAdPushRuleBO;
import com.mall4j.cloud.group.dto.AddPopUpAdPushRuleDTO;
import com.mall4j.cloud.group.dto.UpdatePopUpAdPushRuleDTO;
import com.mall4j.cloud.group.mapper.PopUpAdRuleMapper;
import com.mall4j.cloud.group.model.PopUpAdRule;
import com.mall4j.cloud.group.vo.PopUpAdRuleVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PopUpAdRuleManager {


    @Autowired
    private PopUpAdRuleMapper popUpAdRuleMapper;

    @Autowired
    private MapperFacade mapperFacade;


    public List<PopUpAdRuleVO> getTheRuleByAdId(Long adId){

        List<PopUpAdRule> popUpAdRuleList = popUpAdRuleMapper.selectList(
                new LambdaQueryWrapper<PopUpAdRule>()
                        .eq(PopUpAdRule::getPopUpAdId, adId)
        );

        List<PopUpAdRuleVO> openScreenAdRuleVOS = mapperFacade.mapAsList(popUpAdRuleList, PopUpAdRuleVO.class);

        return openScreenAdRuleVOS;

    }


    @Transactional(rollbackFor = Exception.class)
    public void addBatch(Long popUpAdRuleId, List<AddPopUpAdPushRuleBO> ruleList,
                         Long createUser){
        popUpAdRuleMapper.insertBatch(popUpAdRuleId, ruleList, createUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeIdList(List<Long> ruleIdList){
        popUpAdRuleMapper.deleteBatchIds(ruleIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByAdId(Long adId){
        popUpAdRuleMapper.delete(
                new LambdaQueryWrapper<PopUpAdRule>().eq(PopUpAdRule::getPopUpAdId,adId)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void compareUpdate(PopUpAdRuleVO ruleVO, UpdatePopUpAdPushRuleDTO updatePopUpAdPushRuleDTO, Long userId){
        boolean editFlag = false;
        LambdaUpdateWrapper<PopUpAdRule> updateWrapper = new LambdaUpdateWrapper<>();

        if (!ruleVO.getStartTime().equals(updatePopUpAdPushRuleDTO.getStartTime())){
            editFlag = true;
            updateWrapper
                    .set(PopUpAdRule::getStartTime, updatePopUpAdPushRuleDTO.getStartTime());
        }
        if (!ruleVO.getEndTime().equals(updatePopUpAdPushRuleDTO.getEndTime())){
            editFlag = true;
            updateWrapper
                    .set(PopUpAdRule::getEndTime, updatePopUpAdPushRuleDTO.getEndTime());
        }
        if (editFlag){
            popUpAdRuleMapper.update(null,
                    updateWrapper
                            .set(PopUpAdRule::getUpdateTime, LocalDateTime.now())
                            .set(PopUpAdRule::getUpdateUser, userId)
                            .eq(PopUpAdRule::getPopUpAdRuleId, ruleVO.getPopUpAdRuleId())
            );
        }
    }

}
