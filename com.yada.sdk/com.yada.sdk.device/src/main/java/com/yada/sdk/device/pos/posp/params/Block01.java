package com.yada.sdk.device.pos.posp.params;

import java.nio.ByteBuffer;

/**
 * POS下装参数---终端基本参数--参数块第01块处理
 * 注释出自IST-v3.0.6的第6.1.1章节
 */
public class Block01 {

    /**
     * 参数长度的数组
     * 前26个参数是由32个bit组成用8个16进制数来表示，数组从第27个参数开始记录参数的长度
     */
    private static final int[] paramLengths = new int[]{1, 2, 1, 12, 4, 4, 3, 3, 3, 4, 16, 4, 16, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 25, 20};

    /**
     * 终端是否使用MAC标志
     * 设置POS在与主机的交易中是否送入MAC:false-不送、true-送
     */
    public final boolean useMAC;
    /**
     * 是否允许终端使用标志
     * 设置POS是否可以允许授权和金融交易：false-不允许、true-允许
     */
    public final boolean enable;
    /**
     * 终端自动结帐标志
     * false-非自动结帐POS、true-自动结帐POS
     */
    public final boolean autoCheck;

    /**
     * 批上送标志
     * false-不批上送、true-批上送
     */
    public final boolean allowBatchUpdate;
    /**
     * 允许退货标志
     * false-不允许退货、true-允许退货
     */
    public final boolean allowReturn;
    /**
     * 手工键入卡号标志
     * false-不允许手工键入卡号、true-允许手工键入卡号
     */
    public final boolean allowManuallyEnterCardNo;
    /**
     * 允许MO/TO交易标志
     * false-不允许MO/TO交易、true-允许MO/TO交易
     */
    public final boolean allowMOTO;
    /**
     * 离线交易标志
     * false-不允许离线交易、true-允许离线交易
     */
    public final boolean allOffline;

    /**
     * 使用3DES加密标志
     * false-不使用3DES加密、true-使用3DES加密
     */
    public final boolean useTripleDES;
    /**
     * 允许余额查询交易标志
     * false-不允许余额查询交易、true-允许余额查询交易
     */
    public final boolean allowBalanceQuery;
    /**
     * 允许消费交易标志
     * false-不允许消费交易、true-允许消费交易
     */
    public final boolean allowPay;
    /**
     * 允许预授权标志
     * false-不允许预授权(及其完成)、true-允许预授权(及其完成)
     */
    public final boolean allowPreAuth;
    /**
     * 允许取现交易标志
     * false-不允许取现交易、true-允许取现交易
     */
    public final boolean allowWithdrawals;
    /**
     * 允许调整交易标志
     * false-不允许调整交易、true-允许调整交易
     */
    public final boolean allowAdjust;
    /**
     * 允许小费交易标志
     * false-不允许小费交易、true-允许小费交易
     */
    public final boolean allowTip;
    /**
     * 允许分期付款交易标志
     * false-不允许分期付款交易、true-允许分期付款交易
     */
    public final boolean allowInstallment;

    /**
     * 允许积分交易标志
     * false-不允许积分交易、true-允许积分交易
     */
    public final boolean allowIntegral;
    /**
     * 允许代付交易标志
     * false-不允许代付交易、true-允许代付交易
     */
    public final boolean allowPaymentTransfer;
    /**
     * 允许代收交易标志
     * false-不允许代收交易、true-允许代收交易
     */
    public final boolean allowCollections;
    /**
     * 终端使用操作员标志
     * 本标志指示是否在本终端上使用操作员,false-不使用操作员、true-使用操作员
     */
    public final boolean useOper;
    /**
     * POS汉字类型
     * 设置POS终端是否允许汉字,false-不允许汉字的POS终端、true-允许汉字的POS终端
     */
    public final boolean allowCNChar;
    /**
     * 标志终端是否支持EMV终端
     * 设置POS终端是否EMV终端,false-不是EMV终端、true-是EMV终端
     */
    public final boolean supportEMV;
    /**
     * 标志终端是否允许持卡人自己选择应用
     * 设置POS终端是否允许持卡人自己选择应用,false-不允许、true-允许
     */
    public final boolean allowCardholderSelectApp;
    /**
     * 是否支持FALLBACK
     * false-不支持、true-支持
     */
    public final boolean allowFallback;

    /**
     * 终端是否支持程序下装
     * 设置POS终端是否支持程序下装,false-不支持、true-支持
     */
    public final boolean supportAppDownload;
    /**
     * 终端是否支持PBOC电子现金（同时具有电子现金余额查询功能和电子现金交易明细查询）
     * false-不支持、true-支持
     */
    public final boolean supportElectronicCash;
    /**
     * 指定账户圈存（同时具有电子现金余额查询功能和电子现金交易明细查询）
     * 终端是否支持,false-不支持、true-支持
     */
    public final boolean allowAssignLoad;
    /**
     * 非指定账户圈存（同时具有电子现金余额查询功能和电子现金交易明细查询）
     * 终端是否支持,false-不支持、true-支持
     */
    public final boolean allowUnspecifiedLoad;
    /**
     * 补登圈存（同时支持补登余额查询、电子现金余额查询功能和电子现金交易明细查询）
     * 终端是否支持,false-不支持、true-支持
     */
    public final boolean allowFillBoardLoad;

    /**
     * 结帐方式
     * POS结帐时 0:以主机为主、1:以终端为主、2:不平不结
     */
    public final int checkoutMethod;

    /**
     * POS中允许保存的未上送交易笔数
     * 值的范围是00-99，如果为00，表示允许保存0笔交易（每笔脱机交易完成后必须立即上送）
     * 04，表示允许POS中保存4笔未上送交易，如果第5笔是脱机交易，则必须在脱机交易完成后，将所有未上送交易上送
     */
    public final int maxOfflineCount;
    /**
     * 离线交易金额上限
     * 离线交易时最大允许交易金额
     */
    public final long maxOfflineAmt;
    /**
     * 调整交易上限
     * 调整交易金额最大允许的值，值为原交易金额的百分比。
     * 例如，如果原交易金额为300元，设置的上限值为200，则调整交易最多可以将交易金额调整为900元。
     * 不需要检查下限
     */
    public final int maxAdjustAmtPercentage;
    /**
     * 小费交易上限
     * 小费交易金额最大允许的值，值为原交易金额的百分比。
     * 例如，如果原交易金额为300元，设置的上限值为20，则小费交易最大可以将交易金额调整为360元，即小费金额最多可以为60元。
     * 不需要检查下限。
     */
    public final int maxTip;
    /**
     * 交易币种1
     * POS终端允许的第一个交易币种
     */
    public final String currency1;
    /**
     * 交易币种2
     * POS终端允许的第二个交易币种
     */
    public final String currency2;
    /**
     * 交易币种3
     * POS终端允许的第三个交易币种
     */
    public final String currency3;
    /**
     * 电话拨号前缀
     * 拨号电话号码的前缀号码
     */
    public final String prefixOfDefaultPhone;
    /**
     * 缺省电话号码
     * 要求POS终端正常交易时所拨的缺省电话号码。
     * POS终端初始安装时会设置另一个初始电话号码，并在第一次拨号的时候拨此初始电话号码。
     * 第一次拨号后POS将从主机下装参数，其中就有本域的电话号码。
     * POS然后将此电话号码设置为缺省的电话号码，即正常交易所使用的电话号码。
     */
    public final String defaultPhone;
    /**
     * 备用电话前缀
     * 备用电话号码的前缀号码
     */
    public final String prefixOfSparePhone;
    /**
     * 备用电话号码
     * POS将从主机下装参数，将此电话号码设置为备用电话号码。
     * 如果正常交易过程中拨号不通，POS可使用此备用电话号码进行拨号。
     */
    public final String sparePhone;
    /**
     * 票据打印份数
     * POS签购单打印的份数
     */
    public final int billPrintCount;
    /**
     * 分期付款计划ID1
     * 缺省的分期付款计划ID
     */
    public final String plant1;
    /**
     * 分期付款计划ID2
     * 允许的分期付款计划ID2
     */
    public final String plant2;
    /**
     * 分期付款计划ID3
     * 允许的分期付款计划ID3
     */
    public final String plant3;
    /**
     * 分期付款计划ID4
     * 允许的分期付款计划ID4
     */
    public final String plant4;
    /**
     * 分期付款计划ID5
     * 允许的分期付款计划ID5
     */
    public final String plant5;
    /**
     * 分期付款计划ID6
     * 允许的分期付款计划ID6
     */
    public final String plant6;
    /**
     * 分期付款计划ID7
     * 允许的分期付款计划ID7
     */
    public final String plant7;
    /**
     * 分期付款计划ID8
     * 允许的分期付款计划ID8
     */
    public final String plant8;
    /**
     * 分期付款计划ID9
     * 允许的分期付款计划ID9
     */
    public final String plant9;
    /**
     * 超时时间
     * POS超时时间，由IST设置
     */
    public final int timeout;
    /**
     * 商户名称的英文(拼音)简称
     * 用于交易成功后打印在签购单上
     */
    public final String enMerName;
    /**
     * 商户名称的中文简称
     * 用于交易成功后打印在签购单上
     */
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