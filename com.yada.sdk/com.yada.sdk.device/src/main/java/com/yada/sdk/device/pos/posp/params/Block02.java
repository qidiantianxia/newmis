package com.yada.sdk.device.pos.posp.params;

import com.yada.sdk.packages.comm.Tlv;

/**
 * Created by cuitao-pc on 16/6/21.
 */
public class Block02 {
  private final Tlv[] params;

  public Block02(byte[] raw) {
    params = new Tlv(raw).getChildren();
  }

  public Tlv get(byte[] tag) {
    for(Tlv tlv : params) {
      if(tlv.getTag()[0] == tag[0] && tlv.getTag()[1] == tag[1])
        return tlv;
    }

    return null;
  }
}
