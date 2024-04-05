package com.mall4j.cloud.api.docking.enums;

public enum InventoryStatus {
    OUT_OF_STOCK("OUT_OF_STOCK", "缺货"), NORMAL("NORMAL", "正常");

    private String value;

    private String desc;

    InventoryStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static InventoryStatus instance(String value) {
        InventoryStatus[] enums = values();
        for (InventoryStatus inventoryStatus : enums) {
            if (inventoryStatus.getValue().equals(value)) {
                return inventoryStatus;
            }
        }
        return null;
    }
}
