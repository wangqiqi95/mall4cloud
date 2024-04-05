package com.mall4j.cloud.api.openapi.vo.qiyukf;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QiyukfOrderServerResponseEntity {


    private int rlt;

    private int count;

    private List<QiyukfOrderVO> orders;

}
