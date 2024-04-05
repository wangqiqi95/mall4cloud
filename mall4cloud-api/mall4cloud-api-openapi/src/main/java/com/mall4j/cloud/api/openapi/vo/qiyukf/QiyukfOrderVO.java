package com.mall4j.cloud.api.openapi.vo.qiyukf;

import lombok.Data;

import java.util.List;

@Data
public class QiyukfOrderVO {

    private int index;

    private List<QiyukfBlocksVO> blocks;

}
