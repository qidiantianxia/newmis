package com.yada.sdk.device.encryption.hsm;

import com.yada.sdk.device.encryption.IRsaEncryption;
import com.yada.sdk.device.encryption.model.*;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * rsa加密机实现
 * Created by fengming on 3/31/17.
 */
public class RsaMachine implements IRsaEncryption {
    private final Logger logger = LoggerFactory.getLogger(RsaMachine.class);

    private final String hsmHead = "-------";
    private final HsmClient hsmClient;

    public RsaMachine(HsmClient hsmClient) {
        this.hsmClient = hsmClient;
    }

    @Override
    public RsaKey genKey() {
        ByteBuffer req = new HsmFieldBuilder()
                .appendAN(hsmHead)
                .appendAN("34")
                .appendAN("2048")
                .appendAN("99")
                .build();
        ByteBuffer rece = hsmClient.send(req);
        if (rece == null) {
            return null;
        }
        List<IHsmField> fields = new HsmFieldParser()
                .appendAN(7)
                .appendAN(2)
                .appendAN(2)
                .appendLLB(4)
                .appendLastB()
                .parse(rece);
        if (fields == null) {
            return null;
        } else {
            byte[] priKey = fields.get(3).value();
            byte[] pubKey = fields.get(4).value();
            return new RsaKey(priKey, pubKey);
        }
    }

    @Override
    public byte[] encrypt(byte[] pubKey, byte[] data) {
        ByteBuffer req = new HsmFieldBuilder()
                .appendAN(hsmHead)
                .appendAN("30")
                .appendAN("1")
                .appendAN("99")
                .appendLLB(4, data)
                .appendLastB(pubKey)
                .build();
        ByteBuffer rece = hsmClient.send(req);
        List<IHsmField> fields = new HsmFieldParser()
                .appendAN(7)
                .appendAN(2)
                .appendAN(2)
                .appendLLB(4)
                .parse(rece);
        if (fields == null) {
            return null;
        } else {
            return fields.get(3).value();
        }
    }

    @Override
    public byte[] decrypt(byte[] priKey, byte[] data) {
        ByteBuffer req = new HsmFieldBuilder()
                .appendAN(hsmHead)
                .appendAN("33")
                .appendAN("1")
                .appendAN("99")
                .appendLLB(4, priKey)
                .appendLLB(4, data)
//                .appendLastB(priKey)
                .build();
        ByteBuffer rece = hsmClient.send(req);
        List<IHsmField> fields = new HsmFieldParser()
                .appendAN(7)
                .appendAN(2)
                .appendAN(2)
                .appendLLB(4)
                .parse(rece);
        if (fields == null) {
            return null;
        } else {
            return fields.get(3).value();
        }
    }
}
