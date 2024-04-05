package com.mall4j.cloud.delivery.feign;


import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.service.AreaService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author YXF
 * @date 2021/06/02
 */
@RestController
public class AreaFeignController implements AreaFeignClient {

    @Autowired
    private AreaService areaService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<List<AreaVO>> listProvinceArea() {
        return ServerResponseEntity.success(mapperFacade.mapAsList(areaService.getAreaListInfo(), AreaVO.class));
    }

    @Override
    public ServerResponseEntity<List<AreaVO>> allArea() {
        return ServerResponseEntity.success(mapperFacade.mapAsList(areaService.list(), AreaVO.class));
    }
    
    @Override
    public ServerResponseEntity<AreaVO> getByAreaId(Long areaId) {
        return ServerResponseEntity.success(areaService.getByAreaId(areaId));
    }
}
