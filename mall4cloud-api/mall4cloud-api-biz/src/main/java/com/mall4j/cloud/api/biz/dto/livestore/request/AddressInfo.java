package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddressInfo {


    /**
     * 收件人姓名
     */
    @JsonProperty("receiver_name")
    private String receiverName;

    /**
     * 详细收货地址信息
     */
    @JsonProperty("detailed_address")
    private String detailedAddress;

    /**
     *收件人手机号码
     */
    @JsonProperty("tel_number")
    private String telNumber;

    /**
     * 国家
     */
    @JsonProperty("country")
    private String country;


    /**
     * province
     */
    @JsonProperty("province")
    private String province;

    /**
     * city
     */
    @JsonProperty("city")
    private String city;
    /**
     * town
     */
    @JsonProperty("town")
    private String town;


}
