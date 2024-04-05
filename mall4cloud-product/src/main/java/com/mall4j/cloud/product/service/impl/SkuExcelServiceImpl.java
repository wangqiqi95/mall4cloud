package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.ActivitySkuExportLogDTO;
import com.mall4j.cloud.api.product.enums.SkuPriceLogType;
import com.mall4j.cloud.api.product.dto.ESkuPriceLogDTO;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.api.product.vo.SkuPriceFeeExcelLogVO;
import com.mall4j.cloud.api.product.vo.SkuPriceFeeThreeLowerLogVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtils;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.product.constant.SpuExportError;
import com.mall4j.cloud.product.dto.ActivitySkuExportDTO;
import com.mall4j.cloud.product.listener.UpdateSkuPriceFeeExcelListener;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.mapper.SpuStoreMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SpuStore;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Slf4j
@Service
public class SkuExcelServiceImpl implements SkuExcelService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuStoreMapper spuStoreMapper;
    @Autowired
    private SkuStoreMapper skuStoreMapper;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private SkuPriceLogService skuPriceLogService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSkuBatchByPriceCode(List<Long> spus,List<Sku> skus,Long storeId,String fileName) {

        long startTime = System.currentTimeMillis();
        log.info("----开始执行商品批量改价----");
        Date now=new Date();
//        //TODO 1、批量修改商品sku表：售价price_fee activity_price=price_fee、状态status，条件为price_code
        //修改sku
        skuMapper.updateSkuBatchByPriceCode(skus);
        //修改sku_store
        //TODO 2、批量修改商品sku_store表：售价price_fee条件为price_code store_id
        skuStoreMapper.updateSkuStoreBatchByPriceCode(skus);

//        if(skus.size()>50){
//            //一次500条
//            int number = 50;
//            int limit = (skus.size() + number - 1) / number;
//            log.info("总批次【{}】",limit);
//            //分成limit次发请求到数据库，in（）操作时   可以把多条数据分割成多组请求
//            Stream.iterate(0, n -> n + 1).limit(limit).forEach(a -> {
//                //获取后面1000条中的前500条
//                // 拿到这个参数的流的 （a * applyIdSelectSize）后面的数据  .limit（applyIdSelectSize）->后面数据的500条  .collect(Collectors.toList()->组成一个toList
//                List<Sku> paperEntityList = skus.stream().skip(a * number).limit(number).collect(Collectors.toList());
//                log.info("批次【{}】 执行数量【{}】",a,paperEntityList.size());
//                //doSomething();  eg：数据库操作
//                //TODO 1、批量修改商品sku表：售价price_fee、状态status，条件为price_code
//                //修改sku
//                skuMapper.updateSkuBatchByPriceCode(paperEntityList);
//                //修改sku_store
//                //TODO 2、批量修改商品sku_store表：售价price_fee条件为price_code store_id
//                skuStoreMapper.updateSkuStoreBatchByPriceCode(paperEntityList);
//            });
//        }else{
//            //TODO 1、批量修改商品sku表：售价price_fee、状态status，条件为price_code
//            //修改sku
//            skuMapper.updateSkuBatchByPriceCode(skus);
//            //修改sku_store
//            //TODO 2、批量修改商品sku_store表：售价price_fee条件为price_code store_id
//            skuStoreMapper.updateSkuStoreBatchByPriceCode(skus);
//        }


        //修改商品spu：
        int spusize=CollectionUtil.isNotEmpty(spus)?spus.size():0;
        log.info("修改商品spu 商品数量【{}】",spusize);

        if(CollectionUtil.isNotEmpty(spus)){
            List<SpuVO> updateSpu=new ArrayList<>();
            List<SpuStore> updateSpuStore=new ArrayList<>();
            for(Long spuId:spus){
//                List<SkuVO> skuData = skuMapper.getSpuSkuInfo(spuId, storeId);
                List<Sku> skuData = skuService.lambdaQuery().in(Sku::getSpuId, spuId).list();
                if(CollectionUtil.isNotEmpty(skuData)){
                    Sku skuVO = skuData.stream().min(Comparator.comparing(Sku::getPriceFee)).get();

                    SpuVO spu=new SpuVO();
                    spu.setSpuId(spuId);
                    spu.setPriceFee(skuVO.getPriceFee());
                    spu.setUpdateTime(now);
                    updateSpu.add(spu);

                    SpuStore spuStore=new SpuStore();
                    spuStore.setSpuId(spuId);
                    spuStore.setStoreId(storeId);
                    spuStore.setPriceFee(skuVO.getPriceFee());
                    spuStore.setUpdateTime(now);
                    updateSpuStore.add(spuStore);
                }

            }
            //TODO 3、同时修改商品spu表：price_fee 取sku最低售价 条件为spu_id
            if(CollectionUtil.isNotEmpty(updateSpu)){
                spuMapper.updateBatch(updateSpu);
            }
            //TODO 4、同时修改商品spu_store表：price_fee 取sku最低售价 条件为spu_id、store_id
            if(CollectionUtil.isNotEmpty(updateSpuStore)){
                spuStoreMapper.updateBatchPriceFee(updateSpuStore);
            }
        }

        //TODO 保存改价日志
        saveSkuPriceLog(skus,now,fileName);

        log.info("结束执行商品批量改价，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    @Override
    public SkuVO getSkuBySkuPriceCode(String priceCode) {
        return skuMapper.getSkuBySkuPriceCode(priceCode);
    }

    @Override
    public SpuVO getSpuByCode(String spuCode) {
        return spuMapper.getSpuByCode(spuCode);
    }

    @Override
    public String spuExportError(Map<Integer, List<String>> errorMap) {
        StringBuilder info = new StringBuilder();
        for (Integer key : errorMap.keySet()) {
            List<String> list = errorMap.get(key);
            SpuExportError spuExportError = SpuExportError.instance(key);
            if (Objects.equals(spuExportError, SpuExportError.OTHER)) {
                info.append(list.toString() + "\n");
                continue;
            }
            info.append(spuExportError.errorInfo() + list.toString() + "\n");
        }
        return info.toString();
    }

    private void saveSkuPriceLog(List<Sku> skus,Date now,String remarks){
        if(CollectionUtil.isNotEmpty(skus)){
            StoreVO storeVO=storeFeignClient.findByStoreId(Constant.MAIN_SHOP);
            List<SkuPriceLogDTO> skuPriceLogs=new ArrayList<>();
            skus.forEach(sku -> {
                String spuCode=sku.getPriceCode().split("-").length>1?sku.getPriceCode().substring(0, sku.getPriceCode().indexOf("-")):sku.getPriceCode();
                SkuPriceLogDTO skuPriceLogDTO=new SkuPriceLogDTO();
                skuPriceLogDTO.setSpuCode(spuCode);
                skuPriceLogDTO.setPriceCode(sku.getPriceCode());
                skuPriceLogDTO.setLogType(SkuPriceLogType.APP_UPDATE_BATCH_PRICE.value());
                skuPriceLogDTO.setPrice(sku.getPriceFee().longValue());
                skuPriceLogDTO.setStoreId(storeVO.getStoreId());
                skuPriceLogDTO.setStoreCode(storeVO.getStoreCode());
                skuPriceLogDTO.setUpdateTime(now);
                skuPriceLogDTO.setRemarks(remarks);
                skuPriceLogDTO.setUpdateBy(AuthUserContext.get().getUsername());
                skuPriceLogs.add(skuPriceLogDTO);
            });

            //TODO 保存价格日志
            skuPriceLogService.savePriceLogs(new ESkuPriceLogDTO(skuPriceLogs,
                    AuthUserContext.get().getUsername()+":商品主数据批量改价"+ DateUtil.format(now, "yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * 批量改价3折校验
     * @return
     */
    @Override
    public String checkThreeLower(MultipartFile multipartFile) {
        try {
            Map<String, List<String>> errorMap = new HashMap<>(16);
            UpdateSkuPriceFeeExcelListener staffExcelListener = new UpdateSkuPriceFeeExcelListener(null, errorMap,false);
            EasyExcel.read(multipartFile.getInputStream(), SkuPriceFeeExcelVO.class, staffExcelListener).sheet().doRead();
            List<SkuPriceFeeExcelVO> list=staffExcelListener.getBackList();
            if(CollectionUtil.isEmpty(list)){
                return null;
            }
            // 集合去重复
            list = list.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SkuPriceFeeExcelVO::getPriceCode))), ArrayList::new));

            List<SkuPriceFeeThreeLowerLogVO> logVOSMap=new ArrayList<>();
            for (SkuPriceFeeExcelVO skuPriceFeeExcelVO : list) {
                if(StrUtil.isBlank(skuPriceFeeExcelVO.getPriceFee())){
                    continue;
                }
                if(StrUtil.isBlank(skuPriceFeeExcelVO.getPriceCode())){
                    continue;
                }
                SkuVO skuVO=this.getSkuBySkuPriceCode(skuPriceFeeExcelVO.getPriceCode());
                if(skuVO==null){
                    continue;
                }
                Long prideFee=PriceUtil.toLongCent(new BigDecimal(skuPriceFeeExcelVO.getPriceFee()));
                Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
                if(prideFee <= marketDis3Price){
                    this.loadErrorData(logVOSMap,skuPriceFeeExcelVO,"低于或等于吊牌价3折");
                }
            }

            //低于3折日志
            if(CollectionUtil.isNotEmpty(logVOSMap)){
                log.info("低于3折数量：{}",logVOSMap.size());
                return sendLogThreeLower(logVOSMap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String sendLogThreeLower(List<SkuPriceFeeThreeLowerLogVO> logVOSMap){
        //下载中心记录
        File file=null;
        try {
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, SkuPriceFeeThreeLowerLogVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(logVOSMap);
            String contentType = "application/vnd.ms-excel";
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess()) {
                log.info("批量改价低于3折文件 {}",responseEntity.getData());
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
                return responseEntity.getData();
            }
        }catch (Exception e){
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
        return null;
    }

    private void loadErrorData(List<SkuPriceFeeThreeLowerLogVO> logVOS, SkuPriceFeeExcelVO excelVO, String importRemarks){
        SkuPriceFeeThreeLowerLogVO logVO=new SkuPriceFeeThreeLowerLogVO();
        mapperFacade.map(excelVO,logVO);
        logVO.setImportRemarks(importRemarks);
        if(!logVOS.contains(logVO)){
            logVOS.add(logVO);
        }
    }
}
