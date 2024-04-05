package com.mall4j.cloud.delivery.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryInfoVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryItemInfoVO;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.common.bean.AliQuick;
import com.mall4j.cloud.common.bean.Quick100;
import com.mall4j.cloud.common.bean.QuickBird;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.delivery.bo.DeliveryAliInfoBO;
import com.mall4j.cloud.delivery.bo.DeliveryAliItemInfoBO;
import com.mall4j.cloud.delivery.bo.DeliveryHundredInfoBO;
import com.mall4j.cloud.delivery.bo.DeliveryHundredItemInfoBO;
import com.mall4j.cloud.delivery.constant.DeliveryConstant;
import com.mall4j.cloud.delivery.mapper.DeliveryCompanyMapper;
import com.mall4j.cloud.delivery.model.DeliveryCompany;
import com.mall4j.cloud.delivery.service.DeliveryCompanyService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物流公司
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
@Slf4j
@Service
public class DeliveryCompanyServiceImpl implements DeliveryCompanyService {

    private final Logger logger = LoggerFactory.getLogger(DeliveryCompanyServiceImpl.class);
    @Autowired
    private DeliveryCompanyMapper deliveryCompanyMapper;

    @Autowired
    private FeignShopConfig shopConfig;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public List<DeliveryCompanyVO> list() {
        return deliveryCompanyMapper.list();
    }

    @Override
    public DeliveryCompanyVO getByDeliveryCompanyId(Long deliveryCompanyId) {
        return deliveryCompanyMapper.getByDeliveryCompanyId(deliveryCompanyId);
    }

    @Override
    public DeliveryCompanyVO getByDeliveryCompanyName(String name) {
        return deliveryCompanyMapper.getByDeliveryCompanyName(name);
    }

    @Override
    public void save(DeliveryCompany deliveryCompany) {
        checkDeliveryCompany(deliveryCompany);

        deliveryCompanyMapper.save(deliveryCompany);
    }

    private void checkDeliveryCompany(DeliveryCompany deliveryCompany) {
        //更新校验
        Integer companyCount = deliveryCompanyMapper.countName(deliveryCompany.getName(), null);
        if (companyCount > 0 && deliveryCompany.getDeliveryCompanyId() == null) {
            throw new LuckException("快递公司已存在");
        } else if (companyCount > 1) {
            throw new LuckException("快递公司已存在");
        }
        if (StrUtil.isBlank(deliveryCompany.getAliNo()) && StrUtil.isBlank(deliveryCompany.getBirdNo()) && StrUtil.isBlank(deliveryCompany.getHundredNo())) {
            throw new LuckException("必须填写一个或多个物流公司编号");
        }
    }

    @Override
    public void update(DeliveryCompany deliveryCompany) {
        checkDeliveryCompany(deliveryCompany);
        deliveryCompanyMapper.update(deliveryCompany);
    }

    @Override
    public void deleteById(Long deliveryCompanyId) {
        deliveryCompanyMapper.deleteById(deliveryCompanyId);
    }

    @Override
    public PageVO<DeliveryCompanyVO> page(PageDTO pageDTO, DeliveryCompanyDTO deliveryCompanyDTO) {
        return PageUtil.doPage(pageDTO, () -> deliveryCompanyMapper.listBySearch(deliveryCompanyDTO));
    }

    @Override
    public DeliveryInfoVO query(Long dvyId, String expNo, String consigneeMobile) throws UnsupportedEncodingException {
        DeliveryCompanyVO delivery = getByDeliveryCompanyId(dvyId);
        if (Objects.nonNull(delivery)) {
            // 快递鸟查询
            QuickBird quickBird = shopConfig.getQuickBird();
            if (Objects.nonNull(quickBird) && BooleanUtil.isTrue(quickBird.getIsOpen())) {
                return queryByQuickBird(delivery.getBirdNo(), expNo, quickBird, consigneeMobile);
            }

            // 快递100查询
            Quick100 quick100 = shopConfig.getQuick100();
            if (Objects.nonNull(quick100) && BooleanUtil.isTrue(quick100.getIsOpen())) {
                return queryByQuick100(delivery.getHundredNo(), expNo, quick100, consigneeMobile);
            }

            // 阿里快递查询
            AliQuick aliQuickConfig = shopConfig.getAliQuickConfig();
            if (Objects.nonNull(aliQuickConfig) && BooleanUtil.isTrue(aliQuickConfig.getIsOpen())) {
                return queryByAliQuick(delivery.getAliNo(), expNo, aliQuickConfig, consigneeMobile);
            }
        }
        return new DeliveryInfoVO();
    }

    private DeliveryInfoVO queryByQuickBird(String birdNo, String expNo, QuickBird quickBird, String consigneeMobile) throws UnsupportedEncodingException {
        String requestData = "{" +
                "'OrderCode': ''," +
                "'ShipperCode': '" + birdNo + "'," +
                "'LogisticCode': '" + expNo + "'," +
                // 快递为顺丰时需要传寄件人/收件人手机号后四位，为其他快递时，可不填或保留字段，不可传值
                "'CustomerName': '" + (Objects.equals(birdNo, QuickBird.SF_CODE) ? getLastFourOfMobile(consigneeMobile) : "") + "'," +
                "}";
        Map<String, Object> params = new HashMap<String, Object>(16);
        params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        params.put("EBusinessID", quickBird.geteBusinessID());
        String dataSign = Base64.encode(new Digester(DigestAlgorithm.MD5).digestHex(requestData + quickBird.getAppKey()), "UTF-8");
        params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
        params.put("RequestType", "1002");
        params.put("DataType", "2");
        String repJson = HttpUtil.post(quickBird.getReqUrl(), params);
        log.info("queryByQuickBird ,param:{}，返回结果：{}",JSONObject.toJSONString(params),repJson);
        return Json.parseObject(repJson, DeliveryInfoVO.class);
    }

//    public static void main(String[] strings){
//        Map<String, Object> paramMap = new HashMap<>(16);
//        paramMap.put("com", "yuantong");
//        paramMap.put("num", "YT2184932098001");
//        paramMap.put("from", "");
//        paramMap.put("to", "");
//        paramMap.put("resultv2", 0);
//        paramMap.put("show", "0");
////        if (Objects.equals(expCode, Quick100.SF_CODE) || Objects.equals(expCode, Quick100.FENGWANG_CODE)) {
////            // 快递100中顺丰速运和丰网速运必填收/寄件人手机号码，其他快递公司选填
////            paramMap.put("phone", consigneeMobile);
////        }
//        paramMap.put("order", "desc");
//        String param = JSON.toJSONString(paramMap);
//        String key="jXHaMbzu9153";
//        String customer="4DD90663B72D7E075A95881D4479F3FD";
//        System.out.println("param--->"+param);
////        customer=new Digester(DigestAlgorithm.MD5).digestHex(customer, "UTF-8").toUpperCase();
//        System.out.println("customer---->"+customer);
//        String sign = new Digester(DigestAlgorithm.MD5).digestHex(param + key + customer, "UTF-8").toUpperCase();
//        System.out.println(sign);
//    }


    /**
     * 通过快递100查询
     *
     * @param expCode  物流公司编码
     * @param expNo    物流单号
     * @param quick100 配置信息
     * @return
     */
    private DeliveryInfoVO queryByQuick100(String expCode, String expNo, Quick100 quick100, String consigneeMobile) {
        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("com", expCode);
        paramMap.put("num", expNo);
        if (Objects.equals(expCode, Quick100.SF_CODE) || Objects.equals(expCode, Quick100.FENGWANG_CODE)) {
            // 快递100中顺丰速运和丰网速运必填收/寄件人手机号码，其他快递公司选填
            paramMap.put("phone", consigneeMobile);
        }
        paramMap.put("order", "asc");
        String param = JSON.toJSONString(paramMap);
        String sign = new Digester(DigestAlgorithm.MD5).digestHex(param + quick100.getKey() + quick100.getCustomer(), "UTF-8").toUpperCase();
        Map<String, Object> params = new HashMap<>(16);
        params.put("param", param);
        params.put("sign", sign);
        params.put("customer", quick100.getCustomer());
        String repJson = HttpUtil.post(quick100.getReqUrl(), params);
        log.info("queryByQuick100 ,param:{}，返回结果：{}",JSONObject.toJSONString(params),repJson);
        DeliveryHundredInfoBO hundredInfoBO = Json.parseObject(repJson, DeliveryHundredInfoBO.class);
        DeliveryInfoVO deliveryInfoVO = new DeliveryInfoVO();
        deliveryInfoVO.setState(hundredInfoBO.getState());
        if (CollectionUtil.isEmpty(hundredInfoBO.getData())) {
            deliveryInfoVO.setTraces(new ArrayList<>());
            return deliveryInfoVO;
        }
        List<DeliveryItemInfoVO> deliveryAliInfoList = new ArrayList<>();
        for (DeliveryHundredItemInfoBO itemInfo : hundredInfoBO.getData()) {
            DeliveryItemInfoVO deliveryItemInfoVO = new DeliveryItemInfoVO();
            deliveryItemInfoVO.setAcceptStation(itemInfo.getContext());
            deliveryItemInfoVO.setAcceptTime(itemInfo.getFtime());
            deliveryAliInfoList.add(deliveryItemInfoVO);
        }

        //查询的结果按照时间倒序返回。
        deliveryAliInfoList.sort((t1, t2) -> t2.getAcceptTime().compareTo(t1.getAcceptTime()));
        deliveryInfoVO.setTraces(deliveryAliInfoList);
        return deliveryInfoVO;
    }


    public static void main(String[] args) {
        String repJson = "{\"message\":\"ok\",\"nu\":\"73186714474772\",\"ischeck\":\"0\",\"condition\":\"00\",\"com\":\"zhongtong\",\"status\":\"200\",\"state\":\"5\",\"data\":[{\"time\":\"2022-07-18 15:02:56\",\"ftime\":\"2022-07-18 15:02:56\",\"context\":\"【无锡宜兴】（0510-66120020） 的 宜兴斯凯奇（13914236625） 已揽收\"},{\"time\":\"2022-07-18 15:03:01\",\"ftime\":\"2022-07-18 15:03:01\",\"context\":\"快件离开 【无锡宜兴】 已发往 【无锡中转部】\"},{\"time\":\"2022-07-19 02:03:11\",\"ftime\":\"2022-07-19 02:03:11\",\"context\":\"快件已经到达 【无锡中转部】\"},{\"time\":\"2022-07-19 02:05:55\",\"ftime\":\"2022-07-19 02:05:55\",\"context\":\"快件离开 【无锡中转部】 已发往 【广州中心】\"},{\"time\":\"2022-07-20 02:50:45\",\"ftime\":\"2022-07-20 02:50:45\",\"context\":\"快件已经到达 【广州中心】\"},{\"time\":\"2022-07-20 02:58:31\",\"ftime\":\"2022-07-20 02:58:31\",\"context\":\"快件离开 【广州中心】 已发往 【新二沙岛】\"},{\"time\":\"2022-07-20 06:56:01\",\"ftime\":\"2022-07-20 06:56:01\",\"context\":\"快件已经到达 【新二沙岛】\"},{\"time\":\"2022-07-20 06:56:02\",\"ftime\":\"2022-07-20 06:56:02\",\"context\":\"【新二沙岛】 的张超（18236008090） 正在第1次派件, 请保持电话畅通,并耐心等待（95720为中通快递员外呼专属号码，请放心接听）\"}]}";
        DeliveryHundredInfoBO hundredInfoBO = Json.parseObject(repJson, DeliveryHundredInfoBO.class);
        DeliveryInfoVO deliveryInfoVO = new DeliveryInfoVO();
        deliveryInfoVO.setState(hundredInfoBO.getState());
        if (CollectionUtil.isEmpty(hundredInfoBO.getData())) {
            deliveryInfoVO.setTraces(new ArrayList<>());
        }
        List<DeliveryItemInfoVO> deliveryAliInfoList = new ArrayList<>();
        for (DeliveryHundredItemInfoBO itemInfo : hundredInfoBO.getData()) {
            DeliveryItemInfoVO deliveryItemInfoVO = new DeliveryItemInfoVO();
            deliveryItemInfoVO.setAcceptStation(itemInfo.getContext());
            deliveryItemInfoVO.setAcceptTime(itemInfo.getFtime());
            deliveryAliInfoList.add(deliveryItemInfoVO);
        }

//        deliveryAliInfoList = deliveryAliInfoList.stream().sorted((t1,t2) ->
//                        Long.compare(convertTimeToLong(t2.getAcceptTime()),
//                                convertTimeToLong(t2.getAcceptTime()))).
//                collect(Collectors.toList());
        deliveryAliInfoList.sort((t1, t2) -> t2.getAcceptTime().compareTo(t1.getAcceptTime()));
        deliveryInfoVO.setTraces(deliveryAliInfoList);
        System.out.println(JSONObject.toJSONString(deliveryAliInfoList));

    }



    /**
     * 通过阿里快递查询,返回当前包裹的快递信息
     *
     * @param expCode        物流公司编码
     * @param expNo          物流单号
     * @param aliQuickConfig 配置信息
     * @return 当前包裹的快递信息
     */
    private DeliveryInfoVO queryByAliQuick(String expCode, String expNo, AliQuick aliQuickConfig, String consigneeMobile) {
        Map<String, Object> params = new HashMap<>(16);
        if (Objects.equals(expCode, AliQuick.SF_CODE) && StrUtil.isNotBlank(consigneeMobile)) {
            // 使用阿里快递查询时，如果是顺丰单号，需要在单号后面拼接收件人手机号后四位
            expNo = expNo + ":" + getLastFourOfMobile(consigneeMobile);
        }
        params.put("type", expCode);
        params.put("no", expNo);
        params.put("Authorization", DeliveryConstant.CODE_HEADER + aliQuickConfig.getAliCode());
        //头信息，多个头信息多次调用此方法即可
        String repJson = HttpRequest.get(aliQuickConfig.getReqUrl())
                .header(Header.AUTHORIZATION, DeliveryConstant.CODE_HEADER + aliQuickConfig.getAliCode())
                .form(params)
                .execute().body();
        log.info("queryByAliQuick ,param:{}，返回结果：{}",JSONObject.toJSONString(params),repJson);
        try {
            String data = JSONObject.parseObject(repJson).getString("result");
            DeliveryAliInfoBO deliveryAliInfoBO = Json.parseObject(data, DeliveryAliInfoBO.class);
            DeliveryInfoVO deliveryInfoVO = new DeliveryInfoVO();
            deliveryInfoVO.setCompanyName(deliveryAliInfoBO.getExpName());
            deliveryInfoVO.setCompanyHomeUrl(deliveryAliInfoBO.getExpSite());
            deliveryInfoVO.setDvyFlowId(deliveryAliInfoBO.getNumber());
            deliveryInfoVO.setState(deliveryAliInfoBO.getDeliverystatus());
            if (CollectionUtil.isEmpty(deliveryAliInfoBO.getList())) {
                deliveryInfoVO.setTraces(new ArrayList<>());
                return deliveryInfoVO;
            }
            List<DeliveryItemInfoVO> deliveryAliInfoList = new ArrayList<>();
            for (DeliveryAliItemInfoBO itemInfo : deliveryAliInfoBO.getList()) {
                DeliveryItemInfoVO deliveryItemInfoVO = new DeliveryItemInfoVO();
                deliveryItemInfoVO.setAcceptStation(itemInfo.getStatus());
                deliveryItemInfoVO.setAcceptTime(itemInfo.getTime());
                deliveryAliInfoList.add(deliveryItemInfoVO);
            }
            deliveryInfoVO.setTraces(deliveryAliInfoList);
            return deliveryInfoVO;
        } catch (Exception e) {
            logger.warn("阿里物流查询错误" + e.getMessage());
            return null;
        }

    }

    private String getLastFourOfMobile(String mobile) {
        // 获取手机号后四位
        if (Objects.isNull(mobile) || mobile.length() < 4) {
            return "";
        }
        return mobile.substring(mobile.length() - 4);
    }

}
