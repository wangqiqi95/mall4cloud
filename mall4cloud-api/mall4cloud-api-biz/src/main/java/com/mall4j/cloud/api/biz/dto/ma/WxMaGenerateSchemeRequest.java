package com.mall4j.cloud.api.biz.dto.ma;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : cofedream
 * @date : 2021-01-26
 */
@Data
@Builder(builderMethodName = "newBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class WxMaGenerateSchemeRequest implements Serializable {
  /**
   * 跳转到的目标小程序信息。
   * <pre>
   * 是否必填：否
   * </pre>
   */
  @JSONField(name = "jump_wxa")
  private JumpWxa jumpWxa;

  /**
   * 生成的scheme码类型，到期失效：true，永久有效：false。
   * <pre>
   * 是否必填：否
   * </pre>
   */
  @JSONField(name = "is_expire")
  private Boolean isExpire;

  /**
   * 到期失效的scheme码的失效时间，为Unix时间戳。生成的到期失效scheme码在该时间前有效。最长有效期为1年。生成到期失效的scheme时必填。
   * <pre>
   * 是否必填：否
   * </pre>
   */
  @JSONField(name = "expire_time")
  private Long expireTime;

  /**
   * 默认值0，到期失效的 scheme 码失效类型，失效时间：0，失效间隔天数：1
   */
  @JSONField(name = "expire_type")
  private Integer expireType=0;

  /**
   * 到期失效的 scheme 码的失效间隔天数。生成的到期失效 scheme 码在该间隔时间到达前有效。
   * 最长间隔天数为30天。
   * is_expire 为 true 且 expire_type 为 1 时必填
   */
  @JSONField(name = "expire_interval")
  private Integer expireInterval;

  @Data
  @Builder(builderMethodName = "newBuilder")
  @AllArgsConstructor
  @NoArgsConstructor
  public static class JumpWxa {
    /**
     * 通过scheme码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带query。path为空时会跳转小程序主页。
     * <pre>
     * 是否必填：是
     * </pre>
     */
    @JSONField(name = "path")
    private String path;

    /**
     * 通过scheme码进入小程序时的query，最大128个字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * 返回值
     * <pre>
     * 是否必填：是
     * </pre>
     */
    @JSONField(name = "query")
    private String query;

    /**
     * 要打开的小程序版本。正式版为"release"，体验版为"trial"，开发版为"develop"默认值：release
     */
    @JSONField(name = "env_version")
    private String envVersion = "release";
  }

  public String toJson() {
    return JSON.toJSONString(this);
  }

}
