package com.mall4j.cloud.common.util.csvExport.hanlder;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.Map;

/**
 * excel配置信息
 *
 * @author YXF
 * @date 2021-03-03 09:00:09
 */
public class SheetWrite implements SheetWriteHandler {
    private Map<Integer, String[]> mapDropDown;
    private Integer firstRow;
    private Integer lastRow;

    public SheetWrite() {
    }

    public SheetWrite(Map<Integer, String[]> mapDropDown, Integer firstRow, Integer lastRow) {
        this.mapDropDown = mapDropDown;
        this.firstRow = firstRow < 0 ? 0 : firstRow;
        this.lastRow = lastRow;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
        // 区间设置 第一列第一行和第二行的数据。
        // 由于第一行是头，所以第一、二行的数据实际上是第二三行
        //从第五行开始 100行都是这个
        //fristRow表示 从第几行开始 到第几行结束  表格行从0开始的
        //firstCol表示 从第几列开始 到第几列结束 表格列从0开始的
        for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(this.firstRow, 100000, entry.getKey(), entry.getKey());
            DataValidation dataValidation;
            /***设置下拉框数据**/
            if (useSheet(entry.getValue())) {
                dataValidation = create(writeWorkbookHolder, helper, entry, cellRangeAddressList);
            } else {
                DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
                dataValidation = helper.createValidation(constraint, cellRangeAddressList);
            }
            /***处理Excel兼容性问题**/
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            writeSheetHolder.getSheet().addValidationData(dataValidation);
        }
    }

    private boolean useSheet(String[] value) {
        // excel下拉框允许的最大长度, 超过此长度则不显示下拉框的内容
        int maxlength = 255;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : value) {
            stringBuilder.append(s);
            if (stringBuilder.length() > maxlength) {
                return true;
            }
        }
        return false;
    }

    private DataValidation create(WriteWorkbookHolder writeWorkbookHolder, DataValidationHelper helper, Map.Entry<Integer, String[]> entry, CellRangeAddressList cellRangeAddressList) {
        /*   解决办法从这里开始   */
        //获取一个workbook
        //定义sheet的名称
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        String hiddenName = "hidden" + entry.getKey();
        //1.创建一个隐藏的sheet 名称为 hidden
        Sheet hidden = workbook.createSheet(hiddenName);
        //2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
        int index = 0;
        for (String value : entry.getValue()) {
            // 3:表示你开始的行数  3表示 你开始的列数
            hidden.createRow(firstRow + index).createCell(entry.getKey()).setCellValue(value);
            index++;
        }
        Name name = workbook.createName();
        name.setNameName(hiddenName);
        //4 A1:A代表隐藏域创建第N列createCell(N)时。以A1列开始A行数据获取下拉数组
        String columnLabel = getExcelColumnLabel(entry.getKey());
        name.setRefersToFormula(hiddenName + "!$" + columnLabel + "$1:$" + columnLabel + "$65535");
        //5 将刚才设置的sheet引用到你的下拉列表中
        DataValidationConstraint constraint = helper.createFormulaListConstraint(hiddenName);
        return helper.createValidation(constraint, cellRangeAddressList);
    }

    private static String getExcelColumnLabel(int num) {
        String temp = "";
        double i = Math.floor(Math.log(25.0 * (num) / 26.0 + 1) / Math.log(26)) + 1;
        if (i > 1) {
            double sub = num - 26 * (Math.pow(26, i - 1) - 1) / 25;
            for (double j = i; j > 0; j--) {
                temp = temp + (char) (sub / Math.pow(26, j - 1) + 65);
                sub = sub % Math.pow(26, j - 1);
            }
        } else {
            temp = temp + (char) (num + 65);
        }
        return temp;

    }
}
