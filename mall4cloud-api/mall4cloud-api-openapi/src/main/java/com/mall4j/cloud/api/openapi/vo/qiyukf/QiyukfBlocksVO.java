package com.mall4j.cloud.api.openapi.vo.qiyukf;

import lombok.Data;

import java.util.List;

@Data
public class QiyukfBlocksVO {

    private int index;

    private boolean is_title;

    private List<QiyukfDataVO> data;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isIs_title() {
        return is_title;
    }

    public void setIs_title(boolean is_title) {
        this.is_title = is_title;
    }

    public List<QiyukfDataVO> getData() {
        return data;
    }

    public void setData(List<QiyukfDataVO> data) {
        this.data = data;
    }
}
