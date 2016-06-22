package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.HexUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Block03Test {

    private Block03 block;

    @Before
    public void before() {
        String param03 = "9F0607A0000000031010DF0101019F090214E9DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160100DF170100DF180103DF14029F379F3501229F15028398DF250803201206291652479F7B06000000000000DF4006000000000000DF2006000000000000DF2106000000000000DF41050000000000DF42050000000000DF43050000000000";
        block = new Block03(HexUtil.parseHex(param03));
    }

    @Test
    public void test() {
        Assert.assertEquals("A0000000031010", HexUtil.toHexString(block.aid));
        Assert.assertEquals("01", HexUtil.toHexString(new byte[]{block.asi}));
        Assert.assertEquals("14E9", HexUtil.toHexString(block.appVersion));
        Assert.assertEquals("D84000A800", HexUtil.toHexString(block.tacDefault));
        Assert.assertEquals("D84004F800", HexUtil.toHexString(block.tacOnLine));
        Assert.assertEquals("0010000000", HexUtil.toHexString(block.tacReject));
        Assert.assertEquals(0, block.terminalFloorLimit);
        Assert.assertEquals("00000000", HexUtil.toHexString(block.biasRandomlySelectedThreshold));
        Assert.assertEquals(0, block.biasRandomlySelectedMaxPercentage);
        Assert.assertEquals(0, block.randomlySelectedPercentage);
        Assert.assertEquals("03", HexUtil.toHexString(new byte[]{block.appFlag}));
        Assert.assertEquals("9F37", HexUtil.toHexString(block.ddol));
        Assert.assertEquals(22, block.terminalCategoryCode);
        Assert.assertEquals(8398, block.merchantCategoryCode);
        Assert.assertEquals("0320120629165247", block.paramVersion);
        Assert.assertEquals(0, block.ecTerminalLimit);
        Assert.assertEquals(0, block.contactLessOfflineLimit);
        Assert.assertEquals(0, block.contactLessLimit);
        Assert.assertEquals(0, block.contactLessCVMLimit);
        Assert.assertEquals("0000000000", HexUtil.toHexString(block.contactLessTacDefault));
        Assert.assertEquals("0000000000", HexUtil.toHexString(block.contactLessTacOnline));
        Assert.assertEquals("0000000000", HexUtil.toHexString(block.contactLessTacReject));
    }

}