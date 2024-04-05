package com.mall4j.cloud.docking.skq_erp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mall4j.cloud.api.docking.skq_erp.dto.*;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopSpuStockVO;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdShopService;
import com.mall4j.cloud.docking.utils.StdClients;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service("stdShopService")
public class StdShopServiceImpl implements IStdShopService {

	private static final String SHOP_URI= "/api/ip/std/service";
	private static final String SHOP_URI_DATA= "/api/ip/std/data/service";
	private static final String SHOP_METHOD= "std.universal.shop";
	private static final String SYNC_STOCK_METHOD= "std.universal.syn.stock";

	private static final String GET_SHOP_STOCK_METHOD = "std.universal.storage";

	private static final String STORE_INTEGRAL_RANK="/queryStoreIntegralRank";

	private final static String REDIS_STORE_INTEGRAL_RANK="mall4cloud_docking:STORE_INTEGRAL_RANK_";


	@Resource
	private RedisTemplate redisTemplate;
	/**
	 * 方法描述：门店档案
	 *
	 * @param getShopInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult < com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo>>
	 * @date 2022-01-09 11:07:38
	 */
	@Override
	public ServerResponseEntity<StdPageResult<ShopInfoVo>> getShopInfo(GetShopInfoDto getShopInfoDto) {
		if (null == getShopInfoDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		String s = StdClients.clients().postStd(SHOP_URI, SHOP_METHOD, JSON.toJSONString(getShopInfoDto));
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用中台查询门店信息接口无响应");
			return fail;
		}

		JSONObject resp = JSON.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			String data = resp.getString("data");
			StdPageResult<ShopInfoVo> shopInfoVoStdPageResult = JSONObject.parseObject(data, new TypeReference<StdPageResult<ShopInfoVo>>() {
			});
			return ServerResponseEntity.success(shopInfoVoStdPageResult);
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "查询门店信息失败" : errorMsg);
		return fail;
	}

	/**
	 * 售卖店铺仓库推送
	 * @param pushStoreDtos
	 * @return
	 */
	@Override
	public ServerResponseEntity<String> pushShops(List<PushStoreDto> pushStoreDtos) {
		if (null == pushStoreDtos) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		JSONObject body = new JSONObject();
		body.put("data", pushStoreDtos);
		String s = StdClients.clients().postStd(SHOP_URI_DATA, SYNC_STOCK_METHOD, body.toJSONString());
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("售卖店铺仓库推送接口无响应");
			return fail;
		}

		JSONObject resp = JSON.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			String data = resp.getString("data");
			return ServerResponseEntity.success(resp.toJSONString());
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "售卖店铺仓库推送失败" : errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity<List<ShopSpuStockVO>> getStoresByShops(GetStoresByShopsDto getStoresByShopsDto) {

//		List<ShopSpuStockVO> shopSpuStockVOList = new ArrayList<>();

		if (Objects.isNull(getStoresByShopsDto)) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		String s = StdClients.clients().postStd(SHOP_URI, GET_SHOP_STOCK_METHOD, JSON.toJSONString(getStoresByShopsDto));
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("获取店铺对应的商品的库存接口调用失败");
			return fail;
		}

		JSONObject resp = JSON.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			String data = resp.getString("data");
			List<ShopSpuStockVO> shopSpuStockVOList = JSONObject.parseArray(data, ShopSpuStockVO.class);

			return ServerResponseEntity.success(shopSpuStockVOList);

		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "查询门店信息失败" : errorMsg);
		return fail;
	}

    @Override
    public ServerResponseEntity<List<StoreIntegralRankRespDto>> queryStoreIntegralRank(StoreIntegralRankReqDto reqDto) {
		String redisKey=REDIS_STORE_INTEGRAL_RANK +reqDto.getPageSize()+ reqDto.getMonth();
		BoundValueOperations<String,List<StoreIntegralRankRespDto>> boundValueOperations = redisTemplate.boundValueOps(redisKey);
		List<StoreIntegralRankRespDto> redisStoreIntegralRankRespDtos = boundValueOperations.get();
		if(CollectionUtils.isNotEmpty(redisStoreIntegralRankRespDtos)){
			return ServerResponseEntity.success(redisStoreIntegralRankRespDtos);
		}

		String s = StdClients.clients().postStd(SHOP_URI+STORE_INTEGRAL_RANK, null, JSON.toJSONString(reqDto));
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用中台积分抵现排行接口无响应");
			return fail;
		}
		JSONObject resp = JSON.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			String data = resp.getString("data");
			List<StoreIntegralRankRespDto> storeIntegralRankRespDtos = JSON.parseArray(data, StoreIntegralRankRespDto.class);
			boundValueOperations.set(storeIntegralRankRespDtos);
			return ServerResponseEntity.success(storeIntegralRankRespDtos);
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "查询积分抵现排行信息失败" : errorMsg);
		return fail;
    }
}
