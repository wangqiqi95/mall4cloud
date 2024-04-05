package com.mall4j.cloud.group.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.group.dto.UpdatePopUpAdPageDTO;
import com.mall4j.cloud.group.mapper.PopUpAdPageMapper;
import com.mall4j.cloud.group.model.PopUpAdPage;
import com.mall4j.cloud.group.model.PopUpAdRule;
import com.mall4j.cloud.group.vo.PopUpAdPageVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PopUpAdPageManager {

    @Autowired
    private PopUpAdPageMapper popUpAdPageMapper;

    @Autowired
    private MapperFacade mapperFacade;


    public void addBatch(List<String> pageList, Long userId, Long adId){
        popUpAdPageMapper.insertBatch(pageList, adId, userId);
    }

    public List<PopUpAdPageVO> getListByAdId(Long adId){
        List<PopUpAdPage> popUpAdPageList = popUpAdPageMapper.selectList(
                new LambdaQueryWrapper<PopUpAdPage>()
                        .eq(PopUpAdPage::getPopUpAdId, adId)
        );

        List<PopUpAdPageVO> popUpAdPageVOList = mapperFacade.mapAsList(popUpAdPageList, PopUpAdPageVO.class);

        return popUpAdPageVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void compareUpdate(PopUpAdPageVO pageVO, UpdatePopUpAdPageDTO update, Long userId){

        if (!pageVO.getPageUrl().equals(update.getPageUrl())){
            popUpAdPageMapper.update(null,
                    new LambdaUpdateWrapper<PopUpAdPage>()
                            .set(PopUpAdPage::getPageUrl, update.getPageUrl())
                            .set(PopUpAdPage::getUpdateTime, LocalDateTime.now())
                            .set(PopUpAdPage::getUpdateUser, userId)
                            .eq(PopUpAdPage::getPopUpAdPageId, update.getPopUpAdPageId())
            );
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> pageIdList){
        popUpAdPageMapper.deleteBatchIds(pageIdList);
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeByAdId(Long adId){
        popUpAdPageMapper.delete(new LambdaQueryWrapper<PopUpAdPage>().eq(PopUpAdPage::getPopUpAdId,adId));
    }

}
