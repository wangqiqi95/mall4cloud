package com.mall4j.cloud.product.service.impl;

import com.mall4j.cloud.product.dto.ElPriceProdDTO;
import com.mall4j.cloud.product.mapper.ElPriceProdMapper;
import com.mall4j.cloud.product.model.ElPriceProd;
import com.mall4j.cloud.product.service.ElPriceProdService;
import com.mall4j.cloud.product.vo.ElPriceProdVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 电子价签商品
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:24:29
 */
@Service
public class ElPriceProdServiceImpl implements ElPriceProdService {

    @Autowired
    private ElPriceProdMapper elPriceProdMapper;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public List<ElPriceProdVO> getList(String elId,String prodName) {
        return elPriceProdMapper.getList(elId,prodName);
    }

    @Override
    public ElPriceProd getById(Long id) {
        return elPriceProdMapper.getById(id);
    }

    @Override
    public void save(ElPriceProd elPriceProd) {
        elPriceProdMapper.save(elPriceProd);
    }

    @Override
    public void saveBatch(Long elId,List<ElPriceProdDTO> elPriceProdDTOS) {

        //先删除已配置的商品
        this.deleteByElId(elId);

        //保存商品
        List<ElPriceProd> elPriceProds=new ArrayList<>();
        for (ElPriceProdDTO elPriceProdDTO:elPriceProdDTOS){
            ElPriceProd elPriceProd=mapperFacade.map(elPriceProdDTO,ElPriceProd.class);
            elPriceProd.setElId(elId);
            elPriceProds.add(elPriceProd);
        }
        elPriceProdMapper.saveBatch(elPriceProds);
    }

    @Override
    public void update(ElPriceProd elPriceProd) {
        elPriceProdMapper.update(elPriceProd);
    }

    @Override
    public void deleteById(Long id) {
        elPriceProdMapper.deleteById(id);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        elPriceProdMapper.deleteByIds(ids);
    }

    /**
     * 清空
     * @param elId
     */
    @Override
    public void deleteByElId(Long elId) {
        elPriceProdMapper.deleteByElId(elId);
    }

    @Override
    public List<Long> checkSkus(List<Long> skuIds) {
        return null;
    }

    @Override
    public List<ElPriceProd> getElSpuList(List<Long> spuIds) {
        return elPriceProdMapper.getElSpuList(spuIds);
    }
}
