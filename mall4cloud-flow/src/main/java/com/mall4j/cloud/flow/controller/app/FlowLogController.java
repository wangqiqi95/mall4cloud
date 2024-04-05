package com.mall4j.cloud.flow.controller.app;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.IpHelper;
import com.mall4j.cloud.flow.constant.FlowLogPageEnum;
import com.mall4j.cloud.flow.constant.FlowVisitEnum;
import com.mall4j.cloud.flow.dto.FlowLogDTO;
import com.mall4j.cloud.flow.service.FlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 流量分析—页面分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@RestController("appPageAnalyseController")
@RequestMapping("/ma/flow")
@Api(tags = "app流量分析—页面分析")
public class FlowLogController {

	private static final Logger log = LoggerFactory.getLogger(FlowLogController.class);

    @Autowired
    private FlowService flowService;

	@Autowired
	private RedisTemplate redisTemplate;

	private static Pattern SPU_ID_PATTERN = Pattern.compile("[0-9]+");

	@PostMapping
	@ApiOperation(value = "保存用户操作信息", notes = "保存用户操作信息")
	public ServerResponseEntity<Void> page(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId,@RequestBody FlowLogDTO flowLogDTO, HttpServletRequest request) {
		// 数据为空，打印日志
		if (checkDate(flowLogDTO)) {
			return ServerResponseEntity.success();
		}
		flowLogDTO.setShopId(storeId);
		flowLogDTO.setIp(IpHelper.getIpAddr());
		flowService.log(flowLogDTO);
		return ServerResponseEntity.success();
	}

	/**
	 * 数据是否错误
	 * @param flowLogDTO
	 * @return
	 */
	private Boolean checkDate(FlowLogDTO flowLogDTO) {
		if(Objects.isNull(flowLogDTO.getPageId()) || StrUtil.isBlank(flowLogDTO.getUuid()) ||
				Objects.isNull(flowLogDTO.getVisitType()) || Objects.isNull(flowLogDTO.getStep()) || Objects.isNull(flowLogDTO.getSystemType())){
			log.error("用户流量记录-基本数据不全：" + flowLogDTO.toString());
			return Boolean.TRUE;
		}
		if (Objects.equals(flowLogDTO.getVisitType(), FlowVisitEnum.SHOP_CAT.value())) {
			if (Objects.isNull(flowLogDTO.getBizData())) {
				log.error("用户流量记录-购物车:商品id为空" + flowLogDTO.toString());
				return Boolean.TRUE;
			}
			if (Objects.isNull(flowLogDTO.getNums())) {
				log.error("用户流量记录-加购:加购数量为空" + flowLogDTO.toString());
				return Boolean.TRUE;
			}
		}
		if (Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PROD_INFO.value())) {
			if (Objects.isNull(flowLogDTO.getBizData())) {
				log.error("用户流量记录-商品详情:商品id为空" + flowLogDTO.toString());
				return Boolean.TRUE;
			}
			if (!SPU_ID_PATTERN.matcher(flowLogDTO.getBizData()).matches()) {
				log.error("用户流量记录-商品详情:商品id数据错误" + flowLogDTO.toString());
				return Boolean.TRUE;
			}
		}
		if (Objects.equals(flowLogDTO.getPageId(), FlowLogPageEnum.PAY.value()) && Objects.isNull(flowLogDTO.getBizData())) {
			log.error("用户流量记录-支付:支付单号为空" + flowLogDTO.toString());
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@GetMapping("/user")
	@ApiOperation(value = "测试用户统计", notes = "测试用户统计")
	public ServerResponseEntity<Void> page() {
		flowService.statisticalUser();
		return ServerResponseEntity.success();
	}

	@GetMapping("/prod")
	@ApiOperation(value = "测试商品统计", notes = "测试商品统计")
	public ServerResponseEntity<Void> prod() {
		flowService.statisticalProduct();
		return ServerResponseEntity.success();
	}

//	@PostMapping("/ua/testsavelog")
//	@ApiOperation(value = "测试-保存用户操作信息", notes = "测试-保存用户操作信息")
//	public ServerResponseEntity<Void> testsavelog(@RequestParam String json) {
//		// 用户数据
//		if(StrUtil.isNotBlank(json)){
//			redisTemplate.boundListOps(CacheNames.FLOW_FLOW_DATA).rightPush(json);
//		}
//		return ServerResponseEntity.success();
//	}

//	@PostMapping("/ua/teststatisticalFlowData")
//	@ApiOperation(value = "测试-数据处理", notes = "测试-数据处理")
//	public ServerResponseEntity<Void> testsavelog(@RequestParam String json) {
//		//更新数据再统计
//		flowService.statisticalProduct();
//		return ServerResponseEntity.success();
//	}
}
