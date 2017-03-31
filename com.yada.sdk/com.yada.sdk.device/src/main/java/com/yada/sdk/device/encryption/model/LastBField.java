package com.yada.sdk.device.encryption.model;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * 报文最后的非固定长B类型域
 * Created by fengming on 3/31/17.
 */
public class LastBField implements IHsmField {

    private byte[] data = null;

    @Override
    public int length() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec length");
        }
        return 0;
    }

    @Override
    public String sValue() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec sValue");
        }
        return Hex.encodeHexString(this.data);
    }

    @Override
    public byte[] value() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec value");
        }
        return this.data;
    }

    @Override
    public String type() {
        return "LastB";
    }

    @Override
    public void setValue(ByteBuffer data) {
        int length = data.remaining();
        this.data = new byte[length];
        data.get(this.data);
    }
}
