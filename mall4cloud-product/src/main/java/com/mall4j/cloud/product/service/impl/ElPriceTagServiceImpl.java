package com.mall4j.cloud.product.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.ElPriceTag;
import com.mall4j.cloud.product.mapper.ElPriceTagMapper;
import com.mall4j.cloud.product.service.ElPriceProdService;
import com.mall4j.cloud.product.service.ElPriceTagService;
import com.mall4j.cloud.product.vo.ElPriceProdVO;
import com.mall4j.cloud.product.vo.ElPriceTagVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 电子价签管理
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
@Service
public class ElPriceTagServiceImpl implements ElPriceTagService {

    @Autowired
    private ElPriceTagMapper elPriceTagMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private ElPriceProdService elPriceProdService;

    @Override
    public PageVO<ElPriceTagVO> page(PageDTO pageDTO,String name) {
        PageVO<ElPriceTagVO> pageVO=PageUtil.doPage(pageDTO, () -> elPriceTagMapper.getList(name));

        pageVO.getList().forEach(item->{
            //配置商品集合
            item.setProdList(elPriceProdService.getList(item.getId(),name));
        });

        return pageVO;
    }

    /**
     * 价签商品列表
     * @param pageDTO
     * @param elId
     * @param name
     * @return
     */
    @Override
    public PageVO<ElPriceProdVO> prodPage(PageDTO pageDTO, String elId, String name) {
        PageVO<ElPriceProdVO> pageVO=PageUtil.doPage(pageDTO, () -> elPriceProdService.getList(elId,name));
        return pageVO;
    }

    /**
     * 商品详情
     * @param id 电子价签管理id
     * @return
     */
    @Override
    public ElPriceTagVO getElPriceTagVOById(String id) {
        ElPriceTag elPriceTag=elPriceTagMapper.selectById(id);
        if(elPriceTag==null) return null;
        ElPriceTagVO elPriceTagVO=mapperFacade.map(elPriceTag,ElPriceTagVO.class);

        //配置商品集合
        elPriceTagVO.setProdList(elPriceProdService.getList(id,null));

        return elPriceTagVO;
    }

    @Override
    public void save(ElPriceTag elPriceTag) {
        elPriceTagMapper.insert(elPriceTag);
    }

    @Override
    public void update(ElPriceTag elPriceTag) {
        elPriceTagMapper.updateById(elPriceTag);
    }

    @Override
    public void deleteById(String id) {
        elPriceTagMapper.deleteById(id);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        elPriceTagMapper.deleteByIds(ids);
    }
}
