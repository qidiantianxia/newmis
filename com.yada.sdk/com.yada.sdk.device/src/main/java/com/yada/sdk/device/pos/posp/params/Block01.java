package com.yada.sdk.device.pos.posp.params;

import java.nio.ByteBuffer;

public class Block01 {
    private static final int[] paramLengths = new int[]{1, 2, 1, 12, 4, 4, 3, 3, 3, 4, 16, 4, 16, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 25, 20};

    public final boolean useMAC;
    public final boolean enable;
    public final boolean autoCheck;
    public final boolean allowBatchUpdate;
    public final boolean allowReturn;
    public final boolean allowManuallyEnterCardNo;
    public final boolean allowMOTO;
    public final boolean allOffline;

    public final boolean useTripleDES;
    public final boolean allowBalanceQuery;
    public final boolean allowPay;
    public final boolean allowPreAuth;
    public final boolean allowWithdrawals;
    public final boolean allowAdjust;
    public final boolean allowTip;
    public final boolean allowInstallment;

    public final boolean allowIntegral;
    public final boolean allowPaymentTransfer;
    public final boolean allowCollections;
    public final boolean useOper;
    public final boolean allowCNChar;
    public final boolean supportEMV;
    public final boolean allowCardholderSelectApp;
    public final boolean allowFallback;

    public final boolean supportAppDownload;
    public final boolean supportElectronicCash;
    public final boolean allowAssignLoad;
    public final boolean allowUnspecifiedLoad;
    public final boolean allowFillBoardLoad;

    public final int checkoutMethod;
    public final int maxOfflineCount;
    // 跳过一个备用标志
    public final long maxOfflineAmt;
    public final int maxAdjustAmtPercentage;
    public final int maxTip;
    public final String currency1;
    public final String currency2;
    public final String currency3;
    public final String prefixOfDefaultPhone;
    public final String defaultPhone;
    public final String prefixOfSparePhone;
    public final String sparePhone;
    public final int billPrintCount;
    public final String plant1;
    public final String plant2;
    public final String plant3;
    public final String plant4;
    public final String plant5;
    public final String plant6;
    public final String plant7;
    public final String plant8;
    public final String plant9;
    public final int timeout;
    public final String enMerName;
    public final String cnMerName;

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
        // 跳过备用标志
        position += paramLengths[2];
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