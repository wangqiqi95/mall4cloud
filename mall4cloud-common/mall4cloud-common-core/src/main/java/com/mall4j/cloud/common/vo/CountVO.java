package com.mall4j.cloud.common.vo;

import lombok.Data;

/**
 * 统计Map结果类（基础类）
 */
@Data
public class CountVO<K, V> {

    /*
    * 统计键
    * */
    private K key;

    /*
    * 统计值
    * */
    private V value;

}
