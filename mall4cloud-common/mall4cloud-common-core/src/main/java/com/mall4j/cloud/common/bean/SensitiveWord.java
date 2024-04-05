package com.mall4j.cloud.common.bean;

/**
 * @author Citrus
 * @date 2021/8/6 10:25
 */
public class SensitiveWord {
    /**
     * 敏感词
     */
    private String sensitiveWords;

    public String getSensitiveWords() {
        return sensitiveWords;
    }

    public void setSensitiveWords(String sensitiveWords) {
        this.sensitiveWords = sensitiveWords;
    }

    @Override
    public String toString() {
        return "SensitiveWord{" +
                "sensitiveWords='" + sensitiveWords + '\'' +
                '}';
    }
}
