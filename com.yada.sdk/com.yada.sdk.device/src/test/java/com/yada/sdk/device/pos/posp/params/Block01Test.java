package com.yada.sdk.device.pos.posp.params;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Block01Test {

    Block01 block01;

    @Before
    public void before() {
        String param01 = "ff7fffFF002000000010000000500050156156156    1554                1554            01IP01IP02IP03IP04IP05IP06IP07IP08IP0950TEST MERCHANT            测试商户                 ";
        block01 = new Block01(param01);
    }

    @Test
    public void useMAC() throws Exception {
        Assert.assertTrue(block01.useMAC());
    }

    @Test
    public void enable() throws Exception {
        Assert.assertTrue(block01.enable());
    }

    @Test
    public void autoCheck() throws Exception {
        Assert.assertTrue(block01.autoCheck());
    }

    @Test
    public void allowBatchUpdate() throws Exception {
        Assert.assertTrue(block01.allowBatchUpdate());
    }

    @Test
    public void allowReturn() throws Exception {
        Assert.assertTrue(block01.allowReturn());
    }

    @Test
    public void allowManuallyEnterCardNo() throws Exception {
        Assert.assertTrue(block01.allowManuallyEnterCardNo());
    }

    @Test
    public void allowMOTO() throws Exception {
        Assert.assertTrue(block01.allowMOTO());
    }

    @Test
    public void allOffline() throws Exception {
        Assert.assertTrue(block01.allOffline());
    }

    @Test
    public void useTripleDES() throws Exception {
        Assert.assertFalse(block01.useTripleDES());
    }

    @Test
    public void allowBalanceQuery() throws Exception {
        Assert.assertTrue(block01.allowBalanceQuery());
    }

    @Test
    public void allowPay() throws Exception {
        Assert.assertTrue(block01.allowPay());
    }

    @Test
    public void allowPreAuth() throws Exception {
        Assert.assertTrue(block01.allowPreAuth());
    }

    @Test
    public void allowWithdrawals() throws Exception {
        Assert.assertTrue(block01.allowWithdrawals());
    }

    @Test
    public void allowAdjust() throws Exception {
        Assert.assertTrue(block01.allowAdjust());
    }

    @Test
    public void allowTip() throws Exception {
        Assert.assertTrue(block01.allowTip());
    }

    @Test
    public void allowInstallment() throws Exception {
        Assert.assertTrue(block01.allowInstallment());
    }

    @Test
    public void allowIntegral() throws Exception {
        Assert.assertTrue(block01.allowIntegral());
    }

    @Test
    public void allowPaymentTransfer() throws Exception {
        Assert.assertTrue(block01.allowPaymentTransfer());
    }

    @Test
    public void allowCollections() throws Exception {
        Assert.assertTrue(block01.allowCollections());
    }

    @Test
    public void useOper() throws Exception {
        Assert.assertTrue(block01.useOper());
    }

    @Test
    public void allowCNChar() throws Exception {
        Assert.assertTrue(block01.allowCNChar());
    }

    @Test
    public void supportEMV() throws Exception {
        Assert.assertTrue(block01.supportEMV());
    }

    @Test
    public void allowCardholderSelectApp() throws Exception {
        Assert.assertTrue(block01.allowCardholderSelectApp());
    }

    @Test
    public void allowFallback() throws Exception {
        Assert.assertTrue(block01.allowFallback());
    }

    @Test
    public void supportAppDownload() throws Exception {
        Assert.assertTrue(block01.supportAppDownload());
    }

    @Test
    public void supportElectronicCash() throws Exception {
        Assert.assertTrue(block01.supportElectronicCash());
    }

    @Test
    public void allowAssignLoad() throws Exception {
        Assert.assertTrue(block01.allowAssignLoad());
    }

    @Test
    public void allowUnspecifiedLoad() throws Exception {
        Assert.assertTrue(block01.allowUnspecifiedLoad());
    }

    @Test
    public void allowFillBoardLoad() throws Exception {
        Assert.assertTrue(block01.allowFillBoardLoad());
    }

    @Test
    public void checkoutMethod() throws Exception {
        Assert.assertEquals(0, block01.checkoutMethod());
    }

    @Test
    public void maxOfflineCount() throws Exception {
        Assert.assertEquals(2, block01.maxOfflineCount());
    }

    @Test
    public void maxOfflineAmt() throws Exception {
        Assert.assertEquals(100000, block01.maxOfflineAmt());
    }

    @Test
    public void maxAdjustAmtPercentage() throws Exception {
        Assert.assertEquals(50, block01.maxAdjustAmtPercentage());
    }

    @Test
    public void maxTip() throws Exception {
        Assert.assertEquals(50, block01.maxTip());
    }

    @Test
    public void currency1() throws Exception {
        Assert.assertEquals("156", block01.currency1());
    }

    @Test
    public void currency2() throws Exception {
        Assert.assertEquals("156", block01.currency2());
    }

    @Test
    public void currency3() throws Exception {
        Assert.assertEquals("156", block01.currency3());
    }

    @Test
    public void prefixOfDefaultPhone() throws Exception {
        Assert.assertEquals("", block01.prefixOfDefaultPhone());
    }

    @Test
    public void defaultPhone() throws Exception {
        Assert.assertEquals("1554", block01.defaultPhone());
    }

    @Test
    public void prefixOfSparePhone() throws Exception {
        Assert.assertEquals("", block01.prefixOfSparePhone());
    }

    @Test
    public void sparePhone() throws Exception {
        Assert.assertEquals("1554", block01.sparePhone());
    }

    @Test
    public void billPrintCount() throws Exception {
        Assert.assertEquals(1, block01.billPrintCount());
    }

    @Test
    public void plant1() throws Exception {
        Assert.assertEquals("IP01", block01.plant1());
    }

    @Test
    public void plant2() throws Exception {
        Assert.assertEquals("IP02", block01.plant2());
    }

    @Test
    public void plant3() throws Exception {
        Assert.assertEquals("IP03", block01.plant3());
    }

    @Test
    public void plant4() throws Exception {
        Assert.assertEquals("IP04", block01.plant4());
    }

    @Test
    public void plant5() throws Exception {
        Assert.assertEquals("IP05", block01.plant5());
    }

    @Test
    public void plant6() throws Exception {
        Assert.assertEquals("IP06", block01.plant6());
    }

    @Test
    public void plant7() throws Exception {
        Assert.assertEquals("IP07", block01.plant7());
    }

    @Test
    public void plant8() throws Exception {
        Assert.assertEquals("IP08", block01.plant8());
    }

    @Test
    public void plant9() throws Exception {
        Assert.assertEquals("IP09", block01.plant9());
    }

    @Test
    public void timeout() throws Exception {
        Assert.assertEquals(50, block01.timeout());
    }

    @Test
    public void enMerName() throws Exception {
        Assert.assertEquals("TEST MERCHANT", block01.enMerName());
    }

    @Test
    public void cnMerName() throws Exception {
        Assert.assertEquals("测试商户", block01.cnMerName());
    }
}