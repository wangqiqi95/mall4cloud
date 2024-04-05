package com.mall4j.cloud.order.vo.qiyukf;

import lombok.Data;

import java.util.List;

@Data
public class QiyukfServerResponseEntity {


    private int rlt;

    private int count;

    private List<QiyukfOrderVO> orders;

}
