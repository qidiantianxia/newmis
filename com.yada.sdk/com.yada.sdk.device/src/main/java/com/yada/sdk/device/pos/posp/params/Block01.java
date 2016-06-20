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

  public boolean allowPreAuth() {
    return raw[11] == 1;
  }

  public boolean allowWithdrawals() {
    return raw[12] == 1;
  }

  public boolean allowAdjust() {
    return raw[13] == 1;
  }

  public boolean allowTip() {
    return raw[14] == 1;
  }

  public boolean allowInstallment() {
    return raw[15] == 1;
  }

  public boolean allowIntegral() {
    return raw[16] == 1;
  }

  public boolean allowPaymentTransfer() {
    return raw[17] == 1;
  }

  public boolean allowCollections() {
    return raw[18] == 1;
  }

  public boolean useOper() {
    return raw[19] == 1;
  }

  public boolean allowCNChar() {
    return raw[20] == 1;
  }

  public boolean supportEMV() {
    return raw[21] == 1;
  }

  public boolean allowCardholderSelectApp() {
    return raw[22] == 1;
  }

  public boolean allowFallback() {
    return raw[23] == 1;
  }

  public boolean supportAppDownload() {
    return raw[24] == 1;
  }

  public boolean supportElectronicCash() {
    return (raw[25] & 0x40) != 0;
  }

  public boolean allowAssignLoad() {
    return (raw[25] & 0x20) != 0;
  }

  public boolean allowUnspecifiedLoad() {
    return (raw[25] & 0x10) != 0;
  }

  public boolean allowFillBoardLoad() {
    return (raw[25] & 0x8) != 0;
  }

  public char checkoutMethod() {
    return (char) raw[26];
  }

  public int maxOfflineCount() {
    return Integer.parseInt(new String(raw, 27, 2));
  }

  public long maxOfflineAmt() {
    return Long.parseLong(new String(raw, 30, 12));
  }

  public int maxAdjustAmtPercentage() {
    return Integer.parseInt(new String(raw, 42, 4));
  }

  public int maxTip() {
    return Integer.parseInt(new String(raw, 46, 4));
  }

  public String currency1() {
    return new String(raw, 52, 3);
  }

  public String currency2() {
    return new String(raw, 55, 3);
  }

  public String currency3() {
    return new String(raw, 58, 3);
  }

  public String prefixOfDefaultPhone() {
    return new String(raw, 61, 4);
  }

  public String defaultPhone() {
    return new String(raw, 65, 16);
  }

  public String prefixOfSparePhone() {
    return new String(raw, 81, 4);
  }

  public String sparePhone() {
    return new String(raw, 85, 16);
  }

  public int billPrintCount() {
    return Integer.parseInt(new String(raw, 101, 2));
  }

  public String plant1() {
    return new String(raw, 103, 4);
  }

  public String plant2() {
    return new String(raw, 107, 4);
  }

  public String plant3() {
    return new String(raw, 111, 4);
  }

  public String plant4() {
    return new String(raw, 115, 4);
  }

  public String plant5() {
    return new String(raw, 119, 4);
  }

  public String plant6() {
    return new String(raw, 123, 4);
  }

  public String plant7() {
    return new String(raw, 127, 4);
  }

  public String plant8() {
    return new String(raw, 131, 4);
  }

  public String plant9() {
    return new String(raw, 135, 4);
  }

  public int timeout() {
    return Integer.parseInt(new String(raw, 139, 2));
  }

  public String enMerName() {
    return (new String(raw, 141, 25)).trim();
  }

  public String cnMerName() {
    return (new String(raw, 166, 20)).trim();
  }
}
