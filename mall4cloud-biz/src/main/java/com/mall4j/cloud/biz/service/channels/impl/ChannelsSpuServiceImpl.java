package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.dto.channels.EcProductDetail;
import com.mall4j.cloud.api.biz.dto.channels.EcSkus;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.biz.vo.ChannelsSpuSkuVO;
import com.mall4j.cloud.api.biz.vo.ChannelsSpuVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.wx.wx.channels.EcBasicsApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.ChannelsSkuDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsSpuLogDTO;
import com.mall4j.cloud.biz.dto.channels.PriceCheckDTO;
import com.mall4j.cloud.biz.dto.channels.event.ProductSpuAuditDTO;
import com.mall4j.cloud.biz.dto.channels.query.ChannelsSpuQueryDTO;
import com.mall4j.cloud.biz.dto.channels.request.AddChannlesSpuRequest;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSkuMapper;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSpuMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.service.channels.*;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuExtraVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuProductSpuVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuSkuDetailVO;
import com.mall4j.cloud.biz.vo.channels.excel.ChannelsCheckPriceExcelVO;
import com.mall4j.cloud.biz.vo.channels.excel.ChannelsSpuSkuExcelVO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
@Slf4j
@Service
public class ChannelsSpuServiceImpl extends ServiceImpl<ChannelsSpuMapper, ChannelsSpu> implements ChannelsSpuService {

    @Autowired
    private ChannelsSpuMapper channelsSpuMapper;
    @Autowired
    ChannelsSkuMapper channelsSkuMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    EcProductService ecProductService;
    @Autowired
    private ChannelsSpuUpdateLogService channelsSpuUpdateLogService;
    @Autowired
    private ChannelsSkuService channelsSkuService;
    @Autowired
    WxConfig wxConfig;
    @Autowired
    EcBasicsApi ecBasicsApi;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private ChannelsWindowService channelsWindowService;

    @Autowired
    private LeagueItemService leagueItemService;

    private final Integer MAX_EXCEL_PAGE_SIZE = 5000;

    @Override
    public PageVO<ChannelsSpu> page(ChannelsSpuQueryDTO dto) {
        Wrapper<ChannelsSpu> queryWrapper = getQueryWrapper(dto);
        return PageUtil.doPage(dto, () -> channelsSpuMapper.selectList(queryWrapper));
    }

    private Wrapper<ChannelsSpu> getQueryWrapper(ChannelsSpuQueryDTO dto) {
        LambdaQueryWrapper<ChannelsSpu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper = Wrappers.lambdaQuery(ChannelsSpu.class)
                .in(StrUtil.isNotBlank(dto.getStatus()), ChannelsSpu::getStatus, dto.getStatus().split(","))
                .in(StrUtil.isNotBlank(dto.getEditStatus()), ChannelsSpu::getEditStatus, dto.getEditStatus().split(","))
                .eq(Objects.nonNull(dto.getIsWindow()), ChannelsSpu::getInWinodws, dto.getIsWindow());

        if (StrUtil.isNotBlank(dto.getCodeOrName())){
            queryWrapper.and(and -> and
                    .in(ChannelsSpu::getSpuCode, dto.getCodeOrName().split(","))
                    .or()
                    .like(ChannelsSpu::getTitle, dto.getCodeOrName()));
        }

        return queryWrapper;
    }

    @Override
    public ChannelsSpuSkuDetailVO getById(Long id) {
        ChannelsSpu channelsSpu = channelsSpuMapper.getById(id);
        List<ChannelsSku> channelsSkus = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getChannelsSpuId, id));
        synchronizeChannelsSku(id, channelsSkus);

        // 组装sku信息


        return null;
    }

    /**
     * 同步视频号sku信息
     * @param id spuId
     * @param channelsSkus 涉及的sku
     */
    private void synchronizeChannelsSku(Long id, List<ChannelsSku> channelsSkus) {
        boolean isSynchronizeChannels = false;
        Map<Long, ChannelsSku> channelsSkuMapBySkuId = new HashMap<>();
        for (ChannelsSku sku : channelsSkus) {
            if (Objects.isNull(sku.getOutSkuId())) {
                isSynchronizeChannels = true;
                channelsSkuMapBySkuId = channelsSkus.stream()
                        .collect(Collectors.toMap(ChannelsSku::getSkuId, channelsSku -> channelsSku));
                break;
            }
        }
        if (isSynchronizeChannels) {
            EcProductResponse ecProduct = getEcProductById(id, 3);
            EcProductDetail detail = ecProduct.getEdit_product();

            if (Objects.isNull(detail)) {
                Assert.faild("获取商品信息异常");
            }

            List<EcSkus> ecSkus = detail.getSkus();
            for (EcSkus ecSku : ecSkus) {
                // 将视频号里的skuId同步到 channelsSku表中去，ecOutSkuId = channelsSkuId; channelsOutSkuId = ecSkuId;
                Long outSkuId = Long.parseLong(ecSku.getOut_sku_id());
                ChannelsSku sku = channelsSkuMapBySkuId.get(outSkuId);
                Assert.isNull(sku, "商品信息不一致");
                long ecSkuId = Long.parseLong(ecSku.getSku_id());
                sku.setOutSkuId(ecSkuId);
                channelsSkuMapper.updateById(sku);
            }
        }
    }


    @Override
    public void update(ChannelsSpu channelsSpu) {
        channelsSpuMapper.update1(channelsSpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        ChannelsSpu channelsSpu = channelsSpuMapper.selectById(id);
        if (Objects.isNull(channelsSpu)){
            Assert.faild("商品不存在，请效验参数");
        }
        // 先完成商品下架
        if (channelsSpu.getStatus() == 5) {
            delisting(channelsSpu);
        }
        ecProductService.delete(StrUtil.toString(channelsSpu.getOutSpuId()));

        List<ChannelsSku> channelsSkuList = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getChannelsSpuId, channelsSpu.getId()));
        // 库存重置
        deListingZeroSetStock(channelsSpu, channelsSkuList);

        channelsSpuMapper.deleteById(id);
        channelsSkuMapper.delete(Wrappers.lambdaQuery(ChannelsSku.class).eq(ChannelsSku::getChannelsSpuId, id));

        channelsSpuUpdateLogService.saveLog(new ChannelsSpuLogDTO(channelsSpu, null, 3));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addChannelsSpu(AddChannlesSpuRequest addChannlesSpuRequest) {
        Integer spuCount = channelsSpuMapper.selectCount(Wrappers.lambdaQuery(ChannelsSpu.class)
                .eq(ChannelsSpu::getSpuId, addChannlesSpuRequest.getSpuId()));
        if (spuCount > 0){
            Assert.faild("商品不能重复添加");
        }


        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateChannelsSpu(AddChannlesSpuRequest addChannlesSpuRequest) {
        //String username = AuthUserContext.get().getUsername();

        ChannelsSpu dbShannelsSpu = channelsSpuMapper.selectById(addChannlesSpuRequest.getId());
        if(dbShannelsSpu == null){
            Assert.faild("商品数据不存在，请检查数据后再次操作。");
        }

        /*if (dbShannelsSpu.getStatus() == 5){
            Assert.faild("商品上架中，不能修改，请先下架商品");
        }*/

        addChannlesSpuRequest.setOutSpuId(dbShannelsSpu.getOutSpuId());


    }



    @Override
    public void auditCancel(Long id) {
        ChannelsSpu dbShannelsSpu = channelsSpuMapper.selectById(id);
        if(dbShannelsSpu==null){
            Assert.faild("商品数据不存在，请检查数据后再次操作。");
        }
        EcBaseResponse ecBaseResponse = ecProductService.auditCancel(StrUtil.toString(dbShannelsSpu.getOutSpuId()));
        if(ecBaseResponse.getErrcode()!=0){
            Assert.faild(StrUtil.format("撤回商品审核失败，失败原因:{}",ecBaseResponse.getErrmsg()));
        }
        dbShannelsSpu.setEditStatus(0);
        dbShannelsSpu.setStatus(0);
        dbShannelsSpu.setUpdateBy(AuthUserContext.get().getUsername());
        channelsSpuMapper.updateById(dbShannelsSpu);
    }

    @Override
    public void listing(List<Long> ids) {
        for (Long id : ids) {
            ChannelsSpu dbShannelsSpu = channelsSpuMapper.selectById(id);
            if(dbShannelsSpu == null){
                Assert.faild("商品数据不存在，请检查数据后再次操作。");
            }
            // 这里通过上面是否通过过审核来判断是否执行商品更新操作，防止微信视频号与内部视频号商品信息不一致
            if (Objects.nonNull(dbShannelsSpu.getSuccessTime())) {
                // 上架过的商品，做进行商品更新，并在商品更新时进行商品上架操作
                List<ChannelsSku> channelsSkus = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class).eq(ChannelsSku::getChannelsSpuId, dbShannelsSpu.getId()));
                AddChannlesSpuRequest addChannlesSpuRequest = mapperFacade.map(dbShannelsSpu, AddChannlesSpuRequest.class);
                List<ChannelsSkuDTO> skuDTOList = new ArrayList<>();
                for (ChannelsSku sku : channelsSkus) {
                    ChannelsSkuDTO dto = mapperFacade.map(sku, ChannelsSkuDTO.class);
                    skuDTOList.add(dto);
                }
                addChannlesSpuRequest.setSkus(skuDTOList);
                addChannlesSpuRequest.setIsQuestListing(Boolean.TRUE);
                updateChannelsSpu(addChannlesSpuRequest);
            } else {
                // 首次上架操作直接调用商品上架
                 ecProductService.listing(StrUtil.toString(dbShannelsSpu.getOutSpuId()));
            }
            dbShannelsSpu.setUpdateBy(AuthUserContext.get().getUsername());
            dbShannelsSpu.setEditStatus(2);
            dbShannelsSpu.setStatus(2);
            dbShannelsSpu.setVerifyTime(new Date());
            channelsSpuMapper.updateById(dbShannelsSpu);

            // 同步视频号skuId
            List<ChannelsSku> channelsSkus1 = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class)
                    .eq(ChannelsSku::getChannelsSpuId, id));
            synchronizeChannelsSku(id, channelsSkus1);
        }

    }

    @Override
    public void delisting(List<Long> ids) {
        for (Long id : ids) {
            ChannelsSpu dbShannelsSpu = channelsSpuMapper.selectById(id);
            delisting(dbShannelsSpu);
        }
    }

    private void delisting(ChannelsSpu dbShannelsSpu) {
        if(dbShannelsSpu == null){
            Assert.faild("商品数据不存在，请检查数据后再次操作。");
        }
        List<ChannelsSku> channelsSkuList = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getChannelsSpuId, dbShannelsSpu.getId()));
        if (Objects.isNull(channelsSkuList) || channelsSkuList.isEmpty()){
            Assert.faild("商品数据不存在，请检查数据后再次操作");
        }
        ecProductService.delisting(StrUtil.toString(dbShannelsSpu.getOutSpuId()));

        dbShannelsSpu.setUpdateBy(AuthUserContext.get().getUsername());
        dbShannelsSpu.setStatus(11);
        channelsSpuMapper.updateById(dbShannelsSpu);

        // 设置channelsSku所有库存为0
        deListingZeroSetStock(dbShannelsSpu, channelsSkuList);

        // 橱窗下架
        if (dbShannelsSpu.getInWinodws() == 1) {
            channelsWindowService.offProduct(dbShannelsSpu.getSpuId());
        }

        //优选联盟商品同步下架
        leagueItemService.besedelisting(StrUtil.toString(dbShannelsSpu.getOutSpuId()),dbShannelsSpu.getSpuCode(),"自主下架");
    }

    private void deListingZeroSetStock(ChannelsSpu dbShannelsSpu, List<ChannelsSku> channelsSkuList) {
        channelsSkuService.update(Wrappers.lambdaUpdate(ChannelsSku.class)
                .set(ChannelsSku::getStockNum, 0)
                .eq(ChannelsSku::getChannelsSpuId, dbShannelsSpu.getId()));

        // 释放sku_stock
    }

    @Override
    public Integer getStock(Long id, Long skuId) {
        ChannelsSpu spu = channelsSpuMapper.getById(id);
        if (Objects.isNull(spu)) {
            Assert.faild("未找到商品");
        }
        ChannelsSku sku = channelsSkuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getChannelsSpuId, id)
                .eq(ChannelsSku::getId, skuId));
        if (Objects.isNull(sku)) {
            Assert.faild("未找到商品库存单位信息");
        }
        Long outSpuId = spu.getOutSpuId();
        Long outSkuId = sku.getOutSkuId();

        EcGetStockResponse response = ecProductService.getStock(outSpuId, outSkuId);
        if (response == null) {
            Assert.faild("调用视频号4.0获取实时库存API失败");
        } else if (response.getErrcode() != 0){
            Assert.faild(response.getErrmsg());
        }

        return response.getData().getNormal_stock_num();
    }

    @Override
    public List<String> listProduct() {
        EcProductListResponse list = ecProductService.List();
        List<String> productIds = list.getProduct_ids();
        return productIds;
    }

    @Override
    public EcProductResponse getEcProductById(Long id, Integer type) {
        ChannelsSpu channelsSpu = channelsSpuMapper.selectById(id);
        if(channelsSpu == null){
            Assert.faild("商品数据不存在，请检查数据后再次操作。");
        }

        return ecProductService.get(channelsSpu.getOutSpuId(), type);
    }

    @Override
    public void updateStock(Long id, Long channelsSkuId, Integer stock, Integer type) {
        ChannelsSpu spu = channelsSpuMapper.getById(id);
        if (Objects.isNull(spu)) {
            Assert.faild("未找到商品");
        }
        ChannelsSku sku = channelsSkuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getChannelsSpuId, id)
                .eq(ChannelsSku::getId, channelsSkuId));
        if (Objects.isNull(sku)) {
            Assert.faild("未找到商品库存单位信息");
        }

        Long outSpuId = spu.getOutSpuId();
        Long outSkuId = sku.getOutSkuId();

        EcUpdateStockResponse response = ecProductService.updateStock(outSpuId, outSkuId, stock, type);
        // 这里需要防止MQ推送时，无鉴权用户信息
        String userName = "MQ库存推送";
        UserInfoInTokenBO tokenBO = AuthUserContext.get();
        if (Objects.nonNull(tokenBO)){
            userName = tokenBO.getUsername();
        }
        spu.setUpdateBy(userName);
        channelsSpuMapper.updateById(spu);
        // 修改类型为设置且库存配置为0
        if (type == 3 && stock.equals(0)){
            channelsSkuMapper.setZeroStockBySkuId(channelsSkuId);
        // 修改类型为设置且库存配置不为0， 先将库存设置为0 再加上配置的值
        } else if (type == 3 && !stock.equals(0)) {
            channelsSkuMapper.setZeroStockBySkuId(channelsSkuId);
            channelsSkuMapper.updateStockBySkuId(channelsSkuId, stock);
        } else {
            // 如果类型为减少， 则取反
            if (type == 2){
                stock = Math.negateExact(stock);
            }
            channelsSkuMapper.updateStockBySkuId(channelsSkuId, stock);
        } 

        // 库存同步

        // 保存一条日志记录
        channelsSpuUpdateLogService.saveLog(spu, sku.getSkuId(), stock, type);
    }

    @Override
    public void auditNotify(ProductSpuAuditDTO productSpuAuditDTO) {
        this.update(Wrappers.lambdaUpdate(ChannelsSpu.class)
                .eq(ChannelsSpu::getOutSpuId, productSpuAuditDTO.getProductId())
                // 回调返回 2:审核不通过；3:审核通过。
                .set(ChannelsSpu::getEditStatus, productSpuAuditDTO.getStatus() == 3? 4 : 3)
                .set(ChannelsSpu::getAuditReason, productSpuAuditDTO.getReason())
                .set(productSpuAuditDTO.getStatus() == 3, ChannelsSpu::getSuccessTime, new Date())
                .set(productSpuAuditDTO.getStatus() == 2, ChannelsSpu::getFailTime, new Date()));

        // 如果审核通过且上架状态=上架审核中 则设置商品状态设置为上架
        if (productSpuAuditDTO.getStatus() == 3) {
            this.update(Wrappers.lambdaUpdate(ChannelsSpu.class)
                    .eq(ChannelsSpu::getOutSpuId, productSpuAuditDTO.getProductId())
                    .eq(ChannelsSpu::getStatus, 2)
                    .set(ChannelsSpu::getStatus, 5));

        }

        // 如果审核不通过且上架状态=上架审核中 则设置商品状态设置为初始值
        if (productSpuAuditDTO.getStatus() == 2) {
            this.update(Wrappers.lambdaUpdate(ChannelsSpu.class)
                    .eq(ChannelsSpu::getOutSpuId, productSpuAuditDTO.getProductId())
                    .eq(ChannelsSpu::getStatus, 2)
                    .set(ChannelsSpu::getStatus, 0));
        }
    }

    @Override
    public void listingNotify(ProductSpuAuditDTO productSpuAuditDTO) {
        this.update(Wrappers.lambdaUpdate(ChannelsSpu.class)
                .eq(ChannelsSpu::getOutSpuId, productSpuAuditDTO.getProductId())
                .set(ChannelsSpu::getStatus, productSpuAuditDTO.getStatus())
                .set(StrUtil.isNotBlank(productSpuAuditDTO.getReason()), ChannelsSpu::getAuditReason, productSpuAuditDTO.getReason()));
        // 上架状态不等于5则表示回调下架，清空库存
        if (productSpuAuditDTO.getStatus() != 5) {
            ChannelsSpu channelsSpu = channelsSpuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSpu.class)
                    .eq(ChannelsSpu::getOutSpuId, productSpuAuditDTO.getProductId())
                    .last("LIMIT 1"));

            List<ChannelsSku> channelsSkuList = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class)
                    .eq(ChannelsSku::getChannelsSpuId, channelsSpu.getId()));

            deListingZeroSetStock(channelsSpu, channelsSkuList);

            // 橱窗同步下架
            channelsWindowService.offProduct(channelsSpu.getSpuId());

            //优选联盟商品同步下架
            leagueItemService.besedelisting(productSpuAuditDTO.getProductId(),channelsSpu.getSpuCode(),productSpuAuditDTO.getReason());
        }
    }

    @Override
    public void delisting(Long spuId) {
        ChannelsSpu channelsSpu = channelsSpuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSpu.class).eq(ChannelsSpu::getSpuId, spuId));
        delisting(channelsSpu);
    }

    @Override
    public void export(ChannelsSpuQueryDTO dto, HttpServletResponse response) {
        Wrapper<ChannelsSpu> queryWrapper = getQueryWrapper(dto);
        List<ChannelsSpu> channelsSpuList = channelsSpuMapper.selectList(queryWrapper);
        List<Long> channelsSpuIds = channelsSpuList.stream().map(ChannelsSpu::getId).collect(Collectors.toList());
        Integer channelsSkuCount = channelsSkuMapper.selectCount(Wrappers.lambdaQuery(ChannelsSku.class)
                .in(ChannelsSku::getChannelsSpuId, channelsSpuIds));
        // 创建文件
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = SystemUtils.getExcelFilePath() + "/" + time + "视频号商品数据" + ".xlsx";
        // 创建下载中心记录
        Long downLoadHisId = getDownLoadHisId(time, "视频号商品信息");
        // excel写入
        int page = 1;
        if (channelsSkuCount > 0){
            page += channelsSkuCount / MAX_EXCEL_PAGE_SIZE;
        }
        ExcelWriter excelWriter = ExcelUtil.getExcelWriter(fileName);
        WriteSheet writeSheet = ExcelUtil.getWriteSheet("sheet", ChannelsSpuSkuExcelVO.class);
        for (int i = 0; i < page; i++) {
            String lastSql = "LIMIT " + i * MAX_EXCEL_PAGE_SIZE + "," + (i + 1) * MAX_EXCEL_PAGE_SIZE;
            List<ChannelsSpuSkuExcelVO> channelsSkuList = channelsSkuMapper.selectChannelsSpuSkuExcelVO(channelsSpuIds, lastSql);
            ExcelUtil.write(channelsSkuList, excelWriter, writeSheet, false);
        }
        ExcelUtil.write(null, excelWriter, writeSheet, true);
        uplodaFileAndFinishDown(fileName, time, downLoadHisId, channelsSkuCount, "视频号商品信息");
        FileUtil.del(fileName);
    }

    private void uplodaFileAndFinishDown(String fileName, String time, Long downLoadHisId, Integer channelsSkuCount, String downFileName) {
        // 上传文件
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MultipartFileDto(fileName, fileName,
                    ContentType.APPLICATION_OCTET_STREAM.toString(), Files.newInputStream(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String mimoPath = "excel/" + time + "/" + originalFilename;
        ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
        if (responseEntity.isSuccess()) {
            FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
            finishDownLoadDTO.setId(downLoadHisId);
            finishDownLoadDTO.setCalCount(channelsSkuCount);
            finishDownLoadDTO.setFileName(time + downFileName);
            finishDownLoadDTO.setStatus(1);
            finishDownLoadDTO.setFileUrl(responseEntity.getData());
            finishDownLoadDTO.setRemarks("导出成功");
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
        }
    }

    @Override
    public List<ChannelsSpuSkuVO> listChannelsSpuSkuVO(List<Long> outSpuIds) {
        List<ChannelsSpuSkuVO> channelsSpuSkuVOList = new ArrayList<>();
        for (Long id : outSpuIds) {
            ChannelsSpuSkuVO channelsSpuSkuVO = new ChannelsSpuSkuVO();
            // 组装spu信息
            ChannelsSpu channelsSpu = channelsSpuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSpu.class)
                    .eq(ChannelsSpu::getOutSpuId, id)
                    .last("LIMIT 1"));
            if (channelsSpu == null){
                continue;
            }
            ChannelsSpuVO channelsSpuVO = mapperFacade.map(channelsSpu, ChannelsSpuVO.class);
            channelsSpuSkuVO.setChannelsSpuVO(channelsSpuVO);

            // 组装sku信息
            List<ChannelsSku> channelsSkus = channelsSkuMapper.selectList(Wrappers.lambdaQuery(ChannelsSku.class)
                    .eq(ChannelsSku::getChannelsSpuId, channelsSpu.getId()));
            if (channelsSkus == null || channelsSkus.isEmpty()){
                continue;
            }

            channelsSpuSkuVOList.add(channelsSpuSkuVO);
        }

        return channelsSpuSkuVOList;
    }

    @Override
    public ChannelsSpuSkuVO getChannelsSpuSkuVO(Long outSpuId) {
        List<ChannelsSpuSkuVO> channelsSpuSkuVOS = listChannelsSpuSkuVO(Collections.singletonList(outSpuId));
        if (channelsSpuSkuVOS != null && !channelsSpuSkuVOS.isEmpty()){
            return channelsSpuSkuVOS.get(0);
        }
        return null;
    }

    @Override
    public void checkPrice(List<PriceCheckDTO> priceCheckDTO) {


    }

    @Override
    public void saveAndListing(AddChannlesSpuRequest addChannlesSpuRequest) {
        Boolean isAdd = addChannlesSpuRequest.getIsAdd();
        if (Objects.isNull(isAdd)){
            Assert.faild("参数错误");
        }
        if (isAdd) {
            Long channelsSpuId = addChannelsSpu(addChannlesSpuRequest);
            listing(Collections.singletonList(channelsSpuId));
        } else {
            if (Objects.isNull(addChannlesSpuRequest.getSpuId())){
                Assert.faild("参数错误");
            }
            updateChannelsSpu(addChannlesSpuRequest);
            listing(Collections.singletonList(addChannlesSpuRequest.getSpuId()));
        }
    }

    @Override
    public ChannelsSpuProductSpuVO getChannelsSpuProductSpuVOBySpuId(Long spuId) {

        ChannelsSpu channelsSpu = channelsSpuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSpu.class)
                .eq(ChannelsSpu::getSpuId, spuId).last("LIMIT 1"));
        if (Objects.isNull(channelsSpu)){
            Assert.faild("未找到视频号商品数据");
        }
        ChannelsSpuProductSpuVO vo = new ChannelsSpuProductSpuVO();
        vo.setChannelsSpu(channelsSpu);
        return vo;
    }

    @Override
    public PageVO<ChannelsSpuExtraVO> extraPage(ChannelsSpuQueryDTO dto) {
        PageVO<ChannelsSpuExtraVO> page = new PageVO<>();
        Wrapper<ChannelsSpu> queryWrapper = getQueryWrapper(dto);
        PageVO<ChannelsSpu> pageVO = PageUtil.doPage(dto, () -> channelsSpuMapper.selectList(queryWrapper));

        page.setTotal(pageVO.getTotal());
        page.setPages(pageVO.getPages());
        if (1 > pageVO.getTotal()){
            return page;
        }


        return page;
    }

    @Override
    public void checkPriceAll(ChannelsSpuQueryDTO dto) {
        Wrapper<ChannelsSpu> queryWrapper = getQueryWrapper(dto);
        List<ChannelsSpu> channelsSpuList = channelsSpuMapper.selectList(queryWrapper);
        Map<Long, ChannelsSpu> spuMap = channelsSpuList.stream().collect(Collectors.toMap(ChannelsSpu::getId, Function.identity()));
        List<Long> channelsSpuIds = channelsSpuList.stream().map(ChannelsSpu::getId).collect(Collectors.toList());
        Integer channelsSkuCount = channelsSkuMapper.selectCount(Wrappers.lambdaQuery(ChannelsSku.class)
                .in(ChannelsSku::getChannelsSpuId, channelsSpuIds));

        // 创建文件
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = SystemUtils.getExcelFilePath() + "/" + time + "视频号商品价格检测" + ".xlsx";
        // 创建下载中心记录
        Long downLoadHisId = getDownLoadHisId(time, "视频号商品价格检测");
        // excel写入
        int page = 1;
        if (channelsSkuCount > 0){
            page += channelsSkuCount / MAX_EXCEL_PAGE_SIZE;
        }
        ExcelWriter excelWriter = ExcelUtil.getExcelWriter(fileName);
        WriteSheet writeSheet = ExcelUtil.getWriteSheet("sheet", ChannelsCheckPriceExcelVO.class);
    }

    private Long getDownLoadHisId(String time, String fileName) {
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(time + fileName);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("1");
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }
        return downLoadHisId;
    }

}
