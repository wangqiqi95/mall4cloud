package com.mall4j.cloud.common.constant;

/**
 * @author cl
 * @date 2021-08-13 10:06:42
 */
public enum QRCodeType {
    /** 小程序团购商品*/
    GROUP(1),

    /** 小程序分销商品二维码*/
    DISTRIBUTION(2),

    /**
     * 秒杀商品
     */
    SECKILL(3)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    QRCodeType(Integer num){
        this.num = num;
    }

    public static QRCodeType instance(Integer value) {
        QRCodeType[] enums = values();
        for (QRCodeType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
