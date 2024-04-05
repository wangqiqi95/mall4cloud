package com.mall4j.cloud.product.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.product.constant.SpuExportError;
import com.mall4j.cloud.product.service.SpuExcelService;
import com.mall4j.cloud.product.vo.SpuExcelVO;

import java.util.*;

/**
 * 商品excel导入监听
 *
 * 有个很重要的点 SpuExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author YXF
 * @date 2021/3/11
 */

public class SpuExcelListener extends AnalysisEventListener<SpuExcelVO> {

    private SpuExcelService spuExcelService;
    private Map<Integer, List<String>> errorMap;
    private List<SpuExcelVO> list;
    private SpuExcelVO spuExcel;

    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static String seq;

    public SpuExcelListener() {
    }

    public SpuExcelListener(SpuExcelService spuExcelService, Map<Integer, List<String>> errorMap) {
        this.spuExcelService = spuExcelService;
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(SpuExcelVO spuExcelVO, AnalysisContext analysisContext) {
        if (Objects.isNull(spuExcelVO.getSeq())) {
            if (Objects.isNull(spuExcel)) {
                return;
            }
            spuExcelVO.setSeq(spuExcel.getSeq());
            spuExcelVO.setNameZh(spuExcel.getNameZh());
            spuExcelVO.setNameEn(spuExcel.getNameEn());
            spuExcelVO.setSellingPointZh(spuExcel.getSellingPointZh());
            spuExcelVO.setSellingPointEn(spuExcel.getSellingPointEn());
            spuExcelVO.setBrandName(spuExcel.getBrandName());
            spuExcelVO.setCategoryName(spuExcel.getCategoryName());
            spuExcelVO.setShopCategoryName(spuExcel.getShopCategoryName());
            spuExcelVO.setDeliveryMode(spuExcel.getDeliveryMode());
            spuExcelVO.setDeliveryTemplate(spuExcel.getDeliveryTemplate());
            spuExcelVO.setStatus(spuExcel.getStatus());
        }
        boolean isSave = Objects.nonNull(seq) && !Objects.equals(seq, spuExcelVO.getSeq()) && list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }
        seq = spuExcelVO.getSeq();
        spuExcel = spuExcelVO;
        list.add(spuExcelVO);
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
            spuExcelService.exportExcel(list, errorMap);
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
