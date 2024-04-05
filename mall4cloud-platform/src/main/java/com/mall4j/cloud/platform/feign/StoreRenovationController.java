package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.dto.RenovationBurialPointDTO;
import com.mall4j.cloud.api.platform.feign.StoreRenovationClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.StoreRenovation;
import com.mall4j.cloud.platform.service.StoreRenovationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 微页面装修Feign实现类
 */
@RestController
@Slf4j
@AllArgsConstructor
public class StoreRenovationController implements StoreRenovationClient {

    private final StoreRenovationService storeRenovationService;

    @Override
    public ServerResponseEntity<Void> editRenovationBurialPoint(RenovationBurialPointDTO dto) {
        StoreRenovation renovation = storeRenovationService.getByRenovationId(dto.getRenovationId());
        if (Objects.isNull(renovation)) {
            log.error("未找到对应的微页面信息，装修ID：{}", dto.getRenovationId());
            return ServerResponseEntity.showFailMsg("未找到对应的微页面信息");
        }

        Integer openNumber = renovation.getOpenNumber();
        Integer openPeople = renovation.getOpenPeople();

        StoreRenovation storeRenovation = new StoreRenovation();
        storeRenovation.setRenovationId(dto.getRenovationId());
        storeRenovation.setOpenNumber((Objects.isNull(openNumber) ? 0 : openNumber) + dto.getIncrBrowseNum());
        storeRenovation.setOpenPeople((Objects.isNull(openPeople) ? 0 : openPeople) + dto.getIncrBrowsePeopleNum());
        storeRenovationService.updateById(storeRenovation);

        return ServerResponseEntity.success();
    }
}
