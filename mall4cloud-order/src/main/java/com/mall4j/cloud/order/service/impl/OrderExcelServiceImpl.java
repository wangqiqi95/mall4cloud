package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.dto.ChannelsSharerDto;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.ChannelsSharerFeign;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyExcelVO;
import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.vo.OrderDistributionExcelVO;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.payment.feign.PayInfoFeignClient;
import com.mall4j.cloud.api.payment.vo.PayInfoFeignVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.*;
import com.mall4j.cloud.order.constant.OrderExportError;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.service.OrderExcelService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.utils.SubFileExcelExporter;
import com.mall4j.cloud.order.vo.OrderVO;
import com.mall4j.cloud.order.vo.UnDeliveryOrderExcelVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单excel
 *
 * @author Pineapple
 * @date 2021/7/20 8:48
 */
@Service
public class OrderExcelServiceImpl implements OrderExcelService {

    @Value("${mall4cloud.expose.permission:}")
    private Boolean permission;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;
    @Autowired
    StaffFeignClient staffFeignClient;
    @Autowired
    SpuFeignClient spuFeignClient;
    @Autowired
    SkuFeignClient skuFeignClient;
    @Autowired
    private OnsMQTemplate soldUploadExcelTemplate;
    @Autowired
    CouponFeignClient couponFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    PayInfoFeignClient payInfoFeignClient;

    @Autowired
    DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    ChannelsSharerFeign channelsSharerFeign;

    /**
     * 配送类型
     */
    public final static int DELIVERY_MODE_INDEX = 2;

    /**
     * 快递公司名称
     */
    public final static int DELIVERY_COMPANY_NAME_INDEX = 3;

    private final static int FLOW_ID_MIN = 4;
    private final static int FLOW_ID_MAX = 30;

    public final static String[] DELIVERY_MODE = {DeliveryType.DELIVERY.description(), DeliveryType.NOT_DELIVERY.description()};

    private static final Logger log = LoggerFactory.getLogger(OrderExcelServiceImpl.class);

    @Override
    @Async
    public void excelOrderList(OrderSearchDTO orderSearchDTO) {

        int currentPage = 1;
        int pageSize = 20000;
////
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPageNum(currentPage);
//        pageDTO.setPageSize(pageSize,true);
//        PageVO<OrderExcelVO> page = PageUtil.doPage(pageDTO, () -> orderMapper.excelOrderList(orderSearchDTO));
//        totalPage = page.getTotal().intValue() / pageSize + 1;
//        log.info("当前总记录数量");

        //存放全部导出excel文件
        List<String> filePaths = new ArrayList<>();

        Integer totalCount = orderMapper.excelOrderListCount(orderSearchDTO);
        int totalPage = (totalCount + pageSize - 1) / pageSize;

        List<OrderExcelVO> orderExcelVOS = orderMapper.excelOrderList(orderSearchDTO, new PageAdapter(currentPage, pageSize));
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(OrderExcelVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);

        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }
        String excelFileName = doWriteExcel(orderExcelVOS, currentPage);
        if (StrUtil.isNotEmpty(excelFileName)) {
            filePaths.add(excelFileName);
        }

        /**
         * 根据总条数拆分要导出的数据， 拆分excel。
         * 每pageSize条记录拆分一个文件
         */
        for (int i = 2; i <= totalPage; i++) {
            currentPage = currentPage + 1;
            List<OrderExcelVO> orderExcelVOS2 = orderMapper.excelOrderList(orderSearchDTO, new PageAdapter(currentPage, pageSize));
            log.info("分页执行导出查询，currentPage:{},pageSize:{}.", currentPage, pageSize);
            if (CollUtil.isEmpty(orderExcelVOS2)) {
                return;
            }
//            CalcingDownloadRecordDTO downloadRecordDTO2 = new CalcingDownloadRecordDTO();
//            downloadRecordDTO2.setDownloadTime(new Date());
//            downloadRecordDTO2.setFileName(OrderExcelVO.EXCEL_NAME+""+currentPage);
//            downloadRecordDTO2.setCalCount(0);
//            downloadRecordDTO2.setOperatorName(AuthUserContext.get().getUsername());
//            downloadRecordDTO2.setOperatorNo("" + AuthUserContext.get().getUserId());
//            ServerResponseEntity serverResponseEntity2 = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO2);
//            Long downLoadHisId2 = null;
//            if (serverResponseEntity.isSuccess()) {
//                downLoadHisId2 = Long.parseLong(serverResponseEntity2.getData().toString());
//            }
            String pageExcelFileName = doWriteExcel(orderExcelVOS2, currentPage);
            if (StrUtil.isNotEmpty(pageExcelFileName)) {
                filePaths.add(pageExcelFileName);
            }
        }

        FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        finishDownLoadDTO.setCalCount(totalCount);
        //将需要导出的文件压缩成一个压缩包上传
        if (CollectionUtil.isNotEmpty(filePaths)) {
            try {

                log.info("订单数据导出，导出excel文件总数 【{}】", filePaths.size());

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String zipFilePathExport = "/opt/skechers/temp/tentacle/" + time + "/";

                File copyFile = new File(zipFilePathExport);
                copyFile.mkdirs();

                List<File> fromFileList = new ArrayList<>();
                List<File> backFileList = new ArrayList<>();

                filePaths.forEach(item -> {
                    File file = new File(item);
                    fromFileList.add(file);
                    log.info("订单数据导出，单个excel文件信息 文件名【{}】 文件大小【{}】", file.getName(), cn.hutool.core.io.FileUtil.size(file));
                });
                //文件存放统一目录
                FileUtil.copyCodeToFile(fromFileList, zipFilePathExport, backFileList);
                //压缩统一文件目录
                String zipPath = CompressUtil.zipFile(copyFile, "zip");

                if (new File(zipPath).isFile()) {

                    FileInputStream fileInputStream = new FileInputStream(zipPath);

                    String zipFileName = "soldOrder_" + AuthUserContext.get().getUserId() + "_" + time + ".zip";
                    MultipartFile multipartFile = new MultipartFileDto(zipFileName, zipFileName,
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if (responseEntity.isSuccess()) {
                        log.info("---OrderExcelServiceImpl---" + responseEntity.toString());
                        //下载中心记录
                        String fileName = DateUtil.format(new Date(), "yyyyMMddHHmmss") + "" + OrderExcelVO.EXCEL_NAME;
                        finishDownLoadDTO.setFileName(fileName);
                        finishDownLoadDTO.setStatus(1);
                        finishDownLoadDTO.setFileUrl(responseEntity.getData());
                        finishDownLoadDTO.setRemarks("导出成功");
                        downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    }
                    //删除本地临时文件
                    cn.hutool.core.io.FileUtil.del(zipPath);
                    cn.hutool.core.io.FileUtil.del(copyFile);
                }
            } catch (Exception e) {
                log.info("订单数据导出，excel生成zip失败", e);
                //下载中心记录
                if (finishDownLoadDTO != null) {
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks("订单数据导出，excel生成zip失败");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
            }

        } else {
            if (finishDownLoadDTO != null) {
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("订单数据导出，没有可导出的文件");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }


    }

    private String doWriteExcel(List<OrderExcelVO> orderExcelList, int currentPage) {
        int index = 0;
        Long orderId = null;

        /**
         * 查询用户列表
         */
        List<Long> useridList = orderExcelList.stream().map(OrderExcelVO::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        /**
         * 查询店铺列表
         */
        List<Long> storeidList = orderExcelList.stream().filter(s -> s != null).map(OrderExcelVO::getStoreId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listBypByStoreIdList(storeidList);
        Map<Long, StoreVO> storeMaps = new HashMap<>();
        if (storesResponse != null && storesResponse.isSuccess() && storesResponse.getData().size() > 0) {
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));
        }

        /**
         * 导购
         */
        List<Long> daogouList = orderExcelList.stream().filter(s -> s.getDistributionUserType() == 1).map(OrderExcelVO::getDistributionUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> daogouResponse = staffFeignClient.getStaffBypByIds(daogouList);
        Map<Long, StaffVO> daogouMaps = new HashMap<>();
        if (daogouResponse != null && daogouResponse.isSuccess() && daogouResponse.getData().size() > 0) {
            daogouMaps = daogouResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
        }

        /**
         * 代客下单人
         */
        List<Long> valetOrderList = orderExcelList.stream().filter(s -> s.getBuyStaffId() != null && s.getBuyStaffId() > 0).map(OrderExcelVO::getBuyStaffId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> valetOrderResponse = staffFeignClient.getStaffByIds(valetOrderList);
        Map<Long, StaffVO> valetOrderMaps = new HashMap<>();
        if (valetOrderResponse != null && valetOrderResponse.isSuccess() && valetOrderResponse.getData().size() > 0) {
            valetOrderMaps = valetOrderResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
        }


        /**
         * 微客
         */
        List<Long> weikeidList = orderExcelList.stream().filter(s -> s.getDistributionUserType() == 2).map(OrderExcelVO::getDistributionUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> weikeResponse = userFeignClient.getUserBypByUserIds(weikeidList);
        Map<Long, UserApiVO> weikeMaps = new HashMap<>();
        if (weikeResponse != null && weikeResponse.isSuccess() && weikeResponse.getData().size() > 0) {
            weikeMaps = weikeResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        /**
         * spu
         */
        List<Long> spuList = orderExcelList.stream().map(OrderExcelVO::getSpuId).distinct().collect(Collectors.toList());
        log.info("订单导出，订单商品数据条数【{}】, 商品spuId集合【{}】", spuList.size(), CollectionUtil.isNotEmpty(spuList) ? JSON.toJSON(spuList) : "商品spuId集合为空");
        ServerResponseEntity<List<SpuVO>> spuVOResponse = spuFeignClient.listSpuNameBypBySpuIds(spuList);
        Map<Long, SpuVO> spuMaps = new HashMap<>();
        if (weikeResponse != null && weikeResponse.isSuccess() && weikeResponse.getData().size() > 0) {
            log.info("订单导出，获取到商品数据条数：【{}】，商品集合是否为空【{}】", CollectionUtil.isNotEmpty(spuList), spuVOResponse.getData().isEmpty() ? "商品Data为空" : "商品Data不为空");
            spuMaps = spuVOResponse.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, p -> p));
        }

        List<Long> skuIdList = orderExcelList.stream().map(OrderExcelVO::getSkuId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<SkuVO>> skuResponse = skuFeignClient.listSkuCodeBypByIds(skuIdList);
        Map<Long, SkuVO> skuMaps = new HashMap<>();
        if (skuResponse != null && skuResponse.isSuccess() && skuResponse.getData().size() > 0) {
            skuMaps = skuResponse.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId, p -> p));
        }

        //订单使用优惠券
        List<Long> orderIdList = orderExcelList.stream().map(OrderExcelVO::getOrderId).collect(Collectors.toList());
        ServerResponseEntity<List<TCouponUserOrderDetailVO>> couponResponse = couponFeignClient.getCouponListBypByOrderIds(orderIdList);
        Map<Long, TCouponUserOrderDetailVO> couponMaps = new HashMap<>();
        if (couponResponse != null && couponResponse.isSuccess() && couponResponse.getData().size() > 0) {
            couponMaps = couponResponse.getData().stream().collect(Collectors.toMap(TCouponUserOrderDetailVO::getOrderNo, p -> p));
        }

        //支付信息
        ServerResponseEntity<List<PayInfoFeignVO>> payinfoResponse = payInfoFeignClient.getPayInfoByOrderids(orderIdList);
        Map<String, PayInfoFeignVO> payinfoMaps = new HashMap<>();
        if (payinfoResponse != null && payinfoResponse.isSuccess() && payinfoResponse.getData().size() > 0) {
            payinfoMaps = payinfoResponse.getData().stream().collect(Collectors.toMap(PayInfoFeignVO::getOrderId, Function.identity(),(oldValue, newValue) -> newValue));
        }

        //分享员
        List<String> sharerOpenidList = orderExcelList.stream().filter(s -> s.getOrderSource() == 3).map(OrderExcelVO::getSharerOpenid).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<ChannelsSharerDto>> sharerResponse = channelsSharerFeign.getByOpenIds(sharerOpenidList);
        Map<String, ChannelsSharerDto> sharerMaps = new HashMap<>();
        if (sharerResponse != null && sharerResponse.isSuccess() && sharerResponse.getData().size() > 0) {
            sharerMaps = sharerResponse.getData().stream().collect(Collectors.toMap(ChannelsSharerDto::getOpenid, p -> p));
        }


        for (OrderExcelVO orderExcel : orderExcelList) {
            if (!Objects.equals(orderExcel.getOrderId(), orderId)) {
                index++;
                orderId = orderExcel.getOrderId();
            }
            orderExcel.setSeq(String.valueOf(index));
            // 订单信息
//            log.info("订单导出：对象参数：{}", JSONObject.toJSONString(orderExcel));
            Integer deliveryType = Integer.valueOf(orderExcel.getDeliveryType());
            orderExcel.setDeliveryType(DeliveryType.getDescription(deliveryType));
            orderExcel.setTotal(conversionPrices(orderExcel.getTotal()));
            orderExcel.setFreightAmount(conversionPrices(orderExcel.getFreightAmount()));
            orderExcel.setReduceAmount(conversionPrices(orderExcel.getReduceAmount()));
            orderExcel.setActualTotal(conversionPrices(orderExcel.getActualTotal()));
//            orderExcel.setTotalRefundAmount(conversionPrices(orderExcel.getTotalRefundAmount()));
            String payType = Objects.isNull(orderExcel.getPayType()) ? Constant.UNPAID : PayType.getPayTypeName(Integer.valueOf(orderExcel.getPayType()));
            orderExcel.setPayType(payType);
            //收货手机号
//            if (BooleanUtil.isFalse(permission)) {
//                orderExcel.setMobile(PhoneUtil.hideBetween(orderExcel.getMobile()).toString());
//            }
            if (Objects.equals(deliveryType, DeliveryType.STATION.value())) {
                orderExcel.setStatus(OrderStatus.getStationName(Integer.valueOf(orderExcel.getStatus())));
            } else {
                orderExcel.setStatus(OrderStatus.getDeliveryName(Integer.valueOf(orderExcel.getStatus())));
            }
            String refundStatus = StrUtil.isBlank(orderExcel.getRefundStatus()) ? Constant.NO_AFTER_SALES : RefundStatusEnum.getRefundName(Integer.valueOf(orderExcel.getRefundStatus()));
            orderExcel.setRefundStatus(refundStatus);
            // 订单项信息
            orderExcel.setPrice(conversionPrices(orderExcel.getPrice()));
            orderExcel.setSpuTotalAmount(conversionPrices(orderExcel.getSpuTotalAmount()));
            orderExcel.setShareReduce(conversionPrices(orderExcel.getShareReduce()));
            orderExcel.setActualTotalItem(conversionPrices(orderExcel.getActualTotalItem()));
            String refundStatusItem = StrUtil.isBlank(orderExcel.getRefundStatusItem()) ? Constant.NO_AFTER_SALES : RefundStatusEnum.getRefundName(Integer.valueOf(orderExcel.getRefundStatusItem()));
            orderExcel.setRefundStatusItem(refundStatusItem);

            //用户信息
            UserApiVO user = userMaps.get(orderExcel.getUserId());
//            if (user != null) {
//                orderExcel.setUserName(user.getNickName());
//                orderExcel.setUserPhone(user.getPhone());
//                orderExcel.setUserNo(user.getVipcode());
//            }

            //直播间id
            if (orderExcel.getOrderSource() != null && orderExcel.getOrderSource() == 1) {
                orderExcel.setLivingId(orderExcel.getSourceId());
            }

            //门店信息
            StoreVO storeVO = storeMaps.get(orderExcel.getStoreId());
            if (storeVO != null) {
                orderExcel.setStoreName(storeVO.getName());
                orderExcel.setStoreCode(storeVO.getStoreCode());
            }

            // 微客
            if (orderExcel.getDistributionUserType() == 2) {
                UserApiVO weikeUsers = weikeMaps.get(orderExcel.getDistributionUserId());
                if (weikeUsers != null) {
                    orderExcel.setWeike(weikeUsers.getNickName());
//                    orderExcel.setWeikeNo(weikeUsers.getPhone());
                    orderExcel.setWeikePhone(weikeUsers.getPhone());
                }
            }

            //代客下单人
            String valetOrderStaffName = "";
            String valetOrderStaffPhone = "";
            if (orderExcel.getBuyStaffId() != null && orderExcel.getBuyStaffId() > 0) {
                StaffVO valetOrderStaffVO = valetOrderMaps.get(orderExcel.getBuyStaffId());
                if (valetOrderStaffVO != null) {
                    valetOrderStaffName = valetOrderStaffVO.getStaffName();
                    valetOrderStaffPhone = valetOrderStaffVO.getMobile();
                }
            }
            orderExcel.setValetOrderStaffName(valetOrderStaffName);
            orderExcel.setValetOrderStaffPhone(valetOrderStaffPhone);

            //导购
            if (orderExcel.getDistributionUserType() == 1) {
                StaffVO daogou = daogouMaps.get(orderExcel.getDistributionUserId());
                if (daogou != null) {
                    orderExcel.setDaogou(daogou.getStaffName());
                    orderExcel.setDaogouMobile(daogou.getMobile());
                    orderExcel.setDaogouNo(daogou.getStaffNo());
                }
            }

            //spuCode
            SpuVO spuVO = spuMaps.get(orderExcel.getSpuId());
            if (spuVO != null) {
                log.info("订单导出商品spuCode 【{}】", spuVO.getSpuCode());
                orderExcel.setSpuCode(spuVO.getSpuCode());
            }
            //skuCode
            SkuVO skuVO = skuMaps.get(orderExcel.getSkuId());
            if (skuVO != null) {
                orderExcel.setSkuCode(skuVO.getSkuCode());
                orderExcel.setMarketPrice(conversionPrices(skuVO.getMarketPriceFee().toString()));
                //sku名称
                orderExcel.setSkuName(skuVO.getPriceCode());
            }

            //优惠券
            TCouponUserOrderDetailVO couponUserOrderDetailVO = couponMaps.get(orderExcel.getOrderId());
            if (couponUserOrderDetailVO != null) {
                orderExcel.setCouponId(StrUtil.toString(couponUserOrderDetailVO.getCouponId()));
                orderExcel.setCouponName(couponUserOrderDetailVO.getCouponName());
                orderExcel.setCouponCode(couponUserOrderDetailVO.getCouponCode());
                if (couponUserOrderDetailVO.getCouponAmount() != null && couponUserOrderDetailVO.getCouponAmount() > 0) {
                    orderExcel.setCouponReduceAmount(conversionPrices(String.valueOf(couponUserOrderDetailVO.getCouponAmount())));
                } else {
                    orderExcel.setCouponReduceAmount(conversionPrices(orderExcel.getShopCouponAmount()));
                }
                if (couponUserOrderDetailVO.getSourceType() != null) {
                    if (couponUserOrderDetailVO.getSourceType() == 1) {
                        orderExcel.setCouponSourceType("小程序");
                    } else {
                        orderExcel.setCouponSourceType("CRM");
                    }
                }

            }

            //支付单号
            PayInfoFeignVO payInfoFeignVO = payinfoMaps.get(String.valueOf(orderExcel.getOrderId()));
            if (payInfoFeignVO != null) {
                orderExcel.setPayId(payInfoFeignVO.getPayId());
                orderExcel.setBizPayNo(payInfoFeignVO.getPayBizNo());
            }

            if (orderExcel.getOrderType() != null) {
                if (orderExcel.getOrderType() == 1) {
                    orderExcel.setActivityType("团购订单");
                }
                if (orderExcel.getOrderType() == 2) {
                    orderExcel.setActivityType("秒杀订单");
                }
                if (orderExcel.getOrderType() == 3) {
                    orderExcel.setActivityType("积分订单");
                }
            }

            if (orderExcel.getOrderSource() != null) {
                if(orderExcel.getOrderSource()==0){
                    orderExcel.setOrderSourceName("普通订单");
                }
                if(orderExcel.getOrderSource()==1){
                    orderExcel.setOrderSourceName("直播订单");
                }
                if(orderExcel.getOrderSource()==2){
                    orderExcel.setOrderSourceName("视频号3.0订单");
                }
                if(orderExcel.getOrderSource()==3){
                    orderExcel.setOrderSourceName("视频号4.0订单");
                    orderExcel.setChannelsShopName("SKECHERS斯凯奇官方旗舰店");
                    if(StrUtil.isNotEmpty(orderExcel.getSharerOpenid())){
                        ChannelsSharerDto sharerDto = sharerMaps.get(orderExcel.getSharerOpenid());
                        if(sharerDto!=null){
                            orderExcel.setSharerName(sharerDto.getName());
                        }
                    }
                }
            }

            //计算订单退款金额

            // 国际化信息
//            OrderItemLangVO orderItemLangVO = OrderLangUtil.handleOrderItemLangVO(orderExcel.getOrderItemLangList());
//            if (Objects.nonNull(orderItemLangVO)) {
//                orderExcel.setSkuName(orderItemLangVO.getSkuName());
//                orderExcel.setSpuName(orderItemLangVO.getSpuName());
//            }
        }

//        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
//        downloadRecordDTO.setDownloadTime(new Date());
//        downloadRecordDTO.setFileName(OrderExcelVO.EXCEL_NAME);
//        downloadRecordDTO.setCalCount(orderExcelList.size());
//        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
//        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
//        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
//        Long downLoadHisId = null;
//        if (serverResponseEntity.isSuccess()) {
//            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
//        }
//
//        ExcelUploadDTO excelUploadDTO = new ExcelUploadDTO(downLoadHisId,
//                orderExcelList,
//                OrderExcelVO.EXCEL_NAME,
//                OrderExcelVO.MERGE_ROW_INDEX,
//                OrderExcelVO.MERGE_COLUMN_INDEX,
//                OrderExcelVO.class);
//        soldUploadExcelTemplate.syncSend(excelUploadDTO);

        //下载中心记录
//        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
//        finishDownLoadDTO.setId(downLoadHisId);
//
//        if(CollUtil.isEmpty(orderExcelList)) {
//            finishDownLoadDTO.setRemarks("没有可导出的数据");
//            finishDownLoadDTO.setStatus(2);
//            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
//            log.error("没有可导出的数据");
//            return "";
//        }

        File file = null;
        try {
            int calCount = orderExcelList.size();
            log.info("导出数据行数 【{}】", calCount);
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            long startTime = System.currentTimeMillis();
            log.info("开始执行订单生成excel 总条数【{}】", calCount);
            String pathExport = SkqUtils.getExcelFilePath() + "/" + time + OrderExcelVO.EXCEL_NAME + "：(" + currentPage + ")" + ".xlsx";
            EasyExcel.write(pathExport, OrderExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(orderExcelList);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行订单生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime, orderExcelList.size(), pathExport);
            return pathExport;
//            startTime = System.currentTimeMillis();
//            log.info("导出数据到本地excel，开始执行上传excel.....");
//            file=new File(pathExport);
//            FileInputStream is = new FileInputStream(file);
//            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
//                    contentType, is);
//
//            String originalFilename = multipartFile.getOriginalFilename();
//            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
//            String mimoPath = "excel/" + time + "/" + originalFilename;
//            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
//            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
//                log.info("上传文件地址："+responseEntity.getData());
//                //下载中心记录
//                finishDownLoadDTO.setCalCount(calCount);
//                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+OrderExcelVO.EXCEL_NAME;
//                finishDownLoadDTO.setFileName(fileName);
//                finishDownLoadDTO.setStatus(1);
//                finishDownLoadDTO.setFileUrl(responseEntity.getData());
//                finishDownLoadDTO.setRemarks("导出成功");
//                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
//                // 删除临时文件
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("导出订单信息错误: " + e.getMessage(), e);
//            finishDownLoadDTO.setRemarks("导出订单失败，信息错误："+e.getMessage());
//            finishDownLoadDTO.setStatus(2);
//            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return "";
//        return orderExcelList;
    }


    @Override
    public void excelUnDeliveryOrderList(HttpServletResponse response, OrderSearchDTO orderSearchDTO) {
        List<UnDeliveryOrderExcelVO> unDeliveryOrderList = orderMapper.excelUnDeliveryOrderList(orderSearchDTO, I18nMessage.getLang());
        Map<Integer, String[]> map = new HashMap<>(8);
        map.put(DELIVERY_MODE_INDEX, DELIVERY_MODE);
        int index = 0;
        //合并单元格
        Long orderId = null;
        for (UnDeliveryOrderExcelVO orderExcel : unDeliveryOrderList) {
            if (!Objects.equals(Long.valueOf(orderExcel.getOrderId()), orderId)) {
                index++;
                orderId = Long.valueOf(orderExcel.getOrderId());
            }
            orderExcel.setSeq(String.valueOf(index));
            //物流信息
            orderExcel.setDeliveryType(DeliveryType.getDescription(Integer.valueOf(orderExcel.getDeliveryType())));
            //快递公司名称
            map.put(DELIVERY_COMPANY_NAME_INDEX, getDeliveryCompanyName());
            //收货手机号
            if (BooleanUtil.isFalse(permission)) {
                orderExcel.setMobile(PhoneUtil.hideBetween(orderExcel.getMobile()).toString());
            }
        }
        writeExcel(response, unDeliveryOrderList, map);
    }

    @Override
    public void exportOrderExcel(List<UnDeliveryOrderExcelVO> list, Map<Integer, List<String>> errorMap) {
        List<DeliveryCompanyExcelVO> companyExcelList = deliveryFeignClient.listDeliveryCompany().getData();
        Map<String, Long> companyNameMap = companyExcelList.stream().collect(Collectors.toMap(DeliveryCompanyExcelVO::getName, DeliveryCompanyExcelVO::getDeliveryCompanyId));
        Map<String, List<UnDeliveryOrderExcelVO>> orderMap = list.stream().collect(Collectors.groupingBy(UnDeliveryOrderExcelVO::getSeq));
        for (String seq : orderMap.keySet()) {
            List<UnDeliveryOrderExcelVO> orderExcels = orderMap.get(seq);
            DeliveryOrderDTO deliveryOrder = new DeliveryOrderDTO();
            try {
                loadOrderData(orderExcels, deliveryOrder, companyNameMap, errorMap);
            } catch (Exception e) {
                addOrderExcelError(errorMap, OrderExportError.OTHER, e.getMessage());
                continue;
            }
            if (Objects.nonNull(deliveryOrder.getSelectOrderItems())) {
                //订单发货
                orderService.delivery(deliveryOrder);
            }
        }
    }

    @Override
    public String orderExportError(Map<Integer, List<String>> errorMap) {
        StringBuilder info = new StringBuilder();
        for (Integer key : errorMap.keySet()) {
            List<String> list = errorMap.get(key);
            OrderExportError orderExportError = OrderExportError.instance(key);
            if (Objects.equals(orderExportError, OrderExportError.OTHER)) {
                info.append(list.toString() + "\n");
                continue;
            }
            info.append(orderExportError.errorInfo() + list.toString() + "\n");
        }
        return info.toString();
    }

    @Override
    @Async
    public List<OrderDistributionExcelVO> excelDistributionOrderList(DistributionQueryDTO queryDTO) {
        if (orderService.buildQueryCondition(queryDTO)) {
            return new ArrayList<>();
        }
        SubFileExcelExporter.export(
                OrderDistributionExcelVO.EXCEL_NAME,
                OrderDistributionExcelVO.EXCEL_NAME,
                20000,
                orderService::listDistributionOrders,
                queryDTO,
                this::doExportExcel,
                OrderDistributionExcelVO.class
        );
        return null;
    }

    private List<OrderDistributionExcelVO> doExportExcel(List<EsOrderBO> esOrderBOS) {
        List<OrderDistributionExcelVO> orderDistributionExcelVOS = new ArrayList<>();
        /**
         * 查询用户列表
         */
        List<Long> useridList = esOrderBOS.stream().map(EsOrderBO::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        /**
         * 查询店铺列表
         */
        List<Long> storeidList = esOrderBOS.stream().filter(s -> s != null).map(EsOrderBO::getStoreId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listBypByStoreIdList(storeidList);
        Map<Long, StoreVO> storeMaps = new HashMap<>();
        if (storesResponse != null && storesResponse.isSuccess() && storesResponse.getData().size() > 0) {
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));
        }

        /**
         * 导购
         */
        List<Long> daogouList = esOrderBOS.stream().filter(s -> s.getDistributionUserType() == 1).map(EsOrderBO::getDistributionUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> daogouResponse = staffFeignClient.getStaffByIds(daogouList);
        Map<Long, StaffVO> daogouMaps = new HashMap<>();
        if (daogouResponse != null && daogouResponse.isSuccess() && daogouResponse.getData().size() > 0) {
            daogouMaps = daogouResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
        }

        /**
         * 代客下单人
         */
        List<Long> valetOrderList = esOrderBOS.stream().filter(s -> s.getBuyStaffId() != null && s.getBuyStaffId() > 0).map(EsOrderBO::getBuyStaffId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> valetOrderResponse = staffFeignClient.getStaffByIds(valetOrderList);
        Map<Long, StaffVO> valetOrderMaps = new HashMap<>();
        if (valetOrderResponse != null && valetOrderResponse.isSuccess() && valetOrderResponse.getData().size() > 0) {
            valetOrderMaps = valetOrderResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
        }


        /**
         * 微客
         */
        List<Long> weikeidList = esOrderBOS.stream().filter(s -> s.getDistributionUserType() == 2).map(EsOrderBO::getDistributionUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> weikeResponse = userFeignClient.getUserByUserIds(weikeidList);
        Map<Long, UserApiVO> weikeMaps = new HashMap<>();
        if (weikeResponse != null && weikeResponse.isSuccess() && weikeResponse.getData().size() > 0) {
            weikeMaps = weikeResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }


        /**
         * 发展人
         */
        List<Long> developingList = esOrderBOS.stream().map(EsOrderBO::getDevelopingUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> developingResponse = staffFeignClient.getStaffByIds(developingList);
        Map<Long, StaffVO> developingMaps = new HashMap<>();
        if (developingResponse != null && developingResponse.isSuccess() && developingResponse.getData().size() > 0) {
            developingMaps = developingResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
        }


//
        for (EsOrderBO esOrderBO : esOrderBOS) {
            for (OrderItemVO orderItemVO : esOrderBO.getOrderItemList()) {
                OrderDistributionExcelVO excelVO = new OrderDistributionExcelVO();
                excelVO.setSpuName(orderItemVO.getSpuName());
                //订单类型
                excelVO.setOrderType(buildOrderType(esOrderBO.getOrderType()));
                //退款单号
//                excelVO.setOrderRefundId(orderItemVO.getFinalRefundId());
//                excelVO.setOrderId(esOrderBO.getOrderId());

                excelVO.setOrderRefundNumber(orderItemVO.getFinalRefundNumber());
                excelVO.setOrderNumber(orderItemVO.getOrderNumber());

                excelVO.setCreateTime(esOrderBO.getCreateTime());
                excelVO.setDistributionCreateTime(esOrderBO.getCreateTime());
                excelVO.setPayTime(esOrderBO.getPayTime());
                excelVO.setDeliveryTime(esOrderBO.getDeliveryTime());
                excelVO.setFinallyTime(esOrderBO.getFinallyTime());
                excelVO.setSettledTime(esOrderBO.getSettledTime());

                String distributionStatus = "";
                if (esOrderBO.getDistributionStatus() == 0) {
                    distributionStatus = "待结算";
                } else if (esOrderBO.getDistributionStatus() == 1) {
                    distributionStatus = "已结算-待提现";
                } else if (esOrderBO.getDistributionStatus() == 2) {
                    distributionStatus = "已结算-已提现";
                } else {
                    distributionStatus = "3提现中";
                }
                excelVO.setDistributionStatus(distributionStatus);
                excelVO.setCount(orderItemVO.getCount());
                excelVO.setAmount(String.valueOf(new BigDecimal(orderItemVO.getActualTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
                excelVO.setDistributionAmount(String.valueOf(new BigDecimal(orderItemVO.getDistributionAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
                excelVO.setDevelopingAmount(String.valueOf(new BigDecimal(orderItemVO.getDistributionParentAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));

                String distributionRelationName = "";
                if (esOrderBO.getDistributionRelation() == 1) {
                    distributionRelationName = "分享关系";
                } else if (esOrderBO.getDistributionRelation() == 2) {
                    distributionRelationName = "服务关系";
                } else if (esOrderBO.getDistributionRelation() == 3) {
                    distributionRelationName = "自主下单";
                } else if (esOrderBO.getDistributionRelation() == 4) {
                    distributionRelationName = "代客下单";
                }
                excelVO.setDistributionRelationName(distributionRelationName);

                // 订单分类
                String orderKindName = "";
                if (esOrderBO.getIsDevelopingOrder()) {
                    orderKindName = "发展订单";
                } else {
                    orderKindName = "分销订单";
                }
                excelVO.setOrderKindName(orderKindName);

                StoreVO storeVO = storeMaps.get(esOrderBO.getStoreId());
                if (storeVO != null) {
                    excelVO.setStoreName(storeVO.getName());
                }

                UserApiVO userApiVO = userMaps.get(esOrderBO.getUserId());
//                if (userApiVO != null) {
//                    excelVO.setPhone(userApiVO.getPhone());
//                }

                String distributionName = "";
                //导购
                if (esOrderBO.getDistributionUserType() == 1) {
                    StaffVO staffVO = daogouMaps.get(esOrderBO.getDistributionUserId());
                    if (staffVO != null) {
                        distributionName = staffVO.getStaffName();
                        excelVO.setDistributionCode(staffVO.getStaffNo());
                    }
                } else {
                    //微客
//                    UserApiVO duser = weikeMaps.get(esOrderBO.getDistributionUserId());
//                    if (duser != null) {
//                        distributionName = duser.getNickName();
//                        excelVO.setDistributionPhone(duser.getPhone());
//                    }
                }
                excelVO.setDistributionName(distributionName);

                //代客下单人
                String valetOrderStaffName = "";
                String valetOrderStaffPhone = "";
                if (esOrderBO.getBuyStaffId() != null && esOrderBO.getBuyStaffId() > 0) {
                    StaffVO valetOrderStaffVO = valetOrderMaps.get(esOrderBO.getBuyStaffId());
                    if (valetOrderStaffVO != null) {
                        valetOrderStaffName = valetOrderStaffVO.getStaffName();
                        valetOrderStaffPhone = valetOrderStaffVO.getMobile();
                    }
                }
                excelVO.setValetOrderStaffName(valetOrderStaffName);
                excelVO.setValetOrderStaffPhone(valetOrderStaffPhone);

                StaffVO staffVO = developingMaps.get(esOrderBO.getDevelopingUserId());
                if (staffVO != null) {
                    excelVO.setDevelopingName(staffVO.getStaffName());
                    excelVO.setDevelopingCode(staffVO.getStaffNo());
                }

                excelVO.setWechatOrderId(esOrderBO.getWechatOrderId());
                orderDistributionExcelVOS.add(excelVO);
            }
        }
        return orderDistributionExcelVOS;
    }

    private String buildOrderType(Integer orderType){
        switch (orderType){
            case 0:
                return "普通订单";
            case 1:
                return "团购订单";
            case 2:
                return "秒杀订单";
            case 3:
                return "积分订单";
            case 4:
                return "一口价订单";
            case 5:
                return "企业单";
            default:
                return "未知类型";
        }
    }

    private void loadOrderData(List<UnDeliveryOrderExcelVO> orderExcels, DeliveryOrderDTO deliveryOrder, Map<String, Long> companyNameMap, Map<Integer, List<String>> errorMap) {
        UnDeliveryOrderExcelVO orderExcel = orderExcels.get(0);
        //订单id不能为空且该订单必须为当前店铺的订单，并为待发货状态
        if (Objects.isNull(orderExcel.getOrderId())) {
            addOrderExcelError(errorMap, OrderExportError.ORDER_ID, orderExcel.getSeq());
            return;
        }
        OrderVO order = orderService.getOrderByOrderId(Long.valueOf(orderExcel.getOrderId()));
        if (Objects.isNull(order) || !Objects.equals(order.getStatus(), OrderStatus.PAYED.value())) {
            addOrderExcelError(errorMap, OrderExportError.ORDER_ID, orderExcel.getSeq());
            return;
        }
        List<OrderItemVO> orderItemVOList = orderItemService.listOrderItemAndLangByOrderId(Long.valueOf(orderExcel.getOrderId()));
        for (OrderItemVO orderItemVO : orderItemVOList) {
            if (Objects.equals(orderItemVO.getBeDeliveredNum(), 0) || !Objects.equals(orderItemVO.getBeDeliveredNum(), orderItemVO.getCount())) {
                //如果该订单已经被部分发货或全部发货则不予发货
                addOrderExcelError(errorMap, OrderExportError.BE_DELIVERY, orderExcel.getSeq());
                return;
            }
        }
        deliveryOrder.setOrderId(Long.valueOf(orderExcel.getOrderId()));
        //配送方式不为空
        if (Objects.isNull(orderExcel.getDeliveryType())) {
            addOrderExcelError(errorMap, OrderExportError.DELIVERY_TYPE, orderExcel.getSeq());
            return;
        }
        if (Objects.equals(orderExcel.getDeliveryType(), DeliveryType.DELIVERY.description())) {
            deliveryOrder.setDeliveryType(DeliveryType.DELIVERY.value());
        } else if (Objects.equals(orderExcel.getDeliveryType(), DeliveryType.NOT_DELIVERY.description())) {
            deliveryOrder.setDeliveryType(DeliveryType.NOT_DELIVERY.value());
        }
        //只有快递才需要快递校验
        if (Objects.equals(orderExcel.getDeliveryType(), DeliveryType.DELIVERY.description())) {
            //快递公司校验
            if (Objects.isNull(orderExcel.getDeliveryCompanyName())) {
                addOrderExcelError(errorMap, OrderExportError.DELIVERY_COMPANY_NAME, orderExcel.getSeq());
                return;
            }
            deliveryOrder.setDeliveryCompanyId(companyNameMap.get(orderExcel.getDeliveryCompanyName()));
            //快递单号校验
            String deliveryNo = orderExcel.getDeliveryNo();
            deliveryNo = StringUtils.deleteWhitespace(deliveryNo);
            boolean isTrue = StrUtil.isBlank(deliveryNo) ||
                    (deliveryNo.length() > FLOW_ID_MAX || deliveryNo.length() < FLOW_ID_MIN)
                    || !PrincipalUtil.isMatching(PrincipalUtil.WITHOUT_CHINESE, deliveryNo);
            if (isTrue) {
                addOrderExcelError(errorMap, OrderExportError.DELIVERY_NO, orderExcel.getSeq());
                return;
            }
            deliveryOrder.setDeliveryNo(deliveryNo);
        }
        List<DeliveryOrderItemDTO> orderItemFeignVOList = new ArrayList<>();
        for (OrderItemVO orderItemVO : orderItemVOList) {
            DeliveryOrderItemDTO deliveryOrderItemFeignVO = new DeliveryOrderItemDTO();
            deliveryOrderItemFeignVO.setOrderItemId(orderItemVO.getOrderItemId());
            deliveryOrderItemFeignVO.setChangeNum(orderItemVO.getBeDeliveredNum());
            deliveryOrderItemFeignVO.setPic(orderItemVO.getPic());
            deliveryOrderItemFeignVO.setSpuName(orderItemVO.getSpuName());
            orderItemFeignVOList.add(deliveryOrderItemFeignVO);
        }
        deliveryOrder.setSelectOrderItems(orderItemFeignVOList);
    }

    private void addOrderExcelError(Map<Integer, List<String>> errorMap, OrderExportError orderExportError, String error) {
        List<String> list = errorMap.get(orderExportError.value());
        if (CollUtil.isEmpty(list)) {
            list = new ArrayList<>();
            errorMap.put(orderExportError.value(), list);
        }
        list.add(error);
    }

    /**
     * 转换金额格式
     *
     * @param num
     * @return
     */
    private String conversionPrices(String num) {
        if (StrUtil.isEmpty(num)) {
            return num;
        }
        BigDecimal b1 = new BigDecimal(num);
        BigDecimal b2 = new BigDecimal(Constant.PRICE_MAGNIFICATION);
        double price = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(price);
    }

    private String[] getDeliveryCompanyName() {
        //快递公司名称
        ServerResponseEntity<List<DeliveryCompanyExcelVO>> companyData = deliveryFeignClient.listDeliveryCompany();
        if (!Objects.equals(companyData.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(companyData.getMsg());
        }
        List<DeliveryCompanyExcelVO> companyExcelList = companyData.getData();
        if (CollUtil.isNotEmpty(companyExcelList)) {
            String[] companyNames = new String[companyExcelList.size()];
            for (int i = 0; i < companyExcelList.size(); i++) {
                DeliveryCompanyExcelVO companyExcel = companyExcelList.get(i);
                companyNames[i] = companyExcel.getName();
            }
            return companyNames;
        }
        return null;
    }

    private void writeExcel(HttpServletResponse response, List<UnDeliveryOrderExcelVO> list, Map<Integer, String[]> map) {
        ExcelWriter excelWriter = null;
        try {
            ExcelWriterBuilder excelWriterMerge = ExcelUtil.getExcelWriterMerge(response, UnDeliveryOrderExcelVO.EXCEL_NAME, UnDeliveryOrderExcelVO.MERGE_ROW_INDEX,
                    UnDeliveryOrderExcelVO.MERGE_COLUMN_INDEX);
            excelWriter = ExcelUtil.getExcelModel(excelWriterMerge, map, Constant.START_ROW).build();
            //订单信息
            if (CollUtil.isNotEmpty(list)) {
                //写入
                WriteSheet writeSheet = EasyExcel.writerSheet(UnDeliveryOrderExcelVO.EXCEL_NAME).head(UnDeliveryOrderExcelVO.class).build();
                excelWriter.write(list, writeSheet);
            }
        } catch (Exception e) {
            log.error("导出excel失败", e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
