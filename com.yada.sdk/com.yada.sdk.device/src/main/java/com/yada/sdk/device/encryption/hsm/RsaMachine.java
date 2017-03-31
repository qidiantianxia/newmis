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
        HsmFieldParser parser = new HsmFieldParser()
                .build(new ANField(7))
                .build(new ANField(2))
                .build(new ANField(2))
                .build(new LLBField(4))
                .build(new LastBField());
        List<IHsmField> fields = parser.parse(rece);
        if (fields.size() < 3) {
            logger.warn("parse fields size < 2. rece data is :{}", Hex.encodeHexString(rece.array()));
            return null;
        } else if (fields.size() == 3) {
            logger.warn("respCode has a error. {}", fields.get(2).sValue());
            return null;
        } else if (fields.size() == 5) {
            byte[] priKey = fields.get(3).value();
            byte[] pubKey = fields.get(4).value();
            return new RsaKey(priKey, pubKey);
        } else {
            logger.warn("parse has a unKnow error. rece data is:{}", Hex.encodeHexString(rece.array()));
            return null;
        }
    }

    @Override
    public byte[] encrypt(byte[] pubKey, byte[] data) {
        ByteBuffer req = new HsmFieldBuilder()
                .appendAN(hsmHead)
                .appendAN("30")
                .appendAN("1")
                .appendAN("99")
                .appendLLB(4, pubKey)
                .appendLLB(4, data)
                .build();
        ByteBuffer rece = hsmClient.send(req);
        List<IHsmField> fields = new HsmFieldParser()
                .build(new ANField(7))
                .build(new ANField(2))
                .build(new ANField(2))
                .build(new LLBField(4))
                .parse(rece);
        if (fields.size() < 3) {
            logger.warn("parse fields size < 2. rece data is :{}", Hex.encodeHexString(rece.array()));
            return null;
        } else if (fields.size() == 3) {
            logger.warn("respCode has a error. {}", fields.get(2).sValue());
            return null;
        } else if (fields.size() == 5) {
            return fields.get(4).value();
        } else {
            logger.warn("parse has a unKnow error. rece data is:{}", Hex.encodeHexString(rece.array()));
            return null;
        }
    }
}
