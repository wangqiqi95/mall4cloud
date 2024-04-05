package com.mall4j.cloud.product.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.product.constant.SpuExportError;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.service.SpuExcelService;
import com.mall4j.cloud.product.vo.ElPriceTagSpuReadExcelVO;
import com.mall4j.cloud.product.vo.SpuCodeReadExcelVO;
import com.mall4j.cloud.product.vo.SpuPageVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品excel导入监听
 *
 * 有个很重要的点 SpuExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author YXF
 * @date 2021/3/11
 */

public class ElPriceTagSpuReadExcelListener extends AnalysisEventListener<ElPriceTagSpuReadExcelVO> {

    private SpuExcelService spuExcelService;
    private Map<Integer, List<String>> errorMap;
    private List<String> list;
    private List<SpuPageVO> spuPageVOS=new ArrayList<>();
    private ElPriceTagSpuReadExcelVO elPriceTagSpuReadExcelVO;

    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static String seq;

    public ElPriceTagSpuReadExcelListener() {
    }

    public List<SpuPageVO> getSpuPageVOS() {
        return spuPageVOS;
    }

    public ElPriceTagSpuReadExcelListener(SpuExcelService spuExcelService, Map<Integer, List<String>> errorMap) {
        this.spuExcelService = spuExcelService;
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(ElPriceTagSpuReadExcelVO elPriceTagSpuReadExcelVO, AnalysisContext analysisContext) {

        list.add(elPriceTagSpuReadExcelVO.getSpuCode());
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //确保所有数据都能入库
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        try {
            //执行数据变更
            SpuPageSearchDTO spuPageSearchDTO=new SpuPageSearchDTO();
            spuPageSearchDTO.setSpuCodeExcelList(list);
            spuPageVOS=spuExcelService.listSpuByCodes(spuPageSearchDTO);
        } catch (Exception e) {
            List list = errorMap.get(SpuExportError.OTHER.value());
            if (CollUtil.isEmpty(list)) {
                list = new ArrayList();
                errorMap.put(SpuExportError.OTHER.value(), list);
            }
            list.add(e.getMessage());
        }
        list.clear();
    }

}
