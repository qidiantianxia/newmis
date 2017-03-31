package com.yada.sdk.device.encryption.model;

/**
 * rsa密钥的实体
 * Created by fengming on 3/31/17.
 */
public class RsaKey {

    private final byte[] priKey;
    private final byte[] pubKey;

    /**
     * 创建RSA的密钥对
     *
     * @param priKey 私钥
     * @param pubKey 公钥
     */
    public RsaKey(byte[] priKey, byte[] pubKey) {
        this.priKey = priKey;
        this.pubKey = pubKey;
    }

    /**
     * 获取私钥
     *
     * @return 私钥字节数组
     */
    public byte[] getPriKey() {
        return priKey;
    }

    /**
     * 获取公钥
     *
     * @return 公钥的字节数组
     */
    public byte[] getPubKey() {
        return pubKey;
    }
}
