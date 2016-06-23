package com.yada.sdk.device.pos.posp.params;

import java.nio.ByteBuffer;

/**
 * POS下装参数---终端基本参数--参数块第01块处理
 */
public class Block01 {
    private static final int[] paramLengths = new int[]{1, 2, 1, 12, 4, 4, 3, 3, 3, 4, 16, 4, 16, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 25, 20};

    public final boolean useMAC; // 终端是否使用MAC标志
    public final boolean enable; // 是否允许终端使用标志
    public final boolean autoCheck; // 终端自动结帐标志
    public final boolean allowBatchUpdate; // 批上送标志
    public final boolean allowReturn; // 允许退货标志
    public final boolean allowManuallyEnterCardNo; // 手工键入卡号标志
    public final boolean allowMOTO; // 允许MO/TO交易标志
    public final boolean allOffline; // 离线交易标志

    public final boolean useTripleDES; // 使用3DES加密标志
    public final boolean allowBalanceQuery; // 允许余额查询交易标志
    public final boolean allowPay;  // 允许消费交易标志
    public final boolean allowPreAuth; // 允许预授权标志
    public final boolean allowWithdrawals; // 允许取现交易标志
    public final boolean allowAdjust; // 允许调整交易标志
    public final boolean allowTip; // 允许小费交易标志
    public final boolean allowInstallment; // 允许分期付款交易标志

    public final boolean allowIntegral; // 允许积分交易标志
    public final boolean allowPaymentTransfer; // 允许代付交易标志
    public final boolean allowCollections; // 允许代收交易标志
    public final boolean useOper; // 终端使用操作员标志
    public final boolean allowCNChar; // POS汉字类型
    public final boolean supportEMV; // 标志终端是否支持EMV终端
    public final boolean allowCardholderSelectApp; // 标志终端是否允许持卡人自己选择应用
    public final boolean allowFallback; // 是否支持FALLBACK

    public final boolean supportAppDownload; // 终端是否支持程序下装
    public final boolean supportElectronicCash; // 终端是否支持PBOC电子现金（同时具有电子现金余额查询功能和电子现金交易明细查询）
    public final boolean allowAssignLoad; // 指定账户圈存（同时具有电子现金余额查询功能和电子现金交易明细查询）
    public final boolean allowUnspecifiedLoad; // 非指定账户圈存（同时具有电子现金余额查询功能和电子现金交易明细查询）
    public final boolean allowFillBoardLoad; // 补登圈存（同时支持补登余额查询、电子现金余额查询功能和电子现金交易明细查询）

    public final int checkoutMethod; // 结帐方式,POS结帐时 0:以主机为主、1:以终端为主、2:不平不结

    public final int maxOfflineCount; // POS中允许保存的未上送交易笔数
    // 跳过一个备用标志
    public final long maxOfflineAmt; // 离线交易金额上限
    public final int maxAdjustAmtPercentage; // 调整交易上限
    public final int maxTip; // 小费交易上限
    public final String currency1; // POS终端允许的第一个交易币种
    public final String currency2; // POS终端允许的第二个交易币种
    public final String currency3; // POS终端允许的第三个交易币种
    public final String prefixOfDefaultPhone; // 电话拨号前缀
    public final String defaultPhone; // 缺省电话号码
    public final String prefixOfSparePhone; // 备用电话前缀
    public final String sparePhone; // 备用电话号码
    public final int billPrintCount; // 票据打印份数
    public final String plant1; // 分期付款计划ID1
    public final String plant2; // 分期付款计划ID2
    public final String plant3; // 分期付款计划ID3
    public final String plant4; // 分期付款计划ID4
    public final String plant5; // 分期付款计划ID5
    public final String plant6; // 分期付款计划ID6
    public final String plant7; // 分期付款计划ID7
    public final String plant8; // 分期付款计划ID8
    public final String plant9; // 分期付款计划ID9
    public final int timeout; // 超时时间
    public final String enMerName; // 商户名称的英文(拼音)简称
    public final String cnMerName; // 商户名称的中文简称

    public Block01(String raw) {
        byte[] flag = ByteBuffer.allocate(8).putLong(Long.parseLong(raw.substring(0, 8), 16) << 32).array();
        String additionInfo = raw.substring(8);

        useMAC = (flag[0] & 0X80) != 0;
        enable = (flag[0] & 0X40) != 0;
        autoCheck = (flag[0] & 0X20) != 0;
        allowBatchUpdate = (flag[0] & 0X10) != 0;
        allowReturn = (flag[0] & 0X08) != 0;
        allowManuallyEnterCardNo = (flag[0] & 0X04) != 0;
        allowMOTO = (flag[0] & 0X02) != 0;
        allOffline = (flag[0] & 0X01) != 0;

        useTripleDES = (flag[1] & 0X80) != 0;
        allowBalanceQuery = (flag[1] & 0X40) != 0;
        allowPay = (flag[1] & 0X20) != 0;
        allowPreAuth = (flag[1] & 0X10) != 0;
        allowWithdrawals = (flag[0] & 0X08) != 0;
        allowAdjust = (flag[1] & 0X04) != 0;
        allowTip = (flag[1] & 0X02) != 0;
        allowInstallment = (flag[1] & 0X01) != 0;

        allowIntegral = (flag[2] & 0X80) != 0;
        allowPaymentTransfer = (flag[2] & 0X40) != 0;
        allowCollections = (flag[2] & 0X20) != 0;
        useOper = (flag[2] & 0X10) != 0;
        allowCNChar = (flag[2] & 0X08) != 0;
        supportEMV = (flag[2] & 0X04) != 0;
        allowCardholderSelectApp = (flag[2] & 0X02) != 0;
        allowFallback = (flag[2] & 0X01) != 0;

        supportAppDownload = (flag[3] & 0X80) != 0;
        supportElectronicCash = (flag[3] & 0X20) != 0;
        allowAssignLoad = (flag[3] & 0X10) != 0;
        allowUnspecifiedLoad = (flag[3] & 0X08) != 0;
        allowFillBoardLoad = (flag[3] & 0X04) != 0;

        int position = 0;
        checkoutMethod = additionInfo.charAt(position) - '0';
        position += paramLengths[0];
        maxOfflineCount = Integer.parseInt(additionInfo.substring(position, position += paramLengths[1]));
        position += paramLengths[2];// 跳过备用标志
        maxOfflineAmt = Long.parseLong(additionInfo.substring(position, position += paramLengths[3]));
        maxAdjustAmtPercentage = Integer.parseInt(additionInfo.substring(position, position += paramLengths[4]));
        maxTip = Integer.parseInt(additionInfo.substring(position, position += paramLengths[5]));
        currency1 = additionInfo.substring(position, position += paramLengths[6]);
        currency2 = additionInfo.substring(position, position += paramLengths[7]);
        currency3 = additionInfo.substring(position, position += paramLengths[8]);
        prefixOfDefaultPhone = additionInfo.substring(position, position += paramLengths[9]).trim();
        defaultPhone = additionInfo.substring(position, position += paramLengths[10]).trim();
        prefixOfSparePhone = additionInfo.substring(position, position += paramLengths[11]).trim();
        sparePhone = additionInfo.substring(position, position += paramLengths[12]).trim();
        billPrintCount = Integer.parseInt(additionInfo.substring(position, position += paramLengths[13]));
        plant1 = additionInfo.substring(position, position += paramLengths[14]);
        plant2 = additionInfo.substring(position, position += paramLengths[15]);
        plant3 = additionInfo.substring(position, position += paramLengths[16]);
        plant4 = additionInfo.substring(position, position += paramLengths[17]);
        plant5 = additionInfo.substring(position, position += paramLengths[18]);
        plant6 = additionInfo.substring(position, position += paramLengths[19]);
        plant7 = additionInfo.substring(position, position += paramLengths[20]);
        plant8 = additionInfo.substring(position, position += paramLengths[21]);
        plant9 = additionInfo.substring(position, position += paramLengths[22]);
        timeout = Integer.parseInt(additionInfo.substring(position, position += paramLengths[23]));
        enMerName = additionInfo.substring(position, position += paramLengths[24]).trim();
        cnMerName = additionInfo.substring(position, position + paramLengths[25]).trim();
    }
}