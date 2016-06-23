package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * POS下装参数—国际算法公钥参数--参数块第04块处理
 * 注释出自IST-v3.0.6的第6.1.4章节
 */
public class Block04_1 {

    private final static BerTag ridTag = new BerTag(0x9f, 0x06);
    private final static BerTag publicKeyStoreIndexTag = new BerTag(0xdf, 0x28);
    private final static BerTag publicKeyIndexTag = new BerTag(0x9f, 0x22);
    private final static BerTag expiryDateTag = new BerTag(0xdf, 0x05);
    private final static BerTag hashAlgorithmIndicatorTag = new BerTag(0xdf, 0x06);
    private final static BerTag publicKeyAlgorithmIndicatorTag = new BerTag(0xdf, 0x07);
    private final static BerTag publicKeyModulusTag = new BerTag(0xdf, 0x02);
    private final static BerTag publicKeyExponentTag = new BerTag(0xdf, 0x04);
    private final static BerTag publicKeyHashTag = new BerTag(0xdf, 0x03);
    private final static BerTag paramVersionTag = new BerTag(0xdf, 0x25);

    /**
     * 注册应用提供者标识符 9F06
     * 是AID的前5位，与认证中心公钥索引一起唯一标识出一个公钥
     */
    public final byte[] rid;
    /**
     * 公钥存储位置索引 DF28
     * 用于标志公钥存储的位置，每个RID最多有6把公钥，此处放公钥的存放位置，值的范围为1-6，用于指示保存公钥的位置
     */
    public final byte publicKeyStoreIndex;
    /**
     * 认证中心公钥索引 DF22
     * 和RID一起指定对应支付组织的某个认证中心公钥
     */
    public final byte publicKeyIndex;
    /**
     * 认证中心公钥失效期 DF05
     * 公钥失效日期：YYYYMMDD
     */
    public final String expiryDate;
    /**
     * 认证中心哈希算法标识 DF06
     * 标识用来在数字签名方案中产生哈希结果的哈希算法
     */
    public final byte hashAlgorithmIndicator;
    /**
     * 认证中心公钥算法标识 DF07
     * 标识使用在认证中心公钥上的数字签名算法
     */
    public final byte publicKeyAlgorithmIndicator;
    /**
     * 认证中心公钥模数 DF02
     * 认证中心公钥模数部分的值
     */
    public final byte[] publicKeyModulus;
    /**
     * 认证中心公钥指数 DF04
     * 认证中心公钥指数部分的值
     */
    public final byte[] publicKeyExponent;
    /**
     * 认证中心公钥校验值 DF03
     * 用SHA-1算法对认证中心公钥所有部分（RID, 认证中心公钥索引，认证中心公钥模，认证中心公钥指数）的连接计算得到的校验值。
     */
    public final byte[] publicKeyHash;
    /**
     * 参数版本号(NNYYYYMMDDHHMMSS) DF25
     * 其中NN值为04，在下装版本号的时候，表示下装的是第04块参数对应RID的版本号，放在参数块中，
     * 表示参数块数据域中包含的是对应RID的第04块参数，YYYYMMDDHHMMSS用来表示具体的版本号
     */
    public final String paramVersion;

    public Block04_1(byte[] raw) {
        BerTlvParser p = new BerTlvParser();
        BerTlvs tlvs = p.parse(raw);
        rid = tlvs.find(ridTag).getBytesValue();
        publicKeyStoreIndex = tlvs.find(publicKeyStoreIndexTag).getBytesValue()[0];
        publicKeyIndex = tlvs.find(publicKeyIndexTag).getBytesValue()[0];
        expiryDate = tlvs.find(expiryDateTag).getHexValue();
        hashAlgorithmIndicator = tlvs.find(hashAlgorithmIndicatorTag).getBytesValue()[0];
        publicKeyAlgorithmIndicator = tlvs.find(publicKeyAlgorithmIndicatorTag).getBytesValue()[0];
        publicKeyModulus = tlvs.find(publicKeyModulusTag).getBytesValue();
        publicKeyExponent = tlvs.find(publicKeyExponentTag).getBytesValue();
        publicKeyHash = tlvs.find(publicKeyHashTag).getBytesValue();
        paramVersion = tlvs.find(paramVersionTag).getHexValue();
    }
}
