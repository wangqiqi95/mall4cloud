package com.mall4j.cloud.user.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mall4j.cloud.user.dto.UserExcelDTO;
import com.mall4j.cloud.user.manager.UserExcelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品excel导入监听
 *
 * 有个很重要的点 SpuExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author cl
 * @date 2021/5/14
 */

public class UserExcelListener extends AnalysisEventListener<UserExcelDTO> {

    private UserExcelManager userExcelManager;
    private Map<String, List<String>> errorMap;
    private List<UserExcelDTO> list;
    public static String OTHER = "other";
    public static String ERROR_ROWS = "error_rows";
    public static String IMPORT_ROWS = "import_rows";

    /**
     * 每隔1000条数据存储数据库，然后清理map ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static String seq;

    public UserExcelListener() {
    }

    public UserExcelListener(UserExcelManager userExcelManager, Map<String, List<String>> errorMap) {
        this.userExcelManager = userExcelManager;
        errorMap.put(OTHER, new ArrayList());
        errorMap.put(ERROR_ROWS, new ArrayList());
        errorMap.put(IMPORT_ROWS, new ArrayList());
        this.errorMap = errorMap;
        this.list = new ArrayList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     */

    @Override
    public void invoke(UserExcelDTO userExcelDTO, AnalysisContext analysisContext) {
        boolean isSave = Objects.nonNull(seq) && !Objects.equals(seq, userExcelDTO.getSeq()) && list.size() > BATCH_COUNT;
        if (isSave) {
            saveData();
        }
        seq = userExcelDTO.getSeq();
        if (StrUtil.isBlank(userExcelDTO.getBalance())){
            userExcelDTO.setBalance("0");
        }
        if (StrUtil.isBlank(userExcelDTO.getScore())) {
            userExcelDTO.setScore("0");
        }
        if (StrUtil.isBlank(userExcelDTO.getGrowth())) {
            userExcelDTO.setGrowth("0");
        }
        list.add(userExcelDTO);
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
        userExcelManager.importExcel(list, errorMap);
        list.clear();
    }

}
