package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;

/**
 * POS下装参数--接触/非接触EMV卡POS应用参数—参数块第03块处理
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

  public final byte[] aid; // 应用标识
  public final byte asi; // 应用选择指示符
  public final byte[] appVersion; // 应用版本号
  public final byte[] tacDefault; // TAC－缺省
  public final byte[] tacOnLine; // TAC－联机
  public final byte[] tacReject; // TAC－拒绝
  public final int terminalFloorLimit; // 终端最低限额
  public final byte[] biasRandomlySelectedThreshold; // 偏置随机选择的阈值
  public final byte biasRandomlySelectedMaxPercentage; // 偏置随机选择的最大目标百分数
  public final byte randomlySelectedPercentage; // 随机选择的目标百分数
  public final byte appFlag; // 应用标志参数1
  public final byte[] ddol; // 缺省DDOL
  public final int terminalCategoryCode; // 终端分类码
  public final int merchantCategoryCode; // 商户分类码
  public final String paramVersion; // 参数版本号
  public final long ecTerminalLimit; // 终端电子现金交易限额
  public final long contactLessOfflineLimit; // 非接触读写器脱机限额
  public final long contactLessLimit; // 非接触读写器交易限额
  public final long contactLessCVMLimit; // 非接触读写器持卡人验证方式
  public final byte[] contactLessTacDefault; // 非接触TAC－缺省
  public final byte[] contactLessTacOnline; // 非接触TAC－联机
  public final byte[] contactLessTacReject; // 非接触TAC－拒绝

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
