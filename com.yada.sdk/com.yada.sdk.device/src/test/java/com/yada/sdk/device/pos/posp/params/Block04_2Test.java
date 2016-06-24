package com.yada.sdk.device.pos.posp.params;

import com.payneteasy.tlv.HexUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Block04_2Test {

    private Block04_2 block;

    @Before
    public void before() {
        String param04 = "DF350131DF3604010100009F0605A0000003339F220118DF37021230DF070104DF380111DF024037710FEB7CC3617767874E85509C268E8F931D68773E93A89F39A4247DFE2D280FC5BC838353885B6DAD447C8F90116BD9D314047591989F67F319544D42A48BDF3940DBDA8F27CF839323920DF931DC51943AE26A376F7EB2529F7233230B63CE15ABA76C1E508DA4A98675A2B522D2722BDF5CD93953973F241F568B282BB66CECDDDF280107DF25080420150428091009";
        block = new Block04_2(HexUtil.parseHex(param04));
    }

    @Test
    public void test() {
        Assert.assertEquals("01010000", HexUtil.toHexString(block.serviceId));
        Assert.assertEquals("A000000333", HexUtil.toHexString(block.rid));
        Assert.assertEquals((byte) 0x07, block.publicKeyStoreIndex);
        Assert.assertEquals((byte) 0x18, block.publicKeyIndex);
        Assert.assertEquals("1230", block.expiryDate);
        Assert.assertEquals((byte) 0x04, block.hashAlgorithmIndicator);
        Assert.assertEquals((byte) 0x11, block.publicKeyAlgorithmIndicator);
        Assert.assertEquals("37710FEB7CC3617767874E85509C268E8F931D68773E93A89F39A4247DFE2D280FC5BC838353885B6DAD447C8F90116BD9D314047591989F67F319544D42A48B", HexUtil.toHexString(block.publicKeyModulus));
        Assert.assertEquals("DBDA8F27CF839323920DF931DC51943AE26A376F7EB2529F7233230B63CE15ABA76C1E508DA4A98675A2B522D2722BDF5CD93953973F241F568B282BB66CECDD", HexUtil.toHexString(block.publicKeyHash));
        Assert.assertEquals("0420150428091009", block.paramVersion);
    }

}