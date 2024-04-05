package com.mall4j.cloud.api.delivery.bo;

import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/12/16
 */
public class BmapPointBO {

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    public BmapPointBO() {

    }

    public BmapPointBO(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BmapPointBO) {
            BmapPointBO bmapPoint = (BmapPointBO) obj;
            return (Objects.equals(bmapPoint.getLng(), lng) && Objects.equals(bmapPoint.getLat(), lat));
        } else {
            return false;
        }
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "BmapPointBO{" +
                "lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
