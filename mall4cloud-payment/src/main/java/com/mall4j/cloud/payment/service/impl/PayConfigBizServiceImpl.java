package com.mall4j.cloud.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.docking.skq_sqb.config.SQBParams;
import com.mall4j.cloud.api.payment.bo.MemberOrderInfoBO;
import com.mall4j.cloud.api.payment.constant.PayAmountLimitType;
import com.mall4j.cloud.api.payment.constant.PayMemberLimitType;
import com.mall4j.cloud.api.payment.constant.PaySelectedLimitType;
import com.mall4j.cloud.api.payment.vo.OrderPayTypeVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.payment.dto.PayConfigDTO;
import com.mall4j.cloud.payment.model.PayConfig;
import com.mall4j.cloud.payment.model.PayConfigMember;
import com.mall4j.cloud.payment.service.PayConfigBizService;
import com.mall4j.cloud.payment.service.PayConfigService;
import com.mall4j.cloud.payment.service.PayConfigMemberService;
import com.mall4j.cloud.payment.vo.ExportUserToCreateConfigVO;
import com.mall4j.cloud.payment.vo.PayConfigMemberExcelVO;
import com.mall4j.cloud.payment.vo.PayConfigVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service("payConfigBizService")
public class PayConfigBizServiceImpl implements PayConfigBizService {
	
	private static final Integer SQB_ORDER_PAY_TYPE = 10;
	
	@Autowired
	private PayConfigService payConfigService;
	@Autowired
	private PayConfigMemberService payConfigMemberService;
	@Autowired
	private DownloadCenterFeignClient downloadCenterFeignClient;
	@Autowired
	private MinioUploadFeignClient minioUploadFeignClient;
	@Autowired
	private MapperFacade mapperFacade;
	@Autowired
	private SQBParams sqbParams;
	@Autowired
	private UserFeignClient userFeignClient;
	
	@Override
	public ServerResponseEntity<Void> saveOrUpdatePayConfig(PayConfigDTO param) {
		log.info("支付配置请求参数为: {}", JSON.toJSONString(param));

		PayConfig payConfig = BeanUtil.copyProperties(param, PayConfig.class);
		String username = AuthUserContext.get().getUsername();
		Long id = param.getId();
		payConfig.setUpdateTime(new Date());
		payConfig.setUpdateName(username);
		if (null == id){
			//没传入ID,先查询数据库有没有配置
			PayConfig dbPayConfig = payConfigService.getOne(new LambdaQueryWrapper<>());
			if(Objects.nonNull(dbPayConfig)){
				payConfig.setId(dbPayConfig.getId());
			}else {
				payConfig.setCreateName(username);
				payConfig.setCreateTime(new Date());
			}
		}
		payConfigService.saveOrUpdate(payConfig);
		
		//处理会员数据，做全量数据,先删除后全部新增
		payConfigMemberService.remove(new QueryWrapper<>());
		
		//指定会员配置
		if( Objects.equals(PaySelectedLimitType.SELECTED.value(), param.getSelectedMemberLimit())
				&& Objects.equals(PayMemberLimitType.LIMIT.value(), param.getMemberLimitType())){
			
			List<String> memberList = param.getMemberList().stream().distinct().filter( m -> Objects.equals(true, Pattern.matches(PrincipalUtil.MOBILE_REGEXP, m))).collect(Collectors.toList());
			
			//批量新增
			List<PayConfigMember> result = memberList.stream().map(phone -> {
				PayConfigMember payConfigMember = new PayConfigMember();
				payConfigMember.setCreateTime(new Date());
				payConfigMember.setPhone(phone);
				return payConfigMember;
			}).collect(Collectors.toList());
			
			payConfigMemberService.saveBatch(result);
		}
		
		return ServerResponseEntity.success();
	}
	
	@Override
	public ServerResponseEntity<PayConfigVO> detail() {
		
		//获取基础配置
		PayConfig payConfig = payConfigService.getOne(new LambdaQueryWrapper<>());
		if(Objects.isNull(payConfig)){
			return ServerResponseEntity.success();
		}
		PayConfigVO configVO = BeanUtil.copyProperties(payConfig, PayConfigVO.class);
		//获取会员配置
		if(Objects.equals(PaySelectedLimitType.SELECTED.value(), payConfig.getSelectedMemberLimit())
				&& Objects.equals(PayMemberLimitType.LIMIT.value(), payConfig.getMemberLimitType())){
			
			List<PayConfigMember> PayConfigMemberList = payConfigMemberService.list();
			if(CollectionUtil.isNotEmpty(PayConfigMemberList)){
				List<String> memberList = PayConfigMemberList.stream().map(PayConfigMember::getPhone).collect(Collectors.toList());
				configVO.setMemberList(memberList);
				configVO.setLimitMemberCount(memberList.size());
			}
		}
		
		return ServerResponseEntity.success(configVO);
	}
	
	@Override
	public ServerResponseEntity<List<String>> exportUser(MultipartFile file) {
		log.info("导入文件为：{}", file);
		if (file == null) {
			throw new LuckException("导入文件不能为空！");
		}
		
		// 1.获取上传文件输入流
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
		} catch (Exception e) {
			log.error("获取输入流异常");
			e.printStackTrace();
		}
		
		//调用 hutool 方法读取数据 默认调用第一个sheet
		ExcelReader excelReader = ExcelUtil.getReader(inputStream);
		
		//校验模板的正确性
		List<Object> objects = excelReader.readRow(0);
		log.info("模板表头信息：{}", JSONObject.toJSONString(objects));
		
		//表头不对的时候进行提示
		if(!"会员手机号".equals(objects.get(0))){
			throw new LuckException("请输入正确的【会员手机号】表格列名");
		}
		
		//列名和对象属性名一致
		excelReader.addHeaderAlias("会员手机号", "phone");
		
		//读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
		List<ExportUserToCreateConfigVO> importObjects = excelReader.readAll(ExportUserToCreateConfigVO.class);
		List<String> phoneList = importObjects.stream()
				.map(ExportUserToCreateConfigVO::getPhone)
				.collect(Collectors.toList());
		
		return ServerResponseEntity.success(phoneList);
	}
	
	@Override
	public ServerResponseEntity<String> mobileToDownloadCenter(HttpServletResponse response) {
		FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
		try {
			//拼接文件名称
			StringBuffer fileName = new StringBuffer();
			String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
			fileName.append("支付配置-");
			fileName.append(date);
			fileName.append("-生效会员名单导出");
			
			//获取下载中心文件对应ID
			finishDownLoadDTO = wrapperDownLoadCenterParam(fileName.toString());
			
			if (Objects.isNull(finishDownLoadDTO)){
				return ServerResponseEntity.showFailMsg("文件下载失败，请重试");
			}
			
			//获取会员名单
			List<PayConfigMember> PayConfigMemberList = payConfigMemberService.list();
			
			//校验是否存在可导出数据
			if (checkDownloadData(PayConfigMemberList, finishDownLoadDTO)){
				return ServerResponseEntity.showFailMsg("无会员名单数据可导出");
			}
			
			List<PayConfigMemberExcelVO> memberExcelVOList = mapperFacade.mapAsList(PayConfigMemberList, PayConfigMemberExcelVO.class);
			
			String excelPath = createExcelFile(memberExcelVOList, fileName.toString());
			
			//填充数据到下载中心
			File file = new File(excelPath);
			if(file.isFile()){
				String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				FileInputStream fileInputStream = new FileInputStream(excelPath);
				String contentType = "application/vnd.ms-excel";
				MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
						contentType, fileInputStream);
				String originalFilename = multipartFile.getOriginalFilename();
				String minioPath = "excel/" + time + "/" + originalFilename;
				ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, minioPath, multipartFile.getContentType());
				if(responseEntity.isSuccess()){
					log.info("---ExcelUploadService---" + responseEntity.toString());
					//下载中心记录
					finishDownLoadDTO.setCalCount(PayConfigMemberList.size());
					finishDownLoadDTO.setStatus(1);
					finishDownLoadDTO.setFileUrl(responseEntity.getData());
					finishDownLoadDTO.setRemarks("导出成功");
					downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
				}
				//删除本地临时文件
				cn.hutool.core.io.FileUtil.del(excelPath);
			}
			
			return ServerResponseEntity.success("操作成功，请转至下载中心下载");
		}catch (Exception e){
			log.error("支付配置会员名单导出异常 message is: "+ e);
			
			//下载中心记录
			if(finishDownLoadDTO!=null){
				finishDownLoadDTO.setStatus(2);
				finishDownLoadDTO.setRemarks("支付配置会员数据导出，excel生成zip失败");
				downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
			}
			return ServerResponseEntity.showFailMsg("操作失败");
		}
	}
	
	@Override
	public ServerResponseEntity<OrderPayTypeVO> queryOrderPayType(MemberOrderInfoBO memberOrderInfoBO) {
		
		log.info("会员订单校验支付类型,方法入参为：{}", JSON.toJSONString(memberOrderInfoBO));
		
		//读取nacos配置上的支付类型值(默认支付类型)
		Integer orderPayType = sqbParams.getOrderPayType();
		OrderPayTypeVO orderPayTypeVO = new OrderPayTypeVO();
		
		//获取支付配置信息,不配置/配置无限制则默认使用微信支付
		PayConfig dbPayConfig = payConfigService.getOne(new LambdaQueryWrapper<>());
		
		log.info("默认的支付类型为：【{}】 ,获取到的支付配置信息为：{}" ,orderPayType ,JSON.toJSONString(dbPayConfig));
		
		if(Objects.nonNull(dbPayConfig)){
			
			if( Objects.equals(PaySelectedLimitType.SELECTED.value(), dbPayConfig.getSelectedAmountLimit())
					&& Objects.equals(PaySelectedLimitType.SELECTED.value(), dbPayConfig.getSelectedMemberLimit()) ){
				
				//金额限制和指定会员限制同时生效
				if(Objects.equals(PayAmountLimitType.AMOUNT_LIMIT.value(), dbPayConfig.getAmountLimitType())
						&& Objects.equals(PayMemberLimitType.LIMIT.value(), dbPayConfig.getMemberLimitType()) ){
					
					//满足金额限制,进行会员限制校验
					if(memberOrderInfoBO.getActualTotal() >= dbPayConfig.getAmountLimitNum().multiply(new BigDecimal(100)).longValue()){
						
						//获取会员数据
						ServerResponseEntity<UserApiVO> userApiVOServerResponseEntity = userFeignClient.getUserData(memberOrderInfoBO.getUserId());
						if (Objects.isNull(userApiVOServerResponseEntity)  || userApiVOServerResponseEntity.isFail() || Objects.isNull(userApiVOServerResponseEntity.getData())) {
							return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
						}
						UserApiVO userApiVO = userApiVOServerResponseEntity.getData();
						
						PayConfigMember member = payConfigMemberService.getOne(new LambdaQueryWrapper<PayConfigMember>()
								.eq(PayConfigMember::getPhone, userApiVO.getPhone()));
						
						if(Objects.nonNull(member)){
							//满足金额限制设置为收钱吧支付类型
							orderPayType = SQB_ORDER_PAY_TYPE;
						}{
							log.info("会员：【{}】,不满足指定会员限制条件",userApiVO.getPhone());
						}
					}
				}
				
				//不限金额和不限会员
				if( Objects.equals(PayAmountLimitType.NOT_LIMIT.value(), dbPayConfig.getAmountLimitType())
						&& Objects.equals(PayMemberLimitType.NOT_LIMIT.value(), dbPayConfig.getMemberLimitType()) ){
					orderPayType = SQB_ORDER_PAY_TYPE;
				}
				
				//限制金额，不限会员
				if( Objects.equals(PayAmountLimitType.AMOUNT_LIMIT.value(), dbPayConfig.getAmountLimitType())
						&& Objects.equals(PayMemberLimitType.NOT_LIMIT.value(), dbPayConfig.getMemberLimitType()) ){
					
					if(memberOrderInfoBO.getActualTotal() >= dbPayConfig.getAmountLimitNum().multiply(new BigDecimal(100)).longValue() ){
						orderPayType = SQB_ORDER_PAY_TYPE;
					}
				}
				
				//不限金额，指定会员
				if( Objects.equals(PayAmountLimitType.NOT_LIMIT.value(), dbPayConfig.getAmountLimitType())
						&& Objects.equals(PayMemberLimitType.LIMIT.value(), dbPayConfig.getMemberLimitType()) ){
					//获取会员数据
					ServerResponseEntity<UserApiVO> userApiVOServerResponseEntity = userFeignClient.getUserData(memberOrderInfoBO.getUserId());
					if (Objects.isNull(userApiVOServerResponseEntity)  || userApiVOServerResponseEntity.isFail() || Objects.isNull(userApiVOServerResponseEntity.getData())) {
						return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
					}
					UserApiVO userApiVO = userApiVOServerResponseEntity.getData();
					
					PayConfigMember member = payConfigMemberService.getOne(new LambdaQueryWrapper<PayConfigMember>()
							.eq(PayConfigMember::getPhone, userApiVO.getPhone()));
					
					if(Objects.nonNull(member)){
						//满足指定会员限制设置为收钱吧支付类型
						orderPayType = SQB_ORDER_PAY_TYPE;
					}{
						log.info("会员：【{}】,不满足指定会员限制条件",userApiVO.getPhone());
					}
				}
				
			}
			
			//选中金额限制，未选中会员限制
			if(Objects.equals(PaySelectedLimitType.SELECTED.value(), dbPayConfig.getSelectedAmountLimit())
					&& Objects.equals(PaySelectedLimitType.UNSELECTED.value(), dbPayConfig.getSelectedMemberLimit()) ){
				
				//满额
				if(Objects.equals(PayAmountLimitType.AMOUNT_LIMIT.value(), dbPayConfig.getAmountLimitType())){
					
					//满足金额限制,进行会员限制校验
					if(memberOrderInfoBO.getActualTotal() >= dbPayConfig.getAmountLimitNum().multiply(new BigDecimal(100)).longValue()){
						//满足金额限制设置为收钱吧支付类型
						orderPayType = SQB_ORDER_PAY_TYPE;
					}
				}
				
				//不限
				if( Objects.equals(PayAmountLimitType.NOT_LIMIT.value(), dbPayConfig.getAmountLimitType()) ){
					orderPayType = SQB_ORDER_PAY_TYPE;
				}
			}
			
			//选中会员限制，未选中金额限制
			if( Objects.equals(PaySelectedLimitType.SELECTED.value(), dbPayConfig.getSelectedMemberLimit())
					&& Objects.equals(PaySelectedLimitType.UNSELECTED.value(), dbPayConfig.getSelectedAmountLimit()) ){
				
				//指定会员
				if( Objects.equals(PayMemberLimitType.LIMIT.value(), dbPayConfig.getMemberLimitType()) ){
					
					//获取会员数据
					ServerResponseEntity<UserApiVO> userApiVOServerResponseEntity = userFeignClient.getUserData(memberOrderInfoBO.getUserId());
					if (Objects.isNull(userApiVOServerResponseEntity)  || userApiVOServerResponseEntity.isFail() || Objects.isNull(userApiVOServerResponseEntity.getData())) {
						return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
					}
					UserApiVO userApiVO = userApiVOServerResponseEntity.getData();
					
					PayConfigMember member = payConfigMemberService.getOne(new LambdaQueryWrapper<PayConfigMember>()
							.eq(PayConfigMember::getPhone, userApiVO.getPhone()));
					
					if(Objects.nonNull(member)){
						//满足指定会员限制设置为收钱吧支付类型
						orderPayType = SQB_ORDER_PAY_TYPE;
					}else {
						log.info("会员手机号：【{}】,不满足指定会员限制条件",userApiVO.getPhone());
					}
				}
				
				 //不限会员
				if( Objects.equals(PayMemberLimitType.NOT_LIMIT.value(), dbPayConfig.getMemberLimitType()) ){
					orderPayType = SQB_ORDER_PAY_TYPE;
				}
			}
		}
		log.info("结束获取订单支付类型,会员Id：【{}】,支付类型：【{}】",memberOrderInfoBO.getUserId() , orderPayType);
		
		orderPayTypeVO.setOrderPayType(orderPayType);
		return ServerResponseEntity.success(orderPayTypeVO);
	}
	
	public FinishDownLoadDTO wrapperDownLoadCenterParam(String fileName){
		CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
		downloadRecordDTO.setDownloadTime(new Date());
		downloadRecordDTO.setFileName(fileName);
		downloadRecordDTO.setCalCount(0);
		downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
		downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
		ServerResponseEntity<Long> serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
		
		if(serverResponseEntity.isSuccess()){
			Long downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
			FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
			finishDownLoadDTO.setId(downLoadHisId);
			finishDownLoadDTO.setFileName(fileName);
			return finishDownLoadDTO;
		}
		
		return null;
	}
	
	public Boolean checkDownloadData(List<PayConfigMember> PayConfigMemberList, FinishDownLoadDTO finishDownLoadDTO){
		if (CollectionUtil.isEmpty(PayConfigMemberList)){
			finishDownLoadDTO.setRemarks("无会员名单数据导出");
			finishDownLoadDTO.setStatus(2);
			downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
			log.error("无会员名单数据导出");
			return true;
		}
		return false;
	}
	
	public String createExcelFile(List<PayConfigMemberExcelVO> recordExcelVOList, String  fileName){
		String file=null;
		try {
			String pathExport= SkqUtils.getExcelFilePath()+"/"+fileName+".xlsx";
			EasyExcel.write(pathExport, PayConfigMemberExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(recordExcelVOList);
			return pathExport;//返回文件生成路径
		}catch (Exception e){
			log.error("支付配置会员名单导出创建文件异常 message is: "+ e);
		}
		return file;
	}
}
