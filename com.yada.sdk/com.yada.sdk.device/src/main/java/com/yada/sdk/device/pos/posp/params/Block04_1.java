package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * 6.1.4	POS下装参数—国际算法公钥参数--参数块第04块处理
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

  public final byte[] rid; // 注册应用提供者标识符
  public final byte publicKeyStoreIndex; // 公钥存储位置索引
  public final byte publicKeyIndex; // 认证中心公钥索引
  public final String expiryDate; // 认证中心公钥失效期(YYYYMMDD)
  public final byte hashAlgorithmIndicator; // 认证中心哈什算法标识
  public final byte publicKeyAlgorithmIndicator; // 认证中心公钥算法标识
  public final byte[] publicKeyModulus; // 认证中心公钥模
  public final byte[] publicKeyExponent; // 认证中心公钥指数
  public final byte[] publicKeyHash; // 认证中心公钥校验值
  public final String paramVersion; // 参数版本号

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
