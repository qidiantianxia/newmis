package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * POS下装参数--接触/非接触EMV卡POS应用参数—参数块第03块处理
 * 注释出自IST-v3.0.6的第6.1.3章节
 */
public class Block03 {

    private final static BerTag aidTag = new BerTag(0x9f, 0x06);
    private final static BerTag asiTag = new BerTag(0xdf, 0x01);
    private final static BerTag appVersionTag = new BerTag(0x9f, 0x09);
    private final static BerTag tacDefaultTag = new BerTag(0xdf, 0x11);
    private final static BerTag tacOnlineTag = new BerTag(0xdf, 0x12);
    private final static BerTag tacRejectTag = new BerTag(0xdf, 0x13);
    private final static BerTag terminalFloorLimitTag = new BerTag(0x9F, 0x1B);
    private final static BerTag biasRandomlySelectedThresholdTag = new BerTag(0xdf, 0x15);
    private final static BerTag biasRandomlySelectedMaxPercentageTag = new BerTag(0xdf, 0x16);
    private final static BerTag randomlySelectedPercentageTag = new BerTag(0xdf, 0x17);
    private final static BerTag appFlagTag = new BerTag(0xdf, 0x18);
    private final static BerTag ddolTag = new BerTag(0xdf, 0x14);
    private final static BerTag terminalCategoryCodeTag = new BerTag(0x9f, 0x35);
    private final static BerTag merchantCategoryCodeTag = new BerTag(0x9f, 0x15);
    private final static BerTag paramVersionTag = new BerTag(0xdf, 0x25);
    private final static BerTag ecTerminalLimitTag = new BerTag(0x9f, 0x7b);
    private final static BerTag contactLessOfflineLimitTag = new BerTag(0xdf, 0x40);
    private final static BerTag contactLessLimitTag = new BerTag(0xdf, 0x20);
    private final static BerTag contactLessCVMLimitTag = new BerTag(0xdf, 0x21);
    private final static BerTag contactLessTacDefaultTag = new BerTag(0xdf, 0x41);
    private final static BerTag contactLessTacOnlineTag = new BerTag(0xdf, 0x42);
    private final static BerTag contactLessTacRejectTag = new BerTag(0xdf, 0x43);

    /**
     * 应用标识(AID) 9F06
     * AID由注册的应用提供商（RID）以及专用应用标识符扩展（PIX）组成
     */
    public final byte[] aid;
    /**
     * 应用选择指示符(ASI) DF01
     * 指示应用选择时终端上的AID与卡片中的AID是完全匹配（长度和内容都必须一样），还是部分匹配（卡片AID的前面部分与终端AID相同，长度可以更长）。
     * 终端支持的应用列表中的每个AID仅有一个应用选择指示符。01代表允许部分匹配，00代表AID必须完全匹配。
     */
    public final byte asi;
    /**
     * 应用版本号 9F09
     * 支付系统给应用分配的版本号
     */
    public final byte[] appVersion;
    /**
     * TAC－缺省 DF11
     * 标识如果交易可以联机完成但终端没有联机交易能力时，拒绝交易的收单行条件
     */
    public final byte[] tacDefault;
    /**
     * TAC－联机 DF12
     * 标识联机交易的收单行条件
     */
    public final byte[] tacOnLine;
    /**
     * TAC－拒绝 DF13
     * 标识不作联机尝试即拒绝交易的收单行条件
     */
    public final byte[] tacReject;
    /**
     * 终端最低限额 9F1B
     * IC卡消费时终端允许的最低脱机限额（FLOOR LIMIT），交易金额等于及大于本限额的交易必须联机进行。
     * 以人民币分为单位。例如如果最低限额为50元，则本域的值为5000。
     */
    public final int terminalFloorLimit;
    /**
     * 偏置随机选择的阈值 DF15
     * 在终端风险管理中用于随机交易选择的值（在0和terminalFloorLimit之间的值）。
     * 以人民币分为单位。例如如果阀值为40元，则本域的值为4000。
     */
    public final byte[] biasRandomlySelectedThreshold;
    /**
     * 偏置随机选择的最大目标百分数 DF16
     * 在终端风险管理中用于随机交易选择的值
     */
    public final byte biasRandomlySelectedMaxPercentage;
    /**
     * 随机选择的目标百分数 DF17
     * 在终端风险管理中用于随机交易选择的值
     */
    public final byte randomlySelectedPercentage;
    /**
     * 应用标志参数1 DF18
     * b8-b4保留不用
     * b3指示终端对本AID是否支持联机PIN的输入
     * b2指示终端对本AID是否允许PIN BYPASS不输PIN
     * b1指示终端对本AID是否支持ODCV(On Device Cardholder Verification ,仅针对Mastercard paypass使用)
     */
    public final byte appFlag;
    /**
     * 缺省DDOL DF14
     * 卡片中无DDOL时用于构造内部认证命令的DDOL
     */
    public final byte[] ddol;
    /**
     * 终端分类码(终端级别) 9F35
     */
    public final int terminalCategoryCode;
    /**
     * 商户分类码 9F15
     */
    public final int merchantCategoryCode;
    /**
     * 参数版本号(NNYYYYMMDDHHMMSS) DF25
     * 其中NN值为03，在下装版本号的时候，表示下装的是第03块参数对应AID的版本号，放在参数块中，
     * 表示参数块数据域中包含的是对应AID的第03块参数，YYYYMMDDHHMMSS用来表示具体的版本号
     */
    public final String paramVersion;
    /**
     * 终端电子现金交易限额 9F7B
     * 当授权金额小于该限额时，允许电子现金交易（电子现金允许接触或/非接触输入方式）。
     * 以人民币分为单位。例如如果最低限额为50元，则本域的值为5000。
     */
    public final long ecTerminalLimit;
    /**
     * 非接触读写器脱机限额 DF40
     * 如果非接触脱机交易金额数值大于或等于此数值，则交易不允许采用非接触脱机交易。
     * 以人民币分为单位。例如如果最低限额为50元，则本域的值为5000。
     * 注：对visa paywave/Mastercard paypass, 该限额必须设置为全0（不允许脱机交易）
     */
    public final long contactLessOfflineLimit;
    /**
     * 非接触读写器交易限额 DF20
     * 如果非接触交易金额数值大于或等于此数值，则交易终止，允许在其它界面尝试此交易。
     * 以人民币分为单位。例如如果最低限额为50元，则本域的值为5000。
     */
    public final long contactLessLimit;
    /**
     * 非接触读写器持卡人验证方式 DF21
     * 如果非接触交易金额数值大于等于此数值，读写器要求一个持卡人验证方法，联机 PIN 和签名是本部分定义的持卡人验证方法（CVM）。
     * 以人民币分为单位。例如如果最低限额为50元，则本域的值为5000。
     * 注：如果终端和持卡人移动设备都支持ODCV(仅针对Mastercard paypass),该限额也用来表示交易金额大于等于此限额，则必须使用CVM持卡人身份验证。
     */
    public final long contactLessCVMLimit;
    /**
     * 非接触TAC－缺省 DF41
     * 标识如果非接交易可以联机完成但终端没有联机交易能力时，拒绝交易的收单行条件
     */
    public final byte[] contactLessTacDefault;
    /**
     * 非接触TAC－联机 DF42
     * 标识非接联机交易的收单行条件
     */
    public final byte[] contactLessTacOnline;
    /**
     * 非接触TAC－拒绝 DF43
     * 标识非接不作联机尝试即拒绝交易的收单行条件
     */
    public final byte[] contactLessTacReject;

    public Block03(byte[] raw) {
        BerTlvParser p = new BerTlvParser();
        BerTlvs tlvs = p.parse(raw);
        aid = tlvs.find(aidTag).getBytesValue();
        asi = tlvs.find(asiTag).getBytesValue()[0];
        appVersion = tlvs.find(appVersionTag).getBytesValue();
        tacDefault = tlvs.find(tacDefaultTag).getBytesValue();
        tacOnLine = tlvs.find(tacOnlineTag).getBytesValue();
        tacReject = tlvs.find(tacRejectTag).getBytesValue();
        terminalFloorLimit = Integer.parseInt(tlvs.find(terminalFloorLimitTag).getHexValue());
        biasRandomlySelectedThreshold = tlvs.find(biasRandomlySelectedThresholdTag).getBytesValue();
        biasRandomlySelectedMaxPercentage = tlvs.find(biasRandomlySelectedMaxPercentageTag).getBytesValue()[0];
        randomlySelectedPercentage = tlvs.find(randomlySelectedPercentageTag).getBytesValue()[0];
        appFlag = tlvs.find(appFlagTag).getBytesValue()[0];
        ddol = tlvs.find(ddolTag).getBytesValue();
        terminalCategoryCode = Integer.parseInt(tlvs.find(terminalCategoryCodeTag).getHexValue());
        merchantCategoryCode = Integer.parseInt(tlvs.find(merchantCategoryCodeTag).getHexValue());
        paramVersion = tlvs.find(paramVersionTag).getHexValue();
        ecTerminalLimit = Long.parseLong(tlvs.find(ecTerminalLimitTag).getHexValue());
        contactLessOfflineLimit = Long.parseLong(tlvs.find(contactLessOfflineLimitTag).getHexValue());
        contactLessLimit = Long.parseLong(tlvs.find(contactLessLimitTag).getHexValue());
        contactLessCVMLimit = Long.parseLong(tlvs.find(contactLessCVMLimitTag).getHexValue());
        contactLessTacDefault = tlvs.find(contactLessTacDefaultTag).getBytesValue();
        contactLessTacOnline = tlvs.find(contactLessTacOnlineTag).getBytesValue();
        contactLessTacReject = tlvs.find(contactLessTacRejectTag).getBytesValue();
    }
}
