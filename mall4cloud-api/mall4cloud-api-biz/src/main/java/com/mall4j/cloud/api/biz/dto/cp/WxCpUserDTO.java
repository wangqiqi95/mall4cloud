package com.mall4j.cloud.api.biz.dto.cp;

import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class WxCpUserDTO implements Serializable {
    private static final long serialVersionUID = -5696099236344075582L;

    private String userid;
    private String name;
    private Long[] department;
    private Integer[] orders;
    private String position;
    private String[] positions;
    /**
     * 代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String mobile;
    /**
     * 代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String gender;
    /**
     * 代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String email;
    /**
     * 代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String bizMail;
    /**
     * 代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String avatar;
    private String thumbAvatar;
    private String mainDepartment;
    /**
     * 全局唯一。对于同一个服务商，不同应用获取到企业内同一个成员的open_userid是相同的，最多64个字节。仅第三方应用可获取
     */
    private String openUserId;

    /**
     * 地址。长度最大128个字符，代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String address;
    private String avatarMediaId;
    private Integer status;
    private Integer enable;
    /**
     * 别名；第三方仅通讯录应用可获取
     */
    private String alias;
    private Integer isLeader;
    /**
     * is_leader_in_dept.
     * 个数必须和department一致，表示在所在的部门内是否为上级。1表示为上级，0表示非上级。在审批等应用里可以用来标识上级审批人
     */
    private Integer[] isLeaderInDept;
    private  Extattr extattr;
    private Integer hideMobile;
    private String englishName;
    private String telephone;
    /**
     * 代开发自建应用类型于2022年6月20号后的新建应用将不再返回此字段，需要在【获取访问用户敏感信息】接口中获取
     */
    private String qrCode;
    private Boolean toInvite;
    /**
     * 成员对外信息.
     */
    private List<WxCpUser.ExternalAttribute> externalAttrs = new ArrayList<>();
    private String externalPosition;
    private String externalCorpName;
    private WxCpUser.WechatChannels wechatChannels;

    private String[] directLeader;


    /**
     * Add external attr.
     *
     * @param externalAttr the external attr
     */
    public void addExternalAttr(WxCpUser.ExternalAttribute externalAttr) {
        this.externalAttrs.add(externalAttr);
    }

    /**
     * From json wx cp user.
     *
     * @param json the json
     * @return the wx cp user
     */
    public static WxCpUserDTO fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpUserDTO.class);
    }

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

    public static class Extattr implements Serializable {
        private static final long serialVersionUID = -5696099236344079882L;

        List<Attr> attrs=new ArrayList<>();

        public List<Attr> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<Attr> attrs) {
            this.attrs = attrs;
        }
    }

    public static class Attr implements Serializable {
        private static final long serialVersionUID = -5696099236344075582L;
        private Integer type;
        private String name;
        private TextValue text;
        private String webUrl;
        private String webTitle;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TextValue getText() {
            return text;
        }

        public void setText(TextValue text) {
            this.text = text;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getWebTitle() {
            return webTitle;
        }

        public void setWebTitle(String webTitle) {
            this.webTitle = webTitle;
        }
    }

    public static class TextValue{
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public TextValue(){}

        public TextValue(String value){
            this.value=value;
        }
    }



}
