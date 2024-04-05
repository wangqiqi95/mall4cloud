package com.mall4j.cloud.delivery.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.Station;
import com.mall4j.cloud.delivery.mapper.StationMapper;
import com.mall4j.cloud.delivery.service.StationService;
import com.mall4j.cloud.delivery.vo.StationVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public PageVO<StationVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> stationMapper.list());
    }

    @Override
    public StationVO getByStationId(Long stationId) {
        return stationMapper.getByStationId(stationId);
    }

    @Override
    public void save(Station station) {
        stationMapper.save(station);
    }

    @Override
    public void update(Station station) {
        stationMapper.update(station);
    }

    @Override
    public void deleteById(Long stationId) {
        stationMapper.deleteById(stationId);
    }
}
