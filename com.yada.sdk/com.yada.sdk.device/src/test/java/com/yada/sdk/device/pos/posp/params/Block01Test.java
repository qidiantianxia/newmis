package com.yada.sdk.device.pos.posp.params;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Block01Test {

    Block01 block01;

    @Before
    public void before() {
        // TODO 暂时没有取到非接的参数，先手动添加上非接的参数做测试，后续拿到非接的参数在补全
        String param01 = "6dffff80000000000010000001500150156156156    1554                1554            01IP01IP02IP03IP04IP05IP06IP07IP08IP0950WEE                      POS 厂商测试商户         000000010000000000010000A";
        block01 = new Block01(param01);
    }

    @Test
    public void test() {
        Assert.assertFalse(block01.useMAC);
        Assert.assertTrue(block01.enable);
        Assert.assertTrue(block01.autoCheck);
        Assert.assertFalse(block01.allowBatchUpdate);
        Assert.assertTrue(block01.allowReturn);
        Assert.assertTrue(block01.allowManuallyEnterCardNo);
        Assert.assertFalse(block01.allowMOTO);
        Assert.assertTrue(block01.allOffline);
        Assert.assertTrue(block01.useTripleDES);
        Assert.assertTrue(block01.allowBalanceQuery);
        Assert.assertTrue(block01.allowPay);
        Assert.assertTrue(block01.allowPreAuth);
        Assert.assertTrue(block01.allowWithdrawals);
        Assert.assertTrue(block01.allowAdjust);
        Assert.assertTrue(block01.allowTip);
        Assert.assertTrue(block01.allowInstallment);
        Assert.assertTrue(block01.allowIntegral);
        Assert.assertTrue(block01.allowPaymentTransfer);
        Assert.assertTrue(block01.allowCollections);
        Assert.assertTrue(block01.useOper);
        Assert.assertTrue(block01.allowCNChar);
        Assert.assertTrue(block01.supportEMV);
        Assert.assertTrue(block01.allowCardholderSelectApp);
        Assert.assertTrue(block01.allowFallback);
        Assert.assertTrue(block01.supportAppDownload);
        Assert.assertFalse(block01.supportElectronicCash);
        Assert.assertFalse(block01.allowAssignLoad);
        Assert.assertFalse(block01.allowUnspecifiedLoad);
        Assert.assertFalse(block01.allowFillBoardLoad);
        Assert.assertEquals(0, block01.checkoutMethod);
        Assert.assertEquals(0, block01.maxOfflineCount);
        Assert.assertEquals(100000, block01.maxOfflineAmt);
        Assert.assertEquals(150, block01.maxAdjustAmtPercentage);
        Assert.assertEquals(150, block01.maxTip);
        Assert.assertEquals("156", block01.currency1);
        Assert.assertEquals("156", block01.currency2);
        Assert.assertEquals("156", block01.currency3);
        Assert.assertEquals("", block01.prefixOfDefaultPhone);
        Assert.assertEquals("1554", block01.defaultPhone);
        Assert.assertEquals("", block01.prefixOfSparePhone);
        Assert.assertEquals("1554", block01.sparePhone);
        Assert.assertEquals(1, block01.billPrintCount);
        Assert.assertEquals("IP01", block01.plant1);
        Assert.assertEquals("IP02", block01.plant2);
        Assert.assertEquals("IP03", block01.plant3);
        Assert.assertEquals("IP04", block01.plant4);
        Assert.assertEquals("IP05", block01.plant5);
        Assert.assertEquals("IP06", block01.plant6);
        Assert.assertEquals("IP07", block01.plant7);
        Assert.assertEquals("IP08", block01.plant8);
        Assert.assertEquals("IP09", block01.plant9);
        Assert.assertEquals(50, block01.timeout);
        Assert.assertEquals("WEE", block01.enMerName);
        Assert.assertEquals("POS 厂商测试商户", block01.cnMerName);
        Assert.assertEquals(10000, block01.nonPinLimit);
        Assert.assertEquals(10000, block01.nonSignatureLimit);
        Assert.assertTrue(block01.contactLessStartFlag);
        Assert.assertFalse(block01.binTableStartParam);
        Assert.assertTrue(block01.cdcvmParam);
        Assert.assertFalse(block01.nonSignatureFlag);
    }
}