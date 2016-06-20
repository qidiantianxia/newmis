package com.yada.sdk.device.pos.posp.params;

/**
 * Created by cuitao-pc on 16/6/21.
 */
public class Block01 {
  private byte[] raw;

  public Block01(byte[] raw) {
    this.raw = raw;
  }

  public boolean useMAC() {
    return raw[0] != 0;
  }

  public boolean enable() {
    return raw[1] == 1;
  }

  public boolean autoCheck() {
    return raw[2] == 1;
  }

  public boolean allowBatchUpdate() {
    return raw[3] == 1;
  }

  public boolean allowReturn() {
    return raw[4] == 1;
  }

  public boolean allowManuallyEnterCardNo() {
    return raw[5] == 1;
  }

  public boolean allowMOTO() {
    return raw[6] == 1;
  }

  public boolean allOffline() {
    return raw[7] == 1;
  }

  public boolean useTripleDES() {
    return raw[8] == 1;
  }

  public boolean allowBalanceQuery() {
    return raw[9] == 1;
  }

  public boolean allowPay() {
    return raw[10] == 1;
  }
}
