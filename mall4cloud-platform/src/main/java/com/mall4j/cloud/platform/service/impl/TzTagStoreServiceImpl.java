package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.vo.TzTagStoreDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.TzTagStoreDTO;
import com.mall4j.cloud.platform.model.TzTagStore;
import com.mall4j.cloud.platform.mapper.TzTagStoreMapper;
import com.mall4j.cloud.platform.service.TzTagStoreService;
import com.mall4j.cloud.platform.vo.TzTagSimpleVO;
import com.mall4j.cloud.platform.vo.TzTagStoreVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签关联门店
 *
 * @author gmq
 * @date 2022-09-13 12:01:58
 */
@Slf4j
@Service
public class TzTagStoreServiceImpl extends ServiceImpl<TzTagStoreMapper, TzTagStore> implements TzTagStoreService {

    @Autowired
    private TzTagStoreMapper tzTagStoreMapper;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<TzTagStoreVO> pageStore(PageDTO pageDTO,Long tagId) {
        PageVO<TzTagStoreVO> pageVO=PageUtil.doPage(pageDTO, () -> tzTagStoreMapper.listByTagId(tagId));
        return pageVO;
    }

    @Override
    public List<Long> listByTagId(Long tagId) {
        List<TzTagStore> tzTagStores=this.list(new LambdaQueryWrapper<TzTagStore>()
                .eq(TzTagStore::getTagId,tagId)
                .eq(TzTagStore::getDelFlag,0)
                .groupBy(TzTagStore::getStoreId));
        if(CollectionUtil.isNotEmpty(tzTagStores)){
            List<Long> ids = tzTagStores.stream().map(TzTagStore::getStoreId).collect(Collectors.toList());
            return ids;
        }
        return null;
    }

    @Override
    public List<TzTagStoreDetailVO> listDetailByTagId(Long tagId) {
        List<TzTagStoreVO> tzTagStoreVOS=tzTagStoreMapper.listByTagId(tagId);
        if(CollectionUtil.isNotEmpty(tzTagStoreVOS)){
            List<TzTagStoreDetailVO> stores=mapperFacade.mapAsList(tzTagStoreVOS,TzTagStoreDetailVO.class);
            return stores;
        }
        return null;
    }

    @Override
    public List<TzTagSimpleVO> listTagByStoreId(Long storeId) {
        return tzTagStoreMapper.listTagByStoreId(storeId);
    }

    @Override
    public void saveStoreTags(TzTagStoreDTO tzTagStoreDTO) {

        log.info("门店管理-保存门店标签 操作人: {} storeId: {} tagIds: {} ",
                AuthUserContext.get().getUsername(),
                tzTagStoreDTO.getStoreId(),
                tzTagStoreDTO.getTagIds()
                );

        this.update(new LambdaUpdateWrapper<TzTagStore>()
                .set(TzTagStore::getDelFlag,1)
                .eq(TzTagStore::getStoreId,tzTagStoreDTO.getStoreId())
//                .in(TzTagStore::getTagId, StringUtils.join(tzTagStoreDTO.getTagIds(),","))
        );

//        List<TzTagStore> tzTagStores=this.list(new LambdaQueryWrapper<TzTagStore>()
//                .eq(TzTagStore::getStoreId,tzTagStoreDTO.getStoreId())
//                .in(TzTagStore::getTagId,tzTagStoreDTO.getTagIds()));
//
//        Map<Long, Long> map = tzTagStoreDTO.getTagIds().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
//
//        if(CollectionUtil.isNotEmpty(tzTagStores)){
//            tzTagStores.stream().forEach(item ->{
//                map.remove(item.getTagId());
//            });
//        }
//        List<Long> tagIds=new ArrayList<>(map.values());
        //保存关联门店
        List<TzTagStore> saveTagStores=new ArrayList<>();
        tzTagStoreDTO.getTagIds().forEach(tagId ->{
            TzTagStore tzTagStore=new TzTagStore();
            tzTagStore.setId(null);
            tzTagStore.setTagId(tagId);
            tzTagStore.setStoreId(tzTagStoreDTO.getStoreId());
            tzTagStore.setDelFlag(0);
            saveTagStores.add(tzTagStore);
        });
        this.saveBatch(saveTagStores);

    }

    @Override
    public void deleteById(Long id) {
        tzTagStoreMapper.deleteById(id);
    }

    @Override
    public void deleteByTagId(Long tagId) {
        this.update(new LambdaUpdateWrapper<TzTagStore>().set(TzTagStore::getDelFlag,1).eq(TzTagStore::getTagId,tagId));
//        tzTagStoreMapper.delete(new LambdaQueryWrapper<TzTagStore>().eq(TzTagStore::getTagId,tagId));
    }
}
