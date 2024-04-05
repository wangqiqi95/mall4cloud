package com.mall4j.cloud.delivery.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.cache.constant.DeliveryCacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.delivery.dto.AreaDTO;
import com.mall4j.cloud.delivery.dto.TransfeeDTO;
import com.mall4j.cloud.delivery.dto.TransfeeFreeDTO;
import com.mall4j.cloud.delivery.dto.TransportDTO;
import com.mall4j.cloud.delivery.mapper.*;
import com.mall4j.cloud.delivery.model.*;
import com.mall4j.cloud.delivery.service.TransportService;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;
import com.mall4j.cloud.delivery.vo.TransfeeVO;
import com.mall4j.cloud.delivery.vo.TransportVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 运费模板
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class TransportServiceImpl implements TransportService {

    @Autowired
    private TransportMapper transportMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TranscityFreeMapper transcityFreeMapper;

    @Autowired
    private TransfeeMapper transfeeMapper;

    @Autowired
    private TranscityMapper transcityMapper;

    @Autowired
    private TransfeeFreeMapper transfeeFreeMapper;

    @Autowired
    private SpuFeignClient spuFeignClient;


    @Override
    public PageVO<TransportVO> page(PageDTO pageDTO, TransportDTO transportDTO) {
        return PageUtil.doPage(pageDTO, () -> transportMapper.list(transportDTO));
    }


    @Override
    @Cacheable(cacheNames = DeliveryCacheNames.TRANSPORT_BY_ID_PREFIX, key = "#transportId" , sync = true)
    public TransportVO getTransportAndAllItemsById(Long transportId) {
        TransportVO transport = transportMapper.getTransportAndTransfeeAndTranscityById(transportId);
        if (transport == null) {
            return null;
        }
        List<TransfeeFreeVO> transFeeFrees = transportMapper.getTransFeeFreeAndTransCityFreeByTransportId(transportId);
        transport.setTransFeeFrees(transFeeFrees);
        return transport;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTransportAndTransFee(TransportDTO transportDTO) {
        this.checkIsRepeat(transportDTO);
        Transport transport = mapperFacade.map(transportDTO, Transport.class);
        Date createTime = new Date();
        transport.setCreateTime(createTime);
        // 插入运费模板
        transportMapper.save(transport);
        // 插入所有的运费项和城市
        insertTransFeeAndTransCity(transport.getTransportId(),transportDTO.getTransFees());

        // 插入所有的指定包邮条件项和城市
        if (transport.getHasFreeCondition() == 1) {
            insertTransFeeFreeAndTransCityFree(transport.getTransportId(),transportDTO.getTransFeeFrees());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = DeliveryCacheNames.TRANSPORT_BY_ID_PREFIX, key = "#transportDTO.transportId")
    public void updateTransportAndTransFee(TransportDTO transportDTO) {
        TransportVO transportDb = transportMapper.getTransportAndTransfeeAndTranscityById(transportDTO.getTransportId());
        if (transportDb.getIsFreeFee() != 1) {
            this.checkIsRepeat(transportDTO);
        }
        Long shopId = AuthUserContext.get().getTenantId();
        if (!Objects.equals(transportDb.getShopId(), shopId)) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }

        Transport transport = mapperFacade.map(transportDTO, Transport.class);
        // 更新运费模板
        transportMapper.updateById(transport);

        deleteTransportById(transportDTO.getTransportId());

        // 插入所有的运费项和城市
        insertTransFeeAndTransCity(transport.getTransportId(),transportDTO.getTransFees());
        // 插入所有的指定包邮条件项和城市
        if (transport.getHasFreeCondition() == 1) {
            insertTransFeeFreeAndTransCityFree(transport.getTransportId(),transportDTO.getTransFeeFrees());
        }
    }

    private void checkIsRepeat(TransportDTO transportDTO) {
        int count = transportMapper.countByTransName(transportDTO.getShopId(), transportDTO.getTransName(), transportDTO.getTransportId());
        if (count > 0) {
            throw new LuckException("运费模板名称已存在，请重新输入");
        }
        // 查询是否已经存在包邮模板，一个店铺只能有一个包邮模板
        if (transportDTO.getIsFreeFee() == 1) {
            int freeCount = transportMapper.countByFreeAndShopId(transportDTO.getIsFreeFee(), transportDTO.getShopId());
            if (freeCount > 0) {
                throw new LuckException("已包含包邮模板，不能重复创建");
            }
        }
    }

    private void deleteTransportById(Long transportId) {
        TransportVO dbTransport = getTransportAndAllItemsById(transportId);
        // 删除所有的运费项
        transfeeMapper.deleteByTransportId(dbTransport.getTransportId());
        // 删除所有的指定包邮条件项
        transfeeFreeMapper.deleteTransFeeFreesByTransportId(dbTransport.getTransportId());

        List<Long> transFeeIds = dbTransport.getTransFees().stream().map(TransfeeVO::getTransfeeId).collect(Collectors.toList());
        List<Long> transFeeFreeIds = dbTransport.getTransFeeFrees().stream().map(TransfeeFreeVO::getTransfeeFreeId).collect(Collectors.toList());


        // 删除所有运费项包含的城市
        transcityMapper.deleteBatchByTransFeeIds(transFeeIds);
        if(CollectionUtil.isNotEmpty(transFeeFreeIds)) {
            // 删除所有指定包邮条件项包含的城市
            transcityFreeMapper.deleteBatchByTransFeeFreeIds(transFeeFreeIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransportAndTransFeeAndTransCityById(Long transportId) {
         //TODO 需要查询商品操作
        int count = spuFeignClient.countByTransportId(transportId).getData();
        if (count>0){
            throw new LuckException("模板正在使用中，无法进行删除操作");
        }
        deleteTransportById(transportId);
        // 删除运费模板
        transportMapper.deleteById(transportId);
    }

    private void insertTransFeeAndTransCity(Long transportId, List<TransfeeDTO> transFeeList) {
        for (TransfeeDTO transfeeDTO : transFeeList) {
            transfeeDTO.setTransportId(transportId);
        }
        // 批量插入运费项 并返回运费项id，供下面循环使用
        transfeeMapper.saveBatch(transFeeList);
        List<Transcity> transCities = new ArrayList<>();
        for (TransfeeDTO transFee : transFeeList) {
            List<AreaDTO> cityList = transFee.getCityList();
            if (CollectionUtil.isEmpty(cityList)) {
                continue;
            }
            // 当地址不为空时
            for (AreaDTO area : cityList) {
                Transcity transcity = new Transcity();
                transcity.setTransfeeId(transFee.getTransfeeId());
                transcity.setCityId(area.getAreaId());
                transCities.add(transcity);
            }
        }

        // 批量插入运费项中的城市
        if (CollectionUtil.isNotEmpty(transCities)) {
            transcityMapper.saveBatch(transCities);
        }
    }

    private void insertTransFeeFreeAndTransCityFree(Long transportId, List<TransfeeFreeDTO> transFeeFrees) {
        for (TransfeeFreeDTO transFeeFree : transFeeFrees) {
            transFeeFree.setTransportId(transportId);
        }
        // 批量插入指定包邮条件项 并返回指定包邮条件项 id，供下面循环使用
        transfeeFreeMapper.saveBatch(transFeeFrees);

        List<TranscityFree> transCityFrees = new ArrayList<>();
        for (TransfeeFreeDTO transFeeFree : transFeeFrees) {
            List<AreaDTO> cityList = transFeeFree.getFreeCityList();
            if (CollectionUtil.isEmpty(cityList)) {
                // 请选择指定包邮城市
                throw new LuckException("请选择指定包邮城市");
            }
            // 当地址不为空时
            for (AreaDTO area : cityList) {
                TranscityFree transcityFree = new TranscityFree();
                transcityFree.setTransfeeFreeId(transFeeFree.getTransfeeFreeId());
                transcityFree.setFreeCityId(area.getAreaId());
                transCityFrees.add(transcityFree);
            }
        }

        // 批量插入指定包邮条件项中的城市
        if (CollectionUtil.isNotEmpty(transCityFrees)) {
            transcityFreeMapper.saveBatch(transCityFrees);
        }
    }

    @Override
    @CacheEvict(cacheNames = DeliveryCacheNames.TRANSPORT_BY_ID_PREFIX, key = "#transportId")
    public void removeTransportAndAllItemsCache(Long transportId) {

    }

    @Override
    public List<TransportVO> listTransport(Long tenantId) {
        return transportMapper.listTransport(tenantId);
    }
    
    @Override
    public PageVO<TransportVO> transportList(PageDTO pageDTO, TransportDTO transportDTO) {
        PageVO<TransportVO> pageVO = PageUtil.doPage(pageDTO, () -> transportMapper.list(transportDTO));
        List<Long> ids = pageVO.getList().stream().map(TransportVO::getTransportId).collect(Collectors.toList());
        List<TransportVO> result = new ArrayList<>();
        ids.forEach(id -> {
            TransportVO vo = getTransportAndAllItemsById(id);
            List<TransfeeVO> transFees = vo.getTransFees();
            transFees.forEach( fee ->{ fee.getCityList().clear(); });
            List<TransfeeFreeVO> transFeeFrees = vo.getTransFeeFrees();
            transFeeFrees.forEach( feeFree ->{ feeFree.getFreeCityList().clear(); });
            result.add(vo);
        });
        pageVO.setList(result);
        return pageVO;
    }
}
