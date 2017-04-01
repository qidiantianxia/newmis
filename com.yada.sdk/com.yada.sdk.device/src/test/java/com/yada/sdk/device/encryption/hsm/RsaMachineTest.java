package com.yada.sdk.device.encryption.hsm;

import junit.framework.TestCase;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.mockito.Mockito;

import java.nio.ByteBuffer;

/**
 * 加密机RSA指令测试
 * Created by fengming on 17-4-1.
 */
public class RsaMachineTest extends TestCase {

    public void testEncrypt() throws DecoderException {
        HsmClient hsmClient = Mockito.mock(HsmClient.class);
        Mockito.when(hsmClient.send(Mockito.any(ByteBuffer.class))).thenReturn(ByteBuffer.wrap(Hex.decodeHex("2d2d2d2d2d2d2d3331303030323536876fd95b38beeae4f02e1b8f43f76d4c974ace33853b0cf1e912ed625add5c988f571fedaf193b26b5e0b7d8c7d44997c78ba2e91490c9c341d7152d370af7420e7fc640a7a7502277d528cbb5e7c2fc046bd617be66042a8f2909a13c083a4fe57f2e2f5dc85b8ee4ebd6a7bf2c4d58f54355f8891026e74258d2531f9c567c37cc962defb77cd950ef9f18bade58183bc854dbe78d036c221a0487388ab8cc573f67f7a8b96ea30c6b95394d2e3d4228d6f078e6e3d73a1b395ab4bec798b34a34fa09bb8c9a3f561a5b5b8f814f98f6140cc20fd4ef1a195cbcf710ee075f24c3dbc0999544da222458fc71a350c04abf82998e3981d47b0f8bf1bb0c92a3".toCharArray())));
        byte[] pubKey = Hex.decodeHex("3082010a0282010100b79365003fd7a8365c8d944707f24776bbd427ad69568e75217b11647e86361a3df117239877d3a8e2831b6b2dc6e85004d35a5b37d1170d81cdf125b2ca832243c46fc866c52427d3c669bb0b8b239a686aed87b7fbd3b6dcb6eecfef057ad83e76a2117b4b4d0a763a96dcd56e48d95e6aa4b351d94ed2cb271c377481d731c13043da2e6816048cff9745456331e614d653c0abd3265ec885125e3c587fca8d80d17eb264f1cd74f362ee0c59e6617af0ec3221f446b49eda4a5b8b144a06f6f44e540c5f3636edcd6dc5e10400d6af43c19dadf61d68df11b527a8618a3ba1db801284b5a7ae52670e0cb5a51fd585f8b0f20b93335636ff3a25c30bc42b0203010001".toCharArray());
        byte[] data = "123456".getBytes();
        RsaMachine rsaMachine = new RsaMachine(hsmClient);
        byte[] eData = rsaMachine.encrypt(pubKey, data);
        Assert.assertEquals(Hex.encodeHexString(eData), "876fd95b38beeae4f02e1b8f43f76d4c974ace33853b0cf1e912ed625add5c988f571fedaf193b26b5e0b7d8c7d44997c78ba2e91490c9c341d7152d370af7420e7fc640a7a7502277d528cbb5e7c2fc046bd617be66042a8f2909a13c083a4fe57f2e2f5dc85b8ee4ebd6a7bf2c4d58f54355f8891026e74258d2531f9c567c37cc962defb77cd950ef9f18bade58183bc854dbe78d036c221a0487388ab8cc573f67f7a8b96ea30c6b95394d2e3d4228d6f078e6e3d73a1b395ab4bec798b34a34fa09bb8c9a3f561a5b5b8f814f98f6140cc20fd4ef1a195cbcf710ee075f24c3dbc0999544da222458fc71a350c04abf82998e3981d47b0f8bf1bb0c92a3");

    }
}
