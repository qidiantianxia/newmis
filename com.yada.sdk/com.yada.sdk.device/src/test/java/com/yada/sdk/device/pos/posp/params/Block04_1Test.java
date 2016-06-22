package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.HexUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Block04_1Test {

    private Block04_1 block;

    @Before
    public void before() {
        String param04 = "9F0605A000000025DF2801019F220104DF050420500101DF060101DF070101DF028160D0F543F03F2517133EF2BA4A1104486758630DCFE3A883C77B4E4844E39A9BD6360D23E6644E1E071F196DDF2E4A68B4A3D93D14268D7240F6A14F0D714C17827D279D192E88931AF7300727AE9DA80A3F0E366AEBA61778171737989E1EE309DF040103DF0314FDD7139EC7E0C33167FD61AD3CADBD68D66E91C5DF25080420100420183851";
        block = new Block04_1(HexUtil.parseHex(param04));
    }

    @Test
    public void test() {
        Assert.assertEquals("A000000025", HexUtil.toHexString(block.rid));
        Assert.assertEquals((byte)0x01, block.publicKeyStoreIndex);
        Assert.assertEquals((byte)0x04, block.publicKeyIndex);
        Assert.assertEquals("20500101", block.expiryDate);
        Assert.assertEquals((byte)0x01, block.hashAlgorithmIndicator);
        Assert.assertEquals((byte)0x01, block.publicKeyAlgorithmIndicator);
        Assert.assertEquals("D0F543F03F2517133EF2BA4A1104486758630DCFE3A883C77B4E4844E39A9BD6360D23E6644E1E071F196DDF2E4A68B4A3D93D14268D7240F6A14F0D714C17827D279D192E88931AF7300727AE9DA80A3F0E366AEBA61778171737989E1EE309", HexUtil.toHexString(block.publicKeyModulus));
        Assert.assertEquals("03", HexUtil.toHexString(block.publicKeyExponent));
        Assert.assertEquals("FDD7139EC7E0C33167FD61AD3CADBD68D66E91C5", HexUtil.toHexString(block.publicKeyHash));
        Assert.assertEquals("0420100420183851", block.paramVersion);
    }

}