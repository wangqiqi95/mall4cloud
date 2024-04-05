package com.mall4j.cloud.openapi.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.openapi.dto.qiyukf.QiyukfQueryDTO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfBlocksVO;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfDataVO;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfOrderVO;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfOrderServerResponseEntity;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.vo.EsOrderItemVO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class QiyukfOrderManager {

    @Autowired
    private OrderFeignClient orderFeignClient;


    public QiyukfOrderServerResponseEntity getTheOrderListByUserId(QiyukfQueryDTO qiyukfQueryDTO){

        try {
            OrderSearchDTO orderSearchDTO = new OrderSearchDTO();

            orderSearchDTO.setPageSize(qiyukfQueryDTO.getCount());
            orderSearchDTO.setPageNum(qiyukfQueryDTO.getFrom());
            orderSearchDTO.setUserId(Long.valueOf(qiyukfQueryDTO.getUserid()));


            ServerResponseEntity<PageVO<EsOrderVO>> response = orderFeignClient.supportPage(orderSearchDTO);

            PageVO<EsOrderVO> pageVO;
            if (response.isSuccess() && Objects.nonNull(pageVO = response.getData())
                    && CollectionUtil.isNotEmpty(pageVO.getList())){


                List<QiyukfOrderVO> orderVOList = new ArrayList<>();

                AtomicInteger index = new AtomicInteger(0);
                pageVO.getList().stream().forEach(order -> {
                    QiyukfOrderVO qiyukfOrderVO = new QiyukfOrderVO();

                    qiyukfOrderVO.setIndex(index.get());

                    index.getAndIncrement();

                    AtomicInteger blockIndex = new AtomicInteger(0);

                    List<QiyukfBlocksVO> qiyukfBlocksVOS = new ArrayList<>();

                    qiyukfBlocksVOS.add(this.wrapperTitle(order, blockIndex.get()));
                    blockIndex.getAndIncrement();

                    qiyukfBlocksVOS.add(wrapperDetail(order, blockIndex.get()));

                    order.getOrderItems().stream().forEach(item -> {
                        blockIndex.getAndIncrement();
                        qiyukfBlocksVOS.add(this.wrapperItem(item, blockIndex.get()));

                    });

                    qiyukfOrderVO.setBlocks(qiyukfBlocksVOS);

                    orderVOList.add(qiyukfOrderVO);

                });

                return new QiyukfOrderServerResponseEntity(0,pageVO.getTotal().intValue(),orderVOList);

            }
            return new QiyukfOrderServerResponseEntity(0,0,new ArrayList<>());
        }catch (Exception e){
            log.info("QIYU GET THE ORDER LIST DATA IS FAIL,MESSAGE IS:{}",e);
            return new QiyukfOrderServerResponseEntity(1,0,null);
        }
    }


    /**
     * 组装标题
     * */
    private QiyukfBlocksVO wrapperTitle(EsOrderVO orderVO, int blockIndex){

        QiyukfBlocksVO title = new QiyukfBlocksVO();

        QiyukfDataVO qiyukfDataVO = new QiyukfDataVO(0, "orderNumber", "订单编号", orderVO.getOrderNumber(),"");

        title.setIndex(blockIndex);
        title.setIs_title(Boolean.TRUE);

        title.setData(Arrays.asList(qiyukfDataVO));

        return title;
    }


    /**
     * 组装详细信息
     * */
    private QiyukfBlocksVO wrapperDetail(EsOrderVO orderVO, int blockIndex){

        QiyukfBlocksVO detail = new QiyukfBlocksVO();

        int detailIndex = 0;

        List<QiyukfDataVO> dataVOList = new ArrayList<>();

        detail.setIndex(blockIndex);
        detail.setIs_title(Boolean.FALSE);

        //组装下单时间
        dataVOList.add(new QiyukfDataVO(detailIndex,"createTime","下单时间", DateUtils.dateToString(orderVO.getCreateTime()),""));


        //组装支付方式
        dataVOList.add(new QiyukfDataVO(++detailIndex,"payType","支付方式",PayType.getPayTypeName(orderVO.getPayType()),""));


        //组装配送方式
        dataVOList.add(new QiyukfDataVO(++detailIndex,"deliveryType","配送方式",DeliveryType.getDescription(orderVO.getDeliveryType()),""));


        //组装商品总金额
        dataVOList.add(new QiyukfDataVO(++detailIndex, "total", "商品总额", String.valueOf(orderVO.getTotal()/100F),""));


        //组装订单总金额
        dataVOList.add(new QiyukfDataVO(++detailIndex, "actualTotal","订单总额",String.valueOf(orderVO.getActualTotal()/100F),""));


        //组装订单优惠金额
        dataVOList.add(new QiyukfDataVO(++detailIndex,"reduceAmount","优惠金额",String.valueOf(orderVO.getReduceAmount()/100F),""));



        //组装商品运费
        dataVOList.add(new QiyukfDataVO(++detailIndex,"freightAmount","运费",String.valueOf(orderVO.getFreightAmount()/100F),""));


        //组装订单状态
        dataVOList.add(new QiyukfDataVO(++detailIndex,"status","订单状态", OrderStatus.getDeliveryName(orderVO.getStatus()),""));



        //组装订单备注
        dataVOList.add(new QiyukfDataVO(++detailIndex,"remark","订单备注",orderVO.getRemarks(),""));



        detail.setData(dataVOList);


        return detail;
    }

    private QiyukfBlocksVO wrapperItem(EsOrderItemVO itemVO, int blockIndex){

        QiyukfBlocksVO item = new QiyukfBlocksVO();

        int itemDetailIndex = 0;

        List<QiyukfDataVO> dataVOList = new ArrayList<>();

        item.setIndex(blockIndex);
        item.setIs_title(Boolean.FALSE);

        //组装商品名称
        dataVOList.add(new QiyukfDataVO(itemDetailIndex,"spuName","商品名称",itemVO.getSpuName(),""));

        //组装商品图片
        dataVOList.add(new QiyukfDataVO(++itemDetailIndex,"pic","商品图片",itemVO.getPic(),""));

        //组装商品规格
        dataVOList.add(new QiyukfDataVO(++itemDetailIndex,"skuName","商品规格",itemVO.getSkuName(),""));

        //组装商品单价
        dataVOList.add(new QiyukfDataVO(++itemDetailIndex,"price","商品单价",String.valueOf(itemVO.getPrice()/100F),""));

        //组装商品数量
        dataVOList.add(new QiyukfDataVO(++itemDetailIndex,"count","商品数量",String.valueOf(itemVO.getCount()),""));

        item.setData(dataVOList);

        return item;
    }


}
