package com.mall4j.cloud.product.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.product.vo.SkuPriceFeeExcelLogVO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.common.vo.UploadExcelVO;
import com.mall4j.cloud.product.listener.UpdateSkuPriceFeeExcelListener;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.SkuExcelService;
import com.mall4j.cloud.product.vo.SkuPriceFeeExcelVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2022年3月8日, 0008 15:10
 * @Created by eury
 */
@Slf4j
@Component
public class UpdateSkuPirceStatusManager {

    @Autowired
    private SkuExcelService skuExcelService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ExcelUploadFeignClient excelUploadFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    public UploadExcelVO updateSkuPirceExcel(MultipartFile file,Long downLoadHisId) {

        try {
            Map<String, List<String>> errorMap = new HashMap<>(16);
            UpdateSkuPriceFeeExcelListener staffExcelListener = new UpdateSkuPriceFeeExcelListener(this, errorMap,true);
            staffExcelListener.setDownLoadHisId(downLoadHisId);
            EasyExcel.read(file.getInputStream(), SkuPriceFeeExcelVO.class, staffExcelListener).sheet().doRead();

            UploadExcelVO uploadExcelVO=new UploadExcelVO();
            String info = getSpuImportInfo(errorMap);
            uploadExcelVO.setMessage(info);
            uploadExcelVO.setDate(staffExcelListener.getLogExcel());

            return uploadExcelVO;
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
    }

    /**
     * 处理导入的需要响应的信息
     *
     * @param errorMap 响应信息的集合
     * @return 响应信息
     */
    private String getSpuImportInfo(Map<String, List<String>> errorMap) {
        StringBuffer info = new StringBuffer();
        List<String> importTotal = errorMap.get(UpdateSkuPriceFeeExcelListener.IMPORT_ROWS);
        BigDecimal total = new BigDecimal("0");
        if (CollUtil.isNotEmpty(importTotal)) {
            for (int i = 0; i < importTotal.size(); i++) {
                String item = importTotal.get(i);
                if (StrUtil.isNotBlank(item)) {
                    total = total.add(new BigDecimal(item));
                }
            }
        }
        info.append("共有： " + total.intValue() + "条数据成功导入" + StrUtil.LF);
        // 错误信息
        List<String> errorRows = errorMap.get(UpdateSkuPriceFeeExcelListener.ERROR_ROWS);
        if (CollUtil.isNotEmpty(errorRows)) {
//            info.append("信息错误行数： " + errorRows.toArray() + StrUtil.LF);
            info.append("信息错误行数： " + errorRows.size()+ StrUtil.LF);
        }
        List<String> errors = errorMap.get(UpdateSkuPriceFeeExcelListener.OTHER);
        if (CollUtil.isNotEmpty(errors)) {
            for (String error : errors) {
                info.append(error);
            }
        }
        return info.toString();
    }

    public void importExcel(List<SkuPriceFeeExcelVO> list, Map<String, List<String>> errorMap,Long downLoadHisId) {
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("解析到0条数据");
        }
        int size = list.size();
        long storeId=1;

        //员工列表
        List<Sku> skus=new ArrayList<>();
        Map<String,Long> spuMaps=new LinkedHashMap<>();

        // 集合去重复
        list = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SkuPriceFeeExcelVO::getPriceCode))), ArrayList::new));

        // 第几行数据有误 的集合
        List<Integer> errorRows = new ArrayList<>();
        List<SkuPriceFeeExcelLogVO> logVOSMap=new ArrayList<>();
        int row = 1;
        long startTime = System.currentTimeMillis();
        log.info("----开始解析商品批量改价excel数据----");
        // 处理数据
        for (SkuPriceFeeExcelVO skuPriceFeeExcelVO : list) {
            row++;
            if(StrUtil.isBlank(skuPriceFeeExcelVO.getSpuCode())){
                errorRows.add(row);
                this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"失败","商品货号为空");
                continue;
            }
            if(StrUtil.isBlank(skuPriceFeeExcelVO.getPriceFee())){
                errorRows.add(row);
                this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"失败","商品售价为空");
                continue;
            }
            if(StrUtil.isBlank(skuPriceFeeExcelVO.getPriceCode())){
                errorRows.add(row);
                this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"失败","商品条形码为空");
                continue;
            }
            //校验spu是否存在
//            SpuVO spuVO=skuExcelService.getSpuByCode(skuPriceFeeExcelVO.getSpuCode());
//            if(spuVO==null){
//                errorRows.add(row);
//                continue;
//            }
            //校验SKU是否存在
            SkuVO skuVO=skuExcelService.getSkuBySkuPriceCode(skuPriceFeeExcelVO.getPriceCode());
            if(skuVO==null){
                errorRows.add(row);
                this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"失败","根据商品条形码未找到SKU信息");
                continue;
            }
            //校验不能低于保护价
            if(skuVO.getProtectPrice()>0){
                long priceFee = PriceUtil.toLongCent(new BigDecimal(skuPriceFeeExcelVO.getPriceFee()));
                if(priceFee < skuVO.getProtectPrice()){
                    errorRows.add(row);
                    this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"失败","售价不能低于保护价");
                    continue;
                }
            }

            if(!spuMaps.containsKey(skuPriceFeeExcelVO.getSpuCode())){
                spuMaps.put(skuPriceFeeExcelVO.getSpuCode(),skuVO.getSpuId());
            }

            Sku sku=new Sku();
            sku.setStoreId(storeId);
            if(StrUtil.isNotBlank(skuPriceFeeExcelVO.getStatus())){
                if(skuPriceFeeExcelVO.getStatus().equals("上架")){
                    sku.setStatus(1);
                }else if(skuPriceFeeExcelVO.getStatus().equals("下架")){
                    sku.setStatus(0);
                }
            }
            boolean isThreeLower=false;
            if(skuPriceFeeExcelVO.getPriceFee()!=null){
                sku.setPriceFee(PriceUtil.toLongCent(new BigDecimal(skuPriceFeeExcelVO.getPriceFee())));
                Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
                if(sku.getPriceFee() <= marketDis3Price){
                    isThreeLower=true;
                }
            }
            sku.setPriceCode(skuPriceFeeExcelVO.getPriceCode());
            skus.add(sku);

            this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"成功",isThreeLower?"低于或等于吊牌价3折":"");
        }

        log.info("结束解析商品批量改价excel数据，耗时：{}ms", System.currentTimeMillis() - startTime);

        // 批量改价
        if(CollectionUtil.isNotEmpty(skus)){
            try {
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+SkuPriceFeeExcelLogVO.EXCEL_NAME;
                //
                List<Long> spuList = new ArrayList<Long>(spuMaps.values());

                skuExcelService.updateSkuBatchByPriceCode(spuList,skus,storeId,fileName);
                String rowSuccess = ""+skus.size();
                errorMap.get(UpdateSkuPriceFeeExcelListener.IMPORT_ROWS).add(rowSuccess);

                if (CollUtil.isNotEmpty(errorRows)) {
                    List<String> collect = errorRows.stream().map(item -> item + "").collect(Collectors.toList());
                    errorMap.get(UpdateSkuPriceFeeExcelListener.ERROR_ROWS).addAll(collect);
                }

                //导入日志
                if(CollectionUtil.isNotEmpty(logVOSMap)){
                    createLogAndUpload(downLoadHisId,logVOSMap,System.currentTimeMillis() - startTime,fileName);
                }

            }catch (Exception e){
                log.info("执行商品批量改价失败：{} {}",e,e.getMessage());
                FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
                finishDownLoadDTO.setId(downLoadHisId);
                finishDownLoadDTO.setRemarks("执行商品批量改价，信息错误："+e.getMessage());
                finishDownLoadDTO.setStatus(2);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }
        //删除频繁限制操作
        RedisUtil.del("changeSkusPriceFee");
    }


    private void createLogAndUpload(Long downLoadHisId,List<SkuPriceFeeExcelLogVO> logVOSMap,Long updatePircetime,String fileName){
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        File file=null;
        try {
            int calCount=logVOSMap.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行商品批量改价生成excel 总条数【{}】",calCount);
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, SkuPriceFeeExcelLogVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(logVOSMap);
            String contentType = "application/vnd.ms-excel";
            log.info("执行商品批量改价生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,logVOSMap.size(),pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("上传文件地址："+responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("批量改价日志，耗时："+updatePircetime+" ms");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        }catch (Exception e){
            log.error("执行商品批量改价错误: "+e.getMessage(),e);
            finishDownLoadDTO.setRemarks("执行商品批量改价，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }



    private void loadErrorData(List<SkuPriceFeeExcelLogVO> logVOS,SkuPriceFeeExcelVO excelVO,String importStatus,String importRemarks){
//        String key=excelVO.getSpuCode()+""+excelVO.getPriceCode();
        SkuPriceFeeExcelLogVO logVO=new SkuPriceFeeExcelLogVO();
        mapperFacade.map(excelVO,logVO);
        logVO.setImportStatus(importStatus);
        logVO.setImportRemarks(importRemarks);
        if(!logVOS.contains(logVO)){
            logVOS.add(logVO);
        }
//        if(!logVOS.containsKey(key)){
//            logVOS.put(key,logVO);
//        }
    }

    /**
     * 过滤用户手机号已经存在的用户
     * @param list 用户数据列表
     * @param size 数据数量
     */
//    private void loadErrorData(List<SkuPriceFeeExcelVO> list, Map<String, List<String>> errorMap , int size) {
//        List<String> errorPhones = new ArrayList<>();
//        List<String> mobiles = new ArrayList<>();
//        for (SkuPriceFeeExcelVO userExcelDTO : list) {
//            mobiles.add(userExcelDTO.getMobile());
//        }
//        List<Staff> staffList = staffService.getStaffListByMobiles(mobiles);
//        mobiles = staffList.stream().map(Staff::getMobile).collect(Collectors.toList());
//        Iterator<SkuPriceFeeExcelVO> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            SkuPriceFeeExcelVO importStaffsDto = iterator.next();
//            boolean phone = mobiles.contains(importStaffsDto.getMobile());
//            if (!phone) {
//                mobiles.add(importStaffsDto.getMobile());
//                continue;
//            }
//            if (phone) {
//                errorPhones.add(importStaffsDto.getMobile());
//            } else {
//                mobiles.add(importStaffsDto.getMobile());
//            }
//            iterator.remove();
//        }
//        List<String> errorList = errorMap.get(StaffExcelListener.OTHER);
//        if (CollUtil.isNotEmpty(errorPhones)) {
//            errorList.add("手机号码：" + errorPhones.toString() + "已存在)");
//        }
//    }
}
