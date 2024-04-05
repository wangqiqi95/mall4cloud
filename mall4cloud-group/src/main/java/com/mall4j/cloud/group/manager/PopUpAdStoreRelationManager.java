package com.mall4j.cloud.group.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.platform.dto.StoreSelectedParamFeignDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.mapper.PopUpAdStoreRelationMapper;
import com.mall4j.cloud.group.model.PopUpAdStoreRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopUpAdStoreRelationManager {

    @Autowired
    private PopUpAdStoreRelationMapper popUpAdStoreRelationMapper;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<Long> storeIdList, Long adId){
        popUpAdStoreRelationMapper.insertBatch(storeIdList, adId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByAdId(Long adId){
        popUpAdStoreRelationMapper.delete(
                new LambdaQueryWrapper<PopUpAdStoreRelation>()
                        .eq(PopUpAdStoreRelation::getPopUpAdId, adId)
        );
    }

    public Integer getStoreCountByAdId(Long adId){
        Integer storeCount = popUpAdStoreRelationMapper.selectCount(
                new LambdaQueryWrapper<PopUpAdStoreRelation>()
                        .eq(PopUpAdStoreRelation::getPopUpAdId, adId)
        );

        return storeCount;
    }

    public List<Long> getStoreIdListByAdId(Long adId){
        List<PopUpAdStoreRelation> storeRelationList = popUpAdStoreRelationMapper.selectList(
                new LambdaQueryWrapper<PopUpAdStoreRelation>()
                        .eq(PopUpAdStoreRelation::getPopUpAdId, adId)
        );

        List<Long> storeIdList = storeRelationList.stream()
                .map(PopUpAdStoreRelation::getShopId)
                .collect(Collectors.toList());

        return storeIdList;
    }

    public PageVO<SelectedStoreVO> getStorePage(StoreSelectedParamFeignDTO storeSelectedParamDTO){
        ServerResponseEntity<PageVO<SelectedStoreVO>> pageVOServerResponseEntity = storeFeignClient.selectedPage(storeSelectedParamDTO);

        if (pageVOServerResponseEntity.isSuccess()){
            return pageVOServerResponseEntity.getData();
        }

        return null;
    }
}
