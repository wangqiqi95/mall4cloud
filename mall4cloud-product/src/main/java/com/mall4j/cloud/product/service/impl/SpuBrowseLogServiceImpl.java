package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SpuBrowseLogDTO;
import com.mall4j.cloud.product.mapper.SpuBrowseLogMapper;
import com.mall4j.cloud.product.model.SpuBrowseLog;
import com.mall4j.cloud.product.service.SpuBrowseLogService;
import com.mall4j.cloud.product.vo.SpuBrowseLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 商品浏览记录表
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
@Service
public class SpuBrowseLogServiceImpl implements SpuBrowseLogService {

    @Autowired
    private SpuBrowseLogMapper spuBrowseLogMapper;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @Override
    public PageVO<SpuBrowseLogVO> page(PageDTO pageDTO) {
        PageVO<SpuBrowseLogVO> page = PageUtil.doPage(pageDTO, () -> spuBrowseLogMapper.spuBrowseList(AuthUserContext.get().getUserId()));
        //es 中查询出来的商品名称为null，所以这里注释掉下面的代码。
//        List<Long> spuIds = page.getList().stream().map(SpuBrowseLogVO::getSpuId).collect(Collectors.toList());
//        ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.listSpuBySpuIds(spuIds);
//        Map<Long, String> spuMap = spuResponse.getData().stream().collect(Collectors.toMap(SpuSearchVO::getSpuId, SpuSearchVO::getSpuName));
//        for (SpuBrowseLogVO spuBrowseLogVO : page.getList()) {
//            spuBrowseLogVO.setSpuName(spuMap.get(spuBrowseLogVO.getSpuId()));
//        }
        return page;
    }

    @Override
    public void save(SpuBrowseLog spuBrowseLog) {
        spuBrowseLogMapper.save(spuBrowseLog);
    }

    @Override
    public void update(SpuBrowseLog spuBrowseLog) {
        spuBrowseLogMapper.update(spuBrowseLog);
    }

    @Override
    public SpuBrowseLog getBySpuIdAndUserId(Long spuId, Long userId) {
        return spuBrowseLogMapper.getBySpuIdAndUserId(spuId, userId);
    }

    @Override
    public void deleteById(Long spuBrowseLogId) {
        SpuBrowseLog spuBrowse = new SpuBrowseLog();
        spuBrowse.setStatus(StatusEnum.DELETE.value());
        spuBrowse.setSpuBrowseLogId(spuBrowseLogId);
        spuBrowseLogMapper.update(spuBrowse);
    }

    @Override
    public void delete(SpuBrowseLogDTO spuBrowseLogDTO) {
        if (CollUtil.isEmpty(spuBrowseLogDTO.getSpuBrowseLogIds())) {
            return;
        }
        Long userId = AuthUserContext.get().getUserId();
        spuBrowseLogMapper.batchUpdateStatus(spuBrowseLogDTO.getSpuBrowseLogIds(), userId, StatusEnum.DELETE.value());
    }

    @Override
    public Long recommendCategoryId(Integer spuType) {
        if (Objects.isNull(spuType)) {
            spuType = SpuType.NORMAL.value();
        }
        return spuBrowseLogMapper.recommendCategoryId(AuthUserContext.get().getUserId(), Constant.MAX_SPU_BROWSE_NUM, spuType);
    }

    @Override
    public SpuBrowseLog getCurrentLogBySpuIdAndUserId(SpuBrowseLogDTO spuBrowseLogDTO) {
        return spuBrowseLogMapper.getCurrentLogBySpuIdAndUserId(spuBrowseLogDTO, DateUtil.beginOfDay(new Date()));
    }
}
