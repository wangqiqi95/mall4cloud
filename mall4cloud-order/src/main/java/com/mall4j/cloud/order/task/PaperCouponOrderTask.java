package com.mall4j.cloud.order.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.PaperCouponOrderVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.order.config.PaperCouponOrderConfigProperties;
import com.mall4j.cloud.order.mapper.OrderItemMapper;
import com.mall4j.cloud.order.vo.PaperCouponOrderExcelVO;
import com.mall4j.cloud.order.vo.PaperCouponOrderItemVO;
import com.xxl.job.core.handler.annotation.XxlJob;
import me.chanjar.weixin.cp.api.WxCpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 纸质券订单核销监控定时任务
 *
 */
@Component
public class PaperCouponOrderTask {
	private static final Logger logger = LoggerFactory.getLogger(PaperCouponOrderTask.class);
	
	@Autowired
	private TCouponFeignClient tCouponFeignClient;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private SpuFeignClient spuFeignClient;
	@Autowired
	private SkuFeignClient skuFeignClient;
	@Autowired
	private MinioUploadFeignClient minioUploadFeignClient;
	@Autowired
	private WxCpService wxCpService;
	@Autowired
	private PaperCouponOrderConfigProperties paperCouponOrderConfigProperties;
	
	@XxlJob("paperCouponOrderTask")
	public void paperCouponOrderTask() {
		
		logger.info("-----开始执行纸质券订单核销记录监控定时任务------");
		long currentTime = System.currentTimeMillis();
		
		String content;
		//处理00:00:00时,查询前一天的数据
		Date now = DateUtil.offset(DateTime.now(), DateField.SECOND, -5);
		Date beginTime = DateUtil.beginOfDay(now);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = dateFormat.format(now);
		String startTime = dateFormat.format(beginTime);
		
		//获取00点到当前时间的纸质券核销记录
		ServerResponseEntity<List<PaperCouponOrderVO>> responseEntity = tCouponFeignClient.listPaperCouponOrder(beginTime, now);
		
		if (responseEntity != null && responseEntity.isSuccess() ) {
			
			List<PaperCouponOrderVO> paperCouponOrderList = responseEntity.getData();
			
			logger.info("查询当天00点到当前时间的纸质券核销记录,开始时间【{}】,结束时间【{}】,查询结果条数【{}】", startTime, nowTime, paperCouponOrderList.size());
			
			if(CollectionUtil.isNotEmpty(paperCouponOrderList) ){
				List<Long> orderNoList = paperCouponOrderList.stream().map(PaperCouponOrderVO::getOrderNo).collect(Collectors.toList());
				
				//查询优惠券关联订单信息
				List<PaperCouponOrderItemVO> orderItemVOList = orderItemMapper.listPaperCouponOrderItemByIds(orderNoList);
				if (CollectionUtil.isNotEmpty(orderItemVOList)) {
					
					//聚合spuId和skuId
					List<Long> spuIdList = orderItemVOList.stream().map(PaperCouponOrderItemVO::getSpuId).collect(Collectors.toList());
					List<Long> skuIdList = orderItemVOList.stream().map(PaperCouponOrderItemVO::getSkuId).collect(Collectors.toList());
					
					ServerResponseEntity<List<SpuVO>> responseSpu = spuFeignClient.listSpuNameBypBySpuIds(spuIdList);
					ServerResponseEntity<List<SkuVO>> responseSku = skuFeignClient.listSkuCodeBypByIds(skuIdList);
					
					if ((!responseSpu.isFail() && CollectionUtil.isNotEmpty(responseSpu.getData()))
							&& (!responseSku.isFail() && CollectionUtil.isNotEmpty(responseSku.getData()))) {
						
						//导出结果集合
						List<PaperCouponOrderExcelVO> OrderExcelVOList = new ArrayList<>();
						
						Map<Long, SpuVO> spuMaps = responseSpu.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, a -> a, (k1, k2) -> k1));
						Map<Long, SkuVO> skuMaps = responseSku.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId, a -> a, (k1, k2) -> k1));
						Map<Long, PaperCouponOrderVO> couponMaps = paperCouponOrderList.stream().collect(Collectors.toMap(PaperCouponOrderVO::getOrderNo, a -> a, (k1, k2) -> k1));
						
						//遍历组装数据
						for (PaperCouponOrderItemVO itemVO : orderItemVOList) {
							SpuVO spuVO = spuMaps.get(itemVO.getSpuId());
							SkuVO skuVO = skuMaps.get(itemVO.getSkuId());
							PaperCouponOrderVO couponVO = couponMaps.get(itemVO.getOrderId());
							
							if (Objects.nonNull(spuVO) && Objects.nonNull(skuVO) && Objects.nonNull(couponVO)) {
								PaperCouponOrderExcelVO orderExcelVO = new PaperCouponOrderExcelVO();
								
								orderExcelVO.setOrderNumber(itemVO.getOrderNumber());
								orderExcelVO.setCount(itemVO.getCount());
								orderExcelVO.setCreateTime(itemVO.getCreateTime());
								orderExcelVO.setPayTime(itemVO.getPayTime());
								orderExcelVO.setPrice(PriceUtil.toDecimalPrice(itemVO.getPrice()));
								orderExcelVO.setActualTotal(PriceUtil.toDecimalPrice(itemVO.getActualTotal()));
								orderExcelVO.setShareReduce(PriceUtil.toDecimalPrice(itemVO.getShareReduce()));
								orderExcelVO.setSpuCode(spuVO.getSpuCode());
								orderExcelVO.setSkuCode(skuVO.getSkuCode());
								orderExcelVO.setPriceCode(skuVO.getPriceCode());
								orderExcelVO.setMarketPriceFee(PriceUtil.toDecimalPrice(skuVO.getMarketPriceFee()));
								orderExcelVO.setPayStatus("已支付");
								orderExcelVO.setCouponId(couponVO.getCouponId());
								orderExcelVO.setCouponName(couponVO.getName());
								orderExcelVO.setCouponCode(couponVO.getCouponCode());
								if (couponVO.getType() == 0) {
									orderExcelVO.setAmountLimitAmount( PriceUtil.toDecimalPrice(Objects.nonNull(couponVO.getReduceAmount())?couponVO.getReduceAmount():0L));
								} else {
									orderExcelVO.setAmountLimitAmount( PriceUtil.toDecimalPrice(Objects.nonNull(couponVO.getAmountLimitNum())?couponVO.getAmountLimitNum():0L));
								}
								
								OrderExcelVOList.add(orderExcelVO);
							}
						}
						
						//生成日志文件
						if (CollectionUtil.isNotEmpty(OrderExcelVOList)) {
							logger.info("----------日志行数【{}】----------", OrderExcelVOList.size());
							
							String fileUrl = null;
							try {
								String pathExport = SkqUtils.getExcelFilePath() + "/papercoupon_payorder_" + SkqUtils.getExcelName() + ".xls";
								EasyExcel.write(pathExport, PaperCouponOrderExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(OrderExcelVOList);
								
								//文件地址
								fileUrl = createLogAndUpload(pathExport);
								//企微通知
								if(StrUtil.isNotBlank(fileUrl)){
									DecimalFormat df = new DecimalFormat("0.00");
									StringBuilder sb = new StringBuilder();
									long totalPrice = orderItemVOList.stream().mapToLong(PaperCouponOrderItemVO::getActualTotal).sum();
									long totalShareReduce = orderItemVOList.stream().mapToLong(PaperCouponOrderItemVO::getShareReduce).sum();
									
									content="今日纸质券订单支付客户数(个):{payUserCount}\n" +
											"今日纸质券支付订单数(笔):{payOrderCount}\n" +
											"今日纸质券订单支付金额(元):{totalPrice}\n" +
											"今日纸质券订单优惠抵扣金额(元):{totalShareReduce}\n" +
											"纸质券订单核销记录详情文件:{fileUrl}\n";
									Map<String, Object> stringObjectMap = new HashMap<>();
									stringObjectMap.put("payUserCount",paperCouponOrderList.size());
									stringObjectMap.put("payOrderCount",paperCouponOrderList.size());
									stringObjectMap.put("totalPrice", df.format(PriceUtil.toDecimalPrice(totalPrice)));
									stringObjectMap.put("totalShareReduce", df.format(PriceUtil.toDecimalPrice(totalShareReduce)));
									stringObjectMap.put("fileUrl", fileUrl);
									sb.append(StrUtil.format(content, stringObjectMap, true));
									
									//企微机器人发送消息
									wxCpService.getGroupRobotService().sendText(paperCouponOrderConfigProperties.getWebhookurl(),sb.toString(), Lists.newArrayList(),Lists.newArrayList());
								}
								
							} catch (Exception e) {
								logger.error("查询纸质券订单核销记录执行失败{},{}",e,e.getMessage());
								e.printStackTrace();
							}
							
							//抛出警告
							logger.error( "PAPER_COUPON_WARNING 纸质券支付订单核销记录 日志行数【{}】 文件地址: {}", OrderExcelVOList.size(), fileUrl);
						}
					}
				}
			}else {
				content="今日暂无纸质券订单核销记录";
				try {
					wxCpService.getGroupRobotService().sendText(paperCouponOrderConfigProperties.getWebhookurl(),content, Lists.newArrayList(),Lists.newArrayList());
				} catch (Exception e) {
					logger.error("查询纸质券订单核销记录执行失败{},{}",e,e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		logger.info("-----结束执行纸质券订单核销记录监控定时任务,总耗时:{}ms------", (System.currentTimeMillis() - currentTime) );
	}
	
	private String createLogAndUpload(String pathExport){
		File file=null;
		try {
			String contentType = "application/vnd.ms-excel";
			file=new File(pathExport);
			FileInputStream is = new FileInputStream(file);
			MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),contentType, is);
			
			String originalFilename = multipartFile.getOriginalFilename();
			String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
			String mimoPath = "excel/" + time + "/" + originalFilename;
			ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
			if (responseEntity.isSuccess()) {
				logger.info("上传文件地址："+responseEntity.getData());
				// 删除临时文件
				if (file.exists()) {
					file.delete();
				}
			}
			return responseEntity.getData();
		}catch (Exception e){
			// 删除临时文件
			if (file!=null && file.exists()) {
				file.delete();
			}
			return null;
		}
	}
}


