package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;
import com.payneteasy.tlv.HexUtil;

import java.nio.charset.Charset;

/**
 * POS下装参数—下装应用程序用参数---参数块第02块处理
 * 注释出自IST-v3.0.6的第6.1.2章节和第6.2.2.10章节
 */
public class Block02 {

    private static final BerTag appParamTag = new BerTag(0xdf, 0x29);
    private static final BerTag paramVersionTag = new BerTag(0xdf, 0x25);
    private static final int[] paramLengths = new int[]{16, 16, 21, 21, 7, 7, 1, 2, 1, 20, 20};
    private static final Charset charset = Charset.forName("GBK");

    /**
     * 参数版本号(NNYYYYMMDDHHMMSS) DF25
     * 其中NN值为02，指示的是参数类型，在下装版本号的时候，表示下装的是第02块参数的版本号，放在参数块中，表示参数块数据域中包含的是第02块参数
     * YYYYMMDDHHMMSS用来表示具体的版本号
     */
    public final String paramVersion;
    /**
     * TMSFPU缺省电话号码
     * 通过拨号方式到TMSFPU进行程序更新时，要求POS终端所拨的缺省电话号码。
     * 注意：如果在POS下装参数块A中的电话拨号前缀存在，则在拨本号之前需要加拨下装参数块A中电话拨号前缀。
     */
    public final String tmsfpuDefaultPhone;
    /**
     * TMSFPU备用电话号码
     * 通过拨号方式到TMSFPU进行程序更新时，要求POS终端所拨的备用电话号码。
     * 注意：如果在POS下装参数块A中的备用电话拨号前缀存在，则在拨本号之前需要加拨下装参数块A中备用电话拨号前缀。
     */
    public final String tmsfpuReservePhone;
    /**
     * TMSFPU缺省TCP/IP地址和端口
     * 通过TCP/IP方式到TMSFPU进行程序更新时，对应TMSFPU的缺省IP地址和端口号，
     * 例如格式为：20.121.11.250:28101
     */
    public final String tmsfpuNetworkAddress;
    /**
     * TMSFPU备用TCP/IP地址和端口
     * 通过TCP/IP方式到TMSFPU进行程序更新时，对应TMSFPU的备用IP地址和端口号，
     * 例如格式为：20.121.11.250:28101
     */
    public final String tmsfpuReserveNetworkAddress;
    /**
     * 终端下载程序日期时间
     * 日期时间格式为：YYYYMMDDHHMMSS
     */
    public final String termDownloadDate;
    /**
     * 终端下载限制日期时间
     * 日期时间格式为：YYYYMMDDHHMMSS
     */
    public final String termDownloadLimitDate;
    /**
     * 通讯方式
     * 值的含义,1-异步modern、2-IEN、3-以太网方式、4-GPRS,5-CDMA,默认值为1
     */
    public final String communication;
    /**
     * 下载前空闲时间
     * 以分钟计算，默认值为10分钟
     */
    public final int downloadIdleTime;
    /**
     * TMS用标志参数
     * 值的含义,0-不发送版本号给TMS、1-发送版本号给TMS
     */
    public final int tmsFlag;
    /**
     * 用户名
     */
    public final String username;
    /**
     * 密码
     */
    public final String password;

    public Block02(byte[] raw) {
        BerTlvs params = new BerTlvParser().parse(raw);
        byte[] appParams = params.find(appParamTag).getBytesValue();
        paramVersion = params.find(paramVersionTag).getHexValue();
        int position = 0;
        tmsfpuDefaultPhone = new String(appParams, position, paramLengths[0], charset).trim();
        position += paramLengths[0];
        tmsfpuReservePhone = new String(appParams, position, paramLengths[1], charset).trim();
        position += paramLengths[1];
        tmsfpuNetworkAddress = new String(appParams, position, paramLengths[2], charset).trim();
        position += paramLengths[2];
        tmsfpuReserveNetworkAddress = new String(appParams, position, paramLengths[3], charset).trim();
        position += paramLengths[3];
        termDownloadDate = HexUtil.toHexString(appParams, position, paramLengths[4]);
        position += paramLengths[4];
        termDownloadLimitDate = HexUtil.toHexString(appParams, position, paramLengths[5]);
        position += paramLengths[5];
        communication = new String(appParams, position, paramLengths[6], charset);
        position += paramLengths[6];
        downloadIdleTime = Integer.parseInt(HexUtil.toHexString(appParams, position, paramLengths[7]));
        position += paramLengths[7];
        tmsFlag = appParams[position];
        position += paramLengths[8];
        username = new String(appParams, position, paramLengths[9], charset).trim();
        position += paramLengths[9];
        password = new String(appParams, position, paramLengths[10], charset).trim();
    }


}
