package com.mall4j.cloud.order.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.biz.feign.SendEmailFeignClient;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderVo;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.vo.OrderPriceDiscountConfigVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.order.mapper.OrderItemMapper;
import com.mall4j.cloud.order.vo.OrderItemPriceErrorVO;
import com.mall4j.cloud.order.vo.OrderSkuExcelLogVo;
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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单：每30分钟监控一次成交订单，订单成交价和商品吊牌价对比，低于三折进行预警，预警邮件+短信通知
 * @Date 2022年6月16日, 0016 10:57
 * @Created by eury
 */
@Slf4j
@RequiredArgsConstructor
@Component
//@EnableScheduling
public class OrderPriceErrorTask {

    private static final Logger logger = LoggerFactory.getLogger(OrderPriceErrorTask.class);

    private static final String LOG_TAG="订单价格异常警告";//不可修改删除，已作为服务器监控关键字匹配警告通知
    private static final String LOG_TAG_NO="定时监控订单成交金额";

    @Resource
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private SendEmailFeignClient sendEmailFeignClient;

    @Autowired
    private TCouponFeignClient tCouponFeignClient;

    @Autowired
    private CouponFeignClient couponFeignClient;

//        @Scheduled(cron = "1 * * * * ?")
    @XxlJob("orderPriceErrorTask")
    public void orderPriceErrorTask(){
        logger.info(LOG_TAG_NO+"{}","-----开始执行定时监控订单成交金额------");
        Long startTime=System.currentTimeMillis();

        Date now = new Date();
        Date now_10 = new Date(now.getTime() - 600000*3); //30分钟前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = dateFormat.format(now);
        String nowTime_10 = dateFormat.format(now_10);

        List<OrderItemPriceErrorVO> errorVOList=orderItemMapper.listOrderItemsIsPay(now_10,now);

        log.info(LOG_TAG_NO+" 查询当前时间往前30分钟已支付订单  开始时间【{}】 结束时间【{}】 查询结果条数【{}】",nowTime_10,nowTime,errorVOList.size());

        if(CollectionUtil.isNotEmpty(errorVOList)){

            OrderPriceDiscountConfigVO config=feignShopConfig.getOrderPirceDiscountConfig();
            Integer discount=config!=null && StrUtil.isNotBlank(config.getDiscount()) ?Integer.parseInt(config.getDiscount()):3;
            log.info(LOG_TAG_NO+" 配置折扣比列【{}】 配置的订单过滤优惠券ids【{}】",discount,config.getCouponIds());

            List<Long> spuIdList = errorVOList.stream().map(OrderItemPriceErrorVO::getSpuId).collect(Collectors.toList());
            List<Long> skuIdList = errorVOList.stream().map(OrderItemPriceErrorVO::getSkuId).collect(Collectors.toList());
            List<Long> orderIds = errorVOList.stream().map(OrderItemPriceErrorVO::getOrderId).collect(Collectors.toList());

            log.info(LOG_TAG_NO+"订单数据 spu数量{} sku数量{}",spuIdList.size(),skuIdList.size());

            ServerResponseEntity<List<SpuVO>> responseSpu=spuFeignClient.listSpuNameBypBySpuIds(spuIdList);
            ServerResponseEntity<List<SkuVO>> responseSku=skuFeignClient.listSkuCodeBypByIds(skuIdList);

            //筛选出订单使用了配置的优惠券数据
            Map<Long, Long> couponOrderMaps=contentCouponUserOrder(config,errorVOList);
            log.info(LOG_TAG_NO+" 匹配到的订单过滤优惠券-->订单数据【{}】", JSON.toJSONString(couponOrderMaps));

            //订单关联优惠券信息
            ServerResponseEntity<List<TCouponUserOrderDetailVO>> responseOrderCoupon=couponFeignClient.postCouponListByOrderIds(orderIds);
            Map<Long, TCouponUserOrderDetailVO> orderCouponMaps=null;
            if(responseOrderCoupon.isSuccess() && CollectionUtil.isNotEmpty(responseOrderCoupon.getData())){
                orderCouponMaps = responseOrderCoupon.getData().stream().collect(Collectors.toMap(TCouponUserOrderDetailVO::getOrderNo, a -> a,(k1, k2)->k1));
                log.info("orderCouponMaps: {}", JSON.toJSONString(orderCouponMaps));
            }
            List<OrderSkuExcelLogVo> excelLogVos=new ArrayList<>();

            if( (responseSpu.isSuccess() && CollectionUtil.isNotEmpty(responseSpu.getData()))
                    && (responseSku.isSuccess() && CollectionUtil.isNotEmpty(responseSku.getData()))){

                Map<Long, SpuVO> spuMaps = responseSpu.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, a -> a,(k1, k2)->k1));
                Map<Long, SkuVO> skuMaps = responseSku.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId, a -> a,(k1, k2)->k1));

                for(OrderItemPriceErrorVO item:errorVOList){
                    if(Objects.nonNull(item.getActualTotal()) && skuMaps.containsKey(item.getSkuId())){
                        Long marketPriceFee=skuMaps.get(item.getSkuId()).getMarketPriceFee();
                        Long discountPrice =  marketPriceFee* discount / 10;//计算吊牌价3折
                        log.info(LOG_TAG_NO+" 订单编号【{}】 实际金额【{}】 吊牌价【{}】 折扣金额【{}】",
                                item.getOrderNumber(),
                                item.getActualTotal(),
                                marketPriceFee,
                                discountPrice);
                        if(item.getActualTotal()<discountPrice){//实际成交金额小于吊牌价3折，价格异常警告
                            boolean flag=true;
                            if(CollectionUtil.isNotEmpty(couponOrderMaps) && couponOrderMaps.containsKey(item.getOrderId())){
                                flag=false;//使用了配置的过滤的优惠券则不警告
                                log.info(LOG_TAG_NO+" 使用配置过滤的优惠券 订单编号【{}】 优惠券id【{}】",
                                        item.getOrderNumber(),
                                        couponOrderMaps.get(item.getOrderId()));
                            }
                            log.info(LOG_TAG_NO+" 是否满足告警日志输出【{}】",flag);
                            if(flag){
                                OrderSkuExcelLogVo orderSkuExcelLogVo=mapperFacade.map(item,OrderSkuExcelLogVo.class);
                                //订单优惠券信息
                                if(CollectionUtil.isNotEmpty(orderCouponMaps) && orderCouponMaps.containsKey(item.getOrderId())){
                                    orderSkuExcelLogVo.setCouponId(orderCouponMaps.get(item.getOrderId()).getCouponId().toString());
                                    orderSkuExcelLogVo.setCouponName(orderCouponMaps.get(item.getOrderId()).getCouponName());
                                    orderSkuExcelLogVo.setCouponAmount(PriceUtil.toDecimalPrice(orderCouponMaps.get(item.getOrderId()).getCouponAmount()).intValue()>0?PriceUtil.toDecimalPrice(orderCouponMaps.get(item.getOrderId()).getCouponAmount()).toString():"");
                                }
                                orderSkuExcelLogVo.setSpuCode(spuMaps.get(item.getSpuId()).getSpuCode());
                                orderSkuExcelLogVo.setSkuCode(skuMaps.get(item.getSkuId()).getSkuCode());
                                orderSkuExcelLogVo.setPriceCode(skuMaps.get(item.getSkuId()).getPriceCode());
                                orderSkuExcelLogVo.setMarketPriceFee(skuMaps.get(item.getSkuId()).getMarketPriceFee().toString());
                                orderSkuExcelLogVo.setPriceFee(item.getPrice().toString());
                                orderSkuExcelLogVo.setDiscountPrice(discountPrice.toString());
                                orderSkuExcelLogVo.setDiscount(discount.toString());
                                orderSkuExcelLogVo.setPayStatus("已支付");
                                excelLogVos.add(orderSkuExcelLogVo);
                            }
                        }
                    }
                }

                //生成日志文件
                logger.info(LOG_TAG_NO+"----------日志行数【{}】", excelLogVos.size());
                if(CollectionUtil.isNotEmpty(excelLogVos)){
                    String orderErrorExcel=null;
                    try {
                        CalcingDownloadRecordDTO downloadRecordDTO_sku=new CalcingDownloadRecordDTO();
                        downloadRecordDTO_sku.setDownloadTime(new Date());
                        downloadRecordDTO_sku.setFileName( DateUtil.format(new Date(),"yyyyMMddHHmmss") +OrderSkuExcelLogVo.EXCEL_NAME);
                        downloadRecordDTO_sku.setCalCount(excelLogVos.size());
                        downloadRecordDTO_sku.setOperatorName("系统定时任务");
                        downloadRecordDTO_sku.setOperatorNo("系统定时任务");
                        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO_sku);
                        if(serverResponseEntity.isSuccess()){
                            Long downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
                            String pathExport= SkqUtils.getExcelFilePath()+"/payorder_errorlog_"+SkqUtils.getExcelName()+".xls";
                            EasyExcel.write(pathExport, OrderSkuExcelLogVo.class).sheet(ExcelModel.SHEET_NAME).doWrite(excelLogVos);
                            String fileUrl=createLogAndUpload(downLoadHisId,pathExport);

                            orderErrorExcel=fileUrl;
                            //邮件通知
                            if(StrUtil.isNotBlank(fileUrl) && StrUtil.isNotBlank(config.getEmail())){
                                sendEmailFeignClient.sendEmailToFileUrl(fileUrl,config.getSubject(),config.getContent(),config.getEmail());
                            }

                        }

                    }catch (Exception e){
                        log.info("存入已支付订单价格异常日志 {} {}",e,e.getMessage());
                        e.printStackTrace();
                    }

                    //抛出异常警告
                    log.error(LOG_TAG+" 日志行数【{}】 日志文件 {}",excelLogVos.size(),orderErrorExcel);
                }

            }
        }

        logger.info(LOG_TAG_NO+"-----结束执行定时监控订单成交金额------耗时：{} ms", (System.currentTimeMillis() - startTime));
    }

    private Map<Long, Long> contentCouponUserOrder(OrderPriceDiscountConfigVO config,List<OrderItemPriceErrorVO> errorVOList){
        Map<Long, Long> couponOrderMaps=new HashMap<>();
        List<Long> orderIds = errorVOList.stream().map(OrderItemPriceErrorVO::getOrderId).collect(Collectors.toList());
        //订单使用的优惠券
        ServerResponseEntity<List<TCouponUserOrderVo>> responseCouponUser=tCouponFeignClient.selectByOrderIds(orderIds);
        if(responseCouponUser.isSuccess() && CollectionUtil.isNotEmpty(responseCouponUser.getData())){
            if(config!=null && StrUtil.isNotBlank(config.getCouponIds())){
                //订单优惠券分组
//                Map<Long, TCouponUserOrderVo> couponUserMaps = responseCouponUser.getData().stream().collect(Collectors.toMap(TCouponUserOrderVo::getOrderNo, a -> a,(k1, k2)->k1));

                //配置的优惠券id
                List<String> couponIds=Arrays.asList(config.getCouponIds().split(","));

                if(CollectionUtil.isNotEmpty(couponIds)){
                    Map<Long, Object> couponMaps = couponIds.stream().collect(Collectors.toMap(v->Long.parseLong(v), s->0));

                    responseCouponUser.getData().forEach(item->{
                        //优惠券包含当前订单
                        if(couponMaps.containsKey(item.getCouponId())){
                            if(!couponOrderMaps.containsKey(item.getOrderNo())){
                                couponOrderMaps.put(item.getOrderNo(),item.getCouponId());
                            }
                        }
                    });
                }
            }
        }
        return couponOrderMaps;
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

    public static void main(String[] s){
        Date now = new Date();
        Date now_10 = new Date(now.getTime() - 600000*3); //10分钟前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = dateFormat.format(now);
        String nowTime_10 = dateFormat.format(now_10);
        System.out.println(nowTime);
        System.out.println(nowTime_10);
    }
}
