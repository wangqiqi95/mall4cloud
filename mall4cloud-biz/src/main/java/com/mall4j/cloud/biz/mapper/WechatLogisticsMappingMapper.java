package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WechatLogisticsMappingDO;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * The Table wechat_logistics_mapping.
 * wechat_logistics_mapping
 */
public interface WechatLogisticsMappingMapper{

    /**
     * desc:insert:wechat_logistics_mapping.<br/>
     * descSql =  SELECT LAST_INSERT_ID() INSERT INTO wechat_logistics_mapping( ID ,DELIVERY_ID ,WX_DELIVERY_ID ,WX_DELIVERY_NAME ,CREATE_BY ,CREATE_TIME ,UPDATE_BY ,UPDATE_TIME ,IS_DELETED )VALUES( #{id,jdbcType=BIGINT} , #{deliveryId,jdbcType=VARCHAR} , #{wxDeliveryId,jdbcType=VARCHAR} , #{wxDeliveryName,jdbcType=VARCHAR} , #{createBy,jdbcType=VARCHAR} , #{createTime,jdbcType=TIMESTAMP} , #{updateBy,jdbcType=VARCHAR} , #{updateTime,jdbcType=TIMESTAMP} , 'N' )
     * @param entity entity
     * @return Long
     */
    Long insert(WechatLogisticsMappingDO entity);
    /**
     * desc:update table:wechat_logistics_mapping.<br/>
     * descSql =  UPDATE wechat_logistics_mapping SET DELIVERY_ID = #{deliveryId,jdbcType=VARCHAR} ,WX_DELIVERY_ID = #{wxDeliveryId,jdbcType=VARCHAR} ,WX_DELIVERY_NAME = #{wxDeliveryName,jdbcType=VARCHAR} ,CREATE_BY = #{createBy,jdbcType=VARCHAR} ,CREATE_TIME = #{createTime,jdbcType=TIMESTAMP} ,UPDATE_BY = #{updateBy,jdbcType=VARCHAR} ,UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP} ,IS_DELETED = #{isDeleted,jdbcType=INTEGER} WHERE ID = #{id,jdbcType=BIGINT}
     * @param entity entity
     * @return Long
     */
    Long update(WechatLogisticsMappingDO entity);
    /**
     * desc:delete:wechat_logistics_mapping.<br/>
     * descSql =  DELETE FROM wechat_logistics_mapping WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return Long
     */
    Long deleteByPrimary(Long id);
    /**
     * desc:get:wechat_logistics_mapping.<br/>
     * descSql =  SELECT * FROM wechat_logistics_mapping WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return WechatLogisticsMappingDO
     */
    WechatLogisticsMappingDO getByPrimary(Long id);

    List<WechatLogisticsMappingDO> list();

    LiveLogisticsVO getByDeliveryId(@Param("deliveryId") Long deliveryId);

    WechatLogisticsMappingDO getByWechatDeliveryId(@Param("wechatDeliveryId") String wechatDeliveryId);

    WechatLogisticsMappingDO getDefualtWechatDelivery();
}
