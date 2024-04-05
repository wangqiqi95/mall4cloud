package com.mall4j.cloud.payment.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.dto.PayConfigDTO;
import com.mall4j.cloud.payment.service.PayConfigBizService;
import com.mall4j.cloud.payment.vo.PayConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("payConfigController")
@RequestMapping("/p/payConfig")
@Api(tags = "支付配置管理")
public class PayConfigController {

	@Autowired
	private PayConfigBizService payConfigBizService;
	
	@GetMapping("/detail")
	@ApiOperation(value = "获取支付配置详情",notes = "获取支付配置详情")
	public ServerResponseEntity<PayConfigVO> detail(){
		return payConfigBizService.detail();
	}
	
	@PostMapping("/saveOrUpdate")
	@ApiOperation(value = "新增或修改支付配置",notes = "新增或修改支付配置")
	public ServerResponseEntity<Void> saveOrUpdate(@RequestBody PayConfigDTO param) {
		return payConfigBizService.saveOrUpdatePayConfig(param);
	}
	
	@PostMapping("/exportUser")
	@ApiOperation(value = "导入会员",notes = "导入会员")
	public ServerResponseEntity<List<String>> exportUser(@RequestParam("file") MultipartFile file){
		return payConfigBizService.exportUser(file);
	}
	
	@GetMapping("/mobileToDownloadCenter")
	@ApiOperation(value = "导出生效会员名单",notes = "导出生效会员名单到下载中心")
	public ServerResponseEntity<String> mobileToDownloadCenter(HttpServletResponse response){
		return payConfigBizService.mobileToDownloadCenter(response);
	}
}
