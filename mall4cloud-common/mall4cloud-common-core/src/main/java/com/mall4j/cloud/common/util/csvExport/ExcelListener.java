package com.mall4j.cloud.common.util.csvExport;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//创建读取excel监听器
public class ExcelListener<T> extends AnalysisEventListener<T> {

    //创建list集合封装最终的数据
    private List<T> list = new ArrayList<T>();

    private Class clazz;


    public ExcelListener(Class clazz) {
        this.clazz = clazz;
    }

    //一行一行去读取excle内容
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        list.add(t);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        Field[] fields = clazz.getDeclaredFields();
        Collection<String> headNameList = headMap.values();

        if (headMap.size() != fields.length) {
            throw new ExcelAnalysisException("excel模板错误，请检查。");
        }

        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            String[] value = excelProperty.value();
            String name = value[0];
            if (!headNameList.contains(name)) {
                throw new ExcelAnalysisException("excel模板错误，请检查。");
            }
        }
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // TODO document why this method is empty
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
