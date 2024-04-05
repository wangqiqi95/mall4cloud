package com.mall4j.cloud.delivery.feign;

import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryCompanyFeignClient;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.mapper.DeliveryCompanyMapper;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
@RestController
public class DeliveryCompanyFeignController implements DeliveryCompanyFeignClient {


    @Autowired
    private DeliveryCompanyMapper deliveryCompanyMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<String> getByDeliveryCompanyByCompanyId(Long companyId) {
        DeliveryCompanyVO deliveryCompanyVO = deliveryCompanyMapper.getByDeliveryCompanyId(companyId);
        return ServerResponseEntity.success(deliveryCompanyVO.getName());
    }

    @Override
    public ServerResponseEntity<List<DeliveryCompanyVO>> listBySearch(DeliveryCompanyDTO deliveryCompanyDTO) {
        return ServerResponseEntity.success(deliveryCompanyMapper.listBySearch(deliveryCompanyDTO));
    }
}
