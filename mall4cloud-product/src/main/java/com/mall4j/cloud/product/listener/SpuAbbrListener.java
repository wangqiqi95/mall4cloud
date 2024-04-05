package com.mall4j.cloud.product.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import com.mall4j.cloud.product.dto.SpuAbbrReqDto;
import com.mall4j.cloud.product.mapper.SpuMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Description spu简称
 * @Author axin
 * @Date 2023-06-09
 **/
public class SpuAbbrListener extends AnalysisEventListener<SpuAbbrReqDto> {

    private static final int BATCH_COUNT = 1000;

    private List<SpuAbbrReqDto> list=Lists.newArrayList();

    private SpuMapper spuMapper;

    public SpuAbbrListener(SpuMapper spuMapper) {
        this.spuMapper=spuMapper;
    }

    @Override
    public void invoke(SpuAbbrReqDto reqDto, AnalysisContext context) {
        boolean isSave = list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }
        if(StringUtils.isNotBlank(reqDto.getSpuCode()) && StringUtils.isNotBlank(reqDto.getSpuAbbr()) && reqDto.getSpuAbbr().length() <= 10){
            list.add(reqDto);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        spuMapper.batchUpdateAbbrBySpuCode(list);
        list.clear();
    }

}
