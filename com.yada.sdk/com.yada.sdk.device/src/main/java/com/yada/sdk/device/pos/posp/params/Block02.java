package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * Created by cuitao-pc on 16/6/21.
 */
public class Block02 {
  private final String appParams;
  private static final BerTag appParamTag = new BerTag(0xdf, 0x29);
  private static final BerTag paramVersionTag = new BerTag(0xdf, 0x25);
  private static final int[] paramLengths = new int[] {16, 16, 21, 21, 7, 7, 1, 2, 1, 20, 20};
  public final String paramVersion;
  public final String tmsfpuDefaultPhone;
  public final String tmsfpuReservePhone;
  public final String tmsfpuNetworkAddress;
  public final String TmsfpuReserveNetworkAddress;
  public final String termDownloadDate;
  public final String termDownloadLimitDate;
  public final String communication;
  public final String downloadIdleTime;
  public final String tmsFlag;
  public final String username;
  public final String password;

  public Block02(byte[] raw) {
    BerTlvs params = new BerTlvParser().parse(raw);
    appParams = params.find(appParamTag).getTextValue();
    paramVersion = params.find(paramVersionTag).getTextValue();
    int postion = 0;
    tmsfpuDefaultPhone = appParams.substring(postion, postion += paramLengths[0]);
    tmsfpuReservePhone = appParams.substring(postion, postion += paramLengths[1]);
    tmsfpuNetworkAddress = appParams.substring(postion, postion += paramLengths[2]);
    TmsfpuReserveNetworkAddress = appParams.substring(postion, postion += paramLengths[3]);
    termDownloadDate = appParams.substring(postion, postion += paramLengths[4]);
    termDownloadLimitDate = appParams.substring(postion, postion += paramLengths[5]);
    communication = appParams.substring(postion, postion += paramLengths[6]);
    downloadIdleTime = appParams.substring(postion, postion += paramLengths[7]);
    tmsFlag = appParams.substring(postion, postion += paramLengths[8]);
    username = appParams.substring(postion, postion += paramLengths[9]);
    password = appParams.substring(postion, postion + paramLengths[10]);
  }


}
