
package com.mall4j.cloud.common.bean;

/**
 * @author FrozenWatermelon
 */
public class WxPay {

    /**
     * 微信支付mchId
     */
    private String mchId;

    /**
     * 微信支付mchKey
     */
    private String mchKey;

    /**
     * 签名类型
     */
    private String signType;

	/**
	 * 支付证书路径
	 */
	private String keyPath;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }
}

