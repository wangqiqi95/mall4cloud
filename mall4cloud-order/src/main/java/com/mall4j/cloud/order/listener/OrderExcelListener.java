package com.mall4j.cloud.order.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.order.constant.OrderExportError;
import com.mall4j.cloud.order.service.OrderExcelService;
import com.mall4j.cloud.order.vo.UnDeliveryOrderExcelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 待发货订单导入监听，对数据进行校验
 * @author Pineapple
 * @date 2021/7/20 11:11
 */
public class OrderExcelListener extends AnalysisEventListener<UnDeliveryOrderExcelVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderExcelListener.class);
    private OrderExcelService orderExcelService;
    private Map<Integer, List<String>> errorMap;
    private List<UnDeliveryOrderExcelVO> list;
    private UnDeliveryOrderExcelVO unDeliveryOrderExcel;

    private static final int BATCH_COUNT = 1000;
    private static String seq;

    public OrderExcelListener(){

    }

    public OrderExcelListener(OrderExcelService orderExcelService,Map<Integer,List<String>> errorMap){
        this.orderExcelService = orderExcelService;
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
    }

//    @Override
//    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
//        List<String> headList = new ArrayList<>();
//        for (Integer i : headMap.keySet()) {
//            headList.add(headMap.get(i));
//        }
//        if (!Objects.equals(headList.get(0),"订单信息")){
//            errorMap.put(OrderExportError.EXCEL_ERROR.value(),headList);
//        }
//        //todo 校验表头
//    }

    /**
     * 每条数据解析时均会调用
     */
    @Override
    public void invoke(UnDeliveryOrderExcelVO unDeliveryOrderExcelVO, AnalysisContext analysisContext) {
        if (Objects.isNull(unDeliveryOrderExcelVO.getSeq())){
            if (Objects.isNull(unDeliveryOrderExcel)){
                return;
            }
            unDeliveryOrderExcelVO.setSeq(unDeliveryOrderExcel.getSeq());
            unDeliveryOrderExcelVO.setOrderId(unDeliveryOrderExcel.getOrderId());
            unDeliveryOrderExcelVO.setDeliveryType(unDeliveryOrderExcel.getDeliveryType());
            unDeliveryOrderExcelVO.setDeliveryCompanyName(unDeliveryOrderExcel.getDeliveryCompanyName());
            unDeliveryOrderExcelVO.setDeliveryNo(unDeliveryOrderExcel.getDeliveryNo());
        }
        boolean isSave = Objects.nonNull(seq) && !Objects.equals(seq, unDeliveryOrderExcelVO.getSeq()) && list.size() > BATCH_COUNT;
        if (isSave){
            saveData();
        }
        seq = unDeliveryOrderExcelVO.getSeq();
        unDeliveryOrderExcel = unDeliveryOrderExcelVO;
        list.add(unDeliveryOrderExcelVO);
    }

    /**
     * 所有数据解析完成调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    private void saveData() {
        if (CollUtil.isEmpty(list)){
            return;
        }
        try {
            orderExcelService.exportOrderExcel(list,errorMap);
        } catch (Exception e){
            List<String> errorList = errorMap.get(OrderExportError.OTHER.value());
            if (CollUtil.isEmpty(errorList)){
                errorList = new ArrayList<>();
                errorMap.put(OrderExportError.OTHER.value(),errorList);
            }
            errorList.add(e.getMessage());
        }
        list.clear();
    }
}