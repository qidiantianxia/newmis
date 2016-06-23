package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * POS下装参数—国密算法银联新公钥参数--参数块第04块处理
 * 注释出自IST-v3.0.6的第6.1.5章节
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

  /**
   * 公钥存储位置索引 DF28
   * 表示对应该RID的第几把公钥，取值范围：07-12
   * 注意值01-06范围用于银联国际算法的公钥定义，国密算法6把公钥每把公钥的对应该域记录的值不能相同必须唯一。
   */
  public final byte publicKeyStoreIndex;
  /**
   * 认证中心公钥索引 9F22
   * 和RID一起指定对应银联国密算法的某个认证中心公钥
   */
  public final byte publicKeyIndex;
  /**
   * 认证中心公钥失效期 DF37
   * 公钥失效日期：YYYYMMDD
   */
  public final String expiryDate;
  /**
   * 认证中心公钥签名算法标识 DF07
   * 标识使用在认证中心公钥上的数字签名算法。
   * 其值为：04’表示使用SM2算法，该值也是默认值
   */
  public final byte hashAlgorithmIndicator;
  /**
   * 公钥参数标识 DF38
   * 用于标识椭圆曲线参数
   * 其值为：11’表示国密SM2推荐曲线参数，其它保留，该值也是默认值
   */
  public final byte publicKeyAlgorithmIndicator;
  /**
   * 根CA公钥 DF02
   * 认证中心公钥模数部分的值
   */
  public final byte[] publicKeyModulus;
  /**
   * 数字签名 DF39
   * 表示将(记录头、服务标识、RID、认证中心公钥索引、认证中心公钥失效期、认证中心公钥签名算法标识、公钥参数标识、根CA公钥)
   * 用根CA计算SM2签名的计算结果r||s   默认值为全‘0’字符。
   */
  public final byte[] publicKeyHash;
  /**
   * 参数版本号(NNYYYYMMDDHHMMSS) DF25
   * 其中NN值为04，在下装版本号的时候，表示下装的是第04块参数对应RID的版本号，放在参数块中，
   * 表示参数块数据域中包含的是对应RID的第04块参数，YYYYMMDDHHMMSS用来表示具体的版本号
   */
  public final String paramVersion;
  /**
   * 注册应用提供者标识符 9F06
   * 是AID的前5位，与认证中心公钥索引一起唯一标识出一个公钥
   */
  public final byte[] rid;
  /**
   * 服务标识 DF36
   * 标识一个中国银联借记贷记服务，将相应应用的私有应用标识扩展(PIX)，右补十六进制‘0’构成:
   * 01010000标识“借、贷记”、01010100标识借记、01010200标识贷记、01010300标识准贷记，默认值为：“01010000”
   */
  public final byte[] serviceId;

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
