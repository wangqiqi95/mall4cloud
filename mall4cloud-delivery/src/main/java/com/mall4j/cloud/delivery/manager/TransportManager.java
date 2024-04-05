package com.mall4j.cloud.delivery.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.delivery.constant.TransportChargeType;
import com.mall4j.cloud.api.delivery.constant.TransportFreeType;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.ShopTransFeeVO;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import com.mall4j.cloud.common.order.vo.UserDeliveryInfoVO;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.delivery.service.TransportService;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;
import com.mall4j.cloud.delivery.vo.TransfeeVO;
import com.mall4j.cloud.delivery.vo.TransportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 */
@Component
public class TransportManager {

    @Autowired
    private TransportService transportService;


    public long calculateTransfee(List<ShopCartItemVO> shopCartItems, UserAddrVO userAddr, UserDeliveryInfoVO userDeliveryInfo) {
        Map<Long, ShopTransFeeVO> shopIdWithShopTransFee = new HashMap<>(16);

        // 商品的总运费
        long totalTransfee = 0L;

        // 1 去除没有店铺配送的订单和运费模板的订单
        List<ShopCartItemVO> transPortList = removeNotTransPort(shopCartItems);

        if (CollectionUtil.isEmpty(transPortList)){
            return 0;
        }
        Map<Long, List<ShopCartItemVO>> deliveryTemplateIdWithShopCartItems = transPortList.stream().collect(Collectors.groupingBy(ShopCartItemVO::getDeliveryTemplateId));




        for (Long deliveryTemplateId : deliveryTemplateIdWithShopCartItems.keySet()) {
            List<ShopCartItemVO> deliveryShopCartItems = deliveryTemplateIdWithShopCartItems.get(deliveryTemplateId);
            // 运费模板的运费
            long transfee = repeatListTransfee(deliveryTemplateId, deliveryShopCartItems, userAddr);
            totalTransfee += transfee;

            // 开始计算店铺的运费
            for (ShopCartItemVO deliveryShopCartItem : deliveryShopCartItems) {
                Long shopId = deliveryShopCartItem.getShopId();
                ShopTransFeeVO shopTransFee = shopIdWithShopTransFee.get(shopId);
                // 如果商家没有计算运费就初始化运费
                if (Objects.isNull(shopTransFee)) {
                    shopTransFee = new ShopTransFeeVO();
                    shopTransFee.setTransfee(transfee);
                    shopTransFee.setDeliveryTemplateId(deliveryTemplateId);
                    // 没有减免运费的计算
                    shopTransFee.setFreeTransfee(0L);
                    shopIdWithShopTransFee.put(shopId, shopTransFee);
                }
                // 累加非同一个运费模板的运费
                else if (!Objects.equals(shopTransFee.getDeliveryTemplateId(), deliveryTemplateId)) {
                    shopTransFee.setTransfee(transfee + shopTransFee.getTransfee());
                }
            }
        }
        userDeliveryInfo.setShopIdWithShopTransFee(shopIdWithShopTransFee);
        return totalTransfee;
    }

    /**
     * 去除没有店铺后的订单
     * @param productItems 原用户所有的未处理订单
     * @return 有运费模板的订单
     */
    private List<ShopCartItemVO> removeNotTransPort(List<ShopCartItemVO> productItems){
        // 保存去掉没有运费模板重复的订单
        List<ShopCartItemVO> resList = new ArrayList<>();

        for (ShopCartItemVO productItem : productItems) {
            if (productItem.getDeliveryTemplateId() == null) {
                continue;
            }
            resList.add(productItem);
        }
        return resList;
    }


    /**
     * 计算出运费模板重复的订单运费
     * @param productItems 运费模板是重复的订单
     * @param userAddr
     * @return
     */
    private long repeatListTransfee(Long deliveryTemplateId, List<ShopCartItemVO> productItems, UserAddrVO userAddr){

        // 商品的件数 (总的件数)
        int prodCount = 0;
        // 商品的重量
        double totalWeight = 0.0;
        // 商品的体积
        double totalVolume = 0.0;
        // 商品总金额
        long totalAmount = 0;

        for (ShopCartItemVO productItem : productItems) {
            prodCount += productItem.getCount();
            double itemTotalWeight = productItem.getWeight() == null ? 0 : productItem.getWeight().doubleValue();
            totalWeight = Arith.mul(itemTotalWeight, productItem.getCount());
            double itemTotalVolume = productItem.getVolume() == null ? 0 : productItem.getVolume().doubleValue();
            totalVolume = Arith.mul(itemTotalVolume, productItem.getCount());
            totalAmount += productItem.getTotalAmount();
        }


        // 用户所在区域的id
        Long areaId = userAddr.getAreaId();

        //找出该产品的运费项  运费模板
        TransportVO transport = transportService.getTransportAndAllItemsById(deliveryTemplateId);
        //商家把运费模板删除
        if (transport == null) {
            return 0;
        }

        // 用于计算运费的件数
        double piece = 0.0;

        if (Objects.equals(TransportChargeType.COUNT.value(), transport.getChargeType())) {
            // 按件数计算运费
            piece = prodCount;
        } else if (Objects.equals(TransportChargeType.WEIGHT.value(), transport.getChargeType())) {
            // 按重量计算运费
            piece = totalWeight;
        } else if (Objects.equals(TransportChargeType.VOLUME.value(), transport.getChargeType())) {
            // 按体积计算运费
            piece = totalVolume;
        }
        //如果有包邮的条件
        if (transport.getHasFreeCondition() == 1) {
            // 获取所有的包邮条件
            List<TransfeeFreeVO> transfeeFrees = transport.getTransFeeFrees();
            for (TransfeeFreeVO transfeeFree : transfeeFrees) {
                List<AreaVO> freeCityList = transfeeFree.getFreeCityList();
                for (AreaVO freeCity : freeCityList) {
                    if (!Objects.equals(freeCity.getAreaId(), areaId)) {
                        continue;
                    }
                    //包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
                    boolean isFree = (Objects.equals(transfeeFree.getFreeType(), TransportFreeType.COUNT.value()) && piece >= transfeeFree.getPiece()) ||
                            (Objects.equals(transfeeFree.getFreeType(), TransportFreeType.AMOUNT.value()) && totalAmount >= transfeeFree.getAmount()) ||
                            (Objects.equals(transfeeFree.getFreeType(), TransportFreeType.COUNT_AND_AMOUNT.value()) && piece >= transfeeFree.getPiece() && totalAmount >= transfeeFree.getAmount());
                    if (isFree) {
                        return 0;
                    }
                }
            }
        }
        //订单的运费项
        return getTransfee(areaId, transport, piece);
    }

    private long getTransfee(Long areaId, TransportVO transport, double piece) {
        TransfeeVO transfee = null;
        List<TransfeeVO> transfees = transport.getTransFees();
        for (TransfeeVO dbTransfee : transfees) {
            // 将该商品的运费设置为默认运费
            if (transfee == null && CollectionUtil.isEmpty(dbTransfee.getCityList())) {
                transfee = dbTransfee;
            }
            // 如果在运费模板中的城市找到该商品的运费，则将该商品由默认运费设置为该城市的运费
            boolean isContainer = dbTransfee.getCityList().stream().anyMatch(area -> area.getAreaId().equals(areaId));
            if(isContainer){
                transfee = dbTransfee;
                break;
            }
            // 如果在运费模板中的城市找到该商品的运费，则退出整个循环
            if (transfee != null && CollectionUtil.isNotEmpty(transfee.getCityList())) {
                break;
            }
        }

        // 如果无法获取到任何运费相关信息，则返回0运费
        if (transfee == null) {
            return 0L;
        }
        // 产品的运费
        long fee = transfee.getFirstFee();
        // 如果件数大于首件数量，则开始计算超出的运费
        if (piece > transfee.getFirstPiece()) {
            // 续件数量
            double prodContinuousPiece = Arith.sub(piece, transfee.getFirstPiece());
            // 续件数量的倍数，向上取整
            int mulNumber = (int) Math.ceil(Arith.div(prodContinuousPiece, transfee.getContinuousPiece()));
            // 续件数量运费
            long continuousFee = mulNumber * transfee.getContinuousFee();
            fee += continuousFee;
        }
        return fee;
    }

}
