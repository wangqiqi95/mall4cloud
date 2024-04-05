package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WechatNotifyTaskDO;

/**
 *
 * The Table wechat_notify_task.
 * wechat_notify_task
 */
public interface WechatNotifyTaskMapper{

    /**
     * desc:insert:wechat_notify_task.<br/>
     * descSql =  SELECT LAST_INSERT_ID() INSERT INTO wechat_notify_task( ID ,TENANT_ID ,TYPE ,ITEM_ID ,ITEM_STATUS ,CREATE_TIME ,UPDATE_TIME ,IS_DELETED )VALUES( #{id,jdbcType=BIGINT} , #{tenantId,jdbcType=VARCHAR} , #{type,jdbcType=VARCHAR} , #{itemId,jdbcType=VARCHAR} , #{itemStatus,jdbcType=VARCHAR} , #{createTime,jdbcType=TIMESTAMP} , #{updateTime,jdbcType=TIMESTAMP} , 'N' )
     * @param entity entity
     * @return Long
     */
    Long insert(WechatNotifyTaskDO entity);
    /**
     * desc:update table:wechat_notify_task.<br/>
     * descSql =  UPDATE wechat_notify_task SET TENANT_ID = #{tenantId,jdbcType=VARCHAR} ,TYPE = #{type,jdbcType=VARCHAR} ,ITEM_ID = #{itemId,jdbcType=VARCHAR} ,ITEM_STATUS = #{itemStatus,jdbcType=VARCHAR} ,CREATE_TIME = #{createTime,jdbcType=TIMESTAMP} ,UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP} ,IS_DELETED = #{isDeleted,jdbcType=INTEGER} WHERE ID = #{id,jdbcType=BIGINT}
     * @param entity entity
     * @return Long
     */
    Long update(WechatNotifyTaskDO entity);
    /**
     * desc:delete:wechat_notify_task.<br/>
     * descSql =  DELETE FROM wechat_notify_task WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return Long
     */
    Long deleteByPrimary(Long id);
    /**
     * desc:get:wechat_notify_task.<br/>
     * descSql =  SELECT * FROM wechat_notify_task WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return WechatNotifyTaskDO
     */
    WechatNotifyTaskDO getByPrimary(Long id);
}
