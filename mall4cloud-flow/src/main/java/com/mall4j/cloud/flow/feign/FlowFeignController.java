package com.mall4j.cloud.flow.feign;

import com.mall4j.cloud.api.flow.feign.FlowFeignClient;
import com.mall4j.cloud.api.flow.vo.UserAnalysisVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.flow.mapper.UserAnalysisMapper;
import com.mall4j.cloud.flow.service.ProductAnalyseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YXF
 * @date 2021/07/01
 */
@RestController
public class FlowFeignController implements FlowFeignClient {
    private static final Logger logger = LoggerFactory.getLogger(FlowFeignController.class);
    @Autowired
    private ProductAnalyseService productAnalyseService;
    @Autowired
    private UserAnalysisMapper userAnalysisMapper;


    @Override
    public ServerResponseEntity<Void> deleteSpuDataBySpuId(Long spuId) {
        try {
            productAnalyseService.deleteSpuDataBySpuId(spuId);
        } catch (Exception e) {
            logger.error("删除商品id:{}的统计数据失败", spuId);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageVO<UserAnalysisVO>> listUserAnalysisByUserId(Long userId, PageDTO pageDTO) {
        return ServerResponseEntity.success(PageUtil.doPage(pageDTO, () -> userAnalysisMapper.listUserAnalysisByUserId(userId)));
    }
}
