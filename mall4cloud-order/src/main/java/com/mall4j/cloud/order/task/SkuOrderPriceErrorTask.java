package com.mall4j.cloud.order.task;

import cn.hutool.core.collection.CollectionUtil;
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
import com.mall4j.cloud.order.vo.SkuOrderPriceErrorLogVo;
import com.mall4j.cloud.order.vo.SkuOrderPriceErrorLogVo;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单：1.当小程序实际付款价格 ＜ 吊牌价4折 并且 购买数量>50，
 * 按照22点至 8点，8点至16点，16点至22点三个时间段实时发送报警邮件出来。（4折、数量>50 可配置）
 * 2.显示字段信息：货号（到颜色级别）、吊牌价、销售价、销售数量；文件标题：XXX时间段 商品预警信息
 * @Date 2022年6月16日, 0016 10:57
 * @Created by eury
 */
@Slf4j
@Component
public class SkuOrderPriceErrorTask {

    private static final Logger logger = LoggerFactory.getLogger(SkuOrderPriceErrorTask.class);

    private static final String LOG_TAG="订单成交商品警告";//不可修改删除，已作为服务器监控关键字匹配警告通知
    private static final String LOG_TAG_NO="商品预警信息";

    @Resource
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private SendEmailFeignClient sendEmailFeignClient;

    /**
     * 手动执行传参数
     */
    @XxlJob("skuOrderPriceErrorTaskTop")
    public void skuOrderPriceErrorTaskTop(Date beginDate,Date endDate){
        String param = XxlJobHelper.getJobParam();
        if(StrUtil.isNotBlank(param) && param.split(",").length>1){
            String[] methodParams = param.split(",");
            Date begin=DateUtil.parse(methodParams[0],"yyyy-MM-dd HH:mm:ss");
            Date end=DateUtil.parse(methodParams[1],"yyyy-MM-dd HH:mm:ss");
            skuOrderPriceErrorTask(begin,end);
        }else if(Objects.nonNull(beginDate) && Objects.nonNull(endDate)){
            skuOrderPriceErrorTask(beginDate,endDate);
        }else{
            log.info(LOG_TAG_NO+" skuOrderPriceErrorTaskTop 时间参数为空执行默认当天开始时间到当前时间");
            beginDate=DateUtil.beginOfDay(new Date());
            endDate=new Date();
            skuOrderPriceErrorTask(beginDate,endDate);
        }
    }

    /**
     * 前日22点至当日8点
     */
    @XxlJob("skuOrderPriceErrorTask1")
    public void skuOrderPriceErrorTask1(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-10);
        //取整点
        Date begin=DateUtil.parse(DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH")+":00:00","yyyy-MM-dd HH:mm:ss");
        Date end=DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd HH")+":00:00","yyyy-MM-dd HH:mm:ss");
        skuOrderPriceErrorTask(begin,end);
    }

    /**
     * 当日：8点至16点
     */
    @XxlJob("skuOrderPriceErrorTask2")
    public void skuOrderPriceErrorTask2(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-8);
        //取整点
        Date begin=DateUtil.parse(DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH")+":00:00","yyyy-MM-dd HH:mm:ss");
        Date end=DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd HH")+":00:00","yyyy-MM-dd HH:mm:ss");
        skuOrderPriceErrorTask(begin,end);
    }

    /**
     * 当日：16点至22点
     */
    @XxlJob("skuOrderPriceErrorTask3")
    public void skuOrderPriceErrorTask3(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-6);
        //取整点
        Date begin=DateUtil.parse(DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH")+":00:00","yyyy-MM-dd HH:mm:ss");
        Date end=DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd HH")+":00:00","yyyy-MM-dd HH:mm:ss");
        skuOrderPriceErrorTask(begin,end);
    }

    public void skuOrderPriceErrorTask(Date beginDate,Date endDate){
        logger.info(LOG_TAG_NO+"{}","-----开始执行定时监控商品预警信息成交金额------");
        Long startTime=System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = dateFormat.format(endDate);
        String beginTime = dateFormat.format(beginDate);

        List<OrderItemPriceErrorVO> errorVOList=orderItemMapper.listOrderItemsIsPay(beginDate,endDate);

        log.info(LOG_TAG_NO+" 查询已支付订单  开始时间【{}】 结束时间【{}】 查询结果条数【{}】",beginTime,nowTime,errorVOList.size());

        if(CollectionUtil.isNotEmpty(errorVOList)){

            OrderPriceDiscountConfigVO config=feignShopConfig.getOrderPirceDiscountConfig();
            Integer priceDiscount=config!=null && StrUtil.isNotBlank(config.getPriceDiscount()) ?Integer.parseInt(config.getPriceDiscount()):4;
            Integer priceCodeCount=config!=null && StrUtil.isNotBlank(config.getPriceCodeCount()) ?Integer.parseInt(config.getPriceCodeCount()):50;
            log.info(LOG_TAG_NO+" 配置折扣比列【{}】 预警购买超限数量【{}】",priceDiscount,priceCodeCount);

            String excelTitle="订单低于"+priceDiscount+"折吊牌价成交且当天累计销售超过"+priceCodeCount+"的商品预警";

            List<Long> spuIdList = errorVOList.stream().map(OrderItemPriceErrorVO::getSpuId).collect(Collectors.toList());
            List<Long> skuIdList = errorVOList.stream().map(OrderItemPriceErrorVO::getSkuId).collect(Collectors.toList());
            List<Long> orderIds = errorVOList.stream().map(OrderItemPriceErrorVO::getOrderId).collect(Collectors.toList());

            log.info(LOG_TAG_NO+"订单数量{} spu数量{} sku数量{}",orderIds.size(),spuIdList.size(),skuIdList.size());

            ServerResponseEntity<List<SpuVO>> responseSpu=spuFeignClient.listSpuNameBypBySpuIds(spuIdList);
            ServerResponseEntity<List<SkuVO>> responseSku=skuFeignClient.listSkuCodeBypByIds(skuIdList);

            if( (responseSpu.isSuccess() && CollectionUtil.isNotEmpty(responseSpu.getData()))
                    && (responseSku.isSuccess() && CollectionUtil.isNotEmpty(responseSku.getData()))){

                Map<Long, SpuVO> spuMaps = responseSpu.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, a -> a,(k1, k2)->k1));
                Map<Long, SkuVO> skuMaps = responseSku.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId, a -> a,(k1, k2)->k1));

                Map<String, SkuOrderPriceErrorLogVo> errorLogVoMaps=new HashMap<>();

                for(OrderItemPriceErrorVO item:errorVOList){
                    if(Objects.nonNull(item.getActualTotal()) && skuMaps.containsKey(item.getSkuId())){
                        Long marketPriceFee=skuMaps.get(item.getSkuId()).getMarketPriceFee();
                        Long discountPrice =  marketPriceFee* priceDiscount / 10;//计算吊牌价*折扣
                        log.info(LOG_TAG_NO+" 订单编号【{}】 实际金额【{}】 下单商品售价【{}】 吊牌价【{}】 折扣金额【{}】 折扣等级【{}】",
                                item.getOrderNumber(),
                                item.getActualTotal(),
                                item.getPrice().toString(),
                                marketPriceFee,
                                discountPrice,
                                priceDiscount);
                        if(item.getActualTotal()<discountPrice){//实际成交金额小于吊牌价*折扣，价格异常警告
                            String key=skuMaps.get(item.getSkuId()).getPriceCode()+""+item.getPrice().toString();
                            if(errorLogVoMaps.containsKey(key)){
                                BigDecimal saleNum=errorLogVoMaps.get(key).getSaleNum().add(new BigDecimal(1));
                                errorLogVoMaps.get(key).setSaleNum(saleNum);
                            }else{
                                SkuOrderPriceErrorLogVo errorLogVo=new SkuOrderPriceErrorLogVo();
                                errorLogVo.setSpuCode(spuMaps.get(item.getSpuId()).getSpuCode());
                                errorLogVo.setPriceCode(skuMaps.get(item.getSkuId()).getPriceCode());
                                errorLogVo.setPriceFee(item.getPrice().toString());
                                errorLogVo.setMarketPriceFee(skuMaps.get(item.getSkuId()).getMarketPriceFee().toString());
                                errorLogVo.setSaleNum(new BigDecimal(1));
//                                errorLogVo.setStartTime(beginTime);
//                                errorLogVo.setEndTime(nowTime);
                                errorLogVoMaps.put(key,errorLogVo);
                            }
                        }
                    }
                }
                List<SkuOrderPriceErrorLogVo> excelLogVos=new ArrayList<>();
                for (Map.Entry<String, SkuOrderPriceErrorLogVo> entry : errorLogVoMaps.entrySet()) {
                    if(entry.getValue().getSaleNum().intValue()>priceCodeCount){
                        excelLogVos.add(entry.getValue());
                    }
                }
                //生成日志文件
                logger.info(LOG_TAG_NO+"----------日志行数【{}】", excelLogVos.size());
                if(CollectionUtil.isNotEmpty(excelLogVos)){
                    String orderErrorExcel=null;
                    try {
                        CalcingDownloadRecordDTO downloadRecordDTO_sku=new CalcingDownloadRecordDTO();
                        downloadRecordDTO_sku.setDownloadTime(new Date());
                        downloadRecordDTO_sku.setFileName( DateUtil.format(new Date(),"yyyyMMddHHmmss") +excelTitle);
                        downloadRecordDTO_sku.setCalCount(excelLogVos.size());
                        downloadRecordDTO_sku.setOperatorName("系统定时任务");
                        downloadRecordDTO_sku.setOperatorNo("系统定时任务");
                        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO_sku);
                        if(serverResponseEntity.isSuccess()){
                            Long downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
                            String pathExport= SkqUtils.getExcelFilePath()+"/"+DateUtil.format(beginDate,"yyyyMMddHH")+"-"
                                    +DateUtil.format(endDate,"yyyyMMddHH")+"-ordererror-sku.xls";
                            EasyExcel.write(pathExport, SkuOrderPriceErrorLogVo.class).sheet(ExcelModel.SHEET_NAME).doWrite(excelLogVos);
                            String fileUrl=createLogAndUpload(downLoadHisId,pathExport);

                            orderErrorExcel=fileUrl;
                            //邮件通知
                            if(StrUtil.isNotBlank(fileUrl) && StrUtil.isNotBlank(config.getEmail())){
                                sendEmailFeignClient.sendEmailToFileUrl(fileUrl,config.getSubject(),config.getContent(),config.getEmail());
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //抛出异常警告
                    log.error(LOG_TAG+" 日志文件 {}",orderErrorExcel);
                }
            }
        }
        logger.info(LOG_TAG_NO+"-----结束执行定时监控商品预警信息成交金额------耗时：{} ms", (System.currentTimeMillis() - startTime));
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-6);
        String time = new SimpleDateFormat("yyyy-MM-dd HH").format(calendar.getTime());
        time=time+":00:00";
        System.out.println(time);

        System.out.println("当前开始时间:"+DateUtil.format(DateUtil.beginOfDay(new Date()),"yyyy-MM-dd HH:mm:ss"));
    }
}
