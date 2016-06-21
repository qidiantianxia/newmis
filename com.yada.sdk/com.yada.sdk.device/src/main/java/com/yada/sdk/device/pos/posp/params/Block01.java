package com.yada.sdk.device.pos.posp.params;

import com.yada.sdk.device.pos.util.Utils;

public class Block01 {

    private byte[] raw;
    private byte[] hex;

    public Block01(byte[] raw) {
        this.raw = raw;
        byte[] bytes = new byte[8];
        System.arraycopy(raw, 0, bytes, 0, 8);
        this.hex = Utils.ASCII_To_BCD(bytes);
    }

    public boolean useMAC() {
        return (hex[0] & 0X80) != 0;
    }

    public boolean enable() {
        return (hex[0] & 0X40) != 0;
    }

    public boolean autoCheck() {
        return (hex[0] & 0X20) != 0;
    }

    public boolean allowBatchUpdate() {
        return (hex[0] & 0X10) != 0;
    }

    public boolean allowReturn() {
        return (hex[0] & 0X08) != 0;
    }

    public boolean allowManuallyEnterCardNo() {
        return (hex[0] & 0X04) != 0;
    }

    public boolean allowMOTO() {
        return (hex[0] & 0X02) != 0;
    }

    public boolean allOffline() {
        return (hex[0] & 0X01) != 0;
    }

    public boolean useTripleDES() {
        return (hex[1] & 0X80) != 0;
    }

    public boolean allowBalanceQuery() {
        return (hex[1] & 0X40) != 0;
    }

    public boolean allowPay() {
        return (hex[1] & 0X20) != 0;
    }

    public boolean allowPreAuth() {
        return (hex[1] & 0X10) != 0;
    }

    public boolean allowWithdrawals() {
        return (hex[1] & 0X08) != 0;
    }

    public boolean allowAdjust() {
        return (hex[1] & 0X04) != 0;
    }

    public boolean allowTip() {
        return (hex[1] & 0X02) != 0;
    }

    public boolean allowInstallment() {
        return (hex[1] & 0X01) != 0;
    }

    public boolean allowIntegral() {
        return (hex[2] & 0X80) != 0;
    }

    public boolean allowPaymentTransfer() {
        return (hex[2] & 0X40) != 0;
    }

    public boolean allowCollections() {
        return (hex[2] & 0X20) != 0;
    }

    public boolean useOper() {
        return (hex[2] & 0X10) != 0;
    }

    public boolean allowCNChar() {
        return (hex[2] & 0X08) != 0;
    }

    public boolean supportEMV() {
        return (hex[2] & 0X04) != 0;
    }

    public boolean allowCardholderSelectApp() {
        return (hex[2] & 0X02) != 0;
    }

    public boolean allowFallback() {
        return (hex[2] & 0X01) != 0;
    }

    public boolean supportAppDownload() {
        return (hex[3] & 0X80) != 0;
    }

    public boolean supportElectronicCash() {
        return (hex[3] & 0X20) != 0;
    }

    public boolean allowAssignLoad() {
        return (hex[3] & 0X10) != 0;
    }

    public boolean allowUnspecifiedLoad() {
        return (hex[3] & 0X08) != 0;
    }

    public boolean allowFillBoardLoad() {
        return (hex[3] & 0X04) != 0;
    }

    public char checkoutMethod() {
        return (char) raw[8];
    }

    public int maxOfflineCount() {
        return Integer.parseInt(new String(raw, 9, 2));
    }

    public long maxOfflineAmt() {
        return Long.parseLong(new String(raw, 12, 12));
    }

    public int maxAdjustAmtPercentage() {
        return Integer.parseInt(new String(raw, 24, 4));
    }

    public int maxTip() {
        return Integer.parseInt(new String(raw, 28, 4));
    }

    public String currency1() {
        return new String(raw, 32, 3);
    }

    public String currency2() {
        return new String(raw, 35, 3);
    }

    public String currency3() {
        return new String(raw, 38, 3);
    }

    public String prefixOfDefaultPhone() {
        return new String(raw, 41, 4);
    }

    public String defaultPhone() {
        return new String(raw, 45, 16);
    }

    public String prefixOfSparePhone() {
        return new String(raw, 61, 4);
    }

    public String sparePhone() {
        return new String(raw, 65, 16);
    }

    public int billPrintCount() {
        return Integer.parseInt(new String(raw, 81, 2));
    }

    public String plant1() {
        return new String(raw, 83, 4);
    }

    public String plant2() {
        return new String(raw, 87, 4);
    }

    public String plant3() {
        return new String(raw, 91, 4);
    }

    public String plant4() {
        return new String(raw, 95, 4);
    }

    public String plant5() {
        return new String(raw, 99, 4);
    }

    public String plant6() {
        return new String(raw, 103, 4);
    }

    public String plant7() {
        return new String(raw, 107, 4);
    }

    public String plant8() {
        return new String(raw, 111, 4);
    }

    public String plant9() {
        return new String(raw, 115, 4);
    }

    public int timeout() {
        return Integer.parseInt(new String(raw, 119, 2));
    }

    public String enMerName() {
        return (new String(raw, 121, 25)).trim();
    }

    public String cnMerName() {
        return (new String(raw, 146, 25)).trim();
    }
}