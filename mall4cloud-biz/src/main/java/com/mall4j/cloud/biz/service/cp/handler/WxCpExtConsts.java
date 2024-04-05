package com.mall4j.cloud.biz.service.cp.handler;

import lombok.experimental.UtilityClass;

/**
 * <pre>
 * 企业微信常量
 *
 * @author hwy
 * @date 2018/8/25
 * </pre>
 */
@UtilityClass
public class WxCpExtConsts {
  /**
   * 企业微信端推送过来的事件类型.
   */
  @UtilityClass
  public static class EventType {
    /**
     * 群事件
     */
    public static final String CHANGE_EXTERNAL_CHAT = "change_external_chat";
  }

  /**
   * 企业外部联系人群
   */
  @UtilityClass
  public static class ExternalChatChangeType {
    /**
     * 新增群
     */
    public static final String ADD_CHAT = "create";
    /**
     * 更新群
     */
    public static final String UPDATE_CHAT = "update";
    /**
     * 解散群
     */
    public static final String DEL_CHAT = "dismiss";

  }

  /**
   * 企业外部联系人群
   */
  @UtilityClass
  public static class ExternalChatUpdateType {
    /**
     * 新增客户
     */
    public static final String ADD_MEMBER = "add_member";
    /**
     * 成员退群
     */
    public static final String DEL_MEMBER = "del_member";
    /**
     * 群主变更
     */
    public static final String CHANGE_OWNER = "change_owner";

    /**
     * 群主名变更
     */
    public static final String CHANGE_NAME = "change_name";

    public static final String MSG_AUDIT_APPROVED = "msg_audit_approved";

  }


  /**
   * 企业外部联系人群
   */
  @UtilityClass
  public static class StaffExtend{
    /**
     * 继承失败
     */
    public static final String TRANSFER_FAIL = "transfer_fail";

  }




}
