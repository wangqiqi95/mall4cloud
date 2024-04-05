package com.mall4j.cloud.user.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.util.csvExport.hanlder.ExcelMergeHandler;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.user.manager.UserExcelManager;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserExcelVO;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 会员导出
 * @Date 2022年4月29日, 0029 17:04
 * @Created by eury
 */
//@Component("SoldUsersListener")
//@AllArgsConstructor
public class SoldUsersListener {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private ThreadPoolExecutor userThreadPoolExecutor;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    /**
     * 定义excel中一个sheet最大的行数 一百万行数据, 必须是PageDTO.MAX_PAGE_SIZE 的整数倍
     */
    public static final long SHEET_MAX_ROWS = 1000000;
    /**
     * 账户username前缀
     */
    public static final String TEL_PREFIX = "Tel";

    private static final Logger log = LoggerFactory.getLogger(UserExcelManager.class);

//    @Async
//    @EventListener(SoldUsersEvent.class)
//    public void soldUsersEvent(SoldUsersEvent event) {
//
//        log.info("--开始执行会员导出----");
//
//        Long downLoadHisId= event.getDownLoadHisId();
//        //下载中心记录
//        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
//        finishDownLoadDTO.setId(downLoadHisId);
//
//        UserManagerDTO userManagerDTO=event.getUserManagerDTO();
//
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPageNum(PageDTO.DEFAULT_PAGE_NUM);
//        pageDTO.setPageSize(PageDTO.MAX_PAGE_SIZE);
//        PageVO<UserManagerVO> userPage = userService.getUserInfoPage(pageDTO, userManagerDTO);
//        long total = userPage.getTotal();
//        if (total == 0) {
//            finishDownLoadDTO.setRemarks("无用户数据导出");
//            finishDownLoadDTO.setStatus(2);
//            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
//            log.error("无用户数据导出");
//            return;
//        }
//
//        finishDownLoadDTO.setCalCount((int)total);
//
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        // 总数大于PageDTO.MAX_PAGE_SIZE条
////        if (total > PageDTO.MAX_PAGE_SIZE) {
//
//            // 总共可以分多少页
//            Integer pages = userPage.getPages();
//            // 从第一页开始
//            int page = 1;
//            ExcelWriter excelWriter = null;
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            try {
//                // 先执行合并策略
//                excelWriter = getExcelWriterMerge(os,UserExcelVO.EXCEL_NAME, UserExcelVO.MERGE_ROW_INDEX, UserExcelVO.MERGE_COLUMN_INDEX).build();
//                // 大于 PageDTO.MAX_PAGE_SIZE行，进行分页查询
//                // 计算有多少个sheet
//                int sheetNum = countSlicesNum(SHEET_MAX_ROWS, total);
//                for (int i = 0; i < sheetNum; i++) {
//                    // 第i个sheet最多有多少行数据
//                    long eachSheetRows = Math.min(total - i * SHEET_MAX_ROWS, SHEET_MAX_ROWS);
//                    // 计算每个sheet的最大行数，要分多少次分页查询
//                    int slicesNum = countSlicesNum(PageDTO.MAX_PAGE_SIZE.longValue(), eachSheetRows);
//                    // 收集好获取到当前sheet的所有准备写入的用户数据
//                    List<UserExcelVO> sheetDataList = new ArrayList<>();
//                    for (int j = 0; j < slicesNum; j++) {
//                        if (page > pages) {
//                            break;
//                        }
//                        // 多线程分页查询数据
//                        PageDTO pageIndex = new PageDTO();
//                        pageIndex.setPageSize(PageDTO.MAX_PAGE_SIZE);
//                        // 第几页开始
//                        pageIndex.setPageNum(page);
//                        int tempPage = page;
//                        CompletableFuture<List<UserExcelVO>> userFuture = CompletableFuture.supplyAsync(() -> {
//                            RequestContextHolder.setRequestAttributes(requestAttributes);
//                            List<UserManagerVO> userInfoList = userService.getUserInfoList(pageIndex, userManagerDTO);
//                            List<UserExcelVO> list = mapperFacade.mapAsList(userInfoList, UserExcelVO.class);
//                            // 给数组一个序列号
//                            list.forEach(item -> item.setSeq(Integer.toString(list.indexOf(item) + (tempPage - 1) * pageIndex.getPageSize() + 1)));
//                            return list;
//                        }, userThreadPoolExecutor);
//                        page++;
//                        sheetDataList.addAll(userFuture.get());
//                    }
//                    // 导出excel
//                    int sheetNo = i + 1;
//                    WriteSheet writeSheet = EasyExcel.writerSheet(UserExcelVO.SHEET_NAME + sheetNo).head(UserExcelVO.class).build();
//                    excelWriter.write(sheetDataList, writeSheet);
//                }
//            } catch (Exception e) {
//                log.error("导出用户信息错误: "+e.getMessage(),e);
//                finishDownLoadDTO.setRemarks("导出用户信息错误，信息错误："+e.getMessage());
//                finishDownLoadDTO.setStatus(2);
//                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
//            } finally { // 千万别忘记finish 会帮忙关闭流
//                if (excelWriter != null) {
//                    excelWriter.finish();
//                    // 必须要finish才会写入，不finish只会创建empty的文件
//                    excelUpload(os,finishDownLoadDTO);
//                }
//            }
//
////        } else {
//            // 小于等于 PageDTO.MAX_PAGE_SIZE行 直接导出
////            List<UserExcelVO> list = mapperFacade.mapAsList(userPage.getList(), UserExcelVO.class);
////            list.forEach(item -> item.setSeq(Integer.toString(list.indexOf(item) + 1)));
////            ExcelUtil.soleExcel(response, list, UserExcelVO.EXCEL_NAME, UserExcelVO.MERGE_ROW_INDEX, UserExcelVO.MERGE_COLUMN_INDEX, UserExcelVO.class);
////        }
//    }


    private ServerResponseEntity<String> excelUpload(ByteArrayOutputStream os,FinishDownLoadDTO finishDownLoadDTO) {
        try {
            String excelFileName = System.currentTimeMillis()+ RandomUtil.getRandomStr(4);

            // 必须要finish才会写入，不finish只会创建empty的文件
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            excelFileName = URLEncoder.encode(excelFileName, "UTF-8");
            excelFileName = excelFileName + ExcelTypeEnum.XLSX.getValue();
            String contentType = "application/vnd.ms-excel";

            MultipartFile multipartFile = new MultipartFileDto(excelFileName, excelFileName,
                    contentType, is);
            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("---ExcelUploadService---" + responseEntity.toString());
                //下载中心记录
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ UserExcelVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
            return responseEntity;
        } catch (Exception e) {
            log.info(e.getMessage());
            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }
        return null;
    }



    public ExcelWriterBuilder getExcelWriterMerge(ByteArrayOutputStream os, String excelName, int mergeRowIndex, int[] mergeColumnIndex) throws Exception{
        ExcelWriterBuilder build = EasyExcel.write(os)
                // 自定义合并规则
                .registerWriteHandler(new ExcelMergeHandler(mergeRowIndex, mergeColumnIndex))
                // 宽度自适应
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
        return build;
    }

    /**
     * 分片个数，计算总数total分为每片为eachNum的片数
     *
     * @param eachNum 每片的个数
     * @param total   被分片的总数
     * @return 分片个数
     */
    private int countSlicesNum(Long eachNum, Long total) {
        boolean isZero = Objects.isNull(eachNum) || eachNum == 0 || Objects.isNull(total) || total == 0;
        if (isZero) {
            return 0;
        }
        // 分片数
        int pageSize = new BigDecimal(total).divide(new BigDecimal(eachNum), 1).intValue();
        // 余数
        int mod = new BigDecimal(total).divideAndRemainder(new BigDecimal(eachNum))[1].intValue();
        if (mod > 0) {
            pageSize = pageSize + 1;
        }
        return pageSize;
    }
}
