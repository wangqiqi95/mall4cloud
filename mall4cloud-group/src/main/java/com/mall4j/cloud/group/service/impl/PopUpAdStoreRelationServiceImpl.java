package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.platform.dto.StoreSelectedParamFeignDTO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.QueryStorePageByAdPageDTO;
import com.mall4j.cloud.group.manager.PopUpAdStoreRelationManager;
import com.mall4j.cloud.group.model.PopUpAdStoreRelation;
import com.mall4j.cloud.group.mapper.PopUpAdStoreRelationMapper;
import com.mall4j.cloud.group.service.PopUpAdStoreRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
@Service
public class PopUpAdStoreRelationServiceImpl extends ServiceImpl<PopUpAdStoreRelationMapper, PopUpAdStoreRelation> implements PopUpAdStoreRelationService {

    @Autowired
    private PopUpAdStoreRelationManager popUpAdStoreRelationManager;

    @Override
    public ServerResponseEntity<PageVO<SelectedStoreVO>> getTheStorePageByAdId(QueryStorePageByAdPageDTO pageDTO) {

        PageVO<SelectedStoreVO> storePage = new PageVO<>();

        List<PopUpAdStoreRelation> storeRelationList = lambdaQuery()
                .eq(PopUpAdStoreRelation::getPopUpAdId, pageDTO.getAdId())
                .list();

        List<Long> storeIdList = storeRelationList.stream()
                .map(PopUpAdStoreRelation::getShopId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(storeIdList)){
            StoreSelectedParamFeignDTO storeSelectedParamFeignDTO = new StoreSelectedParamFeignDTO();

            storeSelectedParamFeignDTO.setStoreIdList(storeIdList);
            storeSelectedParamFeignDTO.setKeyword(pageDTO.getStoreKeyword());

            storeSelectedParamFeignDTO.setPageSize(pageDTO.getPageSize());
            storeSelectedParamFeignDTO.setPageNum(pageDTO.getPageNum());

            storePage = popUpAdStoreRelationManager.getStorePage(storeSelectedParamFeignDTO);
        }

        return ServerResponseEntity.success(storePage);

    }

    @Override
    public ServerResponseEntity<List<Long>> getTheStoreIdListByAdId(Long adId) {
        List<PopUpAdStoreRelation> storeList = lambdaQuery()
                        .eq(PopUpAdStoreRelation::getPopUpAdId, adId)
                        .list();

        List<Long> storeIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(storeList)){
            storeIdList = storeList.stream().map(PopUpAdStoreRelation::getShopId).collect(Collectors.toList());
        }

        return ServerResponseEntity.success(storeIdList);
    }
}
