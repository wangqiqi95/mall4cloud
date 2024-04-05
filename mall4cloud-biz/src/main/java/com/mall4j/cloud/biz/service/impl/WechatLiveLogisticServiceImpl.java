package com.mall4j.cloud.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.mall4j.cloud.api.delivery.feign.DeliveryCompanyFeignClient;
import com.mall4j.cloud.biz.wx.wx.api.live.SpuApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.api.biz.dto.livestore.response.LogisticCompany;
import com.mall4j.cloud.api.biz.dto.livestore.response.LogisticCompanyResponse;
import com.mall4j.cloud.biz.mapper.WechatLogisticsMappingMapper;
import com.mall4j.cloud.biz.model.WechatLogisticsMappingDO;
import com.mall4j.cloud.biz.service.WechatLiveLogisticService;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WechatLiveLogisticServiceImpl implements WechatLiveLogisticService {

    private static final String WX_LOGISTICS_LIST = "WX_LOGISTICS_LIST";

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private SpuApi spuApi;

    @Autowired
    private WechatLogisticsMappingMapper logisticsMappingMapper;

    @Autowired
    private DeliveryCompanyFeignClient deliveryCompanyFeignClient;

    /**
     * 查询基本物流列表
     *
     * @param query
     * @return
     */
    @Override
    public List<LiveLogisticsVO> baseList(String query) {
        // 尝试从缓存获取
        LogisticCompanyResponse logisticCompany = RedisUtil.get(WX_LOGISTICS_LIST, 3600, () -> {
            // 调用微信接口获取类目列表
            LogisticCompanyResponse logisticCompanyResponse = spuApi.logsticsList(wxConfig.getWxMaToken(), "{}");
            if (logisticCompanyResponse != null) {
                if (logisticCompanyResponse.getErrcode() != 0) {
                    log.error("调用微信接口获取物流公司列表异常 token={}, res={}", wxConfig.getWxMaToken(), JSON.toJSONString(logisticCompanyResponse));
                    return null;
                }
            }
            return logisticCompanyResponse;
        });

        if (logisticCompany == null) {
            throw new LuckException("调用微信接口获取物流公司列表异常");
        }

        List<LiveLogisticsVO> result = new ArrayList<>();
        for (LogisticCompany company : logisticCompany.getData()) {
            LiveLogisticsVO vo = new LiveLogisticsVO();
            vo.setLogisticsCode(company.getDeliveryId());
            vo.setLogisticsName(company.getDeliveryName());
            if (StringUtils.isNotBlank(query)) {
                if (company.getDeliveryId().contains(query) || company.getDeliveryName().contains(query)) {
                    result.add(vo);
                }
                continue;
            }
            result.add(vo);
        }

        return result;
    }

    /**
     * 查询物流映射列表
     *
     * @return .
     */
    @Override
    public PageVO<LiveLogisticsVO> list(PageDTO pageDTO) {
        PageVO<WechatLogisticsMappingDO> doPage = PageUtil.doPage(pageDTO, () -> logisticsMappingMapper.list());

        PageVO<LiveLogisticsVO> result = new PageVO<>();
        result.setPages(doPage.getPages());
        result.setTotal(doPage.getTotal());

        List<WechatLogisticsMappingDO> list = doPage.getList();
        List<LiveLogisticsVO> resultList = new ArrayList<>();
        for (WechatLogisticsMappingDO wechatLogisticsMappingDO : list) {
            LiveLogisticsVO vo = new LiveLogisticsVO();
            vo.setId(wechatLogisticsMappingDO.getId());
            vo.setLogisticsCode(wechatLogisticsMappingDO.getWxDeliveryId());
            vo.setLogisticsName(wechatLogisticsMappingDO.getWxDeliveryName());
            vo.setCoLogisticsCode(wechatLogisticsMappingDO.getDeliveryId());
            // 使用接口返回值替换 coLogisticsName
            ServerResponseEntity<String> deliveryCompany = deliveryCompanyFeignClient.getByDeliveryCompanyByCompanyId(Long.valueOf(wechatLogisticsMappingDO.getDeliveryId()));
            if (deliveryCompany == null || deliveryCompany.isFail()) {
                log.error("查询不到物流公司, id=" + wechatLogisticsMappingDO.getDeliveryId());
                vo.setCoLogisticsName(wechatLogisticsMappingDO.getWxDeliveryName());
            } else {
                vo.setCoLogisticsName(deliveryCompany.getData());
            }
            vo.setCoLogisticsPhone(wechatLogisticsMappingDO.getPhone());
            vo.setCreateTime(wechatLogisticsMappingDO.getCreateTime());
            vo.setUpdateTime(wechatLogisticsMappingDO.getUpdateTime());
            vo.setPhone(wechatLogisticsMappingDO.getPhone());
            vo.setDeliveryCompanyName(wechatLogisticsMappingDO.getWxDeliveryCompanyName());
            vo.setDeliveryCompanyId(wechatLogisticsMappingDO.getWxDeliveryCompanyId());
            resultList.add(vo);
        }
        result.setList(resultList);
        return result;
    }

    /**
     * 添加物流映射
     *
     * @param liveLogisticsVO .
     */
    @Override
    public void add(LiveLogisticsVO liveLogisticsVO) {
        if (liveLogisticsVO == null) {
            throw new LuckException("参数错误");
        }
        if (liveLogisticsVO.getCoLogisticsCode() == null) {
            throw new LuckException("CoLogisticsCode参数错误");
        }
        if(liveLogisticsVO.getDeliveryCompanyId() == null){
            throw new LuckException("视频号4.0物流公司编码不能为空");
        }
        if(liveLogisticsVO.getDeliveryCompanyName() == null){
            throw new LuckException("视频号4.0快递公司名称不能为空");
        }

        WechatLogisticsMappingDO entity = new WechatLogisticsMappingDO();
        entity.setDeliveryId(liveLogisticsVO.getCoLogisticsCode());
        entity.setWxDeliveryId(liveLogisticsVO.getLogisticsCode());
        entity.setWxDeliveryName(liveLogisticsVO.getLogisticsName());
        entity.setPhone(liveLogisticsVO.getPhone());
        entity.setCreateBy(AuthUserContext.get().getBizUserId());
        entity.setUpdateBy(AuthUserContext.get().getBizUserId());
        entity.setWxDeliveryCompanyId(liveLogisticsVO.getDeliveryCompanyId());
        entity.setWxDeliveryCompanyName(liveLogisticsVO.getDeliveryCompanyName());
        logisticsMappingMapper.insert(entity);
    }

    /**
     * 删除物流映射
     *
     * @param liveLogisticsVO .
     */
    @Override
    public void delete(LiveLogisticsVO liveLogisticsVO) {
        Preconditions.checkNotNull(liveLogisticsVO, "参数错误");
        Preconditions.checkNotNull(liveLogisticsVO.getId(), "参数错误");

        WechatLogisticsMappingDO entity = new WechatLogisticsMappingDO();
        entity.setId(liveLogisticsVO.getId());
        entity.setIsDeleted(1);
        entity.setUpdateBy(AuthUserContext.get().getBizUserId());
        entity.setUpdateTime(new Date());
        logisticsMappingMapper.update(entity);
    }

    @Override
    public LiveLogisticsVO getByDeliveryId(Long deliveryId) {
        return logisticsMappingMapper.getByDeliveryId(deliveryId);
    }

    @Override
    public WechatLogisticsMappingDO getByWechatDeliveryId(String wechatDeliveryId) {
        return logisticsMappingMapper.getByWechatDeliveryId(wechatDeliveryId);
    }

    @Override
    public WechatLogisticsMappingDO getDefualtWechatDelivery() {
        return logisticsMappingMapper.getDefualtWechatDelivery();
    }
    
    @Override
    public LiveLogisticsVO detail(Long id) {
        WechatLogisticsMappingDO wechatLogisticsMappingDO = logisticsMappingMapper.getByPrimary(id);
    
        LiveLogisticsVO vo = new LiveLogisticsVO();
        vo.setId(wechatLogisticsMappingDO.getId());
        vo.setLogisticsCode(wechatLogisticsMappingDO.getWxDeliveryId());
        vo.setLogisticsName(wechatLogisticsMappingDO.getWxDeliveryName());
        vo.setCoLogisticsCode(wechatLogisticsMappingDO.getDeliveryId());
        // 使用接口返回值替换 coLogisticsName
        ServerResponseEntity<String> deliveryCompany = deliveryCompanyFeignClient.getByDeliveryCompanyByCompanyId(Long.valueOf(wechatLogisticsMappingDO.getDeliveryId()));
        if (deliveryCompany == null || deliveryCompany.isFail()) {
            log.error("查询不到物流公司, id=" + wechatLogisticsMappingDO.getDeliveryId());
            vo.setCoLogisticsName(wechatLogisticsMappingDO.getWxDeliveryName());
        } else {
            vo.setCoLogisticsName(deliveryCompany.getData());
        }
        vo.setCoLogisticsPhone(wechatLogisticsMappingDO.getPhone());
        vo.setCreateTime(wechatLogisticsMappingDO.getCreateTime());
        vo.setUpdateTime(wechatLogisticsMappingDO.getUpdateTime());
        vo.setPhone(wechatLogisticsMappingDO.getPhone());
        vo.setDeliveryCompanyName(wechatLogisticsMappingDO.getWxDeliveryCompanyName());
        vo.setDeliveryCompanyId(wechatLogisticsMappingDO.getWxDeliveryCompanyId());
        return vo;
    }
    
    @Override
    public void update(LiveLogisticsVO liveLogisticsVO) {
        Preconditions.checkNotNull(liveLogisticsVO, "参数错误");
        Preconditions.checkNotNull(liveLogisticsVO.getId(), "参数错误");
        if (liveLogisticsVO.getCoLogisticsCode() == null) {
            throw new LuckException("CoLogisticsCode参数错误");
        }
        if(liveLogisticsVO.getDeliveryCompanyId() == null){
            throw new LuckException("视频号4.0物流公司编码不能为空");
        }
        if(liveLogisticsVO.getDeliveryCompanyName() == null){
            throw new LuckException("视频号4.0快递公司名称不能为空");
        }
    
        WechatLogisticsMappingDO entity = new WechatLogisticsMappingDO();
        entity.setId(liveLogisticsVO.getId());
        entity.setDeliveryId(liveLogisticsVO.getCoLogisticsCode());
        entity.setWxDeliveryId(liveLogisticsVO.getLogisticsCode());
        entity.setWxDeliveryName(liveLogisticsVO.getLogisticsName());
        entity.setPhone(liveLogisticsVO.getPhone());
        entity.setWxDeliveryCompanyId(liveLogisticsVO.getDeliveryCompanyId());
        entity.setWxDeliveryCompanyName(liveLogisticsVO.getDeliveryCompanyName());
        entity.setUpdateBy(AuthUserContext.get().getBizUserId());
        entity.setUpdateTime(new Date());
        entity.setIsDeleted(0);
        logisticsMappingMapper.update(entity);
    }
}
