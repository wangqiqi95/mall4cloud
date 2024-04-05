package com.mall4j.cloud.payment.service;


import com.mall4j.cloud.api.payment.bo.MemberOrderInfoBO;
import com.mall4j.cloud.api.payment.vo.OrderPayTypeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.dto.PayConfigDTO;
import com.mall4j.cloud.payment.vo.PayConfigVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PayConfigBizService {
	
	ServerResponseEntity<Void> saveOrUpdatePayConfig(PayConfigDTO param);
	
	ServerResponseEntity<PayConfigVO> detail();
	
	ServerResponseEntity<List<String>> exportUser(MultipartFile file);
	
	ServerResponseEntity<String> mobileToDownloadCenter(HttpServletResponse response);
	
	ServerResponseEntity<OrderPayTypeVO> queryOrderPayType(MemberOrderInfoBO memberOrderInfoBO);
}
