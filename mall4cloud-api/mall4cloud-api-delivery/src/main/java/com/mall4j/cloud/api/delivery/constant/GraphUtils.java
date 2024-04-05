package com.mall4j.cloud.api.delivery.constant;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.delivery.bo.BmapPointBO;

import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/12/16
 */
public class GraphUtils {
    /**
     * 判断点是否在多边形内(基本思路是用交点法)
     *
     * @param point
     * @param boundaryPoints
     * @return
     */
    public static boolean isPointInPolygon(BmapPointBO point, List<BmapPointBO> boundaryPoints) {
        if(CollectionUtil.isEmpty(boundaryPoints) || Objects.isNull(point)){
            return false;
        }
        int pointCount = boundaryPoints.size();

        // 首先判断点是否在多边形的外包矩形内，如果在，则进一步判断，否则返回false
        if (!isPointInRectangle(point, boundaryPoints)) {
            return false;
        }
        for (BmapPointBO boundaryPoint : boundaryPoints) {
            if (point.equals(boundaryPoint)) {
                return true;
            }
        }
        return pointIsInPolygon(pointCount, point, boundaryPoints);
    }

    public static boolean pointIsInPolygon(int pointCount,BmapPointBO point, List<BmapPointBO> boundaryPoints){
        /*
          基本思想是利用X轴射线法，计算射线与多边形各边的交点，如果是偶数，则点在多边形外，否则在多边形内。还会考虑一些特殊情况，如点在多边形顶点上
          ， 点在多边形边上等特殊情况。
         */
        // X轴射线与多边形的交点数
        int intersectPointCount = 0;
        // X轴射线与多边形的交点权值
        float intersectPointWeights = 0;
        // 浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;
        // 边P1P2的两个端点
        BmapPointBO point1 = boundaryPoints.get(0), point2;
        // 循环判断所有的边
        for (int i = 1; i <= pointCount; i++) {
            point2 = boundaryPoints.get(i % pointCount);

            /*
              如果点的y坐标在边P1P2的y坐标开区间范围之外，那么不相交。
             */
            if (point.getLat() < Math.min(point1.getLat(), point2.getLat())
                    || point.getLat() > Math.max(point1.getLat(), point2.getLat())) {
                point1 = point2;
                continue;
            }

            /*
              此处判断射线与边相交
             */
            // 如果点的y坐标在边P1P2的y坐标开区间内
            if (point.getLat() > Math.min(point1.getLat(), point2.getLat())
                    && point.getLat() < Math.max(point1.getLat(), point2.getLat())) {
                // 若边P1P2是垂直的
                if (point1.getLng().equals(point2.getLng())) {
                    if (point.getLng().equals(point1.getLng())) {
                        // 若点在垂直的边P1P2上，则点在多边形内
                        return true;
                    } else if (point.getLng() < point1.getLng()) {
                        // 若点在在垂直的边P1P2左边，则点与该边必然有交点
                        ++intersectPointCount;
                    }
                } else {// 若边P1P2是斜线
                    // 点point的x坐标在点P1和P2的左侧
                    if (point.getLng() <= Math.min(point1.getLng(), point2.getLng())) {
                        ++intersectPointCount;
                    }
                    // 点point的x坐标在点P1和P2的x坐标中间
                    else if (point.getLng() > Math.min(point1.getLng(), point2.getLng())
                            && point.getLng() < Math.max(point1.getLng(), point2.getLng())) {
                        double slopeDiff = 0.0d;
                        if (point1.getLat() > point2.getLat()) {
                            slopeDiff = (point.getLat() - point2.getLat()) / (point.getLng() - point2.getLng())
                                    - (point1.getLat() - point2.getLat()) / (point1.getLng() - point2.getLng());
                        } else {
                            slopeDiff = (point.getLat() - point1.getLat()) / (point.getLng() - point1.getLng())
                                    - (point2.getLat() - point1.getLat()) / (point2.getLng() - point1.getLng());
                        }
                        if (slopeDiff > 0) {
                            // 由于double精度在计算时会有损失，故匹配一定的容差。经试验，坐标经度可以达到0.0001
                            if (slopeDiff < precision) {
                                // 点在斜线P1P2上
                                return true;
                            } else {
                                // 点与斜线P1P2有交点
                                intersectPointCount++;
                            }
                        }
                    }
                }
            } else {
                // 边P1P2水平
                if (point1.getLat().equals(point2.getLat())) {
                    if (point.getLng() <= Math.max(point1.getLng(), point2.getLng())
                            && point.getLng() >= Math.min(point1.getLng(), point2.getLng())) {
                        // 若点在水平的边P1P2上，则点在多边形内
                        return true;
                    }
                }
                /*
                  判断点通过多边形顶点
                 */
                boolean flag = ((point.getLat().equals(point1.getLat()) && point.getLng() < point1.getLng()))
                        || (point.getLat().equals(point2.getLat()) && point.getLng() < point2.getLng());
                if (flag) {
                    if (point2.getLat() < point1.getLat()) {
                        intersectPointWeights += -0.5;
                    } else if (point2.getLat() > point1.getLat()) {
                        intersectPointWeights += 0.5;
                    }
                }
            }
            point1 = point2;
        }
        int d = 2;
        // 偶数在多边形外
        // 奇数在多边形内
        return (intersectPointCount + Math.abs(intersectPointWeights)) % d != 0;
    }

    /**
     * 判断点是否在矩形内在矩形边界上，也算在矩形内(根据这些点，构造一个外包矩形)
     *
     * @param point
     *            点对象
     * @param boundaryPoints
     *            矩形边界点
     * @return
     */
    public static boolean isPointInRectangle(BmapPointBO point, List<BmapPointBO> boundaryPoints) {
        // 西南角点
        BmapPointBO southWestPoint = getSouthWestPoint(boundaryPoints);
        // 东北角点
        BmapPointBO northEastPoint = getNorthEastPoint(boundaryPoints);
        return (point.getLng() >= southWestPoint.getLng() && point.getLng() <= northEastPoint.getLng()
                && point.getLat() >= southWestPoint.getLat() && point.getLat() <= northEastPoint.getLat());

    }

    /**
     * 根据这组坐标，画一个矩形，然后得到这个矩形西南角的顶点坐标
     *
     * @param vertexs
     * @return
     */
    private static BmapPointBO getSouthWestPoint(List<BmapPointBO> vertexs) {
        double minLng = vertexs.get(0).getLng(), minLat = vertexs.get(0).getLat();
        for (BmapPointBO bmapPoint : vertexs) {
            double lng = bmapPoint.getLng();
            double lat = bmapPoint.getLat();
            if (lng < minLng) {
                minLng = lng;
            }
            if (lat < minLat) {
                minLat = lat;
            }
        }
        return new BmapPointBO(minLng, minLat);
    }

    /**
     * 根据这组坐标，画一个矩形，然后得到这个矩形东北角的顶点坐标
     *
     * @param vertexs
     * @return
     */
    private static BmapPointBO getNorthEastPoint(List<BmapPointBO> vertexs) {
        double maxLng = 0.0d, maxLat = 0.0d;
        for (BmapPointBO bmapPoint : vertexs) {
            double lng = bmapPoint.getLng();
            double lat = bmapPoint.getLat();
            if (lng > maxLng) {
                maxLng = lng;
            }
            if (lat > maxLat) {
                maxLat = lat;
            }
        }
        return new BmapPointBO(maxLng, maxLat);
    }
}
