package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;
import com.payneteasy.tlv.HexUtil;

import java.nio.charset.Charset;

/**
 * Created by cuitao-pc on 16/6/21.
 */
public class Block02 {
  private final byte[] appParams;
  private static final BerTag appParamTag = new BerTag(0xdf, 0x29);
  private static final BerTag paramVersionTag = new BerTag(0xdf, 0x25);
  private static final int[] paramLengths = new int[] {16, 16, 21, 21, 7, 7, 1, 2, 1, 20, 20};
  private static final Charset charset = Charset.forName("GBK");
  public final String paramVersion;
  public final String tmsfpuDefaultPhone;
  public final String tmsfpuReservePhone;
  public final String tmsfpuNetworkAddress;
  public final String TmsfpuReserveNetworkAddress;
  public final String termDownloadDate;
  public final String termDownloadLimitDate;
  public final String communication;
  public final int downloadIdleTime;
  public final int tmsFlag;
  public final String username;
  public final String password;

  public Block02(byte[] raw) {
    BerTlvs params = new BerTlvParser().parse(raw);
    appParams = params.find(appParamTag).getBytesValue();
    paramVersion = params.find(paramVersionTag).getHexValue();
    int postion = 0;
    tmsfpuDefaultPhone = new String(appParams, postion, paramLengths[0], charset).trim(); postion += paramLengths[0];
    tmsfpuReservePhone = new String(appParams, postion, paramLengths[1], charset).trim(); postion += paramLengths[1];
    tmsfpuNetworkAddress = new String(appParams, postion, paramLengths[2], charset).trim(); postion += paramLengths[2];
    TmsfpuReserveNetworkAddress = new String(appParams, postion, paramLengths[3], charset).trim(); postion += paramLengths[3];
    termDownloadDate = HexUtil.toHexString(appParams, postion, paramLengths[4]); postion += paramLengths[4];
    termDownloadLimitDate = HexUtil.toHexString(appParams, postion, paramLengths[5]); postion += paramLengths[5];
    communication = new String(appParams, postion, paramLengths[6], charset); postion += paramLengths[6];
    downloadIdleTime = Integer.parseInt(HexUtil.toHexString(appParams, postion, paramLengths[7])); postion += paramLengths[7];
    tmsFlag = appParams[postion]; postion += paramLengths[8];
    username = new String(appParams, postion, paramLengths[9], charset).trim(); postion += paramLengths[9];
    password = new String(appParams, postion, paramLengths[10], charset).trim();
  }


}
