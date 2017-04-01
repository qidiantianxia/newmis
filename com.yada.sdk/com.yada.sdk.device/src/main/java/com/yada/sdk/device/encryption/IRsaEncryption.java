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
     * @param data   明文数据
     * @return 加密后的数据
     */
    byte[] encrypt(byte[] pubKey, byte[] data);

    /**
     * 使用私钥解密
     *
     * @param priKeyLmk 本地主密钥保护的私钥
     * @param data      要解密数据
     * @return 明文数据
     */
    byte[] decrypt(byte[] priKeyLmk, byte[] data);
}
