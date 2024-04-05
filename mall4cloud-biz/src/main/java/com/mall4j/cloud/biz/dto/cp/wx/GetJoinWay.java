package com.mall4j.cloud.biz.dto.cp.wx;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class GetJoinWay {
    private  String config_id;
    public GetJoinWay(String configId){
        this.config_id = configId;
    }
}
