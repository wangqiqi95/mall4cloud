package com.mall4j.cloud.api.product.enums;

public enum SpuChannelEnums {
    CHANNEL_R("R" , "线下渠道"),
    CHANNEL_T("T" , "电商渠道"),
    CHANNEL_L("L" , "清货渠道"),
    ;

    private String code;

    private String name;

    SpuChannelEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getName( Integer code){
        SpuChannelEnums[] enums = values();
        for( SpuChannelEnums e : enums){
            if(e.getCode().equals( code)){
                return e.getName();
            }
        }
        return null;
    }
}
