package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.HexUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Block02Test {
    private Block02 block;

    @Before
    public void before() {
        block = new Block02(HexUtil.parseHex("DF298184202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020080101000000200801010000003200100020202020202020202020202020202020202020202020202020202020202020202020202020202020DF25080220080101000000"));
    }

    @Test
    public void test() {
        Assert.assertEquals("0220080101000000", block.paramVersion);
        Assert.assertEquals("", block.tmsfpuDefaultPhone);
        Assert.assertEquals("", block.tmsfpuReservePhone);
        Assert.assertEquals("", block.tmsfpuNetworkAddress);
        Assert.assertEquals("", block.TmsfpuReserveNetworkAddress);
        Assert.assertEquals("20080101000000", block.termDownloadDate);
        Assert.assertEquals("20080101000000", block.termDownloadLimitDate);
        Assert.assertEquals("2", block.communication);
        Assert.assertEquals(10, block.downloadIdleTime);
        Assert.assertEquals(0, block.tmsFlag);
        Assert.assertEquals("", block.username);
        Assert.assertEquals("", block.password);
    }
}