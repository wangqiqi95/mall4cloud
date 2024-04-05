package com.mall4j.cloud.product.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.biz.feign.SendEmailFeignClient;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.OrderPriceDiscountConfigVO;
import com.mall4j.cloud.api.platform.vo.SpuPriceDiscountConfigVO;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.product.vo.SkuPriceFeeExcelLogVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.model.ElPriceProd;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.SkuStoreService;
import com.mall4j.cloud.product.service.SpuPriceService;
import com.mall4j.cloud.product.service.impl.SpuServiceImpl;
import com.mall4j.cloud.product.vo.SkuExcelLogVo;
import com.mall4j.cloud.product.vo.SkuStoreExcelLogVo;
import com.mall4j.cloud.product.vo.SkuVo;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品价格异常警告
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
//@EnableScheduling
public class ProductPirceErrorTask {

    private static final Logger logger = LoggerFactory.getLogger(ProductPirceErrorTask.class);

    private static final String LOG_TAG="商品价格异常警告";//不可修改删除，已作为服务器监控关键字匹配警告通知
    private static final String LOG_TAG_NO="定时监控商品金额";

    @Autowired
    private SpuServiceImpl spuService;

    @Autowired
    private MapperFacade mapperFacade;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private SkuStoreMapper skuStoreMapper;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private FeignShopConfig feignShopConfig;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private SendEmailFeignClient sendEmailFeignClient;


//    @Scheduled(cron = "1 * * * * ?")
    @XxlJob("productPirceError")
    public void productPirceError(){

        logger.info(LOG_TAG_NO+"{}","-----开始执行定时监控商品金额任务------");
        Long startTime=System.currentTimeMillis();

        /**
         * 上架商品
         * 当前售价低于吊牌价3折
         */

        SpuPriceDiscountConfigVO config=feignShopConfig.getSpuPirceDiscountConfig();
        Integer disp=config!=null && StrUtil.isNotBlank(config.getDiscount()) ?Integer.parseInt(config.getDiscount()):3;
//        if(disp<3){
//            disp=3;
//        }
        Double discount=(double)disp/10;
        log.info(LOG_TAG_NO+"  配置折扣比列【{}】",discount);

        //上架的spu
        List<SkuExcelLogVo> skuExcelLogVos=new ArrayList<>();//sku 异常价格日志
        List<SkuStoreExcelLogVo> skuStoreExcelLogVos=new ArrayList<>();//sku_store 异常价格日志

        //sku基础价格异常
        List<SkuVo> errorSkus=skuMapper.getPriceFeeErrorSkus(discount);
        if(CollectionUtil.isNotEmpty(errorSkus)){
            errorSkus.forEach(skuVo -> {
                if(skuVo.getPriceFee()<skuVo.getDiscountPrice()){
                    //异常价格条码
                    SkuExcelLogVo skuExcelLogVo=mapperFacade.map(skuVo,SkuExcelLogVo.class);
                    skuExcelLogVo.setDiscount(disp.toString());
                    skuExcelLogVos.add(skuExcelLogVo);
                }
            });
        }

        //门店价格(获取运营中的门店)
        ServerResponseEntity<List<StoreCodeVO>> response=storeFeignClient.storeListByStatus(1);

        if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){

            List<StoreCodeVO> storeCodeVOS=response.getData();

            Map<Long, StoreCodeVO> storeCodeVOMap = storeCodeVOS.stream().collect(Collectors.toMap(StoreCodeVO::getStoreId, a -> a,(k1, k2)->k1));
            log.info(LOG_TAG_NO+" 需要获取门店价格门店总数【{}】", storeCodeVOS.size());
            int storeSeq=0;
            for(StoreCodeVO storeCodeVO:storeCodeVOS){
                storeSeq++;
                List<SkuVo> errorSkuStoreVo=skuStoreMapper.getPriceFeeErrorSkus(discount,storeCodeVO.getStoreId(),null);

                log.info(LOG_TAG_NO+" 第【{}】个门店id【{}】 --> 获取到价格异常总数【{}】", storeSeq,storeCodeVO.getStoreId(),errorSkuStoreVo.size());

                if(CollectionUtil.isNotEmpty(errorSkuStoreVo)){
                    errorSkuStoreVo.forEach(skuVo -> {
                        if(storeCodeVOMap.containsKey(skuVo.getStoreId())){
                            if(skuVo.getPriceFee()<skuVo.getDiscountPrice()){
                                //异常价格条码
                                SkuStoreExcelLogVo skuExcelLogVo=mapperFacade.map(skuVo,SkuStoreExcelLogVo.class);
                                skuExcelLogVo.setPosPrice(Objects.nonNull(skuVo.getActivityPrice())?skuVo.getActivityPrice().toString():"0");
                                skuExcelLogVo.setStoreCode(storeCodeVOMap.get(skuVo.getStoreId()).getStoreCode());
                                skuExcelLogVo.setDiscount(disp.toString());
                                skuStoreExcelLogVos.add(skuExcelLogVo);
                            }
                        }
                    });
                }
            }
        }


        //存入sku异常价格日志
        logger.info(LOG_TAG_NO+"----------存入sku异常价格日志行数【{}】", skuExcelLogVos.size());
        if(CollectionUtil.isNotEmpty(skuExcelLogVos)){
            String skuErrorExcel=null;
            try {
                CalcingDownloadRecordDTO downloadRecordDTO_sku=new CalcingDownloadRecordDTO();
                downloadRecordDTO_sku.setDownloadTime(new Date());
                downloadRecordDTO_sku.setFileName( DateUtil.format(new Date(),"yyyyMMddHHmmss") +SkuExcelLogVo.EXCEL_NAME);
                downloadRecordDTO_sku.setCalCount(skuExcelLogVos.size());
                downloadRecordDTO_sku.setOperatorName("系统定时任务");
                downloadRecordDTO_sku.setOperatorNo("系统定时任务");
                ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO_sku);
                if(serverResponseEntity.isSuccess()){
                    Long downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
                    String pathExport= SkqUtils.getExcelFilePath()+"/sku_errorlog_"+SkqUtils.getExcelName()+".xls";
                    EasyExcel.write(pathExport, SkuExcelLogVo.class).sheet(ExcelModel.SHEET_NAME).doWrite(skuExcelLogVos);
                    String fileUrl=createLogAndUpload(downLoadHisId,pathExport);

                    skuErrorExcel=fileUrl;
                    //邮件通知
                    if(StrUtil.isNotBlank(fileUrl) && StrUtil.isNotBlank(config.getEmail())){
                        sendEmailFeignClient.sendEmailToFileUrl(fileUrl,config.getSubject(),config.getContent(),config.getEmail());
                    }
                }

            }catch (Exception e){
                log.info(LOG_TAG_NO+" 处理sku价格错误信息 {} {}",e,e.getMessage());
                e.printStackTrace();
            }

            //抛出异常警告
            log.error(LOG_TAG+"-sku 日志行数【{}】 日志文件 {}",skuExcelLogVos.size(),skuErrorExcel);
        }


        //存入sku_store异常价格日志
        logger.info(LOG_TAG_NO+"----------存入sku_store异常价格日志行数【{}】", skuExcelLogVos.size());
        if(CollectionUtil.isNotEmpty(skuStoreExcelLogVos)){
            String skuStoreErrorExcel=null;
            try {
                CalcingDownloadRecordDTO downloadRecordDTO_sku=new CalcingDownloadRecordDTO();
                downloadRecordDTO_sku.setDownloadTime(new Date());
                downloadRecordDTO_sku.setFileName( DateUtil.format(new Date(),"yyyyMMddHHmmss") +SkuStoreExcelLogVo.EXCEL_NAME);
                downloadRecordDTO_sku.setCalCount(skuStoreExcelLogVos.size());
                downloadRecordDTO_sku.setOperatorName("系统定时任务");
                downloadRecordDTO_sku.setOperatorNo("系统定时任务");
                ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO_sku);
                if(serverResponseEntity.isSuccess()){
                    Long downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
                    String pathExport= SkqUtils.getExcelFilePath()+"/skustore_errorlog_"+SkqUtils.getExcelName()+".xls";
                    EasyExcel.write(pathExport, SkuStoreExcelLogVo.class).sheet(ExcelModel.SHEET_NAME).doWrite(skuStoreExcelLogVos);
                    String fileUrl=createLogAndUpload(downLoadHisId,pathExport);

                    skuStoreErrorExcel=fileUrl;
                    //邮件通知
                    if(StrUtil.isNotBlank(fileUrl) && StrUtil.isNotBlank(config.getEmail())){
                        sendEmailFeignClient.sendEmailToFileUrl(fileUrl,config.getSubject(),config.getContent(),config.getEmail());
                    }
                }

            }catch (Exception e){
                log.info(LOG_TAG_NO+" 处理sku_store价格错误信息 {} {}",e,e.getMessage());
                e.printStackTrace();
            }

            //抛出异常警告
            log.error(LOG_TAG+"-skustore 日志行数【{}】 日志文件 {}",skuStoreExcelLogVos.size(),skuStoreErrorExcel);
        }

        logger.info(LOG_TAG_NO+"-----结束执行定时监控商品金额任务------耗时：{}ms", (System.currentTimeMillis() - startTime));

    }


    private String createLogAndUpload(Long downLoadHisId,String pathExport){
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        File file=null;
        try {
            String contentType = "application/vnd.ms-excel";
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
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            return responseEntity.getData();
        }catch (Exception e){
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
            return null;
        }
    }

    public static void main(String[] strings){
//        int pageSize=50000;
//        int totalCount=18283159;
////        int totalCount=11445961;
//        int totalPage = (totalCount + pageSize -1) / pageSize;
//        System.out.println(""+totalPage);

        Double discount=(double) 3/10;
        System.out.println(discount);
    }
}
