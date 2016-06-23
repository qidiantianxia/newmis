package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;
import com.payneteasy.tlv.HexUtil;

import java.nio.charset.Charset;

/**
 * POS下装参数—下装应用程序用参数---参数块第02块处理
 */
public class Block02 {
  private final byte[] appParams;
  private static final BerTag appParamTag = new BerTag(0xdf, 0x29);
  private static final BerTag paramVersionTag = new BerTag(0xdf, 0x25);
  private static final int[] paramLengths = new int[] {16, 16, 21, 21, 7, 7, 1, 2, 1, 20, 20};
  private static final Charset charset = Charset.forName("GBK");
  public final String paramVersion; // 参数版本号
  public final String tmsfpuDefaultPhone; // TMSFPU缺省电话号码
  public final String tmsfpuReservePhone; // TMSFPU备用电话号码
  public final String tmsfpuNetworkAddress; // TMSFPU缺省TCP/IP地址和端口
  public final String TmsfpuReserveNetworkAddress; // TMSFPU备用TCP/IP地址和端口
  public final String termDownloadDate; // 终端下载程序日期时间(YYYYMMDDHHMMSS)
  public final String termDownloadLimitDate; // 终端下载限制日期时间(YYYYMMDDHHMMSS)
  public final String communication; // 通讯方式1-异步modern,2-IEN,3-以太网方式,4-GPRS,5-CDMA默认值为‘1
  public final int downloadIdleTime; // 下载前空闲时间 以分钟计算，默认值为10分钟
  public final int tmsFlag; // TMS用标志参数
  public final String username; // 用户名
  public final String password; // 密码

  public Block02(byte[] raw) {
    BerTlvs params = new BerTlvParser().parse(raw);
    appParams = params.find(appParamTag).getBytesValue();
    paramVersion = params.find(paramVersionTag).getHexValue();
    int position = 0;
    tmsfpuDefaultPhone = new String(appParams, position, paramLengths[0], charset).trim(); position += paramLengths[0];
    tmsfpuReservePhone = new String(appParams, position, paramLengths[1], charset).trim(); position += paramLengths[1];
    tmsfpuNetworkAddress = new String(appParams, position, paramLengths[2], charset).trim(); position += paramLengths[2];
    TmsfpuReserveNetworkAddress = new String(appParams, position, paramLengths[3], charset).trim(); position += paramLengths[3];
    termDownloadDate = HexUtil.toHexString(appParams, position, paramLengths[4]); position += paramLengths[4];
    termDownloadLimitDate = HexUtil.toHexString(appParams, position, paramLengths[5]); position += paramLengths[5];
    communication = new String(appParams, position, paramLengths[6], charset); position += paramLengths[6];
    downloadIdleTime = Integer.parseInt(HexUtil.toHexString(appParams, position, paramLengths[7])); position += paramLengths[7];
    tmsFlag = appParams[position]; position += paramLengths[8];
    username = new String(appParams, position, paramLengths[9], charset).trim(); position += paramLengths[9];
    password = new String(appParams, position, paramLengths[10], charset).trim();
  }


}
