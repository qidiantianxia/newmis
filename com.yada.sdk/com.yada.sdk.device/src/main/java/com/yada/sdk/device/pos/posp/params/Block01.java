package com.yada.sdk.device.pos.posp.params;

import java.nio.ByteBuffer;

public class Block01 {
  private byte[] flag;
  private String additionInfo;

  public Block01(String raw) {
    flag = ByteBuffer.allocate(8).putLong(Long.parseLong(raw.substring(0, 8), 16) << 32).array();
    additionInfo = raw.substring(8);
  }

  public boolean useMAC() {
    return (flag[0] & 0X80) != 0;
  }

  public boolean enable() {
    return (flag[0] & 0X40) != 0;
  }

  public boolean autoCheck() {
    return (flag[0] & 0X20) != 0;
  }

  public boolean allowBatchUpdate() {
    return (flag[0] & 0X10) != 0;
  }

  public boolean allowReturn() {
    return (flag[0] & 0X08) != 0;
  }

  public boolean allowManuallyEnterCardNo() {
    return (flag[0] & 0X04) != 0;
  }

  public boolean allowMOTO() {
    return (flag[0] & 0X02) != 0;
  }

  public boolean allOffline() {
    return (flag[0] & 0X01) != 0;
  }

  public boolean useTripleDES() {
    return (flag[1] & 0X80) != 0;
  }

  public boolean allowBalanceQuery() {
    return (flag[1] & 0X40) != 0;
  }

  public boolean allowPay() {
    return (flag[1] & 0X20) != 0;
  }

  public boolean allowPreAuth() {
    return (flag[1] & 0X10) != 0;
  }

  public boolean allowWithdrawals() {
    return (flag[1] & 0X08) != 0;
  }

  public boolean allowAdjust() {
    return (flag[1] & 0X04) != 0;
  }

  public boolean allowTip() {
    return (flag[1] & 0X02) != 0;
  }

  public boolean allowInstallment() {
    return (flag[1] & 0X01) != 0;
  }

  public boolean allowIntegral() {
    return (flag[2] & 0X80) != 0;
  }

  public boolean allowPaymentTransfer() {
    return (flag[2] & 0X40) != 0;
  }

  public boolean allowCollections() {
    return (flag[2] & 0X20) != 0;
  }

  public boolean useOper() {
    return (flag[2] & 0X10) != 0;
  }

  public boolean allowCNChar() {
    return (flag[2] & 0X08) != 0;
  }

  public boolean supportEMV() {
    return (flag[2] & 0X04) != 0;
  }

  public boolean allowCardholderSelectApp() {
    return (flag[2] & 0X02) != 0;
  }

  public boolean allowFallback() {
    return (flag[2] & 0X01) != 0;
  }

  public boolean supportAppDownload() {
    return (flag[3] & 0X80) != 0;
  }

  public boolean supportElectronicCash() {
    return (flag[3] & 0X20) != 0;
  }

  public boolean allowAssignLoad() {
    return (flag[3] & 0X10) != 0;
  }

  public boolean allowUnspecifiedLoad() {
    return (flag[3] & 0X08) != 0;
  }

  public boolean allowFillBoardLoad() {
    return (flag[3] & 0X04) != 0;
  }

  public int checkoutMethod() {
    return additionInfo.charAt(0) - '0';
  }

  public int maxOfflineCount() {
    return Integer.parseInt(additionInfo.substring(1, 3));
  }

  public long maxOfflineAmt() {
    return Long.parseLong(additionInfo.substring(4, 16));
  }

  public int maxAdjustAmtPercentage() {
    return Integer.parseInt(additionInfo.substring(16, 20));
  }

  public int maxTip() {
    return Integer.parseInt(additionInfo.substring(20, 24));
  }

  public String currency1() {
    return additionInfo.substring(24, 27);
  }

  public String currency2() {
    return additionInfo.substring(27, 30);
  }

  public String currency3() {
    return additionInfo.substring(30, 33);
  }

  public String prefixOfDefaultPhone() {
    return additionInfo.substring(33, 37);
  }

  public String defaultPhone() {
    return additionInfo.substring(37, 53);
  }

  public String prefixOfSparePhone() {
    return additionInfo.substring(53, 57);
  }

  public String sparePhone() {
    return additionInfo.substring(57, 73);
  }

  public int billPrintCount() {
    return Integer.parseInt(additionInfo.substring(73, 75));
  }

  public String plant1() {
    return additionInfo.substring(75, 79);
  }

  public String plant2() {
    return additionInfo.substring(79, 83);
  }

  public String plant3() {
    return additionInfo.substring(83, 87);
  }

  public String plant4() {
    return additionInfo.substring(87, 91);
  }

  public String plant5() {
    return additionInfo.substring(91, 95);
  }

  public String plant6() {
    return additionInfo.substring(95, 99);
  }

  public String plant7() {
    return additionInfo.substring(99, 103);
  }

  public String plant8() {
    return additionInfo.substring(103, 107);
  }

  public String plant9() {
    return additionInfo.substring(107, 111);
  }

  public int timeout() {
    return Integer.parseInt(additionInfo.substring(111, 113));
  }

  public String enMerName() {
    return additionInfo.substring(113, 138).trim();
  }

  public String cnMerName() {
    return additionInfo.substring(138).trim();
  }
}