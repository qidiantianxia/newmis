package com.yada.sdk.device.encryption;

import com.yada.sdk.device.encryption.model.RsaKey;

/**
 * 用于使用加密机进行RSA相关操作
 * Created by fengming on 3/31/17.
 */
public interface IRsaEncryption {

    /**
     * 产生RSA密钥对
     *
     * @return rsa密钥对
     */
    RsaKey genKey();

    /**
     * 使用公钥加密数据
     *
     * @param pubKey 公钥
     * @param data   数据
     * @return
     */
    byte[] encrypt(byte[] pubKey, byte[] data);
}
