package com.yada.sdk.device.encryption.model;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * 变长B类型域
 * Created by fengming on 3/31/17.
 */
public class LLBField implements IHsmField {

    private byte[] head;
    private byte[] body = null;

    public LLBField(int headLength) {
        head = new byte[headLength];
    }

    @Override
    public int length() {
        if (body == null) {
            throw new RuntimeException("must exec setValue before exec length");
        }
        return body.length;
    }

    @Override
    public String sValue() {
        if (body == null) {
            throw new RuntimeException("must exec setValue before exec sValue");
        }
        return Hex.encodeHexString(body);
    }

    @Override
    public byte[] value() {
        if (body == null) {
            throw new RuntimeException("must exec setValue before exec value");
        }
        return this.body;
    }

    @Override
    public String type() {
        return "LLB";
    }

    @Override
    public void setValue(ByteBuffer data) {
        data.get(head);
        int dataLength = Integer.valueOf(new String(head));
        this.body = new byte[dataLength];
        data.get(body);
    }
}
