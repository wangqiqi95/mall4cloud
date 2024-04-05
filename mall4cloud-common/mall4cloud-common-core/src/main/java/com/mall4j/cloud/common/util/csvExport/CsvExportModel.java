package com.mall4j.cloud.common.util.csvExport;


import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class CsvExportModel {


    private CsvExportDataHandler handler;

    private List<CsvExportLabel> header;

    private long maxSize;

    private int pageSize = 100;

    private int pageNo;

    private long totalSize = 0;

    private String fileName; //导出文件全路径

    private List<String> result = new ArrayList<>();

    private int fileIndex = 0;
    private boolean newFile = true;
    private String tmpFile;

    private boolean start = true;

    private JSONArray tmpData;
    private List<List<String>> beforeHeader;//当前导出支持在标题前有自定义的数据

    public void readAndWrite() {

//        while (start || !handler.isOver(this)) {
//            start = false;
//            //确定是否需要创建新文件
//            newFile();
//            //读取当前页数据
//            tmpData = handler.getData(this);
//            if (tmpData == null) {
//                continue;
//            }
//            //写入文件
//            write(tmpData);
//            //增量数据总数
//            totalSize += tmpData.size();
//            //页码 + 1
//            pageNo++;
//            //计算 是否已经超过最大行,下次读取数据 创建写入新文件
//            if (totalSize >= maxSize) {
//                newFile = true;
//                fileIndex++;
//                totalSize = 0;
//            }
//            tmpData.clear();
//        }
    }

    private void write(JSONArray tmpData) {
//        Stream<String> dataStr = tmpData.stream().map(o -> {
//            JSONObject dataObj = (JSONObject) o;
//            Stream<String> dataStream = header.stream().map(l -> {
//                String data = null;
//                Object thisKeyObj = dataObj.get(l.getLabelKey());
//                if (thisKeyObj instanceof Date) {
//                    data = DateUtils.format((Date) thisKeyObj, "yyyy-MM-dd HH:mm:ss");
//                } else {
//                    data = dataObj.getString(l.getLabelKey());
//                }
//                return transformSpecialChar(data);
//            });
//            return StringUtils.join(dataStream.collect(Collectors.toList()), ",");
//
//        });
//        writeToFile(dataStr.collect(Collectors.toList()));
    }

    private void writeToFile(List<String> dataStr) {
//        try {
//            FileWriter writer = new FileWriter(this.tmpFile, true);
//            dataStr.forEach(row -> {
//                try {
//                    writer.write(row.toString());
//                    writer.write("\n");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            writer.close();
//        } catch (Exception e) {
//            log.error("export csv write row data  error ", e);
//        }
    }

    private void writeHeader() {
//        try {
//            FileWriter writer = new FileWriter(this.tmpFile, true);
//            writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
//            if(CollectionUtils.isNotEmpty(beforeHeader)){
//                beforeHeader.forEach(e->{
//                    try {
//                        writer.write(StringUtils.join(e,","));
//                        writer.write("\n");
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                });
//                writer.write("\n");
//            }
//            writer.write(StringUtils.join(LambdaUtils.mapToList(this.header, l -> l.getLabelName()), ","));
//            writer.write("\n");
//            writer.close();
//        } catch (Exception e) {
//            log.error("export csv write header error ", e);
//        }
    }

    private String transformSpecialChar(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        // 换行,制表符 等替换为 空格
        str = str.replaceAll("[\\t\\n\\r]", " ");
        //如果替换后为空格则返回空字符串
        if(StringUtils.isEmpty(str.trim())){
            return "";
        }
        // 不包含,则直接返回,不需要添加制表符
        if (!str.contains(",")) {
            return str+"\t";
        }
        //包含逗号, 则替换中间的双引号为2个双引号, 并在两头加 双引号   
        return "\"" + str.replaceAll("\"", "\"\"") + "\"";
    }

    private void newFile() {
//        if (newFile) {
//            this.tmpFile = getFileName() + "_" + fileIndex + ".csv";
//            this.result.add(this.tmpFile);
//            File file = new File(tmpFile);
//            try {
//                file.createNewFile();
//                log.info(" export csv new file :" + this.tmpFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error("export csv new file  error :" + this.tmpFile, e);
//            }
//            writeHeader();
//            newFile = false;
//        }
    }

    private String getFileName() {
        if (StringUtils.isEmpty(fileName)) {
            this.fileName = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString();
        }
        return this.fileName;
    }


    public void outCsvStream(HttpServletResponse response, File tempFile) throws IOException {
        java.io.OutputStream out = response.getOutputStream();
        byte[] b = new byte[10240];
        java.io.File fileLoad = new java.io.File(tempFile.getCanonicalPath());
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("content-disposition", "attachment; filename=export.csv");
        java.io.FileInputStream in = new java.io.FileInputStream(fileLoad);
        int n;
        //为了保证excel打开csv不出现中文乱码
        out.write(new   byte []{( byte ) 0xEF ,( byte ) 0xBB ,( byte ) 0xBF });
        while ((n = in.read(b)) != -1) {
            //每次写入out1024字节
            out.write(b, 0, n);
        }
        in.close();
        out.close();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public List<String> getResult() {
        return result;
    }

    public JSONArray getTmpData() {
        return tmpData;
    }

    public void setHandler(CsvExportDataHandler handler) {
        this.handler = handler;
    }

    public void setHeader(List<CsvExportLabel> header) {
        this.header = header;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setBeforeHeader(List<List<String>> beforeHeader) {
        this.beforeHeader = beforeHeader;
    }
}


