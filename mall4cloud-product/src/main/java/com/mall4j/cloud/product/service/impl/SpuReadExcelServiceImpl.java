package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.api.product.dto.ActivitySpuSkuExportLogDTO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtils;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.product.dto.ActivitySpuSkuExportDTO;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuReadExcelService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.vo.SkuExcelImportVO;
import com.mall4j.cloud.product.vo.SpuExcelImportDataVO;
import com.mall4j.cloud.product.vo.SpuExcelImportVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2022年10月17日, 0017 15:30
 */
@Slf4j
@Service
public class SpuReadExcelServiceImpl implements SpuReadExcelService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ExcelUploadFeignClient excelUploadFeignClient;

    @Override
    public SpuExcelImportDataVO importParseSpus(MultipartFile multipartFile) {
        File file = FileUtil.transferFile(multipartFile);
        InputStream inputStream = null;
        List<SpuExcelImportVO> parseList = new ArrayList<SpuExcelImportVO>();
        Map<Long,SpuExcelImportVO> parseMaps = new HashMap<>();
        SpuExcelImportDataVO spuExcelImportDataVO = new SpuExcelImportDataVO();
        try {
            inputStream = new FileInputStream(file);
            ExcelUtils<ActivitySpuSkuExportDTO> excelUtil = new ExcelUtils(ActivitySpuSkuExportDTO.class);
            List<ActivitySpuSkuExportDTO> list = excelUtil.importExcel("批量导入数据", inputStream);

            Map<String,ActivitySpuSkuExportLogDTO> logDTOS = new LinkedHashMap<>();
            List<ActivitySpuSkuExportDTO> newList = cleanExcelData(list, logDTOS);

            List<ActivitySpuSkuExportDTO> importList = new ArrayList<>();
            importList.addAll(newList);

            for (ActivitySpuSkuExportDTO skuExportDTO : importList) {
                //条形码
                if (StrUtil.isNotEmpty(skuExportDTO.getSkuBarcode())) {
                    Sku skuVO = skuService.getOne(new LambdaQueryWrapper<Sku>()
                            .eq(Sku::getSkuCode,skuExportDTO.getSkuBarcode())
                            .eq(Sku::getStatus,1),false);
                    if (skuVO == null) {
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品条形码在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    //判断3折兜底
                    Long marketDis3Price = skuVO.getMarketPriceFee() * 3 / 10;
                    Long discountPrice= PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
                    if(discountPrice < marketDis3Price){
                        this.loadErrorData(logDTOS, skuExportDTO, "成功", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
//                        continue;
                    }
                    if(discountPrice > skuVO.getMarketPriceFee()){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()).longValue()+"】");
                        continue;
                    }
                    SkuExcelImportVO skuImportVO = mapperFacade.map(skuVO, SkuExcelImportVO.class);
                    skuImportVO.setDiscountPrice(skuExportDTO.getDiscountPrice());

                    Spu spuVO = spuService.getById(skuVO.getSpuId());
                    if(Objects.isNull(spuVO)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                    importSpuVO.setParticipationMode(1);
                    importSpuVO.setSeq(skuExportDTO.getSeq());

                    List<SkuExcelImportVO> skus = new ArrayList<>();
                    skus.add(skuImportVO);
                    importSpuVO.setSkus(skus);
                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                    }else{
                        parseMaps.get(importSpuVO.getSpuId()).getSkus().add(skuImportVO);
                    }

                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");

                    continue;
                }

                //skuCode(priceCode)
                if (StrUtil.isNotEmpty(skuExportDTO.getPriceCode())) {
                    List<Sku> skus= skuService.list(new LambdaQueryWrapper<Sku>()
                            .eq(Sku::getPriceCode,skuExportDTO.getPriceCode())
                            .eq(Sku::getStatus,1));
                    if(CollectionUtil.isEmpty(skus)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品skuCode在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    Long marketPriceFee =skus.get(0).getMarketPriceFee();
                    if(Objects.isNull(marketPriceFee)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品吊牌价为空");
                        continue;
                    }
                    //判断3折兜底
                    Long marketDis3Price = marketPriceFee * 3 / 10;
                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
                    if(discountPrice < marketDis3Price){
                        this.loadErrorData(logDTOS, skuExportDTO, "成功", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
//                        continue;
                    }
                    if(discountPrice > marketPriceFee){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
                        continue;
                    }

                    Spu spuVO = spuService.getById(skus.get(0).getSpuId());
                    if(Objects.isNull(spuVO)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                    importSpuVO.setParticipationMode(2);
                    importSpuVO.setSeq(skuExportDTO.getSeq());
                    List<SkuExcelImportVO> importSkus = mapperFacade.mapAsList(skus, SkuExcelImportVO.class);
                    importSkus.forEach(item ->{
                        item.setDiscountPrice(skuExportDTO.getDiscountPrice());
                    });
                    importSpuVO.setSkus(importSkus);
                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                    }else{
                        parseMaps.get(importSpuVO.getSpuId()).getSkus().addAll(importSkus);
                    }

                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");

                    continue;
                }

                //商品货号
                if (StrUtil.isNotEmpty(skuExportDTO.getSpuBarcode())) {
                    SpuVO spuVO = spuMapper.getSpuByCode(skuExportDTO.getSpuBarcode());
                    if (spuVO == null) {
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品货号在数据库中不存在，请检查后重新导入");
                        continue;
                    }

                    List<SkuVO> skus = skuService.getSkuBySpuId(spuVO.getSpuId());
                    if(CollectionUtil.isEmpty(skus)){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品条形码在数据库中不存在，请检查后重新导入");
                        continue;
                    }
                    Long marketPriceFee = spuVO.getMarketPriceFee();
                    if(CollectionUtil.isNotEmpty(skus)){
                        marketPriceFee=skus.get(0).getMarketPriceFee();
                    }
                    //判断3折兜底
                    Long marketDis3Price = marketPriceFee * 3 / 10;
                    Long discountPrice=PriceUtil.toLongCent(skuExportDTO.getDiscountPrice());
                    if(discountPrice < marketDis3Price){
                        this.loadErrorData(logDTOS, skuExportDTO, "成功", "调价低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
//                        continue;
                    }
                    if(discountPrice > marketPriceFee){
                        this.loadErrorData(logDTOS, skuExportDTO, "失败", "活动价不能高于吊牌价，吊牌价为【"+PriceUtil.toDecimalPrice(marketPriceFee).longValue()+"】");
                        continue;
                    }

                    SpuExcelImportVO importSpuVO = mapperFacade.map(spuVO, SpuExcelImportVO.class);
                    importSpuVO.setParticipationMode(0);
                    importSpuVO.setDiscountPrice(skuExportDTO.getDiscountPrice());
                    importSpuVO.setSeq(skuExportDTO.getSeq());
                    List<SkuExcelImportVO> importSkus = mapperFacade.mapAsList(skus, SkuExcelImportVO.class);
                    importSpuVO.setSkus(importSkus);
                    if(!parseMaps.containsKey(importSpuVO.getSpuId())){
                        parseMaps.put(importSpuVO.getSpuId(),importSpuVO);
                    }

                    this.loadErrorData(logDTOS, skuExportDTO, "成功", "");
                }
            }

            //导入日志
            if (CollectionUtil.isNotEmpty(logDTOS)) {

                List<ActivitySpuSkuExportLogDTO> logDTOList=new ArrayList<>(logDTOS.values());

                //排序
                Collections.sort(logDTOList, new Comparator<ActivitySpuSkuExportLogDTO>() {
                    public int compare(ActivitySpuSkuExportLogDTO o1, ActivitySpuSkuExportLogDTO o2) {
                        //升序
                        if(Objects.nonNull(o1.getSeq()) && Objects.nonNull(o2.getSeq())){
                            return o1.getSeq().compareTo(o2.getSeq());
                        }
                        return 0;
                    }
                });

                ExcelUploadDTO excelUploadDTO = new ExcelUploadDTO(null,
                        logDTOList,
                        ActivitySpuSkuExportLogDTO.EXCEL_NAME,
                        ActivitySpuSkuExportLogDTO.MERGE_ROW_INDEX,
                        ActivitySpuSkuExportLogDTO.MERGE_COLUMN_INDEX,
                        ActivitySpuSkuExportLogDTO.class);
                ServerResponseEntity<String> response = excelUploadFeignClient.createAnduploadExcel(excelUploadDTO);
                if (response.isSuccess()) {
//                    List<ActivitySpuSkuExportLogDTO> errorLogs=logDTOList.stream().filter(logDTO -> StrUtil.isNotBlank(logDTO.getImportStatus()) && logDTO.getImportStatus().equals("失败")).collect(Collectors.toList());
                    spuExcelImportDataVO.setDate(response.getData());
                    if(CollectionUtil.isNotEmpty(logDTOList)){
                        spuExcelImportDataVO.setCount(""+logDTOList.size());
                    }else{
                        spuExcelImportDataVO.setCount("0");
                    }
                }
            }

        } catch (LuckException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导入限时调价商品模版文件 操作失败--->",e,e.getMessage());
            throw new LuckException("操作失败，请检查模板及内容是否正确");
        }

        if(CollectionUtil.isNotEmpty(parseMaps)){
            parseList=new ArrayList<>(parseMaps.values());

            //排序
            Collections.sort(parseList, new Comparator<SpuExcelImportVO>() {
                public int compare(SpuExcelImportVO o1, SpuExcelImportVO o2) {
                    //升序
                    if(Objects.nonNull(o1.getSeq()) && Objects.nonNull(o2.getSeq())){
                        return o1.getSeq().compareTo(o2.getSeq());
                    }
                    return 0;
                }
            });
        }
        log.info("限时调价导入获取商品数量 【{}】",parseList.size());
        spuExcelImportDataVO.setParseList(parseList);
        return spuExcelImportDataVO;
    }

    private List<ActivitySpuSkuExportDTO> cleanExcelData(List<ActivitySpuSkuExportDTO> list, Map<String,ActivitySpuSkuExportLogDTO> logDTOS) {

        Map<String, List<ActivitySpuSkuExportDTO>> spuMaps = list.stream().filter(spu->StrUtil.isNotBlank(spu.getSpuBarcode())).collect(Collectors.groupingBy(ActivitySpuSkuExportDTO::getSpuBarcode));
        Map<String, List<ActivitySpuSkuExportDTO>> skuPriceCodeMaps = list.stream().filter(sku->StrUtil.isNotBlank(sku.getPriceCode())).collect(Collectors.groupingBy(ActivitySpuSkuExportDTO::getPriceCode));
        Map<String, List<ActivitySpuSkuExportDTO>> skuMaps = list.stream().filter(sku->StrUtil.isNotBlank(sku.getSkuBarcode())).collect(Collectors.groupingBy(ActivitySpuSkuExportDTO::getSkuBarcode));

        List<ActivitySpuSkuExportDTO> newList = new ArrayList<>();
        for (ActivitySpuSkuExportDTO skuExportDTO : list) {

            //手动判断去除空行
            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode())
                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode())
                    && StrUtil.isEmpty(skuExportDTO.getPriceCode())) {
                continue;
            }
            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode()) && StrUtil.isEmpty(skuExportDTO.getSkuName())
                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode()) && skuExportDTO.getDiscountPrice() == null && skuExportDTO.getPrice() == null) {
                continue;
            }
            if(Objects.isNull(skuExportDTO.getSeq())){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品序号不能为空，请检查数据重新上传");
                continue;
            }
            if(CollectionUtil.isNotEmpty(spuMaps)
                    && spuMaps.containsKey(skuExportDTO.getSpuBarcode())
                    && spuMaps.get(skuExportDTO.getSpuBarcode()).size()>1){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品货号重复，请检查数据重新上传");
                continue;
            }
            if(CollectionUtil.isNotEmpty(skuMaps)
                    && skuMaps.containsKey(skuExportDTO.getSkuBarcode())
                    && skuMaps.get(skuExportDTO.getSkuBarcode()).size()>1){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该商品条形码重复，请检查数据重新上传");
                continue;
            }
            if(CollectionUtil.isNotEmpty(skuPriceCodeMaps)
                    && skuPriceCodeMaps.containsKey(skuExportDTO.getPriceCode())
                    && skuPriceCodeMaps.get(skuExportDTO.getPriceCode()).size()>1){
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "该skuCode重复，请检查数据重新上传");
                continue;
            }
            if (skuExportDTO.getDiscountPrice() == null || skuExportDTO.getDiscountPrice().equals(BigDecimal.ZERO)) {
//                Assert.faild("商品调价金额不能为空，请检查数据重新上传");
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品调价金额不能为空，请检查数据重新上传");
                continue;
            }
            if (NumberUtil.isLess(skuExportDTO.getDiscountPrice(), BigDecimal.ZERO)) {
//                Assert.faild("商品调价金额不能小于0，请检查数据重新上传");
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "商品调价金额不能小于0，请检查数据重新上传");
                continue;
            }
            if (StrUtil.isEmpty(skuExportDTO.getSkuBarcode())
                    && StrUtil.isEmpty(skuExportDTO.getSpuBarcode())
                    && StrUtil.isEmpty(skuExportDTO.getPriceCode())) {
//                Assert.faild("货号或者条形码不允许都为空，请检查数据重新上传");
                this.loadErrorData(logDTOS, skuExportDTO, "失败", "货号或者条形码、skuCode不允许都为空，请检查数据重新上传");
                continue;
            }
            //如果条码和货号同时存在，直接报错提示。
            if(StrUtil.isNotEmpty(skuExportDTO.getSkuBarcode())
                    && StrUtil.isNotEmpty(skuExportDTO.getSpuBarcode())
                    && StrUtil.isNotEmpty(skuExportDTO.getPriceCode())){
                Assert.faild(StrUtil.format("上传数据不允许货号和条码、skuCode同时存在，货号:{},条码:{}，请检查数据重新上传",skuExportDTO.getSkuBarcode(),skuExportDTO.getSpuBarcode()));
            }
            newList.add(skuExportDTO);
        }
        return newList;
    }

    private void loadErrorData(Map<String,ActivitySpuSkuExportLogDTO> logVOS, ActivitySpuSkuExportDTO excelVO, String importStatus, String importRemarks) {
        String key=excelVO.getSpuBarcode()+""+excelVO.getPriceCode()+""+excelVO.getSkuBarcode();
        if(!logVOS.containsKey(key)){
            ActivitySpuSkuExportLogDTO logVO = new ActivitySpuSkuExportLogDTO();
            mapperFacade.map(excelVO, logVO);
            logVO.setImportStatus(importStatus);
            logVO.setImportRemarks(importRemarks);
            logVOS.put(key,logVO);
        }

    }

//    private void loadErrorData(List<ActivitySpuSkuExportLogDTO> logVOS, ActivitySpuSkuExportDTO excelVO, String importStatus, String importRemarks) {
//        ActivitySpuSkuExportLogDTO logVO = new ActivitySpuSkuExportLogDTO();
//        mapperFacade.map(excelVO, logVO);
//        if(!logVOS.contains(logVO)){
//            logVO.setImportStatus(importStatus);
//            logVO.setImportRemarks(importRemarks);
//            logVOS.add(logVO);
//        }
//    }
}
