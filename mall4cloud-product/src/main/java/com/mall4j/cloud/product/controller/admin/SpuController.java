package com.mall4j.cloud.product.controller.admin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.biz.feign.ChannelsSpuFeignClient;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.constant.ShopStatus;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.product.vo.SkuPriceFeeExcelLogVO;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.SpuAttrValueVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.vo.UploadExcelVO;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.dto.SpuCategoryUpdateDTO;
import com.mall4j.cloud.product.dto.SpuIphStatusDTO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.event.SimpleSoldSpuEvent;
import com.mall4j.cloud.product.event.SoldSpuEvent;
import com.mall4j.cloud.product.event.UpdateSkusPricefeeEvent;
import com.mall4j.cloud.product.listener.SpuCodeReadExcelListener;
import com.mall4j.cloud.product.listener.SpuExcelListener;
import com.mall4j.cloud.product.manager.UpdateSkuPirceStatusManager;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.model.SpuDetail;
import com.mall4j.cloud.product.model.SpuExtension;
import com.mall4j.cloud.product.model.SpuSkuPriceLog;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Slf4j
@RestController("adminSpuController")
@RequestMapping("/mp/spu")
@Api(tags = "admin-spu信息")
public class SpuController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuDetailService spuDetailService;
    @Autowired
    private SpuExcelService spuExcelService;
    @Autowired
    private SpuReadExcelService spuReadExcelService;
    @Autowired
    private SkuExcelService skuExcelService;
    @Autowired
    private UpdateSkuPirceStatusManager updateSkuPirceStatusManager;
    @Autowired
    private SkuService skuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private SpuAttrValueService spuAttrValueService;
    @Autowired
    private OnsMQTemplate soldUploadExcelTemplate;
    @Autowired
    private ExcelUploadFeignClient excelUploadFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private SpuChannelPriceService spuChannelPriceService;
    @Autowired
    private SpuSkuPriceLogService spuSkuPriceLogService;
//    @Autowired
//    private CategoryService categoryService;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private ChannelsSpuFeignClient channelsSpuFeignClient;

    @GetMapping
    @ApiOperation(value = "获取spu信息", notes = "根据spuId获取spu信息")
    public ServerResponseEntity<SpuVO> getBySpuId(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId, @RequestParam Long spuId) {
        // 获取spu信息
        SpuVO spuVO = spuService.getBySpuIdAndStoreId(spuId,storeId);
        if (!Objects.equals(spuVO.getShopId(), AuthUserContext.get().getTenantId()) && !Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        List<SpuAttrValueVO> spuAttrsBySpuId = spuAttrValueService.getSpuAttrsBySpuId(spuId);
        spuVO.setSpuAttrValues(spuAttrsBySpuId);
        SpuExtension spuExtension = spuService.getSpuExtension(spuId);
        spuVO.setTotalStock(spuExtension.getActualStock());
        spuVO.setSaleNum(spuExtension.getSaleNum());
        // 物流模板
        if (StrUtil.isNotBlank(spuVO.getDeliveryMode())) {
            SpuVO.DeliveryModeVO deliveryModeVO = Json.parseObject(spuVO.getDeliveryMode(), SpuVO.DeliveryModeVO.class);
            spuVO.setDeliveryModeVO(deliveryModeVO);
        }
        // sku信息
        ServerResponseEntity<Boolean> responseEntity=storeFeignClient.isInviteStore(storeId);
        if(responseEntity.isSuccess() && responseEntity.getData()){
            storeId=1L;//虚拟门店用官店数据
        }
        // 组装Skc列表给前端
        spuVO.setSkus(skuService.listSkuAllInfoBySpuId(spuId, false,storeId));
        if(responseEntity.isSuccess() && responseEntity.getData()){
            spuVO.getSkus().forEach(item ->{
                item.setPriceFee(item.getMarketPriceFee());
            });
        }
        loadSpuAttrs(spuVO);
        // 平台分类、店铺分类信息
        spuVO.setCategory(categoryService.getInfo(spuVO.getCategoryId()));

        if (Objects.nonNull(spuVO.getShopCategoryId()) && !Objects.equals(spuVO.getShopCategoryId(), Constant.DEFAULT_ID)) {
            spuVO.setShopCategory(categoryService.getInfo(spuVO.getShopCategoryId()));
        }
        return ServerResponseEntity.success(spuVO);
    }

    @PostMapping
    @ApiOperation(value = "保存spu信息", notes = "保存spu信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SpuDTO spuDTO) {
        spuDTO.setStatus(StatusEnum.ENABLE.value());
        spuDTO.setSpuType(SpuType.NORMAL.value());
        spuService.save(spuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新spu信息", notes = "更新spu信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SpuDTO spuDTO) {
//        SpuVO spuDb = spuService.getBySpuId(spuDTO.getSpuId());
//        if (!Objects.equals(spuDb.getShopId(), AuthUserContext.get().getTenantId())) {
//            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
//        }
        spuService.updateSpu(spuDTO);
        return ServerResponseEntity.success();
    }


    @DeleteMapping
    @ApiOperation(value = "删除spu信息", notes = "根据spu信息id删除spu信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long spuId) {
        SpuVO spuDb = spuService.getBySpuId(spuId);
        Long shopId = AuthUserContext.get().getTenantId();
        if (!Objects.equals(spuDb.getShopId(), shopId) && !Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        spuService.deleteById(spuId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_spu_data")
    @ApiOperation(value = "修改spu（名称、价格、库存、序号）信息", notes = "更新spu信息")
    public ServerResponseEntity<Void> updateSpuData(@RequestBody SpuDTO spuDTO) {
        SpuVO spuDb = spuService.getBySpuId(spuDTO.getSpuId());
        if (!Objects.equals(spuDb.getShopId(), AuthUserContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        spuService.updateSpuOrSku(spuDTO);
        return ServerResponseEntity.success();
    }


    /**
     * 更新商品状态
     */
    @PutMapping("/prod_status")
    @ApiOperation(value = "商品上下架", notes = "商品上下架")
    public ServerResponseEntity<Void> spuChangeStatus(@RequestBody SpuPageSearchDTO spuDTO) {
        if (Objects.nonNull(spuDTO.getSpuId())) {
            spuDTO.setSpuIds(Collections.singletonList(spuDTO.getSpuId()));
        }
        if (CollUtil.isEmpty(spuDTO.getSpuIds())) {
            throw new LuckException("商品id不能为空");
        }
        Long shopId = AuthUserContext.get().getTenantId();
        List<SpuVO> spuList = spuService.listSpuBySpuIds(spuDTO.getSpuIds());

        if (Objects.equals(spuDTO.getStatus(), StatusEnum.ENABLE.value())) {
            // 上架商品时，需要根据店铺状态判断是否可以上架商品
            ServerResponseEntity<EsShopDetailBO> shopRequest = shopDetailFeignClient.getShopByShopId(AuthUserContext.get().getTenantId());
            EsShopDetailBO shopDetail = shopRequest.getData();
            if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE.value())) {
                throw new LuckException("店铺处于违规下线状态，不能上架商品");
            }
        }
        for (SpuVO spuVO : spuList) {
            if (Objects.equals(spuDTO.getStatus(), StatusEnum.ENABLE.value())) {
                CategoryVO category = categoryService.getInfo(spuVO.getCategoryId());
                if (category == null || Objects.equals(category.getStatus(), StatusEnum.DISABLE.value())) {
                    // 平台分类处于下线中，商品不能上架，请联系管理员后再进行操作
                    throw new LuckException("商品【" + spuVO.getName() + "】的平台分类处于下线中，商品不能上架，请联系管理员后再进行操作");
                }
                if (!Objects.equals(spuVO.getSpuType(), SpuType.SCORE.value())) {
                    CategoryVO shopCategory = categoryService.getById(spuVO.getShopCategoryId());
                    if (shopCategory == null || Objects.equals(shopCategory.getStatus(), StatusEnum.DISABLE.value())) {
                        // 本店分类处于下线中，商品不能上架
                        throw new LuckException("商品【" + spuVO.getName() + "】的本店分类处于下线中，商品不能上架");
                    }
                }
            }
            if (!Objects.equals(spuVO.getShopId(), shopId) && !Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
                return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
            }
        }
        spuBatchUpdateStatus(spuDTO);
        for (SpuVO spuVO : spuList) {
            spuService.removeSpuCacheBySpuId(spuVO.getSpuId());
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/sold_excel")
    @ApiOperation(value = "导出excel", notes = "导出商品excel")
    public ServerResponseEntity<String> spuSoldExcel(HttpServletResponse response, SpuPageSearchDTO spuDTO) {

        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(SoldSpuExcelVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            SoldSpuEvent soldSpuEvent=new SoldSpuEvent();
            soldSpuEvent.setSpuDTO(spuDTO);
            soldSpuEvent.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(soldSpuEvent);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出商品信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @GetMapping("/simple/sold_excel")
    @ApiOperation(value = "导出全量商品excel", notes = "导出全量商品excel")
    public ServerResponseEntity<String> spuSimpleSoldExcel() {

        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(SimpleSpuExcelVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            SimpleSoldSpuEvent simpleSoldSpuEvent=new SimpleSoldSpuEvent();
            simpleSoldSpuEvent.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(simpleSoldSpuEvent);

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出商品信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @GetMapping("/down_model")
    @ApiOperation(value = "导出商品excel模板", notes = "导出商品excel模板")
    public ServerResponseEntity<Void> downloadModel(HttpServletResponse response) {
        spuExcelService.downloadModel(response);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "导入文件")
    @PostMapping("/export_excel")
    public ServerResponseEntity<String> exportExcel(@RequestParam("excelFile") MultipartFile file) {
        if (file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        String info = null;
        try {
            Map<Integer, List<String>> errorMap = new HashMap<>(8);
            EasyExcel.read(file.getInputStream(), SpuExcelVO.class, new SpuExcelListener(spuExcelService, errorMap)).sheet().doRead();
            info = spuExcelService.spuExportError(errorMap);
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
        return ServerResponseEntity.success(info);
    }

    /**
     * spu批量上下架
     *
     * @param spuDTO
     */
    private void spuBatchUpdateStatus(SpuPageSearchDTO spuDTO) {
        List<Long> spuIds = spuDTO.getSpuIds();
        Integer status = spuDTO.getStatus();
        spuDTO.setStatus(null);
        List<SpuVO> spuList = spuService.list(spuDTO);
        if (CollUtil.isEmpty(spuList)) {
            throw new LuckException("获取不到该商品信息，请刷新后重试");
        }
        checkUpdateStatusData(spuList, status);
        if (CollUtil.isEmpty(spuIds)) {
            throw new LuckException("您所选择的商品中没有符合操作条件的商品");
        }
        spuService.batchChangeSpuStatus(spuIds, status);

        //视频号商品同步下架
        if (Objects.equals(status, StatusEnum.DISABLE.value())) {
            channelsSpuFeignClient.deListingProduct(spuIds);
        }
    }

    /**
     * 加载spu属性列表
     *
     * @param spuVO
     */
    private void loadSpuAttrs(SpuVO spuVO) {
        List<SpuAttrValueVO> spuAttrValues = spuAttrValueService.getSpuAttrsBySpuId(spuVO.getSpuId());
        spuVO.setSpuAttrValues(spuAttrValues);
    }

    /**
     * 校验spu上下架信息
     *
     * @param list   商品列表
     * @param status 更新的状态
     * @return
     */
    private void checkUpdateStatusData(List<SpuVO> list, Integer status) {
        ProductLangUtil.spuList(list);
        Long shopId = AuthUserContext.get().getTenantId();
        ServerResponseEntity<EsShopDetailBO> shopRequest = shopDetailFeignClient.getShopByShopId(shopId);
        EsShopDetailBO shopDetail = shopRequest.getData();
        if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE.value()) || Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE_AWAIT_AUDIT.value())) {
            throw new LuckException("店铺处于违规下线中，不能修改商品");
        }
        for (SpuVO spu : list) {
            if (!Objects.equals(spu.getShopId(), shopId) && !Objects.equals(AuthUserContext.get().getTenantId(), Constant.PLATFORM_SHOP_ID)) {
                throw new LuckException(ResponseEnum.UNAUTHORIZED);
            }
            // 商品状态不为启用或未启用状态
            if (!Objects.equals(spu.getStatus(), StatusEnum.ENABLE.value())
                    && !Objects.equals(spu.getStatus(), StatusEnum.DISABLE.value())) {
                throw new LuckException("商品(" + spu.getName() + ")不能进行上下架操作");
            }
            if (Objects.equals(status, StatusEnum.ENABLE.value())) {
                CategoryVO category = categoryService.getById(spu.getCategoryId());
                if (!Objects.equals(category.getStatus(), StatusEnum.ENABLE.value())) {
                    throw new LuckException("商品(" + spu.getName() + ")所属的平台分类处于下线中，商品不能进行上架操作，请联系管理员后再进行操作");
                }
//                if (!Objects.equals(spu.getSpuType(), SpuType.SCORE.value())) {
//                    CategoryVO shopCategory = categoryService.getById(spu.getShopCategoryId());
//                    if (!Objects.equals(shopCategory.getStatus(), StatusEnum.ENABLE.value())) {
//                        throw new LuckException("商品(" + spu.getName() + ")所属的店铺分类处于下线中，商品不能进行上架操作");
//                    }
//                    CategoryShopVO categoryShopVO = categoryShopService.getByShopIdAndCategoryId(spu.getShopId(), spu.getCategoryId());
//                    if (Objects.isNull(categoryShopVO)) {
//                        throw new LuckException("商品(" + spu.getName() + ")所属的平台分类与店铺的签约关系已失效，商品不能进行上架操作");
//                    }
//                }
            }
        }
    }

    @PostMapping("/offline")
    @ApiOperation(value = "下线商品", notes = "下线商品")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        SpuVO spuVO = spuService.getBySpuId(offlineHandleEventDto.getHandleId());
        if (Objects.isNull(spuVO)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        spuService.offline(offlineHandleEventDto);
        spuService.removeSpuCacheBySpuId(offlineHandleEventDto.getHandleId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event/{spuId}")
    @ApiOperation(value = "获取最新下线的事件", notes = "获取最新下线的事件")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@PathVariable Long spuId) {
        SpuVO spuDb = spuService.getBySpuId(spuId);
        if (!Objects.equals(spuDb.getShopId(), AuthUserContext.get().getTenantId()) && !Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        return ServerResponseEntity.success(spuService.getOfflineHandleEvent(spuId));
    }

//    @GetMapping("/page_can_seckill_prod")
//    @ApiOperation(value = "获取可以参与秒杀活动的商品分页列表", notes = "可以参与秒杀活动的商品分页列表")
//    public ServerResponseEntity<PageVO<SpuVO>> pageCanSeckillProd(@Valid PageDTO pageDTO,SpuDTO spuDTO) {
//        spuDTO.setShopId(AuthUserContext.get().getTenantId());
//        return ServerResponseEntity.success(spuService.pageCanSeckillProd(pageDTO, spuDTO));
//    }

    @PostMapping("/audit")
    @ApiOperation(value = "审核商品", notes = "审核商品")
    public ServerResponseEntity<Void> audit(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        spuService.audit(offlineHandleEventDto);
        spuService.removeSpuCacheBySpuId(offlineHandleEventDto.getHandleId());
        return ServerResponseEntity.success();
    }

    @PostMapping("/audit_apply")
    @ApiOperation(value = "违规活动提交审核", notes = "违规活动提交审核")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        SpuVO spuDb = spuService.getBySpuId(offlineHandleEventDto.getHandleId());
        if (!Objects.equals(spuDb.getShopId(), AuthUserContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        spuService.auditApply(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }

    @PutMapping("/shop/category")
    @ApiOperation(value = "批量修改商品导航", notes = "批量修改商品导航")
    public ServerResponseEntity<Boolean> shopCategory(@RequestBody SpuCategoryUpdateDTO shopCategoryUpdateDTO) {
        Long categoryId = shopCategoryUpdateDTO.getCategoryId();
        CategoryVO categoryVO = categoryService.getById(categoryId);
        if (categoryVO.getShopId() != 1L) {
            throw new LuckException("非导航分类导航，请重新分配");
        }
//        boolean update = spuService.lambdaUpdate().set(Spu::getShopCategoryId, categoryId).in(Spu::getSpuId, shopCategoryUpdateDTO.getSpuIdList()).update();
        boolean update=spuService.updateShopCategorys(shopCategoryUpdateDTO);
        return ServerResponseEntity.success(update);
    }

    @PutMapping("/shop/updateIphStatus")
    @ApiOperation(value = "批量修改商品铺货状态", notes = "批量修改商品铺货状态")
    public ServerResponseEntity<Boolean> updateIphStatus(@RequestBody List<SpuIphStatusDTO> spuIphStatusDTOS) {
        spuService.updateIphStatus(spuIphStatusDTOS);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/shop/category")
    @ApiOperation(value = "批量移除商品导航", notes = "批量修改商品导航")
    public ServerResponseEntity<Boolean> shopCategory(@RequestBody List<Long> spuIdList) {
        if (CollectionUtil.isEmpty(spuIdList)) {
            throw new LuckException("请勾选商品数据");
        }
        boolean update = spuService.lambdaUpdate()
                .set(Spu::getShopCategoryId, null)
                .set(Spu::getPrimaryCategoryId, null)
                .set(Spu::getSecondaryCategoryId, null)
                .in(Spu::getSpuId, spuIdList).update();
        return ServerResponseEntity.success(update);
    }

    @PutMapping("/main/category")
    @ApiOperation(value = "批量修改商品分类", notes = "批量修改商品分类")
    public ServerResponseEntity<Boolean> mainCategory(@RequestBody SpuCategoryUpdateDTO shopCategoryUpdateDTO) {
        Long categoryId = shopCategoryUpdateDTO.getCategoryId();
        CategoryVO categoryVO = categoryService.getById(categoryId);
        if (categoryVO.getShopId() != 0L) {
            throw new LuckException("非商品分类导航，请重新分配");
        }
        boolean update = spuService.update(new LambdaUpdateWrapper<Spu>().set(Spu::getCategoryId, categoryId)
                .set(Spu::getPrimaryCategoryId, categoryVO.getPrimaryCategoryId())
                .set(Spu::getSecondaryCategoryId, categoryVO.getSecondaryCategoryId())
                .in(Spu::getSpuId, shopCategoryUpdateDTO.getSpuIdList()));
        return ServerResponseEntity.success(update);
    }

    @GetMapping("/spu/library")
    @ApiOperation(value = "查询当前门店未添加的总店商品", notes = "商品库")
    public ServerResponseEntity<PageVO<Spu>> spuLibrary(@RequestParam(value = "storeId", required = false) Long storeId, SpuPageSearchDTO searchDTO) {
        if (storeId == Constant.PLATFORM_SHOP_ID) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        PageVO<Spu> page = spuService.pageLibrary(storeId, searchDTO);
//        查询当前门店所有已添加的商品信息
//        查询总店商品过滤当前门店所有商品信息进行分页展示

        return ServerResponseEntity.success(page);
    }

    @PostMapping("/spu/page")
    @ApiOperation(value = "平台端分页接口", notes = "默认查询官店信息")
    public ServerResponseEntity<PageVO<SpuPageVO>> spuPage(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId, @RequestBody SpuPageSearchDTO searchDTO) {
        if(Objects.isNull(storeId)){
            storeId=1L;
        }
        log.info("/spu/page storeid:{}",storeId);
        PageVO<SpuPageVO> page = spuService.spuPage(storeId, searchDTO);

        List<Long> spuIdList = page.getList().stream().map(SpuPageVO::getSpuId).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(spuIdList)){
            List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, storeId);
            if(CollectionUtil.isNotEmpty(appSkuPriceBySkuIdList)){
                Map<Long, List<SkuPriceDTO>> skuMap = appSkuPriceBySkuIdList.stream().collect(Collectors.groupingBy(SkuPriceDTO::getSpuId));
                page.getList().forEach(item ->{
                    List<SkuPriceDTO> skuPriceDTOS=skuMap.get(item.getSpuId());
                    if(CollectionUtil.isNotEmpty(skuPriceDTOS)){
                        SkuPriceDTO skuAppVO1 = skuPriceDTOS.stream().min(Comparator.comparing(SkuPriceDTO::getPriceFee)).get();
                        item.setPriceFee(skuAppVO1.getPriceFee());
                    }
                });
            }
        }


        return ServerResponseEntity.success(page);
    }

    @ApiOperation(value = "导入限时调价商品模版文件")
    @PostMapping("/exportActivityTemplateExcel")
    public ServerResponseEntity<SpuExcelImportDataVO> exportActivityTemplateExcel(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }

        return ServerResponseEntity.success(spuService.importParseSpus(multipartFile));
    }

    @ApiOperation(value = "优惠券skucode商品导入")
    @PostMapping("/ua/importSkuCodeTemplateExcel")
    public ServerResponseEntity<SpuExcelImportDataVO> importSkuCodeTemplateExcel(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }

        return ServerResponseEntity.success(spuService.importSkuCodeTemplateExcel(multipartFile));
    }

    @ApiOperation(value = "导入虚拟门店调价商品模版文件")
    @PostMapping("/exportStoreActivityExcel")
    public ServerResponseEntity<SpuExcelImportDataVO> exportStoreActivityExcel(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }

        return ServerResponseEntity.success(spuReadExcelService.importParseSpus(multipartFile));
    }


    @ApiOperation(value = "商品sku批量修改：售价、状态")
    @PostMapping("/changeSkusPriceFee")
//    public ServerResponseEntity<UploadExcelVO> changeSkusPriceFee(@RequestParam("excelFile") MultipartFile file) {
    public ServerResponseEntity<UploadExcelVO> changeSkusPriceFee(@RequestParam("excelFile") MultipartFile multipartFile) {

        String key="changeSkusPriceFee";
        if(Objects.nonNull(RedisUtil.get(key))){
            throw new LuckException("频繁操作，请稍后重试");
        }
        RedisUtil.set(key,key,120);

        if (multipartFile == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(SkuPriceFeeExcelLogVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }
            String threeLowerUrl=skuExcelService.checkThreeLower(multipartFile);
            //异步处理改价，上传零时文件先转本地否则找不到报错，用完后再删除
            File file=FileUtil.transferFile(multipartFile);
            UpdateSkusPricefeeEvent updateSkusPricefeeEvent=new UpdateSkusPricefeeEvent();
            updateSkusPricefeeEvent.setFile(file);
            updateSkusPricefeeEvent.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(updateSkusPricefeeEvent);

            UploadExcelVO uploadExcelVO=new UploadExcelVO();
            uploadExcelVO.setDate(threeLowerUrl);
            uploadExcelVO.setMessage("操作成功，请转至下载中心下载");
            return ServerResponseEntity.success(uploadExcelVO);
        }catch (Exception e){
            log.error("商品sku批量修改：售价、状态信息错误: "+e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @ApiOperation(value = "选择商品导入模板(spuCode)")
    @PostMapping("/readExcelSpuCode")
    public ServerResponseEntity<List<SpuPageVO>> readExcelSpuCode(@RequestParam("excelFile") MultipartFile file) {
        if (file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        return spuExcelService.readExcelSpuCode(file);
//        try {
//            Map<Integer, List<String>> errorMap = new HashMap<>(8);
//            SpuCodeReadExcelListener spuCodeReadExcelListener=new SpuCodeReadExcelListener(spuExcelService, errorMap);
//            EasyExcel.read(file.getInputStream(), SpuCodeReadExcelVO.class, spuCodeReadExcelListener).sheet().doRead();
//            List<SpuPageVO> pageVOS=spuCodeReadExcelListener.getSpuPageVOS();
//            return ServerResponseEntity.success(pageVOS);
//        }catch (Exception e) {
//            throw new LuckException("操作失败");
//        }
    }

    @PostMapping("/channelPriceTask")
    @ApiOperation(value = "重置渠道价", notes = "重置渠道价")
    public ServerResponseEntity<String> channelPriceTask(@RequestParam(value = "spuCodes",defaultValue = "") String spuCodes) {

        spuChannelPriceService.asyncChannelPriceAndCancelTask(spuCodes);

        return ServerResponseEntity.success("渠道价设置需要时间，请稍后验证价格");
    }

    @GetMapping("downloadAllSpuImg")
    @ApiOperation(value = "获取spu信息", notes = "根据spuId获取spu信息")
    @Async
    public void downloadAllSpuImg(Integer pageSize) {
        PageDTO param = new PageDTO();
        param.setPageNum(2);
        param.setPageSize(pageSize,true);
        PageVO<Spu> pageOrder = PageUtil.doPage(param, () -> spuService.list());
        downloadAllSpuImg(pageOrder.getList());
    }

    @GetMapping("downloadAllSpuImgs")
    @ApiOperation(value = "获取spu信息", notes = "根据spuId获取spu信息")
    @Async
    public void downloadAllSpuImgs() {
        downloadAllSpuImg(spuService.list());
    }

    @GetMapping("downloadAllSpuDetailImgs")
    @ApiOperation(value = "获取spu信息", notes = "根据spuId获取spu信息")
    @Async
    public void downloadAllSpuDetailImgs() {
        downloadAllSpuDetailImg(spuDetailService.list());
    }

    @GetMapping("downloadAllSpuDetailImg")
    @ApiOperation(value = "获取spu信息", notes = "根据spuId获取spu信息")
    @Async
    public void downloadAllSpuDetailImg(Integer pageSize) {
        PageDTO param = new PageDTO();
        param.setPageNum(2);
        param.setPageSize(pageSize,true);
        PageVO<SpuDetail> pageOrder = PageUtil.doPage(param, () -> spuDetailService.list());
        downloadAllSpuDetailImg(pageOrder.getList());
    }

    /**
     * 导入spu简称
     * @param file
     */
    @PostMapping("/readExcelSpuAbbr")
    @ApiOperation(value = "导入spu简称", notes = "导入spu简称")
    public  ServerResponseEntity<String> readExcelSpuAbbr(@RequestParam("excelFile") MultipartFile file){
        return ServerResponseEntity.success(spuExcelService.readExcelSpuAbbr(file));
    }

    public void downloadAllSpuImg(List<Spu> list)  {
        log.info("开始执行商品图片数据，数据大小：{}",list.size());
        long startTime = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger spuCount = new AtomicInteger(0);
        list.forEach(spu -> {
            try {
                spuCount.getAndIncrement();
                log.info("执行商品图片下载，当前第{}条,spuId:{}",spuCount,spu.getSpuId());
                ArrayList<String> imgs = new ArrayList<>();
                if (StrUtil.isNotBlank(spu.getMainImgUrl())) {
                    imgs.add(spu.getMainImgUrl());
                }
                if (StrUtil.isNotBlank(spu.getImgUrls())) {
                    String[] split = spu.getImgUrls().split(",");
                    List<String> imgUrls = Arrays.asList(split);
                    imgs.addAll(imgUrls);
                }
                if (imgs.size() > 0) {
                    imgs.forEach(imgUrl -> {
                        if (imgUrl.contains("https://img")){
                            String url = imgUrl;
                            String imgName = imgUrl.replace("https://imgc1-tp.ezrpro.com", "");
                            imgName = imgName.replace("https://imgc1-up.ezrpro.com", "");
                            imgName = imgName.replace("https://img.alicdn.com", "");
                            try {
                                download(url,imgName);
                                count.getAndIncrement();
                            } catch (Exception e) {
                                log.info("下载图片失败：{}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                log.info("下载图片失败：{},spuId:{}",e.getMessage(),spu.getSpuId());
                e.printStackTrace();
            }
        });
        log.info("执行商品图片数据结束，数据大小：{},耗时:{}ms",count,System.currentTimeMillis() - startTime);
    }

    public void downloadAllSpuDetailImg(List<SpuDetail> list)  {
        log.info("开始执行商品详情图片数据，数据大小：{}",list.size());
        long startTime = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger spuDetailcount = new AtomicInteger(0);
        list.forEach(spuDetail -> {
            try {
                ArrayList<String> imgs = new ArrayList<>();
                spuDetailcount.getAndIncrement();
                log.info("执行商品图片下载，当前第{}条,spuId:{}", spuDetailcount, spuDetail.getSpuId());
                if (StrUtil.isNotBlank(spuDetail.getDetail())) {
                    String img = "";
                    Pattern p_image;
                    Matcher m_image;
                    // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片连接地址
                    String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
                    p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                    m_image = p_image.matcher(spuDetail.getDetail());
                    while (m_image.find()) {
                        // 获得<img />数据
                        img = m_image.group();
                        // 匹配<img>中的src数据
                        Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                        while (m.find()) {
                            imgs.add(m.group(1));
                        }
                    }
                }
                if (imgs.size() > 0) {
                    imgs.forEach(imgUrl -> {
                        if (imgUrl.contains("https://img")) {
                            String url = imgUrl;
                            String imgName = imgUrl.replace("https://imgc1-tp.ezrpro.com", "");
                            imgName = imgName.replace("https://imgc1-up.ezrpro.com", "");
                            imgName = imgName.replace("https://img.alicdn.com", "");
                            try {
                                download(url, imgName);
                                count.getAndIncrement();
                            } catch (Exception e) {
                                log.info("下载图片失败：{}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }catch (Exception e) {
                log.info("下载图片失败：{},spuId:{}", e.getMessage(),spuDetail.getSpuId());
                e.printStackTrace();
            }
        });
        log.info("执行商品图片详情数据结束，数据大小：{},耗时:{}ms",count,System.currentTimeMillis() - startTime);
    }

    public void download(String urlString, String imgName) throws Exception{
        File file = null;
        HttpURLConnection httpUrl = (HttpURLConnection) new URL(urlString).openConnection();
        httpUrl.connect();
        file = inputStreamToFile(httpUrl.getInputStream(),imgName);
        MultipartFile multipartFile = toMultipartFile(imgName,file);
        log.info("fileName:{},filePath:{}",multipartFile.getOriginalFilename(),multipartFile.getName());
        ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, multipartFile.getName(), multipartFile.getContentType());
        log.info("上传至OSS：{}",responseEntity.getData());
        httpUrl.disconnect();
    }

    /**
     * inputStream 转 File
     * @param ins
     * @param name
     * @return
     * @throws Exception
     */
    public static File inputStreamToFile(InputStream ins, String name) throws Exception{
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }

    public static MultipartFile toMultipartFile(String fieldName, File file) throws Exception {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        String contentType = new MimetypesFileTypeMap().getContentType(file);
        FileItem fileItem = diskFileItemFactory.createItem(fieldName, contentType, false, file.getName());
        try (
                InputStream inputStream = new ByteArrayInputStream(FileCopyUtils.copyToByteArray(file));
                OutputStream outputStream = fileItem.getOutputStream()
        ) {
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw e;
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }
}
