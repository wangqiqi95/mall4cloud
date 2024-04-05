package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.product.dto.ActivitySkuExportLogDTO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuCodeVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtils;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.product.dto.*;
import com.mall4j.cloud.product.listener.EmportProtectSpuExcelListener;
import com.mall4j.cloud.product.listener.SpuCodeReadExcelListener;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.model.ProtectActivitySpu;
import com.mall4j.cloud.product.mapper.ProtectActivitySpuMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.ProtectActivitySpuService;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
@Slf4j
@Service
public class ProtectActivitySpuServiceImpl extends ServiceImpl<ProtectActivitySpuMapper, ProtectActivitySpu> implements ProtectActivitySpuService {

    @Autowired
    private ProtectActivitySpuMapper protectActivitySpuMapper;

    @Autowired
    private SpuServiceImpl spuService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private SkuService skuService;

    @Override
    public PageVO<ProtectActivitySpuVO> page(PageDTO pageDTO, ProtectActivitySpuPageDTO spuPageDTO) {
        PageVO<ProtectActivitySpuVO> pageVO=PageUtil.doPage(pageDTO, () -> protectActivitySpuMapper.pageList(spuPageDTO));

        pageVO.getList().stream().forEach(spuVO -> {

            spuVO.setProtectPrice(PriceUtil.toDecimalPrice(Long.parseLong(spuVO.getProtectPrice())).toString());
            addStatus(spuVO);

        });
        return pageVO;
    }

    private void addStatus(ProtectActivitySpuVO spuVO){
        //状态 ： 0生效 1失效
        spuVO.setStatusStr(spuVO.getStatus()==0?"生效":"失效");
        //执行状态 : 执行状态：0待审核 1待执行 2进行中 3已结束 4驳回
        Date now=new Date();
        if(spuVO.getExStatus()==0){
            if(now.getTime()>spuVO.getEndTime().getTime()){
                spuVO.setExStatus(3);
                spuVO.setExStatusStr("已结束");
            }else {
                spuVO.setExStatusStr("待审核");
            }
        }else if(spuVO.getExStatus()==4){
            spuVO.setExStatusStr("驳回");
        }else if(Objects.nonNull(spuVO.getStartTime()) && Objects.nonNull(spuVO.getEndTime())){
            boolean flag=com.mall4j.cloud.common.util.DateUtil.isEffectiveDate(now,spuVO.getStartTime(),spuVO.getEndTime());
            if(flag){
                spuVO.setExStatus(2);
                spuVO.setExStatusStr("进行中");
            }else if(now.getTime()>spuVO.getEndTime().getTime()){
                spuVO.setExStatus(3);
                spuVO.setExStatusStr("已结束");
            }else if(spuVO.getStartTime().getTime()>now.getTime()){
                spuVO.setExStatus(1);
                spuVO.setExStatusStr("未开始");
            }
        }
    }

    @Override
    public ProtectActivitySpuVO getDetailById(Long id) {
        ProtectActivitySpu protectActivitySpu=protectActivitySpuMapper.getById(id);
        if(Objects.nonNull(protectActivitySpu)){
            ProtectActivitySpuVO activitySpuVO=mapperFacade.map(protectActivitySpu,ProtectActivitySpuVO.class);
            activitySpuVO.setProtectPrice(PriceUtil.toDecimalPrice(Long.parseLong(activitySpuVO.getProtectPrice())).toString());
            addStatus(activitySpuVO);
            Spu spu=spuService.getById(activitySpuVO.getSpuId());
            if(Objects.nonNull(spu)){
                activitySpuVO.setSpuCode(spu.getSpuCode());
                activitySpuVO.setSpuName(spu.getName());
                activitySpuVO.setSpuCode(spu.getSpuCode());
                activitySpuVO.setImgUrls(spu.getImgUrls());
                activitySpuVO.setMainImgUrl(spu.getMainImgUrl());
            }
            return activitySpuVO;
        }
        return null;
    }

    @Override
    public void saveTo(ProtectActivitySpuDTO protectActivitySpuDTO) {
        if(CollectionUtil.isNotEmpty(protectActivitySpuDTO.getSpuIds())){

            List<Spu> spuList=spuService.list(new LambdaQueryWrapper<Spu>().in(Spu::getSpuId,protectActivitySpuDTO.getSpuIds()));
            List<Sku> skuList=skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getStatus,1).in(Sku::getSpuId,protectActivitySpuDTO.getSpuIds()));

            if(CollectionUtil.isEmpty(spuList)){
                throw new LuckException("未获取到商品数据");
            }

            if(CollectionUtil.isEmpty(skuList)){
                throw new LuckException("未获取到商品sku数据");
            }

            Map<Long, Spu> spuMaps = spuList.stream().collect(Collectors.toMap(Spu::getSpuId, spu -> spu,(k1, k2)->k1));
            Map<Long, List<Sku>> skuMaps = skuList.stream().collect(Collectors.groupingBy(Sku::getSpuId));

            List<ProtectActivitySpu> saveList=new ArrayList<>();
            protectActivitySpuDTO.getSpuIds().forEach(item->{
                /**
                 * 校验电商保护价是否低于吊牌价3折
                 */
                if(!skuMaps.containsKey(item)){
                    throw new LuckException("商品货号【"+spuMaps.get(item).getSpuCode()+"】 -> 未获取到商品sku数据");
                }
                Sku sku = skuMaps.get(item).stream().filter(sku1-> Objects.nonNull(sku1.getMarketPriceFee())).min(Comparator.comparing(Sku::getMarketPriceFee)).get();
                Long marketDis3Price = sku.getMarketPriceFee() * 3 / 10;
//                Long protectPrice=protectActivitySpuDTO.getProtectPrice()*100;
                Long protectPrice=PriceUtil.toLongCent(new BigDecimal(protectActivitySpuDTO.getProtectPrice()));
                if(protectPrice < marketDis3Price){
                    throw new LuckException("商品货号【"+spuMaps.get(item).getSpuCode()+"】设置价格低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(sku.getMarketPriceFee()).longValue()+"】");
                }

                protectActivitySpuDTO.setProtectPrice(protectPrice.toString());
                ProtectActivitySpu protectActivitySpu = mapperFacade.map(protectActivitySpuDTO, ProtectActivitySpu.class);
                protectActivitySpu.setId(null);
                protectActivitySpu.setSpuId(item);
                protectActivitySpu.setProtectPrice(protectPrice);
                protectActivitySpu.setCreateBy(AuthUserContext.get().getUserId().toString());
                protectActivitySpu.setCreateTime(new Date());
                saveList.add(protectActivitySpu);
            });

            this.saveBatch(saveList);
        }

    }

    /**
     * 检查商品冲突
     * @param spuId
     * @return
     */
    private ProtectActivitySpuVO checkClash(Long id,Long spuId){
        List<ProtectActivitySpuVO> clashspus=new ArrayList<>();
        //查询进行中、审核没有驳回的商品
        Date now=new Date();
        List<ProtectActivitySpu> activitySpus=this.list(new LambdaQueryWrapper<ProtectActivitySpu>()
                .eq(ProtectActivitySpu::getSpuId,spuId)
                .eq(ProtectActivitySpu::getDelFlag,0)
                .le(ProtectActivitySpu::getStartTime,now)
                .ge(ProtectActivitySpu::getEndTime,now)
                .notIn(ProtectActivitySpu::getExStatus,4));//0待审核 1待执行 2进行中 3已结束 4驳回
        if(CollectionUtil.isNotEmpty(activitySpus)){
            List<ProtectActivitySpu> activitySpusFilter=activitySpus.stream().filter(activitySpu->!activitySpu.getId().toString().equals(id.toString())).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(activitySpusFilter)){
                ProtectActivitySpu protectActivitySpu=activitySpusFilter.get(0);
                Spu spu=spuService.getById(protectActivitySpu.getSpuId());
                ProtectActivitySpuVO protectActivitySpuVO=mapperFacade.map(protectActivitySpu,ProtectActivitySpuVO.class);
                protectActivitySpuVO.setSpuCode(spu.getSpuCode());
                clashspus.add(protectActivitySpuVO);
            }
        }
        return clashspus.size()>0?clashspus.get(0):null;
    }

    @Override
    public void updateTo(ProtectActivitySpuDTO protectActivitySpuDTO) {
        /**
         * 校验保护价是否低于吊牌价3折
         * 获取商品sku最低吊牌价
         */
        Spu spu=spuService.getById(protectActivitySpuDTO.getSpuIds().get(0));
        List<Sku> skuList=skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getStatus,1).eq(Sku::getSpuId,protectActivitySpuDTO.getSpuIds().get(0)));

        if(Objects.isNull(spu)){
            throw new LuckException("未获取到商品数据");
        }

        if(CollectionUtil.isEmpty(skuList)){
            throw new LuckException("未获取到商品sku数据");
        }
        Sku sku = skuList.stream().filter(sku1-> Objects.nonNull(sku1.getMarketPriceFee())).min(Comparator.comparing(Sku::getMarketPriceFee)).get();
        Long marketDis3Price = sku.getMarketPriceFee() * 3 / 10;
//        Long protectPrice=protectActivitySpuDTO.getProtectPrice()*100;
        Long protectPrice=PriceUtil.toLongCent(new BigDecimal(protectActivitySpuDTO.getProtectPrice()));
        if(protectPrice < marketDis3Price){
            throw new LuckException("商品货号【"+spu.getSpuCode()+"】设置价格低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(sku.getMarketPriceFee()).longValue()+"】");
        }
        protectActivitySpuDTO.setProtectPrice(protectPrice.toString());
        ProtectActivitySpu protectActivitySpu = mapperFacade.map(protectActivitySpuDTO, ProtectActivitySpu.class);
        protectActivitySpu.setSpuId(protectActivitySpuDTO.getSpuIds().get(0));
        protectActivitySpu.setProtectPrice(protectPrice);
        protectActivitySpu.setUpdateBy(AuthUserContext.get().getUserId().toString());
        protectActivitySpu.setUpdateTime(new Date());
        protectActivitySpu.setStatus(1);
        protectActivitySpu.setExStatus(0);
        this.updateById(protectActivitySpu);
    }

    @Override
    public void updateStatus(UpdateProtectActivitySpuStatusDTO protectActivitySpuDTO) {
        if(CollectionUtil.isNotEmpty(protectActivitySpuDTO.getUpdateIds())){
            List<ProtectActivitySpu> updates=new ArrayList<>();
            protectActivitySpuDTO.getUpdateIds().forEach(item->{
                ProtectActivitySpu activitySpu=this.getById(item.getId());
                if(Objects.nonNull(activitySpu)){
                    //状态生效->判断商品冲突
                    if(item.getStatus()==0){//状态：0生效 1失效

                        //校验是否过期
                        Date now=new Date();
                        if(now.getTime()>activitySpu.getEndTime().getTime()){
                            throw new LuckException("商品已过期");
                        }

                        ProtectActivitySpuVO checkActivitySpu=checkClash(activitySpu.getId(),activitySpu.getSpuId());
                        if(Objects.nonNull(checkActivitySpu)){
                            throw new LuckException("商品货号【"+checkActivitySpu.getSpuCode()+"】 " +
                                    "-> 与现有数据冲突->时间范围:"+DateUtil.format(checkActivitySpu.getStartTime(),"yyyy-MM-dd HH:mm:ss")+"-"+
                                    DateUtil.format(checkActivitySpu.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
                        }
                    }

                    ProtectActivitySpu protectActivitySpu=new ProtectActivitySpu();
                    protectActivitySpu.setId(item.getId());
                    protectActivitySpu.setStatus(item.getStatus());
                    protectActivitySpu.setUpdateBy(AuthUserContext.get().getUserId().toString());
                    protectActivitySpu.setUpdateTime(new Date());
                    updates.add(protectActivitySpu);
                }

            });
            if(CollectionUtil.isNotEmpty(updates)){
                this.updateBatchById(updates);
            }
        }
    }

    @Override
    public void updateExStatus(UpdateProtectActivitySpuStatusDTO protectActivitySpuDTO) {
        if(CollectionUtil.isNotEmpty(protectActivitySpuDTO.getUpdateIds())){
            List<ProtectActivitySpu> updates=new ArrayList<>();
            protectActivitySpuDTO.getUpdateIds().forEach(item->{
                ProtectActivitySpu activitySpu=this.getById(item.getId());
                if(Objects.nonNull(activitySpu)){
                    //状态待执行->判断商品冲突
                    if(item.getExStatus()==1 || item.getExStatus()==4){
                        //校验是否过期
                        Date now=new Date();
                        if(now.getTime()>activitySpu.getEndTime().getTime()){
                            throw new LuckException("商品已过期");
                        }
                    }
                    if(item.getExStatus()==1){//执行状态：0待审核 1待执行 2进行中 3已结束 4驳回
                        ProtectActivitySpuVO checkActivitySpu=checkClash(activitySpu.getId(),activitySpu.getSpuId());
                        if(Objects.nonNull(checkActivitySpu)){
                            throw new LuckException("商品货号【"+checkActivitySpu.getSpuCode()+"】 " +
                                    "-> 与现有数据冲突->时间范围:"+DateUtil.format(checkActivitySpu.getStartTime(),"yyyy-MM-dd HH:mm:ss")+"-"+
                                    DateUtil.format(checkActivitySpu.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
                        }
                    }

                    ProtectActivitySpu protectActivitySpu=new ProtectActivitySpu();
                    protectActivitySpu.setId(item.getId());
                    protectActivitySpu.setExStatus(item.getExStatus());
                    protectActivitySpu.setCheckTime(new Date());
                    protectActivitySpu.setCheckBy(AuthUserContext.get().getUserId().toString());
                    updates.add(protectActivitySpu);
                }
            });
            if(CollectionUtil.isNotEmpty(updates)){
                this.updateBatchById(updates);
            }
        }
    }


    @Override
    public void deleteByIds(List<Long> ids) {
        if(CollectionUtil.isNotEmpty(ids)){
            protectActivitySpuMapper.deleteBatch(ids);
        }
    }

    @Override
    public ServerResponseEntity<String> importSpus(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            Map<String, List<String>> errorMap = new HashMap<>(8);
            EmportProtectSpuExcelListener protectSpuExcelListener=new EmportProtectSpuExcelListener(errorMap);
            EasyExcel.read(inputStream, ProtectSpuExportDTO.class, protectSpuExcelListener).sheet().doRead();
            List<ProtectSpuExportDTO> list=protectSpuExcelListener.getList();

            if(CollectionUtil.isEmpty(list)){
                return ServerResponseEntity.showFailMsg("操作失败，未读取到数据");
            }
            if(list.size()>500){
                return ServerResponseEntity.showFailMsg("操作失败，最多可导入500个商品");
            }

            String logUrl=null;//导入日志文件

            //获取表格全部商品
            List<String> spuCodes = list.stream().map(ProtectSpuExportDTO::getSpuCode).collect(Collectors.toList());
            List<SpuCodeVo> spuCodeVos=spuService.listSpuBySpuCodes(spuCodes);
            if(CollectionUtil.isEmpty(spuCodeVos)){
                return ServerResponseEntity.showFailMsg("操作失败，未获取到商品数据");
            }
            Map<String,SpuCodeVo> spuCodeVoMaps=spuCodeVos.stream().collect(Collectors.toMap(SpuCodeVo::getSpuCode, a -> a,(k1,k2)->k1));

            List<Long> spuIds = spuCodeVos.stream().map(SpuCodeVo::getSpuId).collect(Collectors.toList());
            List<Sku> skuList=skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getStatus,1).in(Sku::getSpuId,spuIds));
            if(CollectionUtil.isEmpty(skuList)){
                throw new LuckException("未获取到商品sku数据");
            }
            Map<Long, List<Sku>> skuMaps = skuList.stream().collect(Collectors.groupingBy(Sku::getSpuId));


            //需要保存或者更新的数据
            Map<Long, ProtectActivitySpu> parseMaps = new HashMap<>();
            //导入日志
            List<ProtectPriceSpuLogExportDTO> spuLogExportDTOS=new ArrayList<>();
            for (ProtectSpuExportDTO exportDTO : list) {
                if(StrUtil.isBlank(exportDTO.getSpuCode())){
                    this.loadErrorData(spuLogExportDTOS,exportDTO,"商品货号不能为空");
                    continue;
                }
                if(StrUtil.isBlank(exportDTO.getProtectPrice())){
                    this.loadErrorData(spuLogExportDTOS,exportDTO,"电商保护价不能为空");
                    continue;
                }
                if(Objects.isNull(exportDTO.getStartTime()) || Objects.isNull(exportDTO.getEndTime())){
                    this.loadErrorData(spuLogExportDTOS,exportDTO,"开始或者结束时间不能为空");
                    continue;
                }
                SpuCodeVo spuCodeVo=spuCodeVoMaps.get(exportDTO.getSpuCode());
                if(Objects.isNull(spuCodeVo)){
                    this.loadErrorData(spuLogExportDTOS,exportDTO,"根据货号未获取到商品信息");
                    continue;
                }
                if(parseMaps.containsKey(spuCodeVo.getSpuId())){
                    continue;
                }
                /**
                 * 校验电商保护价是否低于吊牌价3折
                 */
                if(!skuMaps.containsKey(spuCodeVo.getSpuId())){
                    this.loadErrorData(spuLogExportDTOS,exportDTO,"根据货号未获取到商品sku信息");
                    continue;
                }
                Sku sku = skuMaps.get(spuCodeVo.getSpuId()).stream().filter(sku1-> Objects.nonNull(sku1.getMarketPriceFee())).min(Comparator.comparing(Sku::getMarketPriceFee)).get();
                Long marketDis3Price = sku.getMarketPriceFee() * 3 / 10;
                Long protectPrice=PriceUtil.toLongCent(new BigDecimal(exportDTO.getProtectPrice()));
                if(protectPrice < marketDis3Price){
                    this.loadErrorData(spuLogExportDTOS,exportDTO,"价格低于吊牌价3折，吊牌价为【"+PriceUtil.toDecimalPrice(sku.getMarketPriceFee()).longValue()+"】");
                    continue;
                }

                ProtectActivitySpu protectActivitySpu=this.getOne(new LambdaQueryWrapper<ProtectActivitySpu>()
                        .eq(ProtectActivitySpu::getSpuId,spuCodeVo.getSpuId())
                        .eq(ProtectActivitySpu::getDelFlag,0)
//                        .eq(ProtectActivitySpu::getStatus,0)//0生效 1失效
//                        .eq(ProtectActivitySpu::getExStatus,0)//0待审核 1待执行 2进行中 3已结束 4驳回
                        .ge(ProtectActivitySpu::getStartTime,exportDTO.getStartTime())
                        .le(ProtectActivitySpu::getEndTime,exportDTO.getEndTime())
                        ,false);
                if(protectActivitySpu!=null){
                    protectActivitySpu.setStartTime(exportDTO.getStartTime());
                    protectActivitySpu.setEndTime(exportDTO.getEndTime());
                    protectActivitySpu.setProtectPrice(protectPrice);
                    protectActivitySpu.setStatus(1);
                    protectActivitySpu.setExStatus(0);
                    protectActivitySpu.setUpdateTime(new Date());
                    protectActivitySpu.setUpdateBy(AuthUserContext.get().getUserId().toString());
                }else{
                    protectActivitySpu = new ProtectActivitySpu();
                    protectActivitySpu.setId(null);
                    protectActivitySpu.setStatus(1);
                    protectActivitySpu.setExStatus(0);
                    protectActivitySpu.setStartTime(exportDTO.getStartTime());
                    protectActivitySpu.setEndTime(exportDTO.getEndTime());
                    protectActivitySpu.setSpuId(spuCodeVo.getSpuId());
                    protectActivitySpu.setProtectPrice(protectPrice);
                    protectActivitySpu.setCreateBy(AuthUserContext.get().getUserId().toString());
                    protectActivitySpu.setCreateTime(new Date());
                    protectActivitySpu.setDelFlag(0);
                }
                parseMaps.put(spuCodeVo.getSpuId(),protectActivitySpu);

                this.loadErrorData(spuLogExportDTOS,exportDTO,"成功");
            }
            //修改或者更新数据
            if(CollectionUtil.isNotEmpty(parseMaps)){
                List<ProtectActivitySpu> spus=new ArrayList<>(parseMaps.values());
                this.saveOrUpdateBatch(spus);
            }
            //导入日志
            log.info("导入电商保护价商品日志条数 【{}】",spuLogExportDTOS.size());
            if (CollectionUtil.isNotEmpty(spuLogExportDTOS)) {
                try {
                    String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
                    EasyExcel.write(pathExport, ProtectPriceSpuLogExportDTO.class).sheet(ProtectPriceSpuLogExportDTO.SHEET_NAME).doWrite(spuLogExportDTOS);
                    File fileLog=new File(pathExport);
                    if(fileLog.isFile()){
                        FileInputStream fileInputStream = new FileInputStream(pathExport);

                        MultipartFile multipartFile = new MultipartFileDto(fileLog.getName(), fileLog.getName(),
                                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                        String originalFilename = multipartFile.getOriginalFilename();
                        String mimoPath = "excel/" + time + "/" + originalFilename;
                        ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                        if(responseEntity.isSuccess()){
                            log.info("导入电商保护价商品日志文件【{}】" , responseEntity.toString());
                            logUrl=responseEntity.getData();
                        }
                        //删除本地临时文件
                        cn.hutool.core.io.FileUtil.del(pathExport);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return ServerResponseEntity.success(logUrl);
        }catch (Exception e){
            e.printStackTrace();
            log.info("批量导入电商保护价失败 {} {}",e,e.getMessage());
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }


    @Async
    @Override
    public void exportSpus(ProtectActivitySpuPageDTO spuPageDTO, Long downLoadHisId) {

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        List<ProtectActivitySpuVO>  spuVOS=protectActivitySpuMapper.pageList(spuPageDTO);
        if(CollectionUtil.isEmpty(spuVOS)){
            finishDownLoadDTO.setRemarks("无商品数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无商品数据导出");
            return;
        }

        finishDownLoadDTO.setCalCount(spuVOS.size());

        List<ProtectPriceSpuExportDTO> spuExportDTOS=new ArrayList<>();
        spuVOS.forEach(spuVO->{
            ProtectPriceSpuExportDTO spuExportDTO=mapperFacade.map(spuVO,ProtectPriceSpuExportDTO.class);

            spuExportDTO.setStartTime(DateUtil.format(spuVO.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
            spuExportDTO.setEndTime(DateUtil.format(spuVO.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
            spuExportDTO.setProtectPrice(PriceUtil.conversionPrices(spuVO.getProtectPrice().toString()));
            //状态 ： 0生效 1失效
            spuExportDTO.setStatus(spuVO.getStatus()==0?"生效":"失效");
            //执行状态 : 执行状态：0待审核 1待执行 2进行中 3已结束 4驳回
            Date now=new Date();
            if(spuVO.getExStatus()==0){
                if(now.getTime()>spuVO.getEndTime().getTime()){
                    spuExportDTO.setExStatus("已结束");
                }else{
                    spuExportDTO.setExStatus("待审核");
                }
            }else if(spuVO.getExStatus()==4){
                spuExportDTO.setExStatus("驳回");
            }else if(Objects.nonNull(spuVO.getStartTime()) && Objects.nonNull(spuVO.getEndTime())){

                boolean flag=com.mall4j.cloud.common.util.DateUtil.isEffectiveDate(now,spuVO.getStartTime(),spuVO.getEndTime());
                if(flag){
                    spuExportDTO.setExStatus("进行中");
                }else if(now.getTime()>spuVO.getEndTime().getTime()){
                    spuExportDTO.setExStatus("已结束");
                }else if(spuVO.getStartTime().getTime()>now.getTime()){
                    spuExportDTO.setExStatus("未开始");
                }
            }

            spuExportDTOS.add(spuExportDTO);

        });

        if(CollectionUtil.isNotEmpty(spuExportDTOS)){
            try {
                String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
                EasyExcel.write(pathExport, ProtectPriceSpuExportDTO.class).sheet(ExcelModel.SHEET_NAME).doWrite(spuExportDTOS);
                File file=new File(pathExport);
                if(file.isFile()){
                    FileInputStream fileInputStream = new FileInputStream(pathExport);

                    MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if(responseEntity.isSuccess()){
                        log.info("---ExcelUploadService---" + responseEntity.toString());
                        //下载中心记录
                        String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ ProtectPriceSpuExportDTO.EXCEL_NAME;
                        finishDownLoadDTO.setFileName(fileName);
                        finishDownLoadDTO.setStatus(1);
                        finishDownLoadDTO.setFileUrl(responseEntity.getData());
                        finishDownLoadDTO.setRemarks("导出成功");
                        downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    }
                    //删除本地临时文件
                    cn.hutool.core.io.FileUtil.del(pathExport);
                }

            }catch (Exception e){
                //下载中心记录
                if(finishDownLoadDTO!=null){
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks("电脑保护价商品数据导出excel失败");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
            }
        }else{
            finishDownLoadDTO.setRemarks("无商品数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无商品数据导出");
        }

    }

    private void loadErrorData(List<ProtectPriceSpuLogExportDTO> logVOS, ProtectSpuExportDTO excelVO, String importRemarks) {
        ProtectPriceSpuLogExportDTO logVO = new ProtectPriceSpuLogExportDTO();
        mapperFacade.map(excelVO, logVO);
        logVO.setStartTime(DateUtil.format(excelVO.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
        logVO.setEndTime(DateUtil.format(excelVO.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        logVO.setImportRemarks(importRemarks);
        if (!logVOS.contains(logVO)) {
            logVOS.add(logVO);
        }
    }

    public static void main(String[] strings){
        String startTime="2022-06-22 15:37:08",endTime="2022-06-23 15:37:12";
        Date start=DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        Date end=DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        Date now=new Date();
        boolean flag=com.mall4j.cloud.common.util.DateUtil.isEffectiveDate(now,start,end);
        System.out.println(flag);
        if(now.getTime()>end.getTime()){
            System.out.println("已结束");
        }else if(start.getTime()>now.getTime()){
            System.out.println("未开始");
        }

        Long marketDis3Price = 39900L * 3 / 10;
        System.out.println(marketDis3Price);
    }
}
