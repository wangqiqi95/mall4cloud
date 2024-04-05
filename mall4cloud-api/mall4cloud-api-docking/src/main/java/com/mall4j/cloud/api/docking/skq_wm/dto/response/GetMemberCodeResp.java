package com.mall4j.cloud.api.docking.skq_wm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 查询会员码信息出参
 * @author Peter_Tan
 * @date 2023-04-26 11:30
 **/
@Data
public class GetMemberCodeResp{

    @JsonProperty("code")
    private WMCode code;

    @JsonProperty("data")
    private WMData data;

    @JsonProperty("globalTicket")
    private String globalTicket;

    @JsonProperty("monitorTrackId")
    private String monitorTrackId;
}
