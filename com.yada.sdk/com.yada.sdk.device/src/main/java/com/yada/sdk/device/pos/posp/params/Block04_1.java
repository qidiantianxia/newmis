package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * Created by cuitao-pc on 16/6/22.
 */
public class Block04_1 {
  private final static BerTag ridTag = new BerTag(0x9f, 0x06);
  private final static BerTag publicKeyStoreIndexTag = new BerTag(0xdf, 0x28);
  private final static BerTag publicKeyIndexTag = new BerTag(0xdf, 0x22);
  private final static BerTag expiryDateTag = new BerTag(0xdf, 0x05);
  private final static BerTag hashAlgorithmIndicatorTag = new BerTag(0xdf, 0x06);
  private final static BerTag publicKeyAlgorithmIndicatorTag = new BerTag(0xdf, 0x07);
  private final static BerTag publicKeyModulusTag = new BerTag(0xdf, 0x02);
  private final static BerTag publicKeyExponentTag = new BerTag(0xdf, 0x04);
  private final static BerTag publicKeyHashTag = new BerTag(0xdf, 0x03);
  private final static BerTag paramVersionTag = new BerTag(0xdf, 0x25);
  public final byte[] rid;
  public final byte publicKeyStoreIndex;
  public final byte publicKeyIndex;
  public final String expiryDate;
  public final byte hashAlgorithmIndicator;
  public final byte publicKeyAlgorithmIndicator;
  public final byte[] publicKeyModulus;
  public final byte[] publicKeyExponent;
  public final byte[] publicKeyHash;
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
