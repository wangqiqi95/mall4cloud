package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.ESkuPriceLogDTO;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.api.product.enums.SkuPriceLogType;
import com.mall4j.cloud.api.product.vo.SoldSkuPriceLogExcelVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.product.dto.SkuPriceLogParamDTO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuPriceLog;
import com.mall4j.cloud.product.mapper.SkuPriceLogMapper;
import com.mall4j.cloud.product.service.SkuPriceLogService;
import com.mall4j.cloud.product.vo.SkuPriceLogVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
@RefreshScope
@Slf4j
@Service
public class SkuPriceLogServiceImpl extends ServiceImpl<SkuPriceLogMapper, SkuPriceLog> implements SkuPriceLogService {

    @Value("${mall4cloud.product.saveSkuPricELog:false}")
    @Setter
    private boolean saveSkuPricELog;
    @Value("${mall4cloud.product.soldskupricelogpagesize:60000}")
    @Setter
    private Integer soldskupricelogpagesize;
    @Autowired
    private SkuPriceLogMapper skuPriceLogMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Override
    public PageVO<SkuPriceLogVO> page( SkuPriceLogParamDTO skuPriceLogDTO) {
        Integer pageNum = skuPriceLogDTO.getPageNum();
        Integer pageSize = skuPriceLogDTO.getPageSize();
        Page<SkuPriceLogVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> skuPriceLogMapper.list(skuPriceLogDTO));

        page.stream().forEach(skuPriceLogVO -> {
            if(Objects.nonNull(skuPriceLogVO.getPrice())){
                skuPriceLogVO.setPrice(PriceUtil.toDecimalPrice(Long.parseLong(skuPriceLogVO.getPrice())).toString());
            }
        });

        PageVO<SkuPriceLogVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(page.getResult());
        return pageVO;
    }


    @Async
    @Override
    public void savePriceLogs(ESkuPriceLogDTO eSkuPriceLogDTO) {
        log.info("savePriceLogs---> savePriceLogs: {}",saveSkuPricELog);
        if(!saveSkuPricELog){
            return;
        }
        try {
            log.info("保存价格日志，saveSkuPricELog：{} 数据条数：{} 备注信息：{}",
                    saveSkuPricELog,eSkuPriceLogDTO.getSkuPriceLogDTOS().size(),eSkuPriceLogDTO.getFromRemarks());
            executesavePriceLogs(eSkuPriceLogDTO.getSkuPriceLogDTOS());
        }catch (Exception e){
            log.info("保存价格日志异常",e,e.getMessage());
//            e.printStackTrace();
        }
    }

    @Override
    public void executesavePriceLogs(List<SkuPriceLogDTO> skuPriceLogDTOS) {
        if(CollectionUtil.isNotEmpty(skuPriceLogDTOS)){
            List<SkuPriceLog> skuPriceLogs=mapperFacade.mapAsList(skuPriceLogDTOS,SkuPriceLog.class);

            Map<Long, String> storeCodeMap=getStoreMap(skuPriceLogDTOS);

            skuPriceLogs.forEach(item ->{
                if(Objects.isNull(item.getBusinessType())){
                    item.setBusinessType(item.getLogType());
                }
                if(StrUtil.isBlank(item.getStoreCode()) && Objects.nonNull(item.getStoreId()) && CollectionUtil.isNotEmpty(storeCodeMap)){
                    if(storeCodeMap.containsKey(item.getStoreId())){
                        item.setStoreCode(storeCodeMap.get(item.getStoreId()));
                    }
                }
            });
            this.saveBatch(skuPriceLogs);
        }
    }

    private Map<Long,String> getStoreMap(List<SkuPriceLogDTO> skuPriceLogDTOS){
        Map<Long, String> storeCodeMap=new HashMap<>();
        List<Long> storeIds = skuPriceLogDTOS.stream()
                .filter(item->StrUtil.isBlank(item.getStoreCode()) && Objects.nonNull(item.getStoreId()))
                .map(SkuPriceLogDTO::getStoreId).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(storeIds)){//去重复
            storeIds=new ArrayList<>(new HashSet<>(storeIds));
            ServerResponseEntity<List<StoreVO>> response= storeFeignClient.listByStoreIdList(storeIds);
            if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
                storeCodeMap = response.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId,StoreVO::getStoreCode,
                        (storeCodeVO1,storeCodeVO2) -> storeCodeVO2));
            }
        }
        return storeCodeMap;
    }

    @Async
    @Override
    public void soldExcel(Long downHistoryid, SkuPriceLogParamDTO skuPriceLogDTO) {

        log.info("--价格日志导出来了 预警----");

        int currentPage = 1;
        int pageSize = soldskupricelogpagesize;
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downHistoryid);

        int totalCount = skuPriceLogMapper.soldCount(skuPriceLogDTO);
        log.info("价格日志导出总数：{}",totalCount);

        if (totalCount == 0) {
            finishDownLoadDTO.setRemarks("无价格日志导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无价格日志导出");
            return;
        }

        long startTime = System.currentTimeMillis();
        log.info("价格日志导出，开始执行导出任务--------->>>>>");
        //本次导出总条数
        finishDownLoadDTO.setCalCount(totalCount);
        //计算本次导出总页数
        int totalPage = (totalCount + pageSize -1) / pageSize;
        log.info("价格日志导出 总数:{} 总页数:{}",totalCount,totalPage);

        PageAdapter pageAdapter=new PageAdapter(currentPage,pageSize);
        List<String> filePaths=new ArrayList<>();
        int seq=1;

        List<SoldSkuPriceLogExcelVO> logExcelVOS= skuPriceLogMapper.soldExcelList(skuPriceLogDTO,pageAdapter);
        logExcelVOS.forEach(item ->{
            if(Objects.nonNull(item.getPrice())){
                item.setPrice(PriceUtil.toDecimalPrice(Long.parseLong(item.getPrice())).toString());
            }
            if(Objects.nonNull(item.getLogType())){
                item.setLogType(SkuPriceLogType.instance(Integer.parseInt(item.getLogType())).getStr());
            }
        });
        String exlcePath1=createExcelFile(logExcelVOS,seq);
        if(StrUtil.isNotBlank(exlcePath1)){
            filePaths.add(exlcePath1);
        }

        if(totalPage>1){
            for (int i = 2; i <= totalPage; i++) {
                currentPage = currentPage + 1;
                seq++;
                pageAdapter=new PageAdapter(currentPage,pageSize);
                List<SoldSkuPriceLogExcelVO> logExcelVOSPage= skuPriceLogMapper.soldExcelList(skuPriceLogDTO,pageAdapter);
                log.info("价格日志导出--->循环处理中 当前页为第【{}】页  单页限制最大条数【{}】 获取到数据总条数【{}】",pageAdapter.getBegin(),pageAdapter.getSize(),logExcelVOSPage.size());
                if(CollectionUtil.isNotEmpty(logExcelVOSPage)){
                    logExcelVOSPage.forEach(item ->{
                        if(Objects.nonNull(item.getPrice())){
                            item.setPrice(PriceUtil.toDecimalPrice(Long.parseLong(item.getPrice())).toString());
                        }
                        if(Objects.nonNull(item.getLogType())){
                            item.setLogType(SkuPriceLogType.instance(Integer.parseInt(item.getLogType())).getStr());
                        }
                    });
                    String exlcePath2=createExcelFile(logExcelVOSPage,seq);
                    if(StrUtil.isNotBlank(exlcePath2)){
                        filePaths.add(exlcePath2);
                    }
                }
            }
        }

        if(CollectionUtil.isNotEmpty(filePaths)){
            try {

                log.info("价格日志导出，导出excel文件总数 【{}】",filePaths.size());

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";

                File copyFile=new File(zipFilePathExport);
                copyFile.mkdirs();

                List<File> fromFileList=new ArrayList<>();
                List<File> backFileList=new ArrayList<>();

                filePaths.forEach(item->{
                    File file=new File(item);
                    fromFileList.add(file);
                    log.info("价格日志导出，单个excel文件信息 文件名【{}】 文件大小【{}】",file.getName(),cn.hutool.core.io.FileUtil.size(file));
                });
                //文件存放统一目录
                FileUtil.copyCodeToFile(fromFileList,zipFilePathExport,backFileList);
                //压缩统一文件目录
                String zipPath= CompressUtil.zipFile(copyFile,"zip");

                if(new File(zipPath).isFile()){

                    FileInputStream fileInputStream = new FileInputStream(zipPath);

                    String zipFileName="soldskupricelog_"+ AuthUserContext.get().getUserId()+"_"+time+".zip";
                    MultipartFile multipartFile = new MultipartFileDto(zipFileName, zipFileName,
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if(responseEntity.isSuccess()){
                        log.info("---ExcelUploadService---" + responseEntity.toString());
                        //下载中心记录
                        String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ SoldSkuPriceLogExcelVO.EXCEL_NAME;
                        finishDownLoadDTO.setFileName(fileName);
                        finishDownLoadDTO.setStatus(1);
                        finishDownLoadDTO.setFileUrl(responseEntity.getData());
                        finishDownLoadDTO.setRemarks("导出成功");
                        downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    }else{
                        finishDownLoadDTO.setStatus(2);
                        finishDownLoadDTO.setRemarks("文件上传失败");
                        downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    }
                    //删除本地临时文件
                    cn.hutool.core.io.FileUtil.del(zipPath);
                    cn.hutool.core.io.FileUtil.del(copyFile);
                }
            }catch (Exception e){
                e.printStackTrace();
                log.info("价格日志导出，excel生成zip失败 {} {}",e,e.getMessage());
                //下载中心记录
                if(finishDownLoadDTO!=null){
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks("价格日志导出，excel生成zip失败");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
            }

        }else{
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("价格日志导出，没有可导出的文件");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }

        log.info("价格日志导出，结束执行任务------>>耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    private String createExcelFile(List<SoldSkuPriceLogExcelVO> userExcelVOList, int seq){
        String file=null;
        try {
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String pathExport= SkqUtils.getExcelFilePath()+"/"+seq+"_"+time+"_"+SkqUtils.getExcelName()+".xlsx";
            EasyExcel.write(pathExport, SoldSkuPriceLogExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(userExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

}
