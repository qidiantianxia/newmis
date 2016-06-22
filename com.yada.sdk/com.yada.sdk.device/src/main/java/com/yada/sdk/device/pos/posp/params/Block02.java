package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlv;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * Created by cuitao-pc on 16/6/21.
 */
public class Block02 {
  private final BerTlvs params;

  public Block02(byte[] raw) {
    params = new BerTlvParser().parse(raw);
  }

  public BerTlv get(byte[] tag) {
    return params.find(new BerTag(tag, 0, tag.length));
  }
}
