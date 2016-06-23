package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * POS下装参数—国密算法银联新公钥参数--参数块第04块处理
 */
public class Block04_2 {

  private final static BerTag serviceIdTag = new BerTag(0xdf, 0x36);
  private final static BerTag ridTag = new BerTag(0x9f, 0x06);
  private final static BerTag publicKeyIndexTag = new BerTag(0x9f, 0x22);
  private final static BerTag expiryDateTag = new BerTag(0xdf, 0x37);
  private final static BerTag hashAlgorithmIndicatorTag = new BerTag(0xdf, 0x07);
  private final static BerTag publicKeyAlgorithmIndicatorTag = new BerTag(0xdf, 0x38);
  private final static BerTag publicKeyModulusTag = new BerTag(0xdf, 0x02);
  private final static BerTag publicKeyHashTag = new BerTag(0xdf, 0x39);
  private final static BerTag publicKeyStoreIndexTag = new BerTag(0xdf, 0x28);
  private final static BerTag paramVersionTag = new BerTag(0xdf, 0x25);

  public final byte publicKeyStoreIndex; // 对应该RID的公钥编号
  public final byte publicKeyIndex; // 认证中心公钥索引
  public final String expiryDate; // 认证中心公钥失效期(YYYYMMDD)
  public final byte hashAlgorithmIndicator; // 认证中心公钥签名算法标识
  public final byte publicKeyAlgorithmIndicator; // 公钥参数标识
  public final byte[] publicKeyModulus; // 根CA公钥
  public final byte[] publicKeyHash; // 数字签名
  public final String paramVersion; // 公钥需要更新的版本号信息
  public final byte[] rid; // 注册应用提供者标识符
  public final byte[] serviceId; // 服务标识

  public Block04_2(byte[] raw) {
    BerTlvParser p = new BerTlvParser();
    BerTlvs tlvs = p.parse(raw);
    serviceId = tlvs.find(serviceIdTag).getBytesValue();
    rid = tlvs.find(ridTag).getBytesValue();
    publicKeyStoreIndex = tlvs.find(publicKeyStoreIndexTag).getBytesValue()[0];
    publicKeyIndex = tlvs.find(publicKeyIndexTag).getBytesValue()[0];
    expiryDate = tlvs.find(expiryDateTag).getHexValue();
    hashAlgorithmIndicator = tlvs.find(hashAlgorithmIndicatorTag).getBytesValue()[0];
    publicKeyAlgorithmIndicator = tlvs.find(publicKeyAlgorithmIndicatorTag).getBytesValue()[0];
    publicKeyModulus = tlvs.find(publicKeyModulusTag).getBytesValue();
    publicKeyHash = tlvs.find(publicKeyHashTag).getBytesValue();
    paramVersion = tlvs.find(paramVersionTag).getHexValue();
  }
}
